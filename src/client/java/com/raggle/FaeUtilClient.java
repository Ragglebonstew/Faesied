package com.raggle;

import org.jetbrains.annotations.Nullable;

import com.raggle.util.DreamState;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class FaeUtilClient {
	

	public static boolean isDreamAir(BlockPos pos) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc != null && mc.world != null) {
			return FaeUtil.isDreamAir(pos, mc.world);
		}
		return false;
	}

	public static boolean isDreamBlock(BlockPos pos) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc != null && mc.world != null) {
			return FaeUtil.isDreamBlock(pos, mc.world);
		}
		return false;
	}
	
	@Nullable
	public static ClientPlayerEntity getClientPlayer() {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc == null)
			return null;
		return mc.player;
	}
	@Nullable
	public static ClientWorld getClientWorld() {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc == null)
			return null;
		return mc.world;
	}
	public static DreamState getPlayerDream() {
		ClientPlayerEntity player = getClientPlayer();
		if(player == null)
			return DreamState.AWAKE;
		return FaeUtil.getDreamState(getClientPlayer());
	}
	public static boolean canPlayerInteract(BlockPos pos) {
		return FaeUtil.canInteract(getClientPlayer(), pos, getClientWorld());
	}
	public static void scheduleChunkRenderAt(BlockPos pos) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc != null && mc.world != null) {
			mc.worldRenderer.updateBlock(mc.world, pos, null, null, 0);
		}
	}

	public static boolean isInterloped() {
		return FaeUtil.isInterloped(getClientPlayer());
	}
}
