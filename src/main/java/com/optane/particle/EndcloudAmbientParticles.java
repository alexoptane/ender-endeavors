package com.optane.particle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

/**
 * Shared physics and spawning helper for Endcloud-style rising reverse-portal motes.
 *
 * <p>Vanilla {@code ReversePortalParticle} advances by {@code velocity * (age / lifetime)} each tick
 * for {@code age = 1..lifetime}, so its total displacement over its lifetime {@code L} is
 * {@code velocity * (L + 1) / 2}. Dividing {@code (target - start)} by that integral factor ensures
 * the mote reaches the target just as it expires, while scaling {@code lifetime} proportionally to
 * distance preserves the exact same apparent speed.
 */
public final class EndcloudAmbientParticles {
    public static final int LIFETIME_RANDOM_SPAN_TICKS = 2;
    public static final int VANILLA_LIFETIME_MIN_TICKS = 60;
    public static final double VANILLA_TRAVEL_FACTOR =
            travelFactorForMaxLifetime(VANILLA_LIFETIME_MIN_TICKS + LIFETIME_RANDOM_SPAN_TICKS - 1);
    public static final int CHARGED_LIFETIME_MIN_TICKS = 98;
    public static final double CHARGED_TRAVEL_FACTOR =
            travelFactorForMaxLifetime(CHARGED_LIFETIME_MIN_TICKS + LIFETIME_RANDOM_SPAN_TICKS - 1);
    public static final float DEFAULT_EMIT_CHANCE = 0.725F;
    public static final float PALE_PARTICLE_CHANCE = 0.4F;

    private static final double TARGET_HORIZONTAL_MARGIN = 0.08;
    private static final double TARGET_HORIZONTAL_SPAN = 1.0 - 2.0 * TARGET_HORIZONTAL_MARGIN;
    private static final double START_HORIZONTAL_RADIUS_MIN = 0.08;
    private static final double START_HORIZONTAL_RADIUS_MAX = 0.32;
    private static final double START_Y_MIN_OFFSET = -0.70;
    private static final double START_Y_MAX_OFFSET = -0.25;

    /** Prevents instantiation. */
    private EndcloudAmbientParticles() {}

    /**
     * Computes the integral displacement factor {@code (L + 1) / 2} for a reverse-portal particle
     * with maximum lifetime {@code maxLifetimeTicks}.
     *
     * @param maxLifetimeTicks maximum particle lifetime in ticks
     * @return displacement factor used to convert target offset to velocity
     */
    private static double travelFactorForMaxLifetime(int maxLifetimeTicks) {
        return (maxLifetimeTicks + 1) / 2.0;
    }

    /**
     * Checks whether a block at {@code pos} is allowed to emit particles under {@code profile}.
     *
     * @param level world view used to inspect neighboring block collision shapes
     * @param pos block position
     * @param profile emission configuration
     * @return {@code true} if emission is allowed
     */
    public static boolean canEmitFromBlock(BlockGetter level, BlockPos pos, Profile profile) {
        if (!profile.blockedByFullBlockBelow()) {
            return true;
        }
        BlockPos belowPos = pos.below();
        return !level.getBlockState(belowPos).isCollisionShapeFullBlock(level, belowPos);
    }

    /**
     * Emits a particle for a block if obstruction and probability checks pass.
     *
     * @param level world where the particle is spawned
     * @param pos block position
     * @param random random source
     * @param profile emission configuration
     */
    public static void emitFromBlock(Level level, BlockPos pos, RandomSource random, Profile profile) {
        if (!canEmitFromBlock(level, pos, profile)) {
            return;
        }
        emitFromPosition(level, pos.getX(), pos.getY(), pos.getZ(), random, profile);
    }

