package org.andromda.core.simpleuml;

/**
 * defines those methods missing from the TaggedValue in the UML 1.4 schema that are 
 * needed by the UML2EJB based code generation scripts.
 * 
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public interface UMLTaggedValue
	extends UMLModelElement
{
	public String getTag();
	public String getValue();
}
