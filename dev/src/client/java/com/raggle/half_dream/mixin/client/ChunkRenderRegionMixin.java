package com.raggle.half_dream.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.half_dream.FaeUtilClient;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.render.chunk.ChunkRendererRegion;

//@ClientOnly
@Mixin(ChunkRendererRegion.class)
public abstract class ChunkRenderRegionMixin {
	
	//Hides blocks in renderer
	@Inject(method = "getBlockState", at = @At("HEAD"), cancellable = true)
	private void getBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
		if(!FaeUtilClient.canPlayerInteract(pos)){
			cir.setReturnValue(Blocks.AIR.getDefaultState());
		}
	}
	
}