    /**
     * Emits a particle around an arbitrary world origin if the probability check passes.
     * Suitable for non-block emitters (such as radiation effects around entities).
     *
     * @param level world where the particle is spawned
     * @param originX base X coordinate of the 1x1 emitter volume
     * @param originY base Y coordinate of the 1x1 emitter volume
     * @param originZ base Z coordinate of the 1x1 emitter volume
     * @param random random source
     * @param profile emission configuration
     */
    public static void emitFromPosition(
            Level level,
            double originX,
            double originY,
            double originZ,
            RandomSource random,
            Profile profile
    ) {
        if (random.nextFloat() >= profile.emitChance()) {
            return;
        }

        Spawn spawn = createSpawn(originX, originY, originZ, random, profile);
        level.addParticle(
                spawn.particleType(),
                spawn.startX(),
                spawn.startY(),
                spawn.startZ(),
                spawn.velocityX(),
                spawn.velocityY(),
                spawn.velocityZ()
        );
    }

    /**
     * Computes particle type, spawn position, and velocity for a block origin.
     *
     * @param pos block position
     * @param random random source
     * @param profile emission configuration
     * @return computed particle spawn parameters
     */
    public static Spawn createBlockSpawn(BlockPos pos, RandomSource random, Profile profile) {
        return createSpawn(pos.getX(), pos.getY(), pos.getZ(), random, profile);
    }

    /**
     * Computes particle type, spawn position, and velocity for an arbitrary world origin.
     *
     * @param originX base X coordinate of the 1x1 emitter volume
     * @param originY base Y coordinate of the 1x1 emitter volume
     * @param originZ base Z coordinate of the 1x1 emitter volume
     * @param random random source
     * @param profile emission configuration
     * @return computed particle spawn parameters
     */
    public static Spawn createSpawn(
            double originX,
            double originY,
            double originZ,
            RandomSource random,
            Profile profile
    ) {
        double targetX = originX + TARGET_HORIZONTAL_MARGIN + random.nextDouble() * TARGET_HORIZONTAL_SPAN;
        double targetY = originY + profile.targetYOffset();
        double targetZ = originZ + TARGET_HORIZONTAL_MARGIN + random.nextDouble() * TARGET_HORIZONTAL_SPAN;

        double angle = random.nextDouble() * Math.PI * 2.0;
        double radius = START_HORIZONTAL_RADIUS_MIN
                + random.nextDouble() * (START_HORIZONTAL_RADIUS_MAX - START_HORIZONTAL_RADIUS_MIN);
        double startX = targetX + Math.cos(angle) * radius;
        double startY = originY + START_Y_MIN_OFFSET
                + random.nextDouble() * (START_Y_MAX_OFFSET - START_Y_MIN_OFFSET);
        double startZ = targetZ + Math.sin(angle) * radius;

        SimpleParticleType particleType = random.nextFloat() < PALE_PARTICLE_CHANCE
                ? profile.paleParticle()
                : profile.primaryParticle();

        double travelFactor = profile.travelFactor();
        return new Spawn(
                particleType,
                startX,
                startY,
                startZ,
                (targetX - startX) / travelFactor,
                (targetY - startY) / travelFactor,
                (targetZ - startZ) / travelFactor
        );
    }

    /**
     * Declarative configuration describing an Endcloud-style ambient particle emitter.
     *
     * @param emitChance probability in {@code [0, 1]} of spawning a particle per tick attempt
     * @param targetYOffset target Y offset relative to the emitter's base Y
     * @param travelFactor integral displacement factor {@code (maxLifetime + 1) / 2}
     * @param blockedByFullBlockBelow whether a full collision block underneath suppresses emission
     * @param primaryParticle primary purple particle type (60% weight)
     * @param paleParticle pale dust particle type (40% weight)
     */
    public record Profile(
            float emitChance,
            double targetYOffset,
            double travelFactor,
            boolean blockedByFullBlockBelow,
            SimpleParticleType primaryParticle,
            SimpleParticleType paleParticle
    ) {}

    /**
     * Computed spawn parameters for a single ambient particle.
     *
     * @param particleType particle type to emit
     * @param startX initial world X coordinate
     * @param startY initial world Y coordinate
     * @param startZ initial world Z coordinate
     * @param velocityX X velocity parameter passed to {@link Level#addParticle}
     * @param velocityY Y velocity parameter passed to {@link Level#addParticle}
     * @param velocityZ Z velocity parameter passed to {@link Level#addParticle}
     */
    public record Spawn(
            SimpleParticleType particleType,
            double startX,
            double startY,
            double startZ,
            double velocityX,
            double velocityY,
            double velocityZ
    ) {}
}