package com.optane.block;

import com.optane.EnderEndeavors;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;

/** Defines block and item key pairs. */
public final class EnderEndeavorsBlockItemIds {
    /** Prevents instantiation. */
    private EnderEndeavorsBlockItemIds() {}

    public static final BlockItemId ENDCLOUD = create("endcloud");
    public static final BlockItemId CHARGED_ENDCLOUD = create("charged_endcloud");

    /**
     * Creates block and item keys sharing an identifier.
     *
     * @param name registry path
     * @return paired registry keys
     */
    private static BlockItemId create(String name) {
        Identifier id = EnderEndeavors.id(name);
        return BlockItemId.create(id, id);
    }
}
