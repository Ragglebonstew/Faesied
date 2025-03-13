package com.raggle.half_dream.mixin;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.half_dream.common.FaeUtil;

import net.minecraft.client.render.DimensionVisualEffects;

@ClientOnly
@Mixin(DimensionVisualEffects.class)
public class DimensionVisualEffectsMixin {


}
