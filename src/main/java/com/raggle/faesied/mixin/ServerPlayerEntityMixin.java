package com.raggle.faesied.mixin;

import java.util.ArrayList;

import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PlayerLookup;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;

import com.raggle.faesied.api.DreamServerPlayer;
import com.raggle.faesied.common.entity.FaeSkeleton;
import com.raggle.faesied.common.registry.FaeComponentRegistry;
import com.raggle.faesied.networking.FaeMessaging;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements DreamServerPlayer {

	private final ArrayList<FaeSkeleton> skeletonList = new ArrayList<FaeSkeleton>();

	@Override
	public void syncDream() {
		ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(isDream());
		//ServerPlayNetworking.send(player, HDMessaging.DREAM_SYNC, buf);
		
		//syncing players on clients
		/*
		for(ServerPlayerEntity p : PlayerLookup.world(player.getServerWorld())) {
			if(p == player) continue;
			PacketByteBuf buf2 = PacketByteBufs.create();
			buf2.write
			ServerPlayNetworking.send(p, HDMessaging.DREAM_SYNC, buf2);

		}
		*/
		for(ServerPlayerEntity p : PlayerLookup.world(player.getServerWorld())) {
			FaeComponentRegistry.DREAM_ENTITY.sync(p);
		}
		//play sound effect
		player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_CONDUIT_AMBIENT, SoundCategory.BLOCKS, 0, 0);
	}
	
	@Override
	public void setDream(boolean b) {
		getPersistantData().putBoolean(FaeComponentRegistry.DREAM_KEY, b);
		getPersistantData().putByte(FaeComponentRegistry.DREAMSTATE_KEY, b ? (byte)1 : 0);
		syncDream();
	}
	
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
