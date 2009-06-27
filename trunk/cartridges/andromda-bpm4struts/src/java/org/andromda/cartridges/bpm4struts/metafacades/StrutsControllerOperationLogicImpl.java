package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.ServiceOperation;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.utils.StringUtilsHelper;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsControllerOperation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsControllerOperation
 */
public class StrutsControllerOperationLogicImpl
    extends StrutsControllerOperationLogic
{
    public StrutsControllerOperationLogicImpl(
        Object metaObject,
        String context)
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

    /**
     * Overridden because StrutsAction does not extend FrontEndAction.
     *
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#getDeferringActions()
     */
    public java.util.List getDeferringActions()
    {
        final Collection deferringActions = new LinkedHashSet();

        final FrontEndActivityGraph graph = getActivityGraph();
        if (graph != null)
        {
            final Collection actionStates = graph.getActionStates();
            for (final Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
            {
                final Object actionStateObject = actionStateIterator.next();
                if (actionStateObject instanceof StrutsActionState)
                {
                    final StrutsActionState actionState = (StrutsActionState)actionStateObject;
                    final Collection controllerCalls = actionState.getControllerCalls();
                    for (final Iterator controllerCallIterator = controllerCalls.iterator();
                         controllerCallIterator.hasNext();)
                    {
                        final OperationFacade operation = (OperationFacade)controllerCallIterator.next();
                        if (this.equals(operation))
                        {
                            deferringActions.addAll(actionState.getContainerActions());
                        }
                    }
                }
            }

            final Collection transitions = graph.getTransitions();
            for (final Iterator transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
            {
                final StrutsForward transition = (StrutsForward)transitionIterator.next();
                final EventFacade event = transition.getTrigger();
                if (event instanceof StrutsTrigger)
                {
                    final StrutsTrigger trigger = (StrutsTrigger)event;
                    final FrontEndControllerOperation operation = trigger.getControllerCall();
                    if (this.equals(operation))
                    {
                        // we have two types of controller calls: the ones in action states and the ones for decisions
                        final StateVertexFacade source = transition.getSource();
                        if (source instanceof StrutsActionState)
                        {
                            final StrutsActionState sourceActionState = (StrutsActionState)source;
                            deferringActions.addAll(sourceActionState.getContainerActions());
                        }

                        // test for decision
                        final StateVertexFacade target = transition.getTarget();
                        if (target instanceof StrutsPseudostate)
                        {
                            final StrutsPseudostate targetPseudoState = (StrutsPseudostate)target;
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
        final Object owner = getOwner();
        return (owner instanceof StrutsController) ? owner : null;
    }

    /**
     * Overridden since StrutsAction does not extend FrontEndAction.
     *
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#getFormFields()
     */
    public List getFormFields()
    {
        final Map formFieldsMap = new LinkedHashMap();

        // for quick lookup we use a hashset for the argument names, we only consider parameters with a name
        // which is also present in this set
        final Set argumentNames = new LinkedHashSet();
        final Collection arguments = this.getArguments();
        for (final Iterator argumentIterator = arguments.iterator(); argumentIterator.hasNext();)
        {
            final ModelElementFacade element = (ModelElementFacade)argumentIterator.next();
            argumentNames.add(element.getName());
        }

        // get all actions deferring to this operation
        final List deferringActions = this.getDeferringActions();
        for (int i = 0; i < deferringActions.size(); i++)
        {
            final StrutsAction action = (StrutsAction)deferringActions.get(i);
            // store the action parameters
            final List actionFormFields = action.getActionFormFields();
            for (int j = 0; j < actionFormFields.size(); j++)
            {
                final ModelElementFacade parameter = (ModelElementFacade)actionFormFields.get(j);
                if (argumentNames.contains(parameter.getName()))
                {
                    formFieldsMap.put(parameter.getName(), parameter);
                }
            }
            // get all forwards and overwrite when we find a table (or add when not yet present)
            final List forwards = action.getActionForwards();
            for (int j = 0; j < forwards.size(); j++)
            {
                final StrutsForward forward = (StrutsForward)forwards.get(j);
                // only consider forwards directly entering a page
                if (forward.isEnteringPage())
                {
                    final List pageVariables = forward.getForwardParameters();
                    for (int k = 0; k < pageVariables.size(); k++)
                    {
                        final StrutsParameter pageVariable = (StrutsParameter)pageVariables.get(k);
                        if (argumentNames.contains(pageVariable.getName()))
                        {
                            if (!formFieldsMap.containsKey(pageVariable.getName()) || pageVariable.isTable())
                            {
                                formFieldsMap.put(pageVariable.getName(), pageVariable);
                            }
                        }
                    }
                }
            }
        }

        // since all arguments need to be present we add those that haven't yet been stored in the map
        for (final Iterator argumentIterator = arguments.iterator(); argumentIterator.hasNext();)
        {
            final StrutsParameter argument = (StrutsParameter)argumentIterator.next();
            if (!formFieldsMap.containsKey(argument.getName()))
            {
                formFieldsMap.put(argument.getName(), argument);
            }
        }

        return new ArrayList(formFieldsMap.values());
    }

    /**
     * Overridden because StrutsAction does not extend FrontEndAction
     * 
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#isAllArgumentsHaveFormFields()
     */
    public boolean isAllArgumentsHaveFormFields()
    {
        final Collection arguments = this.getArguments();
        final Collection deferringActions = this.getDeferringActions();

        boolean allArgumentsHaveFormFields = true;
        for (final Iterator iterator = arguments.iterator(); iterator.hasNext() && allArgumentsHaveFormFields;)
        {
            final ParameterFacade parameter = (ParameterFacade)iterator.next();
            final String parameterName = parameter.getName();
            final ClassifierFacade parameterType = parameter.getType();
            final String parameterTypeName = parameterType != null ? parameterType.getFullyQualifiedName() : "";

            boolean actionMissingField = false;
            for (final Iterator actionIterator = deferringActions.iterator();
                actionIterator.hasNext() && !actionMissingField;)
            {
                final StrutsAction action = (StrutsAction)actionIterator.next();
                final Collection actionFormFields = action.getActionFormFields();

                boolean fieldPresent = false;
                for (final Iterator fieldIterator = actionFormFields.iterator();
                    fieldIterator.hasNext() && !fieldPresent;)
                {
                    final ParameterFacade field = (ParameterFacade)fieldIterator.next();
                    final ClassifierFacade fieldType = field.getType();
                    final String fieldTypeName = fieldType != null ? fieldType.getFullyQualifiedName() : "";
                    if (parameterName.equals(field.getName()) && parameterTypeName.equals(fieldTypeName))
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

    protected boolean handleIsBackEndServiceOperationMatchingParameters()
    {
        boolean matches = true;

        final ServiceOperation serviceOperation = getBackEndServiceOperation();

        // cache this operation's parameters for easy lookup
        final Map parameterMap = new HashMap();
        final Collection controllerParameters = getParameters();
        for (final Iterator iterator = controllerParameters.iterator(); iterator.hasNext();)
        {
            final ParameterFacade parameter = (ParameterFacade)iterator.next();
            parameterMap.put(parameter.getName(), parameter.getType());
        }

        // make sure that any service parameter exists here too
        final Collection serviceParameters = serviceOperation.getParameters();
        for (final Iterator iterator = serviceParameters.iterator(); iterator.hasNext() && matches;)
        {
            final ParameterFacade serviceParameter = (ParameterFacade)iterator.next();
            final ClassifierFacade controllerParameterType = (ClassifierFacade)parameterMap.get(
                serviceParameter.getName());
            matches = (controllerParameterType != null) && controllerParameterType.equals(serviceParameter.getType());
        }

        return matches;
    }

    protected Object handleGetBackEndServiceOperation()
    {
        Object operation = null;

        final Collection dependencies = getSourceDependencies();
        for (final Iterator dependencyIterator = dependencies.iterator();
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
}
