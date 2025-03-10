package com.raggle.half_dream.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeMaker;

@Mixin(LandPathNodeMaker.class)
public abstract class LandPathNodeMakerMixin extends PathNodeMaker{
	
	/*
	@Inject(method = "getCommonNodeType", at = @At("HEAD"), cancellable = true)
	private static void getCommonNodeType(BlockView world, BlockPos pos, CallbackInfoReturnable<PathNodeType> cir) {
		BlockState blockState = world.getBlockState(pos);
		Block block = blockState.getBlock();
		if(block instanceof DreamBlock) {
			cir.setReturnValue(PathNodeType.DOOR_OPEN);
		}
	}
	
	@Inject(method = "adjustNodeType", at = @At("HEAD"), cancellable = true)
	private void adjustNodeType(BlockView world, BlockPos pos, PathNodeType type, CallbackInfoReturnable<PathNodeType> cir) {
		if(this.entity instanceof DreamEntityComponent de && de.isDream()) {
			if(world instanceof World && FaeUtil.isDreamAir(pos, (World)world)) {
				cir.setReturnValue(PathNodeType.OPEN);
			}
		}
	}
	*/
}
