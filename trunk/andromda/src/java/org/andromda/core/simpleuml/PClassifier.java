package org.andromda.core.simpleuml;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.andromda.core.uml14.UMLStaticHelper;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.Operation;


/**
 * dynamic proxy for a Classifier: dynamically supports the UMLClassifier, 
 * and org.omg.uml.foundation.core.Classifier interfaces.
 *
 *@author    <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public class PClassifier
	extends PModelElement
	implements UMLClassifier
{
	/**
	 *  Description of the Method
	 *
	 *@param  classifier    Description of the Parameter
	 *@param  scriptHelper  Description of the Parameter
	 *@return               Description of the Return Value
	 */
	public static Classifier newInstance(
		UMLStaticHelper scriptHelper,
		Classifier classifier)
	{
		Class[] interfaces = {
			UMLClassifier.class,
			Classifier.class
		};
		
		return (Classifier)java.lang.reflect.Proxy.newProxyInstance(
			classifier.getClass().getClassLoader(),
			interfaces,
			new PClassifier(classifier, scriptHelper));
	}


	
	protected PClassifier(
		Classifier classifier,
		UMLStaticHelper scriptHelper)
	{
		super(classifier,scriptHelper);
	}



	/**
	 *  Gets the attributes attribute of the ClassifierProxy object
	 *
	 *@return    The attributes value
	 */
	public Collection getAttributes()
	{
		Collection attributes = scriptHelper.getAttributes(modelElement);
		Collection attributeProxies = new Vector();
		
		for (Iterator i = attributes.iterator(); i.hasNext(); )
		{
			attributeProxies.add(
				PAttribute.newInstance(scriptHelper,(Attribute)i.next()) 
				);
		}
		
		return attributeProxies;
	}


	/**
	 *  Gets the dependencies attribute of the ClassifierProxy object
	 *
	 *@return    The dependencies value
	 */
	public Collection getDependencies()
	{
		Collection dependencies = scriptHelper.getDependencies(modelElement);
		Collection dependencyProxies = new Vector();
		
		for (Iterator i = dependencies.iterator(); i.hasNext(); )
		{
			dependencyProxies.add(
				PDependency.newInstance(scriptHelper,(Dependency)i.next())
			);
		}
		
		return dependencyProxies;
	}


	/**
	 *  Gets the associationLinks attribute of the ClassifierProxy object
	 *
	 *@return    The associationLinks value
	 */
	public Collection getAssociationLinks()
	{
		return scriptHelper.getAssociationEnds(modelElement);
	}


	/**
	 *  Gets the package attribute of the ClassifierProxy object
	 *
	 *@return    The package value
	 */
	public Object getPackage()
	{
		return this.modelElement;
	}

	public Collection getOperations()
	{
		Collection operations = scriptHelper.getOperations(modelElement);
		Collection operationProxies = new Vector();


		for (Iterator i = operations.iterator(); i.hasNext(); )
		{
			Object proxy = 
				POperation.newInstance(
					scriptHelper, 
					(Operation) (i.next() )
				);
				
			operationProxies.add(proxy);

		}
		
		
		return operationProxies;
	}
	
}

