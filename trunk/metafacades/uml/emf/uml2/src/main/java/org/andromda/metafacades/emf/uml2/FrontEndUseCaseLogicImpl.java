package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndFinalState;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndUseCase.
 *
 * @see org.andromda.metafacades.uml.FrontEndUseCase
 */
public class FrontEndUseCaseLogicImpl
    extends FrontEndUseCaseLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public FrontEndUseCaseLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return hasStereotype(FrontEndApplication)
     * @see org.andromda.metafacades.uml.FrontEndUseCase#isEntryUseCase()
     */
    protected boolean handleIsEntryUseCase()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_APPLICATION);
    }

    /**
     * @return getActivityGraph().getController()
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getController()
     */
    protected Object handleGetController()
    {
        final FrontEndActivityGraph graph = this.getActivityGraph();
        return graph == null ? null : graph.getController();
    }

    /**
     * @return getFirstActivityGraph()
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getActivityGraph()
     */
    protected Object handleGetActivityGraph()
    {
        // There is a method in usecase Facade.
        // We can use it because, for now, we don't support hyperlink neither
        // tag value way to define
        // which activity graph is modelised for this use case.
        return this.getFirstActivityGraph();
    }

    /**
     * @return getModel().findFinalStatesWithNameOrHyperlink(this)
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getReferencingFinalStates()
     */
    protected List handleGetReferencingFinalStates()
    {
        return new ArrayList(this.getModel().findFinalStatesWithNameOrHyperlink(this));
    }

    /**
     * @return getModel().getAllUseCases() instanceof FrontEndUseCase
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getAllUseCases()
     */
    protected List handleGetAllUseCases()
    {
        final List useCases = new ArrayList();
        for (final Iterator useCaseIterator = this.getModel().getAllUseCases().iterator(); useCaseIterator.hasNext();)
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
    private Collection getAssociatedRoles()
    {
        final Collection usersList = new ArrayList();
        final Collection associationEnds = this.getAssociationEnds();
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
     * Recursively collects all roles generalizing the argument user, in the
     * specified collection.
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
     * @return getAssociatedRoles() collectRoles
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
     * @return getAllUseCases().getRoles()
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
     * @return !getRoles().isEmpty()
     * @see org.andromda.metafacades.uml.FrontEndUseCase#isSecured()
     */
    protected boolean handleIsSecured()
    {
        return !this.getRoles().isEmpty();
    }

    /**
     * @return getViews
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
                new ArrayList(
                    this.getModel().getAllActionStatesWithStereotype(
                        graph,
                        UMLProfile.STEREOTYPE_FRONT_END_VIEW));
        }
        return views;
    }

    /**
     * @return getViews
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getViews()
     */
    protected List handleGetActions()
    {
        final Collection actions = new LinkedHashSet();
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
     * @return getInitialView
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

    /**
     * @return getViewVariables
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getViewVariables()
     */
    protected List handleGetViewVariables()
    {
        final Map pageVariableMap = new LinkedHashMap();

        // - page variables can occur twice or more in the usecase if their
        // names are the same for different forms, storing them in a map
        // solves this issue because those names do not have the action-name
        // prefix
        final Collection views = this.getViews();
        for (final Iterator pageIterator = views.iterator(); pageIterator.hasNext();)
        {
            final FrontEndView view = (FrontEndView)pageIterator.next();
            final Collection variables = view.getVariables();
            for (final Iterator variableIterator = variables.iterator(); variableIterator.hasNext();)
            {
                FrontEndParameter variable = (FrontEndParameter)variableIterator.next();
                final String name = variable.getName();
                if (StringUtils.isNotBlank(name))
                {
                    final FrontEndParameter existingVariable = (FrontEndParameter)pageVariableMap.get(name);
                    if (existingVariable != null)
                    {
                        if (existingVariable.isTable())
                        {
                            variable = existingVariable;
                        }
                    }
                    pageVariableMap.put(
                        name,
                        variable);
                }
            }
        }
        return new ArrayList(pageVariableMap.values());
    }
}