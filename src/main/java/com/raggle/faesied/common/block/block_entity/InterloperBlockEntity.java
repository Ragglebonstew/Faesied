package com.raggle.faesied.common.block.block_entity;

import com.raggle.faesied.common.registry.FaeBlockRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class InterloperBlockEntity extends BlockEntity {

	protected InterloperBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}
	public InterloperBlockEntity(BlockPos pos, BlockState state) {
		this(FaeBlockRegistry.INTERLOPER_PORTAL_BLOCK_ENTITY, pos, state);
	}

	public boolean shouldDrawSide(Direction direction) {
		return Block.shouldDrawSide(this.getCachedState(), this.world, this.getPos(), direction, this.getPos().offset(direction));
	}
}
