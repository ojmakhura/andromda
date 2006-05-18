package org.andromda.android.core.cartridge;

import java.util.HashMap;
import java.util.Map;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.internal.cartridge.CartridgeDescriptor;
import org.andromda.android.core.project.IAndroidProject;
import org.andromda.android.core.util.ResourceResolver;
import org.eclipse.core.resources.IContainer;

/**
 * A registry for the cartridge descriptors.
 * 
 * @author Peter Friese
 * @since 31.01.2006
 */
public final class CartridgeRegistry
{

    /** The singleton instance. */
    private static CartridgeRegistry instance;

    /** Stores the cartridge descriptors that already have been loaded. */
    private Map cartridgeDescriptors = new HashMap();

    /**
     * Creates a new CartridgeRegistry. Private, since this is a singleton.
     */
    private CartridgeRegistry()
    {
    }

    /**
     * @return The singleton instance of the cartridge registry.
     */
    public static CartridgeRegistry getInstance()
    {
        if (instance == null)
        {
            instance = new CartridgeRegistry();
        }
        return instance;
    }

    /**
     * Retrieves the cartridge descriptor for the given cartridge. If the descriptor has been loaded previously, a
     * reference to that instance will be returned. Otherwise, the descriptor will be loaded and stored in this
     * registry.
     * 
     * @param cartridgeName The name of the cartridge, e.g. "spring" or "hibernate".
     * @return A cartridge descriptor.
     */
    public ICartridgeDescriptor getCartridgeDescriptor(final String cartridgeName)
    {
        String cartridgesLocation = AndroidCore.getAndroidSettings().getAndroMDACartridgesLocation();
        String key = cartridgesLocation + "::" + cartridgeName;

        ICartridgeDescriptor cartridgeDescriptor = (ICartridgeDescriptor)cartridgeDescriptors.get(key);
        if (cartridgeDescriptor == null)
        {
            String cartridgeJar = ResourceResolver.findCartridge(cartridgesLocation, cartridgeName, "3.2", false);
            cartridgeDescriptor = new CartridgeDescriptor(cartridgeJar, true);
            cartridgeDescriptors.put(key, cartridgeDescriptor);
        }
        return cartridgeDescriptor;
    }

    /**
     * Retrieves the cartridge descriptor for the given cartridge. The descriptor is retrieved from a cartridge in the
     * workspace. If the descriptor has been loaded previously, a reference to that instance will be returned.
     * Otherwise, the descriptor will be loaded and stored in this registry.
     * 
     * @param cartridgeRootFolder The location of the cartridge root folder.
     * @return A cartridge descriptor.
     */
    public ICartridgeDescriptor getCartridgeDescriptor(final IContainer cartridgeRootFolder)
    {
        String location = cartridgeRootFolder.getLocation().toOSString();
        String key = location;
        
        ICartridgeDescriptor cartridgeDescriptor = (ICartridgeDescriptor)cartridgeDescriptors.get(key);
        if (cartridgeDescriptor == null)
        {
            cartridgeDescriptor = new CartridgeDescriptor(cartridgeRootFolder, false);
            cartridgeDescriptors.put(key, cartridgeDescriptor);
        }
        return cartridgeDescriptor;
    }

    /**
     * Retrieves the cartridge descriptor for the given cartridge. The location of the cartridge is read from the given
     * project. If the descriptor has been loaded previously, a reference to that instance will be returned. Otherwise,
     * the descriptor will be loaded and stored in this registry.
     * 
     * @param project The project to read the cartridge location from.
     * @param cartridgeName The name of the cartridge, e.g. "spring" or "hibernate".
     * @return A cartridge descriptor.
     */
    public ICartridgeDescriptor getCartridgeDescriptor(final IAndroidProject project,
        final String cartridgeName)
    {
        String cartridgesLocation = project.getProjectDefinition().getAndroMDACartridgesLocation();
        String key = cartridgesLocation + "::" + cartridgeName;

        ICartridgeDescriptor cartridgeDescriptor = (ICartridgeDescriptor)cartridgeDescriptors.get(key);
        if (cartridgeDescriptor == null)
        {
            String cartridgeJar = ResourceResolver.findCartridge(cartridgesLocation, cartridgeName, "3.2", false);
            cartridgeDescriptor = new CartridgeDescriptor(cartridgeJar, true);
            cartridgeDescriptors.put(key, cartridgeDescriptor);
        }
        return cartridgeDescriptor;
    }
}
