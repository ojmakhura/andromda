package org.andromda.core.simpleuml;

import org.omg.uml.foundation.core.Classifier;

/**
 * defines those methods missing from AssocationEnd in the UML 1.4 schema that are 
 * needed by the UML2EJB based code generation scripts.
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
