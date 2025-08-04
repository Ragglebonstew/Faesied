package com.raggle;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.raggle.api.DreamChunkComponent;
import com.raggle.api.DreamEntityComponent;
import com.raggle.api.DreamPlayerComponent;
import com.raggle.registry.FaeComponentRegistry;
import com.raggle.util.DreamState;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class FaeUtil {
	
	public static DreamState getDreamState(Entity e) {
		Optional<DreamEntityComponent> op = FaeComponentRegistry.DREAM_ENTITY.maybeGet(e);
		if(op.isEmpty() || op.get().getDream() == null)
			return DreamState.AWAKE;
		return op.get().getDream();
	}
	public static void setDream(Entity e, DreamState b) {
		Optional<DreamEntityComponent> op = FaeComponentRegistry.DREAM_ENTITY.maybeGet(e);
		if(op.isEmpty())
			return;
		op.get().setDream(b);
	}
	public static void toggleDream(Entity e) {
		DreamState dream = FaeUtil.getDreamState(e);
		if(dream == DreamState.AWAKE)
			FaeUtil.setDream(e, DreamState.ASLEEP);
		else if(dream == DreamState.ASLEEP)
			FaeUtil.setDream(e, DreamState.AWAKE);
	}
	
	public static boolean setDreamAir(BlockPos pos, boolean append, BlockView world) {
		return setComponentPos(pos, append, world, FaeComponentRegistry.DREAM_AIR);
	}
	public static boolean setDreamBlock(BlockPos pos, boolean append, BlockView world) {
		return setComponentPos(pos, append, world, FaeComponentRegistry.DREAM_BLOCKS);
	}
	public static boolean isDreamAir(BlockPos pos, BlockView world) {
		return componentContainsPos(pos, world, FaeComponentRegistry.DREAM_AIR);
	}
	public static boolean isDreamBlock(BlockPos pos, BlockView world) {
		return componentContainsPos(pos, world, FaeComponentRegistry.DREAM_BLOCKS);
	}
	private static boolean setComponentPos(BlockPos pos, boolean append, BlockView world, ComponentKey<DreamChunkComponent> key) {
		DreamChunkComponent dreamChunk = getDreamChunkComponent(pos, world, key);
		if(dreamChunk != null) {
			if(append) {
				HalfDream.LOGGER.debug("Adding "+pos.getX()+", "+pos.getY()+", "+pos.getZ()+" to "+key);
				return dreamChunk.addPosToList(pos);
			}
			else {
				HalfDream.LOGGER.debug("Removing "+pos.getX()+", "+pos.getY()+", "+pos.getZ()+" to "+key);
				return dreamChunk.removePosFromList(pos);
			}	
		}
		return false;
	}
	public static boolean componentContainsPos(BlockPos pos, BlockView world, ComponentKey<DreamChunkComponent> key) {
		DreamChunkComponent dreamChunk = getDreamChunkComponent(pos, world, key);
		if(dreamChunk != null) {
			return dreamChunk.contains(pos);
		}
		return false;
	}
	private static @Nullable DreamChunkComponent getDreamChunkComponent(BlockPos pos, @Nullable BlockView world, ComponentKey<DreamChunkComponent> key) {
		Chunk chunk = null;
		
		if(world instanceof World w) {
			chunk = w.getChunk(pos);
		}
		else if(world instanceof Chunk c) {
			chunk = c;
		}
		Optional<DreamChunkComponent> op = key.maybeGet(chunk);
		if(op.isEmpty())
			return null;
		return op.get();
	}
	public static boolean queueDreamBlock(BlockPos pos, World world) {
		DreamChunkComponent dreamChunk = getDreamChunkComponent(pos, world, FaeComponentRegistry.DREAM_BLOCKS);
		if(dreamChunk != null) {
			return dreamChunk.addPosToQueue(pos);
		}
		return false;
	}
	public static boolean pushDreamBlock(BlockPos pos, World world) {
		DreamChunkComponent dreamChunk = getDreamChunkComponent(pos, world, FaeComponentRegistry.DREAM_BLOCKS);
		if(dreamChunk != null) {
			return dreamChunk.pushPosFromQueue(pos);
		}
		return false;
	}
	
	public static boolean canInteract(Entity e1, Entity e2) {
		DreamState d1 = getDreamState(e1);
		DreamState d2 = getDreamState(e2);
		return d1 == DreamState.DUAL || d2 == DreamState.DUAL || d1 == d2;
	}
	public static boolean canInteract(Entity entity, BlockPos pos, BlockView world) {
		DreamState dream = getDreamState(entity);
		if(dream == DreamState.ASLEEP) {
			return !isDreamAir(pos, world);
		}
		else if(dream == DreamState.AWAKE) {
			return !isDreamBlock(pos, world);
		}
		return true;
	}
	
	public static boolean isInterloped(PlayerEntity player) {
		Optional<DreamPlayerComponent> op = FaeComponentRegistry.DREAM_PLAYER.maybeGet(player);
		if(op.isEmpty())
			return false; 
		return op.get().isInterloped();
	}
	public static boolean setInterlope(PlayerEntity player, boolean value) {
		Optional<DreamPlayerComponent> op = FaeComponentRegistry.DREAM_PLAYER.maybeGet(player);
		if(op.isEmpty())
			return false;
		op.get().setInterlope(value);
		return true;
	}
	
}