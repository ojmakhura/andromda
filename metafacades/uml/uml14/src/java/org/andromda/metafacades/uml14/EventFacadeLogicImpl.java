package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.omg.uml.behavioralelements.statemachines.Transition;

/**
 * MetafacadeLogic implementation.
 * 
 * @see org.andromda.metafacades.uml.EventFacade
 */
public class EventFacadeLogicImpl
    extends EventFacadeLogic
    implements org.andromda.metafacades.uml.EventFacade
{
    // ---------------- constructor -------------------------------

    public EventFacadeLogicImpl(
        org.omg.uml.behavioralelements.statemachines.Event metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    protected Collection handleGetParameters()
    {
        return metaObject.getParameter();
    }

    protected Object handleGetTransition()
    {
        Transition eventTransition = null;

        Collection allTransitions = UMLMetafacadeUtils.getModel()
            .getStateMachines().getTransition().refAllOfType();
        for (Iterator iterator = allTransitions.iterator(); iterator.hasNext()
            && eventTransition == null;)
        {
            Transition transition = (Transition)iterator.next();
            if (metaObject.equals(transition.getTrigger()))
            {
                eventTransition = transition;
            }
        }

        return eventTransition;
    }
}
