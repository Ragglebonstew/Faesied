package com.raggle.half_dream.common.registry;

import com.raggle.half_dream.Faesied;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;

import com.raggle.half_dream.common.block.BunnyPlushBlock;
import com.raggle.half_dream.common.block.InterloperPortalBlock;
import com.raggle.half_dream.common.block.block_entity.InterloperBlockEntity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FaeBlockRegistry {
	
	public static final InterloperPortalBlock INTERLOPER_PORTAL_BLOCK = new InterloperPortalBlock();
	public static final BunnyPlushBlock BUNNY_PLUSH = new BunnyPlushBlock(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL));

	public static final BlockEntityType<InterloperBlockEntity> INTERLOPER_PORTAL_BLOCK_ENTITY = register(
			"interloper_portal_block_entity",
			FabricBlockEntityTypeBuilder.create(InterloperBlockEntity::new, INTERLOPER_PORTAL_BLOCK).build()
			);
	  
	public static void init() {

		Registry.register(Registries.BLOCK, new Identifier(Faesied.MOD_ID, "interloper_portal_block"), INTERLOPER_PORTAL_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(Faesied.MOD_ID, "bunny_plush"), BUNNY_PLUSH);
		
		//block entities
	}

	private static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Faesied.MOD_ID, path), blockEntityType);
	}
	 

}