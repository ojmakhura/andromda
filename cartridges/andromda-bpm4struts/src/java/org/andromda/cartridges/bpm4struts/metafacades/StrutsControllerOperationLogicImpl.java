package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.StateVertexFacade;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsControllerOperation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsControllerOperation
 */
public class StrutsControllerOperationLogicImpl
        extends StrutsControllerOperationLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsControllerOperation
{
    // ---------------- constructor -------------------------------

    public StrutsControllerOperationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected String handleGetInterfaceName()
    {
        return StringUtilsHelper.upperCamelCaseName(getName()) + "Form";
    }

    protected String handleGetInterfacePackageName()
    {
        return getOwner().getPackageName();
    }

    protected String handleGetInterfaceType()
    {
        return getInterfacePackageName() + '.' + getInterfaceName();
    }

    protected String handleGetInterfaceFullPath()
    {
        return '/' + getInterfaceType().replace('.', '/');
    }

    protected java.util.Collection handleGetDeferringActions()
    {
        Collection deferringActions = new HashSet();

        StrutsActivityGraph graph = getActivityGraph();
        if (graph != null)
        {
            Collection actionStates = graph.getActionStates();
            for (Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
            {
                StrutsActionState actionState = (StrutsActionState) actionStateIterator.next();
                Collection controllerCalls = actionState.getControllerCalls();
                for (Iterator controllerCallIterator = controllerCalls.iterator(); controllerCallIterator.hasNext();)
                {
                    OperationFacade operation = (OperationFacade) controllerCallIterator.next();
                    if (this.equals(operation))
                    {
                        deferringActions.addAll(actionState.getContainerActions());
                    }
                }
            }

            Collection transitions = graph.getTransitions();
            for (Iterator transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
            {
                StrutsForward transition = (StrutsForward) transitionIterator.next();
                EventFacade event = transition.getTrigger();
                if (event instanceof StrutsTrigger)
                {
                    StrutsTrigger trigger = (StrutsTrigger) event;
                    StrutsControllerOperation operation = trigger.getControllerCall();
                    if (this.equals(operation))
                    {
                        StateVertexFacade source = transition.getSource();
                        if (source instanceof StrutsActionState)
                        {
                            deferringActions.addAll(((StrutsActionState) source).getContainerActions());
                        }
                    }
                }
            }
        }
        return deferringActions;
    }

    protected Object handleGetController()
    {
        Object owner = getOwner();
        return (owner instanceof StrutsController) ? owner : null;
    }

    protected Collection handleGetFormFields()
    {
        return this.getArguments();
    }

    protected boolean handleIsAllArgumentsHaveFormFields()
    {
        Collection arguments = getFormFields();
        Collection deferringActions = getDeferringActions();

        boolean allArgumentsHaveFormFields = true;
        for (Iterator argumentIterator = arguments.iterator(); argumentIterator.hasNext() && allArgumentsHaveFormFields;)
        {
            StrutsParameter parameter = (StrutsParameter) argumentIterator.next();
            String parameterName = parameter.getName();
            String parameterType = parameter.getFullyQualifiedName();

            boolean actionMissingField = false;
            for (Iterator actionIterator = deferringActions.iterator(); actionIterator.hasNext() && !actionMissingField;)
            {
                StrutsAction action = (StrutsAction) actionIterator.next();
                Collection actionFormFields = action.getActionFormFields();

                boolean fieldPresent = false;
                for (Iterator fieldIterator = actionFormFields.iterator(); fieldIterator.hasNext() && !fieldPresent;)
                {
                    StrutsParameter field = (StrutsParameter) fieldIterator.next();
                    if (parameterName.equals(field.getName()) && parameterType.equals(field.getFullyQualifiedName()))
                    {
                        fieldPresent = true;
                    }
                }
                actionMissingField = !fieldPresent;
            }
            allArgumentsHaveFormFields = !actionMissingField;
        }
        return allArgumentsHaveFormFields;
    }

    protected Object handleGetActivityGraph()
    {
        Object graph = null;

        ClassifierFacade owner = getOwner();
        if (owner instanceof StrutsController)
        {
            StrutsController controller = (StrutsController) owner;
            if (controller != null)
            {
                StrutsUseCase useCase = controller.getUseCase();
                if (useCase != null)
                {
                    graph = useCase.getActivityGraph();
                }
            }
        }
        return graph;
    }
}
