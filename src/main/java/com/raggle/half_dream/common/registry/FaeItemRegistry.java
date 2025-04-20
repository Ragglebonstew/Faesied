package com.raggle.half_dream.common.registry;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import com.raggle.half_dream.Faesied;
import com.raggle.half_dream.common.item.BunnyPlushItem;
import com.raggle.half_dream.common.item.DreamResin;
import com.raggle.half_dream.common.item.FlameSpawner;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FaeItemRegistry {

	public static final Item SHEEP_LAUREL = new Item(new QuiltItemSettings());
	public static final DreamResin DREAM_RESIN = new DreamResin();
	public static final Item FLAME_SPAWNER = new FlameSpawner();
	public static final BunnyPlushItem BUNNY_PLUSH = new BunnyPlushItem(FaeBlockRegistry.BUNNY_PLUSH, new QuiltItemSettings());
	
	public static void init() {
		
		Registry.register(Registries.ITEM, new Identifier(Faesied.MOD_ID, "sheep_laurel"), SHEEP_LAUREL);
		Registry.register(Registries.ITEM, new Identifier(Faesied.MOD_ID, "dream_resin"), DREAM_RESIN);
		
		Registry.register(Registries.ITEM, new Identifier(Faesied.MOD_ID, "interloper_portal_block"), new BlockItem(FaeBlockRegistry.INTERLOPER_PORTAL_BLOCK, new QuiltItemSettings()));
		
		Registry.register(Registries.ITEM, new Identifier(Faesied.MOD_ID, "flame_spawner"), FLAME_SPAWNER);
		Registry.register(Registries.ITEM, new Identifier(Faesied.MOD_ID, "bunny_plush"), BUNNY_PLUSH);

	}
}