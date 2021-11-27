package org.andromda.cartridges.jakarta.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.andromda.cartridges.jakarta.JakartaGlobals;
import org.andromda.cartridges.jakarta.JakartaProfile;
import org.andromda.cartridges.jakarta.JakartaUtils;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jakarta.metafacades.JakartaView.
 *
 * @see org.andromda.cartridges.jakarta.metafacades.JakartaView
 */
public class JakartaViewLogicImpl
    extends JakartaViewLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JakartaViewLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getMessageKey() + '.' + JakartaGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#getDocumentationKey()
     */
    protected String handleGetDocumentationKey()
    {
        return getMessageKey() + '.' + JakartaGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @return messageKey
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#getMessageKey()
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
    private boolean isNormalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(JakartaGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaViewLogic#handleGetMessageValue()
     */
    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @return documentationValue
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#getDocumentationValue()
     */
    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation(""));
        return value == null ? "" : value;
    }

    /**
     * @return getMessageKey() + '.' + JakartaGlobals.TITLE_MESSAGE_KEY_SUFFIX
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#getTitleKey()
     */
    protected String handleGetTitleKey()
    {
        return this.getMessageKey() + '.' + JakartaGlobals.TITLE_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @return toPhrase(getName())
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#getTitleValue()
     */
    protected String handleGetTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @return path
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#getPath()
     */
    protected String handleGetPath()
    {
        final StringBuilder path = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            path.append(packageName + '.');
        }
        path.append(JakartaUtils.toWebResourceName(StringUtils.trimToEmpty(this.getName())).replace(
                '.',
                '/'));
        return '/' + path.toString().replace(
            '.',
            '/');
    }

    /**
     * @return forwards
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#getForwards()
     */
    protected List<ModelElementFacade> handleGetForwards()
    {
        final Map<String, ModelElementFacade> forwards = new LinkedHashMap<String, ModelElementFacade>();
        for (final FrontEndAction action : this.getActions())
        {
            if (action != null && !action.isUseCaseStart())
            {
                for (final FrontEndForward forward : action.getActionForwards())
                {
                    if (forward instanceof JakartaForward)
                    {
                        forwards.put(((JakartaForward)forward).getName(), forward);
                    }
                    else if (forward instanceof JakartaAction)
                    {
                        forwards.put(((JakartaAction)forward).getName(), forward);
                    }
                }
            }
        }
        return new ArrayList<ModelElementFacade>(forwards.values());
    }

    /**
     * @return tables
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaAction#isTableLink()
     */
    protected List<JakartaParameter> handleGetTables()
    {
        final List<JakartaParameter> tables = new ArrayList<JakartaParameter>();
        final List<FrontEndParameter> variables = this.getVariables();
        for (FrontEndParameter parameter : variables)
        {
            if (parameter instanceof JakartaParameter)
            {
                final JakartaParameter variable = (JakartaParameter)parameter;
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
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaViewLogic#getActionForwards()
     */
    protected List<JakartaForward> handleGetActionForwards()
    {
        final List<JakartaForward> actionForwards = new ArrayList<JakartaForward>(this.getForwards());
        for (final Iterator<JakartaForward> iterator = actionForwards.iterator(); iterator.hasNext();)
        {
            if (!(iterator.next() instanceof JakartaAction))
            {
                iterator.remove();
            }
        }
        return actionForwards;
    }

    /**
     * @return populatorName
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaViewLogic#getFullyQualifiedPopulator()
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
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaViewLogic#getPopulator()
     */
    protected String handleGetPopulator()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(JakartaGlobals.VIEW_POPULATOR_PATTERN)).replaceAll(
            "\\{0\\}",
            StringUtilsHelper.upperCamelCaseName(this.getName()));
    }

    /**
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaViewLogic#handleGetFormActions()
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
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#getFormKey()
     */
    protected String handleGetFormKey()
    {
        final Object formKeyValue = this.findTaggedValue(JakartaProfile.TAGGEDVALUE_ACTION_FORM_KEY);
        return formKeyValue == null ? ObjectUtils.toString(this.getConfiguredProperty(JakartaGlobals.ACTION_FORM_KEY))
                                    : String.valueOf(formKeyValue);
    }

    /**
     * @return getFullyQualifiedPopulator().replace('.', '/')
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#getPopulatorPath()
     */
    protected String handleGetPopulatorPath()
    {
        return this.getFullyQualifiedPopulator().replace(
            '.',
            '/');
    }

    /**
     * @return !getFormActions().isEmpty() || !getVariables().isEmpty()
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#isPopulatorRequired()
     */
    protected boolean handleIsPopulatorRequired()
    {
        return !this.getFormActions().isEmpty() || !this.getVariables().isEmpty();
    }

    /**
     * @return validationRequired
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#isPopulatorRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        boolean required = false;
        for (final FrontEndAction action : this.getActions())
        {
            if (((JakartaAction)action).isValidationRequired())
            {
                required = true;
                break;
            }
        }
        return required;
    }

    /**
     * @return isPopup
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#isPopup()
     */
    protected boolean handleIsPopup()
    {
        return ObjectUtils.toString(this.findTaggedValue(JakartaProfile.TAGGEDVALUE_VIEW_TYPE)).equalsIgnoreCase(
            JakartaGlobals.ACTION_TYPE_POPUP);
    }

    /**
     * @return nonTableVariablesPresent
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#isNonTableVariablesPresent()
     */
    protected boolean handleIsNonTableVariablesPresent()
    {
        boolean present = false;
        for (final FrontEndParameter variable : this.getVariables())
        {
            if (!variable.isTable())
            {
                present = true;
                break;
            }
        }
        return present;
    }

    /**
     * @return hasNameOfUseCase
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#isHasNameOfUseCase()
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
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#getBackingValueVariables()
     */
    protected List<JakartaParameter> handleGetBackingValueVariables()
    {
        final Map<String, JakartaParameter> variables = new LinkedHashMap<String, JakartaParameter>();
        for (final FrontEndParameter frontEndParameter : this.getAllActionParameters())
        {
            if (frontEndParameter instanceof JakartaParameter)
            {
                final JakartaParameter parameter = (JakartaParameter)frontEndParameter;
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
                        final JakartaAttribute jakartaAttribute = (JakartaAttribute)attribute;
                        if (jakartaAttribute.isSelectable(parameter) || jakartaAttribute.isBackingValueRequired(parameter))
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
        return new ArrayList<JakartaParameter>(variables.values());
    }

    /**
     * @return toWebResourceName(this.getUseCase().getName() + "-" + this.getName())
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#getFromOutcome()
     */
    protected String handleGetFromOutcome()
    {
        return JakartaUtils.toWebResourceName(this.getUseCase().getName() + "-" + this.getName());
    }

    /**
     * @return needsFileUpload
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#isNeedsFileUpload()
     */
    protected boolean handleIsNeedsFileUpload()
    {
        if(this.getAllActionParameters().size() == 0)
        {
            return false;
        }

        for (final FrontEndParameter feParameter : this.getAllActionParameters())
        {
            if (feParameter instanceof JakartaParameter)
            {
                final JakartaParameter parameter = (JakartaParameter)feParameter;
                if(parameter.isInputFile())
                {
                    return true;
                }
                if(parameter.isComplex())
                {
                    for(final Iterator attributes = parameter.getAttributes().iterator(); attributes.hasNext();)
                    {
                        if(((JakartaAttribute)attributes.next()).isInputFile())
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#getFullyQualifiedPageObjectClassPath()
     */
    @Override
    protected String handleGetFullyQualifiedPageObjectClassPath() 
    {
        return this.getFullyQualifiedPageObjectClassName().replace(
                        '.',
                        '/');
    }

    /**
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#getFullyQualifiedPageObjectClassName()
     */
    @Override
    protected String handleGetFullyQualifiedPageObjectClassName() 
    {
        final StringBuilder fullyQualifiedName = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append(packageName + '.');
        }
        return fullyQualifiedName.append(this.getPageObjectClassName()).toString();
    }

    /**
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#getPageObjectClassName()
     */
    @Override
    protected String handleGetPageObjectClassName() 
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName());
    }

    /**
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaView#getPageObjectBeanName()
     */
    @Override
    protected String handleGetPageObjectBeanName() 
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
    }

    @Override
    protected String handleGetRestPath() {
        // TODO Auto-generated method stub
        return null;
    }
}
