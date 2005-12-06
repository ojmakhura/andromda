package org.andromda.android.core.project;

import org.eclipse.core.resources.IProject;

/**
 * An Android project represents a view of a project in terms of AndroMDA elements such as the AndroMDA configuration,
 * one or more models and so on.
 * 
 * @author Peter Friese
 * @since 06.10.2005
 */
public interface IAndroidProject
{
    /**
     * Returns the <code>IProject</code> on which this <code>IAndroidProject</code> was created. This is handle-only
     * method.
     * 
     * @return the <code>IProject</code> on which this <code>IJavaProject</code> was created
     */
    IProject getProject();

    IAndroidProjectDefinition getProjectDefinition();

}
