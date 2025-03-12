package com.raggle.half_dream.common.component;

import java.util.ArrayList;

import com.raggle.half_dream.api.DreamChunkComponent;
import com.raggle.half_dream.common.FaeUtil;
import com.raggle.half_dream.common.registry.FaeComponentRegistry;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class DreamChunkComponentImpl implements DreamChunkComponent, AutoSyncedComponent {

	private final Chunk provider;
	private ArrayList<Long> posList;
	private long renderPos;
	
	public DreamChunkComponentImpl(Chunk chunk) {
		this.provider = chunk;
		this.posList = new ArrayList<Long>();
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.posList.clear();
		for(long entry : tag.getLongArray("dreampos")) {
			this.addPosToList(BlockPos.fromLong(entry));
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		long[] posList = new long[this.posList.size()];
		for(int i = 0; i < posList.length; i++) {
			posList[i] = this.posList.get(i);
		}
		tag.putLongArray("dreampos", posList);
	}
	@Override
	public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        NbtCompound tag = new NbtCompound();
        this.writeToNbt(tag);
		tag.putLong("renderpos", this.renderPos);
        buf.writeNbt(tag);
		provider.setNeedsSaving(true);
    }
	@Override
	public void applySyncPacket(PacketByteBuf buf) {
        NbtCompound tag = buf.readNbt();
        if (tag != null) {
            this.readFromNbt(tag);
    		FaeUtil.scheduleChunkRenderAt(BlockPos.fromLong(tag.getLong("renderpos")));
			provider.setNeedsSaving(true);
        }
    }

	@Override
	public boolean contains(BlockPos pos) {
		return !this.posList.isEmpty() && posList.contains(pos.asLong());
	}

	@Override
	public boolean addPosToList(BlockPos pos) {
		if(!posList.contains(pos.asLong())) {
			this.posList.add(pos.asLong());
			this.renderPos = pos.asLong();
			this.sync();
			return true;
		}
		return false;
	}

	@Override
	public boolean removePosFromList(BlockPos pos) {
		if(this.posList.remove(pos.asLong())) {
			this.renderPos = pos.asLong();
			this.sync();
			return true;
		}
		return false;
	}
	
	@Override
	public int clear() {
		int count = this.posList.size();
		this.posList.clear();
		this.sync();
		return count;
	}
	
	private void sync() {
		FaeComponentRegistry.DREAM_AIR.sync(provider);
		FaeComponentRegistry.DREAM_BLOCKS.sync(provider);
	}
}