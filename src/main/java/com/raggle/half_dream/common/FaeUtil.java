package com.raggle.half_dream.common;

import java.util.ArrayList;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.raggle.half_dream.api.DreamEntityComponent;
import com.raggle.half_dream.Faesied;
import com.raggle.half_dream.api.DreamChunkComponent;
import com.raggle.half_dream.common.registry.FaeComponentRegistry;
import com.raggle.half_dream.mixin.WorldRendererAccessor;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.ProtoChunk;

public class FaeUtil {
	
	public static byte getDream(Entity e) {
		Optional<DreamEntityComponent> op = FaeComponentRegistry.DREAM_ENTITY.maybeGet(e);
		if(op.isEmpty())
			return 0;
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
	
	public static boolean setDreamAir(BlockPos pos, boolean append, World world) {
		return setComponentPos(pos, append, world, FaeComponentRegistry.DREAM_AIR);
	}
	public static boolean setDreamBlock(BlockPos pos, boolean append, World world) {
		return setComponentPos(pos, append, world, FaeComponentRegistry.DREAM_BLOCKS);
	}
	private static boolean setComponentPos(BlockPos pos, boolean append, World world, ComponentKey<DreamChunkComponent> key) {
		if(world != null) {
			Chunk chunk = world.getChunk(pos);
			
			if(chunk == null)
				return false;
			else {
				Optional<DreamChunkComponent> op = key.maybeGet(chunk);
				if(op.isEmpty())
					return false;
				if(append) {
					Faesied.LOGGER.info("Adding "+pos.getX()+", "+pos.getY()+", "+pos.getZ()+" to "+key);
					return op.get().addPosToList(pos);
				}
				else {
					Faesied.LOGGER.info("Removing "+pos.getX()+", "+pos.getY()+", "+pos.getZ()+" to "+key);
					return op.get().removePosFromList(pos);
				}
			}
		}
		return false;
	}

	public static boolean isDreamAir(BlockPos pos, BlockView world) {
		return componentContainsPos(pos, world, DREAM_TYPE.AIR);
	}
	public static boolean isDreamBlock(BlockPos pos, BlockView world) {
		//Faesied.LOGGER.info("Passing dream type");
		return componentContainsPos(pos, world, DREAM_TYPE.BLOCK);
	}
	

	public static boolean componentContainsPos(BlockPos pos, BlockView world, DREAM_TYPE type) {
		Chunk chunk = null;
		
		if(world instanceof World w) {
			//Faesied.LOGGER.info("Got a world");
			chunk = w.getChunk(pos);
		}
		else if(world instanceof Chunk c) {
			//Faesied.LOGGER.info("Got a chunk");
			chunk = c;
		}
		
		if(chunk != null) {
			//Faesied.LOGGER.info("Passing chunk further down");
			return componentContainsPos(pos, chunk, type);
		}
		return false;
	}
	private static boolean componentContainsPos(BlockPos pos, Chunk chunk, DREAM_TYPE type) {

		//Faesied.LOGGER.info("type discriminating");
		if(type == DREAM_TYPE.AIR) {
			return componentContainsPos(pos, chunk, FaeComponentRegistry.DREAM_AIR);
		}
		else if(type == DREAM_TYPE.BLOCK) {
			return componentContainsPos(pos, chunk, FaeComponentRegistry.DREAM_BLOCKS);
		}
		return false;
	}
	private static boolean componentContainsPos(BlockPos pos, Chunk chunk, ComponentKey<DreamChunkComponent> key) {
		if(chunk != null && !(chunk instanceof EmptyChunk || chunk instanceof ProtoChunk)) {
			//Faesied.LOGGER.info("getting chunk from component");
			Optional<DreamChunkComponent> op = key.maybeGet(chunk);
			if(op.isEmpty())
				return false;
			//Faesied.LOGGER.info("checking if contained");
			return op.get().contains(pos);
		}
		return false;
	}
	
	public static boolean canInteract(Entity e1, Entity e2) {
		byte d1 = getDream(e1);
		byte d2 = getDream(e2);
		return d1 == 2 || d2 == 2 || d1 == d2;
	}
	public static boolean canInteract(Entity entity, BlockPos pos, BlockView world) {
		byte dream = getDream(entity);
		if(dream == 1) {
			return !isDreamAir(pos, world);
		}
		else if(dream == 0) {
			return !isDreamBlock(pos, world);
		}
		return true;
	}
	
	public enum DREAM_TYPE {
		AIR,
		BLOCK
	}
	
	@Deprecated
	@Nullable
	@ClientOnly
	public static ClientPlayerEntity getClientPlayer() {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc == null)
			return null;
		return mc.player;
	}
	@Deprecated
	@Nullable
	@ClientOnly
	public static ClientWorld getClientWorld() {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc == null)
			return null;
		return mc.world;
	}
	@Deprecated
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
	@Deprecated
	@ClientOnly
	public static byte getPlayerDream() {
		ClientPlayerEntity player = getClientPlayer();
		if(player == null)
			return 1;
		return getDream(getClientPlayer());
	}
	@Deprecated
	@ClientOnly
	public static boolean canPlayerInteract(BlockPos pos) {
		return canInteract(getClientPlayer(), pos, getClientWorld());
	}
	@Deprecated
	@ClientOnly
	public static void scheduleChunkRenderAt(BlockPos pos) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc != null && mc.world != null) {
			ChunkSectionPos chunkPos = ChunkSectionPos.from(pos);
			((WorldRendererAccessor)mc.worldRenderer).invokeScheduleChunkRender(chunkPos.getSectionX(), chunkPos.getSectionY(), chunkPos.getSectionZ(), true);;
		}
	}
	@Deprecated
	@ClientOnly
	public static void scheduleChunkRenderAt(int x, int y, int z) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if(mc.world != null) {
			//mc.worldRenderer.scheduleBlockRender(x, y, z);
			((WorldRendererAccessor)mc.worldRenderer).invokeScheduleChunkRender(x, y, z, true);;
		}
	}
	//coding gods forgive me, for I have written jank (marks replaced dream blocks to be removed from list)
	private static ArrayList<BlockPos> marks = new ArrayList<BlockPos>();
	public static boolean getMarked(BlockPos pos) {
		return marks.remove(pos);
	}
	public static void addMarked(BlockPos blockPos) {
		marks.add(blockPos);
	}
	
}