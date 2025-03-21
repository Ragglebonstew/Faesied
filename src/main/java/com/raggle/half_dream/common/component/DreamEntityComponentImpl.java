package com.raggle.half_dream.common.component;

import com.raggle.half_dream.api.DreamEntityComponent;
import com.raggle.half_dream.client.FaeUtilClient;
import com.raggle.half_dream.common.registry.FaeComponentRegistry;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class DreamEntityComponentImpl implements DreamEntityComponent, AutoSyncedComponent {
	
	private final Entity entity;
	private byte dream;
	
	public DreamEntityComponentImpl(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		NbtCompound tag = buf.readNbt();
        if (tag != null) {
        	if(tag.getByte("dream") != this.dream) {
                this.readFromNbt(tag);
                if(entity.getId() == FaeUtilClient.getClientPlayer().getId()) {
                    MinecraftClient mc = MinecraftClient.getInstance();
                    mc.worldRenderer.reload();
                }
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

}