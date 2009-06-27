package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.apache.commons.collections.Predicate;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;
import org.omg.uml.behavioralelements.statemachines.StateMachine;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.StateMachineFacade.
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
    public StateMachineFacadeLogicImpl (StateMachine metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getContextElement()
     */
    @Override
    protected ModelElement handleGetContextElement()
    {
        return metaObject.getContext();
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getFinalStates()
     */
    @Override
    protected Collection handleGetFinalStates()
    {
        final Predicate filter =
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return object instanceof FinalState;
                }
            };
        return getSubvertices(filter);
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
        return getSubvertices(filter);
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getInitialTransition()
     */
    @Override
    protected TransitionFacade handleGetInitialTransition()
    {
        TransitionFacade transition = null;

        final PseudostateFacade initialState = getInitialState();
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
    protected Collection handleGetTransitions()
    {
        return metaObject.getTransitions();
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getInitialStates()
     */
    @Override
    protected Collection handleGetInitialStates()
    {
        final Predicate filter =
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return (object instanceof Pseudostate) &&
                    (PseudostateKindEnum.PK_INITIAL.equals(((Pseudostate)object).getKind()));
                }
            };
        return getSubvertices(filter);
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getInitialState()
     */
    @Override
    protected PseudostateFacade handleGetInitialState()
    {
        PseudostateFacade initialState = null;

        final Collection<PseudostateFacade> initialStates = getInitialStates();
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
    protected Collection handleGetPseudostates()
    {
        final Predicate filter =
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return (object instanceof Pseudostate);
                }
            };
        return getSubvertices(filter);
    }

    protected Collection getSubvertices(Predicate collectionFilter)
    {
        final CompositeState compositeState = (CompositeState)metaObject.getTop();
        return filter(compositeState.getSubvertex(), collectionFilter);
    }

    private Collection filter(
        Collection collection,
        Predicate collectionFilter)
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
     * @return getPackage()
     */
    public Object handleGetValidationOwner()
    {
        return getPackage();
    }
}