package org.andromda.core.uml14;

import org.omg.uml.foundation.core.Attribute;


/**
 *  Description of the Class
 *
 *@author    amowers
 */
public class AttributeProxy
	extends ModelElementProxy
	implements UMLAttribute
{
	private UMLScriptHelper scriptHelper;


	/**
	 *  Description of the Method
	 *
	 *@param  classifier    Description of the Parameter
	 *@param  scriptHelper  Description of the Parameter
	 *@return               Description of the Return Value
	 */
	public static Attribute newInstance(
		UMLScriptHelper scriptHelper,
		Attribute attribute)
	{
		Class[] interfaces = new Class[]
			{
			UMLAttribute.class,
			Attribute.class
			};

		return (Attribute)java.lang.reflect.Proxy.newProxyInstance(
			attribute.getClass().getClassLoader(),
			interfaces,
			new AttributeProxy(attribute, scriptHelper));
	}


	
	private AttributeProxy(
		Attribute attribute,
		UMLScriptHelper scriptHelper)
	{
		super(attribute,scriptHelper);
	}


	public Object getId()
	{
		return modelElement;
	}

}

