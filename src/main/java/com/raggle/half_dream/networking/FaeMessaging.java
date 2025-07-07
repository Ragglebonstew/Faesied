package com.raggle.half_dream.networking;
import com.raggle.half_dream.Faesied;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class FaeMessaging {

	public static final Identifier SKELETON_LIST_SIZE = new Identifier(Faesied.MOD_ID, "skeleton_list_size");
	public static final Identifier STOP_FOG = new Identifier(Faesied.MOD_ID, "stop_fog");
	public static final Identifier BRIDGE_FOG = new Identifier(Faesied.MOD_ID, "bridge_fog");
	public static final Identifier INTERLOPE = new Identifier(Faesied.MOD_ID, "interlope");
	public static final Identifier FALLING_ASLEEP = new Identifier(Faesied.MOD_ID, "falling_asleep");
	
	public static void registerC2SPackets() {
		ServerPlayNetworking.registerGlobalReceiver(FaeC2S.DEEP_DREAM, FaeC2S::receiveSendToDeepDream);
		ServerPlayNetworking.registerGlobalReceiver(FaeC2S.TOGGLE_DREAM, FaeC2S::receiveToggleDream);
	}

}