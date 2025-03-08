package com.raggle.faesied.common.registry;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import com.raggle.faesied.Faesied;
import com.raggle.faesied.common.item.DreamResin;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FaeItemRegistry {

	public static final Item SHEEP_LAUREL = new Item(new QuiltItemSettings());
	public static final BlockItem DREAM_LOG = new BlockItem(FaeBlockRegistry.DREAM_LOG, new QuiltItemSettings());
	public static final BlockItem DREAM_LEAVES = new BlockItem(FaeBlockRegistry.DREAM_LEAVES, new QuiltItemSettings());
	public static final DreamResin DREAM_RESIN = new DreamResin();
	
	public static void init() {
		
		Registry.register(Registries.ITEM, new Identifier(Faesied.MOD_ID, "sheep_laurel"), SHEEP_LAUREL);
		Registry.register(Registries.ITEM, new Identifier(Faesied.MOD_ID, "dream_block"), new BlockItem(FaeBlockRegistry.DREAM_BLOCK, new QuiltItemSettings()));
		Registry.register(Registries.ITEM, new Identifier(Faesied.MOD_ID, "dream_resin"), DREAM_RESIN);
		
		//dream wood stuff
		Registry.register(Registries.ITEM, new Identifier(Faesied.MOD_ID, "dream_log"), DREAM_LOG);
		Registry.register(Registries.ITEM, new Identifier(Faesied.MOD_ID, "dream_wood"), new BlockItem(FaeBlockRegistry.DREAM_WOOD, new QuiltItemSettings()));
		Registry.register(Registries.ITEM, new Identifier(Faesied.MOD_ID, "stripped_dream_log"), new BlockItem(FaeBlockRegistry.STRIPPED_DREAM_LOG, new QuiltItemSettings()));
		Registry.register(Registries.ITEM, new Identifier(Faesied.MOD_ID, "stripped_dream_wood"), new BlockItem(FaeBlockRegistry.STRIPPED_DREAM_WOOD, new QuiltItemSettings()));
		Registry.register(Registries.ITEM, new Identifier(Faesied.MOD_ID, "dream_leaves"), DREAM_LEAVES);
		
		Registry.register(Registries.ITEM, new Identifier(Faesied.MOD_ID, "interloper_portal_block"), new BlockItem(FaeBlockRegistry.INTERLOPER_PORTAL_BLOCK, new QuiltItemSettings()));
		
		
	}
}