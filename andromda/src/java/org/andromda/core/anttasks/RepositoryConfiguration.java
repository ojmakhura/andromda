package org.andromda.core.anttasks;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.repository.RepositoryFacade;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;

/**
 * 
 * This class implements the <code>&lt;repository&gt;</code> tag
 * which can used within the ant <code>&lt;andromda&gt;</code> tag
 * to configure androMDA to configure the object model repository.
 * 
 * Currently its used as a way of specifying a 
 * <code>&lt;moduleSearchPath/&gt;</code>.
 * 
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 * @author Chad Brandon
 *  
 */
public class RepositoryConfiguration
{
    
    private Project project;
    private Path moduleSearchPath = null;

    public RepositoryConfiguration(Project project)
    {
        this.project = project;
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
