package com.raggle.compat.mixin.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.FaeUtilClient;
import com.raggle.util.DreamState;

import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(WorldSlice.class)
public class WorldSliceMixin {

	@Inject(at = @At("HEAD"), method = "getBlockState(III)Lnet/minecraft/block/BlockState;", cancellable = true)
	private void getBlockState(int a, int b, int c, CallbackInfoReturnable<BlockState> cir){
		if(FaeUtilClient.getPlayerDream() == DreamState.ASLEEP && FaeUtilClient.isDreamAir(new BlockPos(a,b,c))){
			cir.setReturnValue(Blocks.AIR.getDefaultState());
		}
	}
}
