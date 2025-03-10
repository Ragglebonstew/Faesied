package com.raggle.half_dream.common.particles.custom;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

public class GreenFlameParticle extends SpriteBillboardParticle {

	protected GreenFlameParticle(ClientWorld clientWorld, double x, double y, double z, 
			double xd, double yd, double zd, SpriteProvider spriteProvider) {
		super(clientWorld, x, y, z, xd, yd, zd);
		this.setSpriteForAge(spriteProvider);

		this.velocityMultiplier = 1.0F;
		this.velocityX = xd;
		this.velocityY = yd;
		this.velocityZ = zd;

		this.scale *= 40f + world.random.nextFloat() * 0.5f;
		this.maxAge = 300;
		
		this.colorRed = 1.0F;
		this.colorGreen = 1.0F;
		this.colorBlue = 1.0F;
		this.colorAlpha = 0.5F;
	}

	@Override
	public void tick() {
		super.tick();
		fadeOut();
	}
	
	private void fadeOut() {
		this.colorAlpha = (1.0F-(this.age/(float)maxAge));
	}
	
	@Override
	public int getBrightness(float tint) {
		float f = ((float)this.age + tint) / (float)this.maxAge;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		int i = super.getBrightness(tint);
		int j = i & 0xFF;
		int k = i >> 16 & 0xFF;
		j += (int)(f * 15.0F * 16.0F);
		if (j > 240) {
			j = 240;
		}

		return j | k << 16;
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@ClientOnly
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;
		
		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(DefaultParticleType particleEffect, ClientWorld clientWorld, 
				double x, double y, double z, 
				double dx, double dy, double dz) {
			return new GreenFlameParticle(clientWorld, x, y, z, dx, dy, dz, this.spriteProvider);
		}
		
		
		
	}

}
