package com.raggle.client.sequence;

import com.raggle.FaeUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

@Environment(EnvType.CLIENT)
public class SequenceManager {

	private static DreamSequence dreamSequence;
	private static FogEffect fogEffect;

	public static void tick(MinecraftClient client) {
		if(client.isPaused())
			return;
		
		//fogEffect ticking
		if(fogEffect != null) {
			fogEffect.tick(client);
			if(fogEffect.finished) {
				fogEffect.stop();
				fogEffect = null;
			}
		}
		else if(FaeUtil.getDream(client.player) == 1) {
			setFogEffect(FogEffect.DREAM_FOG);
		}
			
		
		//sequence ticking
		if (hasSequence() && client.player != null) {
			dreamSequence.tick();
			if(dreamSequence.finished) {
				dreamSequence.stop();
				dreamSequence = null;
			}
		}
	}
	public static void render(DrawContext g, float tickDelta) {
		if(hasSequence())
			dreamSequence.render(g, tickDelta);
	}
	public static void start(DreamSequence newSequence) {
		if(!hasSequence()) {
			dreamSequence = newSequence;
			dreamSequence.start();
		}
	}
	public static boolean hasSequence() {
		return dreamSequence != null;
	}
	public static DreamSequence getSequence() {
		return dreamSequence;
	}
	public static boolean hasFogEffect() {
		return fogEffect != null;
	}
	public static void setFogEffect(FogEffect effect) {
		fogEffect = effect;
		fogEffect.start();
	}
	public static FogEffect getFogEffect() {
		return fogEffect;
	}
	public static boolean isCurrentSequenceImportant() {
		if(!hasSequence()) {
			return false;
		}
		return dreamSequence.isSequenceImportant();
	}
}