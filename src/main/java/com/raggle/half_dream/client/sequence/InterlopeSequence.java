package com.raggle.half_dream.client.sequence;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferRenderer;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

public class InterlopeSequence extends DreamSequence {

	@Override
	public void tick() {
		ticks++;
		
		if(ticks == totalLength/2) {
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
		if(ticks > totalLength/2){
			backgroundProgress = (int)(scaleNum*(2 - (2.0F*ticks/totalLength)));
		}
		else {
			backgroundProgress = scaleNum;
		}
		
		backgroundProgress = (int)(scaleNum*(1.0F-Math.abs((ticks*2.0F-totalLength)/totalLength)));


		RenderSystem.setShaderTexture(0, new Identifier("textures/entity/end_portal.png"));
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		RenderSystem.enableBlend();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBufferBuilder();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
		
		if(ticks < totalLength/2) {
			backgroundProgress = scaleNum;
		}
		bufferBuilder.vertex(0.0D, height, 0.0D).color(255, 255, 255, backgroundProgress).uv(0, 0).next();
		bufferBuilder.vertex(width, height, 0.0D).color(255, 255, 255, backgroundProgress).uv(0, 1).next();
		bufferBuilder.vertex(width, 0.0D, -90.0D).color(255, 255, 255, backgroundProgress).uv(1, 1).next();
		bufferBuilder.vertex(0.0D, 0.0D, -90.0D).color(255, 255, 255, backgroundProgress).uv(1, 0).next();
		BufferRenderer.drawWithShader(bufferBuilder.end());
		RenderSystem.disableBlend();
		

		
		
	}
}
