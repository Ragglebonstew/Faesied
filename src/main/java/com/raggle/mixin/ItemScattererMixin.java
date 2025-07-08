package com.raggle.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.raggle.FaeUtil;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.world.World;

@Mixin(ItemScatterer.class)
public class ItemScattererMixin {

	@Inject(method = "spawn(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
	private static void spawn(World world, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
		double d = (double)EntityType.ITEM.getWidth();
		double e = 1.0 - d;
		double f = d / 2.0;
		double g = Math.floor(x) + world.random.nextDouble() * e + f;
		double h = Math.floor(y) + world.random.nextDouble() * e;
		double i = Math.floor(z) + world.random.nextDouble() * e + f;

		while(!stack.isEmpty()) {
			ItemEntity itemEntity = new ItemEntity(world, g, h, i, stack.split(world.random.nextInt(21) + 10));
			itemEntity.setVelocity(
				world.random.nextTriangular(0.0, 0.11485000171139836),
				world.random.nextTriangular(0.2, 0.11485000171139836),
				world.random.nextTriangular(0.0, 0.11485000171139836)
			);
			world.spawnEntity(itemEntity);
			if(FaeUtil.isDreamBlock(itemEntity.getBlockPos(), world)) {
				FaeUtil.setDream(itemEntity, (byte)1);
			}
		}
		ci.cancel();
	}
}
