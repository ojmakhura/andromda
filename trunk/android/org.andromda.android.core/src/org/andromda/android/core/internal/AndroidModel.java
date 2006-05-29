package org.andromda.android.core.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.cartridge.CartridgeParsingException;
import org.andromda.android.core.cartridge.CartridgeRegistry;
import org.andromda.android.core.cartridge.ICartridgeDescriptor;
import org.andromda.android.core.internal.project.AndroidProject;
import org.andromda.android.core.project.IAndroidProject;
import org.andromda.android.core.settings.IAndroidSettings;
import org.andromda.core.configuration.NamespaceDocument.Namespace;
import org.andromda.core.namespace.NamespaceDocument;
import org.andromda.core.namespace.PropertiesDocument.Properties;
import org.andromda.core.namespace.PropertyGroupDocument.PropertyGroup;
import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

/**
 * Provides central access to the Android data model.
 * 
 * @author Peter Friese
 * @since 07.10.2005
 */
public class AndroidModel
{
    /** Cache for projects that have an Android nature. */
    private Map cachedAndroidProjects = new HashMap();

    /**
     * Retrieves a list of all Android projects in the workspace.
     * 
     * @return An array of {@link IAndroidProject}s.
     * @throws CoreException If this method fails.
     */
    public IAndroidProject[] getAllAndroidProjects() throws CoreException
    {
        IWorkspaceRoot wRoot = ResourcesPlugin.getWorkspace().getRoot();
        IProject[] projects = wRoot.getProjects();
        List androidProjects = new ArrayList();
        for (int i = 0; i < projects.length; i++)
        {
            IProject project = projects[i];
            if (project.hasNature(AndroidCore.NATURE_ID))
            {
                androidProjects.add(new AndroidProject(project));
            }
        }
        return (IAndroidProject[])androidProjects.toArray(new IAndroidProject[androidProjects.size()]);
    }

    /**
     * Retrieves the Android project for the given Eclipse project.
     * 
     * @param project The project to retrieve the Android project for.
     * @return An Android project.
     */
    public IAndroidProject getAndroidProject(final IProject project)
    {
        IAndroidProject androidProject = (IAndroidProject)cachedAndroidProjects.get(project);
        if (androidProject == null)
        {
            androidProject = getAndroidProject(project, false);
            cachedAndroidProjects.put(project, androidProject);
        }
        return androidProject;
    }

    /**
     * Retrieves the Android project for the given Eclipse project.
     * 
     * @param project The project to retrieve the Android project for.
     * @param force If set to <code>true</code>, the project will be created in any case.
     * @return An Android project.
     */
    public IAndroidProject getAndroidProject(final IProject project,
        final boolean force)
    {
        return new AndroidProject(project, force);
    }

    /**
     * Returns the active Java project associated with the specified resource, or <code>null</code> if no Java project
     * yet exists for the resource.
     * 
     * @param resource The resource to get the Android project for.
     * @return The Android project the given resource belongs to.
     */
    public IAndroidProject getAndroidProject(final IResource resource)
    {
        switch (resource.getType())
        {
            case IResource.FOLDER:
                return new AndroidProject(((IFolder)resource).getProject());
            case IResource.FILE:
                return new AndroidProject(((IFile)resource).getProject());
            case IResource.PROJECT:
                return new AndroidProject((IProject)resource);
            default:
                throw new IllegalArgumentException("Illegal argument.");
        }
    }

    /**
     * Retrieves the property groups belonging to the given namespace.
     * 
     * @param configurationNamespace The configuration namespace for which we want to retrieve the property groups.
     * @param project The project we're in.
     * @return All property groups belonging to the given namespace.
     */
    public PropertyGroup[] getCartridgePropertyGroups(final Namespace configurationNamespace,
        final IAndroidProject project) 
    {
        String name = configurationNamespace.getName();

        ICartridgeDescriptor cartridgeDescriptor = CartridgeRegistry.getInstance()
                .getCartridgeDescriptor(project, name);

        PropertyGroup[] result = null;
        NamespaceDocument cartridgeNamespaceDescriptor;
        try
        {
            org.andromda.core.namespace.NamespaceDocument.Namespace namespace = cartridgeDescriptor.getNamespace();
            Properties[] propertiesArray = namespace.getPropertiesArray();
            for (int i = 0; i < propertiesArray.length; i++)
            {
                Properties properties = propertiesArray[i];
                PropertyGroup[] propertyGroupArray = properties.getPropertyGroupArray();
                result = (PropertyGroup[])ArrayUtils.addAll(result, propertyGroupArray);
            }
        }
        catch (CartridgeParsingException e)
        {
            AndroidCore.log(e);
        }
        return result;
    }

    /**
     * Returns the configuration file for the given project.
     * 
     * @param project The project in which we will look for the AndroMDA configuration file.
     * @return A handle to the configuration file.
     */
    public IFile getProjectConfiguration(final IProject project)
    {
        IAndroidSettings androidSettings = AndroidCore.getAndroidSettings();
        String configurationLocation = androidSettings.getConfigurationLocation(project);
        IFile configurationFile = project.getFile(configurationLocation);
        return configurationFile;
    }

}
