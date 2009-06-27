package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.andromda.metafacades.uml.PseudostateFacade;
import org.apache.commons.collections.Predicate;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.FinalState;
import org.eclipse.uml2.Pseudostate;
import org.eclipse.uml2.PseudostateKind;
import org.eclipse.uml2.Region;
import org.eclipse.uml2.State;


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
    public StateMachineFacadeLogicImpl(
        final org.eclipse.uml2.StateMachine metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getContextElement()
     */
    protected java.lang.Object handleGetContextElement()
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
        return this.getSubvertices(filter);
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
        return this.getSubvertices(filter);
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getInitialTransition()
     */
    protected java.lang.Object handleGetInitialTransition()
    {
        Object transition = null;

        final PseudostateFacade initialState = this.getInitialState();
        if (initialState != null)
        {
            final Collection transitions = initialState.getOutgoings();
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
        Collection regions = this.metaObject.getRegions();
        Collection transitions = new LinkedList();
        for (Iterator it = regions.iterator(); it.hasNext();)
        {
            Region region = (Region)it.next();
            transitions.addAll(region.getTransitions());
        }
        return transitions;
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
                    (PseudostateKind.INITIAL == ((Pseudostate)object).getKind().getValue());
                }
            };
        return this.getSubvertices(filter);
    }

    /**
     * @see org.andromda.metafacades.uml.StateMachineFacade#getInitialState()
     */
    protected java.lang.Object handleGetInitialState()
    {
        Object initialState = null;

        final Collection initialStates = this.getInitialStates();
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
        return this.getSubvertices(filter);
    }

    protected Collection getSubvertices(final Predicate collectionFilter)
    {
        Collection regions = this.metaObject.getRegions();
        Collection subvertices = new LinkedList();
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

    public Object getValidationOwner()
    {
        return this.getPackage();
    }
}