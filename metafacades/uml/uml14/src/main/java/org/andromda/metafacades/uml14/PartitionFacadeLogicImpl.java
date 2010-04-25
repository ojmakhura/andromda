package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.activitygraphs.Partition;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.PartitionFacade.
 *
 * @see org.andromda.metafacades.uml.PartitionFacade
 * @author Bob Fields
 */
public class PartitionFacadeLogicImpl
        extends PartitionFacadeLogic
{
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