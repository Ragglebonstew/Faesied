package com.raggle.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.raggle.FaeUtilClient;
import com.raggle.HalfDream;
import com.raggle.util.DreamState;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
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

        if(FaeUtilClient.getPlayerDream() != DreamState.ASLEEP)
        	return;
        if (category == SoundCategory.WEATHER) {
            cir.setReturnValue(0.0F);
        }
        else if (id.getNamespace() != HalfDream.MOD_ID && (category == SoundCategory.NEUTRAL || category == SoundCategory.HOSTILE)) {
            cir.setReturnValue(0.0F);
        }

    }

}
