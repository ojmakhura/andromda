package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
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
import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.FinalStateFacade;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.Role;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCase
 */
public class StrutsUseCaseLogicImpl
    extends StrutsUseCaseLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public StrutsUseCaseLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetTitleKey()
     */
    protected String handleGetTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(normalizeMessages() ? getTitleValue() : getName()) + ".title";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetTitleValue()
     */
    protected String handleGetTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetOnlineHelpKey()
     */
    protected String handleGetOnlineHelpKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName()) + ".online.help";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetOnlineHelpValue()
     */
    protected String handleGetOnlineHelpValue()
    {
        final String crlf = "<br/>";
        final StringBuilder buffer = new StringBuilder();

        buffer.append(!this.isDocumentationPresent() ? "No use-case documentation has been specified"
            : StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false)));
        buffer.append(crlf);

        return StringUtilsHelper.toResourceMessage(buffer.toString());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetActionPath()
     */
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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetActionPathRoot()
     */
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
     * @return isCyclic
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCase#isCyclic()
     */
    protected boolean handleIsCyclic()
    {
        boolean selfTargetting = false;
        final ActivityGraphFacade graph = getActivityGraph();
        if (graph != null)
        {
            final Collection<FinalStateFacade> finalStates = graph.getFinalStates();
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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetActionRoles()
     */
    protected String handleGetActionRoles()
    {
        final Collection<Role> users = this.getRoles();
        final StringBuilder rolesBuffer = new StringBuilder();
        boolean first = true;
        for (final Iterator<Role> userIterator = users.iterator(); userIterator.hasNext();)
        {
            if (first)
            {
                first = false;
            }
            else
            {
                rolesBuffer.append(',');
            }
            final Role role = userIterator.next();
            rolesBuffer.append(role.getName());
        }
        return rolesBuffer.toString();
    }

    /**
     * Collections.EMPTY_LIST
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#getOperations()
     */
    public List getOperations()
    {
        return Collections.emptyList();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetPages()
     */
    protected List<FrontEndView> handleGetPages()
    {
        return this.getViews();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetAllPages()
     */
    protected List<StrutsJsp> handleGetAllPages()
    {
        final List<StrutsJsp> pagesList = new ArrayList<StrutsJsp>();
        for (final ActionStateFacade actionState : getModel().getAllActionStates())
        {
            if (actionState instanceof StrutsJsp)
                pagesList.add((StrutsJsp)actionState);
        }
        return pagesList;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetFormFields()
     */
    protected List handleGetFormFields()
    {
        final List formFields = new ArrayList(); // parameter names are supposed to be unique

        for (final StrutsJsp jsp : getPages())
        {
            for (final StrutsParameter parameter : jsp.getPageVariables())
            {
                formFields.add(parameter);
            }
            for (final FrontEndParameter parameter : jsp.getAllActionParameters())
            {
                formFields.add(parameter);
            }
        }
        return formFields;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleIsValidationRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        for (final StrutsJsp jsp : this.getAllPages())
        {
            if (jsp.isValidationRequired())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleIsApplicationValidationRequired()
     */
    protected boolean handleIsApplicationValidationRequired()
    {
        for (final FrontEndUseCase useCase : this.getAllUseCases())
        {
            if (((StrutsUseCase)useCase).isValidationRequired())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Overridden because StrutsAction does not extend FrontEndAction.
     *
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getActions()
     */
    public List getActions()
    {
        final Collection actions = new LinkedHashSet();

        for (final StrutsJsp jsp : getPages())
        {
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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetPageVariables()
     */
    protected List<FrontEndParameter>  handleGetPageVariables()
    {
        return this.getViewVariables();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleIsApplicationUseCase()
     */
    protected boolean handleIsApplicationUseCase()
    {
        return this.isEntryUseCase();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetCssFileName()
     */
    protected String handleGetCssFileName()
    {
        return this.getPackagePath() + '/' + Bpm4StrutsUtils.toWebFileName(this.getName()) + ".css";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetApplicationHierarchyRoot()
     */
    protected TreeNode handleGetApplicationHierarchyRoot()
    {
        final UseCaseNode root = new UseCaseNode(this);
        this.createHierarchy(root);
        return root;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetHierarchyRoot()
     */
    protected TreeNode handleGetHierarchyRoot()
    {
        UseCaseNode hierarchy = null;

        final Collection<FrontEndUseCase> allUseCases = this.getAllUseCases();
        for (final Iterator<FrontEndUseCase> useCaseIterator = allUseCases.iterator(); useCaseIterator.hasNext();)
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
            final Collection<FinalStateFacade> finalStates = graph.getFinalStates();
            for (final Iterator<FinalStateFacade> finalStateIterator = finalStates.iterator(); finalStateIterator.hasNext();)
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

        final List<UseCaseNode> nodeList = new ArrayList<>(); //Collections.list(root.breadthFirstEnumeration().);
        Enumeration<TreeNode> nodes = root.breadthFirstEnumeration();
        
        while(nodes.hasMoreElements()) {
            UseCaseNode node = (UseCaseNode) nodes.nextElement();
            nodeList.add(node);
        }

        for (final Iterator<UseCaseNode> nodeIterator = nodeList.iterator(); nodeIterator.hasNext() && useCaseNode == null;)
        {
            UseCaseNode node = nodeIterator.next();
            if (useCase.equals(node.getUserObject()))
            {
                useCaseNode = node;
            }
        }
        return useCaseNode;
    }

    /**
     *
     */
    public static final class UseCaseNode
        extends DefaultMutableTreeNode
    {
        /**
         * Automatically generated variable: serialVersionUID
         */
        private static final long serialVersionUID = -4839356733341754931L;

        /**
         * @param useCase
         */
        public UseCaseNode(StrutsUseCase useCase)
        {
            super(useCase);
        }

        /**
         * @return getUserObject()
         */
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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetAllMessages()
     */
    protected Map handleGetAllMessages()
    {
        final boolean normalize = this.normalizeMessages();
        final Map<String, String> messages = (normalize) ? new TreeMap() : new LinkedHashMap<String, String>();

        if (this.isApplicationUseCase())
        {
            final List<FrontEndUseCase> useCases = this.getAllUseCases();
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
                    for (FrontEndForward forward : action.getTransitions())
                    {
                        messages.putAll(((StrutsForward)forward).getSuccessMessages());
                        messages.putAll(((StrutsForward)forward).getWarningMessages());
                    }

                    // EXCEPTION FORWARDS
                    final List<StrutsExceptionHandler> exceptions = action.getActionExceptions();

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
                            for (final StrutsExceptionHandler exception : exceptions)
                            {
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
                                final StrutsExceptionHandler exception = exceptions.get(l);
                                // we construct the key using the action message too because the exception can
                                // belong to more than one action (therefore it cannot return the correct value
                                // in .getExceptionKey())
                                messages.put(action.getMessageKey() + '.' + exception.getExceptionKey(),
                                    "{0} (" + exception.getExceptionType() + ')');
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

                    final List<StrutsParameter> pageVariables = page.getPageVariables();
                    for (int k = 0; k < pageVariables.size(); k++)
                    {
                        // PAGE-VARIABLE
                        final StrutsParameter parameter = pageVariables.get(k);

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
                            final Collection<String> columnNames = parameter.getTableColumnNames();
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
                        // final List parameters = (List)action.getActionParameters();
                        // for (int l = 0; l < parameters.size(); l++)
                        // {
                        //     final StrutsParameter parameter = (StrutsParameter)parameters.get(l);
                        //     messages.put(parameter.getMessageKey(), parameter.getMessageValue());
                        //     messages.put(parameter.getOnlineHelpKey(), parameter.getOnlineHelpValue());
                        //     messages.put(parameter.getDocumentationKey(), parameter.getDocumentationValue());
                        //     messages.put(parameter.getTitleKey(), parameter.getTitleValue());

                        //     if (parameter.getValidWhen() != null)
                        //     {
                        //         // this key needs to be fully qualified since the valid when value can be different
                        //         final String completeKeyPrefix = (normalize)
                        //             ? parameter.getMessageKey()
                        //             : useCase.getTitleKey() + '.' +
                        //             page.getMessageKey() + '.' +
                        //             action.getMessageKey() + '.' +
                        //             parameter.getMessageKey();
                        //         messages.put(completeKeyPrefix + "_validwhen",
                        //             "{0} is only valid when " + parameter.getValidWhen());
                        //     }

                        //     if (parameter.getOptionCount() > 0)
                        //     {
                        //         final List optionKeys = parameter.getOptionKeys();
                        //         final List optionValues = parameter.getOptionValues();

                        //         for (int m = 0; m < optionKeys.size(); m++)
                        //         {
                        //             messages.put((String)optionKeys.get(m), (String)optionValues.get(m));
                        //             messages.put((String)optionKeys.get(m) + ".title", (String)optionValues.get(m));
                        //         }
                        //     }
                        // }
                    }
                }
            }
        }

        return messages;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetOnlineHelpPagePath()
     */
    protected String handleGetOnlineHelpPagePath()
    {
        final StringBuilder buffer = new StringBuilder();

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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCaseLogic#handleGetOnlineHelpActionPath()
     */
    protected String handleGetOnlineHelpActionPath()
    {
        final StringBuilder buffer = new StringBuilder();

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
     * @return Bpm4StrutsProfile.TAGGEDVALUE_ACTION_FORM_KEY
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCase#getFormKey()
     */
    protected String handleGetFormKey()
    {
        final Object formKeyValue = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_FORM_KEY);
        return formKeyValue == null
            ? Bpm4StrutsProfile.TAGGEDVALUE_ACTION_FORM_DEFAULT_KEY
            : String.valueOf(formKeyValue);
    }

    @Override
    public Collection getAllowedRoles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFilename() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRestPath() {
        // TODO Auto-generated method stub
        return null;
    }

}
