package com.raggle.half_dream.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.half_dream.api.DreamHorse;
import com.raggle.half_dream.common.FaeUtil;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {

	@Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
	public void setTarget(@Nullable LivingEntity target, CallbackInfo ci) {
		if(target != null) {
			//cancels attack if 
			if(!FaeUtil.canInteract(target, (MobEntity)(Object)this)) {
				ci.cancel();
			}
		}
	}
	@Inject(method = "interact", at = @At("HEAD"), cancellable = true)
	public final void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		if(!FaeUtil.canInteract(player, (MobEntity)(Object)this) && !(this instanceof DreamHorse))
			cir.setReturnValue(ActionResult.FAIL);
	}

}
