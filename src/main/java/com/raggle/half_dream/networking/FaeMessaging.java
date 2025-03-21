package com.raggle.half_dream.networking;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import com.raggle.half_dream.Faesied;

import net.minecraft.util.Identifier;

public class FaeMessaging {
	
	public static final Identifier DREAM_SYNC = new Identifier(Faesied.MOD_ID, "half_dream");
	public static final Identifier SKELETON_LIST_SIZE = new Identifier(Faesied.MOD_ID, "skeleton_list_size");
	public static final Identifier STOP_FOG = new Identifier(Faesied.MOD_ID, "stop_fog");
	public static final Identifier BRIDGE_FOG = new Identifier(Faesied.MOD_ID, "bridge_fog");
	
	public static final Identifier DEEP_DREAM = new Identifier(Faesied.MOD_ID, "deep_dream");
	public static final Identifier ON_LOAD_CLIENT = new Identifier(Faesied.MOD_ID, "on_load_client");
	
	public static void registerS2CPackets() {
		ClientPlayNetworking.registerGlobalReceiver(SKELETON_LIST_SIZE, FaeS2C::recieveListSize);
		ClientPlayNetworking.registerGlobalReceiver(STOP_FOG, FaeS2C::stopFogEffect);
		ClientPlayNetworking.registerGlobalReceiver(BRIDGE_FOG, FaeS2C::startBridgeSequence);
	}
	public static void registerC2SPackets() {
		ServerPlayNetworking.registerGlobalReceiver(DEEP_DREAM, FaeC2S::receiveSendToDeepDream);
		ServerPlayNetworking.registerGlobalReceiver(ON_LOAD_CLIENT, FaeC2S::onLoadClient);
	}

}