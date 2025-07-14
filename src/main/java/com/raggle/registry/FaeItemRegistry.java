package com.raggle.registry;
import com.raggle.HalfDream;
import com.raggle.item.BunnyPlushItem;
import com.raggle.item.DreamResin;
import com.raggle.item.ResinHorn;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FaeItemRegistry {

	public static final Item SHEEP_LAUREL = new Item(new FabricItemSettings());
	public static final DreamResin DREAM_RESIN = new DreamResin();
	public static final BunnyPlushItem BUNNY_PLUSH = new BunnyPlushItem(FaeBlockRegistry.BUNNY_PLUSH, new FabricItemSettings());
	public static final ResinHorn RESIN_HORN = new ResinHorn();
	public static final BlockItem COPPER_BRAZIER = new BlockItem(FaeBlockRegistry.COPPER_BRAZIER, new FabricItemSettings());
	
	public static void init() {
		
		Registry.register(Registries.ITEM, new Identifier(HalfDream.MOD_ID, "sheep_laurel"), SHEEP_LAUREL);
		Registry.register(Registries.ITEM, new Identifier(HalfDream.MOD_ID, "dream_resin"), DREAM_RESIN);
		
		Registry.register(Registries.ITEM, new Identifier(HalfDream.MOD_ID, "interloper_portal_block"), new BlockItem(FaeBlockRegistry.INTERLOPER_PORTAL_BLOCK, new FabricItemSettings()));
		
		Registry.register(Registries.ITEM, new Identifier(HalfDream.MOD_ID, "bunny_plush"), BUNNY_PLUSH);
		Registry.register(Registries.ITEM, new Identifier(HalfDream.MOD_ID, "resin_horn"), RESIN_HORN);
		Registry.register(Registries.ITEM, new Identifier(HalfDream.MOD_ID, "copper_brazier"), COPPER_BRAZIER);


	}
}