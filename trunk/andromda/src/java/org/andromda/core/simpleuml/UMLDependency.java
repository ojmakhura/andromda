package org.andromda.core.simpleuml;


import org.omg.uml.foundation.core.ModelElement;


/**
 * @author Anthony Mowers
 *
 * 
 */
public interface UMLDependency
	extends UMLModelElement
{
	public Object getId();
	public ModelElement getTargetType();
}
