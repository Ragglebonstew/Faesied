package com.raggle.half_dream.client;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

import com.raggle.half_dream.client.block.InterloperPortalEntityRenderer;
import com.raggle.half_dream.client.particle.FallenStarParticle;
import com.raggle.half_dream.client.particle.InterloperPortalParticle;
import com.raggle.half_dream.client.sequence.SequenceManager;
import com.raggle.half_dream.common.registry.FaeBlockRegistry;
import com.raggle.half_dream.common.registry.FaeEntityRegistry;
import com.raggle.half_dream.common.registry.FaeParticleRegistry;
import com.raggle.half_dream.networking.FaeMessaging;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.StrayEntityRenderer;

public class FaesiedClient implements ClientModInitializer {
	
	@Override
	public void onInitializeClient(ModContainer mod) {
		
        EntityRendererRegistry.register(FaeEntityRegistry.HDSKELETON, StrayEntityRenderer::new);
        
        FaeMessaging.registerS2CPackets();
        
		ClientTickEvents.START.register(SequenceManager::tick);
		HudRenderCallback.EVENT.register(SequenceManager::render);
        
		BlockEntityRendererFactories.register(FaeBlockRegistry.INTERLOPER_PORTAL_BLOCK_ENTITY, InterloperPortalEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(FaeParticleRegistry.INTERLOPER_MIST, InterloperPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(FaeParticleRegistry.FALLEN_STAR, FallenStarParticle.Factory::new);
	}
	
}