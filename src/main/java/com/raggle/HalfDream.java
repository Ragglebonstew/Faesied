package com.raggle;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.raggle.networking.FaeMessaging;
import com.raggle.registry.FaeBlockRegistry;
import com.raggle.registry.FaeComponentRegistry;
import com.raggle.registry.FaeEntityRegistry;
import com.raggle.registry.FaeEventRegistry;
import com.raggle.registry.FaeItemRegistry;
import com.raggle.registry.FaeParticleRegistry;
import com.raggle.registry.FaeTagRegistry;

public class HalfDream implements ModInitializer {
	public static final String MOD_ID = "half-dream";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		FaeBlockRegistry.init();
		FaeComponentRegistry.init();
		FaeEntityRegistry.init();
		FaeEventRegistry.init();
		FaeItemRegistry.init();
		FaeMessaging.registerC2SPackets();
		FaeTagRegistry.init();
		FaeParticleRegistry.init();
	}
}