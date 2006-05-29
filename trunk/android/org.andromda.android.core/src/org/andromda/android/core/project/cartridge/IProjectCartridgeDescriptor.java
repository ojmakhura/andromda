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
    public static final String PROPERTY_APPLICATION_PARENT_DIRECTORY = "applicationParentDirectory";

    /** Cartridge prompt ID for the project ID */
    public static final String PROPERTY_PROJECT_ID = "applicationId";

    /** Cartridge prompt ID for the project name. */
    public static final String PROPERTY_PROJECT_NAME = "applicationName";

    /** These properties will be gathered by the BasicProjectInformationWizardPage. */
    public static final String[] PROJECT_BASIC_PROPERTIES = new String[] { 
        PROPERTY_PROJECT_NAME, 
        PROPERTY_PROJECT_ID,
        PROPERTY_APPLICATION_PARENT_DIRECTORY };

    /**
     * Get the type of the project generator.
     * 
     * @throws CartridgeParsingException
     */
    public String getType() throws CartridgeParsingException;

    /** Get all prompt groups. */
    public Collection getPromptGroups() throws CartridgeParsingException;

}