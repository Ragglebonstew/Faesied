package com.raggle.half_dream.client.sequence;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferRenderer;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.raggle.half_dream.client.FaeUtilClient;
import com.raggle.half_dream.common.FaeUtil;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.render.GameRenderer;

public class FallingHalfAsleepSequence extends DreamSequence {

	public FallingHalfAsleepSequence() {
		ticks = 0;
	}
	@Override
	public void stop() {
		super.stop();
	}

	@Override
	public boolean isSequenceImportant() {
		return true;
	}
	public boolean hasTransitioned() {
		return ticks >= totalLength/3;
	}
	public void tick() {
		ticks++;
		
		if(ticks == totalLength/3) {
			//switch dream state
			FaeUtil.toggleDream(FaeUtilClient.getClientPlayer());
		}
		else if (ticks >= totalLength - 1) {
			finished = true;
		}
	}
	@Override
	public void render(GuiGraphics g, float tickDelta) {

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
		RenderSystem.disableBlend();
	}

}