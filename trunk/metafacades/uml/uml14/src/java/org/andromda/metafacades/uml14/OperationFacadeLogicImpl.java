package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;


/**
 * <p>
 *  Returns true/false if the operation is declared static.
 * </p>
 *
 * Metaclass facade implementation.
 *
 */
public class OperationFacadeLogicImpl
       extends OperationFacadeLogic
       implements org.andromda.metafacades.uml.OperationFacade
{
    // ---------------- constructor -------------------------------
    
    public OperationFacadeLogicImpl (org.omg.uml.foundation.core.Operation metaObject, String context)
    {
        super (metaObject, context);
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationFacade#getSignature()
     */
    public String getSignature()
    {
        Iterator it = metaObject.getParameter().iterator();
        if (!it.hasNext())
        {
            return metaObject.getName() + "()";
        }

        StringBuffer sb = new StringBuffer();
        sb.append(metaObject.getName());
        sb.append("(");
        sb.append(getTypedParameterList());
        sb.append(")");

        return sb.toString();
    }

    public String getTypedParameterList()
    {
        StringBuffer sb = new StringBuffer();
        Iterator it = metaObject.getParameter().iterator();

        boolean commaNeeded = false;
        while (it.hasNext())
        {
            Parameter p = (Parameter) it.next();

            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                String type = null;
                if (p.getType() == null)
                {
                    this.logger.error("ERROR! No type specified for parameter --> '" 
                        + p.getName() + "' on operation --> '" 
                        + this.getName() + "', please chek your model");
                }
                else
                {
                    type =
                        ((ClassifierFacade) this.shieldedElement(p.getType()))
                            .getFullyQualifiedName();
                }

                if (commaNeeded)
                {
                    sb.append(", ");
                }
                sb.append(type);
                sb.append(" ");
                sb.append(p.getName());
                commaNeeded = true;
            }
        }
        return sb.toString();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationFacade#getCall()
     */
    public String getCall()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(metaObject.getName());
        sb.append("(");
        sb.append(getParameterNames());
        sb.append(")");

        return sb.toString();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationFacade#getParameterNames()
     */
    public String getParameterNames()
    {
        StringBuffer sb = new StringBuffer();

        Iterator it = metaObject.getParameter().iterator();

        boolean commaNeeded = false;
        while (it.hasNext())
        {
            Parameter p = (Parameter) it.next();

            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                if (commaNeeded)
                {
                    sb.append(", ");
                }
                sb.append(p.getName());
                commaNeeded = true;
            }
        }
        return sb.toString();
    }
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationFacade#getParameterTypeNames()
     */
    public String getParameterTypeNames()
    {
        StringBuffer sb = new StringBuffer();
        
        Iterator it = metaObject.getParameter().iterator();
        
        boolean commaNeeded = false;
        while (it.hasNext())
        {
            Parameter p = (Parameter) it.next();
            
            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                if (commaNeeded)
                {
                    sb.append(", ");
                }
                ParameterFacade facade = (ParameterFacade)shieldedElement(p);
                sb.append(facade.getType().getFullyQualifiedName());
                commaNeeded = true;
            }
        }
        return sb.toString();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationFacade#handleGetType()
     */
    protected Object handleGetType()
    {
        Collection parms = metaObject.getParameter();
        for (Iterator i = parms.iterator(); i.hasNext();)
        {
            Parameter p = (Parameter) i.next();
            if (ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                return p.getType();
            }
        }

        return null;
    }
   
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationFacade#handleGetOwner()
     */
    public Object handleGetOwner() 
    {
        return this.metaObject.getOwner();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationFacade#handleGetParameters()
     */
    protected Collection handleGetParameters()
    {
        ArrayList ret = new ArrayList();

        for (Iterator i = metaObject.getParameter().iterator();
            i.hasNext();
            )
        {
            Parameter p = (Parameter) i.next();
            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                ret.add(p);
            }
        }

        return ret;
    }
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationFacade#findTaggedValue(java.lang.String, boolean)
     */
    public String findTaggedValue(String name, boolean follow) 
    {
        name = StringUtils.trimToEmpty(name);
        String value = findTaggedValue(name);
        if (follow) {
            ClassifierFacade type = this.getType();
            while (value == null && type != null) {
                value = type.findTaggedValue(name);
                type = (ClassifierFacade)type.getGeneralization();
            }
        }
        return value;
    }
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AttributeFacade#isStatic()
     */
    public boolean isStatic() 
    {
        return ScopeKindEnum.SK_CLASSIFIER.equals(this.metaObject.getOwnerScope());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationFacade#isAbstract()
     */
    public boolean isAbstract()
    {
        return metaObject.isAbstract();
    }
}
