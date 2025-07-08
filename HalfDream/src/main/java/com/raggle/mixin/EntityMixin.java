package com.raggle.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.FaeUtil;
import com.raggle.api.DreamHorse;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(Entity.class)
public abstract class EntityMixin {
	
	@Shadow
	public abstract World getWorld();
	
	@Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
	private void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if(!cir.getReturnValueZ() && damageSource.getSource() != null) {
			Entity e1 = (Entity)(Object)this;
			Entity e2 = damageSource.getSource();
			if(!FaeUtil.canInteract(e1, e2)) {
				cir.setReturnValue(true);
			}
		}
	}
	
	@Inject(method = "isInsideWall", at = @At("HEAD"), cancellable = true)
	private void isInsideWall(CallbackInfoReturnable<Boolean> cir) {
		if(FaeUtil.getDream((Entity)(Object)this) != 0) {
				cir.setReturnValue(false);
		}
	}
	
	@Inject(method = "addPassenger", at = @At("TAIL"))
	protected void addPassenger(Entity passenger, CallbackInfo ci) {
		if ((Object)this instanceof DreamHorse dh && passenger instanceof PlayerEntity player) {
			dh.setPlayer(player);
		}
	}

	@Inject(method = "removePassenger", at = @At("TAIL"))
	protected void removePassenger(Entity passenger, CallbackInfo ci) {
		if ( (Object)this instanceof DreamHorse dh && passenger instanceof PlayerEntity player) {
			dh.setPlayer(null);
		}
	}
}
