package org.andromda.core.uml14;


import java.util.Iterator;
import org.omg.uml.foundation.core.TaggedValue;

/**
 * @author tony
 *
 * 
 */
public class TaggedValueProxy
	extends ModelElementProxy
	implements UMLTaggedValue
{
	
	public static Object newInstance(
		UMLScriptHelper scriptHelper,
		TaggedValue taggedValue)
	{
		Class[] interfaces = {
			UMLTaggedValue.class,
			TaggedValue.class
		};
		
		return java.lang.reflect.Proxy.newProxyInstance(
			taggedValue.getClass().getClassLoader(),
			interfaces,
			new TaggedValueProxy(taggedValue, scriptHelper));
	}


	
	protected TaggedValueProxy(
		TaggedValue taggedValue,
		UMLScriptHelper scriptHelper)
	{
		super(taggedValue,scriptHelper);
	}

	public String getTag()
	{
		return modelElement.getName();
	}
	
	public String getValue()
	{
		TaggedValue tv = (TaggedValue)modelElement;
		
		
		StringBuffer sb = new StringBuffer();
		for (Iterator i = tv.getDataValue().iterator(); i.hasNext(); )
		{
			Object v = i.next();
			sb.append(v.toString());
			
			if (i.hasNext())
			{
				sb.append(" ");
			}
		}
		
		return sb.toString();
	}
}
