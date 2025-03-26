package com.raggle.half_dream.mixin.common;

import java.util.ArrayList;

import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;

import com.raggle.half_dream.api.DreamServerPlayer;
import com.raggle.half_dream.common.entity.FaeSkeleton;
import com.raggle.half_dream.networking.FaeMessaging;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements DreamServerPlayer {

	private final ArrayList<FaeSkeleton> skeletonList = new ArrayList<FaeSkeleton>();

	/*
	@Override
	public void syncDream() {
		ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(isDream());
		//ServerPlayNetworking.send(player, HDMessaging.DREAM_SYNC, buf);
		
		//syncing players on clients
		for(ServerPlayerEntity p : PlayerLookup.world(player.getServerWorld())) {
			FaeComponentRegistry.DREAM_ENTITY.sync(p);
		}
		//play sound effect
		player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_CONDUIT_AMBIENT, SoundCategory.BLOCKS, 0, 0);
	}
	*/
	
	@Override
	public void addToList(FaeSkeleton skeleton) {
		if(!skeletonList.contains(skeleton)) {
			skeletonList.add(skeleton);
			syncList();
		}
	}
	@Override
	public void removeFromList(FaeSkeleton skeleton) {
		if(!skeletonList.contains(skeleton)) {
			skeletonList.remove(skeleton);
			syncList();
		}
	}

	@Override
	public ArrayList<FaeSkeleton> getList(){
		return skeletonList;
	}

	
	private void syncList() {
		ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(skeletonList.size());
		ServerPlayNetworking.send(player, FaeMessaging.SKELETON_LIST_SIZE, buf);
	}

}
