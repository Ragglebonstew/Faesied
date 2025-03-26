package com.raggle.half_dream.client;

import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import com.raggle.half_dream.common.FaeUtil;
import com.raggle.half_dream.mixin.client.WorldRendererAccessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;

@ClientOnly
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
	public static byte getPlayerDream() {
		ClientPlayerEntity player = getClientPlayer();
		if(player == null)
			return 0;
		return FaeUtil.getDream(getClientPlayer());
	}
	public static boolean canPlayerInteract(BlockPos pos) {
		return FaeUtil.canInteract(getClientPlayer(), pos, getClientWorld());
	}
	public static void scheduleChunkRenderAt(BlockPos pos) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc != null && mc.world != null) {
			ChunkSectionPos chunkPos = ChunkSectionPos.from(pos);
			((WorldRendererAccessor)mc.worldRenderer).invokeScheduleChunkRender(chunkPos.getSectionX(), chunkPos.getSectionY(), chunkPos.getSectionZ(), true);;
		}
	}

	public static boolean isInterloped() {
		return FaeUtil.isInterloped(getClientPlayer());
	}
}
