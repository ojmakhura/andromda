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
 *  Description of the Interface
 *
 *@author    tony
 */
public class JAXBRepositoryFacade
	 implements RepositoryFacade
{
	private URL modelURL;
	private Model model;


	/**
	 *  Constructor for the JAXBMetaDataRepository object
	 *
	 *@param  typeMappings               Description of the Parameter
	 *@param  inFile                     Description of the Parameter
	 *@exception  MetaDataReadException  Description of the Exception
	 *@exception  IOException            Description of the Exception
	 */
	public void read(URL modelURL) throws 
		IOException, 
		RepositoryReadException
	{
		this.modelURL = modelURL;
		model = null;

		try
		{
			// let JAXB read the XML file and unmarshal it into a model tree
			JAXBContext jc =
				JAXBContext.newInstance("de.mbohlen.tools.uml2ejb.xml");

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
		return modelURL.openConnection().getLastModified();
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

