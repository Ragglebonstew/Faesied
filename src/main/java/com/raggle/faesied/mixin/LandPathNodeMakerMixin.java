package com.raggle.faesied.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.faesied.api.DreamEntityComponent;
import com.raggle.faesied.common.FaeUtil;
import com.raggle.faesied.common.block.DreamBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@Mixin(LandPathNodeMaker.class)
public abstract class LandPathNodeMakerMixin extends PathNodeMaker{
	
	@Inject(method = "getCommonNodeType", at = @At("HEAD"), cancellable = true)
	private static void getCommonNodeType(BlockView world, BlockPos pos, CallbackInfoReturnable<PathNodeType> cir) {
		BlockState blockState = world.getBlockState(pos);
		Block block = blockState.getBlock();
		if(block instanceof DreamBlock) {
			cir.setReturnValue(PathNodeType.DOOR_OPEN);
		}
	}
	
	@Inject(method = "adjustNodeType", at = @At("HEAD"), cancellable = true)
	private void adjustNodeType(BlockView world, boolean a, boolean b, BlockPos pos, PathNodeType type, CallbackInfoReturnable<PathNodeType> cir) {
		if(this.entity instanceof DreamEntityComponent de && de.isDream()) {
			if(world instanceof World && FaeUtil.isDreamless(pos, (World)world)) {
				cir.setReturnValue(PathNodeType.OPEN);
			}
		}
	}

}
