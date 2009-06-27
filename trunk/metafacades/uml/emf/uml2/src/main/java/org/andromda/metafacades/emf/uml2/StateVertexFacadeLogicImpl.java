package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.uml2.Element;
import org.eclipse.uml2.StateMachine;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.StateVertexFacade.
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
    public StateVertexFacadeLogicImpl(
        final org.eclipse.uml2.Vertex metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getOutgoings()
     */
    protected Collection handleGetOutgoings()
    {
        ArrayList outList = new ArrayList();
        outList.addAll(this.metaObject.getOutgoings());
        return outList;
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getIncomings()
     */
    protected Collection handleGetIncomings()
    {
        ArrayList inList = new ArrayList();
        inList.addAll(this.metaObject.getIncomings());
        return inList;
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getContainer()
     */
    protected Object handleGetContainer()
    {
        //TODO: What's this ?
        return this.metaObject.getContainer().getNamespace();
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getPartition()
     */
    protected Object handleGetPartition()
    {
        return this.metaObject.getContainer();
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getStateMachine()
     */
    protected Object handleGetStateMachine()
    {
        Element owner = this.metaObject;
        while (!(owner instanceof StateMachine))
        {
            owner = owner.getOwner();
        }
        return owner;
    }

    public Object getValidationOwner()
    {
        return getStateMachine();
    }
}