package org.andromda.core.common;

import java.util.Collection;

/**
 * Provides the basic operations needed by the template engine
 * to locate objects in a model and get sufficient information
 * about those objects to apply code generate templates.
 *
 * @author Anthony Mowers
 */
public interface ScriptHelper
{

	/**
	 * Associates a model with this script helper.
	 * 
	 * This method is usually called by the template engine after it
	 * has fetched a model object from the repository.
	 * 
	 * @param model
	 */
	public void setModel(Object model);

    /**
     * Returns the model object.
     * 
     * In it is possible for the getModel to return a different object
     * than was set in the setModel operation. This can happen when
     * the script helper wants to wrap the model object with some helper
     * operations
     */
    public Object getModel();
    
	/**
	 * Associates mapping rules for mapping model datatypes 
	 * to database data types.
	 * 
	 * This method is usually called by the template engine in
	 * order to provide the script helper with rules about how
	 * to convert model data types into implementation specific
	 * data types.
	 * 
	 * @param mappings database mapping to be use by this scripthelper
	 */
	public void setTypeMappings(DbMappingTable mappings);

	/**
	 * Given a repository extracted model element this operation
	 * returns the collection of associated stereotypes.
	 *
	 * <p>A stereotype name is used by the template engine to
	 * locate the set of templates to apply to an object carrying
	 * that stereotype name.</p>.  
	 * 
	 *@param modelElement an object from the model
	 *@return Collection of strings representing stereotype names
	 */
	public Collection getStereotypeNames(Object modelElement);

	/**
	 * Returns a human readable name for the model element.
	 * 
	 * <p>The template engine usually use this name to help construct
	 * a file name for output of the code generation templates.</p>
	 * 
	 *@param  modelElement  an object from the model
	 *@return String representing the objects name
	 */
	public String getName(Object modelElement);

	/**
	 * Returns a human readable name for the package which
	 * contains the given model element.
	 *
	 *@param  modelElement  an object from the model
	 *@return String representing the objects package name
	 */
	public String getPackageName(Object modelElement);

	/**
	 * Returns a collection of all the objects contained by
	 * the model.
	 * 
	 * <p>It does not really have to be all the objects. It only
	 * needs to be those objects which might need processing
	 * by the template engine.</p>
	 *
	 *@return    The modelElements value
	 */
	public Collection getModelElements();

}
