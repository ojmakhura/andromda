package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCase
 */
public class StrutsUseCaseLogicImpl
        extends StrutsUseCaseLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsUseCase
{
    // ---------------- constructor -------------------------------

    public StrutsUseCaseLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------
    public String handleGetTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName()) + ".title";
    }

    public String handleGetTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    public String handleGetActionPath()
    {
        return getActivityGraph().getFirstAction().getActionPath();
    }

    public String handleGetActionPathRoot()
    {
        return getActivityGraph().getFirstAction().getActionPathRoot();
    }

    public String handleGetFullFormBeanPath()
    {
        return '/' + getFormBeanPackageName().replace('.', '/') + '/' + StringUtilsHelper.toJavaClassName(getName()) + "Form";
    }

    public String handleGetFormBeanName()
    {
        return StringUtilsHelper.lowerCaseFirstLetter(getFormBeanClassName());
    }

    public String handleGetFormBeanClassName()
    {
        return StringUtilsHelper.toJavaClassName(getName()) + "Form";
    }

    public String handleGetFormBeanType()
    {
        return getFormBeanPackageName() + '.' + getFormBeanClassName();
    }

    public String handleGetFormBeanPackageName()
    {
        return getController().getPackageName();
    }

    public String handleGetActionRoles()
    {
        final Collection users = getAllUsers();
        StringBuffer rolesBuffer = new StringBuffer();
        for (Iterator userIterator = users.iterator(); userIterator.hasNext();)
        {
            StrutsUser strutsUser = (StrutsUser) userIterator.next();
            rolesBuffer.append(strutsUser.getRole() + ' ');
        }
        return StringUtilsHelper.separate(rolesBuffer.toString(), ",");
    }

    // ------------- relations ------------------
    protected Collection handleGetAllServices()
    {
        // find all controller dependencies on <<Service>> classes
        final Collection useCases = getAllUseCases();
        final Collection services = new HashSet();
        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            StrutsUseCase useCase = (StrutsUseCase) iterator.next();
            services.addAll(useCase.getController().getServices());
        }
        return services;
    }

    public java.lang.Object handleGetActivityGraph()
    {
        /*
         * In case there is a tagged value pointing to an activity graph, and this graph is found,
         * then return it.
         */
/* @todo: commented out, MUST BE ENABLED LATER
        final Object activity = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_USECASE_ACTIVITY);
        if (activity != null)
        {
            String activityName = activity.toString();
            Collection activityGraphs = getModel().getAllActivityGraphs();
            for (Iterator iterator = activityGraphs.iterator(); iterator.hasNext();)
            {
                Object obj = iterator.next();
                if (obj instanceof StrutsActivityGraph)
                {
                    StrutsActivityGraph activityGraph = (StrutsActivityGraph)obj;
                    if (activityName.equalsIgnoreCase(activityGraph.getName()))
                        return activityGraph;
                }
            }
        }
*/

        /*
         * Otherwise just take the first one in this use-case's namespace.
         */
        Collection ownedElements = getOwnedElements();
        for (Iterator iterator = ownedElements.iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            if (obj instanceof StrutsActivityGraph)
                return obj;
        }

        /*
         * Nothing was found
         */
        return null;
    }

    protected Collection handleGetUsers()
    {
        final Collection usersList = new LinkedList();

        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndFacade associationEnd = (AssociationEndFacade) iterator.next();
            ClassifierFacade classifier = associationEnd.getOtherEnd().getType();
            if (classifier instanceof StrutsUser)
                usersList.add(classifier);
        }

        return usersList;
    }

    protected Collection handleGetAllUsers()
    {
        final Collection allUsersList = new HashSet();
        final Collection associatedUsers = getUsers();
        for (Iterator iterator = associatedUsers.iterator(); iterator.hasNext();)
        {
            StrutsUser user = (StrutsUser) iterator.next();
            collectUsers(user, allUsersList);
        }
        return allUsersList;
    }

    private void collectUsers(StrutsUser user, Collection users)
    {
        if (!users.contains(user))
        {
            users.add(user);

            Collection childUsers = user.getGeneralizedByUsers();
            for (Iterator iterator = childUsers.iterator(); iterator.hasNext();)
            {
                StrutsUser childUser = (StrutsUser) iterator.next();
                collectUsers(childUser, users);
            }
        }
    }

    protected Collection handleGetPages()
    {
        final Collection pagesList = new LinkedList();
        final Collection allActionStates = getActivityGraph().getActionStates();

        for (Iterator actionStateIterator = allActionStates.iterator(); actionStateIterator.hasNext();)
        {
            Object actionState = shieldedElement(actionStateIterator.next());
            if (actionState instanceof StrutsJsp)
                pagesList.add(actionState);
        }
        return pagesList;
    }

    protected Collection handleGetAllPages()
    {
        final Collection pagesList = new LinkedList();
        final Collection allActionStates = getModel().getAllActionStates();

        for (Iterator actionStateIterator = allActionStates.iterator(); actionStateIterator.hasNext();)
        {
            Object actionState = shieldedElement(actionStateIterator.next());
            if (actionState instanceof StrutsJsp)
                pagesList.add(actionState);
        }
        return pagesList;
    }

    protected Collection handleGetAllUseCases()
    {
        final Collection useCases = new LinkedList();

        for (Iterator iterator = getModel().getAllUseCases().iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (object instanceof StrutsUseCase)
                useCases.add(object);
        }
        return useCases;
    }

    protected Object handleGetController()
    {
        return getActivityGraph().getController();
    }

    protected Collection handleGetFormFields()
    {
        final Map formFieldsMap = new HashMap();
        final Collection transitions = getActivityGraph().getTransitions();
        for (Iterator iterator = transitions.iterator(); iterator.hasNext();)
        {
            Object transitionObject = iterator.next();
            if (transitionObject instanceof StrutsAction)
            {
                Collection parameters = ((StrutsAction) transitionObject).getActionFormFields();
                for (Iterator parameterIterator = parameters.iterator(); parameterIterator.hasNext();)
                {
                    ParameterFacade parameter = (ParameterFacade) parameterIterator.next();
                    formFieldsMap.put(parameter.getName(), parameter);
                }
            }
        }
        return formFieldsMap.values();
    }

    protected Collection handleGetFinalStates()
    {
        final Collection finalStatesList = new LinkedList();
        final Collection allFinalStates = getModel().getAllFinalStates();

        for (Iterator iterator = allFinalStates.iterator(); iterator.hasNext();)
        {
            ModelElementFacade modelElement = (ModelElementFacade) iterator.next();
            if (getName().equalsIgnoreCase(modelElement.getName()))
                finalStatesList.add(modelElement);
        }

        return finalStatesList;
    }
}
