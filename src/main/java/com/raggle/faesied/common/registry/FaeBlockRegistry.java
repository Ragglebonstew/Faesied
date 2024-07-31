package com.raggle.faesied.common.registry;

import com.raggle.faesied.Faesied;
import com.raggle.faesied.common.block.DreamBlock;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FaeBlockRegistry {
	
	public static final DreamBlock DREAM_BLOCK = new DreamBlock();
	public static final DreamBlock DREAM_LOG = new DreamBlock();
	public static final DreamBlock DREAM_WOOD = new DreamBlock();
	public static final DreamBlock STRIPPED_DREAM_LOG = new DreamBlock();
	public static final DreamBlock STRIPPED_DREAM_WOOD = new DreamBlock();
	public static final DreamBlock DREAM_LEAVES = new DreamBlock();
	
	public static void init() {
		
		Registry.register(Registry.BLOCK, new Identifier(Faesied.MOD_ID, "dream_block"), DREAM_BLOCK);
		
		//dream wood stuff
		Registry.register(Registry.BLOCK, new Identifier(Faesied.MOD_ID, "dream_log"), DREAM_LOG);
		Registry.register(Registry.BLOCK, new Identifier(Faesied.MOD_ID, "dream_wood"), DREAM_WOOD);
		Registry.register(Registry.BLOCK, new Identifier(Faesied.MOD_ID, "stripped_dream_log"), STRIPPED_DREAM_LOG);
		Registry.register(Registry.BLOCK, new Identifier(Faesied.MOD_ID, "stripped_dream_wood"), STRIPPED_DREAM_WOOD);
		Registry.register(Registry.BLOCK, new Identifier(Faesied.MOD_ID, "dream_leaves"), DREAM_LEAVES);
		
		
	}

}