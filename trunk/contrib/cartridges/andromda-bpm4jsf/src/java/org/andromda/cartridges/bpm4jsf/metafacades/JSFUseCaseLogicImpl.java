package org.andromda.cartridges.bpm4jsf.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.andromda.cartridges.bpm4jsf.BPM4JSFGlobals;
import org.andromda.cartridges.bpm4jsf.BPM4JSFProfile;
import org.andromda.cartridges.bpm4jsf.BPM4JSFUtils;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndFinalState;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase.
 *
 * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase
 */
public class JSFUseCaseLogicImpl
    extends JSFUseCaseLogic
{
    public JSFUseCaseLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#getPath()
     */
    protected java.lang.String handleGetPath()
    {
        String actionPath = null;
        final FrontEndActivityGraph graph = this.getActivityGraph();
        if (graph != null)
        {
            final JSFAction action =  (JSFAction)graph.getInitialAction();
            if (action != null)
            {
                actionPath = action.getPath();
            }
        }
        return actionPath;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#getPathRoot()
     */
    protected String handleGetPathRoot()
    {
        final StringBuffer pathRoot = new StringBuffer("/");
        final String packagePath = this.getPackagePath();
        final String prefix = packagePath != null ? packagePath.trim() : "";
        pathRoot.append(prefix);
        return pathRoot.toString();
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#getPathRoot()
     */
    protected String handleGetForwardName()
    {
        return BPM4JSFUtils.toWebResourceName(this.getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#getTitleKey()
     */
    protected String handleGetTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(
            this.isNormalizeMessages() ? this.getTitleValue() : this.getName()) + '.' +
        BPM4JSFGlobals.TITLE_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#getTitleValue()
     */
    protected String handleGetTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * Indicates whether or not we should normalize messages.
     *
     * @return true/false
     */
    private final boolean isNormalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(BPM4JSFGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#getAllMessages()
     */
    protected Map handleGetAllMessages()
    {
        final boolean normalize = this.isNormalizeMessages();
        final Map messages = (normalize) ? (Map)new TreeMap() : (Map)new LinkedHashMap();

        // - only retrieve the messages for the entry use case (i.e. the use case 
        //   where the application begins)
        if (this.isEntryUseCase())
        {
            final List useCases = this.getAllUseCases();
            for (int ctr = 0; ctr < useCases.size(); ctr++)
            {
                // - usecase
                final JSFUseCase useCase = (JSFUseCase)useCases.get(ctr);
                messages.put(
                    useCase.getTitleKey(),
                    useCase.getTitleValue());

                final List pages = useCase.getViews();
                for (int ctr2 = 0; ctr2 < pages.size(); ctr2++)
                {
                    // - view
                    final JSFView view = (JSFView)pages.get(ctr2);
                    messages.put(
                        view.getTitleKey(),
                        view.getTitleValue());
                    messages.put(
                        view.getMessageKey(),
                        view.getMessageValue());
                    messages.put(
                        view.getDocumentationKey(),
                        view.getDocumentationValue());

                    final List pageVariables = view.getVariables();
                    for (int ctr3 = 0; ctr3 < pageVariables.size(); ctr3++)
                    {
                        // - page variables
                        final JSFParameter parameter = (JSFParameter)pageVariables.get(ctr3);

                        messages.put(
                            parameter.getMessageKey(),
                            parameter.getMessageValue());

                        // - table

                        if (parameter.isTable())
                        {
                            final Collection columnNames = parameter.getTableColumnNames();
                            for (final Iterator columnNameIterator = columnNames.iterator();
                                columnNameIterator.hasNext();)
                            {
                                final String columnName = (String)columnNameIterator.next();
                                messages.put(
                                    parameter.getTableColumnMessageKey(columnName),
                                    parameter.getTableColumnMessageValue(columnName));
                            }
                        }
                    }

                    final List actions = useCase.getActions();
                    for (int ctr3 = 0; ctr3 < actions.size(); ctr3++)
                    {
                        // - action
                        final JSFAction action = (JSFAction)actions.get(ctr3);

                        // - event/trigger
                        final JSFEvent event = (JSFEvent)action.getTrigger();
                        if (event != null)
                        {
                            // only add these when a trigger is present, otherwise it's no use having them
                            messages.put(
                                action.getDocumentationKey(),
                                action.getDocumentationValue());

                            // the regular trigger messages
                            messages.put(
                                event.getResetMessageKey(),
                                event.getResetMessageValue());

                            // this one is the same as doing: action.getMessageKey()
                            messages.put(
                                event.getMessageKey(),
                                event.getMessageValue());

                            // - IMAGE LINK

                            /*if (action.isImageLink())
                            {
                                messages.put(
                                    action.getImageMessageKey(),
                                    action.getImagePath());
                            }*/
                        }

                        // - forwards

                        /*final List transitions = action.getTransitions();
                        for (int l = 0; l < transitions.size(); l++)
                        {
                            final FrontEndForward forward = (FrontEndForward)transitions.get(l);
                            messages.putAll(forward.getSuccessMessages());
                            messages.putAll(forward.getWarningMessages());
                        }*/

                        // - action parameters
                        final List parameters = action.getParameters();
                        for (int l = 0; l < parameters.size(); l++)
                        {
                            final JSFParameter parameter = (JSFParameter)parameters.get(l);
                            messages.put(
                                parameter.getMessageKey(),
                                parameter.getMessageValue());
                            messages.put(
                                parameter.getDocumentationKey(),
                                parameter.getDocumentationValue());

                            /*if (parameter.getValidWhen() != null)
                            {
                                // this key needs to be fully qualified since the valid when value can be different
                                final String completeKeyPrefix =
                                    (normalize)
                                    ? useCase.getTitleKey() + '.' + view.getMessageKey() + '.' +
                                    action.getMessageKey() + '.' + parameter.getMessageKey() : parameter.getMessageKey();
                                messages.put(
                                    completeKeyPrefix + "_validwhen",
                                    "{0} is only valid when " + parameter.getValidWhen());
                            }*/
                            /*if (parameter.getOptionCount() > 0)
                            {
                                final List optionKeys = parameter.getOptionKeys();
                                final List optionValues = parameter.getOptionValues();

                                for (int m = 0; m < optionKeys.size(); m++)
                                {
                                    messages.put(
                                        optionKeys.get(m),
                                        optionValues.get(m));
                                    messages.put(
                                        optionKeys.get(m) + ".title",
                                        optionValues.get(m));
                                }
                            }*/
                        }

                        // - exception forwards

                        /*
                        final List exceptions = action.getActionExceptions();

                        if (normalize)
                        {
                            if (exceptions.isEmpty())
                            {
                                messages.put("exception.occurred", "{0}");
                            }
                            else
                            {
                                for (int l = 0; l < exceptions.size(); l++)
                                {
                                    final FrontEndExceptionHandler exception =
                                        (FrontEndExceptionHandler)exceptions.get(l);
                                    messages.put(action.getMessageKey() + '.' + exception.getExceptionKey(), "{0}");
                                }
                            }
                        }
                        else
                        {
                            if (exceptions.isEmpty())
                            {
                                if (!action.isUseCaseStart())
                                {
                                    messages.put(action.getMessageKey() + ".exception", "{0} (java.lang.Exception)");
                                }
                            }
                            else
                            {
                                for (int l = 0; l < exceptions.size(); l++)
                                {
                                    final FrontEndExceptionHandler exception =
                                        (FrontEndExceptionHandler)exceptions.get(l);

                                    // we construct the key using the action message too because the exception can
                                    // belong to more than one action (therefore it cannot return the correct value
                                    // in .getExceptionKey())
                                    messages.put(
                                        action.getMessageKey() + '.' + exception.getExceptionKey(),
                                        "{0} (" + exception.getExceptionType() + ")");
                                }
                            }
                        }*/
                    }
                }
            }
        }
        return messages;
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#getActionForwards()
     */
    protected List handleGetActionForwards()
    {
        final Set actionForwards = new LinkedHashSet();
        final List views = this.getViews();
        for (final Iterator iterator = views.iterator(); iterator.hasNext();)
        {
            final JSFView view = (JSFView)iterator.next();
            actionForwards.addAll(view.getActionForwards());
        }
        return new ArrayList(actionForwards);
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#getActionClassName()
     */
    protected String handleGetActionClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#getFullyQualifiedActionClassPath()
     */
    protected String handleGetFullyQualifiedActionClassPath()
    {
        return this.getFullyQualifiedActionClassName().replace('.', '/') + ".java";
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#getControllerAction()
     */
    protected String handleGetControllerAction()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#getFullyQualifiedActionClassName()
     */
    protected String handleGetFullyQualifiedActionClassName()
    {
        final StringBuffer path = new StringBuffer();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            path.append(packageName);
            path.append('.');
        }
        path.append(this.getActionClassName());
        return path.toString();
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#getFormKey()
     */
    protected String handleGetFormKey()
    {
        final Object formKeyValue = this.findTaggedValue(BPM4JSFProfile.TAGGEDVALUE_ACTION_FORM_KEY);
        return formKeyValue == null ? ObjectUtils.toString(this.getConfiguredProperty(BPM4JSFGlobals.ACTION_FORM_KEY))
                                    : String.valueOf(formKeyValue);
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#getInitialTargetPath()
     */
    protected String handleGetInitialTargetPath()
    {
        String path = null;
        final Object target = this.getInitialTarget();
        if (target instanceof JSFView)
        {
            path = ((JSFView)target).getPath();
        }
        else if (target instanceof JSFUseCase)
        {
            path = ((JSFUseCase)target).getPath();
        }
        return path;
    }
    
    /**
     * Gets the initial target when this use case is entered.
     * 
     * @return the initial target.
     */
    private final Object getInitialTarget()
    {
        Object initialTarget = null;
        final FrontEndActivityGraph graph = this.getActivityGraph();
        final FrontEndAction action = graph != null ? this.getActivityGraph().getInitialAction() : null;
        final Collection forwards = action != null ? action.getActionForwards() : null;
        if (forwards != null && !forwards.isEmpty())
        {
            final FrontEndForward forward = (FrontEndForward)forwards.iterator().next();
            final Object target = forward.getTarget();
            if (target instanceof FrontEndView)
            {
                initialTarget = target;
            }
            else if (target instanceof FrontEndFinalState)
            {
                final FrontEndFinalState finalState = (FrontEndFinalState)target;
                final FrontEndUseCase targetUseCase = finalState.getTargetUseCase();
                if (targetUseCase != null && !targetUseCase.equals(this.THIS()))
                {
                    initialTarget = targetUseCase;
                }
            }
        }
        return initialTarget;        
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#isValidationRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        boolean required = false;
        final Collection views = this.getViews();
        for (final Iterator iterator = views.iterator(); iterator.hasNext();)
        {
            final JSFView view = (JSFView)iterator.next();
            if (view.isValidationRequired())
            {
                required = true;
                break;
            }
        }
        return required;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#isInitialTargetView()
     */
    protected boolean handleIsInitialTargetView()
    {
        return this.getInitialTarget() instanceof JSFView;
    }    
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#isInitialTargetView()
     */
    protected boolean handleIsApplicationValidationRequired()
    {
        boolean required = false;
        final Collection useCases = this.getAllUseCases();
        for (final Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            final JSFUseCase useCase = (JSFUseCase)iterator.next();
            if (useCase.isValidationRequired())
            {
                required = true;
                break;
            }
        }
        return required;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFUseCase#isViewHasNameOfUseCase()
     */
    protected boolean handleIsViewHasNameOfUseCase()
    {
        boolean sameName = false;
        for (final Iterator iterator = this.getViews().iterator(); iterator.hasNext();)
        {
            final ModelElementFacade view = (ModelElementFacade)iterator.next();
            final String name = view.getName();
            if (name != null && name.equalsIgnoreCase(this.getName()))
            {
                sameName = true;
                break;
            }
        }
        return sameName;
    }
}