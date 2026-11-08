package com.optane.event;

import com.optane.event.events.EndFlashEvent;

/** Registers Ender Endeavors events. */
public final class EnderEndeavorsEvents {
    /** Prevents instantiation. */
    private EnderEndeavorsEvents() {}
    /** Registers event hooks. Must be called during mod initialization. */
    public static void register() {
        EndFlashEvent.register();
    }
}
