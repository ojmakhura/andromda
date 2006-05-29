package org.andromda.android.core.project;

import java.util.Map;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.internal.project.AndroidProjectDefinition;
import org.andromda.android.core.internal.project.generator.AndroMDAppRunner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.JavaCore;

/**
 * Creates a new Android project.
 * 
 * @author Peter Friese
 * @since 09.10.2005
 */
public final class AndroidProjectFactory
{

    /**
     * Hidden c'tor (it is a utility class).
     */
    private AndroidProjectFactory()
    {
    }

    /**
     * Creates a new Android project.
     * 
     * @param monitor the progress monitor.
     * @param projectName the name of the project.
     * @param projectProperties the settings for the project.
     */
    public static void createAndroidProject(final IProgressMonitor monitor,
        final String projectName,
        final Map projectProperties)
    {
        monitor.beginTask("", 2000);
        try
        {
            SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1000);
            subMonitor.beginTask("Getting project", 200);
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IWorkspaceRoot root = workspace.getRoot();
            IProject project = root.getProject(projectName);
            subMonitor.beginTask("Generating contents", 800);
            AndroMDAppRunner runner = new AndroMDAppRunner();
            runner.setConfiguration(projectProperties);
            runner.run();
            subMonitor.done();
            subMonitor = null;

            subMonitor = new SubProgressMonitor(monitor, 1000);
            project.create(subMonitor);
            project.open(subMonitor);
            addNatureToProject(project, JavaCore.NATURE_ID, subMonitor);
            addNatureToProject(project, AndroidCore.NATURE_ID, subMonitor);
            new AndroidProjectDefinition(project);
            subMonitor.done();
        }
        catch (Exception e)
        {
            AndroidCore.log(e);
        }
        monitor.done();
    }

    /**
     * Adds the given nature to the project.
     * 
     * @param project the project to add the nature to.
     * @param natureId the ID of the nature.
     * @param monitor the progress monitor displaying progress.
     * @throws CoreException if something goes wrong
     */
    public static void addNatureToProject(final IProject project,
        final String natureId,
        final IProgressMonitor monitor) throws CoreException
    {
        IProjectDescription description = project.getDescription();
        String[] prevNatures = description.getNatureIds();
        String[] newNatures = new String[prevNatures.length + 1];
        System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
        newNatures[prevNatures.length] = natureId;
        description.setNatureIds(newNatures);
        project.setDescription(description, monitor);
    }

}
