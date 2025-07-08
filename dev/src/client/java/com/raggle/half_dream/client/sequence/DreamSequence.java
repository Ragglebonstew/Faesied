package com.raggle.half_dream.client.sequence;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

@Environment(EnvType.CLIENT)
public class DreamSequence {
	
	protected static MinecraftClient client = MinecraftClient.getInstance();

	protected int ticks;
	protected int totalLength = 60;
	protected boolean finished;
	protected boolean cancelled;
	
	public boolean isSequenceImportant() {
		return false;
	}
	public void start() {}
	public void stop() {}
	public void cancel() {
		this.cancelled = true;
	}
	
	public void tick() {}
	public void render(DrawContext g, float tickDelta) {}
	
}