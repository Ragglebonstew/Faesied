package com.raggle.half_dream.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.half_dream.Faesied;
import com.raggle.half_dream.common.FaeUtil;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.light.ChunkLightProvider;

@Mixin(ChunkLightProvider.class)
public class ChunkLightProviderMixin {

/*
	@Inject(method = "getState", at = @At("HEAD"), cancellable = true)
	private void getState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
		ChunkProvider chunkProvider = ((ChunkLightProviderAccessor)this).getChunkProvider();
		World world = (World) chunkProvider.getWorld();
		if(FaeUtil.isDreamBlock(pos, world)) {
			cir.setReturnValue(Blocks.AIR.getDefaultState());
		}
	}*/
	
	//DO NOT USE ABSOLUTE BREAKAGE
	/*
	@Inject(method = "shapeOccludes", at = @At("HEAD"), cancellable = true)
	private void shapeOccludes(long sourceId, BlockState sourceState, long targetId, BlockState targetState, Direction direction, CallbackInfoReturnable<Boolean> cir) {
		ChunkProvider chunkProvider = ((ChunkLightProviderAccessor)this).getChunkProvider();
		World world = (World) chunkProvider.getWorld();
		BlockPos pos = BlockPos.fromLong(sourceId);
		if(!world.isChunkLoaded(pos))
			return;
		if(FaeUtil.isDreamBlock(pos, world)) {
			cir.setReturnValue(false);
		}
	}
*/
}
