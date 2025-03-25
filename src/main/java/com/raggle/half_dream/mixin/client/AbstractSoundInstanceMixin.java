package com.raggle.half_dream.mixin.client;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.half_dream.Faesied;
import com.raggle.half_dream.client.FaeUtilClient;
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

        if(FaeUtilClient.getPlayerDream() != 1)
        	return;
        if (category == SoundCategory.WEATHER) {
            cir.setReturnValue(0.0F);
        }
        else if (id.getNamespace() != Faesied.MOD_ID && (category == SoundCategory.NEUTRAL || category == SoundCategory.HOSTILE)) {
            cir.setReturnValue(0.0F);
        }

    }

}
