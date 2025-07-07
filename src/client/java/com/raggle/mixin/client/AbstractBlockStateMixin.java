package com.raggle.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.half_dream.FaeUtilClient;
import com.raggle.half_dream.common.FaeUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

@Environment(EnvType.CLIENT)
@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
	
	@Shadow
	public abstract boolean isAir();

	@Shadow
	public abstract Block getBlock();
	
	@Inject(method = "getCameraCollisionShape", at = @At("HEAD"), cancellable = true)
	private void getCameraCollisionShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {

		if(context instanceof EntityShapeContext esc) {
			Entity entity = esc.getEntity();
			if(!FaeUtil.canInteract(entity, pos, world))
				cir.setReturnValue(VoxelShapes.empty());
		}
		
	}

	@Inject(method = "shouldBlockVision", at = @At("HEAD"), cancellable = true)
	private void shouldBlockVision(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if(FaeUtilClient.getClientPlayer() != null) {
			if(FaeUtilClient.getPlayerDream() == 1) {
				if(FaeUtilClient.isDreamAir(pos)) 
					cir.setReturnValue(false);
			}
			else {
				if(FaeUtilClient.isDreamBlock(pos)) 
					cir.setReturnValue(false);
			}
		}
	}
}
