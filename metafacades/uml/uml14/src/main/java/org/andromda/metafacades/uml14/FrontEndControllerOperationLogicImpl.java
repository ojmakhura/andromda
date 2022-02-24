package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActionState;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndPseudostate;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.apache.log4j.Logger;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndControllerOperation.
 *
 * @see org.andromda.metafacades.uml.FrontEndControllerOperation
 * @author Bob Fields
 */
public class FrontEndControllerOperationLogicImpl
    extends FrontEndControllerOperationLogic
{
    private static final long serialVersionUID = 6536778815148740646L;

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndControllerOperationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(FrontEndControllerOperationLogicImpl.class);

    /**
     * Override to return the owner's package as the package name.
     *
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetPackageName()
     */
    public String handleGetPackageName()
    {
        final ClassifierFacade owner = this.getOwner();
        return owner != null ? owner.getPackageName() : "";
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#isOwnerIsController()
     */
    @Override
    protected boolean handleIsOwnerIsController()
    {
        return this.getOwner() instanceof FrontEndController;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#getFormFields()
     */
    @Override
    protected List<FrontEndParameter> handleGetFormFields()
    {
        final Map<String, FrontEndParameter> formFieldsMap = new LinkedHashMap<String, FrontEndParameter>();

        // for quick lookup we use a hashset for the argument names, we only consider parameters with a name
        // which is also present in this set
        final Set argumentNames = new LinkedHashSet();
        final Collection<ParameterFacade> arguments = this.getArguments();
        for (final Iterator argumentIterator = arguments.iterator(); argumentIterator.hasNext();)
        {
            final ModelElementFacade element = (ModelElementFacade)argumentIterator.next();
            argumentNames.add(element.getName());
        }

        // - get all actions deferring to this operation
        final List<FrontEndAction> deferringActions = this.getDeferringActions();
        for (final Iterator<FrontEndAction> iterator = deferringActions.iterator(); iterator.hasNext();)
        {
            final FrontEndAction action = iterator.next();
            // store the action parameters
            final List<FrontEndParameter> actionFormFields = action.getFormFields();
            for (final Iterator<FrontEndParameter> fieldIterator = actionFormFields.iterator(); fieldIterator.hasNext();)
            {
                final FrontEndParameter parameter = fieldIterator.next();
                final String name = parameter.getName();
                // - only add if the parameter is an action parameter and its an argument of this operation
                if (parameter.getAction() != null && argumentNames.contains(name))
                {
                    formFieldsMap.put(name, parameter);
                }
            }
            // get all forwards and overwrite when we find a table (or add when not yet present)
            final List<FrontEndForward> forwards = action.getActionForwards();
            for (final Iterator<FrontEndForward> forwardIterator = forwards.iterator(); forwardIterator.hasNext();)
            {
                final FrontEndForward forward = forwardIterator.next();
                // - only consider forwards directly entering a view
                if (forward.isEnteringView())
                {
                    final List<FrontEndParameter> viewVariables = forward.getForwardParameters();
                    for (final Iterator<FrontEndParameter> variableIterator = viewVariables.iterator(); variableIterator.hasNext();)
                    {
                        final FrontEndParameter viewVariable = variableIterator.next();
                        final String name = viewVariable.getName();
                        if (argumentNames.contains(name))
                        {
                            if (!formFieldsMap.containsKey(name) || viewVariable.isTable())
                            {
                                formFieldsMap.put(name, viewVariable);
                            }
                        }
                    }
                }
            }
        }

        // since all arguments need to be present we add those that haven't yet been stored in the map
        for (final Iterator argumentIterator = arguments.iterator(); argumentIterator.hasNext();)
        {
            final FrontEndParameter argument = (FrontEndParameter)argumentIterator.next();
            final String name = argument.getName();
            if (!formFieldsMap.containsKey(name))
            {
                formFieldsMap.put(name, argument);
            }
        }
        return new ArrayList<FrontEndParameter>(formFieldsMap.values());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#getActivityGraph()
     */
    @Override
    protected FrontEndActivityGraph handleGetActivityGraph()
    {
        FrontEndActivityGraph graph = null;

        final ClassifierFacade owner = getOwner();
        if (owner instanceof FrontEndController)
        {
            final FrontEndController controller = (FrontEndController)owner;
            final FrontEndUseCase useCase = controller.getUseCase();
            if (useCase != null)
            {
                graph = useCase.getActivityGraph();
            }
        }
        return graph;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#getDeferringActions()
     */
    @Override
    protected List<FrontEndAction> handleGetDeferringActions()
    {
        final Collection<FrontEndAction> deferringActions = new LinkedHashSet<FrontEndAction>();

        final FrontEndActivityGraph graph = this.getActivityGraph();
        if (graph != null)
        {
            final Collection<ActionStateFacade> actionStates = graph.getActionStates();
            for (final Iterator<ActionStateFacade> actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
            {
                final ActionStateFacade actionStateObject = actionStateIterator.next();
                if (actionStateObject instanceof FrontEndActionState)
                {
                    final FrontEndActionState actionState = (FrontEndActionState)actionStateObject;
                    final Collection<OperationFacade> controllerCalls = actionState.getControllerCalls();
                    for (final Iterator<OperationFacade> controllerCallIterator = controllerCalls.iterator();
                        controllerCallIterator.hasNext();)
                    {
                        final OperationFacade operation = controllerCallIterator.next();
                        if (this.equals(operation))
                        {
                            deferringActions.addAll(actionState.getContainerActions());
                        }
                    }
                }
            }

            final Collection<TransitionFacade> transitions = graph.getTransitions();
            for (final Iterator<TransitionFacade> transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
            {
                final FrontEndForward transition = (FrontEndForward)transitionIterator.next();
                final EventFacade event = transition.getTrigger();
                if (event instanceof FrontEndEvent)
                {
                    final FrontEndEvent trigger = (FrontEndEvent)event;
                    final FrontEndControllerOperation operation = trigger.getControllerCall();
                    if (this.equals(operation))
                    {
                        // we have two types of controller calls: the ones in action states and the ones for decisions
                        final StateVertexFacade source = transition.getSource();
                        if (source instanceof FrontEndActionState)
                        {
                            final FrontEndActionState sourceActionState = (FrontEndActionState)source;
                            deferringActions.addAll(sourceActionState.getContainerActions());
                        }

                        // test for decision
                        final StateVertexFacade target = transition.getTarget();
                        if (target instanceof FrontEndPseudostate)
                        {
                            final FrontEndPseudostate targetPseudoState = (FrontEndPseudostate)target;
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

    /**
     * @see org.andromda.metafacades.uml.FrontEndControllerOperation#isAllArgumentsHaveFormFields()
     */
    @Override
    protected boolean handleIsAllArgumentsHaveFormFields()
    {
        final Collection<ParameterFacade> arguments = this.getArguments();
        final Collection<FrontEndAction> deferringActions = this.getDeferringActions();

        boolean allArgumentsHaveFormFields = true;
        for (final Iterator<ParameterFacade> iterator = arguments.iterator(); iterator.hasNext() && allArgumentsHaveFormFields;)
        {
            final ParameterFacade parameter = iterator.next();
            final String parameterName = parameter.getName();
            final ClassifierFacade parameterType = parameter.getType();
            final String parameterTypeName = parameterType != null ? parameterType.getFullyQualifiedName() : "";

            boolean actionMissingField = false;
            for (final Iterator<FrontEndAction> actionIterator = deferringActions.iterator();
                actionIterator.hasNext() && !actionMissingField;)
            {
                final Object obj = actionIterator.next();
                // BPM4Struts throws a ClassCastException when validating model unless we add try/catch
                try
                {
                    final FrontEndAction action = (FrontEndAction)obj;
                    final Collection<FrontEndParameter> actionFormFields = action.getFormFields();

                    boolean fieldPresent = false;
                    for (final Iterator<FrontEndParameter> fieldIterator = actionFormFields.iterator();
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
                catch (ClassCastException e)
                {
                    logger.error("ClassCastException on handleIsAllArgumentsHaveFormFields for " + obj + ' ' + e.getMessage());
                }
            }
            allArgumentsHaveFormFields = !actionMissingField;
        }
        return allArgumentsHaveFormFields;
    }

    @Override
    protected String handleGetFullyQualifiedFormPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetFullyQualifiedFormName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetFormName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetFormCall() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetFormSignature() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetImplementationFormSignature() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetHandleFormSignature() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetHandleFormSignatureImplementation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean handleIsExposed() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String handleGetRestPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Collection handleGetAllowedRoles() {
        // TODO Auto-generated method stub
        return null;
    }
}