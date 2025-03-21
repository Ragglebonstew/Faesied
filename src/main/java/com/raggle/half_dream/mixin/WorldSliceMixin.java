package com.raggle.half_dream.mixin;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.half_dream.client.FaeUtilClient;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

@Pseudo
@ClientOnly
@Mixin(WorldSlice.class)
public class WorldSliceMixin {

	@Inject(at = @At("HEAD"), method = "getBlockState(III)Lnet/minecraft/block/BlockState;", cancellable = true)
	private void getBlockState(int a, int b, int c, CallbackInfoReturnable<BlockState> cir){
		if(FaeUtilClient.getPlayerDream() == 1 && FaeUtilClient.isDreamAir(new BlockPos(a,b,c))){
			cir.setReturnValue(Blocks.AIR.getDefaultState());
		}
	}
}
