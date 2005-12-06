package org.andromda.android.ui.internal.settings.properties;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.project.IAndroidProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * This abstract property page contains convenience methods for Android projects.
 * 
 * @author Peter Friese
 * @since 06.12.2005
 */
public abstract class AbstractAndroidPropertyPage
        extends PropertyPage
{

    /**
     * Returns the selected project as an AndroidProject.
     * 
     * @return A casted reference to the project.
     */
    protected IAndroidProject getAndroidProject()
    {
        IProject project = (IProject)getElement().getAdapter(IProject.class);
        IAndroidProject androidProject = AndroidCore.create(project);
        return androidProject;
    }

}
