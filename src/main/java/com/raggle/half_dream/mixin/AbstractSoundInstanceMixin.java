package com.raggle.half_dream.mixin;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.half_dream.Faesied;
import com.raggle.half_dream.common.FaeUtil;

import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

@ClientOnly
@Mixin(AbstractSoundInstance.class)
public abstract class AbstractSoundInstanceMixin {

    @Final
    @Shadow
    protected SoundCategory category;

    @Final
    @Shadow
    protected Identifier id;
   
    @Inject(method = "getVolume()F", at = @At("HEAD"), cancellable = true)
	private void getVolume(CallbackInfoReturnable<Float> cir) {
        Faesied.LOGGER.info("Disabling the sound: {}", id);

        if(!FaeUtil.isPlayerDream())
        	return;
        if (category == SoundCategory.WEATHER) {
            Faesied.LOGGER.debug("Disabling the sound: {}", id);
            cir.setReturnValue(0.0F);
        }
        else if (id.getNamespace() != Faesied.MOD_ID && (category == SoundCategory.NEUTRAL || category == SoundCategory.HOSTILE)) {
            Faesied.LOGGER.debug("Disabling the sound: {}", id);
            cir.setReturnValue(0.0F);
        }

    }

}
