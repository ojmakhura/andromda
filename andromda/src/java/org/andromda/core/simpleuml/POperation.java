package org.andromda.core.simpleuml;


import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.andromda.core.uml14.UMLStaticHelper;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;

/**
 * dynamic proxy for an Operation: dynamically supports the UMLOperation, 
 * and org.omg.uml.foundation.core.Operation interfaces.
 *
 *@author    Anthony Mowers
 */
public class POperation extends PModelElement implements UMLOperation
{
	private UMLStaticHelper scriptHelper;

	/**
	 *  Description of the Method
	 *
	 *@param  operation    Description of the Parameter
	 *@param  scriptHelper  Description of the Parameter
	 *@return               Description of the Return Value
	 */
	public static Operation newInstance(
		UMLStaticHelper scriptHelper,
		Operation operation)
	{
		Class[] interfaces =
			new Class[] { UMLOperation.class, Operation.class };

		return (Operation) java.lang.reflect.Proxy.newProxyInstance(
			operation.getClass().getClassLoader(),
			interfaces,
			new POperation(operation, scriptHelper));
	}

	private POperation(Operation operation, UMLStaticHelper scriptHelper)
	{
		super(operation, scriptHelper);
	}

	public Object getId()
	{
		return this.modelElement;
	}

	public VisibilityKind getVisibility()
	{
		VisibilityKind visibility;

		visibility = ((Operation)modelElement).getVisibility();
		if (VisibilityKindEnum.VK_PRIVATE.equals(visibility))
		{
			return JavaVisibilityEnum.PRIVATE;
		}
		else if (VisibilityKindEnum.VK_PROTECTED.equals(visibility))
		{
			return JavaVisibilityEnum.PROTECTED;
		}
		else if (VisibilityKindEnum.VK_PUBLIC.equals(visibility))
		{
			return JavaVisibilityEnum.PUBLIC;
		}

		return JavaVisibilityEnum.PACKAGE;
	}

    public Collection getParameters()
    {
        Collection parms = ((Operation)modelElement).getParameter();
        Vector ret = new Vector();
     
        for (Iterator i = parms.iterator(); i.hasNext(); )
        {
            Parameter p = (Parameter) i.next();
            if ( !ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind())) 
            {
                ret.add(p);
            }
        }
        
        return ret;
    }
    
    public Object getType()
    {
        Collection parms = ((Operation)modelElement).getParameter();
        for (Iterator i = parms.iterator(); i.hasNext(); )
        {
            Parameter p = (Parameter) i.next();
            if ( ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind())) 
            {
               return p.getType();
            }
        }
        
        return null;    
    }
    
}
