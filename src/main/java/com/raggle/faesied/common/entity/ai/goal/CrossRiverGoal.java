package com.raggle.faesied.common.entity.ai.goal;

import com.raggle.faesied.Faesied;
import com.raggle.faesied.api.DreamHorse;
import com.raggle.faesied.api.DreamPlayer;
import com.raggle.faesied.client.sequence.BridgeFogEffect;
import com.raggle.faesied.client.sequence.SequenceManager;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.tag.BiomeTags;
import net.minecraft.util.Holder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class CrossRiverGoal extends Goal{
	
	private final SkeletonHorseEntity horse;
	private final World world;
	
	private Direction startFacing;
	private BlockPos startPos;
	private boolean failed;

	public CrossRiverGoal(SkeletonHorseEntity horse) {
		this.horse = horse;
		this.world = horse.getWorld();
		this.failed = false;
	}
	
	@Override
	public boolean canStart() {
		Holder<Biome> holder = world.getBiome(horse.getBlockPos());
		if(
				horse.isTouchingWater() || 
				horse instanceof DreamHorse dh &&
				dh.getPlayer() == null
				) {//fails if horse is touching water, player is null, or player isDream
			return false;
		}
		if(this.failed) {
			if(!holder.isIn(BiomeTags.IS_RIVER)) {
				this.failed = false;
				Faesied.LOGGER.info("reset failure");
			}
			else {
				return false;
			}
		}
		
		return holder.isIn(BiomeTags.IS_RIVER) && this.overWater();
	}
	@Override
	public boolean shouldContinue() {
		if(horse instanceof DreamHorse dh && dh.getPlayer() == null || this.failed) {
			return false;
		}
		Holder<Biome> holder = world.getBiome(horse.getBlockPos());
		return holder.isIn(BiomeTags.IS_RIVER);
	}
	@Override
	public void start() {
		this.startFacing = horse.getHorizontalFacing();
		this.startPos = horse.getBlockPos();
		if(horse instanceof DreamHorse dh && dh.getPlayer() instanceof DreamPlayer dp)
			SequenceManager.setFogEffect(new BridgeFogEffect(this.startPos, this.startFacing, dp.isDream()));
	}
	@Override
	public void stop() {
		Holder<Biome> holder = world.getBiome(horse.getBlockPos());
		BlockPos dPos = horse.getBlockPos().subtract(this.startPos);
		int x = dPos.getX() * this.startFacing.getVector().getX();
		int z = dPos.getZ() * this.startFacing.getVector().getZ();
		if(
				!holder.isIn(BiomeTags.IS_RIVER)
				&& startFacing == horse.getHorizontalFacing()
				&& (x > 0 || z > 0)//checks if the change in position is in the same direction as initial facing
				&& !this.failed
				&& horse instanceof DreamHorse dh
				&& dh.getPlayer() instanceof DreamPlayer dp
		) {
			dp.setDream(!dp.isDream());
		}
		else if(SequenceManager.hasFogEffect()){
			SequenceManager.getFogEffect().cancel();
		}
	}
	@Override
	public void tick() {
		if(horse.isTouchingWater()) {
			this.failed = true;
			return;
		}
	}
	private boolean overWater() {
		for(int i = 0; i < 5; i++) {
			if(world.getBlockState(horse.getBlockPos().down(i)).isOf(Blocks.WATER)) {
				return true;
			}
		}
		return false;
	}

}
