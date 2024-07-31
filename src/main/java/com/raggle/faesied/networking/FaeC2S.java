package com.raggle.faesied.networking;

import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.worldgen.dimension.api.QuiltDimensions;

import com.raggle.faesied.api.DreamServerPlayer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

public class FaeC2S {

	public static void receiveSendToDeepDream(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		server.execute(() -> {
			//QuiltDimensions.teleport(player, server.getWorld(FaeMessaging.DEEP_DREAM), new TeleportTarget(player.getPos(), new Vec3d(0,0,0), 0, 0));
		});
	}
	public static void onLoadClient(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		if(player instanceof DreamServerPlayer dp && dp.isDream()) {
			server.execute(() -> dp.syncDream());
		}
	}
	
}