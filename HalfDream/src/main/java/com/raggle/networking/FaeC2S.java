package com.raggle.networking;

import com.raggle.FaeUtil;
import com.raggle.HalfDream;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class FaeC2S {

	public static final Identifier DEEP_DREAM = new Identifier(HalfDream.MOD_ID, "deep_dream");
	public static final Identifier ON_LOAD_CLIENT = new Identifier(HalfDream.MOD_ID, "on_load_client");
	public static final Identifier TOGGLE_DREAM = new Identifier(HalfDream.MOD_ID, "on_load_client");

	public static void receiveSendToDeepDream(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		server.execute(() -> {
			//QuiltDimensions.teleport(player, server.getWorld(FaeMessaging.DEEP_DREAM), new TeleportTarget(player.getPos(), new Vec3d(0,0,0), 0, 0));
		});
	}
	public static void receiveToggleDream(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		server.execute(() -> {
			FaeUtil.toggleDream(player);
		});
	}
	
}