package com.raggle.client.sequence;

import com.raggle.FaeUtil;
import com.raggle.FaeUtilClient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class BridgeFogEffect extends FogEffect {

	private final BlockPos startPos;
	private final Direction startFacing;
	private final byte startDream;
	private int progress;
	
	public BridgeFogEffect(BlockPos startPos, Direction startFacing, byte startDream) {
		this.startPos = startPos;
		this.startFacing = startFacing;
		this.startDream = startDream;
		this.progress = 1;
		this.alpha = this.startDream == 1 ? 1.0F : 0.0F;
	}
	@Override
	public void tick(MinecraftClient client) {
		if(FaeUtil.getDream(client.player) != this.startDream) {
			this.finished = true;
		}
		if(this.cancelled) {
			progress++;
		}
		if(this.progress > 30)
			this.finished = true;

		Vec3d dPos = FaeUtilClient.getClientPlayer().getPos().subtract(Vec3d.ofCenter(this.startPos));
		double x = dPos.x * this.startFacing.getVector().getX();
		double z = dPos.z * this.startFacing.getVector().getZ();
		double s;
		s = (z + x)/10;
		if(this.cancelled)
			s = s/this.progress;
		s = MathHelper.clamp(s, 0, 1);
		if(this.startDream == 1)
			s = 1 - s;
		
		this.alpha = (float)s;
		
	}
}