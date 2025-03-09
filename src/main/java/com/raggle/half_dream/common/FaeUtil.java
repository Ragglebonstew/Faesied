package com.raggle.half_dream.common;

import java.util.Optional;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.raggle.half_dream.api.DreamClientPlayer;
import com.raggle.half_dream.api.DreamEntityComponent;
import com.raggle.half_dream.api.DreamlessComponent;
import com.raggle.half_dream.common.block.DreamBlock;
import com.raggle.half_dream.common.registry.FaeComponentRegistry;
import com.raggle.half_dream.mixin.WorldRendererAccessor;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.quasar.particle.ParticleEmitter;
import foundry.veil.api.quasar.particle.ParticleSystemManager;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class FaeUtil {
	
	public static boolean isDream(Entity e) {
		return FaeComponentRegistry.DREAM_ENTITY.get(e).isDream();
	}

	@ClientOnly
	public static boolean isDreamless(BlockPos pos) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc != null && mc.world != null) {
			return isDreamless(pos, mc.world);
		}
		return false;
	}
	public static boolean isDreamless(BlockPos pos, World world) {
		if(world != null) {
			Chunk chunk = world.getChunk(pos);
			Block block = chunk.getBlockState(pos).getBlock();
			if(block instanceof DreamBlock)
				return false;
			if(chunk != null) {
				Optional<DreamlessComponent> op = FaeComponentRegistry.DREAMLESS.maybeGet(chunk);
				if(op.isEmpty())
					return false;
				return op.get().isDreamless(pos);
			}
		}
		return false;
	}
	public static boolean setDreamless(BlockPos pos, boolean dreamless, World world) {
		if(world != null) {
			Chunk chunk = world.getChunk(pos);
			
			if(chunk == null || chunk.getBlockState(pos).getBlock() instanceof DreamBlock)
				return false;
			else {
				Optional<DreamlessComponent> op = FaeComponentRegistry.DREAMLESS.maybeGet(chunk);
				if(op.isEmpty())
					return false;
				if(dreamless)
					return op.get().addPosToList(pos);
				else
					return op.get().removePosFromList(pos);
			}
		}
		return false;
	}
	public static boolean canInteract(Entity e1, Entity e2) {
		if(e1 instanceof DreamEntityComponent d1 && e2 instanceof DreamEntityComponent d2) {
			byte s1 = d1.getDream();
			byte s2 = d2.getDream();
			if(s1 == 2 || s2 == 2 || s1 == s2)
				return true;
		}
		return false;
	}
	@ClientOnly
	public static ClientPlayerEntity getClientPlayer() {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc == null)
			return null;
		return mc.player;
	}
	@ClientOnly
	public static boolean hasClientPlayer() {
		return getClientPlayer() != null;
	}
	@ClientOnly
	public static boolean isPlayerDream() {
		return getClientPlayer() instanceof DreamClientPlayer dcp && dcp.isDream();
	}
	@ClientOnly
	public static void scheduleChunkRenderAt(BlockPos pos) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc != null && mc.world != null) {
			ChunkSectionPos chunkPos = ChunkSectionPos.from(pos);
			((WorldRendererAccessor)mc.worldRenderer).invokeScheduleChunkRender(chunkPos.getSectionX(), chunkPos.getSectionY(), chunkPos.getSectionZ(), true);;
		}
	}
	@ClientOnly
	public static void scheduleChunkRenderAt(int x, int y, int z) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc.world != null) {
			//mc.worldRenderer.scheduleBlockRender(x, y, z);
			((WorldRendererAccessor)mc.worldRenderer).invokeScheduleChunkRender(x, y, z, true);;
		}
	}
	
	
	//quasar
	
	public static void spawnParticle(Entity entity, Identifier id){
	    try {
	        ParticleSystemManager manager = VeilRenderSystem.renderer().getParticleManager();
	        ParticleEmitter emitter = manager.createEmitter(id);
	        emitter.setAttachedEntity(entity);
	        manager.addParticleSystem(emitter);
	    } catch (Exception ignored) {

	    }
	}
	public static void spawnParticle(Vec3d position, Identifier id){
	    try {
	        ParticleSystemManager manager = VeilRenderSystem.renderer().getParticleManager();
	        ParticleEmitter emitter = manager.createEmitter(id);
	        emitter.setPosition(position);
	        manager.addParticleSystem(emitter);
	    } catch (Exception ignored) {

	    }
	}
	
}