package org.andromda.android.core.internal.cartridge;

import org.andromda.android.core.cartridge.ICartridgeMetafacadeVariableDescriptor;
import org.eclipse.jdt.core.IType;

/**
 * Describes a variable from a cartridge that is backed by a Metefacade type.
 *
 * @author Peter Friese
 * @since 02.02.2006
 */
public class CartridgeMetafacadeVariableDescriptor
        extends CartridgeJavaVariableDescriptor
        implements ICartridgeMetafacadeVariableDescriptor
{

    /** Whether this variable is just a collection. */
    private final boolean collection;
    
    /** The path to the template this variable is valid in. */
    private final String templatePath;

    /**
     * Creates a new CartridgeJavaVariableDescriptor.
     *
     * @param name The name of the variable.
     * @param documentation The documentation of this variable.
     * @param type The Java type of the variable.
     * @param isCollection Whether this variable represents a collection of model elements.
     * @param templatePath The path to the template in which this variable is valid in.
     */
    public CartridgeMetafacadeVariableDescriptor(final String name,
        final String documentation,
        final IType type,
        final boolean isCollection,
        final String templatePath)
    {
        super(name, documentation, type);
        this.collection = isCollection;
        this.templatePath = templatePath;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCollection()
    {
        return collection;
    }

    /**
     * {@inheritDoc}
     */
    public String getTemplatePath()
    {
        return templatePath;
    }

}
