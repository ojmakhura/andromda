package org.andromda.cartridges.jsf.metafacades;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.andromda.cartridges.jsf.JSFGlobals;
import org.andromda.cartridges.jsf.JSFProfile;
import org.andromda.cartridges.jsf.JSFUtils;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFAction.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFAction
 */
public class JSFActionLogicImpl
    extends JSFActionLogic
{
    public JSFActionLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(JSFActionLogicImpl.class);

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getFormBeanName()
     */
    protected java.lang.String handleGetFormBeanName()
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
        final String pattern = ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.FORM_BEAN_PATTERN));
        final ModelElementFacade useCase = this.getUseCase();
        final String useCaseName = withUseCaseName && useCase != null ? StringUtilsHelper.lowerCamelCaseName(useCase.getName()) : "";
        final String formBeanName = pattern.replaceFirst("\\{0\\}", useCaseName); 
        final String triggerName = !pattern.equals(formBeanName) ? StringUtils.capitalize(this.getTriggerName()) : this.getTriggerName();
        return formBeanName.replaceFirst(
            "\\{1\\}",
            triggerName);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    public String getName()
    {
        return JSFUtils.toWebResourceName(this.getUseCase().getName() + "-" + super.getName());
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getTriggerName()
     */
    protected String handleGetTriggerName()
    {
        String name = null;
        if (this.isExitingInitialState())
        {
            final JSFUseCase useCase = (JSFUseCase)this.getUseCase();
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getFormImplementationName()
     */
    protected java.lang.String handleGetFormImplementationName()
    {
        final String pattern =
            ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.FORM_IMPLEMENTATION_PATTERN));
        return pattern.replaceFirst(
            "\\{0\\}",
            StringUtils.capitalize(this.getTriggerName()));
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getFullyQualifiedFormImplementationName()
     */
    protected boolean handleIsTableAction()
    {
        return JSFGlobals.ACTION_TYPE_TABLE.equals(this.findTaggedValue(JSFProfile.TAGGEDVALUE_ACTION_TYPE));
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getFullyQualifiedFormImplementationName()
     */
    protected java.lang.String handleGetFullyQualifiedFormImplementationName()
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getFullyQualifiedFormImplementationPath()
     */
    protected java.lang.String handleGetFullyQualifiedFormImplementationPath()
    {
        return this.getFullyQualifiedFormImplementationName().replace(
            '.',
            '/');
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getFullyQualifiedFormImplementationPath()
     */
    protected String handleGetFormScope()
    {
        String scope = ObjectUtils.toString(this.findTaggedValue(JSFProfile.TAGGEDVALUE_ACTION_FORM_SCOPE));
        if (StringUtils.isEmpty(scope))
        {
            scope = ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.FORM_SCOPE));
        }
        return scope;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getFormImplementationInterfaceList()
     */
    protected String handleGetFormImplementationInterfaceList()
    {
        final List deferredOperations = this.getDeferredOperations();
        for (final Iterator iterator = deferredOperations.iterator(); iterator.hasNext();)
        {
            // - remove any forms that don't have arguments
            final JSFControllerOperation operation = (JSFControllerOperation)iterator.next();
            if (operation.getArguments().isEmpty())
            {
                iterator.remove();
            }
        }
        final StringBuilder list = new StringBuilder();
        for (final Iterator iterator = deferredOperations.iterator(); iterator.hasNext();)
        {
            final JSFControllerOperation operation = (JSFControllerOperation)iterator.next();
            list.append(operation.getFormName());
            if (iterator.hasNext())
            {
                list.append(", ");
            }
        }
        return list.toString();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFActionLogic#handleGetPath()
     */
    protected String handleGetPath()
    {
        String path = this.getPathRoot() + '/' + JSFUtils.toWebResourceName(this.getTriggerName());
        if (this.isExitingInitialState())
        {
            final JSFUseCase useCase = (JSFUseCase)this.getUseCase();
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getPathRoot()
     */
    protected String handleGetPathRoot()
    {
        final StringBuilder pathRoot = new StringBuilder();
        final JSFUseCase useCase = (JSFUseCase)this.getUseCase();
        if (useCase != null)
        {
            pathRoot.append(useCase.getPathRoot());
        }
        return pathRoot.toString();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        String messageKey = null;

        final Object trigger = this.getTrigger();
        if (trigger instanceof JSFEvent)
        {
            final JSFEvent actionTrigger = (JSFEvent)trigger;
            if (actionTrigger != null)
            {
                messageKey = actionTrigger.getMessageKey();
            }
        }
        return messageKey;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getDocumentationKey()
     */
    protected String handleGetDocumentationKey()
    {
        final Object trigger = this.getTrigger();
        JSFEvent event = null;
        if (trigger instanceof JSFEvent)
        {
            event = (JSFEvent)trigger;
        }
        return (event == null ? this.getMessageKey() + ".is.an.action.without.trigger" : event.getMessageKey()) +
        '.' + JSFGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getDocumentationValue()
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getViewFragmentPath()
     */
    protected String handleGetViewFragmentPath()
    {
        return '/' + this.getPackageName().replace(
            '.',
            '/') + '/' + JSFUtils.toWebResourceName(this.getTriggerName());
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getTableLinkName()
     */
    protected String handleGetTableLinkName()
    {
        String tableLink = null;

        final Object value = findTaggedValue(JSFProfile.TAGGEDVALUE_ACTION_TABLELINK);
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getTableLinkColumnName()
     */
    protected String handleGetTableLinkColumnName()
    {
        String tableLink = null;
        final Object value = findTaggedValue(JSFProfile.TAGGEDVALUE_ACTION_TABLELINK);
        if (value != null)
        {
            tableLink = StringUtils.trimToNull(value.toString());

            if (tableLink != null)
            {
                final int columnOffset = tableLink.indexOf('.');
                tableLink =
                    (columnOffset == -1 || columnOffset == tableLink.length() - 1) ? null
                                                                                   : tableLink.substring(
                        columnOffset + 1);
            }
        }
        return tableLink;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#isTableLink()
     */
    protected Object handleGetTableLinkParameter()
    {
        JSFParameter tableLinkParameter = null;
        final String tableLinkName = this.getTableLinkName();
        if (tableLinkName != null)
        {
            final JSFView view = (JSFView)this.getInput();
            if (view != null)
            {
                final List tables = view.getTables();
                for (int ctr = 0; ctr < tables.size() && tableLinkParameter == null; ctr++)
                {
                    final Object object = tables.get(ctr);
                    if (object instanceof JSFParameter)
                    {
                        final JSFParameter table = (JSFParameter)object;
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#isTableLink()
     */
    protected boolean handleIsTableLink()
    {
        return this.getTableLinkParameter() != null;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#isHyperlink()
     */
    protected boolean handleIsHyperlink()
    {
        final Object value = findTaggedValue(JSFProfile.TAGGEDVALUE_ACTION_TYPE);
        return JSFGlobals.ACTION_TYPE_HYPERLINK.equalsIgnoreCase(value == null ? null : value.toString());
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getActionClassName()
     */
    protected String handleGetActionClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getTriggerName());
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getFullyQualifiedActionClassPath()
     */
    protected String handleGetFullyQualifiedActionClassPath()
    {
        return this.getFullyQualifiedActionClassName().replace(
            '.',
            '/') + ".java";
    }

    /**
     * Overriddent to provide the owning use case's package name.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName()
     */
    public String getPackageName()
    {
        final UseCaseFacade useCase = this.getUseCase();
        return useCase != null ? useCase.getPackageName() : "";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getControllerAction()
     */
    protected String handleGetControllerAction()
    {
        return this.getTriggerName();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getFullyQualifiedActionClassName()
     */
    protected String handleGetFullyQualifiedActionClassName()
    {
        final StringBuilder path = new StringBuilder();
        final JSFUseCase useCase = (JSFUseCase)this.getUseCase();
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
     * @see org.andromda.cartridges.struts.metafacades.JSFAction#isResettable()
     */
    protected boolean handleIsResettable()
    {
        final Object value = findTaggedValue(JSFProfile.TAGGEDVALUE_ACTION_RESETTABLE);
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getOtherUseCaseFormActions()
     */
    protected List handleGetOtherUseCaseFormActions()
    {
        final List otherActions = new ArrayList(this.getUseCase().getActions());
        for (final Iterator iterator = otherActions.iterator(); iterator.hasNext();)
        {
            final FrontEndAction action = (FrontEndAction)iterator.next();

            // - remove this action and any forms that don't have form fields
            if (action.equals(this.THIS()) || action.getFormFields().isEmpty())
            {
                iterator.remove();
            }
        }
        return otherActions;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getFormKey()
     */
    protected String handleGetFormKey()
    {
        final Object formKeyValue = this.findTaggedValue(JSFProfile.TAGGEDVALUE_ACTION_FORM_KEY);
        return formKeyValue == null ? ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.ACTION_FORM_KEY))
                                    : String.valueOf(formKeyValue);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#handleGetHiddenParameters()
     */
    protected List handleGetHiddenParameters()
    {
        final List hiddenParameters = new ArrayList(this.getParameters());
        CollectionUtils.filter(
            hiddenParameters,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    boolean valid = false;
                    if (object instanceof JSFParameter)
                    {
                        final JSFParameter parameter = (JSFParameter)object;
                        valid = parameter.isInputHidden();
                        if (!valid)
                        {
                            for (final Iterator iterator = parameter.getAttributes().iterator(); iterator.hasNext();)
                            {
                                JSFAttribute attribute = (JSFAttribute)iterator.next();
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#handleGetHiddenParameters()
     */
    protected boolean handleIsValidationRequired()
    {
        boolean required = false;
        final Collection actionParameters = this.getParameters();
        for (final Iterator iterator = actionParameters.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof JSFParameter)
            {
                final JSFParameter parameter = (JSFParameter)object;
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#isPopup()
     */
    protected boolean handleIsPopup()
    {
        boolean popup = ObjectUtils.toString(this.findTaggedValue(JSFProfile.TAGGEDVALUE_ACTION_TYPE)).equalsIgnoreCase(
            JSFGlobals.VIEW_TYPE_POPUP);
        if (!popup)
        {
            for (final Iterator iterator = this.getTargetViews().iterator(); iterator.hasNext();)
            {
                final JSFView view = (JSFView)iterator.next();
                popup = view.isPopup();
                if (!popup)
                {
                    break;
                }
            }
        }
        return popup;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#isFormResetRequired()
     */
    protected boolean handleIsFormResetRequired()
    {
        boolean resetRequired = this.isFormReset();
        if (!resetRequired)
        {
            for (final Iterator iterator = this.getParameters().iterator(); iterator.hasNext();)
            {
                final Object object = iterator.next();
                if (object instanceof JSFParameter)
                {
                    final JSFParameter parameter = (JSFParameter)object;
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getFormSerialVersionUID()
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
        
        for (final Iterator iterator = this.getParameters().iterator(); iterator.hasNext();)
        {
            final ModelElementFacade parameter = (ModelElementFacade)iterator.next();
            buffer.append(parameter.getName());
        }
        
        for (final Iterator iterator = this.getActionForwards().iterator(); iterator.hasNext();)
        {
            final ModelElementFacade forward = (ModelElementFacade)iterator.next();
            buffer.append(forward.getName());
        }
        
        for (final Iterator iterator = this.getActions().iterator(); iterator.hasNext();)
        {
            final ModelElementFacade action = (ModelElementFacade)iterator.next();
            buffer.append(action.getName());
        }
        
        for (final Iterator iterator = this.getActionStates().iterator(); iterator.hasNext();)
        {
            final ModelElementFacade state = (ModelElementFacade)iterator.next();
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
            final String message = "Error performing JSFAction.getFormSerialVersionUID";
            logger.error(
                message,
                exception);
        }
        return serialVersionUID;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#isFormRequired()
     */
    protected boolean handleIsFormReset()
    {
        return Boolean.valueOf(ObjectUtils.toString(this.findTaggedValue(JSFProfile.TAGGEDVALUE_ACTION_FORM_RESET))).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getFormImplementationGetter()
     */
    protected String handleGetFormImplementationGetter()
    {
        return "get" + StringUtils.capitalize(this.getFormBeanName(false)) + "()";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#isFinalStateTarget()
     */
    protected boolean handleIsFinalStateTarget()
    {
        return this.getTarget() instanceof JSFFinalState;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#getFromOutcome()
     */
    protected String handleGetFromOutcome()
    {
        return this.getName();
    }
    
    protected boolean handleIsSuccessMessagesPresent()
    {
        return !this.getSuccessMessages().isEmpty();
    }

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
    private Map getMessages(String taggedValue)
    {
        Map messages;

        final Collection taggedValues = this.findTaggedValues(taggedValue);
        if (taggedValues.isEmpty())
        {
            messages = Collections.EMPTY_MAP;
        }
        else
        {
            messages = new LinkedHashMap(); // we want to keep the order

            for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
            {
                final String value = (String)iterator.next();
                messages.put(StringUtilsHelper.toResourceMessageKey(value), value);
            }
        }

        return messages;
    }

    protected Map handleGetSuccessMessages()
    {
        return this.getMessages(JSFProfile.TAGGEDVALUE_ACTION_SUCCESS_MESSAGE);
    }

    protected Map handleGetWarningMessages()
    {
        return this.getMessages(JSFProfile.TAGGEDVALUE_ACTION_WARNING_MESSAGE);
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAction#isNeedsFileUpload()
     */
    protected boolean handleIsNeedsFileUpload()
    {
        if(this.getParameters().size() == 0)
            return false;
            
        for (final Iterator iterator = this.getParameters().iterator(); iterator.hasNext();)
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
