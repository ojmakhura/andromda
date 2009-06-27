package org.andromda.repositories.mdr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;
import javax.jmi.xmi.XmiReader;
import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.repository.RepositoryFacade;
import org.andromda.core.repository.RepositoryFacadeException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.netbeans.api.xmi.XMIWriter;
import org.netbeans.api.xmi.XMIWriterFactory;


/**
 * Implements an AndroMDA object model repository by using the <a href="http://mdr.netbeans.org">NetBeans
 * MetaDataRepository </a>.
 *
 * @author <A HREF="httplo://www.amowers.com">Anthony Mowers </A>
 * @author Chad Brandon
 */
public class MDRepositoryFacade
    implements RepositoryFacade
{
    private static final Logger logger = Logger.getLogger(MDRepositoryFacade.class);
    private ModelAccessFacade modelFacade = null;
    private MDRepository repository = null;
    protected URL metamodelUri;
    protected RefPackage model = null;

    public MDRepositoryFacade()
    {
        // configure MDR to use an in-memory storage implementation
        System.setProperty(
            "org.netbeans.mdr.storagemodel.StorageFactoryClassName",
            "org.netbeans.mdr.persistence.memoryimpl.StorageFactoryImpl");

        final MDRManager mdrManager = MDRManager.getDefault();
        if (mdrManager == null)
        {
            throw new RepositoryFacadeException("Could not retrieve the MDR manager");
        }
        this.repository = mdrManager.getDefaultRepository();
    }

    /**
     * Keeps track of whether or not the repository is open.
     */
    private boolean open = false;

    /**
     * Opens the repository and prepares it to read in models.
     * <p/>
     * All the file reads are done within the context of a transaction: this seems to speed up the processing. </p>
     *
     * @see org.andromda.core.repository.RepositoryFacade#open()
     */
    public void open()
    {
        if (!this.open)
        {
            repository.beginTrans(true);
            this.open = true;
        }
    }

    /**
     * Closes the repository and reclaims all resources.
     * <p/>
     * This should only be called after all model processing has been completed. </p>
     *
     * @see org.andromda.core.repository.RepositoryFacade#close()
     */
    public void close()
    {
        if (this.open)
        {
            repository.endTrans(false);
            this.clear();
            MDRManager.getDefault().shutdownAll();
            this.open = false;
        }
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#readModel(java.lang.String[], java.lang.String[])
     */
    public void readModel(
        String[] uris,
        String[] moduleSearchPath)
    {
        try
        {
            final MofPackage metaModel = this.loadMetaModel(this.getMetamodelUri());
            this.model = this.loadModel(
                    uris,
                    moduleSearchPath,
                    metaModel);
        }
        catch (final Throwable throwable)
        {
            throw new RepositoryFacadeException(throwable);
        }
    }

    /**
     * Retrieves the URI of the metamodel.
     *
     * @return the metamodel uri.
     */
    private URL getMetamodelUri()
    {
        if (this.metamodelUri == null)
        {
            throw new RepositoryFacadeException("No metamodel was defined");
        }
        return this.metamodelUri;
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#readModel(java.io.InputStream[], java.lang.String[], java.lang.String[])
     */
    public void readModel(
        final InputStream[] streams,
        final String[] uris,
        final String[] moduleSearchPath)
    {
        if (streams != null && uris != null && uris.length != streams.length)
        {
            throw new IllegalArgumentException("'streams' and 'uris' must be of the same length");
        }
        try
        {
            final MofPackage metaModel = this.loadMetaModel(this.getMetamodelUri());
            this.model = this.loadModel(
                    streams,
                    uris,
                    moduleSearchPath,
                    metaModel);
        }
        catch (final Throwable throwable)
        {
            throw new RepositoryFacadeException(throwable);
        }
    }

    /**
     * The default XMI version if none is specified.
     */
    private static final String DEFAULT_XMI_VERSION = "1.2";

    /**
     * The default encoding if none is specified
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * @see org.andromda.core.repository.RepositoryFacade#writeModel(java.lang.Object, java.lang.String,
     *      java.lang.String)
     */
    public void writeModel(
        Object model,
        String outputLocation,
        String xmiVersion)
    {
        this.writeModel(
            model,
            outputLocation,
            xmiVersion,
            null);
    }

    /**
     * Sets the location of the metamodel.
     *
     * @param metamodelLocation the metamodel location.
     */
    public void setMetamodelLocation(final String metamodelLocation)
    {
        this.metamodelUri = MDRepositoryFacade.class.getResource(metamodelLocation);
        if (this.metamodelUri == null)
        {
            ResourceUtils.toURL(metamodelLocation);
        }
        if (this.metamodelUri == null)
        {
            throw new RepositoryFacadeException("No metamodel could be loaded from --> '" + metamodelLocation + "'");
        }
    }

    /**
     * The metamodel package name.
     */
    private String metamodelPackage;

    /**
     * Sets the name of the root package of the metamodel.
     *
     * @param metamodelPackage the root is metamodel package name.
     */
    public void setMetamodelPackage(final String metamodelPackage)
    {
        this.metamodelPackage = metamodelPackage;
    }

    /**
     * The org.netbeans.api.xmi.XMIReaderFactory implementation name.
     */
    private String xmiReaderFactory;

    /**
     * Sets the org.netbeans.api.xmi.XMIReaderFactory implementation to use.
     *
     * @param xmiReaderFactory the fully qualified implementation class name to use.
     */
    public void setXmiReaderFactory(final String xmiReaderFactory)
    {
        this.xmiReaderFactory = xmiReaderFactory;
    }

    /**
     * Stores the xmiReader instances.
     */
    private Map xmiReaderFactoryInstances = new HashMap();

    /**
     * Retrieves the javax.jmi.xmi.XmiReader implementation.
     *
     * @return the javax.jmi.xmi.XmiReader class
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private synchronized XMIReaderFactory getXMIReaderFactory()
        throws InstantiationException, IllegalAccessException
    {
        XMIReaderFactory factory =
            (org.netbeans.api.xmi.XMIReaderFactory)this.xmiReaderFactoryInstances.get(this.xmiReaderFactory);
        if (factory == null)
        {
            if (this.xmiReaderFactory == null || this.xmiReaderFactory.trim().length() == 0)
            {
                throw new RepositoryFacadeException("No 'xmiReaderFactory' has been set");
            }
            Object instance = ClassUtils.loadClass(this.xmiReaderFactory).newInstance();
            if (instance instanceof XMIReaderFactory)
            {
                factory = (XMIReaderFactory)instance;
                this.xmiReaderFactoryInstances.put(
                    this.xmiReaderFactory,
                    factory);
            }
            else
            {
                throw new RepositoryFacadeException("The 'xmiReaderFactory' must be an instance of '" +
                    XMIReaderFactory.class.getName() + "'");
            }
        }
        return factory;
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#writeModel(java.lang.Object, java.lang.String,
     *      java.lang.String)
     */
    public void writeModel(
        Object model,
        String outputLocation,
        String xmiVersion,
        String encoding)
    {
        ExceptionUtils.checkNull(
            "model",
            model);
        ExceptionUtils.checkNull(
            "outputLocation",
            outputLocation);
        ExceptionUtils.checkAssignable(
            RefPackage.class,
            "model",
            model.getClass());
        if (StringUtils.isEmpty(xmiVersion))
        {
            xmiVersion = DEFAULT_XMI_VERSION;
        }
        if (StringUtils.isEmpty(encoding))
        {
            encoding = DEFAULT_ENCODING;
        }
        try
        {
            // ensure the directory we're writing to exists
            final File file = new File(outputLocation);
            final File parent = file.getParentFile();
            if (parent != null)
            {
                parent.mkdirs();
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            final XMIWriter xmiWriter = XMIWriterFactory.getDefault().createXMIWriter();
            xmiWriter.getConfiguration().setEncoding(encoding);
            xmiWriter.write(
                outputStream,
                outputLocation,
                (RefPackage)model,
                xmiVersion);
            outputStream.close();
            outputStream = null;
        }
        catch (final Throwable throwable)
        {
            throw new RepositoryFacadeException(throwable);
        }
    }

    private Class modelAccessFacade;

    /**
     * Sets the model access facade instance to be used with this repository.
     *
     * @param modelAccessFacade the model access facade
     */
    public void setModelAccessFacade(Class modelAccessFacade)
    {
        this.modelAccessFacade = modelAccessFacade;
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#getModel()
     */
    public ModelAccessFacade getModel()
    {
        if (this.modelFacade == null)
        {
            try
            {
                this.modelFacade =
                    (ModelAccessFacade)ComponentContainer.instance().newComponent(
                        this.modelAccessFacade,
                        ModelAccessFacade.class);
            }
            catch (final Throwable throwable)
            {
                throw new RepositoryFacadeException(throwable);
            }
        }
        if (this.model != null)
        {
            this.modelFacade.setModel(this.model);
        }
        else
        {
            this.modelFacade = null;
        }
        return this.modelFacade;
    }

    /**
     * Loads a metamodel into the repository.
     *
     * @param metamodelUri the url to the meta-model
     * @return MofPackage for newly loaded metamodel
     * @throws CreationFailedException
     * @throws IOException
     * @throws MalformedXMIException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private MofPackage loadMetaModel(final URL metamodelUri)
        throws Exception
    {
        long start = System.currentTimeMillis();
        if (logger.isDebugEnabled())
        {
            logger.debug("creating MetaModel using URL --> '" + metamodelUri + "'");
        }

        // Use the metamodelUri as the name for the repository extent.
        // This ensures we can load mutiple metamodels without them colliding.
        ModelPackage metaModelExtent = (ModelPackage)repository.getExtent(metamodelUri.toExternalForm());

        if (metaModelExtent == null)
        {
            metaModelExtent = (ModelPackage)repository.createExtent(metamodelUri.toExternalForm());
        }

        MofPackage metaModelPackage = findPackage(
                this.metamodelPackage,
                metaModelExtent);
        if (metaModelPackage == null)
        {
            final XmiReader xmiReader = this.getXMIReaderFactory().createXMIReader();
            xmiReader.read(
                metamodelUri.toExternalForm(),
                metaModelExtent);

            // locate the UML package definition that was just loaded in
            metaModelPackage = findPackage(
                    this.metamodelPackage,
                    metaModelExtent);
        }

        if (logger.isDebugEnabled())
        {
            long duration = System.currentTimeMillis() - start;
            logger.debug("MDRepositoryFacade: loaded metamodel in " + duration + " milliseconds");
        }
        return metaModelPackage;
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#clear()
     */
    public void clear()
    {
        this.removeModel(EXTENT_NAME);
        this.model = null;
        this.modelFacade = null;
    }

    private void removeModel(final String modelUri)
    {
        // remove the model from the repository (if there is one)
        RefPackage model = repository.getExtent(modelUri);
        if (model != null)
        {
            model.refDelete();
        }
    }

    /**
     * Loads a model into the repository and validates the model against the given metaModel.
     *
     * @param modelUris the URIs of the model
     * @param moduleSearchPath the paths to search for shared modules.
     * @param metaModel meta model of model
     * @return populated model
     * @throws CreationFailedException unable to create model in repository
     */
    private RefPackage loadModel(
        final String[] modelUris,
        final String[] moduleSearchPath,
        final MofPackage metaModel)
        throws Exception
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("loadModel: starting to load model from '" + modelUris[0] + "'");
        }
        long start = System.currentTimeMillis();

        RefPackage model = null;
        if (modelUris != null)
        {
            model = this.createModel(metaModel);
            final XmiReader xmiReader =
                this.getXMIReaderFactory().createXMIReader(
                    new MDRXmiReferenceResolver(
                        new RefPackage[] {model},
                        moduleSearchPath));
            try
            {
                final int uriNumber = modelUris.length;
                for (int ctr = 0; ctr < uriNumber; ctr++)
                {
                    final String uri = modelUris[ctr];
                    if (uri != null)
                    {
                        xmiReader.read(
                            modelUris[ctr],
                            model);
                    }
                }
            }
            catch (final Throwable throwable)
            {
                throw new RepositoryFacadeException(throwable);
            }
            if (logger.isDebugEnabled())
            {
                logger.debug("read URIs and created model");
            }
        }

        if (logger.isDebugEnabled())
        {
            long duration = System.currentTimeMillis() - start;
            logger.debug("loadModel: finished loading model in " + duration + " milliseconds.");
        }

        return model;
    }

    /**
     * Loads a model into the repository and validates the model against the given metaModel.
     *
     * @param modelStreams input streams containing the models.
     * @param uris the URIs of the models.
     * @param moduleSearchPaths the paths to search for shared modules.
     * @param metaModel meta model of model
     * @return populated model
     * @throws CreationFailedException unable to create model in repository
     */
    private RefPackage loadModel(
        final InputStream[] modelStreams,
        final String[] uris,
        final String[] moduleSearchPaths,
        final MofPackage metaModel)
        throws Exception
    {
        final RefPackage model = this.createModel(metaModel);
        if (modelStreams != null)
        {
            final XmiReader xmiReader =
                this.getXMIReaderFactory().createXMIReader(
                    new MDRXmiReferenceResolver(
                        new RefPackage[] {model},
                        moduleSearchPaths));
            try
            {
                final int streamNumber = modelStreams.length;
                for (int ctr = 0; ctr < streamNumber; ctr++)
                {
                    final InputStream stream = modelStreams[ctr];
                    String uri = null;
                    if (uris != null)
                    {
                        uri = uris[ctr];
                    }
                    if (stream != null)
                    {
                        xmiReader.read(
                            stream,
                            uri,
                            model);
                    }
                }
            }
            catch (final Throwable throwable)
            {
                throw new RepositoryFacadeException(throwable);
            }
            if (logger.isDebugEnabled())
            {
                logger.debug("read URIs and created model");
            }
        }
        return model;
    }

    /**
     * The name of the extent under which all models loaded into the repository
     * are stored (makes up one big model).
     */
    private static final String EXTENT_NAME = "model";

    /**
     * Constructs the model from the given <code>metaModel</code>.
     *
     * @param metaModel the meta model.
     * @return the package.
     * @throws CreationFailedException
     */
    private RefPackage createModel(final MofPackage metaModel)
        throws CreationFailedException
    {
        RefPackage model = this.repository.getExtent(EXTENT_NAME);
        if (model != null)
        {
            this.removeModel(EXTENT_NAME);
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("creating the new meta model");
        }
        model = repository.createExtent(
                EXTENT_NAME,
                metaModel);
        if (logger.isDebugEnabled())
        {
            logger.debug("created model extent");
        }
        return model;
    }

    /**
     * Searches a meta model for the specified package.
     *
     * @param packageName name of package for which to search
     * @param metaModel   meta model to search
     * @return MofPackage
     */
    private MofPackage findPackage(
        final String packageName,
        final ModelPackage metaModel)
    {
        MofPackage mofPackage = null;
        for (final Iterator iterator = metaModel.getMofPackage().refAllOfClass().iterator(); iterator.hasNext();)
        {
            final javax.jmi.model.ModelElement element = (javax.jmi.model.ModelElement)iterator.next();
            if (element.getName().equals(packageName))
            {
                mofPackage = (MofPackage)element;
                break;
            }
        }
        return mofPackage;
    }
}