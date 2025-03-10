package com.raggle.faesied.mixin;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.faesied.api.DreamEntityComponent;
import com.raggle.faesied.api.DreamHorse;
import com.raggle.faesied.common.FaeUtil;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

@ClientOnly
@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

	@Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
	private void shouldRender(T entity, Frustum frustum, double d, double e, double f, CallbackInfoReturnable<Boolean> cir) {
		if(entity instanceof DreamEntityComponent de && !(entity instanceof DreamHorse)) {
			if(de.isDream() != FaeUtil.isPlayerDream()) {
				cir.setReturnValue(false);
			}
		}
	}
	
	@Inject(method = "getSkyLight", at = @At("HEAD"), cancellable = true)
	private void getSkyLight(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		if(FaeUtil.isPlayerDream())
			cir.setReturnValue(0);
	}

	@Inject(method = "getBlockLight", at = @At("HEAD"), cancellable = true)
	private void getBlockLight(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		if(FaeUtil.isPlayerDream() && FaeUtil.isDreamless(pos))
			cir.setReturnValue(4);
	}
}
