package org.andromda.core.uml14;

import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Classifier;

/**
 * @author amowers
 *
 * 
 */
public class AssociationEndProxy
	extends ModelElementProxy
	implements UMLAssociationEnd
{
	public static AssociationEnd newInstance(
		UMLScriptHelper scriptHelper,
		AssociationEnd associationEnd)
	{
		Class[] interfaces = {
			UMLAssociationEnd.class,
			AssociationEnd.class
		};
		
		return (AssociationEnd)java.lang.reflect.Proxy.newProxyInstance(
			associationEnd.getClass().getClassLoader(),
			interfaces,
			new AssociationEndProxy(associationEnd, scriptHelper));
	}


	
	protected AssociationEndProxy(
		AssociationEnd associationEnd,
		UMLScriptHelper scriptHelper)
	{
		super(associationEnd,scriptHelper);
	}
	
	public String getRoleName()
	{
		String roleName = modelElement.getName();
		if ( (roleName == null) || (roleName.length() == 0) )
		{
			AssociationEnd ae = (AssociationEnd)modelElement;
			roleName = "The" + ae.getParticipant().getName();
		}
		
		return roleName;
	}
	
	public Classifier getType()
	{
		AssociationEnd ae = (AssociationEnd)modelElement;
		
		return ClassifierProxy.newInstance(
			scriptHelper, ae.getParticipant());
	}
	
	public Object getId()
	{
		return modelElement.refMofId();
	}
	
	public String getNavigable()
	{
		AssociationEnd ae = (AssociationEnd)modelElement;
		
		return ae.isNavigable() ? "true" : "false";
	}
}
