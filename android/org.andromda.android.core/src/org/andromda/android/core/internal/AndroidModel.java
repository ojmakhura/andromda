package org.andromda.android.core.internal;

import java.util.ArrayList;
import java.util.List;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.internal.cartridge.Cartridge;
import org.andromda.android.core.internal.cartridge.CartridgeParsingException;
import org.andromda.android.core.internal.project.AndroidProject;
import org.andromda.android.core.project.IAndroidProject;
import org.andromda.core.configuration.NamespaceDocument.Namespace;
import org.andromda.core.namespace.NamespaceDocument;
import org.andromda.core.namespace.PropertiesDocument.Properties;
import org.andromda.core.namespace.PropertyGroupDocument.PropertyGroup;
import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.resources.IProject;
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
     * @param configurationNamespace
     * @return
     */
    public PropertyGroup[] getCartridgePropertyGroups(Namespace configurationNamespace)
    {
        String name = configurationNamespace.getName();
        Cartridge cartridge = new Cartridge(
                "file:/D:/Einstellungen/U402101/.maven/repository/andromda/jars/andromda-" + name + "-cartridge-3.2-RC1-SNAPSHOT.jar");
        PropertyGroup[] result = null;
        NamespaceDocument cartridgeNamespaceDescriptor;
        try
        {
            cartridgeNamespaceDescriptor = cartridge.getNamespaceDescriptor();
            Properties[] propertiesArray = cartridgeNamespaceDescriptor.getNamespace().getPropertiesArray();
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
