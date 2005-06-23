package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.activitygraphs.Partition;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.statemachines.StateMachine;

import java.util.Collection;
import java.util.Iterator;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.StateVertexFacade
 */
public class StateVertexFacadeLogicImpl
        extends StateVertexFacadeLogic
{
    public StateVertexFacadeLogicImpl(org.omg.uml.behavioralelements.statemachines.StateVertex metaObject,
                                      java.lang.String context)
    {
        super(metaObject, context);
    }

    protected Object handleGetStateMachine()
    {
        return metaObject.getContainer().getStateMachine();
    }

    protected Object handleGetContainer()
    {
        return metaObject.getContainer();
    }

    protected Collection handleGetIncoming()
    {
        return metaObject.getIncoming();
    }

    protected Collection handleGetOutgoing()
    {
        return metaObject.getOutgoing();
    }

    public Object getValidationOwner()
    {
        return getStateMachine();
    }

    protected Object handleGetPartition()
    {
        Partition thePartition = null;

        final StateMachine stateMachine = metaObject.getContainer().getStateMachine();
        if (stateMachine instanceof ActivityGraph)
        {
            final ActivityGraph activityGraph = (ActivityGraph)stateMachine;
            final Collection partitions = activityGraph.getPartition();
            for (Iterator partitionIterator = partitions.iterator(); partitionIterator.hasNext();)
            {
                final Partition partition = (Partition)partitionIterator.next();
                if (partition.getContents().contains(metaObject))
                {
                    thePartition = partition;
                    break;
                }
            }
        }

        return thePartition;
    }
}
