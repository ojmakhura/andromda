package org.andromda.core.simpleuml;

import org.andromda.core.uml14.UMLStaticHelper;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Operation;

/**
 * @author tony
 *
 * 
 */
public class PDependency 
	extends PModelElement 
	implements UMLDependency
{

	public static Dependency newInstance(
		UMLStaticHelper scriptHelper,
		Dependency dependency)
	{
		Class[] interfaces = {
			UMLDependency.class,
			Dependency.class
		};
		
		return (Dependency)java.lang.reflect.Proxy.newProxyInstance(
			dependency.getClass().getClassLoader(),
			interfaces,
			new PDependency(dependency, scriptHelper));
	}


	
	protected PDependency(
		Dependency dependency,
		UMLStaticHelper scriptHelper)
	{
		super(dependency,scriptHelper);
	}
	
	public Object getId()
	{
		return modelElement;
	}
	
	public ModelElement getTargetType()
	{
		Dependency dependency = (Dependency)modelElement;
		ModelElement supplier = (ModelElement)dependency.getSupplier().iterator().next();
		
		if (supplier instanceof Attribute)
		{
			return PAttribute.newInstance(
				scriptHelper, (Attribute)supplier );
		}
			
		if (supplier instanceof Operation)
		{
			return POperation.newInstance(
				scriptHelper, (Operation)supplier );
		}
		
		if (supplier instanceof Classifier)
		{
			return PClassifier.newInstance(
				scriptHelper, (Classifier)supplier );
		}
		
		return PModelElement.newInstance(
			scriptHelper,supplier );
				
	}
}
