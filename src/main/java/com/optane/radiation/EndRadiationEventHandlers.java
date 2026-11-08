package com.optane.radiation;

/** Registers the handlers for End Radiation. */
public final class EndRadiationEventHandlers {
    /** Prevents instantiation. */
    private EndRadiationEventHandlers() {}

    /** Registers the radiation handlers during mod initialization. */
    public static void register() {
        PlayerEndRadiationHandler.register();
    }
}
