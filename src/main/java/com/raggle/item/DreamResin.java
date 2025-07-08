package com.raggle.item;

import com.raggle.FaeUtil;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DreamResin extends Item {

	public DreamResin() {
		super(new FabricItemSettings().fireproof());
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos().offset(context.getSide());
		
		if(!world.isClient() && FaeUtil.setDreamAir(pos, false, world)) {
			context.getStack().decrement(1);
			world.playSound(null, pos, SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.BLOCKS, 1, 1);
			world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.BLOCKS, 1, 1);
		}
		world.scheduleBlockRerenderIfNeeded(pos, Blocks.AIR.getDefaultState(), world.getBlockState(pos));
		return ActionResult.success(world.isClient());
	}
}