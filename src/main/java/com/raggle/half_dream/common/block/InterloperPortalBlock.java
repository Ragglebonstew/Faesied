package com.raggle.half_dream.common.block;

import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

import com.raggle.half_dream.client.FaeUtilClient;
import com.raggle.half_dream.common.FaeUtil;
import com.raggle.half_dream.common.block.block_entity.InterloperBlockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

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
				//.blockVision(InterloperPortalBlock::shouldBlockVisionPredicate)
				);

		setDefaultState(getDefaultState().with(ACTIVE, true));
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!player.getAbilities().allowModifyWorld) {
			// Skip if the player isn't allowed to modify the world.
			return ActionResult.PASS;
		} else {
			// Get the current value of the "activated" property
			boolean activated = state.get(ACTIVE);

			// Flip the value of activated and save the new blockstate.
			world.setBlockState(pos, state.with(ACTIVE, !activated));

			return ActionResult.SUCCESS;
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new InterloperBlockEntity(pos, state);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
	}

	public static int getLuminance(BlockState state) {
		boolean activated = state.get(ACTIVE);
		return activated ? 8 : 0;
	}
	/*
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return state.get(ACTIVE) ? BlockRenderType.MODEL : BlockRenderType.INVISIBLE;
	}*/
	public static boolean shouldBlockVisionPredicate(BlockState state, BlockView world, BlockPos pos) {
		return state.get(ACTIVE);
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (entity instanceof ServerPlayerEntity player 
				&& !world.isClient 
				&& player.squaredDistanceTo(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5) <= 0.0401
				&& state.get(ACTIVE)
				&& FaeUtil.isInterloped(player)
		) {
			FaeUtil.setInterlope(player, false);
			FaeUtil.toggleDream(player);
			world.setBlockState(pos, state.with(ACTIVE, false));
		}
	}
	@Override
	public BlockState getStateForNeighborUpdate(
			BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
		) {
		if(neighborState.getBlock() == this) {
			return neighborState;
		}
		return state;
	}
	
}
