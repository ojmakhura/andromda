package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.andromda.core.common.CollectionFilter;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class ActivityGraphFacadeLogicImpl
       extends ActivityGraphFacadeLogic
       implements org.andromda.metafacades.uml.ActivityGraphFacade
{
    // ---------------- constructor -------------------------------
    
    public ActivityGraphFacadeLogicImpl (org.omg.uml.behavioralelements.activitygraphs.ActivityGraph metaObject, String context)
    {
        super (metaObject, context);
    }
    
    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StateMachineDecorator ...

    // ------------- relations ------------------
    
    public Collection handleGetInitialStates()
    {
        final CollectionFilter filter = new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return (object instanceof Pseudostate) &&
                    (PseudostateKindEnum.PK_INITIAL.equals(((Pseudostate)object).getKind()));
            }
        };
        return getSubvertices(filter);
    }

    public Collection handleGetPseudostates()
    {
        final CollectionFilter filter = new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return (object instanceof Pseudostate);
            }
        };
        return getSubvertices(filter);
    }

    public Collection handleGetActionStates()
    {
        final CollectionFilter filter = new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return object instanceof ActionState;
            }
        };
        return getSubvertices(filter);
    }

    public Collection handleGetObjectFlowStates()
    {
        final CollectionFilter filter = new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return object instanceof ObjectFlowState;
            }
        };
        return getSubvertices(filter);
    }

    public Collection handleGetFinalStates()
    {
        final CollectionFilter filter = new CollectionFilter()
        {
            public boolean accept(Object object)
            {
                return object instanceof FinalState;
            }
        };
        return getSubvertices(filter);
    }

    protected Collection getSubvertices(CollectionFilter collectionFilter)
    {
        CompositeState compositeState = (CompositeState) metaObject.getTop();
        return filter(compositeState.getSubvertex(), collectionFilter);
    }

    private Collection filter(Collection collection, CollectionFilter collectionFilter)
    {
        final Set filteredCollection = new LinkedHashSet();
        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (collectionFilter.accept(object))
            {
                filteredCollection.add(object);
            }
        }
        return filteredCollection;
    }

    protected Object handleGetContextElement()
    {
        return metaObject.getContext();
    }

    protected Collection handleGetTransitions()
    {
        return metaObject.getTransitions();
    }
}
