package com.raggle.faesied.common.registry;

import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

import com.raggle.faesied.Faesied;
import com.raggle.faesied.common.block.DreamBlock;
import com.raggle.faesied.common.block.InterloperPortalBlock;
import com.raggle.faesied.common.block.block_entity.InterloperBlockEntity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FaeBlockRegistry {
	
	public static final DreamBlock DREAM_BLOCK = new DreamBlock();
	public static final DreamBlock DREAM_LOG = new DreamBlock();
	public static final DreamBlock DREAM_WOOD = new DreamBlock();
	public static final DreamBlock STRIPPED_DREAM_LOG = new DreamBlock();
	public static final DreamBlock STRIPPED_DREAM_WOOD = new DreamBlock();
	public static final DreamBlock DREAM_LEAVES = new DreamBlock();
	public static final InterloperPortalBlock INTERLOPER_PORTAL_BLOCK = new InterloperPortalBlock();
	
	public static void init() {
		
		Registry.register(Registry.BLOCK, new Identifier(Faesied.MOD_ID, "dream_block"), DREAM_BLOCK);
		
		//dream wood stuff
		Registry.register(Registry.BLOCK, new Identifier(Faesied.MOD_ID, "dream_log"), DREAM_LOG);
		Registry.register(Registry.BLOCK, new Identifier(Faesied.MOD_ID, "dream_wood"), DREAM_WOOD);
		Registry.register(Registry.BLOCK, new Identifier(Faesied.MOD_ID, "stripped_dream_log"), STRIPPED_DREAM_LOG);
		Registry.register(Registry.BLOCK, new Identifier(Faesied.MOD_ID, "stripped_dream_wood"), STRIPPED_DREAM_WOOD);
		Registry.register(Registry.BLOCK, new Identifier(Faesied.MOD_ID, "dream_leaves"), DREAM_LEAVES);

		Registry.register(Registry.BLOCK, new Identifier(Faesied.MOD_ID, "interloper_portal_block"), INTERLOPER_PORTAL_BLOCK);
		
		//block entities
	}

	  public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
	    return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Faesied.MOD_ID, path), blockEntityType);
	  }
	 
	  public static final BlockEntityType<InterloperBlockEntity> INTERLOPER_PORTAL_BLOCK_ENTITY = register(
	      "interloper_portal_block_entity",
	      QuiltBlockEntityTypeBuilder.create(InterloperBlockEntity::new, INTERLOPER_PORTAL_BLOCK).build()
	  );

}