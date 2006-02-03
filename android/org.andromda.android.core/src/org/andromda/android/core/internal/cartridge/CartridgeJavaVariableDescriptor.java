package org.andromda.android.core.internal.cartridge;

import org.andromda.android.core.cartridge.ICartridgeJavaVariableDescriptor;
import org.eclipse.jdt.core.IType;

/**
 * Describes a variabale from a cartridge that is backed by a Java type.
 *  
 * @author Peter Friese
 * @since 02.02.2006
 */
public class CartridgeJavaVariableDescriptor
        extends CartridgeVariableDescriptor
        implements ICartridgeJavaVariableDescriptor
{

    /** The Java type this variable represents. */
    private final IType type;

    /**
     * Creates a new CartridgeJavaVariableDescriptor.
     * 
     * @param name The name of the variable.
     * @param documentation The documentation of this variable.
     * @param type The Java type of the variable.
     */
    public CartridgeJavaVariableDescriptor(final String name,
        final String documentation,
        final IType type)
    {
        super(name, documentation);
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    public IType getType()
    {
        return type;
    }

}
