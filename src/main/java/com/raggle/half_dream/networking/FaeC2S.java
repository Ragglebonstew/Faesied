package com.raggle.half_dream.networking;

import org.quiltmc.qsl.networking.api.PacketSender;

import com.raggle.half_dream.common.FaeUtil;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class FaeC2S {

	public static void receiveSendToDeepDream(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		server.execute(() -> {
			//QuiltDimensions.teleport(player, server.getWorld(FaeMessaging.DEEP_DREAM), new TeleportTarget(player.getPos(), new Vec3d(0,0,0), 0, 0));
		});
	}
	public static void onLoadClient(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		if(FaeUtil.getDream(player) != 0) {
			//server.execute(() -> dp.syncDream());
		}
	}
	
}