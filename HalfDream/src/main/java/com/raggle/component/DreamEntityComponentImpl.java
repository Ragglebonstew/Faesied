package com.raggle.component;

import com.raggle.api.DreamEntityComponent;
import com.raggle.registry.FaeComponentRegistry;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class DreamEntityComponentImpl implements DreamEntityComponent, AutoSyncedComponent {
	
	private final Entity entity;
	
	private byte dream;

	private boolean shouldReloadWorldRenderer;
	
	public DreamEntityComponentImpl(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		NbtCompound tag = buf.readNbt();
        if (tag != null) {
        	if(tag.getByte("dream") != this.dream) {
                this.readFromNbt(tag);
                this.shouldReloadWorldRenderer = true;
        	}
        }
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.dream = tag.getByte("dream");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putByte("dream", this.dream);
	}
	@Override
	public byte getDream() {
		return this.dream;
	}
	@Override
	public void setDream(byte b) {
		this.dream = b;
		FaeComponentRegistry.DREAM_ENTITY.sync(this.entity);
	}

	@Override
	public boolean shouldUpdateClient() {
		if(this.shouldReloadWorldRenderer) {
			this.shouldReloadWorldRenderer = false;
			return true;
		}
		return false;
	}

}