package com.raggle.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

import com.raggle.HalfDream;
import com.raggle.api.DreamChunkComponent;
import com.raggle.registry.FaeComponentRegistry;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class DreamChunkComponentImpl implements DreamChunkComponent, AutoSyncedComponent {

	private final Chunk provider;
	private final BitSet[] cubeList;
	private final ArrayList<Long> posQueue;
	private long renderPos;
	
	public DreamChunkComponentImpl(Chunk chunk) {
		this.provider = chunk;
		this.cubeList = new BitSet[chunk.getHeight() >> 4];
		this.posQueue = new ArrayList<>();
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		for (int i = 0; i < this.cubeList.length; i++) { // loop through subchunks of this chunk
			byte[] blocks = tag.getByteArray("dream-cube-"+i);
			BitSet set = BitSet.valueOf(blocks);
			if (!set.isEmpty()) this.cubeList[i] = set;
			else this.cubeList[i] = null;
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		for (int i = 0; i < this.cubeList.length; i++) { // loop through subchunks of this chunk
			BitSet set = this.cubeList[i];
			if(set == null) continue;
			if (set.isEmpty()) tag.putByteArray("dream-cube-"+i, new byte[0]);
			else tag.putByteArray("dream-cube-"+i, set.toByteArray());
		}
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
            this.renderPos = tag.getLong("renderpos");
			provider.setNeedsSaving(true);
        }
    }

	@Override
	public boolean contains(BlockPos pos) {

		int i = this.getSectionIndex(pos);
		if (i < 0) {
			// HalfDream.LOGGER.error("pos is {}, and bottom y is {}, and i is {}", pos.getY(), this.provider.getBottomY(), i);
			return false;
		}
		BitSet set = this.cubeList[i];
		return set != null && set.get(this.getSectionPos(pos));

	}

	@Override
	public void addPosToList(BlockPos pos) {

		int i = this.getSectionIndex(pos);
		if (this.cubeList[i] == null) this.cubeList[i] = new BitSet(16*16*16);
		this.cubeList[i].set(this.getSectionPos(pos));
	}

	@Override
	public void removePosFromList(BlockPos pos) {

		int i = this.getSectionIndex(pos);
		if (i >= 0 && this.cubeList[i] != null) this.cubeList[i].clear(this.getSectionPos(pos));

		this.renderPos = pos.asLong();
		this.sync();
	}
	@Override
	public boolean addPosToQueue(BlockPos pos) {
		if(!posQueue.contains(pos.asLong())) {
			this.posQueue.add(pos.asLong());
			return true;
		}
		return false;
	}

	@Override
	public boolean pushPosFromQueue(BlockPos pos) {
		if(this.posQueue.remove(pos.asLong())) {
			this.addPosToList(pos);
		}
		return true;
	}
	
	@Override
	public int clear() {
		int count = Arrays.stream(this.cubeList).mapToInt(set -> set == null ? 0 : set.cardinality()).sum();
		for (BitSet set: this.cubeList) {
			if (set != null) set.clear();
		}
		this.sync();
		return count;
	}
	@Override
	public long getRenderPos() {
		long pos = this.renderPos;
		this.renderPos = 0;
		return pos;
	}
	
	private void sync() {
		FaeComponentRegistry.DREAM_AIR.sync(provider);
		FaeComponentRegistry.DREAM_BLOCKS.sync(provider);
	}
	private int getSectionIndex(BlockPos pos) {
		return (pos.getY() - this.provider.getBottomY()) >> 4;
	}
	private int getSectionPos(BlockPos pos) {
		int x = (pos.getX() & 15);
		int y = (pos.getY() & 15) << 4;
		int z = (pos.getZ() & 15) << 8;
		return x+y+z;
	}
}