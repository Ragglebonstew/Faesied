package com.raggle.half_dream.common;

import java.util.Optional;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.raggle.half_dream.api.DreamEntityComponent;
import com.raggle.half_dream.api.DreamlessComponent;
import com.raggle.half_dream.common.registry.FaeComponentRegistry;
import com.raggle.half_dream.mixin.WorldRendererAccessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class FaeUtil {
	
	@Deprecated
	public static boolean isDream(Entity e) {
		Optional<DreamEntityComponent> op = FaeComponentRegistry.DREAM_ENTITY.maybeGet(e);
		if(op.isEmpty())
			return false;
		return op.get().isDream();
	}
	public static byte getDream(Entity e) {
		Optional<DreamEntityComponent> op = FaeComponentRegistry.DREAM_ENTITY.maybeGet(e);
		if(op.isEmpty())
			return -1;
		return op.get().getDream();
	}
	public static void setDream(Entity e, byte b) {
		Optional<DreamEntityComponent> op = FaeComponentRegistry.DREAM_ENTITY.maybeGet(e);
		if(op.isEmpty())
			return;
		op.get().setDream(b);
	}
	public static void toggleDream(Entity e) {
		byte dream = FaeUtil.getDream(e);
		if(dream == 0)
			FaeUtil.setDream(e, (byte)1);
		else if(dream == 1)
			FaeUtil.setDream(e, (byte)0);
	}

	@ClientOnly
	public static boolean isDreamAir(BlockPos pos) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc != null && mc.world != null) {
			return isDreamAir(pos, mc.world);
		}
		return false;
	}
	@ClientOnly
	public static boolean isDreamBlock(BlockPos pos) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc != null && mc.world != null) {
			return isDreamBlock(pos, mc.world);
		}
		return false;
	}
	
	public static boolean setDreamAir(BlockPos pos, boolean dreamless, World world) {
		if(world != null) {
			Chunk chunk = world.getChunk(pos);
			
			if(chunk == null)
				return false;
			else {
				Optional<DreamlessComponent> op = FaeComponentRegistry.DREAM_AIR.maybeGet(chunk);
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
	public static boolean setDreamBlock(BlockPos pos, boolean dreamless, World world) {
		if(world != null) {
			Chunk chunk = world.getChunk(pos);
			
			if(chunk == null)
				return false;
			else {
				Optional<DreamlessComponent> op = FaeComponentRegistry.DREAM_BLOCKS.maybeGet(chunk);
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

	public static boolean isDreamAir(BlockPos pos, World world) {
		if(world != null) {
			Optional<DreamlessComponent> op = FaeComponentRegistry.DREAM_AIR.maybeGet(world.getChunk(pos));
			if(op.isEmpty())
				return false;
			return op.get().contains(pos);
		}
		return false;
	}
	public static boolean isDreamBlock(BlockPos pos, World world) {
		if(world != null) {
			Optional<DreamlessComponent> op = FaeComponentRegistry.DREAM_BLOCKS.maybeGet(world.getChunk(pos));
			if(op.isEmpty())
				return false;
			return op.get().contains(pos);
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
	public static boolean canInteract(Entity entity, BlockPos pos, World world) {
		byte dream = getDream(entity);
		if(dream == 1) {
			return !isDreamAir(pos, world);
		}
		else if(dream == 0) {
			return !isDreamBlock(pos, world);
		}
		return true;
	}
	
	@ClientOnly
	public static ClientPlayerEntity getClientPlayer() {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc == null)
			return null;
		return mc.player;
	}
	@ClientOnly
	public static ClientWorld getClientWorld() {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc == null)
			return null;
		return mc.world;
	}
	@ClientOnly
	public static boolean hasClientPlayer() {
		return getClientPlayer() != null;
	}

	@ClientOnly
	public static boolean isPlayerDream() {
		ClientPlayerEntity player = getClientPlayer();
		if(player == null)
			return false;
		return getDream(getClientPlayer()) == 1;
	}
	@ClientOnly
	public static byte getPlayerDream() {
		ClientPlayerEntity player = getClientPlayer();
		if(player == null)
			return -1;
		return getDream(getClientPlayer());
	}
	@ClientOnly
	public static boolean canPlayerInteract(BlockPos pos) {
		return canInteract(getClientPlayer(), pos, getClientWorld());
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
	
}