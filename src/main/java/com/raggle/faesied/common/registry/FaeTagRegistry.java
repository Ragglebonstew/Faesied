package com.raggle.faesied.common.registry;

import com.raggle.faesied.Faesied;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FaeTagRegistry {
	  public static final TagKey<Block> DREAMING_BLOCKS = TagKey.of(Registry.BLOCK_KEY, new Identifier(Faesied.MOD_ID, "dreaming_blocks"));
	  public static final TagKey<Item> DREAMING_ITEMS = TagKey.of(Registry.ITEM_KEY, new Identifier(Faesied.MOD_ID, "dreaming_items"));

	  public static void init() {}
}
