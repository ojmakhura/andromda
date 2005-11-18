package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.StateVertexFacade.
 *
 * @see org.andromda.metafacades.uml.StateVertexFacade
 */
public class StateVertexFacadeLogicImpl
    extends StateVertexFacadeLogic
{
    public StateVertexFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getOutgoing()
     */
    protected java.util.Collection handleGetOutgoing()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getIncoming()
     */
    protected java.util.Collection handleGetIncoming()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getContainer()
     */
    protected java.lang.Object handleGetContainer()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getPartition()
     */
    protected java.lang.Object handleGetPartition()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getStateMachine()
     */
    protected java.lang.Object handleGetStateMachine()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return getStateMachine();
    }
}