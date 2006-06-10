package org.andromda.android.ui.internal.navigator;

import org.andromda.android.ui.AndroidUIPlugin;
import org.andromda.android.ui.util.SWTResourceManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * The label provider for the AndroMDA navigator contribution.
 * 
 * @author Peter Friese
 * @since 24.03.2006
 */
public class AndroMDANavigatorLabelProvider
        implements ILabelProvider
{

    /**
     * {@inheritDoc}
     */
    public String getText(final Object element)
    {
        if (NavigatorUtils.isAndroMDAConfigurationFile(element))
        {
            return "AndroMDA Configuration";
        }
        else
        {
            return WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider().getText(element);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Image getImage(final Object element)
    {
        if (NavigatorUtils.isAndroMDAConfigurationFile(element))
        {
            return SWTResourceManager.getPluginImage(AndroidUIPlugin.getDefault(),
                    "icons/andromda_configuration_editor.gif");
        }
        return WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider().getImage(element);
    }

    /**
     * {@inheritDoc}
     */
    public void addListener(final ILabelProviderListener listener)
    {
    }

    /**
     * {@inheritDoc}
     */
    public void removeListener(final ILabelProviderListener listener)
    {
    }

    /**
     * {@inheritDoc}
     */
    public void dispose()
    {
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLabelProperty(final Object element,
        final String property)
    {
        return false;
    }

}
