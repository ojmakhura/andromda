package org.andromda.core.simpleuml;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.andromda.core.uml14.UMLStaticHelper;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TaggedValue;


/**
 *  Description of the Class
 *
 *@author    Anthony Mowers
 */
public class PModelElement
	 implements 
	 	java.lang.reflect.InvocationHandler, 
	 	UMLModelElement
{	
	protected ModelElement modelElement;
	protected UMLStaticHelper scriptHelper;
	
	public static ModelElement newInstance(
		UMLStaticHelper scriptHelper,
		ModelElement modelElement)
	{
		Class[] interfaces = {
			UMLClassifier.class,
			ModelElement.class
		};
		
		return (ModelElement)java.lang.reflect.Proxy.newProxyInstance(
			modelElement.getClass().getClassLoader(),
			interfaces,
			new PModelElement(modelElement, scriptHelper));
	}

	protected PModelElement(
		ModelElement modelElement,
		UMLStaticHelper scriptHelper)
	{
		this.scriptHelper = scriptHelper;
		this.modelElement = modelElement;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  proxy          Description of the Parameter
	 *@param  m              Description of the Parameter
	 *@param  args           Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Throwable  Description of the Exception
	 */
	public Object invoke(Object proxy, Method m, Object[] args)
		throws Throwable
	{
		if (m.getDeclaringClass().isAssignableFrom(this.getClass()))
		{
			return m.invoke(this, args);
		}

		return m.invoke(modelElement, args);
	}

	public Collection getTaggedValues()
	{
		Collection taggedValues = scriptHelper.getTaggedValues(modelElement);
		Collection taggedValueProxies = new Vector();
		
		for (Iterator i = taggedValues.iterator(); i.hasNext(); )
		{
			TaggedValue taggedValue = (TaggedValue)i.next();
			taggedValueProxies.add(
				PTaggedValue.newInstance(scriptHelper,taggedValue) 
				);
		}
		
		
		return taggedValueProxies;
	}
    
    public Object getId()
    {
        return this.modelElement;
    }
}

