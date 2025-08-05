package com.raggle.registry;

import com.raggle.HalfDream;
import com.raggle.block.BunnyPlushBlock;
import com.raggle.block.CopperBrazierBlock;
import com.raggle.block.InterloperPortalBlock;
import com.raggle.block.block_entity.InterloperBlockEntity;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FaeBlockRegistry {
	
	public static final InterloperPortalBlock INTERLOPER_PORTAL_BLOCK = new InterloperPortalBlock();
	public static final BunnyPlushBlock BUNNY_PLUSH = new BunnyPlushBlock(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL));
	public static final CopperBrazierBlock COPPER_BRAZIER = new CopperBrazierBlock(FabricBlockSettings.copyOf(Blocks.CUT_COPPER));

	public static final BlockEntityType<InterloperBlockEntity> INTERLOPER_PORTAL_BLOCK_ENTITY = register(
			"interloper_portal_block_entity",
			FabricBlockEntityTypeBuilder.create(InterloperBlockEntity::new, INTERLOPER_PORTAL_BLOCK).build()
			);
	  
	public static void init() {

		Registry.register(Registries.BLOCK, new Identifier(HalfDream.MOD_ID, "interloper_portal_block"), INTERLOPER_PORTAL_BLOCK);
		Registry.register(Registries.BLOCK, new Identifier(HalfDream.MOD_ID, "bunny_plush"), BUNNY_PLUSH);
		Registry.register(Registries.BLOCK, new Identifier(HalfDream.MOD_ID, "copper_brazier"), COPPER_BRAZIER);
		
		//block entities
	}

	private static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(HalfDream.MOD_ID, path), blockEntityType);
	}
	 

}