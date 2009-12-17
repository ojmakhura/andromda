package org.andromda.core.repository;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.namespace.BaseNamespaceComponent;


/**
 * Represents the repository namespace component.  This is what
 * configures a repository instance.
 *
 * @author Chad Brandon
 */
public class Repository
    extends BaseNamespaceComponent
{
    /**
     * Stores the repository facade implementation.
     */
    private RepositoryFacade implementation;

    /**
     * Sets the implementation class name.
     *
     * @param implementationClass the implementation class.
     */
    public void setImplementationClass(final String implementationClass)
    {
        final Class type = ClassUtils.loadClass(implementationClass);
        if (!RepositoryFacade.class.isAssignableFrom(type))
        {
            throw new RepositoryException(
                "Implementation '" + implementationClass + "' must be an instance of '" +
                RepositoryFacade.class.getName() + '\'');
        }
        implementation = (RepositoryFacade)ClassUtils.newInstance(type);
    }
    
    /**
     * Gets the repository implementation.
     * 
     * @return the repository implementation.
     */
    public RepositoryFacade getImplementation()
    {
        return this.implementation;
    }
}