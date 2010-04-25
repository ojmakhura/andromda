package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.activitygraphs.Partition;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.StateVertex;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.StateVertexFacade
 * @author Bob Fields
 */
public class StateVertexFacadeLogicImpl
        extends StateVertexFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public StateVertexFacadeLogicImpl(StateVertex metaObject,
                                      String context)
    {
        super(metaObject, context);
    }

    protected StateMachine handleGetStateMachine()
    {
        // throws NullPointer if metaObject has no Container... Need to check for null on return.
        if (metaObject.getContainer()==null)
        {
            return null;
        }
        return metaObject.getContainer().getStateMachine();
    }

    protected CompositeState handleGetContainer()
    {
        return metaObject.getContainer();
    }

    protected Collection handleGetIncomings()
    {
        return metaObject.getIncoming();
    }

    protected Collection handleGetOutgoings()
    {
        return metaObject.getOutgoing();
    }

    /**
     * @return getStateMachine
     */
    public Object handleGetValidationOwner()
    {
        return getStateMachine();
    }

    protected Partition handleGetPartition()
    {
        Partition thePartition = null;

        final StateMachine stateMachine = metaObject.getContainer().getStateMachine();
        if (stateMachine instanceof ActivityGraph)
        {
            final ActivityGraph activityGraph = (ActivityGraph)stateMachine;
            final Collection<Partition> partitions = activityGraph.getPartition();
            for (final Iterator<Partition> partitionIterator = partitions.iterator(); partitionIterator.hasNext() && thePartition == null;)
            {
                final Partition partition = partitionIterator.next();
                if (partition.getContents().contains(metaObject))
                {
                    thePartition = partition;
                }
            }
        }

        return thePartition;
    }
}