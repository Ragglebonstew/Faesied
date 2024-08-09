package com.raggle.faesied.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.faesied.common.FaeUtil;

import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

@Mixin(WorldSlice.class)
public class WorldSliceMixin {

	@Inject(at = @At("HEAD"), method = "getBlockState", cancellable = true)
	private void getBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> cir){
		if(FaeUtil.isDreamless(pos) && FaeUtil.isPlayerDream()){
			cir.setReturnValue(Blocks.AIR.getDefaultState());
		}
	}
}
