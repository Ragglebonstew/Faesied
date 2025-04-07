package com.raggle.half_dream.mixin.common;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.half_dream.client.FaeUtilClient;
import com.raggle.half_dream.common.FaeUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
	
	@Shadow
	public abstract boolean isAir();

	@Shadow
	public abstract Block getBlock();
    
	@Inject(at = @At("HEAD"), method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", cancellable = true)
    private void getCollisionShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        
        if(context instanceof EntityShapeContext esc) {
            Entity entity = esc.getEntity();
			if(entity != null && !FaeUtil.canInteract(entity, pos, world))
				cir.setReturnValue(VoxelShapes.empty());
        }
        
    }
	
	@Inject(method = "getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
	private void getOutlineShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if(context instanceof EntityShapeContext esc) {
			Entity entity = esc.getEntity();
			if(entity != null && !FaeUtil.canInteract(entity, pos, world))
				cir.setReturnValue(VoxelShapes.empty());
		}
	}

	@Inject(method = "canReplace", at = @At("HEAD"), cancellable = true)
	public void canReplace(ItemPlacementContext context, CallbackInfoReturnable<Boolean> cir) {
		PlayerEntity player = context.getPlayer();
		if(FaeUtil.getDream(player) == 0) {
			if(FaeUtil.isDreamBlock(context.getBlockPos(), context.getWorld())) {
				cir.setReturnValue(true);
			}
		}
	}
	
	@ClientOnly
	@Inject(method = "getCameraCollisionShape", at = @At("HEAD"), cancellable = true)
	private void getCameraCollisionShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {

		if(context instanceof EntityShapeContext esc) {
			Entity entity = esc.getEntity();
			if(!FaeUtil.canInteract(entity, pos, world))
				cir.setReturnValue(VoxelShapes.empty());
		}
		
	}
	
	@Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    private void onEntityCollision(World world, BlockPos pos, Entity entity, CallbackInfo ci) {
		if(!FaeUtil.canInteract(entity, pos, world))
			ci.cancel();
    }
	@ClientOnly
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

	/*public boolean canPathfindThrough(BlockView world, BlockPos pos, NavigationType type) {
		return this.getBlock().canPathfindThrough(this.asBlockState(), world, pos, type);
	}*/

	//handles light passage for dream blocks
	@Inject(method = "getOpacity", at = @At("HEAD"), cancellable = true)
	private void getOpacity(BlockView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		if(world instanceof Chunk w && !(w instanceof EmptyChunk)) {
			if(FaeUtil.isDreamBlock(pos, world)) {
				cir.setReturnValue(0);
			}
		}
		else if(world instanceof World w) {
			if(FaeUtil.isDreamBlock(pos, world)) {
				cir.setReturnValue(0);
			}
		}
	}
	
	//is called after items are dropped, allowing ItemScattererMixin to detect its state
	@Inject(method = "onStacksDropped", at = @At("TAIL"), cancellable = false)
	public void onStacksDropped(ServerWorld world, BlockPos pos, ItemStack stack, boolean dropExperience, CallbackInfo ci) {
		FaeUtil.setDreamBlock(pos, false, world);
	}
	
	
	//Endless light wrangling below (Everything is just to get light to pass through dream blocks)
	
	//This method completely also bricks world generation apparently
	/*
	@Inject(method = "getCullingFace", at = @At("HEAD"), cancellable = true)
	private void getCullingFace(BlockView world, BlockPos pos, Direction direction, CallbackInfoReturnable<VoxelShape> cir) {
		if(FaeUtil.isDreamBlock(pos, world)) {
			cir.setReturnValue(VoxelShapes.empty());
		}
	}*/
	/*
	@Inject(method = "isTranslucent", at = @At("HEAD"), cancellable = true)
	private void isTranslucent(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if(FaeUtil.isDreamBlock(pos, world)) {
			cir.setReturnValue(true);
		}
	}*/
	/*
	@Inject(method = "getAmbientOcclusionLightLevel", at = @At("HEAD"), cancellable = true)
	private void getAmbientOcclusionLightLevel(BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
		if(FaeUtil.isDreamBlock(pos, world)) {
			cir.setReturnValue(1.0F);
		}
	}*/
	/*
	@Inject(method = "isSolidBlock", at = @At("HEAD"), cancellable = true)
	private void isSolidBlock(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if(FaeUtil.isDreamBlock(pos, world)) {
			cir.setReturnValue(false);
		}
	}
	*/
}
