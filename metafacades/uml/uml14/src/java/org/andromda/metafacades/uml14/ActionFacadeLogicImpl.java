package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;

import java.util.Collection;
import java.util.Iterator;

/**
 * MetafacadeLogic implementation.
 * 
 * @see org.andromda.metafacades.uml.ActionFacade
 */
public class ActionFacadeLogicImpl
    extends ActionFacadeLogic
    implements org.andromda.metafacades.uml.ActionFacade
{
    // ---------------- constructor -------------------------------

    public ActionFacadeLogicImpl(
        org.omg.uml.behavioralelements.commonbehavior.Action metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    protected Object handleGetTransition()
    {
        Transition effectTransition = null;

        Collection allTransitions = UMLMetafacadeUtils.getModel()
            .getStateMachines().getTransition().refAllOfType();
        for (Iterator iterator = allTransitions.iterator(); iterator.hasNext()
            && effectTransition == null;)
        {
            Transition transition = (Transition)iterator.next();
            if (metaObject.equals(transition.getEffect()))
            {
                effectTransition = transition;
            }
        }

        return effectTransition;
    }

    protected Object handleGetActionState()
    {
        ActionState entryState = null;

        Collection allActionStates = UMLMetafacadeUtils.getModel().getActivityGraphs().getActionState().refAllOfType();
        for (Iterator iterator = allActionStates.iterator(); iterator.hasNext() && entryState == null;)
        {
            ActionState actionState = (ActionState) iterator.next();
            if (metaObject.equals(actionState.getEntry()))
            {
                entryState = actionState;
            }
        }

        return entryState;
    }

    public Object getValidationOwner()
    {
        Object validationOwner = getTransition();

        if (validationOwner == null)
        {
            validationOwner = getActionState();
        }

        return validationOwner;
    }
}
