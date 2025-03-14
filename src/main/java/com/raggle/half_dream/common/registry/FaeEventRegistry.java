package com.raggle.half_dream.common.registry;

import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.entity.event.api.ServerPlayerEntityCopyCallback;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.raggle.half_dream.Faesied;
import com.raggle.half_dream.common.FaeUtil;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
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
				dream_resin.setPickupDelay(40);
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

		if(world.isClient()) {
			return ActionResult.PASS;
		}
		//trying to figure out if block was placed on another, or replaced it
		if(FaeUtil.getDream(player) == 1) {
			Iterable<ItemStack> stacks = player.getItemsHand();
			for(ItemStack itemStack : stacks) {
				ItemPlacementContext itemContext = new ItemPlacementContext(world, player, hand, itemStack, hitResult);
				Block block_hand = Block.getBlockFromItem(itemStack.getItem());

				if(block_hand != Blocks.AIR && block_hand != null && itemContext.canPlace()) {
					FaeUtil.setDreamBlock(itemContext.getBlockPos(), true, world);
					break;
				}
			}
		}
		else if(FaeUtil.getDream(player) == 0) {
			Iterable<ItemStack> stacks = player.getItemsHand();
			for(ItemStack itemStack : stacks) {
				ItemPlacementContext itemContext = new ItemPlacementContext(world, null, hand, itemStack, hitResult);
				Block block_hand = Block.getBlockFromItem(itemStack.getItem());

				if(block_hand != Blocks.AIR && block_hand != null && itemContext.canPlace()) {
					if(block_hand == world.getBlockState(itemContext.getBlockPos()).getBlock()) {
						FaeUtil.setDreamBlock(itemContext.getBlockPos(), false, world);
					}
					else
						FaeUtil.addMarked(itemContext.getBlockPos());
					break;
				}
			}
			
			
			
			/*
			Iterable<ItemStack> stacks = player.getItemsHand();
			BlockPos pos = hitResult.getBlockPos();
			BlockPos pos_offset = pos.offset(hitResult.getSide());
			BlockState state = world.getBlockState(pos);
			
			for(ItemStack itemStack : stacks) {
				ItemPlacementContext newContext = new ItemPlacementContext(world, null, hand, itemStack, hitResult);
				if(state.canReplace(newContext)) {
					FaeUtil.setDreamBlock(pos, true, world);
				}
				Block block_hand = Block.getBlockFromItem(itemStack.getItem());
				if(block_hand != Blocks.AIR && block_hand != null) {
					FaeUtil.setDreamBlock(pos_offset, true, world);
					break;
				}
			}
			*/
		}
		
		return ActionResult.PASS;
	}
}