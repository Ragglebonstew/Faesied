package com.raggle.block;

import com.raggle.FaeUtil;
import com.raggle.networking.FaeMessaging;
import com.raggle.util.DreamState;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CopperBrazierBlock extends Block {

	public CopperBrazierBlock(Settings settings) {
		super(settings.nonOpaque().noBlockBreakParticles().breakInstantly());
	}
	@Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(4, 0, 4, 12, 6, 12);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.scheduleBlockTick(pos, this, 20);
        PlayerEntity player = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5, false);
        if(player != null && player.getSleepTimer() >= 50 && player instanceof ServerPlayerEntity spe && FaeUtil.getDreamState(player) == DreamState.AWAKE) {
        	ServerPlayNetworking.send(spe, FaeMessaging.FALLING_ASLEEP, PacketByteBufs.empty());
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient()) {
            world.scheduleBlockTick(pos, this, 20);
        }
    }
}
