package org.andromda.core.metadecorators.uml14;

import java.util.List;

import org.omg.uml.foundation.datatypes.VisibilityKind;

/**
 * 
 * Implements an enumeration of the four types of operation/attribute visibilities in
 * java: private,public, protected and 'package' level visibility.
 * 
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 *
 */
public class JavaVisibilityEnum implements VisibilityKind
{
    public final static VisibilityKind PRIVATE = new JavaVisibilityEnum("private");
    public final static VisibilityKind PUBLIC = new JavaVisibilityEnum("public");
    public final static VisibilityKind PROTECTED = new JavaVisibilityEnum("protected");
    public final static VisibilityKind PACKAGE = new JavaVisibilityEnum("");
    
	private String visibility;
	
	private JavaVisibilityEnum(String visibility)
	{
		this.visibility = visibility;
	}

	public String toString()
	{
		return visibility;
	}
	
	/**
	 * @see javax.jmi.reflect.RefEnum#refTypeName()
	 */
	public List refTypeName()
	{
		return null;
	}

}
