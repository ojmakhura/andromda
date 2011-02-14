package org.andromda.cartridges.jsf.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.andromda.cartridges.jsf.JSFGlobals;
import org.andromda.cartridges.jsf.JSFProfile;
import org.andromda.cartridges.jsf.JSFUtils;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFView.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFView
 */
public class JSFViewLogicImpl
    extends JSFViewLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JSFViewLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getMessageKey() + '.' + JSFGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#getDocumentationKey()
     */
    protected String handleGetDocumentationKey()
    {
        return getMessageKey() + '.' + JSFGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @return messageKey
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        final StringBuilder messageKey = new StringBuilder();

        if (!this.isNormalizeMessages())
        {
            final UseCaseFacade useCase = this.getUseCase();
            if (useCase != null)
            {
                messageKey.append(StringUtilsHelper.toResourceMessageKey(useCase.getName()));
                messageKey.append('.');
            }
        }

        messageKey.append(StringUtilsHelper.toResourceMessageKey(getName()));
        return messageKey.toString();
    }

    /**
     * Indicates whether or not we should normalize messages.
     *
     * @return true/false
     */
    private final boolean isNormalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(JSFGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFViewLogic#handleGetMessageValue()
     */
    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @return documentationValue
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#getDocumentationValue()
     */
    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation(""));
        return value == null ? "" : value;
    }

    /**
     * @return getMessageKey() + '.' + JSFGlobals.TITLE_MESSAGE_KEY_SUFFIX
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#getTitleKey()
     */
    protected String handleGetTitleKey()
    {
        return this.getMessageKey() + '.' + JSFGlobals.TITLE_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @return toPhrase(getName())
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#getTitleValue()
     */
    protected String handleGetTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @return path
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#getPath()
     */
    protected String handleGetPath()
    {
        final StringBuilder path = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            path.append(packageName + '.');
        }
        path.append(JSFUtils.toWebResourceName(StringUtils.trimToEmpty(this.getName())).replace(
                '.',
                '/'));
        return '/' + path.toString().replace(
            '.',
            '/');
    }

    /**
     * @return forwards
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#getForwards()
     */
    protected List<ModelElementFacade> handleGetForwards()
    {
        final Map<String, ModelElementFacade> forwards = new LinkedHashMap<String, ModelElementFacade>();
        for (final Iterator iterator = this.getActions().iterator(); iterator.hasNext();)
        {
            final FrontEndAction action = (FrontEndAction)iterator.next();
            if (action != null && !action.isUseCaseStart())
            {
                for (final FrontEndForward forward : action.getActionForwards())
                {
                    if (forward instanceof JSFForward)
                    {
                        forwards.put(((JSFForward)forward).getName(), forward);
                    }
                    else if (forward instanceof JSFAction)
                    {
                        forwards.put(((JSFAction)forward).getName(), forward);
                    }  
                }
            }
        }
        return new ArrayList<ModelElementFacade>(forwards.values());
    }

    /**
     * @return tables
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#isTableLink()
     */
    protected List<JSFParameter> handleGetTables()
    {
        final List<JSFParameter> tables = new ArrayList<JSFParameter>();
        final List<FrontEndParameter> variables = this.getVariables();
        for (FrontEndParameter parameter : variables)
        {
            if (parameter instanceof JSFParameter)
            {
                final JSFParameter variable = (JSFParameter)parameter;
                if (variable.isTable())
                {
                    tables.add(variable);
                }
            }
        }
        return tables;
    }

    /**
     * @return actionForwards
     * @see org.andromda.cartridges.jsf.metafacades.JSFViewLogic#getActionForwards()
     */
    protected List<JSFForward> handleGetActionForwards()
    {
        final List<JSFForward> actionForwards = new ArrayList<JSFForward>(this.getForwards());
        for (final Iterator<JSFForward> iterator = actionForwards.iterator(); iterator.hasNext();)
        {
            if (!(iterator.next() instanceof JSFAction))
            {
                iterator.remove();
            }
        }
        return actionForwards;
    }

    /**
     * @return populatorName
     * @see org.andromda.cartridges.jsf.metafacades.JSFViewLogic#getFullyQualifiedPopulator()
     */
    protected String handleGetFullyQualifiedPopulator()
    {
        final StringBuilder name = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            name.append(packageName);
            name.append('.');
        }
        name.append(this.getPopulator());
        return name.toString();
    }

    /**
     * @return populator
     * @see org.andromda.cartridges.jsf.metafacades.JSFViewLogic#getPopulator()
     */
    protected String handleGetPopulator()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.VIEW_POPULATOR_PATTERN)).replaceAll(
            "\\{0\\}",
            StringUtilsHelper.upperCamelCaseName(this.getName()));
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFViewLogic#handleGetFormActions()
     */
    protected List<FrontEndAction> handleGetFormActions()
    {
        final List<FrontEndAction> actions = new ArrayList<FrontEndAction>(this.getActions());
        for (final Iterator<FrontEndAction> iterator = actions.iterator(); iterator.hasNext();)
        {
            final FrontEndAction action = iterator.next();
            if (action.getFormFields().isEmpty())
            {
                iterator.remove();
            }
        }
        return actions;
    }

    /**
     * @return formKey
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#getFormKey()
     */
    protected String handleGetFormKey()
    {
        final Object formKeyValue = this.findTaggedValue(JSFProfile.TAGGEDVALUE_ACTION_FORM_KEY);
        return formKeyValue == null ? ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.ACTION_FORM_KEY))
                                    : String.valueOf(formKeyValue);
    }

    /**
     * @return getFullyQualifiedPopulator().replace('.', '/')
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#getPopulatorPath()
     */
    protected String handleGetPopulatorPath()
    {
        return this.getFullyQualifiedPopulator().replace(
            '.',
            '/');
    }

    /**
     * @return !getFormActions().isEmpty() || !getVariables().isEmpty()
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#isPopulatorRequired()
     */
    protected boolean handleIsPopulatorRequired()
    {
        return !this.getFormActions().isEmpty() || !this.getVariables().isEmpty();
    }

    /**
     * @return validationRequired
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#isPopulatorRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        boolean required = false;
        final Collection actions = getActions();
        for (final Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
        {
            final JSFAction action = (JSFAction)actionIterator.next();
            if (action.isValidationRequired())
            {
                required = true;
                break;
            }
        }
        return required;
    }

    /**
     * @return isPopup
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#isPopup()
     */
    protected boolean handleIsPopup()
    {
        return ObjectUtils.toString(this.findTaggedValue(JSFProfile.TAGGEDVALUE_VIEW_TYPE)).equalsIgnoreCase(
            JSFGlobals.VIEW_TYPE_POPUP);
    }

    /**
     * @return nonTableVariablesPresent
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#isNonTableVariablesPresent()
     */
    protected boolean handleIsNonTableVariablesPresent()
    {
        boolean present = false;
        for (final Iterator iterator = this.getVariables().iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof FrontEndParameter)
            {
                final FrontEndParameter variable = (FrontEndParameter)object;
                if (!variable.isTable())
                {
                    present = true;
                    break;
                }
            }
        }
        return present;
    }

    /**
     * @return hasNameOfUseCase
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#isHasNameOfUseCase()
     */
    protected boolean handleIsHasNameOfUseCase()
    {
        boolean sameName = false;
        final ModelElementFacade useCase = this.getUseCase();
        final String useCaseName = useCase != null ? useCase.getName() : null;
        if (useCaseName != null && useCaseName.equalsIgnoreCase(this.getName()))
        {
            sameName = true;
        }
        return sameName;
    }

    /**
     * @return backingValueVariables
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#getBackingValueVariables()
     */
    protected List<JSFParameter> handleGetBackingValueVariables()
    {
        final Map<String, JSFParameter> variables = new LinkedHashMap<String, JSFParameter>();
        for (final FrontEndParameter frontEndParameter : this.getAllActionParameters())
        {
            if (frontEndParameter instanceof JSFParameter)
            {
                final JSFParameter parameter = (JSFParameter)frontEndParameter;
                final String parameterName = parameter.getName();
                final Collection attributes = parameter.getAttributes();
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
                    for (final Iterator attributeIterator = attributes.iterator(); attributeIterator.hasNext();)
                    {
                        final JSFAttribute attribute = (JSFAttribute)attributeIterator.next();
                        if (attribute.isSelectable(parameter) || attribute.isBackingValueRequired(parameter))
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
        return new ArrayList<JSFParameter>(variables.values());
    }

    /**
     * @return toWebResourceName(this.getUseCase().getName() + "-" + this.getName())
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#getFromOutcome()
     */
    protected String handleGetFromOutcome()
    {
        return JSFUtils.toWebResourceName(this.getUseCase().getName() + "-" + this.getName());
    }

    /**
     * @return needsFileUpload
     * @see org.andromda.cartridges.jsf.metafacades.JSFView#isNeedsFileUpload()
     */
    protected boolean handleIsNeedsFileUpload()
    {
        if(this.getAllActionParameters().size() == 0)
            return false;
        
        for (final Iterator iterator = this.getAllActionParameters().iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof JSFParameter){
                final JSFParameter parameter = (JSFParameter)object;
                if(parameter.isInputFile())
                     return true;
                if(parameter.isComplex()){
                    for(final Iterator attributes = parameter.getAttributes().iterator(); attributes.hasNext();)
                        if(((JSFAttribute)attributes.next()).isInputFile())
                            return true;
                }
            }
        }
        return false;
    }
}
