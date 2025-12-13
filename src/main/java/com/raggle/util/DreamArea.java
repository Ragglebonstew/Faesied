package com.raggle.util;

import net.minecraft.util.math.BlockPos;

public class DreamArea {
	
	BlockPos pos1, pos2;
	
	public DreamArea(BlockPos pos1, BlockPos pos2) {
		this.pos1 = pos1;
		this.pos2 = pos2;
	}
	public boolean isContained(DreamArea area2) {
		if(this.pos1.asLong() == area2.pos1.asLong() && this.pos2.asLong() == area2.pos2.asLong()) return true;
		if(
				this.pos1.getX() >= area2.pos1.getX() &&
				this.pos1.getY() >= area2.pos1.getY() &&
				this.pos1.getZ() >= area2.pos1.getZ() &&
				this.pos2.getX() <= area2.pos2.getX() &&
				this.pos2.getY() <= area2.pos2.getY() &&
				this.pos2.getZ() <= area2.pos2.getZ()
				)
			return true;
		return false;
	}
}
