package org.andromda.core.anttasks;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.repository.RepositoryFacade;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;

/**
 * 
 * This class implements the <code>&lt;repository&gt;</code> tag
 * which can used within the ant <code>&lt;andromda&gt;</code> tag
 * to configure androMDA to use a different object model repository.
 * 
 * <p> One application of this tag is that it supports the
 * possibility of loading an object model from a source other
 * than an XMI file. It could implemented by: <p>
 * 
 * <ul>
 * <li> introducing a new class that extends 
 * org.andromda.core.mdr.MDRespositoryFacade </li>
 * <li> overriding the readModel(URL) method so
 * that it reads the object model from the new object model source </li>
 * <li> storing the results of the object model
 * read into the MDR UML v1.4 repository </li>
 * </ul>
 * 
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 * @author Chad Brandon
 *  
 */
public class RepositoryConfiguration
{
    
    private Project project;
    private Path moduleSearchPath = null;
    private String repositoryClassName = null;

    public RepositoryConfiguration(Project project)
    {
        this.project = project;
    }
    
    /**
     * Sets the name of the class to use as the repository.  The
     * class must implement the RepositoryFacade interface.
     * 
     * @see org.andromda.core.repository.RepositoryFacade
     * 
     * @param repositoryClassName
     */
    public void setClassname(String repositoryClassName)
    {
        this.repositoryClassName = repositoryClassName;
    }

    /**
     * Creates an instance of the repository.
     * 
     * @return RepositoryFacade
     */
    public RepositoryFacade createRepository()
    {
        return (RepositoryFacade)ComponentContainer.instance().findComponent(
        	this.repositoryClassName, 
        	RepositoryFacade.class);
    }

    /**
     * Handles the nested &lt;moduleSearchPath&gt; element.
     * The user can specify her own search path for submodels
     * of the models that she is going to process.
     * 
     * @return Path the module search path
     */
    public Path createModuleSearchPath()
    {
        if (moduleSearchPath == null)
        {
            moduleSearchPath = new Path(project);
        }
        return moduleSearchPath;
    }
}
