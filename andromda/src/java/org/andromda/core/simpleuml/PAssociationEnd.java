package org.andromda.core.simpleuml;

import org.andromda.core.uml14.UMLStaticHelper;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Classifier;

/**
 * @author amowers
 *
 * 
 */
public class PAssociationEnd
	extends PModelElement
	implements UMLAssociationEnd
{
	public static AssociationEnd newInstance(
		UMLStaticHelper scriptHelper,
		AssociationEnd associationEnd)
	{
		Class[] interfaces = {
			UMLAssociationEnd.class,
			AssociationEnd.class
		};
		
		return (AssociationEnd)java.lang.reflect.Proxy.newProxyInstance(
			associationEnd.getClass().getClassLoader(),
			interfaces,
			new PAssociationEnd(associationEnd, scriptHelper));
	}


	
	protected PAssociationEnd(
		AssociationEnd associationEnd,
		UMLStaticHelper scriptHelper)
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
		
		return PClassifier.newInstance(
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
