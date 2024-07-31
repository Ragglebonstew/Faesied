package com.raggle.faesied.common.registry;

import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;
import com.raggle.faesied.Faesied;
import com.raggle.faesied.common.entity.FaeSkeleton;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;

public class FaeEntityRegistry {

    public static final EntityType<FaeSkeleton> HDSKELETON = Registry.register(
    		Registry.ENTITY_TYPE,
            new Identifier(Faesied.MOD_ID, "hdskeleton"),
            QuiltEntityTypeBuilder.create(SpawnGroup.CREATURE, FaeSkeleton::new).build()
    );
    
    public static void init() {
    	
        //FabricDefaultAttributeRegistry.register(HDSKELETON, FaeSkeleton.createAttributes());
        //BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biome.PLAINS), SpawnGroup.MONSTER, HDSKELETON, 40, 1, 2);
        //SpawnRestriction.register(HDSKELETON, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        
    }
}