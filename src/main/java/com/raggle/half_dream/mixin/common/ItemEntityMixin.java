package com.raggle.half_dream.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.raggle.half_dream.common.FaeUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
	
	@Shadow public abstract ItemStack getStack();

	@Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
	private void onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
		if(!FaeUtil.canInteract(player, (ItemEntity)(Object)this)) {
			ci.cancel();
		}
	}
	/*@Override
	public boolean isDream() {
		return getStack().isIn(FaeTagRegistry.DREAMING_ITEMS);
	}*/
}
