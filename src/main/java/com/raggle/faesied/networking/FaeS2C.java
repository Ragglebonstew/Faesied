package com.raggle.faesied.networking;

import org.quiltmc.qsl.networking.api.PacketSender;
import com.raggle.faesied.api.DreamClientPlayer;
import com.raggle.faesied.client.sequence.FallingHalfAsleepSequence;
import com.raggle.faesied.client.sequence.SequenceManager;
import com.raggle.faesied.client.sequence.SkeletonCircleFogEffect;
import com.raggle.faesied.common.registry.FaeComponentRegistry;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class FaeS2C {

	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		DreamClientPlayer player = ((DreamClientPlayer)client.player);
		boolean startDream = player.isDream();
		boolean endDream = buf.readBoolean();
		client.execute(() -> {
			SequenceManager.start(new FallingHalfAsleepSequence(player, startDream, endDream));
			//player.setDream(endDream);
			FaeComponentRegistry.DREAM_ENTITY.get(client.player).setDream(endDream);
		});
	}
	
	public static void recieveListSize(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		DreamClientPlayer player = ((DreamClientPlayer)client.player);
		int size = buf.readInt();
		player.setListSize(size);
		if(size >= 4) {
			client.execute(() -> {
				SequenceManager.setFogEffect(new SkeletonCircleFogEffect());
			});
		}
	}
}