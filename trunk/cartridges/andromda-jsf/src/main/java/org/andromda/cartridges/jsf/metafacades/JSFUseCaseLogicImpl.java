package org.andromda.cartridges.jsf.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.andromda.cartridges.jsf.JSFGlobals;
import org.andromda.cartridges.jsf.JSFProfile;
import org.andromda.cartridges.jsf.JSFUtils;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndFinalState;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.IncludeFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFUseCase.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase
 */
public class JSFUseCaseLogicImpl
    extends JSFUseCaseLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JSFUseCaseLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return actionPath
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getPath()
     */
    protected String handleGetPath()
    {
        String actionPath = null;
        final FrontEndActivityGraph graph = this.getActivityGraph();
        if (graph != null)
        {
            final JSFAction action = (JSFAction)graph.getInitialAction();
            if (action != null)
            {
                actionPath = action.getPath();
            }
        }
        return actionPath;
    }

    /**
     * @return pathRoot
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getPathRoot()
     */
    protected String handleGetPathRoot()
    {
        final StringBuilder pathRoot = new StringBuilder("/");
        final String packagePath = this.getPackagePath();
        final String prefix = packagePath != null ? packagePath.trim() : "";
        pathRoot.append(prefix);
        return pathRoot.toString();
    }

    /**
     * @return forwardName
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getPathRoot()
     */
    protected String handleGetForwardName()
    {
        return JSFUtils.toWebResourceName(this.getName()) + JSFGlobals.USECASE_FORWARD_NAME_SUFFIX;
    }

    /**
     * @return titleKey
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getTitleKey()
     */
    protected String handleGetTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(
            this.isNormalizeMessages() ? this.getTitleValue() : this.getName()) + '.' +
        JSFGlobals.TITLE_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @return toPhrase(getName())
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getTitleValue()
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
        final String normalizeMessages = (String)getConfiguredProperty(JSFGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    /**
     * @return allMessages
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getAllMessages()
     */
    @SuppressWarnings("unchecked")
    protected Map handleGetAllMessages()
    {
        final boolean normalize = this.isNormalizeMessages();
        final Map<String, String> messages = (normalize) ?
            (Map<String, String>)new TreeMap() : (Map<String, String>)new LinkedHashMap<String, String>();

        // - only retrieve the messages for the entry use case (i.e. the use case
        //   where the application begins)
        if (this.isEntryUseCase())
        {
            final List<FrontEndUseCase> useCases = this.getAllUseCases();
            for (int ctr = 0; ctr < useCases.size(); ctr++)
            {
                // - usecase
                final JSFUseCase useCase = (JSFUseCase)useCases.get(ctr);
                messages.put(
                    useCase.getTitleKey(),
                    useCase.getTitleValue());

                final List<FrontEndView> views = useCase.getViews();
                for (int ctr2 = 0; ctr2 < views.size(); ctr2++)
                {
                    // - view
                    final JSFView view = (JSFView)views.get(ctr2);
                    messages.put(
                        view.getTitleKey(),
                        view.getTitleValue());
                    messages.put(
                        view.getMessageKey(),
                        view.getMessageValue());
                    messages.put(
                        view.getDocumentationKey(),
                        view.getDocumentationValue());

                    final List<FrontEndParameter> viewVariables = view.getVariables();
                    for (int ctr3 = 0; ctr3 < viewVariables.size(); ctr3++)
                    {
                        // - page variables
                        final Object object = viewVariables.get(ctr3);
                        if (object instanceof JSFParameter)
                        {
                            final JSFParameter parameter = (JSFParameter)object;

                            final Collection<ClassifierFacade> resolvingTypes = new ArrayList<ClassifierFacade>();
                            this.collectAttributeMessages(messages, parameter.getAttributes(), resolvingTypes);
                            this.collectAssociationEndMessages(messages, parameter.getNavigableAssociationEnds(), resolvingTypes);
                            messages.put(
                                parameter.getMessageKey(),
                                parameter.getMessageValue());

                            // - table
                            if (parameter.isTable())
                            {
                                for (String columnName : parameter.getTableColumnNames())
                                {
                                     messages.put(
                                        parameter.getTableColumnMessageKey(columnName),
                                        parameter.getTableColumnMessageValue(columnName));
                                }
                            }
                        }
                    }

                    final List<FrontEndAction> actions = useCase.getActions();
                    for (int ctr3 = 0; ctr3 < actions.size(); ctr3++)
                    {
                        // - action
                        final JSFAction action = (JSFAction)actions.get(ctr3);

                        // - event/trigger
                        final Object trigger = action.getTrigger();
                        if (trigger != null && trigger instanceof JSFEvent)
                        {
                            final JSFEvent event = (JSFEvent)trigger;
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
                        for (final FrontEndForward forward : action.getTransitions())
                        {
                            if (forward instanceof JSFForward)
                            {
                                final JSFForward forwardTransition = (JSFForward)forward;
                                messages.putAll(forwardTransition.getSuccessMessages());
                                messages.putAll(forwardTransition.getWarningMessages());
                            }
                            else
                            {
                                final JSFAction actionTransition = (JSFAction)forward;
                                messages.putAll(actionTransition.getSuccessMessages());
                                messages.putAll(actionTransition.getWarningMessages());
                            }

                        }

                        // - action parameters
                        final List<FrontEndParameter> parameters = action.getParameters();
                        for (int l = 0; l < parameters.size(); l++)
                        {
                            final Object object = parameters.get(l);
                            if (object instanceof JSFParameter)
                            {
                                final JSFParameter parameter = (JSFParameter)object;
                                final Collection attributes = parameter.getAttributes();
                                if (!attributes.isEmpty())
                                {
                                    for (final Iterator iterator = attributes.iterator(); iterator.hasNext();)
                                    {
                                        final JSFAttribute attribute = (JSFAttribute)iterator.next();
                                        messages.put(
                                            attribute.getMessageKey(),
                                            attribute.getMessageValue());
                                    }
                                }
                                final Collection associationEnds = parameter.getNavigableAssociationEnds();
                                if (!associationEnds.isEmpty())
                                {
                                    for (final Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
                                    {
                                        final AssociationEndFacade end = (AssociationEndFacade)iterator.next();
                                        final ClassifierFacade type = end.getType();
                                        if (type != null)
                                        {
                                            final Collection<AttributeFacade> typeAttributes = type.getAttributes();
                                            if (!attributes.isEmpty())
                                            {
                                                for (final Iterator<AttributeFacade> attributeIterator = typeAttributes.iterator();
                                                    attributeIterator.hasNext();)
                                                {
                                                    final JSFAttribute attribute = (JSFAttribute)attributeIterator.next();
                                                    messages.put(
                                                        attribute.getMessageKey(),
                                                        attribute.getMessageValue());
                                                }
                                            }
                                        }
                                    }
                                }
                                messages.put(
                                    parameter.getMessageKey(),
                                    parameter.getMessageValue());
                                messages.put(
                                    parameter.getDocumentationKey(),
                                    parameter.getDocumentationValue());

                                // - submittable input table
                                if (parameter.isInputTable())
                                {
                                    final Collection<String> columnNames = parameter.getTableColumnNames();
                                    for (final Iterator<String> columnNameIterator = columnNames.iterator();
                                        columnNameIterator.hasNext();)
                                    {
                                        final String columnName = columnNameIterator.next();
                                        messages.put(
                                            parameter.getTableColumnMessageKey(columnName),
                                            parameter.getTableColumnMessageValue(columnName));
                                    }
                                }
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
                        }

                        // - portlet preferences
                        final JSFPortletPreferences preferences = useCase.getPreferences();
                        if (preferences != null)
                        {
                            final Collection<AttributeFacade> attributes = preferences.getAttributes(true);
                            if (!attributes.isEmpty())
                            {
                                for (final Iterator iterator = attributes.iterator(); iterator.hasNext();)
                                {
                                    final JSFAttribute attribute = (JSFAttribute)iterator.next();
                                    messages.put(
                                        attribute.getMessageKey(),
                                        attribute.getMessageValue());
                                }
                            }
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
     * Collects all attribute messages into the given Map.
     *
     * @param messages the Map in which messages are collected.
     * @param attributes the attributes to collect the messages from.
     * @param resolvingTypes used to prevent endless recursion.
     */
    private void collectAttributeMessages(Map<String,String> messages, Collection attributes,
        final Collection<ClassifierFacade> resolvingTypes)
    {
        if (attributes != null && !attributes.isEmpty())
        {
            for (final Iterator iterator = attributes.iterator(); iterator.hasNext();)
            {
                final JSFAttribute attribute = (JSFAttribute)iterator.next();
                messages.put(
                    attribute.getMessageKey(),
                    attribute.getMessageValue());
                // - lets go another level for nested attributes
                this.collectTypeMessages(messages, attribute.getType(), resolvingTypes);
            }
        }
    }

    /**
     * Collects all association end messages into the given Map.
     *
     * @param messages the Map in which messages are collected.
     * @param associationEnds the association ends to collect the messages from.
     * @param resolvingTypes used to prevent endless recursion.
     */
    private void collectAssociationEndMessages(Map<String,String> messages, Collection associationEnds,
        final Collection<ClassifierFacade> resolvingTypes)
    {
        if (associationEnds != null && !associationEnds.isEmpty())
        {
            for (final Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
            {
                final AssociationEndFacade end = (AssociationEndFacade)iterator.next();
                this.collectTypeMessages(messages, end.getType(), resolvingTypes);
            }
        }
    }

   private void collectTypeMessages(Map<String,String> messages, ClassifierFacade type,
        final Collection<ClassifierFacade> resolvingTypes)
   {
       if (type != null)
       {
           if (!resolvingTypes.contains(type))
           {
               resolvingTypes.add(type);
               if (type.isArrayType())
               {
                   type = type.getNonArray();
               }
               //check again, since the type can be changed
               if (!resolvingTypes.contains(type))
               {
                   this.collectAttributeMessages(messages, type.getAttributes(), resolvingTypes);
                   this.collectAssociationEndMessages(messages, type.getNavigableConnectingEnds(), resolvingTypes);
               }
           }
           resolvingTypes.remove(type);
       }
   }

    /**
     * @return actionForwards
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getActionForwards()
     */
    protected List<JSFAction> handleGetActionForwards()
    {
        final Set<JSFAction> actionForwards = new LinkedHashSet<JSFAction>();
        for (final FrontEndView view : this.getViews())
        {
            actionForwards.addAll(((JSFView)view).getActionForwards());
        }
        return new ArrayList<JSFAction>(actionForwards);
    }

    /**
     * @return forwards
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getForwards()
     */
    protected List<JSFForward> handleGetForwards()
    {
        final Map<String, JSFForward> forwards = new LinkedHashMap<String, JSFForward>();
        for (final FrontEndAction action : this.getActions())
        {
            for (final FrontEndForward forward : action.getActionForwards())
            {
                if (forward instanceof JSFForward)
                {
                    forwards.put(forward.getName(), (JSFForward) forward);
                }
            }
        }
        return new ArrayList(forwards.values());
    }

    /**
     * @return allForwards
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getAllForwards()
     */
    @SuppressWarnings("unchecked")
    protected List<ModelElementFacade> handleGetAllForwards()
    {
        final Map<String, ModelElementFacade> forwards = new LinkedHashMap<String, ModelElementFacade>();
        for (final JSFAction forward : this.getActionForwards())
        {
            forwards.put(forward.getName(), forward);
        }
        for (final JSFForward forward : this.getForwards())
        {
            forwards.put(forward.getName(), forward);
        }
        return new ArrayList<ModelElementFacade>(forwards.values());
    }

    /**
     * @return upperCamelCaseName(this.getName())
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getActionClassName()
     */
    protected String handleGetActionClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName());
    }

    /**
     * @return getFullyQualifiedActionClassName().replace('.', '/') + ".java"
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getFullyQualifiedActionClassPath()
     */
    protected String handleGetFullyQualifiedActionClassPath()
    {
        return this.getFullyQualifiedActionClassName().replace(
            '.',
            '/') + ".java";
    }

    /**
     * @return lowerCamelCaseName(this.getName())
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getControllerAction()
     */
    protected String handleGetControllerAction()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
    }

    /**
     * @return fullyQualifiedActionClassName
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getFullyQualifiedActionClassName()
     */
    protected String handleGetFullyQualifiedActionClassName()
    {
        final StringBuilder path = new StringBuilder();
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
     * @return formKeyValue
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getFormKey()
     */
    protected String handleGetFormKey()
    {
        final Object formKeyValue = this.findTaggedValue(JSFProfile.TAGGEDVALUE_ACTION_FORM_KEY);
        return formKeyValue == null ? ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.ACTION_FORM_KEY))
                                    : String.valueOf(formKeyValue);
    }

    /**
     * @return initialTargetPath
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getInitialTargetPath()
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
        final Collection<FrontEndForward> forwards = action != null ? action.getActionForwards() : null;
        if (forwards != null && !forwards.isEmpty())
        {
            final Object target = forwards.iterator().next().getTarget();
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
     * @return required
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#isValidationRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        boolean required = false;
        for (final FrontEndView feView : this.getViews())
        {
            final JSFView view = (JSFView)feView;
            if (view.isValidationRequired())
            {
                required = true;
                break;
            }
        }
        return required;
    }

    /**
     * @return getInitialTarget() instanceof JSFView
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#isInitialTargetView()
     */
    protected boolean handleIsInitialTargetView()
    {
        return this.getInitialTarget() instanceof JSFView;
    }

    /**
     * @return required
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#isInitialTargetView()
     */
    protected boolean handleIsApplicationValidationRequired()
    {
        boolean required = false;
        for (final FrontEndUseCase feUseCase : this.getAllUseCases())
        {
            final JSFUseCase useCase = (JSFUseCase)feUseCase;
            if (useCase.isValidationRequired())
            {
                required = true;
                break;
            }
        }
        return required;
    }

    /**
     * @return sameName
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#isViewHasNameOfUseCase()
     */
    protected boolean handleIsViewHasNameOfUseCase()
    {
        boolean sameName = false;
        for (final FrontEndView view : this.getViews())
        {
            sameName = ((JSFView)view).isHasNameOfUseCase();
            if (sameName)
            {
                break;
            }
        }
        return sameName;
    }

    /**
     * @return hasStereotype(JSFProfile.STEREOTYPE_FRONT_END_REGISTRATION)
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#isRegistrationUseCase()
     */
    protected boolean handleIsRegistrationUseCase()
    {
        return this.hasStereotype(JSFProfile.STEREOTYPE_FRONT_END_REGISTRATION);
    }

    /**
     * @return useCases
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getRegistrationUseCases()
     */
    @SuppressWarnings("unchecked")
    protected List<FrontEndUseCase> handleGetRegistrationUseCases()
    {
        final List<FrontEndUseCase> useCases = new ArrayList<FrontEndUseCase>(this.getAllUseCases());
        for (final Iterator<FrontEndUseCase> iterator = useCases.iterator(); iterator.hasNext();)
        {
            final FrontEndUseCase useCase = iterator.next();
            if (useCase instanceof JSFUseCase)
            {
                if (!((JSFUseCase)useCase).isRegistrationUseCase())
                {
                    iterator.remove();
                }
            }
            else
            {
                iterator.remove();
            }
        }
        return useCases;
    }

    /**
     * The suffix for the forwards class name.
     */
    private static final String FORWARDS_CLASS_NAME_SUFFIX = "Forwards";

    /**
     * @return getName() + FORWARDS_CLASS_NAME_SUFFIX
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getForwardsClassName()
     */
    protected String handleGetForwardsClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName()) + FORWARDS_CLASS_NAME_SUFFIX ;
    }

    /**
     * @return navigationRules
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getNavigationRules()
     */
    @SuppressWarnings("unchecked")
    protected Collection<Object> handleGetNavigationRules()
    {
        final Map<String, Object> rules = new LinkedHashMap<String, Object>();
        for (final FrontEndView feView : this.getViews())
        {
            final JSFView view = (JSFView)feView;
            rules.put(view.getFromOutcome(), view);
            for (final Iterator forwardIterator = view.getForwards().iterator(); forwardIterator.hasNext();)
            {
                final Object forward = forwardIterator.next();
                String name;
                if (forward instanceof JSFForward)
                {
                    name = ((JSFForward)forward).getFromOutcome();
                }
                else
                {
                    name = ((JSFAction)forward).getFromOutcome();
                }
                rules.put(name, forward);
            }
        }
        return rules.values();
    }

    /**
     * @return navigationChildren
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getNavigationChildren()
     */
    protected Collection<UseCaseFacade> handleGetNavigationChildren()
    {
        return CollectionUtils.collect(getIncludes(), new Transformer()
        {
            public Object transform(Object object)
            {
                final IncludeFacade include = (IncludeFacade)object;
                return include.getAddition();
            }
        });
    }

    private static boolean isParent(final JSFUseCase useCase1, final JSFUseCase useCase2)
    {
        return CollectionUtils.exists(useCase2.getIncludes(), new Predicate()
        {
            public boolean evaluate(Object object)
            {
                final IncludeFacade include = (IncludeFacade)object;
                return include.getAddition().equals(useCase1);
            }
        });
    }

    /**
     * @return navigationParents
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getNavigationParents()
     */
    protected Collection<FrontEndUseCase> handleGetNavigationParents()
    {
            final JSFUseCase theUseCase = this;
            return CollectionUtils.select(getAllUseCases(),new Predicate() {
                @SuppressWarnings("synthetic-access")
                public boolean evaluate(Object o)
                {
                    final JSFUseCase useCase = (JSFUseCase)o;
                    if (theUseCase.equals(useCase))
                        return false;
                    return isParent(theUseCase, useCase);
                }
            });
    }

    /**
     * @return actionRoles
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCase#getActionRoles()
     */
    protected String handleGetActionRoles()
    {
        final StringBuilder rolesBuffer = new StringBuilder();
        boolean first = true;
        for (final Role role : this.getRoles())
        {
            if (first)
            {
                first = false;
            }
            else
            {
                rolesBuffer.append(',');
            }
            rolesBuffer.append(role.getName());
        }
        return rolesBuffer.toString();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCaseLogic#handleGetPreferences()
     */
    @Override
    protected Object handleGetPreferences()
    {
        JSFPortletPreferences preferences = null;
        final Collection<DependencyFacade> dependencies = this.getSourceDependencies();
        if (dependencies != null && !dependencies.isEmpty())
        {
            for (final DependencyFacade dependency : dependencies)
            {
                final Object target = dependency.getTargetElement();
                if (dependency.getTargetElement() instanceof JSFPortletPreferences)
                {
                    preferences = (JSFPortletPreferences)target;
                    break;
                }
            }
        }
        return preferences;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCaseLogic#handleGetPortletEditForwardName()
     */
    @Override
    protected String handleGetPortletEditForwardName()
    {
        return this.getWebResourceName() + "-portlet-edit";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCaseLogic#handleGetPortletEditPath()
     */
    @Override
    protected String handleGetPortletEditPath()
    {
        return this.getPathRoot() + "/" + this.getPortletEditForwardName();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCaseLogic#handleGetPortletHelpForwardName()
     */
    @Override
    protected String handleGetPortletHelpForwardName()
    {
        return this.getWebResourceName() + "-portlet-help";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCaseLogic#handleGetPortletHelpPath()
     */
    @Override
    protected String handleGetPortletHelpPath()
    {
        return this.getPathRoot() + "/" + this.getPortletHelpForwardName();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCaseLogic#handleGetPortletViewForwardName()
     */
    @Override
    protected String handleGetPortletViewForwardName()
    {
        return this.getWebResourceName() + "-portlet-view";
    }

    private String getWebResourceName()
    {
        return JSFUtils.toWebResourceName(this.getName());
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCaseLogic#handleGetPortletViewPath()
     */
    @Override
    protected String handleGetPortletViewPath()
    {
        return this.getPath();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFUseCaseLogic#handleGetAllViews()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Collection<FrontEndView> handleGetAllViews()
    {
        final Set<FrontEndView> allViews = new LinkedHashSet<FrontEndView>();
        for (final FrontEndUseCase useCase : this.getAllUseCases())
        {
            allViews.addAll(useCase.getViews());
        }
        return allViews;
    }
}
