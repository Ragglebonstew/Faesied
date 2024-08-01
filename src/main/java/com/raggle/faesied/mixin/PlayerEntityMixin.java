package com.raggle.faesied.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.raggle.faesied.api.DreamPlayer;

import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements DreamPlayer {
	
}
