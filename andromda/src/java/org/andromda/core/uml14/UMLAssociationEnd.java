package org.andromda.core.uml14;

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
	public String getId();
	public String getNavigable();
}
