package org.andromda.core.common;

import java.util.Collection;

/**
 * An interface for objects responsible for providing the code generation engine and the
 * code generation scripts with a basic level of visibility into the object model.
 * 
 * <p> This interface is the minimal set of API that must be supported by an object model
 * in order for it to be usable by ANDROMDA.  If an object model can support this API it
 * can then be used by AndroMDA. </p>
 * 
 * @see org.andromda.core.common.RepositoryFacade
 * 
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public interface ScriptHelper
{

	/**
	 * sets the model
	 * 
	 * @param model top level object in object model
	 */
	public void setModel(Object model);

    /**
     * returns the top level object of the object model
     */
    public Object getModel();
    
	/**
	 * sets rules for how to map model types to JDBC and SQL types
	 * 
	 * @param mappings database type mapper object
	 */
	public void setTypeMappings(DbMappingTable mappings);

	/**
	 * returns the collection of stereotypes names associated with the
     * given model element.
     * 
	 *@param modelElement an object from the model
	 *@return Collection of strings representing stereotype names
	 */
	public Collection getStereotypeNames(Object modelElement);

	/**
	 * returns a human readable name for given model element.
	 * 
	 *@param  modelElement  an object from the model
	 *@return String representing the objects name
	 */
	public String getName(Object modelElement);

	/**
	 * returns a human readable name for the package in the model
     * that contains the given model element.
	 *
	 *@param  modelElement  an object from the model
	 *@return String representing the objects package name
	 */
	public String getPackageName(Object modelElement);

	/**
	 * returns the collection of model elements that are contained
     * in the model
	 *
	 *@return  Collection of model elements
	 */
	public Collection getModelElements();

}
