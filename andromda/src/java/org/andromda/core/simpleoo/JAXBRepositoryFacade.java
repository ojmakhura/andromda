package org.andromda.core.simpleoo;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.andromda.core.common.RepositoryFacade;
import org.andromda.core.common.RepositoryReadException;
import org.andromda.core.xml.Model;

/**
 *  Implements a repository for simple object oriented models using JAXB.
 *
 *@author    Matthias Bohlen
 */
public class JAXBRepositoryFacade
	 implements RepositoryFacade
{
	private URL modelURL;
	private Model model;


	/**
	 * @see org.andromda.core.common.RepositoryFacade#readModel(URL)
	 */
	public void readModel(URL modelURL) throws 
		IOException, 
		RepositoryReadException
	{
		this.modelURL = modelURL;
		model = null;

		try
		{
			// let JAXB read the XML file and unmarshal it into a model tree
			JAXBContext jc =
				JAXBContext.newInstance("org.andromda.core.xml");

			Unmarshaller u = jc.createUnmarshaller();

			model = (Model) u.unmarshal(modelURL);

		}
		catch (JAXBException jbe)
		{
			// We include the complete stack trace here because
			// sometimes there is an exception without its own message
			// but with a nested exception that says the real reason!
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			jbe.printStackTrace(pw);
			pw.close();

			throw new RepositoryReadException(
				"Error while XML-parsing the model "
				 + modelURL.toExternalForm()
				 + ": "
				 + sw.toString());
		}

	}


	/**
	 *  Gets the lastModified attribute of the JAXBMetaDataRepositoryFacade object
	 *
	 *@return    The lastModified value
	 */
	public long getLastModified()
	{
        long lastModified = 0;
        
        try {
            lastModified = modelURL.openConnection().getLastModified();
        }
        catch (IOException ioe)
        {
            // eat the exception
        }
        
        return lastModified;
	}



	/**
	 *  Gets the model attribute of the MetaDataRepository object
	 *
	 *@return    The model value
	 */
	public Object getModel()
	{
		return model;
	}


}

