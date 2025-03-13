package com.raggle.half_dream.client;

import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import com.raggle.half_dream.common.FaeUtil;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

@ClientOnly
public class FaeUtilClient {
	

	@Nullable
	public static ClientPlayerEntity getClientPlayer() {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc == null)
			return null;
		return mc.player;
	}
	@Nullable
	public static ClientWorld getClientWorld() {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc == null)
			return null;
		return mc.world;
	}
	public static byte getPlayerDream() {
		ClientPlayerEntity player = getClientPlayer();
		if(player == null)
			return 1;
		return FaeUtil.getDream(getClientPlayer());
	}
}
