package org.andromda.core.common;

import java.io.File;
import java.net.URL;
import java.io.IOException;

/**
 * A facade that is used to hide the underlying data repository technology.
 *
 * A data repository is a facility for loading and retrieving an object model.
 *  
 * @author    amowers
 */
public interface RepositoryFacade
{

	/**
	 * Reads an object model from a specified URL
     * 
     * <p>The model must conform to the meta model used by the repository</p>
     * 
	 * @param modelURL url of model
	 * @throws MetaDataReadException if model syntax is violated
	 * @throws IOException if io error occurs during file read
	 */
	public void readModel(URL modelURL) throws RepositoryReadException, IOException;

	/**
	 *  Returns the date/time of when the model was last modified
	 *
	 *@return    The lastModified value
	 */
	public long getLastModified();

	/**
	 *  Gets the top-level model object from the repository
	 *
	 *@return    The model value
	 */
	public Object getModel();

}
