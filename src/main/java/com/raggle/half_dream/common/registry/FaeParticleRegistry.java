package com.raggle.half_dream.common.registry;

import com.raggle.half_dream.Faesied;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FaeParticleRegistry {
	
    public static final DefaultParticleType INTERLOPER_MIST = FabricParticleTypes.simple();
    public static final DefaultParticleType FALLEN_STAR = FabricParticleTypes.simple();
    public static final DefaultParticleType STAR_IMPACT_FLARE = FabricParticleTypes.simple();

	public static void init() {
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(Faesied.MOD_ID, "mist"), INTERLOPER_MIST);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(Faesied.MOD_ID, "fallen_star"), FALLEN_STAR);
	}
}
