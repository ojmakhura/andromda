package org.andromda.core.mdr;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;

import org.andromda.core.common.RepositoryFacade;
import org.andromda.core.common.RepositoryReadException;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;

/**
 *  Implements a repository, for models, using the NetBeans MDR (MetaDataRepository).
 *
 *@author    Anthony Mowers
 */
public class MDRepositoryFacade implements RepositoryFacade
{
	private final static String META_PACKAGE = "UML";

	static {
		// configure MDR to use an in-memory storage implementation
		System.setProperty(
			"org.netbeans.mdr.storagemodel.StorageFactoryClassName",
			"org.netbeans.mdr.persistence.memoryimpl.StorageFactoryImpl");
		// set the logging so output does not go to standard out
		System.setProperty("org.netbeans.lib.jmi.Logger.fileName", "mdr.log");
	};

	private URL metaModelURL;
	private URL modelURL;
	private RefPackage model;

	/**
	 * Constructs a Facade around a netbeans MDR (MetaDataRepository).
	 */
    public MDRepositoryFacade()
    {
        // the default metamodel is UML
        metaModelURL = 
            MDRepositoryFacade.class.getResource("/umlmodel.xml");
            
        modelURL = null;
        model = null;
    }
    
	/**
	 * Starts a transaction before reading any models.
	 * 
	 * Doing the model file reads within the context of a transaction seems to
	 * speed up processing.
	 * 
	 * @see org.andromda.core.common.RepositoryFacade#open()
	 */
    public void open()
    {
    	MDRManager.getDefault().getDefaultRepository().beginTrans(true);
    }
    
	/**
	 * Closes a transactions.
	 * 
	 * This can be called after all the models have been read and all querys 
	 * have completed.
	 * 
	 * @see org.andromda.core.common.RepositoryFacade#close()
	 */
    public void close()
    {
    	MDRManager.getDefault().getDefaultRepository().endTrans(true);
    }
    
	/**
	 * @see org.andromda.core.common.RepositoryFacade#readModel(URL)
	 */
	public void readModel(URL modelURL)
		throws RepositoryReadException, IOException
	{
		log("MDR: creating repository");

		this.modelURL = modelURL;

		MDRepository repository =
			MDRManager.getDefault().getDefaultRepository();

		try
		{
			repository.beginTrans(true);
			MofPackage metaModel = loadMetaModel(metaModelURL, repository);

			this.model = loadModel(modelURL, metaModel, repository);
		}
		catch (CreationFailedException cfe)
		{
			throw new RepositoryReadException(
				"unable to create metadata repository",
				cfe);
		}
		catch (MalformedXMIException mxe)
		{
			throw new RepositoryReadException("malformed XMI data", mxe);
		}
		finally {
		}

		log("MDR: created repository");
	}

    /**
     *  Gets the lastModified attribute of the UMLTest object
     *
     *@return    The lastModified value
     */
    public long getLastModified()
    {
        long lastModified = 0;

        try
        {
            URLConnection urlConnection = modelURL.openConnection();
            lastModified = urlConnection.getLastModified();
            // perhaps there should be a corresponding close
            // to match the open. 
            // But, where is the close in the URL API?
        }
        catch (IOException ioe)
        {
            // log and then eat the exception
            log(ioe);
        }

        return lastModified;
    }

    /**
     *  Gets the model attribute of the UMLTest object
     *
     *@return    The model value
     */
    public Object getModel()
    {
        return this.model;
    }

	/**
	 *  Description of the Method
	 *
	 *@param  repository                   Description of the Parameter
	 *@return                              Description of the Return Value
	 *@exception  CreationFailedException  Description of the Exception
	 *@exception  IOException              Description of the Exception
	 *@exception  MalformedXMIException    Description of the Exception
	 */
	private static MofPackage loadMetaModel(
		URL metaModelURL,
		MDRepository repository)
		throws CreationFailedException, IOException, MalformedXMIException
	{
		log("MDR: creating MetaModel using URL =" + metaModelURL.toExternalForm());

        // Use the metaModelURL as the name for the repository extent.
        // This ensures we can load mutiple metamodels without them colliding.
		ModelPackage metaModelExtent =
			(ModelPackage) repository.getExtent(metaModelURL.toExternalForm());

		if (metaModelExtent == null)
		{
			metaModelExtent =
				(ModelPackage) repository.createExtent(
					metaModelURL.toExternalForm());
		}

		MofPackage metaModelPackage =
			findPackage(META_PACKAGE, metaModelExtent);
		if (metaModelPackage == null)
		{
			XMIReader xmiReader =
				XMIReaderFactory.getDefault().createXMIReader();

			xmiReader.read(metaModelURL.toExternalForm(), metaModelExtent);

			// locate the UML package definition that was just loaded in
			metaModelPackage = findPackage(META_PACKAGE, metaModelExtent);
		}

		log("MDR: created MetaModel");
		return metaModelPackage;
	}

	/**
	 *  Description of the Method
	 *
	 *@param  modelURL                     url of model
	 *@param  repository                   netbeans MDR
	 *@param  metaModel                    meta model of model
	 *@return  populate model
	 *@exception  CreationFailedException unable to create model in repository
	 *@exception  IOException  unable to read model
	 *@exception  MalformedXMIException model violates metamodel
	 */
	private static RefPackage loadModel(
		URL modelURL,
		MofPackage metaModel,
		MDRepository repository)
		throws CreationFailedException, IOException, MalformedXMIException
	{
		log("MDR: creating Model");

		RefPackage model = repository.getExtent("MODEL");
		if (model != null)
		{
			log("MDR: deleting exising model");
			model.refDelete();
		}

		log("MDR: creating model extent");
		model = repository.createExtent("MODEL", metaModel);
		log("MDR: created model extent");

		XMIReader xmiReader = XMIReaderFactory.getDefault().createXMIReader();

		log("MDR: reading XMI - " + modelURL.toExternalForm());
		try
		{
			xmiReader.read(modelURL.toExternalForm(), model);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new IOException("could not read XMI");
		}
		log("MDR: read XMI ");

		log("MDR: created Model");
		return model;
	}

	/**
	 *  Description of the Method
	 *
	 *@param  packageName  Description of the Parameter
	 *@param  model        Description of the Parameter
	 *@return              Description of the Return Value
	 */
	private static MofPackage findPackage(
		String packageName,
		ModelPackage model)
	{
		for (Iterator it = model.getMofPackage().refAllOfClass().iterator();
			it.hasNext();
			)
		{
			javax.jmi.model.ModelElement temp =
				(javax.jmi.model.ModelElement) it.next();

			if (temp.getName().equals(packageName))
			{
				return (MofPackage) temp;
			}
		}

		return null;
	}

	
	// a method for logging that is stubbed out now
	static private void log(Object object)
	{
        
	}
}
