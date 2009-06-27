package org.andromda.metafacades.emf.uml22;

import java.util.Collection;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Vertex;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.PartitionFacade.
 *
 * @see org.andromda.metafacades.uml.PartitionFacade
 */
public class PartitionFacadeLogicImpl
    extends PartitionFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public PartitionFacadeLogicImpl(
        final Region metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.PartitionFacade#getActivityGraph()
     */
    @Override
    protected StateMachine handleGetActivityGraph()
    {
        Element owner = this.metaObject.getOwner();
        if (owner instanceof StateMachine)
        {
            return (StateMachine)owner;
        }
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.PartitionFacade#getVertices()
     */
    @Override
    protected Collection<Vertex> handleGetVertices()
    {
        return this.metaObject.getSubvertices();
    }
}
