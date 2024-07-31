package com.raggle.faesied.client.sequence;

import com.raggle.faesied.api.DreamClientPlayer;

import net.minecraft.client.MinecraftClient;

public class DreamFogEffect extends FogEffect{
	@Override
	public void tick(MinecraftClient client) {
		if(client.player instanceof DreamClientPlayer dcp && !dcp.isDream())
			this.finished = true;
	}
}