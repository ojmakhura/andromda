package org.andromda.cartridges.thymeleaf.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.andromda.cartridges.thymeleaf.ThymeleafGlobals;
import org.andromda.cartridges.thymeleaf.ThymeleafProfile;
import org.andromda.cartridges.thymeleaf.ThymeleafUtils;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView.
 *
 * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView
 */
public class ThymeleafViewLogicImpl
    extends ThymeleafViewLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ThymeleafViewLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getMessageKey() + '.' + ThymeleafGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#getDocumentationKey()
     */
    protected String handleGetDocumentationKey()
    {
        return getMessageKey() + '.' + ThymeleafGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @return messageKey
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#getMessageKey()
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
        final String normalizeMessages = (String)getConfiguredProperty(ThymeleafGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafViewLogic#handleGetMessageValue()
     */
    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @return documentationValue
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#getDocumentationValue()
     */
    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation(""));
        return value == null ? "" : value;
    }

    /**
     * @return getMessageKey() + '.' + ThymeleafGlobals.TITLE_MESSAGE_KEY_SUFFIX
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#getTitleKey()
     */
    protected String handleGetTitleKey()
    {
        return this.getMessageKey() + '.' + ThymeleafGlobals.TITLE_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @return toPhrase(getName())
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#getTitleValue()
     */
    protected String handleGetTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @return path
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#getPath()
     */
    protected String handleGetPath()
    {
        final StringBuilder path = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            path.append(packageName + '.');
        }
        path.append(ThymeleafUtils.toWebResourceName(StringUtils.trimToEmpty(this.getName())).replace(
                '.',
                '/'));
        return '/' + path.toString().replace(
            '.',
            '/');
    }

    /**
     * @return forwards
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#getForwards()
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
                    if (forward instanceof ThymeleafForward)
                    {
                        forwards.put(((ThymeleafForward)forward).getName(), forward);
                    }
                    else if (forward instanceof ThymeleafAction)
                    {
                        forwards.put(((ThymeleafAction)forward).getName(), forward);
                    }
                }
            }
        }
        return new ArrayList<ModelElementFacade>(forwards.values());
    }

    /**
     * @return tables
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#isTableLink()
     */
    protected List<ThymeleafParameter> handleGetTables()
    {
        final List<ThymeleafParameter> tables = new ArrayList<ThymeleafParameter>();
        final List<FrontEndParameter> variables = this.getVariables();
        for (FrontEndParameter parameter : variables)
        {
            if (parameter instanceof ThymeleafParameter)
            {
                final ThymeleafParameter variable = (ThymeleafParameter)parameter;
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
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafViewLogic#getActionForwards()
     */
    protected List<ThymeleafForward> handleGetActionForwards()
    {
        final List<ThymeleafForward> actionForwards = new ArrayList<ThymeleafForward>(this.getForwards());
        for (final Iterator<ThymeleafForward> iterator = actionForwards.iterator(); iterator.hasNext();)
        {
            if (!(iterator.next() instanceof ThymeleafAction))
            {
                iterator.remove();
            }
        }
        return actionForwards;
    }

    /**
     * @return populatorName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafViewLogic#getFullyQualifiedPopulator()
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
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafViewLogic#getPopulator()
     */
    protected String handleGetPopulator()
    {
        return Objects.toString(this.getConfiguredProperty(ThymeleafGlobals.VIEW_POPULATOR_PATTERN)).replaceAll(
            "\\{0\\}",
            StringUtilsHelper.upperCamelCaseName(this.getName()));
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafViewLogic#handleGetFormActions()
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
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#getFormKey()
     */
    protected String handleGetFormKey()
    {
        final Object formKeyValue = this.findTaggedValue(ThymeleafProfile.TAGGEDVALUE_ACTION_FORM_KEY);
        return formKeyValue == null ? Objects.toString(this.getConfiguredProperty(ThymeleafGlobals.ACTION_FORM_KEY))
                                    : String.valueOf(formKeyValue);
    }

    /**
     * @return getFullyQualifiedPopulator().replace('.', '/')
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#getPopulatorPath()
     */
    protected String handleGetPopulatorPath()
    {
        return this.getFullyQualifiedPopulator().replace(
            '.',
            '/');
    }

    /**
     * @return !getFormActions().isEmpty() || !getVariables().isEmpty()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#isPopulatorRequired()
     */
    protected boolean handleIsPopulatorRequired()
    {
        return !this.getFormActions().isEmpty() || !this.getVariables().isEmpty();
    }

    /**
     * @return validationRequired
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#isPopulatorRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        boolean required = false;
        for (final FrontEndAction action : this.getActions())
        {
            if (((ThymeleafAction)action).isValidationRequired())
            {
                required = true;
                break;
            }
        }
        return required;
    }

    /**
     * @return isPopup
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#isPopup()
     */
    protected boolean handleIsPopup()
    {
        return Objects.toString(this.findTaggedValue(ThymeleafProfile.TAGGEDVALUE_VIEW_TYPE)).equalsIgnoreCase(
            ThymeleafGlobals.ACTION_TYPE_POPUP);
    }

    /**
     * @return nonTableVariablesPresent
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#isNonTableVariablesPresent()
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
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#isHasNameOfUseCase()
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
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#getBackingValueVariables()
     */
    protected List<ThymeleafParameter> handleGetBackingValueVariables()
    {
        final Map<String, ThymeleafParameter> variables = new LinkedHashMap<String, ThymeleafParameter>();
        for (final FrontEndParameter frontEndParameter : this.getAllActionParameters())
        {
            if (frontEndParameter instanceof ThymeleafParameter)
            {
                final ThymeleafParameter parameter = (ThymeleafParameter)frontEndParameter;
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
                        final ThymeleafAttribute thymeleafAttribute = (ThymeleafAttribute)attribute;
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
        return new ArrayList<ThymeleafParameter>(variables.values());
    }

    /**
     * @return toWebResourceName(this.getUseCase().getName() + "-" + this.getName())
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#getFromOutcome()
     */
    protected String handleGetFromOutcome()
    {
        return ThymeleafUtils.toWebResourceName(this.getUseCase().getName() + "-" + this.getName());
    }

    /**
     * @return needsFileUpload
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#isNeedsFileUpload()
     */
    protected boolean handleIsNeedsFileUpload()
    {
        if(this.getAllActionParameters().size() == 0)
        {
            return false;
        }

        for (final FrontEndParameter feParameter : this.getAllActionParameters())
        {
            if (feParameter instanceof ThymeleafParameter)
            {
                final ThymeleafParameter parameter = (ThymeleafParameter)feParameter;
                if(parameter.isInputFile())
                {
                    return true;
                }
                if(parameter.isComplex())
                {
                    for(final Iterator attributes = parameter.getAttributes().iterator(); attributes.hasNext();)
                    {
                        if(((ThymeleafAttribute)attributes.next()).isInputFile())
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
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#getFullyQualifiedPageObjectClassPath()
     */
    @Override
    protected String handleGetFullyQualifiedPageObjectClassPath() 
    {
        return this.getFullyQualifiedPageObjectClassName().replace(
                        '.',
                        '/');
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#getFullyQualifiedPageObjectClassName()
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
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#getPageObjectClassName()
     */
    @Override
    protected String handleGetPageObjectClassName() 
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName());
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafView#getPageObjectBeanName()
     */
    @Override
    protected String handleGetPageObjectBeanName() 
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
    }
}
