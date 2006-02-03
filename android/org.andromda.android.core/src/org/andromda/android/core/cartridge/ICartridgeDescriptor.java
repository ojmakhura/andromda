package org.andromda.android.core.cartridge;

import java.util.Collection;

import org.andromda.core.cartridge.CartridgeDocument.Cartridge;
import org.andromda.core.metafacade.MetafacadeDocument.Metafacade;
import org.andromda.core.namespace.NamespaceDocument.Namespace;

/**
 * This interface provides access to a cartridge.
 * 
 * @author Peter Friese
 * @since 30.01.2006
 */
public interface ICartridgeDescriptor
{
    
    /**
     * @return The cartridge document of the cartridge. 
     * @throws CartridgeParsingException If the cartridge could not be parsed.
     */
    Cartridge getCartridge() throws CartridgeParsingException;
    
    /**
     * @return The namespace document of the cartridge.
     * @throws CartridgeParsingException If the cartridge could not be parsed.
     */
    Namespace getNamespace() throws CartridgeParsingException;
    
    /**
     * @return The metafacade document of the cartridge.
     * @throws CartridgeParsingException If the cartridge could not be parsed.
     */
    Metafacade getMetafacade() throws CartridgeParsingException;
    
    /**
     * @return A collection of all variables that are defined in this cartridge.
     * @throws CartridgeParsingException If the cartridge could not be parsed.
     */
    Collection getVariableDescriptors() throws CartridgeParsingException;


    // TODO import profile.xsd
//    ProfileDocument getProfile();

}
