package com.raggle.faesied.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.faesied.common.FaeUtil;
import com.raggle.faesied.common.block.DreamBlock;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

@Mixin(World.class)
public abstract class WorldMixin {
	
	@Shadow public abstract BlockState getBlockState(BlockPos pos);
	
	@Inject(at = @At("TAIL"), method = "getTopY", cancellable = true)
    private void getTopY(Heightmap.Type heightmap, int x, int z, CallbackInfoReturnable<Integer> cir) {
        if (heightmap == Heightmap.Type.MOTION_BLOCKING) {
        	for(int i = cir.getReturnValueI()-1; i > -65; i--) {
        		BlockState state = getBlockState(new BlockPos(x, i, z));
                if ((state.isOpaque() || !state.getFluidState().isEmpty()) && !(state.getBlock() instanceof DreamBlock)) {
                    cir.setReturnValue(i+1);
                    break;
                }
        	}
        }
    }
	

    @Inject(method = "getRainGradient", at = @At("HEAD"), cancellable = true)
    public void getRainGradient(float delta, CallbackInfoReturnable<Float> cir) {
        if (FaeUtil.isPlayerDream()) {
        	cir.setReturnValue(0F);
        }
    }

    @Inject(method = "getThunderGradient", at = @At("HEAD"), cancellable = true)
    public void getThunderGradient(float delta, CallbackInfoReturnable<Float> cir) {
        if (FaeUtil.isPlayerDream()) {
        	cir.setReturnValue(0F);
        }
    }
}
