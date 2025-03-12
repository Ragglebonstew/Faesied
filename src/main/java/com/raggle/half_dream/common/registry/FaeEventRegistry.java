package com.raggle.half_dream.common.registry;

import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.entity.event.api.ServerPlayerEntityCopyCallback;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.raggle.half_dream.api.DreamChunkComponent;
import com.raggle.half_dream.common.FaeUtil;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class FaeEventRegistry {
	
    
	
	public static void init() {
		ServerPlayerEntityCopyCallback.EVENT.register(FaeEventRegistry::afterRespawn);
		PlayerBlockBreakEvents.BEFORE.register(FaeEventRegistry::beforeBlockBreak);
		UseBlockCallback.EVENT.register(FaeEventRegistry::onBlockUse);
		
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
				CommandManager
				.literal("dreamstate")
				.requires(source -> source.hasPermissionLevel(2))
				.then(CommandManager.argument("value", IntegerArgumentType.integer())
						.executes(context -> 
				{
					byte dream = (byte)IntegerArgumentType.getInteger(context, "value");
					if(dream >= 0 && dream <= 2) {
						FaeUtil.setDream(context.getSource().getPlayer(), dream);
						context.getSource().sendFeedback(() -> Text.literal("Set dream to %s".formatted(dream)), false);
					}
					else {
						context.getSource().sendFeedback(() -> Text.literal("Value must be between 0 and 2"), false);
					}
					return 1;
		}))));
		
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
				CommandManager
				.literal("dreamclear")
				.requires(source -> source.hasPermissionLevel(2))
				.executes(context -> 
				{
					context.getSource().getServer();
					Chunk chunk = context.getSource().getPlayer().getWorld().getChunk(context.getSource().getPlayer().getBlockPos());
					int air_count = FaeComponentRegistry.DREAM_AIR.get(chunk).clear();
					int block_count = FaeComponentRegistry.DREAM_BLOCKS.get(chunk).clear();
					
					context.getSource().sendFeedback(() -> Text.literal("Deleted "+air_count+" dream air and "+block_count+" dream blocks"), false);
					return 1;
		})));
	}
	
	private static void afterRespawn(ServerPlayerEntity copy, ServerPlayerEntity original, boolean wasDeath) {
		FaeUtil.setDream(copy, FaeUtil.getDream(original));
	}
	private static boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity){
		
		byte player_dream = FaeUtil.getDream(player);
		
		if(player_dream == 1) {
			if(FaeUtil.isDreamBlock(pos, world)) {
				FaeUtil.setDreamBlock(pos, false, world);
				return true;
			}
			else {
				FaeUtil.setDreamAir(pos, true, world);
				ItemEntity dream_resin = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(FaeItemRegistry.DREAM_RESIN));
				world.spawnEntity(dream_resin);
				FaeUtil.setDream(dream_resin, (byte)1);
				return false;
			}
		}
		else {
			FaeUtil.setDreamAir(pos, false, world);
		}
		return true;
	}
	
	private static ActionResult onBlockUse(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {

		BlockPos pos = hitResult.getBlockPos().offset(hitResult.getSide());
		Iterable<ItemStack> stacks = player.getItemsHand();
		
		if(FaeUtil.getDream(player) == 1) {
			for(ItemStack itemStack : stacks) {
				if(Item.BLOCK_ITEMS.containsValue(itemStack.getItem())) {
					FaeUtil.setDreamBlock(pos, true, world);
					break;
				}
			}
		}
		
		return ActionResult.PASS;
	}
	/*static BlockPos lastPlayerPos;
	private static void clientTick(MinecraftClient mc) {
		if(!mc.isPaused() && mc.player instanceof DreamClientPlayer dcp && dcp.isDream()) {
			if(mc != null && mc.world != null) {
				
				if(lastPlayerPos == mc.player.getBlockPos()) {
					return;
				}
				lastPlayerPos = mc.player.getBlockPos();
				
				//maximum light level emitted by player
				int l = 7;
				
				int x = mc.player.getBlockX();
				int y = mc.player.getBlockY();
				int z = mc.player.getBlockZ();
				boolean[] direction = new boolean[3];
				if(x >> 4 == x+l >> 4) {
					direction[0] = true;
				}
				if(y >> 4 == y+l >> 4) {
					direction[1] = true;
				}
				if(z >> 4 == z+l >> 4) {
					direction[2] = true;
				}
				
				int x1 = x >> 4;
				int x2 = x + (direction[0] ? -l : l) >> 4;
				int y1 = x >> 4;
				int y2 = x + (direction[1] ? -l : l) >> 4;
				int z1 = x >> 4;
				int z2 = x + (direction[2] ? -l : l) >> 4;

				FaeUtil.scheduleChunkRenderAt(x1, y1, z1);
				FaeUtil.scheduleChunkRenderAt(x2, y1, z1);
				FaeUtil.scheduleChunkRenderAt(x1, y2, z1);
				FaeUtil.scheduleChunkRenderAt(x2, y2, z1);
				FaeUtil.scheduleChunkRenderAt(x1, y1, z2);
				FaeUtil.scheduleChunkRenderAt(x2, y1, z2);
				FaeUtil.scheduleChunkRenderAt(x1, y2, z2);
				FaeUtil.scheduleChunkRenderAt(x2, y2, z2);

			}
		}
	}*/
}