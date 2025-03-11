package com.raggle.half_dream.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.half_dream.common.FaeUtil;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkCache;

@Mixin(ChunkCache.class)
public abstract class ChunkCacheMixin {
	
	@Shadow
	public abstract Chunk getChunk(BlockPos pos);


	@Inject(method = "getBlockState", at = @At("HEAD"), cancellable = true)
	private void getBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
		Chunk chunk = this.getChunk(pos);
		if(FaeUtil.isDreamBlock(pos, chunk)) {
			cir.setReturnValue(Blocks.AIR.getDefaultState());
		}
	}
	
}
