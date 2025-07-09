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
	
	private DreamState dream;

	private boolean shouldWorldRendererReload;
	
	public DreamEntityComponentImpl(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		NbtCompound tag = buf.readNbt();
        if (tag != null) {
        	if(this.dream == null || tag.getByte("dream") != DreamState.toByte(this.dream)) {
                this.readFromNbt(tag);
                this.shouldWorldRendererReload = true;
        	}
        }
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.dream = DreamState.fromByte(tag.getByte("dream"));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		if(this.dream != null)
			tag.putByte("dream", this.dream.asByte());
		else
			tag.putByte("dream", (byte) 0);
	}
	@Override
	public DreamState getDream() {
		return this.dream;
	}
	@Override
	public void setDream(DreamState b) {
		this.dream = b;
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