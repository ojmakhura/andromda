package org.andromda.core.simpleuml;

import java.util.Collection;

/**
 *  Description of the Interface
 *
 *@author    Anthony Mowers
 */
public interface UMLClassifier
	extends UMLModelElement
{
	/**
	 *  Gets the attributes attribute of the UMLClassifier object
	 *
	 *@return    The attributes value
	 */
	Collection getAttributes();


	/**
	 *  Gets the dependencies attribute of the UMLClassifier object
	 *
	 *@return    The dependencies value
	 */
	Collection getDependencies();


	/**
	 *   Gets the operations attribute of the UMLClassifier object
	 * 
	 * @return Collection
	 */
	Collection getOperations();
	
	/**
	 *  Gets the associationLinks attribute of the UMLClassifier object
	 *
	 *@return    The associationLinks value
	 */
	Collection getAssociationLinks();


	/**
	 *  Gets the package attribute of the UMLClassifier object
	 *
	 *@return    The package value
	 */
	Object getPackage();

}

