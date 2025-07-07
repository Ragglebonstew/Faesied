package com.raggle.half_dream.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

public class InterloperPortalParticle extends SpriteBillboardParticle {

	protected InterloperPortalParticle(ClientWorld clientWorld, double x, double y, double z, 
			double xd, double yd, double zd, SpriteProvider spriteProvider) {
		super(clientWorld, x, y, z, xd, yd, zd);
		this.setSpriteForAge(spriteProvider);

		this.velocityMultiplier = 1.0F;
		this.velocityX = xd + world.random.nextFloat() * 0.001f - 0.0005F;
		this.velocityY = yd + world.random.nextFloat() * 0.001f - 0.0005F;
		this.velocityZ = zd + world.random.nextFloat() * 0.001f - 0.0005F;

		this.scale *= 4f + world.random.nextFloat() * 0.5f;
		this.maxAge = 100;
		
		this.red = 0.95F + world.random.nextFloat() * 0.05F;
		this.green = 0.95F + world.random.nextFloat() * 0.05f;
		this.blue = 1.0F;
		this.alpha = 1.0F;
	}

	@Override
	public void tick() {
		super.tick();
		fadeOut();
	}
	
	private void fadeOut() {
		//this.colorAlpha = (1.0F-(this.age/(float)maxAge));
		this.scale *= (1-(this.age/(float)maxAge));
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

	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;
		
		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(DefaultParticleType particleEffect, ClientWorld clientWorld, 
				double x, double y, double z, 
				double dx, double dy, double dz) {
			return new InterloperPortalParticle(clientWorld, x, y, z, dx, dy, dz, this.spriteProvider);
		}
		
		
		
	}

}
