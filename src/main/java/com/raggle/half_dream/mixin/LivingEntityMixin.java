package com.raggle.half_dream.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.half_dream.common.FaeUtil;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	
	@Shadow public abstract boolean hasStatusEffect(StatusEffect effect);
	
	/*@Inject(method = "wakeUp", at = @At("INVOKE"))
	public void wakeup(CallbackInfo ci) {
		if(this instanceof DreamServerPlayer player) {
			player.setDream(!player.isDream());
			player.syncDream();
		}
	}*/
	
	@Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("INVOKE"), cancellable = true)
	public void canTarget(LivingEntity target, CallbackInfoReturnable<Boolean> ci) {
		//Faesied.LOGGER.info("attempting to target: "+target.getEntityName());
		if(!FaeUtil.canInteract(target, (LivingEntity)(Object)this)) {
			ci.setReturnValue(false);
		}
	}
}
