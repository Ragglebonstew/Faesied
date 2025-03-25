package com.raggle.half_dream.mixin.common;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.raggle.half_dream.common.FaeUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

@Mixin(Block.class)
public class BlockMixin {
	
	@Inject(method = "dropStack(Lnet/minecraft/world/World;Ljava/util/function/Supplier;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
	private static void dropStack(World world, Supplier<ItemEntity> itemEntitySupplier, ItemStack stack, CallbackInfo ci) {
		if (!world.isClient && !stack.isEmpty() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
			ItemEntity itemEntity = (ItemEntity)itemEntitySupplier.get();
			itemEntity.setToDefaultPickupDelay();
			world.spawnEntity(itemEntity);
			if(FaeUtil.isDreamBlock(itemEntity.getBlockPos(), world)) {
				FaeUtil.setDream(itemEntity, (byte)1);
			}
			ci.cancel();
		}
	}
}
