package com.raggle.client.sequence;

import com.raggle.FaeUtil;

import net.minecraft.client.MinecraftClient;

public class DreamFogEffect extends FogEffect{
	@Override
	public void tick(MinecraftClient client) {
		if(FaeUtil.getDream(client.player) != 1)
			this.finished = true;
	}
}