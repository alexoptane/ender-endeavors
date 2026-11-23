package com.optane.client;

import com.optane.particle.EndcloudAmbientParticles;
import com.optane.particle.EnderEndeavorsParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteSet;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ReversePortalParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.core.particles.SimpleParticleType;

/** Provides the mod's client initialization entry point. */
public class EnderEndeavorsClient implements ClientModInitializer {
	private static final float PALE_RED = 0.78F;
	private static final float PALE_GREEN = 0.74F;
	private static final float PALE_BLUE = 0.76F;

	/** Client setup hook. */
	@Override
	public void onInitializeClient() {
		ParticleProviderRegistry registry = ParticleProviderRegistry.getInstance();
		registry.register(
				EnderEndeavorsParticles.ENDCLOUD_DUST_PALE,
				sprites -> createReversePortalProvider(sprites, null, true)
		);
		registry.register(
				EnderEndeavorsParticles.CHARGED_ENDCLOUD_DUST,
				sprites -> createReversePortalProvider(
						sprites, EndcloudAmbientParticles.CHARGED_LIFETIME_MIN_TICKS, false)
		);
		registry.register(
				EnderEndeavorsParticles.CHARGED_ENDCLOUD_DUST_PALE,
				sprites -> createReversePortalProvider(
						sprites, EndcloudAmbientParticles.CHARGED_LIFETIME_MIN_TICKS, true)
		);
	}

	/**
	 * Wraps vanilla's {@link ReversePortalParticle.ReversePortalProvider} with optional
	 * custom lifetime and pale tint.
	 *
	 * @param sprites particle sprite set
	 * @param minLifetimeTicks base lifetime in ticks ({@code null} keeps vanilla's 60-61 ticks)
	 * @param paleTint whether to apply the pale Endcloud dust color
	 * @return configured particle provider
	 */
	private static ParticleProvider<SimpleParticleType> createReversePortalProvider(FabricSpriteSet sprites, Integer minLifetimeTicks, boolean paleTint
	) {
		ParticleProvider<SimpleParticleType> reversePortal = new ReversePortalParticle.ReversePortalProvider(sprites);
		return (type, level, x, y, z, velocityX, velocityY, velocityZ, random) -> {
			SingleQuadParticle particle = (SingleQuadParticle) reversePortal.createParticle(
					type, level, x, y, z, velocityX, velocityY, velocityZ, random
			);
			if (minLifetimeTicks != null) {
				particle.setLifetime(minLifetimeTicks
						+ random.nextInt(EndcloudAmbientParticles.LIFETIME_RANDOM_SPAN_TICKS));
			}
			if (paleTint) {
				particle.setColor(PALE_RED, PALE_GREEN, PALE_BLUE);
			}
			return particle;
		};
	}
}