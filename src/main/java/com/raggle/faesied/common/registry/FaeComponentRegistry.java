package com.raggle.faesied.common.registry;

import com.raggle.faesied.Faesied;
import com.raggle.faesied.api.DreamEntityComponent;
import com.raggle.faesied.api.DreamlessComponent;
import com.raggle.faesied.common.component.DreamEntityComponentP;
import com.raggle.faesied.common.component.DreamlessChunkComponent;

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class FaeComponentRegistry implements ChunkComponentInitializer, EntityComponentInitializer {

	public static final ComponentKey<DreamlessComponent> DREAMLESS = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Faesied.MOD_ID, "dreamless"), DreamlessComponent.class);
	public static final ComponentKey<DreamEntityComponent> DREAM_ENTITY = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(Faesied.MOD_ID, "dream_player"), DreamEntityComponent.class);

	public static void init() {
		
	}

	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(DREAMLESS, DreamlessChunkComponent::new);
	}
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(PlayerEntity.class, DREAM_ENTITY, DreamEntityComponentP::new);
		//registry.registerForPlayers(DREAM_ENTITY, DreamEntityComponentP::new);
	}
}