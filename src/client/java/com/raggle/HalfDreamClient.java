package com.raggle;

import java.util.Optional;

import com.raggle.api.DreamChunkComponent;
import com.raggle.api.DreamEntityComponent;
import com.raggle.client.block.InterloperPortalEntityRenderer;
import com.raggle.client.particle.FallenStarParticle;
import com.raggle.client.particle.InterloperPortalParticle;
import com.raggle.client.sequence.SequenceManager;
import com.raggle.networking.FaeMessaging;
import com.raggle.networking.FaeS2C;
import com.raggle.registry.FaeBlockRegistry;
import com.raggle.registry.FaeComponentRegistry;
import com.raggle.registry.FaeParticleRegistry;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.math.BlockPos;

public class HalfDreamClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ClientTickEvents.END_CLIENT_TICK.register(HalfDreamClient::checkDreamState);

        registerS2CPackets();
        
		ClientTickEvents.START_CLIENT_TICK.register(SequenceManager::tick);
		HudRenderCallback.EVENT.register(SequenceManager::render);
        
		BlockEntityRendererFactories.register(FaeBlockRegistry.INTERLOPER_PORTAL_BLOCK_ENTITY, InterloperPortalEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(FaeParticleRegistry.INTERLOPER_MIST, InterloperPortalParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(FaeParticleRegistry.FALLEN_STAR, FallenStarParticle.Factory::new);
	}

	private static void registerS2CPackets() {
		ClientPlayNetworking.registerGlobalReceiver(FaeMessaging.SKELETON_LIST_SIZE, FaeS2C::recieveListSize);
		ClientPlayNetworking.registerGlobalReceiver(FaeMessaging.STOP_FOG, FaeS2C::stopFogEffect);
		ClientPlayNetworking.registerGlobalReceiver(FaeMessaging.BRIDGE_FOG, FaeS2C::startBridgeSequence);
		
		ClientPlayNetworking.registerGlobalReceiver(FaeMessaging.INTERLOPE, FaeS2C::startInterlopeSequence);
		ClientPlayNetworking.registerGlobalReceiver(FaeMessaging.FALLING_ASLEEP, FaeS2C::startFallingAsleepSequence);
	}
	private static void checkDreamState(MinecraftClient client) {
		Optional<DreamEntityComponent> op1 = FaeComponentRegistry.DREAM_ENTITY.maybeGet(client.player);
		if(op1.isPresent() && op1.get().shouldUpdateClient()) {
			client.worldRenderer.reload();
		}
		if(client.player != null) {
			Optional<DreamChunkComponent> op2 = FaeComponentRegistry.DREAM_AIR.maybeGet(client.world.getChunk(client.player.getBlockPos()));
			if(op2.isPresent()) {
				long renderPos = op2.get().getRenderPos();
				if(renderPos != 0)
					client.worldRenderer.updateBlock(client.world, BlockPos.fromLong(renderPos), null, null, 0);
			}
		}
	}
}