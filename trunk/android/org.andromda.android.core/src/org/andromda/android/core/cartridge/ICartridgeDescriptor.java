package org.andromda.android.core.cartridge;

import org.andromda.android.core.internal.cartridge.CartridgeParsingException;
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
     * @throws CartridgeParsingException 
     */
    Cartridge getCartridge() throws CartridgeParsingException;
    
    /**
     * @return The namespace document of the cartridge.
     * @throws CartridgeParsingException 
     */
    Namespace getNamespace() throws CartridgeParsingException;
    
    /**
     * @return The metafacade document of the cartridge.
     * @throws CartridgeParsingException 
     */
    Metafacade getMetafacade() throws CartridgeParsingException;
    

    // TODO import profile.xsd
//    ProfileDocument getProfile();

}
