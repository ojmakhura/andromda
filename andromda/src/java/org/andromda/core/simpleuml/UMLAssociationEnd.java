package org.andromda.core.simpleuml;

import org.omg.uml.foundation.core.Classifier;
/**
 * 
 * @author Anthony Mowers
 */
public interface UMLAssociationEnd
	extends UMLModelElement
{
	public Classifier getType();
	public String getRoleName();
	public Object getId();
	public String getNavigable();
}
