package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.ServiceOperation;
import org.andromda.metafacades.uml.StateVertexFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsControllerOperation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsControllerOperation
 */
public class StrutsControllerOperationLogicImpl
        extends StrutsControllerOperationLogic
{
    // ---------------- constructor -------------------------------

    public StrutsControllerOperationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected String handleGetInterfaceName()
    {
        return StringUtilsHelper.upperCamelCaseName(getName()) + Bpm4StrutsGlobals.FORM_SUFFIX;
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

    protected java.util.List handleGetDeferringActions()
    {
        final Collection deferringActions = new HashSet();

        StrutsActivityGraph graph = getActivityGraph();
        if (graph != null)
        {
            Collection actionStates = graph.getActionStates();
            for (Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
            {
                StrutsActionState actionState = (StrutsActionState)actionStateIterator.next();
                Collection controllerCalls = actionState.getControllerCalls();
                for (Iterator controllerCallIterator = controllerCalls.iterator(); controllerCallIterator.hasNext();)
                {
                    OperationFacade operation = (OperationFacade)controllerCallIterator.next();
                    if (this.equals(operation))
                    {
                        deferringActions.addAll(actionState.getContainerActions());
                    }
                }
            }

            Collection transitions = graph.getTransitions();
            for (Iterator transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
            {
                StrutsForward transition = (StrutsForward)transitionIterator.next();
                EventFacade event = transition.getTrigger();
                if (event instanceof StrutsTrigger)
                {
                    StrutsTrigger trigger = (StrutsTrigger)event;
                    StrutsControllerOperation operation = trigger.getControllerCall();
                    if (this.equals(operation))
                    {
                        // we have two types of controller calls: the ones in action states and the ones for decisions
                        StateVertexFacade source = transition.getSource();
                        if (source instanceof StrutsActionState)
                        {
                            StrutsActionState sourceActionState = (StrutsActionState)source;
                            deferringActions.addAll(sourceActionState.getContainerActions());
                        }

                        // test for decision
                        StateVertexFacade target = transition.getTarget();
                        if (target instanceof StrutsPseudostate)
                        {
                            StrutsPseudostate targetPseudoState = (StrutsPseudostate)target;
                            if (targetPseudoState.isDecisionPoint())
                            {
                                deferringActions.addAll(targetPseudoState.getContainerActions());
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList(deferringActions);
    }

    protected Object handleGetController()
    {
        Object owner = getOwner();
        return (owner instanceof StrutsController) ? owner : null;
    }

    protected List handleGetFormFields()
    {
        return new ArrayList(this.getArguments());
    }

    protected boolean handleIsAllArgumentsHaveFormFields()
    {
        Collection arguments = getFormFields();
        Collection deferringActions = getDeferringActions();

        boolean allArgumentsHaveFormFields = true;
        for (Iterator argumentIterator = arguments.iterator();
             argumentIterator.hasNext() && allArgumentsHaveFormFields;)
        {
            StrutsParameter parameter = (StrutsParameter)argumentIterator.next();
            String parameterName = parameter.getName();
            String parameterType = parameter.getFullyQualifiedName();

            boolean actionMissingField = false;
            for (Iterator actionIterator = deferringActions.iterator();
                 actionIterator.hasNext() && !actionMissingField;)
            {
                StrutsAction action = (StrutsAction)actionIterator.next();
                Collection actionFormFields = action.getActionFormFields();

                boolean fieldPresent = false;
                for (Iterator fieldIterator = actionFormFields.iterator(); fieldIterator.hasNext() && !fieldPresent;)
                {
                    StrutsParameter field = (StrutsParameter)fieldIterator.next();
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
            StrutsController controller = (StrutsController)owner;
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

    protected boolean handleIsBackEndServiceOperationMatchingParameters()
    {
        boolean matches = true;

        final ServiceOperation serviceOperation = getBackEndServiceOperation();

        // cache this operation's parameters for easy lookup
        final Map parameterMap = new HashMap();
        final Collection controllerParameters = getParameters();
        for (Iterator iterator = controllerParameters.iterator(); iterator.hasNext();)
        {
            final ParameterFacade parameter = (ParameterFacade)iterator.next();
            parameterMap.put(parameter.getName(), parameter.getType());
        }

        // make sure that any service parameter exists here too
        final Collection serviceParameters = serviceOperation.getParameters();
        for (Iterator iterator = serviceParameters.iterator(); iterator.hasNext() && matches;)
        {
            final ParameterFacade serviceParameter = (ParameterFacade)iterator.next();
            final ClassifierFacade controllerParameterType = (ClassifierFacade)parameterMap.get(
                    serviceParameter.getName());
            matches = (controllerParameterType == null) ?
                    false : controllerParameterType.equals(serviceParameter.getType());
        }

        return matches;
    }

    protected Object handleGetBackEndServiceOperation()
    {
        Object operation = null;

        final Collection dependencies = getSourceDependencies();
        for (Iterator dependencyIterator = dependencies.iterator();
             dependencyIterator.hasNext() && operation == null;)
        {
            final DependencyFacade dependency = (DependencyFacade)dependencyIterator.next();
            final Object target = dependency.getTargetElement();
            if (target instanceof ServiceOperation)
            {
                operation = target;
            }
        }

        return operation;
    }

    protected boolean handleIsCallingBackEnd()
    {
        return getBackEndServiceOperation() != null;
    }
    
    protected boolean handleIsOwnerIsController()
    {
        return this.getOwner() instanceof StrutsController;
    }
}
