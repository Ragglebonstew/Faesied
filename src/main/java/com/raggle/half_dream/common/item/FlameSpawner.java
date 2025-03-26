package com.raggle.half_dream.common.item;

import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import com.raggle.half_dream.common.registry.FaeParticleRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FlameSpawner extends Item {

	public FlameSpawner() {
		super(new QuiltItemSettings());
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		
		BlockPos pos = context.getBlockPos().offset(context.getSide());
		if(world.isClient()) {
			world.addParticle(FaeParticleRegistry.INTERLOPER_MIST, pos.getX(), pos.getY(), pos.getZ(), 0, 0.05F, 0);
			//FaeParticles.spawnParticle(new Vector3d(pos.getX(), pos.getY(), pos.getZ()), FaeParticles.FLAME_1);
		}
		return ActionResult.success(true);
	}
}
