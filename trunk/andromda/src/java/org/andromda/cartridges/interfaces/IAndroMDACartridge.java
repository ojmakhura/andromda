package org.andromda.cartridges.interfaces;

/**
 * Interface between an AndroMDA code generator cartridge
 * and the generator's core.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * 
 */
public interface IAndroMDACartridge {
	
	
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
    public void setDescriptor (ICartridgeDescriptor d);
}
