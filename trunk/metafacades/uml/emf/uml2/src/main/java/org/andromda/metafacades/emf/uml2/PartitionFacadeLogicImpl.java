package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.StateMachine;


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
        final org.eclipse.uml2.Region metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return owner instanceof StateMachine
     * @see org.andromda.metafacades.uml.PartitionFacade#getActivityGraph()
     */
    protected Object handleGetActivityGraph()
    {
        Element owner = this.metaObject.getOwner();
        if (owner instanceof StateMachine)
        {
            return owner;
        }
        return null;
    }

    /**
     * @return metaObject.getSubvertices()
     * @see org.andromda.metafacades.uml.PartitionFacade#getVertices()
     */
    protected Collection handleGetVertices()
    {
        return this.metaObject.getSubvertices();
    }
}