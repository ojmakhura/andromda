package org.andromda.core.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.configuration.Model;
import org.andromda.core.transformation.Transformer;
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
                    "', you must specify one of the following as your repository name: [" +
                    StringUtils.join(
                        this.repositories.keySet().iterator(),
                        ", ") + "]";
            }
            throw new RepositoryException(message);
        }
        return implementation;
    }

    /**
     * Loads the model defined in the configuration model instance into the repository
     * to which the model belongs.
     *
     * If the model has previously been loaded, this will only load the model
     * if it needs to be re-loaded (i.e. it has been changed).
     *
     * @param model the configuration model instance that contains the information about the model to load.
     * @return true if the model was loaded/re-loaded, false if the model was already loaded, and not re-loaded.
     */
    public boolean loadModel(final Model model)
    {
        ExceptionUtils.checkNull("model", model);
        boolean loaded = model.isChanged();
        if (loaded)
        {
            final org.andromda.core.configuration.Repository repository = model.getRepository();
            final String repositoryName = repository != null ? repository.getName() : null;
            if (repositoryName == null)
            {
                throw new RepositoryException("Could not retrieve the repository to which the '" + model + "' belongs");
            }
    
            // - first perform any transformations
            final Transformer transformer =
                (Transformer)ComponentContainer.instance().findRequiredComponent(Transformer.class);
            final String[] uris = model.getUris();
            final int uriNumber = uris.length;
            final InputStream[] streams = new InputStream[uriNumber];
            for (int ctr = 0; ctr < uriNumber; ctr++)
            {
                streams[ctr] = transformer.transform(
                        uris[ctr],
                        model.getTransformations());
            }
    
            // - now load the models into the repository
            for (int ctr = 0; ctr < uriNumber; ctr++)
            {
                final String uri = uris[ctr];
                AndroMDALogger.info("loading model --> '" + uri + "'");
            }
            final RepositoryFacade repositoryImplementation = this.getImplementation(repositoryName);
            repositoryImplementation.readModel(
                streams,
                uris,
                model.getModuleSearchLocationPaths());
    
            // - set the package filter
            repositoryImplementation.getModel(model.getAccessFacadeType()).setPackageFilter(model.getPackages());
            try
            {
                for (int ctr = 0; ctr < uriNumber; ctr++)
                {
                    InputStream stream = streams[ctr];
                    stream.close();
                    stream = null;
                }
            }
            catch (final IOException exception)
            {
                // ignore since the stream just couldn't be closed
            }
        }
        return loaded;
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