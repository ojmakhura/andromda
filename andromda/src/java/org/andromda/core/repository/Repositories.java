package org.andromda.core.repository;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.engine.ModelProcessorException;
import org.apache.commons.lang.StringUtils;

/**
 * This class provides access to all repositories available within the system (that
 * is: any repository registered within a namespace).
 * 
 * @author Chad Brandon
 */
public class Repositories
{
    /**
     * The shared instance of this class
     */
    private static Repositories instance;
    
    /**
     * Retrieves the shared instance of this class.
     * 
     * @return the shared instance.
     */
    public static final Repositories instance()
    {
        if (instance == null)
        {
            instance = new Repositories();
        }
        return instance;
    }
    
    /**
     * Stores all the repository implementations keyed by name.
     */
    private final Map repositories = new LinkedHashMap();
    
    /**
     * Discovers and initializes all repositories within this class.
     */
    public void initialize()
    {
        // - find and open any repositories
        if (this.repositories.isEmpty())
        {
            final Collection repositories = ComponentContainer.instance().findComponentsOfType(Repository.class);
            for (final Iterator iterator = repositories.iterator(); iterator.hasNext();)
            {
                final Repository repository = (Repository)iterator.next();
                final RepositoryFacade repositoryImplementation = repository.getImplementation();
                repositoryImplementation.open();
                this.repositories.put(
                    repository.getNamespace(),
                    repositoryImplementation);
            }
        }
    }
    
    /**
     * Retrieves the repository implementation with the given name (i.e. namespace).
     *
     * @param name the name of the repository implementation to retrieve.
     * @return the repository implementation.
     */
    public RepositoryFacade getImplementation(final String name)
    {
        final RepositoryFacade implementation = (RepositoryFacade)this.repositories.get(name);
        if (implementation == null)
        {
            String message;
            if (this.repositories.isEmpty())
            {
                message =
                    "No repository implementations have been registered, " +
                    "make sure you have at least one valid repository registered under a namespace on your classpath";
            }
            else
            {
                message =
                    "No repository implementation registered under namespace '" + name +
                    "', you must specify one of the available as your repository name: '" +
                    StringUtils.join(
                        this.repositories.keySet().iterator(),
                        ",") + "'";
            }
            throw new ModelProcessorException(message);
        }
        return implementation;
    }
    
    /**
     * Clears out any resources used by this class.
     */
    public void clear()
    {
        // - clear out any repositories
        if (!this.repositories.isEmpty())
        {
            for (final Iterator iterator = this.repositories.values().iterator(); iterator.hasNext();)
            {
                ((RepositoryFacade)iterator.next()).clear();
            }
        }
    }
    
}
