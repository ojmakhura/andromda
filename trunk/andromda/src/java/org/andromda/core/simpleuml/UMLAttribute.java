package org.andromda.core.simpleuml;

/**
 * defines those methods missing from Attribute in the UML 1.4 schema that are 
 * needed by the UML2EJB based code generation scripts.
 * 
 * @author Anthony Mowers
 */
public interface UMLAttribute
	extends UMLModelElement
{
	public Object getId();
}

