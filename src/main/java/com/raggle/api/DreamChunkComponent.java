package com.raggle.api;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.util.math.BlockPos;

public interface DreamChunkComponent extends ComponentV3 {
	
	public boolean contains(BlockPos pos);

	public boolean addPosToList(BlockPos pos);
	
	public boolean removePosFromList(BlockPos pos);
	
	public int clear();

	long getRenderPos();
	
}