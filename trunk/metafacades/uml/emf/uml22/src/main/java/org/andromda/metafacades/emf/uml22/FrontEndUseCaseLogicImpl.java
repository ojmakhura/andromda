package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.ActorFacade;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndController;
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
     * @see org.andromda.metafacades.uml.FrontEndUseCase#isEntryUseCase()
     */
    @Override
    protected boolean handleIsEntryUseCase()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_APPLICATION);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getController()
     */
    @Override
    protected FrontEndController handleGetController()
    {
        final FrontEndActivityGraph graph = this.getActivityGraph();
        return graph == null ? null : graph.getController();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getActivityGraph()
     */
    @Override
    protected ActivityGraphFacade handleGetActivityGraph()
    {
        // There is a method in use case Facade.
        // We can use it because, for now, we don't support hyperlink neither
        // tag value way to define
        // which activity graph is modelized for this use case.
        return this.getFirstActivityGraph();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getReferencingFinalStates()
     */
    @Override
    protected List handleGetReferencingFinalStates()
    {
        return new ArrayList(this.getModel().findFinalStatesWithNameOrHyperlink(this));
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getAllUseCases()
     */
    @Override
    protected List<FrontEndUseCase> handleGetAllUseCases()
    {
        final List<FrontEndUseCase> useCases = new ArrayList();
        for (final Iterator useCaseIterator = this.getModel().getAllUseCases().iterator(); useCaseIterator.hasNext();)
        {
            final Object object = useCaseIterator.next();
            if (object instanceof FrontEndUseCase)
            {
                useCases.add((FrontEndUseCase)object);
            }
        }
        return useCases;
    }

    /**
     * Gets those roles directly associated to this use-case.
     */
    private Collection<Role> getAssociatedRoles()
    {
        final Collection<Role> usersList = new ArrayList<Role>();
        final Collection<AssociationEndFacade> associationEnds = this.getAssociationEnds();
        for (final Iterator<AssociationEndFacade> iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            final AssociationEndFacade associationEnd = (AssociationEndFacade)iterator.next();
            final ClassifierFacade classifier = associationEnd.getOtherEnd().getType();
            if (classifier instanceof Role)
            {
                usersList.add((Role)classifier);
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
        final Collection<Role> roles)
    {
        if (!roles.contains(role))
        {
            roles.add(role);
            final Collection<ActorFacade> childUsers = role.getGeneralizedByActors();
            for (final Iterator<ActorFacade> iterator = childUsers.iterator(); iterator.hasNext();)
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
    @Override
    protected List<Role> handleGetRoles()
    {
        final Collection allRoles = new LinkedHashSet();
        final Collection<Role> associatedUsers = this.getAssociatedRoles();
        for (final Iterator<Role> iterator = associatedUsers.iterator(); iterator.hasNext();)
        {
            final Role user = iterator.next();
            this.collectRoles(
                user,
                allRoles);
        }
        return new ArrayList<Role>(allRoles);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getAllRoles()
     */
    @Override
    protected List<Role> handleGetAllRoles()
    {
        final Collection<Role> allRoles = new LinkedHashSet();
        for (final Iterator<FrontEndUseCase> iterator = this.getAllUseCases().iterator(); iterator.hasNext();)
        {
            allRoles.addAll(((FrontEndUseCase)iterator.next()).getRoles());
        }
        return new ArrayList<Role>(allRoles);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#isSecured()
     */
    @Override
    protected boolean handleIsSecured()
    {
        return !this.getRoles().isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getViews()
     */
    @Override
    protected List<ActionStateFacade> handleGetViews()
    {
        List<ActionStateFacade> views;
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
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getViews()
     */
    @Override
    protected List<FrontEndAction> handleGetActions()
    {
        final Collection<FrontEndAction> actions = new LinkedHashSet();
        final Collection<FrontEndView> pages = this.getViews();
        for (final Iterator<FrontEndView> pageIterator = pages.iterator(); pageIterator.hasNext();)
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
        return new ArrayList<FrontEndAction>(actions);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getInitialView()
     */
    @Override
    protected FrontEndView handleGetInitialView()
    {
        FrontEndView view = null;
        final FrontEndActivityGraph graph = this.getActivityGraph();
        final FrontEndAction action = graph != null ? this.getActivityGraph().getInitialAction() : null;
        final Collection<FrontEndForward> forwards = action != null ? action.getActionForwards() : null;
        if (forwards != null)
        {
            for (final Iterator<FrontEndForward> iterator = forwards.iterator(); iterator.hasNext();)
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
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getViewVariables()
     */
    @Override
    protected List<FrontEndParameter> handleGetViewVariables()
    {
        final Map<String, FrontEndParameter> pageVariableMap = new LinkedHashMap<String, FrontEndParameter>();

        // - page variables can occur twice or more in the usecase if their
        // names are the same for different forms, storing them in a map
        // solves this issue because those names do not have the action-name
        // prefix
        final Collection views = this.getViews();
        for (final Iterator pageIterator = views.iterator(); pageIterator.hasNext();)
        {
            final FrontEndView view = (FrontEndView)pageIterator.next();
            final Collection<FrontEndParameter> variables = view.getVariables();
            for (final Iterator<FrontEndParameter> variableIterator = variables.iterator(); variableIterator.hasNext();)
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
        return new ArrayList<FrontEndParameter>(pageVariableMap.values());
    }
}
