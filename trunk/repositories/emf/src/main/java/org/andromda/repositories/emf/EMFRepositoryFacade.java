package org.andromda.repositories.emf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.andromda.core.common.ResourceFinder;
import org.andromda.core.engine.ModelProcessor;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.repository.RepositoryFacade;
import org.andromda.core.repository.RepositoryFacadeException;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;


/**
 * An abstract EMF {@link RepositoryFacade} instance that should be extended by any repository wishing to load EMF models.
 *
 * @author Steve Jerman
 * @author Chad Brandon
 * @author Bob Fields (Multiple model support)
 */
public abstract class EMFRepositoryFacade
    implements RepositoryFacade
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(EMFRepositoryFacade.class);

    /**
     * Stores the resources (i.e. models) loaded into EMF.
     */
    protected ResourceSet resourceSet;
    
    protected ModelAccessFacade modelFacade;

    /**
     * Stores the actual loaded model.
     */
    // Modification required for multi-model support - fix compile errors after change
    // and implement multiple model processing by iterating through models in the list.
    protected List<Resource> model;

    /**
     * The options for loading the model.
     */
    private Map loadOptions = new HashMap();

    /**
     * Gets the current load options.
     *
     * @return the load options.
     */
    protected Map getLoadOptions()
    {
        return this.loadOptions;
    }

    /**
     * Reads the model with the given <code>uri</code>.
     *
     * @param uri the URI to the model
     */
    protected void readModel(final String uri)
    {
        try
        {
            if (this.model==null) 
            {
                this.model = new ArrayList<Resource>();
            }
            Resource modelResource = this.resourceSet.createResource(EMFRepositoryFacadeUtils.createUri(uri));
            if (modelResource == null)
            {
                throw new RepositoryFacadeException("'" + uri + "' is an invalid model");
            }
            modelResource.load(this.getLoadOptions());
            // Show errors and warnings, if any....
            EList errors = modelResource.getErrors();
            if (errors!=null && errors.size()>0)
            {
                logger.info(errors);
            }
            EList warnings = modelResource.getWarnings();
            if (warnings!=null && warnings.size()>0)
            {
                logger.info(warnings);
            }
            // Don't validate that model resources can be loaded, if not necessary
            if (ModelProcessor.getModelValidation())
            {
                try {
                    //logger.debug("EMFRepositoryFacade.readModel.resolve: " + modelResource.getURI());
                    // Duplicate call to EcoreUtil.resolveAll(modelResource);
                    //long now = System.currentTimeMillis();
                    for (Iterator<EObject> i = modelResource.getAllContents();  i.hasNext(); )
                    {
                        long now1 = System.currentTimeMillis();
                        EObject eObject = i.next();
                        //logger.debug("EMFRepositoryFacade.resolveAll.crossRef: " + EcoreUtil.getURI(eObject) + " " + (System.currentTimeMillis()-now1) + " ms");
                        for (Iterator<EObject> crossRefIterator =  eObject.eCrossReferences().iterator();  crossRefIterator.hasNext(); )
                        {
                            long now2 = System.currentTimeMillis();
                            EObject crossRef = crossRefIterator.next();
                            //EObject resolved = EcoreUtil.resolve(crossRef, this.resourceSet);
                            //logger.debug("EMFRepositoryFacade.resolveAll.crossRef: " + crossRef.toString() + " = " + EcoreUtil.getURI(crossRef) + " " + (System.currentTimeMillis()-now2) + " ms");
                        }
                    }
                } catch (RuntimeException e) {
                    logger.error("EMFRepositoryFacade.readModel.resolveAll :" + e);
                }
            }
            this.model.add(modelResource);
        }
        catch (final Exception exception)
        {
            throw new RepositoryFacadeException(exception);
        }
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#open()
     */
    public void open()
    {
        this.resourceSet = this.createNewResourceSet();
    }

    /**
     * Creates and returns a new resource suitable suitable for loading models into EMF.
     * This callback is used when (re-)initializing this repository so that it can be reused between different
     * AndroMDA runs, once a resource set is used for a model it becomes 'polluted' so that subsequent models
     * will see things from the previous runs, which might mess up the processing.
     *
     * @return a new resource set to be used by this repository
     */
    protected abstract ResourceSet createNewResourceSet();

    /**
     * @see org.andromda.core.repository.RepositoryFacade#close()
     */
    public void close()
    {
        // Ignore. Avoid compiler warning.
    }

    /**
     * The path to any modules found on the classpath.
     */
    private static final String MODULES_PATH = "META-INF/emf/modules";

    /**
     * @see org.andromda.core.repository.RepositoryFacade#readModel(String[], String[])
     */
    public final void readModel(
        String[] modelUris,
        String[] moduleSearchPaths)
    {
        if (modelUris == null || modelUris.length == 0)
        {
            throw new RepositoryFacadeException("No model specified.");
        }
        final List moduleSearchPathList = new ArrayList();
        if (moduleSearchPaths != null)
        {
            moduleSearchPathList.addAll(Arrays.asList(moduleSearchPaths));
        }

        // - first add the default module search paths maps that are found on the classpath
        final URL[] classpathSearchPaths = ResourceFinder.findResources(MODULES_PATH);

        if (classpathSearchPaths != null)
        {
            final int numberOfClasspathSearchPaths = classpathSearchPaths.length;
            for (int ctr = 0; ctr < numberOfClasspathSearchPaths; ctr++)
            {
                final URL classpathSearchPath = classpathSearchPaths[ctr];
                if (classpathSearchPath != null)
                {
                    moduleSearchPathList.add(classpathSearchPath.toString());
                }
            }
        }
        logger.debug("ModuleSearchPaths: " + moduleSearchPathList);
        this.resourceSet.setURIConverter(new EMFURIConverter(moduleSearchPathList));
        if (modelUris.length > 0)
        {
            final int numberOfModelUris = modelUris.length;
            for (int ctr = 0; ctr < numberOfModelUris; ctr++)
            {
                this.readModel(modelUris[ctr]);
            }
        }
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#readModel(java.io.InputStream[], String[], String[])
     */
    public void readModel(
        InputStream[] stream,
        String[] modelUri,
        String[] moduleSearchPaths)
    {
        this.readModel(
            modelUri,
            moduleSearchPaths);
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#writeModel(Object, String, String, String)
     */
    public void writeModel(
        Object modelIn,
        String location,
        String version,
        String encoding)
    {
        this.writeModel(
            modelIn,
            location,
            "");
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#writeModel(Object, String, String)
     */
    public void writeModel(
        Object modelIn,
        String location,
        String version)
    {
        final org.eclipse.emf.ecore.EModelElement element = (org.eclipse.emf.ecore.EModelElement)modelIn;
        final Resource resource = element.eResource();
        final URI uri = URI.createURI(location);
        resource.setURI(uri);
        try
        {
            resource.save(null);
        }
        catch (IOException exception)
        {
            throw new RepositoryFacadeException("Could not save model", exception);
        }
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#clear()
     */
    public void clear()
    {
        this.model = null;
        this.resourceSet = this.createNewResourceSet();
    }
}