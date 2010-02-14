package org.andromda.core.repository;

import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Introspector;
import org.andromda.core.configuration.Model;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.namespace.PropertyDefinition;
import org.andromda.core.transformation.Transformer;
import org.apache.commons.io.IOUtils;
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
    public static Repositories instance()
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
    private final Map<String, RepositoryFacade> repositories = new LinkedHashMap<String, RepositoryFacade>();

    /**
     * Discovers and initializes all repositories within this class.
     */
    public void initialize()
    {
        // - find and open any repositories
        if (this.repositories.isEmpty())
        {
            final Namespaces namespaces = Namespaces.instance();
            final Collection<Repository> repositories = ComponentContainer.instance().findComponentsOfType(Repository.class);
            for (final Repository repository : repositories)
            {
                final RepositoryFacade repositoryImplementation = repository.getImplementation();
                final String namespace = repository.getNamespace();
                final PropertyDefinition[] properties = namespaces.getPropertyDefinitions(namespace);
                if (properties != null)
                {
                    for (final PropertyDefinition property : properties)
                    {
                        final String propertyName = property.getName();
                        if (Introspector.instance().isWritable(repositoryImplementation, propertyName))
                        {
                            Introspector.instance().setProperty(
                                repositoryImplementation,
                                property.getName(),
                                namespaces.getPropertyValue(
                                    namespace,
                                    property.getName()));
                        }
                    }
                }
                repositoryImplementation.open();
                this.repositories.put(
                    namespace,
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
        final RepositoryFacade implementation = this.repositories.get(name);
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
                if (name.equals("emf-uml2"))
                {
                    message =
                        "emf-uml2 (UML2 1.x) has been deprecated and is no longer the UML2 default, change your project " +
                        "andromda.xml configuration to 'emf-uml22' and export your model to UML2 2.x or upgrade to RSM 7.x, " + 
                        "you must specify one of the following as your repository name: [" +
                        StringUtils.join(
                            this.repositories.keySet().iterator(),
                            ", ") + ']';
                }
                else
                {
                    message =
                        "No repository implementation registered under namespace '" + name +
                        "', you must specify one of the following as your repository name: [" +
                        StringUtils.join(
                            this.repositories.keySet().iterator(),
                            ", ") + ']';
                }
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
        ExceptionUtils.checkNull(
            "model",
            model);
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
            for (final String uri : uris)
            {
                AndroMDALogger.info("loading model --> '" + uri + '\'');
            }
            final RepositoryFacade repositoryImplementation = this.getImplementation(repositoryName);
            repositoryImplementation.readModel(
                streams,
                uris,
                model.getModuleSearchLocationPaths());

            // - set the package filter
            if (repositoryImplementation.getModel()!=null)
            {
                repositoryImplementation.getModel().setPackageFilter(model.getPackages());            
            }
            for (InputStream stream : streams)
            {
                IOUtils.closeQuietly(stream);
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
        for (final RepositoryFacade repositoryFacade : this.repositories.values())
        {
            repositoryFacade.clear();
        }
    }
}
