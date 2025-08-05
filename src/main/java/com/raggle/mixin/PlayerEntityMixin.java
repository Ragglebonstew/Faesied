package com.raggle.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.FaeUtil;
import com.raggle.registry.FaeItemRegistry;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

	@Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("RETURN"), cancellable = true)
	public void dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
		ItemEntity item = cir.getReturnValue();
		if(item != null) {
			FaeUtil.setDream(item, FaeUtil.getDreamState((PlayerEntity)(Object)this));
		}
	}


	@Inject(method = "canResetTimeBySleeping", at = @At("HEAD"), cancellable = true)
	public void canResetTimeBySleeping(CallbackInfoReturnable<Boolean> ci) {
		PlayerEntity player = (PlayerEntity)(Object)this;
		if(player.isHolding(FaeItemRegistry.BUNNY_PLUSH) && !FaeUtil.isInterloped(player)) {
			ci.setReturnValue(false);
		}
	}
}
