package com.raggle.half_dream.mixin.client;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.half_dream.client.FaeUtilClient;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.LightType;

@ClientOnly
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	
	@Inject(method = "hasSkyBlockingEffect", at = @At("HEAD"), cancellable = true)
	private void hasSkyBlockingEffect(Camera camera, CallbackInfoReturnable<Boolean> cir) {
		if(FaeUtilClient.getPlayerDream() == 1) {
			cir.setReturnValue(true);
		}
	}
	
	@Inject(method = "getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I", at = @At("RETURN"), cancellable = true)
	private static void getLightmapCoordinates(BlockRenderView world, BlockState state, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		if(FaeUtilClient.getPlayerDream() == 1) {
			int blockLight = world.getLightLevel(LightType.BLOCK, pos);
			if(FaeUtilClient.isDreamAir(pos)) {
				blockLight = 4;
			}

			cir.setReturnValue(0 << 20 | blockLight << 4);
		}
	}
}
