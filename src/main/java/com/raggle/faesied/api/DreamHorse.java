package com.raggle.faesied.api;

import net.minecraft.entity.player.PlayerEntity;

public interface DreamHorse extends DreamEntityComponent{

	public void setPlayer(PlayerEntity player);
	
	public PlayerEntity getPlayer();
}
