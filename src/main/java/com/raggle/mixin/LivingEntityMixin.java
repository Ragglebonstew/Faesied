package com.raggle.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.FaeUtil;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	
	@Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("INVOKE"), cancellable = true)
	public void canTarget(LivingEntity target, CallbackInfoReturnable<Boolean> ci) {
		//Faesied.LOGGER.info("attempting to target: "+target.getEntityName());
		if(!FaeUtil.canInteract(target, (LivingEntity)(Object)this)) {
			ci.setReturnValue(false);
		}
	}
}
