package com.raggle.registry;

import com.raggle.HalfDream;
import com.raggle.api.DreamChunkComponent;
import com.raggle.api.DreamEntityComponent;
import com.raggle.api.DreamPlayerComponent;
import com.raggle.component.DreamChunkComponentImpl;
import com.raggle.component.DreamEntityComponentImpl;
import com.raggle.component.DreamPlayerComponentImpl;

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class FaeComponentRegistry implements ChunkComponentInitializer, EntityComponentInitializer {

	public static final ComponentKey<DreamChunkComponent> DREAM_AIR = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(HalfDream.MOD_ID, "dream_air"), DreamChunkComponent.class);
	public static final ComponentKey<DreamChunkComponent> DREAM_BLOCKS = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(HalfDream.MOD_ID, "dream_blocks"), DreamChunkComponent.class);
	public static final ComponentKey<DreamEntityComponent> DREAM_ENTITY = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(HalfDream.MOD_ID, "dream_entity"), DreamEntityComponent.class);
	public static final ComponentKey<DreamPlayerComponent> DREAM_PLAYER = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(HalfDream.MOD_ID, "dream_player"), DreamPlayerComponent.class);

	public static final String DREAMSTATE_KEY = "half_dream_state";
	
	public static void init() {
		
	}

	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(DREAM_AIR, DreamChunkComponentImpl::new);
		registry.register(DREAM_BLOCKS, DreamChunkComponentImpl::new);
	}
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(ItemEntity.class, DREAM_ENTITY, DreamEntityComponentImpl::new);
		registry.registerFor(LivingEntity.class, DREAM_ENTITY, DreamEntityComponentImpl::new);
		registry.registerFor(PlayerEntity.class, DREAM_PLAYER, DreamPlayerComponentImpl::new);
	}
}