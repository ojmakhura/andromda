package org.andromda.cartridges.interfaces;

import java.util.Properties;

import org.andromda.core.common.CodeGenerationContext;

/**
 * Interface between an AndroMDA code generator cartridge
 * and the generator's core.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * 
 */
public interface IAndroMDACartridge
{
    /**
     * Initializes the cartridge.
     * @param velocityProperties the properties to use when initializing the Velocity engine
     */
    public void init (Properties velocityProperties)
        throws Exception;

    /**
     * Shuts down the cartridge. The meaning of this is defined
     * by the cartridge itself. At least, it should close any logfiles.
     */
    public void shutdown();
    
    /**
     * Returns the descriptor data of this particular cartridge.
     * 
     * @return ICartridgeDescriptor
     */
    public ICartridgeDescriptor getDescriptor();

    /**
     * Sets the descriptor data of this particular cartridge. Used by cartridge
     * manager.
     * 
     * @param d the new cartridge descriptor
     * 
     */
    public void setDescriptor(ICartridgeDescriptor d);

    /**
     * Generates code for one model element.
     * 
     * @param context the code generation context
     * @param modelElement the model element to generate code for
     * @param stereotypeName the name of the stereotype that selected the model
     * element
     * @throws CartridgeException if something goes wrong
     */
    public void processModelElement(
        CodeGenerationContext context,
        Object modelElement,
        String stereotypeName)
        throws CartridgeException;
}
