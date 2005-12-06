package org.andromda.android.core.internal.project;

import java.io.IOException;

import org.andromda.android.core.project.AndroidNature;
import org.andromda.android.core.project.IAndroidProject;
import org.andromda.android.core.project.IAndroidProjectDefinition;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * AndroidProject is a thin wrapper around {@link IProject}.
 * 
 * @author Peter Friese
 * @since 06.10.2005
 */
public class AndroidProject
        implements IAndroidProject
{

    /** The project decorated by this {@link AndroidProject}. */
    private final IProject project;

    /**
     * Creates a new Android project.
     * 
     * @param project
     */
    public AndroidProject(IProject project)
    {
        this.project = project;
        try
        {
            addNature(null);
        }
        catch (CoreException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param project2
     * @param force
     */
    public AndroidProject(IProject project,
        boolean force)
    {
        this(project);
        try
        {
            getProjectDefinition();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (CoreException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @see org.andromda.android.core.project.IAndroidProject#getProject()
     */
    public IProject getProject()
    {
        return project;
    }

    public IAndroidProjectDefinition getProjectDefinition() throws IOException, CoreException
    {
        return new AndroidProjectDefinition(getProject());
    }

    private void addNature(IProgressMonitor monitor) throws CoreException
    {
        IProjectDescription description = project.getDescription();
        String[] prevNatures = description.getNatureIds();
        String[] newNatures = new String[prevNatures.length + 1];
        System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
        newNatures[prevNatures.length] = AndroidNature.ID;
        description.setNatureIds(newNatures);
        project.setDescription(description, monitor);
    }

}
