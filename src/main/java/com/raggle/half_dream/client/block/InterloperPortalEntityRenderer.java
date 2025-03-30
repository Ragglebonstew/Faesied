package com.raggle.half_dream.client.block;

import org.joml.Matrix4f;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.raggle.half_dream.common.block.InterloperPortalBlock;
import com.raggle.half_dream.common.block.block_entity.InterloperBlockEntity;
import com.raggle.half_dream.common.registry.FaeBlockRegistry;
import com.raggle.half_dream.common.registry.FaeParticleRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@ClientOnly
public class InterloperPortalEntityRenderer<T extends InterloperBlockEntity> implements BlockEntityRenderer<T> {
	
	private int count = 0;

	public InterloperPortalEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

	@Override
	public void render(T entity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		
		World world = entity.getWorld();
		
		if(world == null 
				|| !entity.getCachedState().get(InterloperPortalBlock.ACTIVE)
		) {
			return;
		}
		BlockPos pos = entity.getPos();
		
		if(count < 60) {
			count += 1;
		}
		else {
			count = 0;
		}
		
		if(count%20 == 0) {
			world.addParticle(FaeParticleRegistry.FALLEN_STAR, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0, 0F, 0);
			world.addParticle(FaeParticleRegistry.FALLEN_STAR, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0, 0F, 0);
		}

		Matrix4f matrix4f = matrixStack.peek().getModel();
		this.renderSides(entity, matrix4f, vertexConsumerProvider.getBuffer(this.getLayer()));
		this.renderInsides(entity, matrix4f, vertexConsumerProvider.getBuffer(this.getLayer()));
	}
	
	private void renderInsides(T entity, Matrix4f matrix4f, VertexConsumer vertexConsumer) {
		float g = 0.999F;
		float f = 0.001F;
		this.renderInside(entity, matrix4f, vertexConsumer, f, g, f, g, g, g, g, g, Direction.SOUTH);
		this.renderInside(entity, matrix4f, vertexConsumer, f, g, g, f, f, f, f, f, Direction.NORTH);
		this.renderInside(entity, matrix4f, vertexConsumer, g, g, g, f, f, g, g, f, Direction.EAST);
		this.renderInside(entity, matrix4f, vertexConsumer, f, f, f, g, f, g, g, f, Direction.WEST);
		this.renderInside(entity, matrix4f, vertexConsumer, f, g, f, f, f, f, g, g, Direction.DOWN);
		this.renderInside(entity, matrix4f, vertexConsumer, f, g, g, g, g, g, f, f, Direction.UP);
	}
	private void renderSides(T entity, Matrix4f matrix, VertexConsumer vertexConsumer) {
		this.renderSide(entity, matrix, vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
		this.renderSide(entity, matrix, vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
		this.renderSide(entity, matrix, vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
		this.renderSide(entity, matrix, vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
		this.renderSide(entity, matrix, vertexConsumer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
		this.renderSide(entity, matrix, vertexConsumer, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
	}

	private void renderSide(
		T entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction side
	) {
		if (entity.shouldDrawSide(side)){
			vertices.vertex(model, x1, y1, z1).next();
			vertices.vertex(model, x2, y1, z2).next();
			vertices.vertex(model, x2, y2, z3).next();
			vertices.vertex(model, x1, y2, z4).next();
		}
	}
	private void renderInside(
			T entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction side
	) {
		BlockState state = entity.getWorld().getBlockState(entity.getPos().offset(side));
		if (!state.isOf(FaeBlockRegistry.INTERLOPER_PORTAL_BLOCK)){
			vertices.vertex(model, x1, y2, z4).next();
			vertices.vertex(model, x2, y2, z3).next();
			vertices.vertex(model, x2, y1, z2).next();
			vertices.vertex(model, x1, y1, z1).next();
		}
	}

	protected RenderLayer getLayer() {
		return RenderLayer.getEndGateway();
	}
}
