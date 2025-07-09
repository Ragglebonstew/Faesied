package com.raggle.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.raggle.FaeUtil;
import com.raggle.api.DreamHorse;
import com.raggle.entity.ai.goal.CrossRiverGoal;
import com.raggle.util.DreamState;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(SkeletonHorseEntity.class)
public abstract class SkeletonHorseEntityMixin extends AbstractHorseEntity implements DreamHorse {
	
	protected SkeletonHorseEntityMixin(EntityType<? extends AbstractHorseEntity> entityType, World world) {
		super(entityType, world);
		FaeUtil.setDream(this, DreamState.DUAL);
	}


	public PlayerEntity mountedPlayer;
	
	
	@Inject(method = "initCustomGoals", at = @At("TAIL"))
	protected void initCustomGoals(CallbackInfo ci) {
		this.goalSelector.add(1, new CrossRiverGoal((SkeletonHorseEntity)(Object)this));
	}


	@Override
	public void setPlayer(PlayerEntity player) {
		this.mountedPlayer = player;
	}


	@Override
	public PlayerEntity getPlayer() {
		return this.mountedPlayer;
	}
	
}
