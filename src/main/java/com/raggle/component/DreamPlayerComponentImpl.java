package com.raggle.component;

import com.raggle.api.DreamPlayerComponent;
import com.raggle.registry.FaeComponentRegistry;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class DreamPlayerComponentImpl implements DreamPlayerComponent, AutoSyncedComponent{

	private final PlayerEntity player;
	
	private boolean isInterloped;
	
	public DreamPlayerComponentImpl(PlayerEntity entity) {
		this.player = entity;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.isInterloped = tag.getBoolean("interloped");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("interloped", this.isInterloped);
	}

	@Override
	public boolean isInterloped() {
		return this.isInterloped;
	}

	@Override
	public void setInterlope(boolean b) {
		this.isInterloped = b;
		FaeComponentRegistry.DREAM_PLAYER.sync(this.player);
	}

}
