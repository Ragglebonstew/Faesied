package com.raggle.half_dream.common.block;

import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

import com.raggle.half_dream.common.block.block_entity.InterloperBlockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;

public class InterloperPortalBlock extends BlockWithEntity implements Waterloggable {
	
	public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

	private InterloperPortalBlock(Settings settings) {
		super(settings);
	}
	
	public InterloperPortalBlock() {
		this(QuiltBlockSettings.copyOf(Blocks.GRASS)
				.noCollision()
				.strength(-1.0F, 3600000.0F)
				.dropsNothing()
				.luminance(InterloperPortalBlock::getLuminance)
				);

		setDefaultState(getDefaultState().with(ACTIVE, false));
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new InterloperBlockEntity(pos, state);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
	}

	public static int getLuminance(BlockState currentBlockState) {
		// Get the value of the "activated" property.
		boolean activated = currentBlockState.get(ACTIVE);

		// Return a light level if activated = true
		return activated ? 8 : 0;
	}
	
}
