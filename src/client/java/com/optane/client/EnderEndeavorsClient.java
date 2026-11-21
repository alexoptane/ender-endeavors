package com.optane.client;

import com.optane.EnderEndeavors;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteSet;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ReversePortalParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.core.particles.SimpleParticleType;

/** Provides the mod's client initialization entry point. */
public class EnderEndeavorsClient implements ClientModInitializer {
	/** Client setup hook. */
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ParticleProviderRegistry.getInstance().register(
				EnderEndeavors.ENDCLOUD_DUST_PALE,
				EnderEndeavorsClient::createPaleEndcloudParticleProvider
		);
	}

	private static ParticleProvider<SimpleParticleType> createPaleEndcloudParticleProvider(FabricSpriteSet sprites) {
		ParticleProvider<SimpleParticleType> reversePortal = new ReversePortalParticle.ReversePortalProvider(sprites);

		return (type, level, x, y, z, velocityX, velocityY, velocityZ, random) -> {
            SingleQuadParticle particle = (SingleQuadParticle) reversePortal.createParticle(
					type, level, x, y, z, velocityX, velocityY, velocityZ, random
			);
            particle.setColor(0.78F, 0.74F, 0.76F);
			return particle;
		};
	}
}
