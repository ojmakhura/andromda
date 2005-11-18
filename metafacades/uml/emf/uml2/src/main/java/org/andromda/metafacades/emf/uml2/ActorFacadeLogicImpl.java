package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ActorFacade.
 *
 * @see org.andromda.metafacades.uml.ActorFacade
 */
public class ActorFacadeLogicImpl
    extends ActorFacadeLogic
{
    public ActorFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ActorFacade#getGeneralizedByActors()
     */
    protected java.util.List handleGetGeneralizedByActors()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ActorFacade#getGeneralizedActors()
     */
    protected java.util.List handleGetGeneralizedActors()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getPackage();
    }
}