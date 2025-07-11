package com.raggle.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.world.World;

@Mixin(ChunkRendererRegion.class)
public interface ChunkRendererRegionAccessor {

    @Accessor
    World getWorld();
}
