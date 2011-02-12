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
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public StateVertexFacadeLogicImpl(StateVertex metaObject,
                                      String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml14.StateVertexFacadeLogic#handleGetStateMachine()
     */
    protected StateMachine handleGetStateMachine()
    {
        // throws NullPointer if metaObject has no Container... Need to check for null on return.
        if (metaObject.getContainer()==null)
        {
            return null;
        }
        return metaObject.getContainer().getStateMachine();
    }

    /**
     * @see org.andromda.metafacades.uml14.StateVertexFacadeLogic#handleGetContainer()
     */
    protected CompositeState handleGetContainer()
    {
        return metaObject.getContainer();
    }

    /**
     * @see org.andromda.metafacades.uml14.StateVertexFacadeLogic#handleGetIncomings()
     */
    protected Collection handleGetIncomings()
    {
        return metaObject.getIncoming();
    }

    /**
     * @see org.andromda.metafacades.uml14.StateVertexFacadeLogic#handleGetOutgoings()
     */
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

    /**
     * @see org.andromda.metafacades.uml14.StateVertexFacadeLogic#handleGetPartition()
     */
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