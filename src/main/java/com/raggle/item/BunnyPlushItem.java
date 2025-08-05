package com.raggle.item;

import com.raggle.FaeUtil;
import com.raggle.networking.FaeMessaging;
import com.raggle.util.DreamState;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class BunnyPlushItem extends BlockItem {

	public BunnyPlushItem(Block block, Settings settings) {
		super(block, settings);
	}
	

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if(selected && !world.isClient() && entity instanceof ServerPlayerEntity player) {
			if(player.isSleeping() && player.getSleepTimer() >= 100) {
				FaeUtil.setInterlope(player, true);
				player.wakeUp();
			}
			else if(player.isSubmergedInWater() && player.getWorld().isNight() && FaeUtil.isInterloped(player) && FaeUtil.getDreamState(player) == DreamState.AWAKE) {
				FaeUtil.setInterlope(player, false);
				ServerPlayNetworking.send(player, FaeMessaging.FALLING_ASLEEP, PacketByteBufs.empty());
			}
			else if(player.isSubmergedInWater() && isSunrise(player) && FaeUtil.getDreamState(player) == DreamState.ASLEEP) {
				FaeUtil.setInterlope(player, false);
				ServerPlayNetworking.send(player, FaeMessaging.FALLING_ASLEEP, PacketByteBufs.empty());
			}
		}
	}
	
	public boolean isSunrise(PlayerEntity player) {
		long time = player.getWorld().getTimeOfDay();
		return time >= 23000;
	}

	public boolean isSunset(PlayerEntity player) {
		long time = player.getWorld().getTimeOfDay();
		return time >= 12000 && time <= 12786;
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BLOCK;
	}

}
