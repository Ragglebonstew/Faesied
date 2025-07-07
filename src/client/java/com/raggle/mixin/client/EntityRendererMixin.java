package com.raggle.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.half_dream.FaeUtilClient;
import com.raggle.half_dream.common.FaeUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

	@Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
	private void shouldRender(T entity, Frustum frustum, double d, double e, double f, CallbackInfoReturnable<Boolean> cir) {
		if(!FaeUtil.canInteract(entity, FaeUtilClient.getClientPlayer())) {
			cir.setReturnValue(false);
		}
	}
	
	@Inject(method = "getSkyLight", at = @At("HEAD"), cancellable = true)
	private void getSkyLight(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		if(FaeUtilClient.getPlayerDream() == 1)
			cir.setReturnValue(0);
	}

	@Inject(method = "getBlockLight", at = @At("HEAD"), cancellable = true)
	private void getBlockLight(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		if(FaeUtilClient.getPlayerDream() == 1 && FaeUtilClient.isDreamAir(pos))
			cir.setReturnValue(4);
	}
}
