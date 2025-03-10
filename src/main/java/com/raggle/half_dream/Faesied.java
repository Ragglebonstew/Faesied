package com.raggle.half_dream;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.raggle.half_dream.common.particles.FaeParticles;
import com.raggle.half_dream.common.registry.FaeBlockRegistry;
import com.raggle.half_dream.common.registry.FaeComponentRegistry;
import com.raggle.half_dream.common.registry.FaeEntityRegistry;
import com.raggle.half_dream.common.registry.FaeEventRegistry;
import com.raggle.half_dream.common.registry.FaeItemRegistry;
import com.raggle.half_dream.common.registry.FaeTagRegistry;
import com.raggle.half_dream.networking.FaeMessaging;

public class Faesied implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod name as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("Half Dream");
	
	public static final String MOD_ID = "half_dream";

	@Override
	public void onInitialize(ModContainer mod) {
		
		FaeBlockRegistry.init();
		FaeComponentRegistry.init();
		FaeEntityRegistry.init();
		FaeEventRegistry.init();
		FaeItemRegistry.init();
		FaeMessaging.registerC2SPackets();
		FaeTagRegistry.init();
		FaeParticles.init();
		
	}
}
/*
bug list (things that are broke)
- water flow gets wack w/ dream blocks
- broken pathfinding
	- entities jump while passing through dream blocks
	- dream mobs get stuck in dreamless blocks
- can attack entities despite dream state
- entities can push player in dream state, and probably the other way around
- skybox renders w/ sodium
- placed dream blocks don't register as dream
- living and item entities are not registered for dream

check list (things that may/may not be broke anymore)
- empty!


ideas (things that aren't in atm)
- only travel into dream world during night. If don't leave by sunrise, semi-trapped
- Potion of Half Sleeping - since the old one was deleted. Should only toggle state, not use StatusEffect
- interloper portal lures players into dream world
- dream horn echolocates interloper portals, giving players access to skeleton horses
- interloper portal has cool veil particle effect
*/