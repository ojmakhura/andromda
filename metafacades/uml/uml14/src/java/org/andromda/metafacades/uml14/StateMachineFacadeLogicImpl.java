package org.andromda.metafacades.uml14;

import org.apache.commons.collections.Predicate;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;
import org.andromda.metafacades.uml.PseudostateFacade;

import java.util.Collection;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.StateMachineFacade.
 *
 * @see org.andromda.metafacades.uml.StateMachineFacade
 */
public class StateMachineFacadeLogicImpl
    extends StateMachineFacadeLogic
{

    public StateMachineFacadeLogicImpl (org.omg.uml.behavioralelements.statemachines.StateMachine metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getContextElement()
     */
    protected java.lang.Object handleGetContextElement()
    {
        return metaObject.getContext();
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getFinalStates()
     */
    protected java.util.Collection handleGetFinalStates()
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
    protected java.util.Collection handleGetStates()
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
    protected java.lang.Object handleGetInitialTransition()
    {
        Object transition = null;

        final PseudostateFacade initialState = getInitialState();
        if (initialState != null)
        {
            final Collection transitions = initialState.getOutgoing();
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
    protected java.util.Collection handleGetTransitions()
    {
        return metaObject.getTransitions();
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getInitialStates()
     */
    protected java.util.Collection handleGetInitialStates()
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
    protected java.lang.Object handleGetInitialState()
    {
        Object initialState = null;

        final Collection initialStates = getInitialStates();
        if (!initialStates.isEmpty())
        {
            initialState = initialStates.iterator().next();
        }

        return initialState;
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getPseudostates()
     */
    protected java.util.Collection handleGetPseudostates()
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

    public Object getValidationOwner()
    {
        return getPackage();
    }
}