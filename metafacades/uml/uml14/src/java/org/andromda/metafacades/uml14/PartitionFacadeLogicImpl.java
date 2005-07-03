package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.statemachines.StateVertex;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.PartitionFacade.
 *
 * @see org.andromda.metafacades.uml.PartitionFacade
 */
public class PartitionFacadeLogicImpl
        extends PartitionFacadeLogic
{
    // ---------------- constructor -------------------------------

    public PartitionFacadeLogicImpl(org.omg.uml.behavioralelements.activitygraphs.Partition metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.PartitionFacade#getActivityGraph()
     */
    protected java.lang.Object handleGetActivityGraph()
    {
        return metaObject.getActivityGraph();
    }

    protected Collection handleGetVertices()
    {
        Collection vertices = new ArrayList();

        final Collection contents = metaObject.getContents();
        for (final Iterator contentIterator = contents.iterator(); contentIterator.hasNext();)
        {
            final Object element = contentIterator.next();
            if (element instanceof StateVertex)
            {
                vertices.add(element);
            }
        }

        return vertices;
    }
}
