package org.andromda.core.metadecorators.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;

/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Operation
 *
 *
 */
public class OperationDecoratorImpl extends OperationDecorator
{
    // ---------------- constructor -------------------------------

    public OperationDecoratorImpl(
        org.omg.uml.foundation.core.Operation metaObject)
    {
        super(metaObject);
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.OperationDecorator#getSignature()
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
        sb.append(getOperationTypedParameterList());
        sb.append(")");

        return sb.toString();
    }

    private String getOperationTypedParameterList()
    {
        StringBuffer sb = new StringBuffer();
        Iterator it = metaObject.getParameter().iterator();

        boolean commaNeeded = false;
        while (it.hasNext())
        {
            Parameter p = (Parameter) it.next();

            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                String type;
                if (p.getType() == null)
                {
                    type = "int";
                }
                else
                {
                    type =
                        ((ClassifierDecorator) DecoratorFactory
                            .getInstance()
                            .createDecoratorObject(p.getType()))
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
     * @see org.andromda.core.metadecorators.uml14.OperationDecorator#getCall()
     */
    public String getCall()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(metaObject.getName());
        sb.append("(");
        sb.append(getOperationParameterNames());
        sb.append(")");

        return sb.toString();
    }

    private String getOperationParameterNames()
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
     * @see org.andromda.core.metadecorators.uml14.OperationDecorator#handleGetType()
     */
    protected ModelElement handleGetType()
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
     * @see org.andromda.core.metadecorators.uml14.OperationDecorator#handleGetParameters()
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

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class OperationDecorator ...

    // ------------- relations ------------------

}
