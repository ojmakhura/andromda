package org.andromda.core.simpleuml;


import org.andromda.core.uml14.UMLStaticHelper;
import org.omg.uml.foundation.core.AssociationEnd;

/**
 * Implements operations for querying an assocation from the perspective
 * of one of the assocation ends.
 * 
 * @author Anthony Mowers
 */
public class DirectionalAssociationEnd
    extends org.andromda.core.uml14.DirectionalAssociationEnd
{
	private UMLStaticHelper scriptHelper;

	public DirectionalAssociationEnd(
        UMLStaticHelper scriptHelper,
		AssociationEnd associationEnd)
	{
        super(associationEnd);
        this.scriptHelper = scriptHelper;
	}

	public AssociationEnd getSource()
	{
		return PAssociationEnd.newInstance(scriptHelper,associationEnd);
	}


	public AssociationEnd getTarget()
	{
		return PAssociationEnd.newInstance(scriptHelper,getOtherEnd());	
	}

		
}
