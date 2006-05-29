package org.andromda.android.ui.internal.project.wizard;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.andromda.android.core.cartridge.CartridgeParsingException;
import org.andromda.android.core.project.cartridge.IProjectCartridgeDescriptor;
import org.andromda.android.core.project.cartridge.IPromptGroup;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

/**
 * The Dynamic wizard display a series of pages that will be read from a given project cartridge descriptor. 
 *   
 * @author Peter Friese
 * @since 23.05.2006
 */
public class DynamicWizard
        extends Wizard
{

    /** The project cartridge descriptor that will be used to dynamically create the pages. */
    private final IProjectCartridgeDescriptor projectCartridgeDescriptor;
    
    /** The map of properties the user has configred by completing the wizard. */
    private final Map projectProperties;

    /**
     * Creates a new DynamicWizard.
     * 
     * @param projectCartridgeDescriptor
     */
    public DynamicWizard(IProjectCartridgeDescriptor projectCartridgeDescriptor, Map projectProperties)
    {
        this.projectCartridgeDescriptor = projectCartridgeDescriptor;
        this.projectProperties = projectProperties;
        setNeedsProgressMonitor(true);
    }
    
    /**
     * {@inheritDoc}
     */
    public void addPages()
    {
        super.addPages();
        try
        {
            Collection promptGroups = projectCartridgeDescriptor.getPromptGroups();
            for (Iterator iter = promptGroups.iterator(); iter.hasNext();)
            {
                IPromptGroup promptGroup = (IPromptGroup)iter.next();
                String name = promptGroup.getName();
                String description = promptGroup.getDescription();
                DynamicWizardPage dynamicWizardPage = new DynamicWizardPage(promptGroup, projectProperties);
                projectProperties.put("andromdappType", projectCartridgeDescriptor.getType());
                addPage(dynamicWizardPage);
            }
        }
        catch (CartridgeParsingException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean performFinish()
    {
        // have all pages write their gathered information into the project
        // properties map
        for (int i = 0; i < getPages().length; i++)
        {
            IWizardPage page = getPages()[i];
            if (page instanceof DynamicWizardPage)
            {
                DynamicWizardPage dynamicWizardPage = (DynamicWizardPage)page;
                dynamicWizardPage.updateData();
            }
        }
        return true;
    }

}
