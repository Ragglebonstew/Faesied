package com.raggle.mixin.client;

import net.minecraft.client.render.*;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.FaeUtilClient;
import com.raggle.util.DreamState;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.LightType;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

	@Shadow
	@Nullable
	private ClientWorld world;

	@Inject(method = "hasBlindnessOrDarkness", at = @At("HEAD"), cancellable = true)
	private void hasBlindnessOrDarkness(Camera camera, CallbackInfoReturnable<Boolean> cir) {
		if(FaeUtilClient.getPlayerDream() == DreamState.ASLEEP) {
			cir.setReturnValue(true);
		}
	}
	
	@Inject(method = "getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I", at = @At("RETURN"), cancellable = true)
	private static void getLightmapCoordinates(BlockRenderView world, BlockState state, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		if(FaeUtilClient.getPlayerDream() == DreamState.ASLEEP) {
			int blockLight = world.getLightLevel(LightType.BLOCK, pos);
			blockLight = Math.max(blockLight/2, 0);
			if(FaeUtilClient.isDreamAir(pos)) {
				blockLight = 4;
			}

			cir.setReturnValue(0 << 20 | blockLight << 4);
		}
	}
	/*
	@Inject(method = "render", at = @At("TAIL"))
	public void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {
		// Render block selection outlines
		BlockPos pos = camera.getBlockPos();
		DreamChunkComponent chunkAir = FaeUtil.getDreamChunkComponent(pos, FaeUtilClient.getClientWorld(), FaeComponentRegistry.DREAM_AIR);

        assert chunkAir != null;
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		RenderSystem.enableBlend();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

		Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        for(DreamArea area: chunkAir.getAreaList()){
			bufferBuilder.vertex(matrix4f, area.x1, area.y1, area.z1).color(1.0F, 0.0F, 0.0F, 1.0F).next();
			bufferBuilder.vertex(matrix4f, area.x1, area.y2, area.z1).color(1.0F, 0.0F, 0.0F, 1.0F).next();
			bufferBuilder.vertex(matrix4f, area.x1, area.y2, area.z2).color(1.0F, 0.0F, 0.0F, 1.0F).next();
			bufferBuilder.vertex(matrix4f, area.x2, area.y2, area.z2).color(1.0F, 0.0F, 0.0F, 1.0F).next();
			bufferBuilder.vertex(matrix4f, area.x2, area.y1, area.z2).color(1.0F, 0.0F, 0.0F, 1.0F).next();
			bufferBuilder.vertex(matrix4f, area.x2, area.y1, area.z1).color(1.0F, 0.0F, 0.0F, 1.0F).next();
			bufferBuilder.vertex(matrix4f, area.x1, area.y1, area.z1).color(1.0F, 0.0F, 0.0F, 1.0F).next();
		}

		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
		RenderSystem.disableBlend();
	}
	*/
}
