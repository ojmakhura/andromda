package org.andromda.android.core.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.cartridge.ICartridgeDescriptor;
import org.andromda.android.core.internal.cartridge.CartridgeDescriptor;
import org.andromda.android.core.internal.cartridge.CartridgeParsingException;
import org.andromda.android.core.internal.project.AndroidProject;
import org.andromda.android.core.project.IAndroidProject;
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
 * 
 * @author Peter Friese
 * @since 07.10.2005
 */
public class AndroidModel
{

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
     * @param project
     * @return
     */
    public IAndroidProject getAndroidProject(IProject project)
    {
        return getAndroidProject(project, false);
    }

    /**
     * @param project
     * @param force
     * @return
     */
    public IAndroidProject getAndroidProject(IProject project,
        boolean force)
    {
        return new AndroidProject(project, force);
    }

    /**
     * Returns the active Java project associated with the specified resource, or <code>null</code> if no Java project
     * yet exists for the resource.
     * 
     * @exception IllegalArgumentException if the given resource is not one of an IProject, IFolder, or IFile.
     */
    public IAndroidProject getAndroidProject(IResource resource)
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
     * @param configurationNamespace
     * @return
     * @throws CoreException
     * @throws IOException
     */
    public PropertyGroup[] getCartridgePropertyGroups(final Namespace configurationNamespace,
        final IAndroidProject project)
    {
        String androMDACartridgesLocation = project.getProjectDefinition().getAndroMDACartridgesLocation();

        String name = configurationNamespace.getName();
        ICartridgeDescriptor cartridgeDescriptor = new CartridgeDescriptor("file:/" + androMDACartridgesLocation
                + "/andromda-" + name + "-cartridge-3.2-RC1-SNAPSHOT.jar");

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

}
