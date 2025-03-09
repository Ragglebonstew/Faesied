package com.raggle.half_dream.common.particles;

import com.raggle.half_dream.Faesied;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FaeParticles {
	public final static Identifier FLAME_1 = new Identifier(Faesied.MOD_ID, "flame1");
    public static final DefaultParticleType GREEN_FLAME = FabricParticleTypes.simple();

	public static void init() {
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(Faesied.MOD_ID, "green_flame"), GREEN_FLAME);
	}
}
