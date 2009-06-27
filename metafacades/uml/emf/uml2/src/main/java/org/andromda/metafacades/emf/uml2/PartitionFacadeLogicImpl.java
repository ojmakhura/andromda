package org.andromda.metafacades.emf.uml2;

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
    public PartitionFacadeLogicImpl(
        final org.eclipse.uml2.Region metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.PartitionFacade#getActivityGraph()
     */
    protected java.lang.Object handleGetActivityGraph()
    {
        Element owner = this.metaObject.getOwner();
        if (owner instanceof StateMachine)
        {
            return owner;
        }
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.PartitionFacade#getVertices()
     */
    protected java.util.Collection handleGetVertices()
    {
        return this.metaObject.getSubvertices();
    }
}