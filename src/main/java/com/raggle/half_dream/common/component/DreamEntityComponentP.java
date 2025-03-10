package com.raggle.half_dream.common.component;

import com.raggle.half_dream.api.DreamClientPlayer;
import com.raggle.half_dream.api.DreamEntityComponent;
import com.raggle.half_dream.client.sequence.FallingHalfAsleepSequence;
import com.raggle.half_dream.client.sequence.SequenceManager;
import com.raggle.half_dream.common.FaeUtil;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class DreamEntityComponentP implements DreamEntityComponent, AutoSyncedComponent {
	
	private final PlayerEntity player;
	
	public DreamEntityComponentP(PlayerEntity player) {
		this.player = player;
	}
	@Override
	public void setDream(boolean b) {
		FaeUtil.setDream(this.player, b ? (byte) 1 : (byte) 0);
	}
	@Override
	public boolean isDream() {
		return FaeUtil.isDream(player);
	}
	@Override
	public void applySyncPacket(PacketByteBuf buf) {
		NbtCompound tag = buf.readNbt();
        if (tag != null) {
    		SequenceManager.start(new FallingHalfAsleepSequence((DreamClientPlayer)this.player, FaeUtil.isPlayerDream(), tag.getBoolean("dream")));
            this.readFromNbt(tag);
        }
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		FaeUtil.setDream(player, tag.getBoolean("dream") ? (byte) 1 : (byte) 0);
		FaeUtil.setDream(player, tag.getByte("dream_level"));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("dream", FaeUtil.isDream(player));
		tag.putByte("dream_level", FaeUtil.getDream(player));
	}
	@Override
	public byte getDream() {
		return FaeUtil.getDream(player);
	}
	@Override
	public void setDream(byte b) {
		FaeUtil.setDream(player, b);
	}
	@Override
	public NbtCompound getPersistantData() {
		return ((DreamEntityComponent)this.player).getPersistantData();
	}

}