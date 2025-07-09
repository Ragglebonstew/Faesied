package com.raggle.client.sequence;

import com.raggle.FaeUtil;
import com.raggle.util.DreamState;

import net.minecraft.client.MinecraftClient;

public class DreamFogEffect extends FogEffect{
	@Override
	public void tick(MinecraftClient client) {
		if(FaeUtil.getDreamState(client.player) != DreamState.ASLEEP)
			this.finished = true;
	}
}