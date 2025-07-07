package com.raggle.half_dream.common.registry;

import com.raggle.half_dream.Faesied;
import com.raggle.half_dream.common.entity.FaeSkeleton;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FaeEntityRegistry {

    public static final EntityType<FaeSkeleton> HDSKELETON = Registry.register(
    		Registries.ENTITY_TYPE,
            new Identifier(Faesied.MOD_ID, "hdskeleton"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, FaeSkeleton::new).build()
    );
    
    public static void init() {
    	
        //FabricDefaultAttributeRegistry.register(HDSKELETON, FaeSkeleton.createAttributes());
        //BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biome.PLAINS), SpawnGroup.MONSTER, HDSKELETON, 40, 1, 2);
        //SpawnRestriction.register(HDSKELETON, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        
    }
}