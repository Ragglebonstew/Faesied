package com.raggle.block;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import com.raggle.FaeUtil;
import com.raggle.block.block_entity.InterloperBlockEntity;
import com.raggle.networking.FaeMessaging;
import com.raggle.registry.FaeBlockRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class InterloperPortalBlock extends BlockWithEntity implements Waterloggable {

	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
	
	public static int ACTIVE_TICKS = 400;

	private InterloperPortalBlock(Settings settings) {
		super(settings);
	}
	
	public InterloperPortalBlock() {
		this(FabricBlockSettings.copyOf(Blocks.STONE)
				//.noCollision()
				.strength(-1.0F, 3600000.0F)
				.dropsNothing()
				.luminance(InterloperPortalBlock::getLuminance)
				.nonOpaque()
				//.blockVision(InterloperPortalBlock::shouldBlockVisionPredicate)
				);

		setDefaultState(getDefaultState()
				.with(ACTIVE, false)
	            .with(WATERLOGGED, false)
		);
	}
	/*
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
	*/

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new InterloperBlockEntity(pos, state);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE, WATERLOGGED);
	}

	public static int getLuminance(BlockState state) {
		boolean activated = state.get(ACTIVE);
		return activated ? 8 : 0;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER));
        }
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return state.get(ACTIVE) ? BlockRenderType.INVISIBLE : BlockRenderType.MODEL;
	}
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return !state.get(ACTIVE) ? state.getOutlineShape(world, pos) : VoxelShapes.empty();
	}
	
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
			ServerPlayNetworking.send(player, FaeMessaging.FALLING_ASLEEP, PacketByteBufs.empty());
		}
	}
	@Override
	public BlockState getStateForNeighborUpdate(
			BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
		) {

		if (state.get(WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		
		if(neighborState.getBlock() == this) {
			return neighborState;
		}
		return state;
	}
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(ACTIVE)) {
			world.setBlockState(pos, state.with(ACTIVE, false));
		}
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return !world.isClient && world.getDimension().hasSkyLight() ? checkType(type, FaeBlockRegistry.INTERLOPER_PORTAL_BLOCK_ENTITY, InterloperPortalBlock::tick) : null;
	}
	private static void tick(World world, BlockPos pos, BlockState state, InterloperBlockEntity blockEntity) {
		
		if (!state.get(ACTIVE) && world.getTime() % 20L == 0L && checkNerabyInterlopedPlayers(world, pos, state)) {
			world.setBlockState(pos, state.with(ACTIVE, true));
			world.scheduleBlockTick(pos, state.getBlock(), ACTIVE_TICKS);
		}
		if(world.getTime() % 20L == 0L) {
			setDreamBlocks(world, pos, !state.get(ACTIVE));
		}
	}
	
	private static boolean checkNerabyInterlopedPlayers(World world, BlockPos pos, BlockState state) {
		int distance = 10;
		Box bounding_box = new Box(pos.getX()-distance, pos.getY()-distance, pos.getZ()-distance, pos.getX()+1+distance, pos.getY()+1+distance, pos.getZ()+1+distance);
		List<PlayerEntity> players = world.getPlayers(TargetPredicate.createNonAttackable(), null, bounding_box);
		for(PlayerEntity player : players) {
			if(FaeUtil.isInterloped(player)) {
				return true;
			}
		}
		return false;
	}
	private static void setDreamBlocks(World world, BlockPos pos, boolean b) {
		FaeUtil.setDreamBlock(pos, b, world);
		FaeUtil.setDreamBlock(pos.offset(Direction.EAST), b, world);
		FaeUtil.setDreamBlock(pos.offset(Direction.NORTH), b, world);
		FaeUtil.setDreamBlock(pos.offset(Direction.WEST), b, world);
		FaeUtil.setDreamBlock(pos.offset(Direction.SOUTH), b, world);
		FaeUtil.setDreamBlock(pos.offset(Direction.NORTH).offset(Direction.EAST), b, world);
		FaeUtil.setDreamBlock(pos.offset(Direction.NORTH).offset(Direction.WEST), b, world);
		FaeUtil.setDreamBlock(pos.offset(Direction.SOUTH).offset(Direction.EAST), b, world);
		FaeUtil.setDreamBlock(pos.offset(Direction.SOUTH).offset(Direction.WEST), b, world);
	}
}
