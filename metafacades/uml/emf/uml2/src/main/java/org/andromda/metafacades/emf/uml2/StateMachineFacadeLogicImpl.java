package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.apache.commons.collections.Predicate;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.FinalState;
import org.eclipse.uml2.Pseudostate;
import org.eclipse.uml2.PseudostateKind;
import org.eclipse.uml2.Region;
import org.eclipse.uml2.State;
import org.eclipse.uml2.StateMachine;
import org.eclipse.uml2.Transition;
import org.eclipse.uml2.Vertex;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.StateMachineFacade.
 *
 * @see org.andromda.metafacades.uml.StateMachineFacade
 * @author Bob Fields
 */
public class StateMachineFacadeLogicImpl
    extends StateMachineFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public StateMachineFacadeLogicImpl(
        final StateMachine metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getContextElement()
     */
    @Override
    protected Element handleGetContextElement()
    {
        Element context = this.metaObject.getContext();
        if (context == null)
        {
            context = this.metaObject.getOwner();
        }
        return context;
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getFinalStates()
     */
    @Override
    protected Collection<Vertex> handleGetFinalStates()
    {
        final Predicate filter =
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return object instanceof FinalState;
                }
            };
        return this.getSubvertices(filter);
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getStates()
     */
    @Override
    protected Collection handleGetStates()
    {
        final Predicate filter =
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return object instanceof State;
                }
            };
        return this.getSubvertices(filter);
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getInitialTransition()
     */
    @Override
    protected TransitionFacade handleGetInitialTransition()
    {
        TransitionFacade transition = null;

        final PseudostateFacade initialState = this.getInitialState();
        if (initialState != null)
        {
            final Collection<TransitionFacade> transitions = initialState.getOutgoings();
            if (!transitions.isEmpty())
            {
                transition = transitions.iterator().next();
            }
        }

        return transition;
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getTransitions()
     */
    @Override
    protected Collection<Transition> handleGetTransitions()
    {
        Collection<Region> regions = this.metaObject.getRegions();
        Collection<Transition> transitions = new LinkedList<Transition>();
        for (Iterator<Region> it = regions.iterator(); it.hasNext();)
        {
            Region region = it.next();
            transitions.addAll(region.getTransitions());
        }
        return transitions;
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getInitialStates()
     */
    @Override
    protected Collection<Vertex> handleGetInitialStates()
    {
        final Predicate filter =
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return (object instanceof Pseudostate) &&
                    (PseudostateKind.INITIAL == ((Pseudostate)object).getKind().getValue());
                }
            };
        return this.getSubvertices(filter);
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getInitialState()
     */
    @Override
    protected PseudostateFacade handleGetInitialState()
    {
        PseudostateFacade initialState = null;

        final Collection<PseudostateFacade> initialStates = this.getInitialStates();
        if (!initialStates.isEmpty())
        {
            initialState = initialStates.iterator().next();
        }

        return initialState;
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getPseudostates()
     */
    @Override
    protected Collection<Vertex> handleGetPseudostates()
    {
        final Predicate filter =
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return (object instanceof Pseudostate);
                }
            };
        return this.getSubvertices(filter);
    }

    /**
     * @param collectionFilter
     * @return region.getSubvertices()
     */
    protected Collection<Vertex> getSubvertices(final Predicate collectionFilter)
    {
        Collection<Region> regions = this.metaObject.getRegions();
        Collection<Vertex> subvertices = new LinkedList<Vertex>();
        for (Iterator it = regions.iterator(); it.hasNext();)
        {
            Region region = (Region)it.next();
            subvertices.addAll(region.getSubvertices());
        }
        return this.filter(
            subvertices,
            collectionFilter);
    }

    private Collection filter(
        final Collection collection,
        final Predicate collectionFilter)
    {
        final Set filteredCollection = new LinkedHashSet();
        for (final Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (collectionFilter.evaluate(object))
            {
                filteredCollection.add(object);
            }
        }
        return filteredCollection;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        return this.getPackage();
    }
}