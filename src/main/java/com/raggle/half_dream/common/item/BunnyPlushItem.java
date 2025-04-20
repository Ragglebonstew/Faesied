package com.raggle.half_dream.common.item;

import com.raggle.half_dream.common.FaeUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class BunnyPlushItem extends BlockItem {

	public BunnyPlushItem(Block block, Settings settings) {
		super(block, settings);
	}
	

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if(selected && !world.isClient() && entity instanceof PlayerEntity le && le.isSleeping()) {
			FaeUtil.setInterlope(le, true);
		}
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BLOCK;
	}

}
