package com.raggle.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import com.raggle.api.DreamClientPlayer;

import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin implements DreamClientPlayer {

	public int listSize;
	
	@Override
	public void setListSize(int i) {
		listSize = i;
	}
	@Override
	public int getListSize() {
		return listSize;
	}
	@Override
	public void incrementList() {
		listSize++;
	}
	@Override
	public void decrementList() {
		listSize--;
	}

}
