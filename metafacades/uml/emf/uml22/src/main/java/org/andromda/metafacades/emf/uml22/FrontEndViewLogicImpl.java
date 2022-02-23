package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndAttribute;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.web.MetafacadeWebGlobals;
import org.andromda.metafacades.uml.web.MetafacadeWebProfile;
import org.andromda.metafacades.uml.web.MetafacadeWebUtils;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndView.
 *
 * @see org.andromda.metafacades.uml.FrontEndView
 */
public class FrontEndViewLogicImpl
    extends FrontEndViewLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public FrontEndViewLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndView#isFrontEndView()
     */
    @Override
    protected boolean handleIsFrontEndView()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_VIEW);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getUseCase()
     */
    @Override
    protected UseCaseFacade handleGetUseCase()
    {
        UseCaseFacade useCase = null;
        final StateMachineFacade graphContext = this.getStateMachine();
        if (graphContext instanceof ActivityGraphFacade)
        {
            useCase = ((ActivityGraphFacade)graphContext).getUseCase();
            if (!(useCase instanceof FrontEndUseCase))
            {
                useCase = null;
            }
        }
        return useCase;
    }

    /**
     * Override to create the package of the view.
     *
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogic#handleGetPackageName()
     */
    @Override
    public String handleGetPackageName()
    {
        String packageName = null;
        final StateMachineFacade graphContext = this.getStateMachine();

        // TODO: Why not use getUseCase ?
        if (graphContext instanceof ActivityGraphFacade)
        {
            final UseCaseFacade graphUseCase = ((ActivityGraphFacade)graphContext).getUseCase();
            if (graphUseCase instanceof FrontEndUseCase)
            {
                final FrontEndUseCase useCase = (FrontEndUseCase)graphUseCase;
                packageName = useCase.getPackageName();
            }
        }
        return packageName;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getVariables()
     */
    @Override
    protected List<ModelElementFacade> handleGetVariables()
    {
        final Map<String, ModelElementFacade> variablesMap = new LinkedHashMap<String, ModelElementFacade>();
        final Collection<TransitionFacade> incoming = this.getIncomings();
        for (final Iterator<TransitionFacade> iterator = incoming.iterator(); iterator.hasNext();)
        {
            final TransitionFacade transition = iterator.next();
            final EventFacade trigger = transition.getTrigger();
            if (trigger != null)
            {
                for (final Iterator<ParameterFacade> parameterIterator = trigger.getParameters().iterator();
                    parameterIterator.hasNext();)
                {
                    final ModelElementFacade modelElement = (ModelElementFacade)parameterIterator.next();
                    variablesMap.put(
                        modelElement.getName(),
                        modelElement);
                }
            }
        }
        return new ArrayList<ModelElementFacade>(variablesMap.values());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getAllActionParameters()
     */
    @Override
    protected List<FrontEndParameter> handleGetAllActionParameters()
    {
        final List<FrontEndParameter> actionParameters = new ArrayList<FrontEndParameter>();
        final Collection<FrontEndAction> actions = this.getActions();
        for (FrontEndAction action : actions)
        {
            actionParameters.addAll(action.getParameters());
        }
        return actionParameters;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getAllFormFields()
     */
    @Override
    protected List<FrontEndParameter> handleGetAllFormFields()
    {
        final List<FrontEndParameter> actionParameters = new ArrayList<FrontEndParameter>();
        final Collection<FrontEndAction> actions = this.getActions();
        for (FrontEndAction action : actions)
        {
            actionParameters.addAll(action.getParameters());
        }
        return actionParameters;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.FrontEndViewLogic#getTables()
     */
    @Override
    protected List<FrontEndParameter> handleGetTables()
    {
        final List<FrontEndParameter> variables = new ArrayList<FrontEndParameter>(this.getVariables());
        for (final Iterator iterator = variables.iterator(); iterator.hasNext();)
        {
            final FrontEndParameter parameter = (FrontEndParameter)iterator.next();
            if (!parameter.isTable())
            {
                iterator.remove();
            }
        }
        return variables;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getActions()
     */
    @Override
    protected List<FrontEndAction> handleGetActions()
    {
        final List<FrontEndAction> actions = new ArrayList<FrontEndAction>();
        final Collection<TransitionFacade> outgoing = this.getOutgoings();
        for (final Iterator<TransitionFacade> iterator = outgoing.iterator(); iterator.hasNext();)
        {
            final TransitionFacade object = iterator.next();
            if (object instanceof FrontEndAction)
            {
                actions.add((FrontEndAction)object);
            }
        }
        return actions;
    }

    @Override
    protected String handleGetPath() {

        return UMLMetafacadeUtils.getPath(this);

        // if(StringUtils.isNotBlank(p)) {
        //     return p;
        // }


        // final StringBuilder path = new StringBuilder();
        // final String packageName = this.getPackageName();
        // if (StringUtilsHelper.isNotBlank(packageName)) {
        //     path.append(packageName + '.');
        // }
        // path.append(MetafacadeWebUtils.toWebResourceName(StringUtilsHelper.trimToEmpty(this.getName())).replace('.', '/'));
        // return '/' + path.toString().replace('.', '/');
    }

    @Override
    protected String handleGetTitleKey() {
        return this.getMessageKey() + '.' + MetafacadeWebGlobals.TITLE_MESSAGE_KEY_SUFFIX;
    }

    @Override
    protected String handleGetTitleValue() {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * Indicates whether or not we should normalize messages.
     *
     * @return true/false
     */
    private boolean isNormalizeMessages() {
        final String normalizeMessages = (String) getConfiguredProperty(MetafacadeWebGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    @Override
    protected String handleGetMessageKey() {
        final StringBuilder messageKey = new StringBuilder();

        if (!this.isNormalizeMessages()) {
            final UseCaseFacade useCase = this.getUseCase();
            if (useCase != null) {
                messageKey.append(StringUtilsHelper.toResourceMessageKey(useCase.getName()));
                messageKey.append('.');
            }
        }

        messageKey.append(StringUtilsHelper.toResourceMessageKey(getName()));
        return messageKey.toString();
    }

    @Override
    protected String handleGetDocumentationKey() {
        return getMessageKey() + '.' + MetafacadeWebGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX;
    }

    @Override
    protected String handleGetDocumentationValue() {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation(""));
        return value == null ? "" : value;
    }

    @Override
    protected String handleGetMessageValue() {
        return StringUtilsHelper.toPhrase(getName());
    }

    @Override
    protected String handleGetFullyQualifiedPopulator() {
        final StringBuilder name = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtilsHelper.isNotBlank(packageName)) {
            name.append(packageName);
            name.append('.');
        }
        name.append(this.getPopulator());
        return name.toString();
    }

    @Override
    protected String handleGetPopulatorPath() {
        return this.getFullyQualifiedPopulator().replace('.', '/');
    }

    @Override
    protected String handleGetPopulator() {
        return Objects.toString(this.getConfiguredProperty(MetafacadeWebGlobals.VIEW_POPULATOR_PATTERN), "")
                .replaceAll("\\{0\\}", StringUtilsHelper.upperCamelCaseName(this.getName()));
    }

    @Override
    protected boolean handleIsPopulatorRequired() {
        return !this.getFormActions().isEmpty() || !this.getVariables().isEmpty();
    }

    @Override
    protected boolean handleIsValidationRequired() {
        boolean required = false;
        for (final FrontEndAction action : this.getActions()) {
            if (action.isValidationRequired()) {
                required = true;
                break;
            }
        }
        return required;
    }

    @Override
    protected boolean handleIsPopup() {
        return Objects.toString(this.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_VIEW_TYPE), "")
                .equalsIgnoreCase(MetafacadeWebGlobals.ACTION_TYPE_POPUP);
    }

    @Override
    protected boolean handleIsNonTableVariablesPresent() {
        boolean present = false;
        for (final FrontEndParameter variable : this.getVariables()) {
            if (!variable.isTable()) {
                present = true;
                break;
            }
        }
        return present;
    }

    @Override
    protected boolean handleIsHasNameOfUseCase() {
        boolean sameName = false;
        final ModelElementFacade useCase = this.getUseCase();
        final String useCaseName = useCase != null ? useCase.getName() : null;
        if (useCaseName != null && useCaseName.equalsIgnoreCase(this.getName())) {
            sameName = true;
        }
        return sameName;
    }

    @Override
    protected String handleGetFormKey() {
        final Object formKeyValue = this.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_ACTION_FORM_KEY);
        return formKeyValue == null ? Objects.toString(this.getConfiguredProperty(MetafacadeWebGlobals.ACTION_FORM_KEY), "")
                : String.valueOf(formKeyValue);
    }

    @Override
    protected String handleGetFromOutcome() {
        return MetafacadeWebUtils.toWebResourceName(this.getUseCase().getName() + "-" + this.getName());
    }

    @Override
    protected boolean handleIsNeedsFileUpload() {
        if (this.getAllActionParameters().size() == 0) {
            return false;
        }

        for (final FrontEndParameter parameter : this.getAllActionParameters()) {
                if (parameter.isInputFile()) {
                    return true;
                }
                if (parameter.isComplex()) {
                    for (final Iterator attributes = parameter.getAttributes().iterator(); attributes.hasNext();) {
                        if (((FrontEndAttribute) attributes.next()).isInputFile()) {
                            return true;
                        }
                    }
                }
        }
        return false;
    }

    @Override
    protected String handleGetFullyQualifiedPageObjectClassPath() {
        return this.getFullyQualifiedPageObjectClassName().replace('.', '/');
    }

    @Override
    protected String handleGetFullyQualifiedPageObjectClassName() {
        final StringBuilder fullyQualifiedName = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtilsHelper.isNotBlank(packageName)) {
            fullyQualifiedName.append(packageName + '.');
        }
        return fullyQualifiedName.append(this.getPageObjectClassName()).toString();
    }

    @Override
    protected String handleGetPageObjectClassName() {
        return StringUtilsHelper.upperCamelCaseName(this.getName());
    }

    @Override
    protected List<FrontEndAction> handleGetFormActions() {
        final List<FrontEndAction> actions = new ArrayList<FrontEndAction>(this.getActions());
        for (final Iterator<FrontEndAction> iterator = actions.iterator(); iterator.hasNext();) {
            final FrontEndAction action = iterator.next();
            if (action.getFormFields().isEmpty()) {
                iterator.remove();
            }
        }
        return actions;
    }

    @Override
    protected List<FrontEndForward> handleGetActionForwards() {
        final List<FrontEndForward> actionForwards = new ArrayList<FrontEndForward>(this.getForwards());
        for (final Iterator<FrontEndForward> iterator = actionForwards.iterator(); iterator.hasNext();) {
            if (!(iterator.next() instanceof FrontEndAction)) {
                iterator.remove();
            }
        }
        return actionForwards;
    }

    @Override
    protected List<FrontEndForward> handleGetForwards() {
        final Map<String, FrontEndForward> forwards = new LinkedHashMap<String, FrontEndForward>();
        for (final FrontEndAction action : this.getActions()) {
            if (action != null && !action.isUseCaseStart()) {
                for (final FrontEndForward forward : action.getActionForwards()) {
                    forwards.put(((FrontEndForward) forward).getName(), forward);
                }
            }
        }
        return new ArrayList<FrontEndForward>(forwards.values());
    }

    @Override
    public Collection<FrontEndParameter> handleGetBackingValueVariables() {
        final Map<String, FrontEndParameter> variables = new LinkedHashMap<String, FrontEndParameter>();
        for (final FrontEndParameter frontEndParameter : this.getAllActionParameters())
        {
            if (frontEndParameter instanceof FrontEndParameter)
            {
                final FrontEndParameter parameter = (FrontEndParameter)frontEndParameter;
                final String parameterName = parameter.getName();
                final Collection<AttributeFacade> attributes = parameter.getAttributes();
                if (parameter.isBackingValueRequired() || parameter.isSelectable())
                {
                    if (parameter.isBackingValueRequired() || parameter.isSelectable())
                    {
                        variables.put(parameterName, parameter);
                    }
                }
                else
                {
                    boolean hasBackingValue = false;
                    for (final AttributeFacade attribute : attributes)
                    {
                        final FrontEndAttribute thymeleafAttribute = (FrontEndAttribute)attribute;
                        if (thymeleafAttribute.isSelectable(parameter) || thymeleafAttribute.isBackingValueRequired(parameter))
                        {
                            hasBackingValue = true;
                            break;
                        }
                    }
                    if (hasBackingValue)
                    {
                        variables.put(parameterName, parameter);
                    }
                }
            }
        }
        return new ArrayList<FrontEndParameter>(variables.values());
    }

    @Override
    public String handleGetPageObjectBeanName() {
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
    }

    @Override
    protected String handleGetFilename() {
        return UMLMetafacadeUtils.getFilename(this);
    }

    @Override
    protected Collection handleGetAllowedRoles() {
        return UMLMetafacadeUtils.getAllowedRoles(this);
    }
}
