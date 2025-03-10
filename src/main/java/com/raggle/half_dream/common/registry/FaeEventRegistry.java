package com.raggle.half_dream.common.registry;

import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.entity.event.api.ServerPlayerEntityCopyCallback;

import com.raggle.half_dream.api.DreamServerPlayer;
import com.raggle.half_dream.common.FaeUtil;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FaeEventRegistry {
	
    
	
	public static void init() {
		ServerPlayerEntityCopyCallback.EVENT.register(FaeEventRegistry::afterRespawn);
		PlayerBlockBreakEvents.BEFORE.register(FaeEventRegistry::beforeBlockBreak);
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("dreamstate").executes(context -> {
			if(context.getSource().getEntity() instanceof DreamServerPlayer dsp) {
				dsp.setDream(!dsp.isDream());
			}
	     
			return 1;
		})));
	}
	
	private static void afterRespawn(ServerPlayerEntity copy, ServerPlayerEntity original, boolean wasDeath) {
		FaeUtil.setDream(copy, FaeUtil.getDream(original));
	}
	private static boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity){
		
		if(FaeUtil.isDream(player)) {
			if(FaeUtil.isDreamBlock(pos, world)) {
				FaeUtil.setDreamBlock(pos, false, world);
			}
			else {
				FaeUtil.setDreamAir(pos, true, world);
				world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(FaeItemRegistry.DREAM_RESIN)));
				return false;
			}
		}
		else {
			FaeUtil.setDreamAir(pos, false, world);
		}
		return true;
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