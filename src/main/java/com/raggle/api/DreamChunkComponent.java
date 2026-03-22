package com.raggle.api;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.util.math.BlockPos;

public interface DreamChunkComponent extends ComponentV3 {
	
	boolean contains(BlockPos pos);

	void addPosToList(BlockPos pos);
	
	void removePosFromList(BlockPos pos);
	
	int clear();

	long getRenderPos();

	boolean addPosToQueue(BlockPos pos);

	boolean pushPosFromQueue(BlockPos pos);

}