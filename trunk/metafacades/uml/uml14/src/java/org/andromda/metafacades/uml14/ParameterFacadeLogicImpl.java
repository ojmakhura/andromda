package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.UMLProfile;
import org.omg.uml.foundation.datatypes.Expression;
import org.omg.uml.foundation.datatypes.ParameterDirectionKind;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.behavioralelements.statemachines.SignalEvent;

import java.util.Collection;
import java.util.Iterator;


/**
 * Metaclass facade implementation.
 */
public class ParameterFacadeLogicImpl
       extends ParameterFacadeLogic
       implements org.andromda.metafacades.uml.ParameterFacade
{
    // ---------------- constructor -------------------------------

    public ParameterFacadeLogicImpl (org.omg.uml.foundation.core.Parameter metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml14.ParameterFacade#getDefaultValue()
     */
    public String handleGetDefaultValue()
    {
        final Expression expression = metaObject.getDefaultValue();
        return (expression == null) ? "" : expression.getBody();
    }


    // ------------- relations ------------------

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getType()
     */
    protected Object handleGetType()
    {
        return metaObject.getType();
    }
    
    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#sReturn()
     */
    public boolean handleIsReturn()
    {
        final ParameterDirectionKind kind = metaObject.getKind();
        return kind != null ? kind.equals(ParameterDirectionKindEnum.PDK_RETURN) : false;
    }

    /**
     * @see org.andromda.metafacades.uml14.ParameterFacade#isRequired()
     */
    protected boolean handleIsRequired()
    {
        return !this.hasStereotype(UMLProfile.STEREOTYPE_NULLABLE);
    }

    // ------------------------------------------------------------

    protected Object handleGetOperation()
    {
        Operation parameterOperation = null;

        Collection allOperations = UMLMetafacadeUtils.getModel().getCore().getOperation().refAllOfType();
        for (Iterator iterator = allOperations.iterator(); iterator.hasNext() && parameterOperation==null;)
        {
            Operation operation = (Operation) iterator.next();
            if (operation.getParameter().contains(metaObject))
            {
                parameterOperation = operation;
            }
        }

        return parameterOperation;
    }

    protected Object handleGetEvent()
    {
        SignalEvent parameterSignalEvent = null;

        Collection allSignalEvents = UMLMetafacadeUtils.getModel().getStateMachines().getSignalEvent().refAllOfType();
        for (Iterator iterator = allSignalEvents.iterator(); iterator.hasNext() && parameterSignalEvent==null;)
        {
            SignalEvent signalEvent = (SignalEvent) iterator.next();
            if (signalEvent.getParameter().contains(metaObject))
            {
                parameterSignalEvent = signalEvent;
            }
        }

        return parameterSignalEvent;
    }
}
