package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndFinalState;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndUseCase.
 *
 * @see org.andromda.metafacades.uml.FrontEndUseCase
 */
public class FrontEndUseCaseLogicImpl
    extends FrontEndUseCaseLogic
{
    public FrontEndUseCaseLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#isEntryUseCase()
     */
    protected boolean handleIsEntryUseCase()
    {
        return hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_APPLICATION);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getController()
     */
    protected java.lang.Object handleGetController()
    {
        final FrontEndActivityGraph graph = this.getActivityGraph();
        return graph == null ? null : graph.getController();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getActivityGraph()
     */
    protected java.lang.Object handleGetActivityGraph()
    {
        ActivityGraphFacade activityGraph = null;

        // - in case there is a tagged value pointing to an activity graph, and this graph is found,
        //   return it.
        final Object activity = findTaggedValue(UMLProfile.TAGGEDVALUE_PRESENTATION_USECASE_ACTIVITY);
        if (activity != null)
        {
            final String activityName = String.valueOf(activity.toString());
            activityGraph = getModel().findActivityGraphByName(activityName);
        }

        // - otherwise just take the first one in this use-case's namespace.
        if (activityGraph == null)
        {
            final Collection ownedElements = this.getOwnedElements();
            for (final Iterator iterator = ownedElements.iterator(); iterator.hasNext();)
            {
                final Object object = iterator.next();
                if (object instanceof FrontEndActivityGraph)
                {
                    return object;
                }
            }
        }
        return activityGraph;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getReferencingFinalStates()
     */
    protected List handleGetReferencingFinalStates()
    {
        return new ArrayList(this.getModel().findFinalStatesWithNameOrHyperlink(this));
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getAllUseCases()
     */
    protected List handleGetAllUseCases()
    {
        final List useCases = new ArrayList();
        for (final Iterator useCaseIterator = getModel().getAllUseCases().iterator(); useCaseIterator.hasNext();)
        {
            final Object object = useCaseIterator.next();
            if (object instanceof FrontEndUseCase)
            {
                useCases.add(object);
            }
        }
        return useCases;
    }

    /**
     * Gets those roles directly associated to this use-case.
     */
    private final Collection getAssociatedRoles()
    {
        final Collection usersList = new ArrayList();
        final Collection associationEnds = getAssociationEnds();
        for (final Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            final AssociationEndFacade associationEnd = (AssociationEndFacade)iterator.next();
            final ClassifierFacade classifier = associationEnd.getOtherEnd().getType();
            if (classifier instanceof Role)
            {
                usersList.add(classifier);
            }
        }
        return usersList;
    }

    /**
     * Recursively collects all roles generalizing the argument user, in the specified collection.
     */
    private void collectRoles(
        final Role role,
        final Collection roles)
    {
        if (!roles.contains(role))
        {
            roles.add(role);
            final Collection childUsers = role.getGeneralizedByActors();
            for (final Iterator iterator = childUsers.iterator(); iterator.hasNext();)
            {
                final Role childUser = (Role)iterator.next();
                this.collectRoles(
                    childUser,
                    roles);
            }
        }
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getRoles()
     */
    protected List handleGetRoles()
    {
        final Collection allRoles = new LinkedHashSet();
        final Collection associatedUsers = this.getAssociatedRoles();
        for (final Iterator iterator = associatedUsers.iterator(); iterator.hasNext();)
        {
            final Role user = (Role)iterator.next();
            this.collectRoles(
                user,
                allRoles);
        }
        return new ArrayList(allRoles);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getAllRoles()
     */
    protected List handleGetAllRoles()
    {
        final Collection allRoles = new LinkedHashSet();
        for (final Iterator iterator = this.getAllUseCases().iterator(); iterator.hasNext();)
        {
            allRoles.addAll(((FrontEndUseCase)iterator.next()).getRoles());
        }
        return new ArrayList(allRoles);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#isSecured()
     */
    protected boolean handleIsSecured()
    {
        return !this.getRoles().isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getViews()
     */
    protected List handleGetViews()
    {
        List views;
        final ActivityGraphFacade graph = this.getActivityGraph();
        if (graph == null)
        {
            views = Collections.EMPTY_LIST;
        }
        else
        {
            views =
                new ArrayList(getModel().getAllActionStatesWithStereotype(
                        graph,
                        UMLProfile.STEREOTYPE_FRONT_END_VIEW));
        }
        return views;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getViews()
     */
    protected List handleGetActions()
    {
        final Collection actions = new HashSet();
        final Collection pages = this.getViews();
        for (final Iterator pageIterator = pages.iterator(); pageIterator.hasNext();)
        {
            final FrontEndView view = (FrontEndView)pageIterator.next();
            actions.addAll(view.getActions());
        }

        final FrontEndActivityGraph graph = this.getActivityGraph();
        if (graph != null)
        {
            final FrontEndAction action = graph.getInitialAction();
            if (action != null)
            {
                actions.add(action);
            }
        }
        return new ArrayList(actions);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getInitialView()
     */
    protected Object handleGetInitialView()
    {
        FrontEndView view = null;
        final FrontEndActivityGraph graph = this.getActivityGraph();
        final FrontEndAction action = graph != null ? this.getActivityGraph().getInitialAction() : null;
        final Collection forwards = action != null ? action.getActionForwards() : null;
        if (forwards != null)
        {
            for (final Iterator iterator = forwards.iterator(); iterator.hasNext();)
            {
                final FrontEndForward forward = (FrontEndForward)iterator.next();
                final Object target = forward.getTarget();
                if (target instanceof FrontEndView)
                {
                    view = (FrontEndView)target;
                }
                else if (target instanceof FrontEndFinalState)
                {
                    final FrontEndFinalState finalState = (FrontEndFinalState)target;
                    final FrontEndUseCase targetUseCase = finalState.getTargetUseCase();
                    if (targetUseCase != null && !targetUseCase.equals(this.THIS()))
                    {
                        view = targetUseCase.getInitialView();
                    }
                }
            }
        }
        return view;
    }
}