package org.andromda.android.core.project.cartridge;

import java.util.HashMap;
import java.util.Map;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.internal.project.cartridge.ProjectCartridgeDescriptor;
import org.andromda.android.core.util.ResourceResolver;

/**
 * This registry manages project cartridges.
 *
 * @author Peter Friese
 * @since 22.05.2006
 */
public final class ProjectCartridgeRegistry
{
    /** The singleton instance. */
    private static ProjectCartridgeRegistry instance;

    /** Stores the cartridge descriptors that already have been loaded. */
    private Map projectCartridgeDescriptors = new HashMap();

    /**
     * Creates a new CartridgeRegistry. Private, since this is a singleton.
     */
    private ProjectCartridgeRegistry()
    {
    }

    /**
     * @return The singleton instance of the cartridge registry.
     */
    public static ProjectCartridgeRegistry getInstance()
    {
        if (instance == null)
        {
            instance = new ProjectCartridgeRegistry();
        }
        return instance;
    }

    /**
     * Retrieves the project cartridge descriptor for the given cartridge. If the descriptor has been loaded previously,
     * a reference to that instance will be returned. Otherwise, the descriptor will be loaded and stored in this
     * registry.
     *
     * @param cartridgeName The name of the project cartridge, e.g. "j2ee-maven2".
     * @return A cartridge descriptor.
     */
    public IProjectCartridgeDescriptor getCartridgeDescriptor(final String cartridgeName)
    {
        String cartridgesLocation = AndroidCore.getAndroidSettings().getAndroMDACartridgesLocation();
        String key = cartridgesLocation + "::" + cartridgeName;

        IProjectCartridgeDescriptor projectCartridgeDescriptor = (IProjectCartridgeDescriptor)projectCartridgeDescriptors
                .get(key);
        if (projectCartridgeDescriptor == null)
        {
            String preferredVersion = AndroidCore.getAndroidSettings().getAndroMDAPreferredVersion();
            String projectCartridgeJar = ResourceResolver.findProjectCartridge(cartridgesLocation, cartridgeName,
                    preferredVersion, false);
            projectCartridgeDescriptor = new ProjectCartridgeDescriptor(projectCartridgeJar);
            projectCartridgeDescriptors.put(key, projectCartridgeDescriptor);
        }
        return projectCartridgeDescriptor;
    }

    /**
     * Retrieves all project cartridges that can be found on the class path.
     *
     * @return An array of project cartridge descriptors.
     */
    public IProjectCartridgeDescriptor[] getCartridgeDescriptors()
    {
        // @tag NewProjectWizard (project cartridge): dynamically read available cartridges on class path
        IProjectCartridgeDescriptor[] result = new IProjectCartridgeDescriptor[2];
        result[0] = getCartridgeDescriptor("j2ee-maven2");
        result[1] = getCartridgeDescriptor(("richclient-ant"));
        return result;
    }

}
