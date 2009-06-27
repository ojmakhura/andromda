package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsUtils;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.Role;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCase
 */
public class StrutsUseCaseLogicImpl
    extends StrutsUseCaseLogic
{
    public StrutsUseCaseLogicImpl(
        java.lang.Object metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    protected String handleGetTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(normalizeMessages() ? getTitleValue() : getName()) + ".title";
    }

    protected String handleGetTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    protected String handleGetOnlineHelpKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName()) + ".online.help";
    }

    protected String handleGetOnlineHelpValue()
    {
        final String crlf = "<br/>";
        final StringBuffer buffer = new StringBuffer();

        final String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        buffer.append((value == null) ? "No use-case documentation has been specified" : value);
        buffer.append(crlf);

        return StringUtilsHelper.toResourceMessage(buffer.toString());
    }

    protected String handleGetActionPath()
    {
        String actionPath = null;

        final StrutsActivityGraph graph = (StrutsActivityGraph)getActivityGraph();
        if (graph != null)
        {
            final StrutsAction action = graph.getFirstAction();
            if (action != null)
            {
                actionPath = action.getActionPath();
            }
        }
        return actionPath;
    }

    protected String handleGetActionPathRoot()
    {
        String actionPathRoot = null;

        final StrutsActivityGraph graph = (StrutsActivityGraph)getActivityGraph();
        if (graph != null)
        {
            final StrutsAction action = graph.getFirstAction();
            if (action != null)
            {
                actionPathRoot = action.getActionPathRoot();
            }
        }
        return actionPathRoot;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCase#isCyclic()
     */
    protected boolean handleIsCyclic()
    {
        boolean selfTargetting = false;
        final ActivityGraphFacade graph = getActivityGraph();
        if (graph != null)
        {
            final Collection finalStates = graph.getFinalStates();
            for (final Iterator finalStateIterator = finalStates.iterator();
                 finalStateIterator.hasNext() && !selfTargetting;)
            {
                final StrutsFinalState finalState = (StrutsFinalState)finalStateIterator.next();
                if (this.equals(finalState.getTargetUseCase()))
                {
                    selfTargetting = true;
                }
            }
        }
        return selfTargetting;
    }

    protected String handleGetActionRoles()
    {
        final Collection users = this.getRoles();
        final StringBuffer rolesBuffer = new StringBuffer();
        boolean first = true;
        for (final Iterator userIterator = users.iterator(); userIterator.hasNext();)
        {
            if (first)
            {
                first = false;
            }
            else
            {
                rolesBuffer.append(',');
            }
            final Role role = (Role)userIterator.next();
            rolesBuffer.append(role.getName());
        }
        return rolesBuffer.toString();
    }

    public Collection getOperations()
    {
        return Collections.EMPTY_LIST;
    }

    protected List handleGetPages()
    {
        return this.getViews();
    }

    protected List handleGetAllPages()
    {
        final List pagesList = new ArrayList();
        final Collection allActionStates = getModel().getAllActionStates();

        for (final Iterator actionStateIterator = allActionStates.iterator(); actionStateIterator.hasNext();)
        {
            final Object actionState = actionStateIterator.next();
            if (actionState instanceof StrutsJsp)
                pagesList.add(actionState);
        }
        return pagesList;
    }

    protected List handleGetFormFields()
    {
        final List formFields = new ArrayList(); // parameter names are supposed to be unique

        final Collection pages = getPages();
        for (final Iterator pageIterator = pages.iterator(); pageIterator.hasNext();)
        {
            final StrutsJsp jsp = (StrutsJsp)pageIterator.next();
            final Collection variables = jsp.getPageVariables();
            for (final Iterator variableIterator = variables.iterator(); variableIterator.hasNext();)
            {
                formFields.add(variableIterator.next());
            }
            final Collection parameters = jsp.getAllActionParameters();
            for (final Iterator parameterIterator = parameters.iterator(); parameterIterator.hasNext();)
            {
                formFields.add(parameterIterator.next());
            }
        }
        return formFields;
    }

    protected boolean handleIsValidationRequired()
    {
        final Collection allPages = this.getAllPages();
        for (final Iterator iterator = allPages.iterator(); iterator.hasNext();)
        {
            final StrutsJsp jsp = (StrutsJsp)iterator.next();
            if (jsp.isValidationRequired())
            {
                return true;
            }
        }
        return false;
    }

    protected boolean handleIsApplicationValidationRequired()
    {
        final Collection useCases = this.getAllUseCases();
        for (final Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            final StrutsUseCase useCase = (StrutsUseCase)iterator.next();
            if (useCase.isValidationRequired())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Overriden because StrutsAction does not extend FrontEndAction.
     *
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getActions()
     */
    public List getActions()
    {
        final Collection actions = new LinkedHashSet();

        final Collection pages = getPages();
        for (final Iterator pageIterator = pages.iterator(); pageIterator.hasNext();)
        {
            final StrutsJsp jsp = (StrutsJsp)pageIterator.next();
            actions.addAll(jsp.getActions());
        }

        final StrutsActivityGraph graph = (StrutsActivityGraph)getActivityGraph();
        if (graph != null)
        {
            final StrutsAction action = graph.getFirstAction();
            if (action != null) actions.add(action);
        }

        return new ArrayList(actions);
    }

    protected List handleGetPageVariables()
    {
        return this.getViewVariables();
    }

    protected boolean handleIsApplicationUseCase()
    {
        return this.isEntryUseCase();
    }

    protected String handleGetCssFileName()
    {
        return this.getPackagePath() + '/' + Bpm4StrutsUtils.toWebFileName(this.getName()) + ".css";
    }

    protected TreeNode handleGetApplicationHierarchyRoot()
    {
        final UseCaseNode root = new UseCaseNode(this);
        this.createHierarchy(root);
        return root;
    }

    protected TreeNode handleGetHierarchyRoot()
    {
        UseCaseNode hierarchy = null;

        final Collection allUseCases = this.getAllUseCases();
        for (final Iterator useCaseIterator = allUseCases.iterator(); useCaseIterator.hasNext();)
        {
            final StrutsUseCase useCase = (StrutsUseCase)useCaseIterator.next();
            if (useCase.isApplicationUseCase())
            {
                final UseCaseNode root = (UseCaseNode)useCase.getApplicationHierarchyRoot();
                hierarchy = this.findNode(root, this);
            }
        }
        return hierarchy;
    }

    /**
     * Recursively creates a hierarchy of use-cases, starting with the argument use-case as the root. This is primarily
     * meant to build a set of menu items.
     */
    private void createHierarchy(UseCaseNode root)
    {
        final StrutsUseCase useCase = (StrutsUseCase)root.getUserObject();

        final FrontEndActivityGraph graph = useCase.getActivityGraph();
        if (graph != null)
        {
            final Collection finalStates = graph.getFinalStates();
            for (final Iterator finalStateIterator = finalStates.iterator(); finalStateIterator.hasNext();)
            {
                final StrutsFinalState finalState = (StrutsFinalState)finalStateIterator.next();
                final StrutsUseCase targetUseCase = (StrutsUseCase)finalState.getTargetUseCase();
                if (targetUseCase != null)
                {
                    final UseCaseNode useCaseNode = new UseCaseNode(targetUseCase);
                    if (!isNodeAncestor(root, useCaseNode))
                    {
                        root.add(useCaseNode);
                        createHierarchy(useCaseNode);
                    }
                }
            }
        }
    }

    /**
     * <code>true</code> if the argument ancestor node is actually an ancestor of the first node.
     * <p>
     * <em>Note: DefaultMutableTreeNode's isNodeAncestor does not work because of its specific impl.</em>
     */
    private boolean isNodeAncestor(
        UseCaseNode node,
        UseCaseNode ancestorNode)
    {
        boolean ancestor = false;

        if (node.getUseCase().equals(ancestorNode.getUseCase()))
        {
            ancestor = true;
        }
        while (!ancestor && node.getParent() != null)
        {
            node = (UseCaseNode)node.getParent();
            if (this.isNodeAncestor(node, ancestorNode))
            {
                ancestor = true;
            }
        }
        return ancestor;
    }

    /**
     * Given a root use-case, finds the node in the hierarchy that represent the argument StrutsUseCase node.
     */
    private UseCaseNode findNode(
        UseCaseNode root,
        StrutsUseCase useCase)
    {
        UseCaseNode useCaseNode = null;

        final List nodeList = Collections.list(root.breadthFirstEnumeration());
        for (final Iterator nodeIterator = nodeList.iterator(); nodeIterator.hasNext() && useCaseNode == null;)
        {
            UseCaseNode node = (UseCaseNode)nodeIterator.next();
            if (useCase.equals(node.getUserObject()))
            {
                useCaseNode = node;
            }
        }
        return useCaseNode;
    }

    public final static class UseCaseNode
        extends DefaultMutableTreeNode
    {
        public UseCaseNode(StrutsUseCase useCase)
        {
            super(useCase);
        }

        public StrutsUseCase getUseCase()
        {
            return (StrutsUseCase)getUserObject();
        }
    }

    private boolean normalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    protected Map handleGetAllMessages()
    {
        final boolean normalize = this.normalizeMessages();
        final Map messages = (normalize) ? (Map)new TreeMap() : (Map)new LinkedHashMap();

        if (this.isApplicationUseCase())
        {
            final List useCases = this.getAllUseCases();
            for (int i = 0; i < useCases.size(); i++)
            {
                // USECASE
                final StrutsUseCase useCase = (StrutsUseCase)useCases.get(i);
                messages.put(useCase.getTitleKey(), useCase.getTitleValue());
                messages.put(useCase.getOnlineHelpKey(), useCase.getOnlineHelpValue());

                final List actions = useCase.getActions();
                for (int j = 0; j < actions.size(); j++)
                {
                    final StrutsAction action = (StrutsAction)actions.get(j);

                    // FORWARDS
                    final List transitions = action.getTransitions();
                    for (int l = 0; l < transitions.size(); l++)
                    {
                        final StrutsForward forward = (StrutsForward)transitions.get(l);
                        messages.putAll(forward.getSuccessMessages());
                        messages.putAll(forward.getWarningMessages());
                    }

                    // EXCEPTION FORWARDS
                    final List exceptions = action.getActionExceptions();

                    if (normalize)
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
                                final StrutsExceptionHandler exception = (StrutsExceptionHandler)exceptions.get(l);
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
                                final StrutsExceptionHandler exception = (StrutsExceptionHandler)exceptions.get(l);
                                // we construct the key using the action message too because the exception can
                                // belong to more than one action (therefore it cannot return the correct value
                                // in .getExceptionKey())
                                messages.put(action.getMessageKey() + '.' + exception.getExceptionKey(),
                                    "{0} (" + exception.getExceptionType() + ")");
                            }
                        }
                    }

                    // TRIGGER
                    final StrutsTrigger trigger = action.getActionTrigger();
                    if (trigger != null)
                    {
                        // only add these when a trigger is present, otherwise it's no use having them
                        messages.put(action.getOnlineHelpKey(), action.getOnlineHelpValue());
                        messages.put(action.getDocumentationKey(), action.getDocumentationValue());

                        // the regular trigger messages
                        messages.put(trigger.getTitleKey(), trigger.getTitleValue());
                        messages.put(trigger.getNotAllowedTitleKey(), trigger.getNotAllowedTitleValue());
                        messages.put(trigger.getResetMessageKey(), trigger.getResetMessageValue());
                        messages.put(trigger.getResetNotAllowedTitleKey(), trigger.getResetNotAllowedTitleValue());
                        messages.put(trigger.getResetTitleKey(), trigger.getResetTitleValue());
                        // this one is the same as doing: action.getMessageKey()
                        messages.put(trigger.getTriggerKey(), trigger.getTriggerValue());

                        // IMAGE LINK
                        if (action.isImageLink())
                        {
                            messages.put(action.getImageMessageKey(), action.getImagePath());
                        }
                    }
                }

                final List pages = useCase.getPages();
                for (int j = 0; j < pages.size(); j++)
                {
                    // PAGE
                    final StrutsJsp page = (StrutsJsp)pages.get(j);
                    messages.put(page.getTitleKey(), page.getTitleValue());
                    messages.put(page.getMessageKey(), page.getMessageValue());
                    messages.put(page.getOnlineHelpKey(), page.getOnlineHelpValue());
                    messages.put(page.getDocumentationKey(), page.getDocumentationValue());

                    final List pageVariables = page.getPageVariables();
                    for (int k = 0; k < pageVariables.size(); k++)
                    {
                        // PAGE-VARIABLE
                        final StrutsParameter parameter = (StrutsParameter)pageVariables.get(k);

                        messages.put(parameter.getMessageKey(), parameter.getMessageValue());
/*
                        if (normalize)
                        {
                            // the next line is in comment because it's not actually being used
                            //messages.put(parameter.getTitleKey(), parameter.getTitleValue());
                            messages.put(parameter.getMessageKey(), parameter.getMessageValue());
                        }
                        else
                        {
                            // the next line is in comment because it's not actually being used
                            //messages.put(page.getTitleKey() + '.' + parameter.getTitleKey(), parameter.getTitleValue());
                            messages.put(page.getTitleKey() + '.' + parameter.getMessageKey(),
                                    parameter.getMessageValue());
                        }
*/

                        // TABLE
                        if (parameter.isTable())
                        {
                            final Collection columnNames = parameter.getTableColumnNames();
                            for (final Iterator columnNameIterator = columnNames.iterator();
                                 columnNameIterator.hasNext();)
                            {
                                final String columnName = (String)columnNameIterator.next();
                                messages.put(parameter.getTableColumnMessageKey(columnName),
                                    parameter.getTableColumnMessageValue(columnName));
                            }
                        }
                    }

                    for (int k = 0; k < actions.size(); k++)
                    {
                        // ACTION
                        final StrutsAction action = (StrutsAction)actions.get(k);

                        // ACTION PARAMETERS
                        final List parameters = action.getActionParameters();
                        for (int l = 0; l < parameters.size(); l++)
                        {
                            final StrutsParameter parameter = (StrutsParameter)parameters.get(l);
                            messages.put(parameter.getMessageKey(), parameter.getMessageValue());
                            messages.put(parameter.getOnlineHelpKey(), parameter.getOnlineHelpValue());
                            messages.put(parameter.getDocumentationKey(), parameter.getDocumentationValue());
                            messages.put(parameter.getTitleKey(), parameter.getTitleValue());

                            if (parameter.getValidWhen() != null)
                            {
                                // this key needs to be fully qualified since the valid when value can be different
                                final String completeKeyPrefix = (normalize)
                                    ? parameter.getMessageKey()
                                    : useCase.getTitleKey() + '.' +
                                    page.getMessageKey() + '.' +
                                    action.getMessageKey() + '.' +
                                    parameter.getMessageKey();
                                messages.put(completeKeyPrefix + "_validwhen",
                                    "{0} is only valid when " + parameter.getValidWhen());
                            }

                            if (parameter.getOptionCount() > 0)
                            {
                                final List optionKeys = parameter.getOptionKeys();
                                final List optionValues = parameter.getOptionValues();

                                for (int m = 0; m < optionKeys.size(); m++)
                                {
                                    messages.put(optionKeys.get(m), optionValues.get(m));
                                    messages.put(optionKeys.get(m) + ".title", optionValues.get(m));
                                }
                            }
                        }
                    }
                }
            }
        }

        return messages;
    }

    protected String handleGetOnlineHelpPagePath()
    {
        final StringBuffer buffer = new StringBuffer();

        if (StringUtils.isNotBlank(this.getPackagePath()))
        {
            buffer.append('/');
            buffer.append(this.getPackagePath());
        }
        buffer.append('/');
        buffer.append(StringUtilsHelper.separate(this.getName(), "-"));
        buffer.append("_help");

        return buffer.toString();
    }

    protected String handleGetOnlineHelpActionPath()
    {
        final StringBuffer buffer = new StringBuffer();

        if (StringUtils.isNotBlank(this.getPackagePath()))
        {
            buffer.append('/');
            buffer.append(this.getPackagePath());
        }
        buffer.append('/');
        buffer.append(StringUtilsHelper.upperCamelCaseName(this.getName()));
        buffer.append("Help");

        return buffer.toString();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCase#getFormKey()
     */
    protected String handleGetFormKey()
    {
        final Object formKeyValue = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_FORM_KEY);
        return formKeyValue == null
            ? Bpm4StrutsProfile.TAGGEDVALUE_ACTION_FORM_DEFAULT_KEY
            : String.valueOf(formKeyValue);
    }

}
