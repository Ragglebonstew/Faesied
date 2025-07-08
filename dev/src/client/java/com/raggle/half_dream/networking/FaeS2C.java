package com.raggle.half_dream.networking;

import com.raggle.half_dream.FaeUtilClient;
import com.raggle.half_dream.api.DreamClientPlayer;
import com.raggle.half_dream.client.sequence.BridgeFogEffect;
import com.raggle.half_dream.client.sequence.FallingHalfAsleepSequence;
import com.raggle.half_dream.client.sequence.InterlopeSequence;
import com.raggle.half_dream.client.sequence.SequenceManager;
import com.raggle.half_dream.client.sequence.SkeletonCircleFogEffect;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class FaeS2C {
	
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
	public static void startBridgeSequence(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {		
		BlockPos pos = client.player.getBlockPos();
		Direction facing = client.player.getHorizontalFacing();
		
		client.execute(() -> {
			SequenceManager.setFogEffect(new BridgeFogEffect(pos, facing, FaeUtilClient.getPlayerDream()));
		});
	}
	public static void stopFogEffect(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {				
		client.execute(() -> {
			if(SequenceManager.hasFogEffect()){
				SequenceManager.getFogEffect().cancel();
			}
		});
	}
	public static void startFallingAsleepSequence(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {		
		client.execute(() -> {
			SequenceManager.start(new FallingHalfAsleepSequence());
		});
	}
	public static void startInterlopeSequence(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {		
		client.execute(() -> {
			SequenceManager.start(new InterlopeSequence());
		});
	}
}