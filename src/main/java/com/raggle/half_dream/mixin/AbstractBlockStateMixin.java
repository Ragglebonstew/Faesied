package com.raggle.half_dream.mixin;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.half_dream.api.DreamEntityComponent;
import com.raggle.half_dream.common.FaeUtil;
import com.raggle.half_dream.common.block.DreamBlock;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
	
    @Shadow public abstract Block getBlock();
    //@Shadow public abstract VoxelShape getCollisionShape(BlockView world, BlockPos pos, ShapeContext context);
    
	@Inject(at = @At("HEAD"), method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", cancellable = true)
    private void getCollisionShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        
        if(context instanceof EntityShapeContext esc) {
            Entity entity = esc.getEntity();
            if(entity instanceof DreamEntityComponent de) {
            	if(de.isDream()){
            		if(world instanceof World w && FaeUtil.isDreamAir(pos, w)) {
            			cir.setReturnValue(VoxelShapes.empty());
            		}
            	}
            	else if(this.getBlock() instanceof DreamBlock) {
            		cir.setReturnValue(VoxelShapes.empty());
            	}
            }
        }
        
    }
	
	@Inject(method = "getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
	private void getOutlineShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if(context instanceof EntityShapeContext esc && world instanceof World aw) {
			Entity entity = esc.getEntity();
			if(entity != null && entity instanceof DreamEntityComponent de) {
				if(de.isDream()) {
					if(FaeUtil.isDreamAir(pos, aw))
						cir.setReturnValue(VoxelShapes.empty());
				}
				else if(this.getBlock() instanceof DreamBlock)
					cir.setReturnValue(VoxelShapes.empty());
			}
		}
	}

	@Inject(method = "canReplace", at = @At("HEAD"), cancellable = true)
	public void canReplace(ItemPlacementContext context, CallbackInfoReturnable<Boolean> cir) {
		PlayerEntity player = context.getPlayer();
		if(!FaeUtil.isDream(player)) {
			if(FaeUtil.isDreamAir(context.getBlockPos(), context.getWorld()))
				cir.setReturnValue(true);
		}
		//return this.getBlock().canReplace(this.asBlockState(), context);
	}
	
	@ClientOnly
	@Inject(method = "getCameraCollisionShape", at = @At("HEAD"), cancellable = true)
	private void getCameraCollisionShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {

		if(context instanceof EntityShapeContext esc && world instanceof World aw) {
			Entity entity = esc.getEntity();
			if(!FaeUtil.canInteract(entity, pos, aw))
				cir.setReturnValue(VoxelShapes.empty());
		}
		
	}
	
	@Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    private void onEntityCollision(World world, BlockPos pos, Entity entity, CallbackInfo ci) {
		if(FaeUtil.canInteract(entity, pos, world))
			ci.cancel();
    }
	@Inject(method = "shouldBlockVision", at = @At("HEAD"), cancellable = true)
	private void shouldBlockVision(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if(FaeUtil.hasClientPlayer()) {
			if(FaeUtil.isPlayerDream()) {
				if(FaeUtil.isDreamAir(pos)) 
					cir.setReturnValue(false);
			}
			else {
				if(this.getBlock() instanceof DreamBlock) 
					cir.setReturnValue(false);
			}
		}
	}
	
}
