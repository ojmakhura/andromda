package org.andromda.android.core.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * Project nature for Android projects.
 *
 * @author Peter Friese
 * @since 09.10.2005
 */
public class AndroidNature
        implements IProjectNature
{

    public static final String ID = "org.andromda.android.core.androidnature";

    private IProject project;

    /**
     * @see org.eclipse.core.resources.IProjectNature#configure()
     */
    public void configure() throws CoreException
    {
        System.out.println("Configuring Android nature.");
    }

    /**
     * @see org.eclipse.core.resources.IProjectNature#deconfigure()
     */
    public void deconfigure() throws CoreException
    {
        System.out.println("Deconfiguring Android natue.");
    }

    /**
     * @see org.eclipse.core.resources.IProjectNature#getProject()
     */
    public IProject getProject()
    {
        return project;
    }

    /**
     * @see org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject)
     */
    public void setProject(IProject project)
    {
        this.project = project;
    }

}
