package org.andromda.core.cartridge;

import org.andromda.core.common.CodeGenerationContext;

/**
 * Interface between an AndroMDA code generator cartridge
 * and the generator's core.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 */
public interface AndroMDACartridge
{
    /**
     * Performs any initialization required by
     * the cartridge.
     */
    public void init() throws Exception;

    /**
     * Shuts down the cartridge. The meaning of this is defined
     * by the cartridge itself. At least, it should close any logfiles.
     */
    public void shutdown();
    
    /**
     * Returns the descriptor data of this particular cartridge.
     * 
     * @return CartridgeDescriptor
     */
    public CartridgeDescriptor getDescriptor();

    /**
     * Sets the descriptor data of this particular cartridge. Used by cartridge
     * manager.
     * 
     * @param descriptor the new cartridge descriptor
     * 
     */
    public void setDescriptor(CartridgeDescriptor descriptor);
    
    /**
     * Processes all model elements with relevant stereotypes
     * by retrieving the model elements from the model facade
     * contained within the <code>context</code>.
     * 
     * @param context the context containing the ModelFacade 
     *        (amoung other things).
     */
    public void processModelElements(CodeGenerationContext context);
    
}
