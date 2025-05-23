package com.raggle.half_dream.mixin.client;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.half_dream.client.FaeUtilClient;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@ClientOnly
@Mixin(World.class)
public abstract class WorldMixin {
	
	@Shadow public abstract BlockState getBlockState(BlockPos pos);
	@Shadow public abstract boolean isClient();
	
	/*
	@Inject(at = @At("TAIL"), method = "getTopY", cancellable = true)
    private void getTopY(Heightmap.Type heightmap, int x, int z, CallbackInfoReturnable<Integer> cir) {
        if (heightmap == Heightmap.Type.MOTION_BLOCKING) {
        	for(int i = cir.getReturnValueI()-1; i > -65; i--) {
        		BlockPos pos = new BlockPos(x, i, z);
        		BlockState state = getBlockState(pos);
                if ((state.isOpaque() || !state.getFluidState().isEmpty()) && !FaeUtil.isDreamBlock(pos, (World)(Object)this)) {
                    cir.setReturnValue(i+1);
                    break;
                }
        	}
        }
    }
    */
	

    @Inject(method = "getRainGradient", at = @At("HEAD"), cancellable = true)
    public void getRainGradient(float delta, CallbackInfoReturnable<Float> cir) {
        if (this.isClient() && FaeUtilClient.getPlayerDream() == 1) {
        	cir.setReturnValue(0F);
        }
    }

    @Inject(method = "getThunderGradient", at = @At("HEAD"), cancellable = true)
    public void getThunderGradient(float delta, CallbackInfoReturnable<Float> cir) {
        if (this.isClient() && FaeUtilClient.getPlayerDream() == 1) {
        	cir.setReturnValue(0F);
        }
    }
    
	//Hides blocks in renderer
	@Inject(method = "getBlockState", at = @At("HEAD"), cancellable = true)
	private void getBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
		if(this.isClient() && !FaeUtilClient.canPlayerInteract(pos)){
			cir.setReturnValue(Blocks.AIR.getDefaultState());
		}
	}
    
}
