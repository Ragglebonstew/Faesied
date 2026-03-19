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
        return this.pos1.getX() >= area2.pos1.getX() &&
                this.pos1.getY() >= area2.pos1.getY() &&
                this.pos1.getZ() >= area2.pos1.getZ() &&
                this.pos2.getX() <= area2.pos2.getX() &&
                this.pos2.getY() <= area2.pos2.getY() &&
                this.pos2.getZ() <= area2.pos2.getZ();
    }

	public boolean isAttachable(DreamArea area2){
		boolean cornerx1 = this.pos1.getX() == area2.pos1.getX();
		boolean cornery1 = this.pos1.getY() == area2.pos1.getY();
		boolean cornerz1 = this.pos1.getZ() == area2.pos1.getZ();

		boolean cornerx2 = this.pos2.getX() == area2.pos2.getX();
		boolean cornery2 = this.pos2.getY() == area2.pos2.getY();
		boolean cornerz2 = this.pos2.getZ() == area2.pos2.getZ();

		if(cornerx1 && cornery1){
			if(cornerx2 && cornery2) {
				return Math.abs(this.pos1.getZ() - area2.pos1.getZ()) <= 1
						|| Math.abs(this.pos2.getZ() - area2.pos2.getZ()) <= 1;
			}
		} else if (cornerx1 && cornerz1) {
			if(cornerx2 && cornerz2) {
				return Math.abs(this.pos1.getY() - area2.pos1.getY()) <= 1
						|| Math.abs(this.pos2.getY() - area2.pos2.getY()) <= 1;
			}
		} else if (cornery1 && cornerz1) {
			if(cornery2 && cornerz2) {
				return Math.abs(this.pos1.getX() - area2.pos1.getX()) <= 1
						|| Math.abs(this.pos2.getX() - area2.pos2.getX()) <= 1;
			}
		}
		return false;
	}
}
