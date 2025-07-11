package com.raggle.mixin.client;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.FaeUtil;
import com.raggle.FaeUtilClient;
import com.raggle.HalfDream;
import com.raggle.util.DreamState;

import net.fabricmc.fabric.impl.client.indigo.renderer.aocalc.AoCalculator;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

@Pseudo
@Mixin(AoCalculator.class)
public class AoCalculatorMixin {

    @Dynamic
    @Inject(method = "getLightmapCoordinates", at = @At(value = "RETURN", ordinal = 0), require = 0, cancellable = true, remap = false)
    private static void getLightmapCoordinates(BlockRenderView world, BlockState state, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
    	if(FaeUtilClient.getPlayerDream() == DreamState.ASLEEP) {
			int blockLight = world.getLightLevel(LightType.BLOCK, pos);
			blockLight = Math.max(blockLight/2, 0);
			World world2 = ((ChunkRendererRegionAccessor)world).getWorld();
			if(FaeUtil.isDreamAir(pos, world2)) {
				blockLight = 4;
			}

			cir.setReturnValue(0 << 20 | blockLight << 4);
		}
    }
}
