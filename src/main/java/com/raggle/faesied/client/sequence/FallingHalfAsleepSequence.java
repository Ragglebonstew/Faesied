package com.raggle.faesied.client.sequence;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferRenderer;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.raggle.faesied.Faesied;
import com.raggle.faesied.api.DreamClientPlayer;

import net.minecraft.client.render.GameRenderer;

public class FallingHalfAsleepSequence extends DreamSequence {

	private boolean toDream;
	private boolean startDream;

	public FallingHalfAsleepSequence(DreamClientPlayer player, boolean startDream, boolean toDream) {
		this.startDream = startDream;
		this.toDream = toDream;
		ticks = 0;
		Faesied.LOGGER.info("Starting half asleep sequence");
	}
	@Override
	public void stop() {
		super.stop();
		Faesied.LOGGER.info("Stopping half asleep sequence");
	}

	@Override
	public boolean isSequenceImportant() {
		return true;
	}
	@Override
	public boolean getDreamState() {
		return hasTransitioned() ? toDream : startDream;
	}
	public boolean hasTransitioned() {
		return ticks >= totalLength/3;
	}
	public void setStartDream(boolean startDream) {
		this.startDream = startDream;
	}
	public void setEndDream(boolean endDream) {
		this.toDream = endDream;
	}
	public boolean getStartDream() {
		return startDream;
	}
	public boolean getEndDream() {
		return toDream;
	}
	@Override
	public void tick() {
		ticks++;
		
		if(ticks == totalLength/3) {
			client.worldRenderer.reload();
		}
		else if (ticks >= totalLength - 1) {
			finished = true;
		}
	}
	@Override
	public void render(float tickDelta) {

		//render dream fade in and out
		int width = client.getWindow().getScaledWidth();
		int height = client.getWindow().getScaledHeight();
		int backgroundProgress;
		int scaleNum = 255;
		if (ticks < totalLength/3) {
			backgroundProgress = scaleNum*ticks*3/(totalLength);
		} 
		else if(ticks > totalLength*2/3){
			backgroundProgress = (int)(scaleNum*(1 - (ticks*3.0F - totalLength*2)/(totalLength)));
		}
		else {
			backgroundProgress = scaleNum;
		}
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBufferBuilder();
		//RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		bufferBuilder.vertex(0.0D, height, 0.0D).color(0, 0, 0, backgroundProgress).next();
		bufferBuilder.vertex(width, height, 0.0D).color(0, 0, 0, backgroundProgress).next();
		bufferBuilder.vertex(width, 0.0D, -90.0D).color(0, 0, 0, backgroundProgress).next();
		bufferBuilder.vertex(0.0D, 0.0D, -90.0D).color(0, 0, 0, backgroundProgress).next();
		BufferRenderer.drawWithShader(bufferBuilder.end());
	}

}