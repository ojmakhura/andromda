package org.andromda.android.core.project;

import org.eclipse.core.resources.IProject;

/**
 *
 * @author Peter Friese
 * @since 23.10.2005
 */
public interface IAndroidProjectDefinition
{

    String getConfigurationLocation();

    void load(IProject project);

    void save();

}
