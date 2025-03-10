package com.raggle.half_dream.common.component;

import com.raggle.half_dream.api.DreamEntityComponent;
import com.raggle.half_dream.common.registry.FaeComponentRegistry;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class DreamEntityComponentImpl implements DreamEntityComponent, AutoSyncedComponent {
	
	private final Entity entity;
	private byte dream;
	
	public DreamEntityComponentImpl(Entity entity) {
		this.entity = entity;}
	
	@Override
	public boolean isDream() {
		return this.dream != 0;
	}
	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		NbtCompound tag = buf.readNbt();
        if (tag != null) {
        	//SequenceManager.start(new FallingHalfAsleepSequence(dcp, this.dream, tag.getBoolean("dream")));
            this.readFromNbt(tag);
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

}