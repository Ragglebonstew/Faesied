package com.raggle.half_dream.common.particles.custom;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class GreenFlameParticle extends SpriteBillboardParticle {

	protected GreenFlameParticle(ClientWorld clientWorld, double x, double y, double z, 
			double xd, double yd, double zd) {
		super(clientWorld, x, y, z, xd, yd, zd);

		this.velocityMultiplier = 0.6F;
		this.x = xd;
		this.y = yd;
		this.z = zd;
		this.scale *= 1.0F;
		this.maxAge = 20;
		
		this.colorRed = 0F;
		this.colorGreen = 1F;
		this.colorBlue = 0F;
	}

	@Override
	public void tick() {
		super.tick();
		fadeOut();
	}
	
	private void fadeOut() {
		this.colorAlpha = (-(1/(float)maxAge)*age+1);
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@ClientOnly
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider sprites;
		
		public Factory(SpriteProvider spriteSet) {
			this.sprites = spriteSet;
		}

		@Override
		public Particle createParticle(DefaultParticleType particleEffect, ClientWorld clientWorld, 
				double x, double y, double z, 
				double dx, double dy, double dz) {
			return new GreenFlameParticle(clientWorld, x, y, z, dx, dy, dz);
		}
		
		
		
	}

}
