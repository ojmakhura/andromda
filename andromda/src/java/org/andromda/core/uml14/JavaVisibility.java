package org.andromda.core.uml14;

import java.util.List;

import org.omg.uml.foundation.datatypes.VisibilityKind;

/**
 * @author tony
 *
 * 
 */
public class JavaVisibility implements VisibilityKind
{
	private String visibility;
	
	public JavaVisibility(String visibility)
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
