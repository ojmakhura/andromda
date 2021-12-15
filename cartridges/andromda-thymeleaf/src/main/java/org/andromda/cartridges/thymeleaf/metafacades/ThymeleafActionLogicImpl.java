package org.andromda.cartridges.thymeleaf.metafacades;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.andromda.cartridges.thymeleaf.ThymeleafGlobals;
import org.andromda.cartridges.thymeleaf.ThymeleafProfile;
import org.andromda.cartridges.thymeleaf.ThymeleafUtils;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActionState;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction.
 *
 * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction
 */
public class ThymeleafActionLogicImpl
    extends ThymeleafActionLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ThymeleafActionLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger LOGGER = Logger.getLogger(ThymeleafActionLogicImpl.class);

    /**
     * @return getFormBeanName(true)
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getFormBeanName()
     */
    protected String handleGetFormBeanName()
    {
        return this.getFormBeanName(true);
    }

    /**
     * Constructs the form bean name, with our without prefixing the use case name.
     *
     * @param withUseCaseName whether or not to prefix the use case name.
     * @return the constructed form bean name.
     */
    private String getFormBeanName(boolean withUseCaseName)
    {
        final String pattern = Objects.toString(this.getConfiguredProperty(ThymeleafGlobals.FORM_BEAN_PATTERN));
        final ModelElementFacade useCase = this.getUseCase();
        final String useCaseName = withUseCaseName && useCase != null
            ? StringUtilsHelper.lowerCamelCaseName(useCase.getName()) : "";
        final String formBeanName = pattern.replaceFirst("\\{0\\}", useCaseName);
        final String triggerName = !pattern.equals(formBeanName)
            ? StringUtils.capitalize(this.getTriggerName()) : this.getTriggerName();
        return formBeanName.replaceFirst(
            "\\{1\\}",
            triggerName);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    public String getName()
    {
        return ThymeleafUtils.toWebResourceName(this.getUseCase().getName() + "-" + super.getName());
    }

    /**
     * @return useCase.getName()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getTriggerName()
     */
    protected String handleGetTriggerName()
    {
        String name = null;
        if (this.isExitingInitialState())
        {
            final ThymeleafUseCase useCase = (ThymeleafUseCase)this.getUseCase();
            if (useCase != null)
            {
                name = useCase.getName();
            }
        }
        else
        {
            final EventFacade trigger = this.getTrigger();
            final String suffix = trigger == null ? this.getTarget().getName() : trigger.getName();
            name = this.getSource().getName() + ' ' + suffix;
        }
        return StringUtilsHelper.lowerCamelCaseName(name);
    }

    /**
     * @return formImplementationName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getFormImplementationName()
     */
    protected String handleGetFormImplementationName()
    {
        final String pattern =
            Objects.toString(this.getConfiguredProperty(ThymeleafGlobals.FORM_IMPLEMENTATION_PATTERN));
        return pattern.replaceFirst(
            "\\{0\\}",
            StringUtils.capitalize(this.getTriggerName()));
    }

    /**
     * @return isTableAction
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getFullyQualifiedFormImplementationName()
     */
    protected boolean handleIsTableAction()
    {
        return ThymeleafGlobals.ACTION_TYPE_TABLE.equals(this.findTaggedValue(ThymeleafProfile.TAGGEDVALUE_ACTION_TYPE));
    }

    /**
     * @return fullyQualifiedFormImplementationName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getFullyQualifiedFormImplementationName()
     */
    protected String handleGetFullyQualifiedFormImplementationName()
    {
        final StringBuilder fullyQualifiedName = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append(packageName + '.');
        }
        return fullyQualifiedName.append(this.getFormImplementationName()).toString();
    }

    /**
     * @return fullyQualifiedFormImplementationPath
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getFullyQualifiedFormImplementationPath()
     */
    protected String handleGetFullyQualifiedFormImplementationPath()
    {
        return this.getFullyQualifiedFormImplementationName().replace(
            '.',
            '/');
    }

    /**
     * @return scope
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getFullyQualifiedFormImplementationPath()
     */
    protected String handleGetFormScope()
    {
        String scope = Objects.toString(this.findTaggedValue(ThymeleafProfile.TAGGEDVALUE_ACTION_FORM_SCOPE));
        if (StringUtils.isEmpty(scope))
        {
            scope = Objects.toString(this.getConfiguredProperty(ThymeleafGlobals.FORM_SCOPE));
        }
        return scope;
    }

    /**
     * @return formImplementationInterfaceList
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getFormImplementationInterfaceList()
     */
    protected String handleGetFormImplementationInterfaceList()
    {
        final List<FrontEndControllerOperation> deferredOperations = this.getDeferredOperations();
        for (final Iterator<FrontEndControllerOperation> iterator = deferredOperations.iterator(); iterator.hasNext();)
        {
            // - remove any forms that don't have arguments
            final ThymeleafControllerOperation operation = (ThymeleafControllerOperation)iterator.next();
            if (operation.getArguments().isEmpty())
            {
                iterator.remove();
            }
        }
        final StringBuilder list = new StringBuilder();
        for (final Iterator<FrontEndControllerOperation> iterator = deferredOperations.iterator(); iterator.hasNext();)
        {
            final ThymeleafControllerOperation operation = (ThymeleafControllerOperation)iterator.next();
            list.append(operation.getFormName());
            if (iterator.hasNext())
            {
                list.append(", ");
            }
        }
        return list.toString();
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafActionLogic#handleGetPath()
     */
    protected String handleGetPath()
    {
        String path = this.getPathRoot() + '/' + ThymeleafUtils.toWebResourceName(this.getTriggerName());
        if (this.isExitingInitialState())
        {
            final ThymeleafUseCase useCase = (ThymeleafUseCase)this.getUseCase();
            if (useCase != null && useCase.isViewHasNameOfUseCase())
            {
                // - add the uc prefix to make the trigger name unique
                //   when a view contained within the use case has the same name
                //   as the use case
                path = path + "uc";
            }
        }
        return path;
    }

    /**
     * @return pathRoot
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getPathRoot()
     */
    protected String handleGetPathRoot()
    {
        final StringBuilder pathRoot = new StringBuilder();
        final ThymeleafUseCase useCase = (ThymeleafUseCase)this.getUseCase();
        if (useCase != null)
        {
            pathRoot.append(useCase.getPathRoot());
        }
        return pathRoot.toString();
    }

    /**
     * @return messageKey
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        String messageKey = null;

        final Object trigger = this.getTrigger();
        if (trigger instanceof ThymeleafEvent)
        {
            final ThymeleafEvent actionTrigger = (ThymeleafEvent)trigger;
            messageKey = actionTrigger.getMessageKey();
        }
        return messageKey;
    }

    /**
     * @return documentationKey
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getDocumentationKey()
     */
    protected String handleGetDocumentationKey()
    {
        final Object trigger = this.getTrigger();
        ThymeleafEvent event = null;
        if (trigger instanceof ThymeleafEvent)
        {
            event = (ThymeleafEvent)trigger;
        }
        return (event == null ? this.getMessageKey() + ".is.an.action.without.trigger" : event.getMessageKey()) +
            '.' + ThymeleafGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @return documentationValue
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getDocumentationValue()
     */
    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation(
                    "",
                    64,
                    false));
        return value == null ? "" : value;
    }

    /**
     * @return viewFragmentPath
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getViewFragmentPath()
     */
    protected String handleGetViewFragmentPath()
    {
        return '/' + this.getPackageName().replace(
            '.',
            '/') + '/' + ThymeleafUtils.toWebResourceName(this.getTriggerName());
    }

    /**
     * @return tableLink
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getTableLinkName()
     */
    protected String handleGetTableLinkName()
    {
        String tableLink = null;

        final Object value = findTaggedValue(ThymeleafProfile.TAGGEDVALUE_ACTION_TABLELINK);
        if (value != null)
        {
            tableLink = StringUtils.trimToNull(value.toString());

            if (tableLink != null)
            {
                final int columnOffset = tableLink.indexOf('.');
                tableLink = columnOffset == -1 ? tableLink : tableLink.substring(
                        0,
                        columnOffset);
            }
        }

        return tableLink;
    }

    /**
     * @return tableLink
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getTableLinkColumnName()
     */
    protected String handleGetTableLinkColumnName()
    {
        String tableLink = null;
        final Object value = findTaggedValue(ThymeleafProfile.TAGGEDVALUE_ACTION_TABLELINK);
        if (value != null)
        {
            tableLink = StringUtils.trimToNull(value.toString());

            if (tableLink != null)
            {
                final int columnOffset = tableLink.indexOf('.');
                tableLink = (columnOffset == -1 || columnOffset == tableLink.length() - 1)
                    ? null : tableLink.substring(columnOffset + 1);
            }
        }
        return tableLink;
    }

    /**
     * @return tableLinkParameter
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#isTableLink()
     */
    protected Object handleGetTableLinkParameter()
    {
        ThymeleafParameter tableLinkParameter = null;
        final String tableLinkName = this.getTableLinkName();
        if (tableLinkName != null)
        {
            final ThymeleafView view = (ThymeleafView)this.getInput();
            if (view != null)
            {
                final List<FrontEndParameter> tables = view.getTables();
                for (int ctr = 0; ctr < tables.size() && tableLinkParameter == null; ctr++)
                {
                    final Object object = tables.get(ctr);
                    if (object instanceof ThymeleafParameter)
                    {
                        final ThymeleafParameter table = (ThymeleafParameter)object;
                        if (tableLinkName.equals(table.getName()))
                        {
                            tableLinkParameter = table;
                        }
                    }
                }
            }
        }
        return tableLinkParameter;
    }

    /**
     * @return getTableLinkParameter() != null
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#isTableLink()
     */
    protected boolean handleIsTableLink()
    {
        return this.getTableLinkParameter() != null;
    }

    /**
     * @return hyperlink
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#isHyperlink()
     */
    protected boolean handleIsHyperlink()
    {
        final Object value = findTaggedValue(ThymeleafProfile.TAGGEDVALUE_ACTION_TYPE);
        return ThymeleafGlobals.ACTION_TYPE_HYPERLINK.equalsIgnoreCase(value == null ? null : value.toString());
    }

    /**
     * @return StringUtilsHelper.upperCamelCaseName(this.getTriggerName())
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getActionClassName()
     */
    protected String handleGetActionClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getTriggerName());
    }

    /**
     * @return fullyQualifiedActionClassPath
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getFullyQualifiedActionClassPath()
     */
    protected String handleGetFullyQualifiedActionClassPath()
    {
        return this.getFullyQualifiedActionClassName().replace(
            '.',
            '/') + ".java";
    }

    /**
     * Overridden to provide the owning use case's package name.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName()
     */
    public String getPackageName()
    {
        final UseCaseFacade useCase = this.getUseCase();
        return useCase != null ? useCase.getPackageName() : "";
    }

    /**
     * @return getTriggerName()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getControllerAction()
     */
    protected String handleGetControllerAction()
    {
        return this.getTriggerName();
    }

    /**
     * @return fullyQualifiedActionClassName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getFullyQualifiedActionClassName()
     */
    protected String handleGetFullyQualifiedActionClassName()
    {
        final StringBuilder path = new StringBuilder();
        final ThymeleafUseCase useCase = (ThymeleafUseCase)this.getUseCase();
        if (useCase != null)
        {
            final String packageName = useCase.getPackageName();
            if (StringUtils.isNotBlank(packageName))
            {
                path.append(packageName);
                path.append('.');
            }
        }
        path.append(this.getActionClassName());
        return path.toString();
    }

    /**
     * @return findTaggedValue(ThymeleafProfile.TAGGEDVALUE_ACTION_RESETTABLE) isTrue
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#isResettable()
     */
    protected boolean handleIsResettable()
    {
        final Object value = findTaggedValue(ThymeleafProfile.TAGGEDVALUE_ACTION_RESETTABLE);
        return this.isTrue(value == null ? null : value.toString());
    }

    /**
     * Convenient method to detect whether or not a String instance represents a boolean <code>true</code> value.
     */
    private boolean isTrue(String string)
    {
        return "yes".equalsIgnoreCase(string) || "true".equalsIgnoreCase(string) || "on".equalsIgnoreCase(string) ||
        "1".equalsIgnoreCase(string);
    }

    /**
     * @return otherActions
     */
    protected List<FrontEndAction> handleGetOtherUseCaseFormActions()
    {
        final List<FrontEndAction> otherActions = new ArrayList<FrontEndAction>(this.getUseCase().getActions());
        for (final Iterator<FrontEndAction> iterator = otherActions.iterator(); iterator.hasNext();)
        {
            final FrontEndAction action = iterator.next();

            // - remove this action and any forms that don't have form fields
            if (action.equals(this.THIS()) || action.getFormFields().isEmpty())
            {
                iterator.remove();
            }
        }
        return otherActions;
    }

    /**
     * @return hiddenParameters
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getFormKey()
     */
    protected String handleGetFormKey()
    {
        final Object formKeyValue = this.findTaggedValue(ThymeleafProfile.TAGGEDVALUE_ACTION_FORM_KEY);
        return formKeyValue == null ? Objects.toString(this.getConfiguredProperty(ThymeleafGlobals.ACTION_FORM_KEY))
                                    : String.valueOf(formKeyValue);
    }

    /**
     * @return hiddenParameters
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getHiddenParameters()
     */
    protected List<FrontEndParameter> handleGetHiddenParameters()
    {
        final List<FrontEndParameter> hiddenParameters = new ArrayList<FrontEndParameter>(this.getParameters());
        CollectionUtils.filter(
            hiddenParameters,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    boolean valid = false;
                    if (object instanceof ThymeleafParameter)
                    {
                        final ThymeleafParameter parameter = (ThymeleafParameter)object;
                        valid = parameter.isInputHidden();
                        if (!valid)
                        {
                            for (final Iterator iterator = parameter.getAttributes().iterator(); iterator.hasNext();)
                            {
                                ThymeleafAttribute attribute = (ThymeleafAttribute)iterator.next();
                                valid = attribute.isInputHidden();
                                if (valid)
                                {
                                    break;
                                }
                            }
                        }
                    }
                    return valid;
                }
            });
        return hiddenParameters;
    }

    /**
     * @return required
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getHiddenParameters()
     */
    protected boolean handleIsValidationRequired()
    {
        boolean required = false;
        for (final FrontEndParameter frontEndParam : this.getParameters())
        {
            if (frontEndParam instanceof ThymeleafParameter)
            {
                final ThymeleafParameter parameter = (ThymeleafParameter)frontEndParam;
                if (parameter.isValidationRequired())
                {
                    required = true;
                    break;
                }
            }
        }
        return required;
    }

    /**
     * @return popup
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#isPopup()
     */
    protected boolean handleIsPopup()
    {
        boolean popup = Objects.toString(this.findTaggedValue(ThymeleafProfile.TAGGEDVALUE_ACTION_TYPE)).equalsIgnoreCase(
            ThymeleafGlobals.ACTION_TYPE_POPUP);
        return popup;
    }

    /**
     * @return dialog
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#isDialog()
     */
    protected boolean handleIsDialog()
    {
        return this.isPopup();
        //currently only popup dialogs are supported and all dialog = popup.
        //It should change when Thymeleaf
        //starts supporting some conversation scope, or the thymeleaf cartridge supports
        //some extension that adds this kind of feature
        
//        boolean dialog = Objects.toString(this.findTaggedValue(ThymeleafProfile.TAGGEDVALUE_ACTION_TYPE)).equalsIgnoreCase(
//            ThymeleafGlobals.ACTION_TYPE_DIALOG);
//        return dialog;
    }

    /**
     * @return resetRequired
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#isFormResetRequired()
     */
    protected boolean handleIsFormResetRequired()
    {
        boolean resetRequired = this.isFormReset();
        if (!resetRequired)
        {
            for (final FrontEndParameter feParameter : this.getParameters())
            {
                if (feParameter instanceof ThymeleafParameter)
                {
                    final ThymeleafParameter parameter = (ThymeleafParameter)feParameter;
                    resetRequired = parameter.isReset();
                    if (resetRequired)
                    {
                        break;
                    }
                }
            }
        }
        return resetRequired;
    }

    //TODO remove after 3.4 release
    /**
     * Hack to keep the compatibility with Andromda 3.4-SNAPSHOT
     * @return getSource() instanceof FrontEndView
     */
    public FrontEndView getInput()
    {
        FrontEndView input = null;
        final ModelElementFacade source = this.getSource();
        if (source instanceof FrontEndView)
        {
            input = (FrontEndView)source;
        }
        return input;
    }

    /**
     * @return formSerialVersionUID
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getFormSerialVersionUID()
     */
    protected String handleGetFormSerialVersionUID()
    {
        final StringBuilder buffer = new StringBuilder();

        buffer.append(this.getName());

        final ModelElementFacade input = (ModelElementFacade)this.getInput();
        buffer.append(input != null ? input.getName() : "");

        final ModelElementFacade guard = this.getGuard();
        buffer.append(guard != null ? guard.getName() : "");

        final ModelElementFacade effect = this.getEffect();
        buffer.append(effect != null ? effect.getName() : "");

        final ModelElementFacade decisionsTrigger = this.getDecisionTrigger();
        buffer.append(decisionsTrigger != null ? decisionsTrigger.getName() : "");

        buffer.append(StringUtils.trimToEmpty(this.getActionClassName()));

        for (final FrontEndParameter parameter : this.getParameters())
        {
            buffer.append(parameter.getName());
        }

        for (final FrontEndForward forward : this.getActionForwards())
        {
            buffer.append(forward.getName());
        }

        for (final FrontEndAction action : this.getActions())
        {
            buffer.append(action.getName());
        }

        for (final FrontEndActionState state : this.getActionStates())
        {
            buffer.append(state.getName());
        }
        final String signature = buffer.toString();

        String serialVersionUID = String.valueOf(0L);
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] hashBytes = md.digest(signature.getBytes());

            long hash = 0;
            for (int ctr = Math.min(
                        hashBytes.length,
                        8) - 1; ctr >= 0; ctr--)
            {
                hash = (hash << 8) | (hashBytes[ctr] & 0xFF);
            }
            serialVersionUID = String.valueOf(hash);
        }
        catch (final NoSuchAlgorithmException exception)
        {
            final String message = "Error performing ThymeleafAction.getFormSerialVersionUID";
            LOGGER.error(
                message,
                exception);
        }
        return serialVersionUID;
    }

    /**
     * @return findTaggedValue(ThymeleafProfile.TAGGEDVALUE_ACTION_FORM_RESET)
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#isFormResetRequired()
     */
    protected boolean handleIsFormReset()
    {
        return Boolean.valueOf(Objects.toString(this.findTaggedValue(
            ThymeleafProfile.TAGGEDVALUE_ACTION_FORM_RESET), "")).booleanValue();
    }

    /**
     * @return "get" + StringUtils.capitalize(this.getFormBeanName(false)) + "()"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getFormImplementationGetter()
     */
    protected String handleGetFormImplementationGetter()
    {
        return "get" + StringUtils.capitalize(this.getFormBeanName(false)) + "()";
    }

    /**
     * @return getTarget() instanceof ThymeleafFinalState
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#isFinalStateTarget()
     */
    protected boolean handleIsFinalStateTarget()
    {
        return this.getTarget() instanceof ThymeleafFinalState;
    }

    /**
     * @return getName()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getFromOutcome()
     */
    protected String handleGetFromOutcome()
    {
        return this.getName();
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafActionLogic#handleIsSuccessMessagesPresent()
     */
    protected boolean handleIsSuccessMessagesPresent()
    {
        return !this.getSuccessMessages().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafActionLogic#handleIsWarningMessagesPresent()
     */
    protected boolean handleIsWarningMessagesPresent()
    {
        return !this.getWarningMessages().isEmpty();
    }

    /**
     * Collects specific messages in a map.
     *
     * @param taggedValue the tagged value from which to read the message
     * @return maps message keys to message values, but only those that match the arguments
     *         will have been recorded
     */
    private Map<String, String> getMessages(String taggedValue)
    {
        Map<String, String> messages;

        final Collection taggedValues = this.findTaggedValues(taggedValue);
        if (taggedValues.isEmpty())
        {
            messages = Collections.EMPTY_MAP;
        }
        else
        {
            messages = new LinkedHashMap<String, String>(); // we want to keep the order
            for (final Object tag : taggedValues)
            {
                final String value = (String)tag;
                messages.put(StringUtilsHelper.toResourceMessageKey(value), value);
            }
        }

        return messages;
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafActionLogic#handleGetSuccessMessages()
     */
    protected Map<String, String> handleGetSuccessMessages()
    {
        return this.getMessages(ThymeleafProfile.TAGGEDVALUE_ACTION_SUCCESS_MESSAGE);
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafActionLogic#handleGetWarningMessages()
     */
    protected Map<String, String> handleGetWarningMessages()
    {
        return this.getMessages(ThymeleafProfile.TAGGEDVALUE_ACTION_WARNING_MESSAGE);
    }

    /**
     * @return needsFileUpload
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#isNeedsFileUpload()
     */
    protected boolean handleIsNeedsFileUpload()
    {
        if(this.getParameters().size() == 0)
        {
            return false;
        }

        for (final FrontEndParameter feParameter : this.getParameters())
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
                        if(((ThymeleafAttribute)attributes.next()).isInputFile())
                            return true;
                }
            }
        }
        return false;
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafAction#getTriggerMethodName
     */
    @Override
    protected String handleGetTriggerMethodName()
    {
        final StringBuilder methodName = new StringBuilder();
        if (this.isExitingInitialState())
        {
            final ThymeleafUseCase useCase = (ThymeleafUseCase)this.getUseCase();
            methodName.append(StringUtilsHelper.lowerCamelCaseName(useCase.getName())+"_started");
        }
        else
        {
            methodName.append(StringUtilsHelper.lowerCamelCaseName(this.getSource().getName()));
            methodName.append('_');
            final EventFacade trigger = this.getTrigger();
            final String suffix = trigger == null ? this.getTarget().getName() : trigger.getName();
            methodName.append(StringUtilsHelper.lowerCamelCaseName(suffix));
        }
        return "_"+methodName.toString();
    }

    @Override
    protected String handleGetRestFormParams() {
        // TODO Auto-generated method stub

        if(getFormFields() == null || getFormFields().size() == 0)
            return "";

        StringBuilder builder = new StringBuilder();

        for(FrontEndParameter p : getFormFields()) {
            ThymeleafParameter param = (ThymeleafParameter)p;
            if(param.isActionParameter()) {
                if(builder.length() > 0) {
                    builder.append(", ");
                }

                if(param.isComplex()) {
                    builder.append("@thymeleaf.ws.rs.BeanParam ");
                } else {
                    builder.append("@thymeleaf.ws.rs.FormParam(\"");
                    builder.append(param.getName());
                    builder.append("\")");
                }
                builder.append(param.getType().getFullyQualifiedName());
                builder.append(" ");
                builder.append(param.getName());
            }
        }

        return builder.toString();

    }
}
