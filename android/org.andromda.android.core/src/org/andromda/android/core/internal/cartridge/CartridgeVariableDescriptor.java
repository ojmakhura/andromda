package org.andromda.android.core.internal.cartridge;

import org.andromda.android.core.cartridge.ICartridgeVariableDescriptor;

/**
 * A cartridge variable decriptor.
 * 
 * @author Peter Friese
 * @since 02.02.2006
 */
public class CartridgeVariableDescriptor
        implements ICartridgeVariableDescriptor
{

    /** The name of this variable. */
    private final String name;

    /** The documentation for this variable. */
    private final String documentation;

    /**
     * Creates a new CartridgeVariableDescriptor.
     * 
     * @param name The name of the variable.
     * @param documentation The documentation for this variable.
     */
    public CartridgeVariableDescriptor(final String name,
        final String documentation)
    {
        super();
        this.name = name;
        this.documentation = documentation;
    }

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public String getDocumentation()
    {
        return documentation;
    }

}
