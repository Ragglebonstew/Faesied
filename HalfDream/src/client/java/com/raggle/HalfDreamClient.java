package com.raggle;

import java.util.Optional;

import com.raggle.api.DreamEntityComponent;
import com.raggle.registry.FaeComponentRegistry;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class HalfDreamClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ClientTickEvents.END_CLIENT_TICK.register(HalfDreamClient::checkDreamState);
	}
	private static void checkDreamState(MinecraftClient client) {
		Optional<DreamEntityComponent> op = FaeComponentRegistry.DREAM_ENTITY.maybeGet(client.player);
		if(!op.isEmpty() && op.get().shouldUpdateClient()) {
			client.worldRenderer.reload();
		}
	}
}