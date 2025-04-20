package com.raggle.half_dream.client.block;

import org.joml.Matrix4f;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferRenderer;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.raggle.half_dream.client.FaeUtilClient;
import com.raggle.half_dream.common.block.InterloperPortalBlock;
import com.raggle.half_dream.common.block.block_entity.InterloperBlockEntity;
import com.raggle.half_dream.common.registry.FaeBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@ClientOnly
public class InterloperPortalEntityRenderer<T extends InterloperBlockEntity> implements BlockEntityRenderer<T> {
	
	private final int range = 10*10;
	private int count = 0;

	public InterloperPortalEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

	@Override
	public void render(T entity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		
		World world = entity.getWorld();
		ClientPlayerEntity player = FaeUtilClient.getClientPlayer();
		double sdistance = player.squaredDistanceTo(entity.getPos().getX(),entity.getPos().getY(), entity.getPos().getZ());
		
		if(world == null 
				|| player == null
				|| !entity.getCachedState().get(InterloperPortalBlock.ACTIVE)
				|| sdistance > range
		) {
			return;
		}

		if(count < 60) {
			count += 1;
		}
		else {
			count = 0;
		}
		
		if(count%20 == 0) {
			//world.addParticle(FaeParticleRegistry.FALLEN_STAR, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0, 0F, 0);
			//world.addParticle(FaeParticleRegistry.FALLEN_STAR, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0, 0F, 0);
		}

		
		RenderSystem.setShaderTexture(0, new Identifier("textures/entity/end_portal.png"));
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		RenderSystem.enableBlend();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBufferBuilder();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);

		Matrix4f matrix4f = matrixStack.peek().getModel();
		float alpha = 1.0F - (float)(sdistance/range);

		this.renderInsides(entity, matrix4f, bufferBuilder, alpha);
		//this.renderSides(entity, matrix4f, bufferBuilder, alpha);
		
		
		
		BufferRenderer.drawWithShader(bufferBuilder.end());
		RenderSystem.disableBlend();
	}
	
	private void renderInsides(T entity, Matrix4f matrix4f, VertexConsumer vertexConsumer, float alpha) {
		float g = 0.9999F;
		float f = 0.0001F;
		this.renderInside(entity, matrix4f, vertexConsumer, f, g, f, g, g, g, g, g, alpha, Direction.SOUTH);
		this.renderInside(entity, matrix4f, vertexConsumer, f, g, g, f, f, f, f, f, alpha, Direction.NORTH);
		this.renderInside(entity, matrix4f, vertexConsumer, g, g, g, f, f, g, g, f, alpha, Direction.EAST);
		this.renderInside(entity, matrix4f, vertexConsumer, f, f, f, g, f, g, g, f, alpha, Direction.WEST);
		this.renderInside(entity, matrix4f, vertexConsumer, f, g, f, f, f, f, g, g, alpha, Direction.DOWN);
		this.renderInside(entity, matrix4f, vertexConsumer, f, g, g, g, g, g, f, f, alpha, Direction.UP);
	}
	/*
	private void renderSides(T entity, Matrix4f matrix, VertexConsumer vertexConsumer, float alpha) {
		this.renderSide(entity, matrix, vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, alpha, Direction.SOUTH);
		this.renderSide(entity, matrix, vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, alpha, Direction.NORTH);
		this.renderSide(entity, matrix, vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, alpha, Direction.EAST);
		this.renderSide(entity, matrix, vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, alpha, Direction.WEST);
		this.renderSide(entity, matrix, vertexConsumer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, alpha, Direction.DOWN);
		this.renderSide(entity, matrix, vertexConsumer, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, alpha, Direction.UP);
	}

	private void renderSide(
		T entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, float alpha, Direction side
	) {
		if (entity.shouldDrawSide(side)){
			vertices.vertex(model, x1, y1, z1).color(1.0F, 1.0F, 1.0F, alpha).uv(0, 0).next();
			vertices.vertex(model, x2, y1, z2).color(1.0F, 1.0F, 1.0F, alpha).uv(0, 1).next();
			vertices.vertex(model, x2, y2, z3).color(1.0F, 1.0F, 1.0F, alpha).uv(1, 1).next();
			vertices.vertex(model, x1, y2, z4).color(1.0F, 1.0F, 1.0F, alpha).uv(1, 0).next();
		}
	}
	*/
	private void renderInside(
			T entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, float alpha, Direction side
	) {
		BlockState state = entity.getWorld().getBlockState(entity.getPos().offset(side));
		if (!state.isOf(FaeBlockRegistry.INTERLOPER_PORTAL_BLOCK)){
			vertices.vertex(model, x1, y2, z4).color(1.0F, 1.0F, 1.0F, alpha).uv(0, 0).next();
			vertices.vertex(model, x2, y2, z3).color(1.0F, 1.0F, 1.0F, alpha).uv(0, 1).next();
			vertices.vertex(model, x2, y1, z2).color(1.0F, 1.0F, 1.0F, alpha).uv(1, 1).next();
			vertices.vertex(model, x1, y1, z1).color(1.0F, 1.0F, 1.0F, alpha).uv(1, 0).next();
		}
	}

	protected RenderLayer getLayer() {
		return RenderLayer.getWaterMask();
	}
}
