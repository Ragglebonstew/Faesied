package com.raggle.component;

import com.raggle.api.DreamEntityComponent;
import com.raggle.registry.FaeComponentRegistry;
import com.raggle.util.DreamState;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class DreamEntityComponentImpl implements DreamEntityComponent, AutoSyncedComponent {
	
	private final Entity entity;
	
	private DreamState dreamState;

	private boolean shouldWorldRendererReload;
	
	public DreamEntityComponentImpl(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		NbtCompound tag = buf.readNbt();
        if (tag != null) {
        	if(this.dreamState == null || tag.getByte("dream") != DreamState.toByte(this.dreamState)) {
                this.readFromNbt(tag);
                this.shouldWorldRendererReload = true;
        	}
        }
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.dreamState = DreamState.fromByte(tag.getByte("dream"));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		if(this.dreamState != null)
			tag.putByte("dream", this.dreamState.asByte());
		else
			tag.putByte("dream", (byte) 0);
	}
	@Override
	public DreamState getDream() {
		return this.dreamState;
	}
	@Override
	public void setDream(DreamState dreamState) {
		this.dreamState = dreamState;
		FaeComponentRegistry.DREAM_ENTITY.sync(this.entity);
	}
	@Override
	public boolean shouldUpdateClient() {
		if(this.shouldWorldRendererReload) {
			this.shouldWorldRendererReload = false;
			return true;
		}
		return false;
	}

}