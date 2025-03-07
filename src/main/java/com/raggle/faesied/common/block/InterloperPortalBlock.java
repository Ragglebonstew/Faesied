package com.raggle.faesied.common.block;

import com.raggle.faesied.common.block.block_entity.InterloperBlockEntity;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class InterloperPortalBlock extends BlockWithEntity {

	public InterloperPortalBlock(Settings settings) {
		super(settings);
	}
	
	public InterloperPortalBlock() {
		this(AbstractBlock.Settings.of(Material.PORTAL, MapColor.BLACK).noCollision().strength(-1.0F, 3600000.0F).dropsNothing());
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new InterloperBlockEntity(pos, state);
	}
	
}
