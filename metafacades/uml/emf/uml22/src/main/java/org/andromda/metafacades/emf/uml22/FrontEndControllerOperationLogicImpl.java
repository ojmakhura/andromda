package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.web.MetafacadeWebGlobals;
import org.andromda.metafacades.uml.web.MetafacadeWebUtils;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndControllerOperation.
 *
 * @see org.andromda.metafacades.uml.FrontEndControllerOperation
 */
public class FrontEndControllerOperationLogicImpl
    extends FrontEndControllerOperationLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public FrontEndControllerOperationLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * Override to return the owner's package as the package name.
     *
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogic#handleGetPackageName()
     */
    @Override
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

        // for quick lookup we use a hashset for the argument names, we only
        // consider parameters with a name
        // which is also present in this set
        final Set<String> argumentNames = new LinkedHashSet<String>();
        final Collection<ParameterFacade> arguments = this.getArguments();
        for (final Iterator<ParameterFacade> argumentIterator = arguments.iterator(); argumentIterator.hasNext();)
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

                // - only add if the parameter is an action parameter and its an
                // argument of this operation
                if (parameter.getAction() != null && argumentNames.contains(name))
                {
                    formFieldsMap.put(
                        name,
                        parameter);
                }
            }

            // get all forwards and overwrite when we find a table (or add when
            // not yet present)
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
                                formFieldsMap.put(
                                    name,
                                    viewVariable);
                            }
                        }
                    }
                }
            }
        }

        // since all arguments need to be present we add those that haven't yet
        // been stored in the map
        for (ParameterFacade parameter : arguments)
        {
            final FrontEndParameter argument = (FrontEndParameter)parameter;
            final String name = argument.getName();
            if (!formFieldsMap.containsKey(name))
            {
                formFieldsMap.put(
                    name,
                    argument);
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

        final ClassifierFacade owner = this.getOwner();
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
                final Object actionStateObject = actionStateIterator.next();
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
                        // we have two types of controller calls: the ones in
                        // action states and the ones for decisions
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
        return new ArrayList<FrontEndAction>(deferringActions);
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
                final FrontEndAction action = actionIterator.next();
                final Collection<FrontEndParameter> actionFormFields = action.getFormFields();

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

    @Override
    public boolean handleIsExposed() {
        // Private methods are for doc and future use purposes, but are allowed.
        boolean visibility = this.getVisibility().equals("public") || this.getVisibility().equals("protected");
        return visibility && (this.getOwner().hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE) ||
                this.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION));
    }

    @Override
    protected String handleGetFullyQualifiedFormPath() {
        return this.getFullyQualifiedFormName().replace('.', '/');
    }

    @Override
    protected String handleGetFullyQualifiedFormName() {
        final StringBuilder fullyQualifiedName = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtilsHelper.isNotBlank(packageName)) {
            fullyQualifiedName.append(packageName + '.');
        }
        return fullyQualifiedName.append(StringUtilsHelper.capitalize(this.getFormName())).toString();
    }

    @Override
    protected String handleGetFormName() {
        final String pattern = Objects.toString(this.getConfiguredProperty(MetafacadeWebGlobals.FORM_PATTERN), "");
        return pattern.replaceFirst("\\{0\\}", StringUtilsHelper.capitalize(this.getName()));
    }

    @Override
    protected String handleGetFormCall() {
        final StringBuilder call = new StringBuilder();
        call.append(this.getName());
        call.append("(");
        if (!this.getFormFields().isEmpty()) {
            call.append("form");
        }
        call.append(")");
        return call.toString();
    }

    @Override
    protected String handleGetFormSignature() {
        return this.getFormSignature(true);
    }

    @Override
    protected String handleGetImplementationFormSignature() {
        return this.getFormSignature(false);
    }

    /**
     * Constructs the signature that takes the form for this operation.
     *
     * @param isAbstract whether or not the signature is abstract.
     * @return the appropriate signature.
     */
    private String getFormSignature(boolean isAbstract) {
        final StringBuilder signature = new StringBuilder();
        signature.append(this.getVisibility() + ' ');
        if (isAbstract) {
            signature.append("abstract ");
        }
        final ModelElementFacade returnType = this.getReturnType();
        signature.append(returnType != null ? returnType.getFullyQualifiedName() : null);
        signature.append(" " + this.getName() + "(");
        if (!this.getFormFields().isEmpty()) {
            signature.append(this.getFormName() + " form");
        }
        signature.append(")");
        return signature.toString();
    }

    @Override
    protected String handleGetHandleFormSignature() {
        return this.getHandleFormSignature(true);
    }

    @Override
    protected String handleGetHandleFormSignatureImplementation() {
        return this.getHandleFormSignature(false);
    }

    /**
     * Constructs the signature that takes the form for this operation.
     *
     * @param isAbstract whether or not the signature is abstract.
     * @return the appropriate signature.
     */
    private String getHandleFormSignature(boolean isAbstract) {
        final StringBuilder signature = new StringBuilder();
        signature.append(this.getVisibility() + ' ');
        if (isAbstract) {
            signature.append("abstract ");
        }
        final ModelElementFacade returnType = this.getReturnType();
        signature.append(returnType != null ? returnType.getFullyQualifiedName() : null);
        signature.append(" handle" + StringUtilsHelper.capitalize(this.getName()) + "(");
        if (!this.getFormFields().isEmpty()) {
            signature.append(this.getFormName() + " form");
        }
        signature.append(")");
        return signature.toString();
    }

    @Override
    protected Collection handleGetAllowedRoles() {
        return UMLMetafacadeUtils.getAllowedRoles(this);
    }
    
    private static final String EMPTY_STRING = "";
    private static final String DEFAULT = "default";
    private static final String SLASH = "/";
    private static final String QUOTE = "\"";

    @Override
    protected String handleGetRestPath() {
        String path = StringUtils.strip(((String) this.findTaggedValue(UMLProfile.TAGGEDVALUE_PRESENTATION_REST_PATH)));
        if (StringUtils.isBlank(path))
        {
            path = EMPTY_STRING;
        }

        if (StringUtils.isBlank(path) || path.equals(DEFAULT))
        {
            path = MetafacadeWebUtils.toWebResourceName(this.getName());
        }
        else
        {
            if (!path.startsWith(QUOTE))
            {
                path = path;
            }
            if (!path.endsWith(QUOTE) || path.length()<2)
            {
                path = path;
            }
            
            if(path.endsWith(SLASH)) {
                path = path.substring(0, path.length() - 1);
            }
        }
        
        if(!path.startsWith(SLASH)) {
            path = SLASH + path;
        }
        
        return path;
    }
}
