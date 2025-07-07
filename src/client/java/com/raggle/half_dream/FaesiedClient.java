package com.raggle.half_dream;

import com.raggle.half_dream.client.block.InterloperPortalEntityRenderer;
import com.raggle.half_dream.client.particle.FallenStarParticle;
import com.raggle.half_dream.client.particle.InterloperPortalParticle;
import com.raggle.half_dream.client.sequence.SequenceManager;
import com.raggle.half_dream.common.registry.FaeBlockRegistry;
import com.raggle.half_dream.common.registry.FaeEntityRegistry;
import com.raggle.half_dream.common.registry.FaeParticleRegistry;
import com.raggle.half_dream.networking.FaeS2C;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.StrayEntityRenderer;
import net.minecraft.util.Identifier;

public class FaesiedClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		
        EntityRendererRegistry.register(FaeEntityRegistry.HDSKELETON, StrayEntityRenderer::new);
        
        registerS2CPackets();
        
		ClientTickEvents.START_CLIENT_TICK.register(SequenceManager::tick);
		HudRenderCallback.EVENT.register(SequenceManager::render);
        
		BlockEntityRendererFactories.register(FaeBlockRegistry.INTERLOPER_PORTAL_BLOCK_ENTITY, InterloperPortalEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(FaeParticleRegistry.INTERLOPER_MIST, InterloperPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(FaeParticleRegistry.FALLEN_STAR, FallenStarParticle.Factory::new);
	}

	public static final Identifier SKELETON_LIST_SIZE = new Identifier(Faesied.MOD_ID, "skeleton_list_size");
	public static final Identifier STOP_FOG = new Identifier(Faesied.MOD_ID, "stop_fog");
	public static final Identifier BRIDGE_FOG = new Identifier(Faesied.MOD_ID, "bridge_fog");
	public static final Identifier INTERLOPE = new Identifier(Faesied.MOD_ID, "interlope");
	public static final Identifier FALLING_ASLEEP = new Identifier(Faesied.MOD_ID, "falling_asleep");
	
	public static void registerS2CPackets() {
		ClientPlayNetworking.registerGlobalReceiver(SKELETON_LIST_SIZE, FaeS2C::recieveListSize);
		ClientPlayNetworking.registerGlobalReceiver(STOP_FOG, FaeS2C::stopFogEffect);
		ClientPlayNetworking.registerGlobalReceiver(BRIDGE_FOG, FaeS2C::startBridgeSequence);
		
		ClientPlayNetworking.registerGlobalReceiver(INTERLOPE, FaeS2C::startInterlopeSequence);
		ClientPlayNetworking.registerGlobalReceiver(FALLING_ASLEEP, FaeS2C::startFallingAsleepSequence);
	}
	
}