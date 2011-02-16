package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.activitygraphs.Partition;
import org.omg.uml.behavioralelements.statemachines.StateVertex;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.PartitionFacade.
 *
 * @see org.andromda.metafacades.uml.PartitionFacade
 * @author Bob Fields
 */
public class PartitionFacadeLogicImpl
        extends PartitionFacadeLogic
{
    private static final long serialVersionUID = 8178073921061583901L;
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public PartitionFacadeLogicImpl(Partition metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.PartitionFacade#getActivityGraph()
     */
    @Override
    protected ActivityGraph handleGetActivityGraph()
    {
        return metaObject.getActivityGraph();
    }

    /**
     * @see org.andromda.metafacades.uml14.PartitionFacadeLogic#handleGetVertices()
     */
    protected Collection<StateVertex> handleGetVertices()
    {
        Collection<StateVertex> vertices = new ArrayList<StateVertex>();

        final Collection contents = metaObject.getContents();
        for (final Iterator contentIterator = contents.iterator(); contentIterator.hasNext();)
        {
            final Object element = contentIterator.next();
            if (element instanceof StateVertex)
            {
                vertices.add((StateVertex)element);
            }
        }

        return vertices;
    }
}