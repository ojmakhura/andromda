package org.andromda.repositories.mdr;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.repository.RepositoryFacade;
import org.andromda.core.repository.RepositoryFacadeException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.netbeans.api.xmi.XMIWriter;
import org.netbeans.api.xmi.XMIWriterFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.Iterator;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;


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
    private static Logger logger = Logger.getLogger(MDRepositoryFacade.class);
    protected final static String META_PACKAGE = "UML";
    private ModelAccessFacade modelFacade = null;
    private static MDRepository repository = null;
    protected URL metaModelURL;
    protected RefPackage model = null;

    /**
     * Constructs a Facade around a netbeans MDR (MetaDataRepository).
     */
    public MDRepositoryFacade()
    {
        // configure MDR to use an in-memory storage implementation
        System.setProperty(
            "org.netbeans.mdr.storagemodel.StorageFactoryClassName",
            "org.netbeans.mdr.persistence.memoryimpl.StorageFactoryImpl");

        // set the logging so output does not go to standard out
        System.setProperty("org.netbeans.lib.jmi.Logger.fileName", "mdr.log");
        repository = MDRManager.getDefault().getDefaultRepository();

        final String metamodelUri = "/M2_DiagramInterchangeModel.xml";

        // the default metamodel is now UML 1.4 plus UML 2.0 diagram extensions
        metaModelURL = MDRepositoryFacade.class.getResource(metamodelUri);

        if (metaModelURL == null)
        {
            throw new RepositoryFacadeException("Could not find meta model --> ' " + metamodelUri + "'");
        }
    }

    /**
     * Opens the repository and prepares it to read in models.
     * <p/>
     * All the file reads are done within the context of a transaction: this seems to speed up the processing. </p>
     *
     * @see org.andromda.core.repository.RepositoryFacade#open()
     */
    public void open()
    {
        repository.beginTrans(true);
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#clear()
     */
    public void clear()
    {
        // remove the model from the repository (if there is one)
        RefPackage model = repository.getExtent(EXTENT_NAME);
        if (model != null)
        {
            model.refDelete();
        }
        this.model = null;
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
        repository.endTrans(false);
        this.clear();
        MDRManager.getDefault().shutdownAll();
    }

    /**
     * @see org.andromda.core.common.RepositoryFacade#readModel(java.net.URL, java.lang.String[])
     */
    public void readModel(
        final URL modelURL,
        final String[] moduleSearchPath)
    {
        try
        {
            final MofPackage metaModel = loadMetaModel(metaModelURL);
            this.model = loadModel(modelURL, moduleSearchPath, metaModel);
        }
        catch (final Throwable throwable)
        {
            throw new RepositoryFacadeException(throwable);
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("created repository");
        }
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#readModel(java.io.InputStream, java.lang.String, java.lang.String[])
     */
    public void readModel(
        final InputStream stream,
        final String uri,
        final String[] moduleSearchPath)
    {
        try
        {
            final MofPackage metaModel = loadMetaModel(metaModelURL);
            this.model = loadModel(stream, uri, moduleSearchPath, metaModel);
        }
        catch (final Throwable throwable)
        {
            throw new RepositoryFacadeException(throwable);
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("created repository");
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
        this.writeModel(model, outputLocation, xmiVersion, null);
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
        final String methodName = "MDRepositoryFacade.writeMode";
        ExceptionUtils.checkNull(methodName, "model", model);
        ExceptionUtils.checkNull(methodName, "outputLocation", outputLocation);
        ExceptionUtils.checkAssignable(
            methodName,
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
            xmiWriter.write(outputStream, outputLocation, (RefPackage)model, xmiVersion);
            outputStream.close();
            outputStream = null;
        }
        catch (final Throwable throwable)
        {
            throw new RepositoryFacadeException(throwable);
        }
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
                    (ModelAccessFacade)ComponentContainer.instance().findComponent(ModelAccessFacade.class);
                if (this.modelFacade == null)
                {
                    throw new RepositoryFacadeException(
                        "Could not find implementation for the component --> '" + ModelAccessFacade.class + "'");
                }
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
     * @param repository MetaDataRepository
     * @return MofPackage for newly loaded metamodel
     * @throws CreationFailedException
     * @throws IOException
     * @throws MalformedXMIException
     */
    private static MofPackage loadMetaModel(final URL metaModelURL)
        throws CreationFailedException, IOException, MalformedXMIException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("creating MetaModel using URL --> '" + metaModelURL + "'");
        }

        // Use the metaModelURL as the name for the repository extent.
        // This ensures we can load mutiple metamodels without them colliding.
        ModelPackage metaModelExtent = (ModelPackage)repository.getExtent(metaModelURL.toExternalForm());

        if (metaModelExtent == null)
        {
            metaModelExtent = (ModelPackage)repository.createExtent(metaModelURL.toExternalForm());
        }

        MofPackage metaModelPackage = findPackage(META_PACKAGE, metaModelExtent);

        if (metaModelPackage == null)
        {
            XMIReader xmiReader = XMIReaderFactory.getDefault().createXMIReader();
            xmiReader.read(
                metaModelURL.toExternalForm(),
                metaModelExtent);

            // locate the UML package definition that was just loaded in
            metaModelPackage = findPackage(META_PACKAGE, metaModelExtent);
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("created MetaModel");
        }
        return metaModelPackage;
    }

    /**
     * The name (unique within the repository) for the new package extent
     */
    private static final String EXTENT_NAME = "MODEL";

    /**
     * Loads a model into the repository and validates the model against the given metaModel.
     *
     * @param modelURL  url of model
     * @param moduleSearchPath the paths to search for shared modules.
     * @param metaModel meta model of model
     * @return populated model
     * @throws CreationFailedException unable to create model in repository
     */
    private final RefPackage loadModel(
        final URL modelURL,
        final String[] moduleSearchPath,
        final MofPackage metaModel)
        throws CreationFailedException
    {
        final RefPackage model = this.getModel(metaModel);
        if (modelURL != null)
        {
            final XMIReader xmiReader =
                XMIReaderFactory.getDefault().createXMIReader(
                    new MDRXmiReferenceResolver(
                        new RefPackage[] {model},
                        moduleSearchPath));
            if (logger.isDebugEnabled())
            {
                logger.debug("reading model XMI --> '" + modelURL + "'");
            }
            try
            {
                xmiReader.read(
                    modelURL.toString(),
                    model);
            }
            catch (final Throwable throwable)
            {
                throw new RepositoryFacadeException(throwable);
            }
            if (logger.isDebugEnabled())
            {
                logger.debug("read XMI and created model");
            }
        }
        return model;
    }

    /**
     * Loads a model into the repository and validates the model against the given metaModel.
     *
     * @param modelStream an input stream containing the model.
     * @param uri the URI of the model.
     * @param moduleSearchPath the paths to search for shared modules.
     * @param metaModel meta model of model
     * @return populated model
     * @throws CreationFailedException unable to create model in repository
     */
    private final RefPackage loadModel(
        final InputStream modelStream,
        final String uri,
        final String[] moduleSearchPath,
        final MofPackage metaModel)
        throws CreationFailedException
    {
        final RefPackage model = this.getModel(metaModel);
        if (modelStream != null)
        {
            final XMIReader xmiReader =
                XMIReaderFactory.getDefault().createXMIReader(
                    new MDRXmiReferenceResolver(
                        new RefPackage[] {model},
                        moduleSearchPath));
            try
            {
                xmiReader.read(modelStream, uri, model);
            }
            catch (Throwable throwable)
            {
                throw new RepositoryFacadeException(throwable);
            }
            if (logger.isDebugEnabled())
            {
                logger.debug("read XMI and created model");
            }
        }
        return model;
    }

    /**
     * Constructs the model from the given <code>metaModel</code>.
     *
     * @param metaModel the meta model.
     * @return the package.
     * @throws CreationFailedException
     */
    private RefPackage getModel(final MofPackage metaModel)
        throws CreationFailedException
    {
        RefPackage model = repository.getExtent(EXTENT_NAME);
        if (model == null)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("creating the new meta model");
            }
            model = repository.createExtent(EXTENT_NAME, metaModel);
            if (logger.isDebugEnabled())
            {
                logger.debug("created model extent");
            }
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
    private final static MofPackage findPackage(
        String packageName,
        ModelPackage metaModel)
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