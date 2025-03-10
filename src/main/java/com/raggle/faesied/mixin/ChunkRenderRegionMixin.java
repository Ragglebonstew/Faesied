package com.raggle.faesied.mixin;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.faesied.common.FaeUtil;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.chunk.ChunkRenderRegion;
import net.minecraft.util.math.BlockPos;

@ClientOnly
@Mixin(ChunkRenderRegion.class)
public abstract class ChunkRenderRegionMixin {

	@Inject(method = "getBlockState", at = @At("HEAD"), cancellable = true)
	private void getBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
		if(FaeUtil.isDreamless(pos) && FaeUtil.isPlayerDream()){
			cir.setReturnValue(Blocks.AIR.getDefaultState());
		}
	}
	
}
