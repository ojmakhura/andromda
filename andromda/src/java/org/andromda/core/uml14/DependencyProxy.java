package org.andromda.core.uml14;

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
public class DependencyProxy 
	extends ModelElementProxy 
	implements UMLDependency
{

	public static Dependency newInstance(
		UMLScriptHelper scriptHelper,
		Dependency dependency)
	{
		Class[] interfaces = {
			UMLDependency.class,
			Dependency.class
		};
		
		return (Dependency)java.lang.reflect.Proxy.newProxyInstance(
			dependency.getClass().getClassLoader(),
			interfaces,
			new DependencyProxy(dependency, scriptHelper));
	}


	
	protected DependencyProxy(
		Dependency dependency,
		UMLScriptHelper scriptHelper)
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
			return AttributeProxy.newInstance(
				scriptHelper, (Attribute)supplier );
		}
			
		if (supplier instanceof Operation)
		{
			return OperationProxy.newInstance(
				scriptHelper, (Operation)supplier );
		}
		
		if (supplier instanceof Classifier)
		{
			return ClassifierProxy.newInstance(
				scriptHelper, (Classifier)supplier );
		}
		
		return ModelElementProxy.newInstance(
			scriptHelper,supplier );
				
	}
}
