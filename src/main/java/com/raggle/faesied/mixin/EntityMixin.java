package com.raggle.faesied.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.faesied.api.DreamEntityComponent;
import com.raggle.faesied.api.DreamHorse;
import com.raggle.faesied.common.registry.FaeComponentRegistry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

@Mixin(Entity.class)
public abstract class EntityMixin implements DreamEntityComponent{
	
	private NbtCompound dreamNbt;
	
	@Override
	public boolean isDream() {
		return getPersistantData().getBoolean(FaeComponentRegistry.DREAM_KEY);
	}

	@Override
	public void setDream(boolean b) {
		getPersistantData().putBoolean(FaeComponentRegistry.DREAM_KEY, b);
		getPersistantData().putByte(FaeComponentRegistry.DREAMSTATE_KEY, b ? (byte)1 : 0);
	}
	@Override
	public byte getDream() {
		return getPersistantData().getByte(FaeComponentRegistry.DREAMSTATE_KEY);
	}
	@Override
	public void setDream(byte b) {
		getPersistantData().putByte(FaeComponentRegistry.DREAMSTATE_KEY, b);
	}
	@Override
	public NbtCompound getPersistantData() {
		if (dreamNbt == null) {
			dreamNbt = new NbtCompound();
		}
		
		return dreamNbt;
	}
	
	@Shadow public abstract World getWorld();
	
	@Inject(method = "writeNbt", at = @At("HEAD"))
	public void writeNbt(NbtCompound tag, CallbackInfoReturnable<NbtCompound> ci) {
		if(dreamNbt != null) {
			tag.put("faesied", dreamNbt);
		}
	}
	@Inject(method = "readNbt", at = @At("HEAD"))
	public void readNbt(NbtCompound tag, CallbackInfo ci) {
		if(tag.contains("faesied", 10)) {
			dreamNbt = tag.getCompound("faesied");
		}
	}

	@Inject(method = "isInsideWall", at = @At("HEAD"), cancellable = true)
	private void isInsideWall(CallbackInfoReturnable<Boolean> cir) {
		if(this.isDream()) {
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
