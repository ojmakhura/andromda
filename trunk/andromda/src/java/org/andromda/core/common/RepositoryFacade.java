package org.andromda.core.common;

import java.io.IOException;
import java.net.URL;

/**
 * An interface for objects responsible for being a repository into which an object model can 
 * be loaded.
 * 
 * <p> AndroMDA does code generation from an object model.  There must exist a repository in which
 * the model can be loaded.  The repository must be able to load the object model 
 * given a URL. Any repository that supports this API can be used by AndroMDA. </p>
 * 
 * @see org.andromda.core.common.ScriptHelper
 * 
 * @author    Anthony Mowers
 */
public interface RepositoryFacade
{
	
	/**
	 * open and initialize the repository.
	 *
	 */
	public void open();
	
	
	/**
	 * close the repository and reclaim all resources
	 * 
	 */
	public void close();
	
	/**
	 * read the object model into the repository from the given URL
     * 
     * <p> An URLs can be used to point to files on the filesystem, 
     * a file in a jar file, a file from a website, data from a database, etc... </p>
     * 
	 * @param modelURL url of model
	 * @throws MetaDataReadException if model syntax is violated
	 * @throws IOException if io error occurs during file read
	 */
	public void readModel(URL modelURL) throws RepositoryReadException, IOException;

	/**
	 *  returns the date and time of when the model was last modified
	 *
	 *@return the lastModified time
	 */
	public long getLastModified();

	/**
	 *  returns the top-level model object from the repository
	 *
	 *@return    The model value
	 */
	public Object getModel();

}
