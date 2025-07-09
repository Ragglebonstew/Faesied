package com.raggle;

import java.util.Optional;

import com.raggle.api.DreamChunkComponent;
import com.raggle.api.DreamEntityComponent;
import com.raggle.api.DreamPlayerComponent;
import com.raggle.registry.FaeComponentRegistry;
import com.raggle.util.DreamState;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.ProtoChunk;

public class FaeUtil {
	
	public static DreamState getDreamState(Entity e) {
		Optional<DreamEntityComponent> op = FaeComponentRegistry.DREAM_ENTITY.maybeGet(e);
		if(op.isEmpty())
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
					HalfDream.LOGGER.debug("Adding "+pos.getX()+", "+pos.getY()+", "+pos.getZ()+" to "+key);
					return op.get().addPosToList(pos);
				}
				else {
					HalfDream.LOGGER.debug("Removing "+pos.getX()+", "+pos.getY()+", "+pos.getZ()+" to "+key);
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
		return componentContainsPos(pos, world, DREAM_TYPE.BLOCK);
	}
	

	public static boolean componentContainsPos(BlockPos pos, BlockView world, DREAM_TYPE type) {
		Chunk chunk = null;
		
		if(world instanceof ServerWorld w && w.getServer().isOnThread()) {
			chunk = w.getChunk(pos);
		}
		else if(world instanceof World w && w.isClient()) {
			chunk = w.getChunk(pos);
		}
		else if(world instanceof Chunk c) {
			chunk = c;
		}
		
		if(chunk != null) {
			return componentContainsPos(pos, chunk, type);
		}
		return false;
	}
	private static boolean componentContainsPos(BlockPos pos, Chunk chunk, DREAM_TYPE type) {

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
			Optional<DreamChunkComponent> op = key.maybeGet(chunk);
			if(op.isEmpty())
				return false;
			return op.get().contains(pos);
		}
		return false;
	}
	
	public static boolean canInteract(Entity e1, Entity e2) {
		DreamState d1 = getDreamState(e1);
		DreamState d2 = getDreamState(e2);
		//if(e1 != null && e2 != null)
			//Faesied.LOGGER.debug("Checking interaction between "+e1.getEntityName()+" and "+e2.getEntityName());
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
	
	public enum DREAM_TYPE {
		AIR,
		BLOCK
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