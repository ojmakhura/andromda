package org.andromda.cartridges.bpm4jsf.metafacades;

import java.util.Iterator;
import java.util.List;

import org.andromda.cartridges.bpm4jsf.BPM4JSFGlobals;
import org.andromda.cartridges.bpm4jsf.BPM4JSFProfile;
import org.andromda.cartridges.bpm4jsf.BPM4JSFUtils;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndFinalState;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;



/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4jsf.metafacades.JSFAction.
 *
 * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction
 */
public class JSFActionLogicImpl
    extends JSFActionLogic
{

    public JSFActionLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getFormBeanName()
     */
    protected java.lang.String handleGetFormBeanName()
    {
        final String pattern = ObjectUtils.toString(this.getConfiguredProperty(BPM4JSFGlobals.FORM_BEAN_PATTERN));
        return pattern.replaceFirst("\\{0\\}", this.getTriggerName());            
    }   
    
    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    public String getName()
    {
        return BPM4JSFUtils.toWebResourceName(super.getName());
    }
 
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getTriggerName()
     */
    protected String handleGetTriggerName()
    {
        String name = null;
        if (this.isExitingInitialState())
        {
            final UseCaseFacade useCase = this.getUseCase();
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
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getFormImplementationName()
     */
    protected java.lang.String handleGetFormImplementationName()
    {
        final String pattern = ObjectUtils.toString(this.getConfiguredProperty(BPM4JSFGlobals.FORM_IMPLEMENTATION_PATTERN));
        return pattern.replaceFirst("\\{0\\}", StringUtils.capitalize(this.getTriggerName()));
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getFullyQualifiedFormImplementationName()
     */
    protected java.lang.String handleGetFullyQualifiedFormImplementationName()
    {
        final StringBuffer fullyQualifiedName = new StringBuffer();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append(packageName + '.');
        }
        return fullyQualifiedName.append(this.getFormImplementationName()).toString();
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getFullyQualifiedFormImplementationPath()
     */
    protected java.lang.String handleGetFullyQualifiedFormImplementationPath()
    {
        return this.getFullyQualifiedFormImplementationName().replace('.', '/');
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getFullyQualifiedFormImplementationPath()
     */
    protected String handleGetFormScope()
    {
        String scope = ObjectUtils.toString(this.findTaggedValue(BPM4JSFProfile.TAGGEDVALUE_ACTION_FORM_SCOPE));
        if (StringUtils.isEmpty(scope))
        {
            scope = ObjectUtils.toString(this.getConfiguredProperty(BPM4JSFGlobals.FORM_SCOPE));
        }
        return scope;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getFormImplementationInterfaceList()
     */
    protected String handleGetFormImplementationInterfaceList()
    {
        final StringBuffer list = new StringBuffer();
        for (final Iterator iterator = this.getDeferredOperations().iterator(); iterator.hasNext();)
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
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFActionLogic#handleGetPath()
     */
    protected String handleGetPath()
    {
        String path = null;
        final Object target = this.getTarget();
        if (target instanceof FrontEndFinalState)
        {
            final FrontEndFinalState finalState = (FrontEndFinalState)target;
            final JSFUseCase useCase = (JSFUseCase)finalState.getTargetUseCase();
            if (useCase != null)
            {
                path = useCase.getPath();
            }
        }
        return path;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getPathRoot()
     */
    protected String handleGetPathRoot()
    {
        final StringBuffer pathRoot = new StringBuffer();
        final JSFUseCase useCase = (JSFUseCase)this.getUseCase();
        if (useCase != null)
        {
            pathRoot.append(useCase.getPathRoot());
        }
        return pathRoot.toString();
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getPathRoot()
     */
    protected String handleGetMessageKey()
    {
        String messageKey = null;

        final JSFEvent actionTrigger = (JSFEvent)this.getTrigger();
        if (actionTrigger != null)
        {
            messageKey = actionTrigger.getMessageKey();
        }

        return messageKey;
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getDocumentationKey()
     */
    protected String handleGetDocumentationKey()
    {
        final JSFEvent trigger = (JSFEvent)this.getTrigger();
        return (trigger == null
            ? this.getMessageKey() + ".is.an.action.without.trigger"
            : trigger.getMessageKey()) + '.' + BPM4JSFGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getDocumentationValue()
     */
    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        return value == null ? "" : value;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getViewFragmentPath()
     */
    protected String handleGetViewFragmentPath()
    {
        return '/' + this.getPackageName().replace('.', '/') + '/' + BPM4JSFUtils.toWebResourceName(this.getTriggerName());
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getTableLinkName()
     */
    protected String handleGetTableLinkName()
    {
        String tableLink = null;
        
        final Object value = findTaggedValue(BPM4JSFProfile.TAGGEDVALUE_ACTION_TABLELINK);
        if (value != null)
        {
            tableLink = StringUtils.trimToNull(value.toString());

            if (tableLink != null)
            {
                final int columnOffset = tableLink.indexOf('.');
                tableLink = columnOffset == -1 ? tableLink : tableLink.substring(0, columnOffset);
            }
        }

        return tableLink;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#getTableLinkColumnName()
     */
    protected String handleGetTableLinkColumnName()
    {
        String tableLink = null;

        final Object value = findTaggedValue(BPM4JSFProfile.TAGGEDVALUE_ACTION_TABLELINK);
        if (value != null)
        {
            tableLink = StringUtils.trimToNull(value.toString());

            if (tableLink != null)
            {
                final int columnOffset = tableLink.indexOf('.');
                tableLink = (columnOffset == -1 || columnOffset == tableLink.length() - 1) ?
                    null : tableLink.substring(columnOffset + 1);
            }
        }

        return tableLink;
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#isTableLink()
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
                    final JSFParameter table = (JSFParameter)tables.get(ctr);
                    if (tableLinkName.equals(table.getName()))
                    {
                        tableLinkParameter = table;
                    }
                }
            }
        }
        return tableLinkParameter;
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#isTableLink()
     */
    protected boolean handleIsTableLink()
    {
        return this.getTableLinkParameter() != null;
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFAction#isHyperlink()
     */
    protected boolean handleIsHyperlink()
    {
        final Object value = findTaggedValue(BPM4JSFProfile.TAGGEDVALUE_ACTION_TYPE);
        return BPM4JSFGlobals.ACTION_TYPE_HYPERLINK.equalsIgnoreCase(
            value == null ? null : value.toString());
    }
}    