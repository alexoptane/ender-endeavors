package com.optane.event.mixin;

import net.minecraft.client.renderer.EndFlashState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Reads vanilla's current flash values without interpolating or advancing them. */
@Mixin(EndFlashState.class)
public interface EndFlashStateAccessor {
    @Accessor("intensity")
    float enderEndeavors$getIntensity();

    @Accessor("offset")
    int enderEndeavors$getOffset();

    @Accessor("duration")
    int enderEndeavors$getDuration();
}
