package org.andromda.android.core.project.cartridge;

import java.util.Collection;

import org.andromda.android.core.cartridge.CartridgeParsingException;

/**
 * This interface describes a project cartridge.
 * 
 * @author Peter Friese
 * @since 22.05.2006
 */
public interface IProjectCartridgeDescriptor
{

    /** Cartridge prompt ID for the directory we will put the project in. */
    String PROPERTY_APPLICATION_PARENT_DIRECTORY = "applicationParentDirectory";

    /** Cartridge prompt ID for the project ID. */
    String PROPERTY_PROJECT_ID = "applicationId";

    /** Cartridge prompt ID for the project name. */
    String PROPERTY_PROJECT_NAME = "applicationName";

    /** These properties will be gathered by the BasicProjectInformationWizardPage. */
    String[] PROJECT_BASIC_PROPERTIES = new String[] { PROPERTY_PROJECT_NAME, PROPERTY_PROJECT_ID,
            PROPERTY_APPLICATION_PARENT_DIRECTORY };

    /**
     * Get the type of the project generator.
     * 
     * @return The type of te project generator.
     * 
     * @throws CartridgeParsingException If the cartridge descriptor could not be parsed.
     */
    String getType() throws CartridgeParsingException;

    /**
     * Get all prompt groups.
     * 
     * @return A collection of prompt groups.
     * @throws CartridgeParsingException If the cartridge descrptor could not be parsed.
     */
    Collection getPromptGroups() throws CartridgeParsingException;

}
