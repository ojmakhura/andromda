package org.andromda.metafacades.uml14;


/**
 * Metaclass facade implementation.
 */
public class ActorFacadeLogicImpl
    extends ActorFacadeLogic
{
    // ---------------- constructor -------------------------------

    public ActorFacadeLogicImpl(
        org.omg.uml.behavioralelements.usecases.Actor metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ActorFacade#getGeneralizedActors()
     */
    protected java.util.Collection handleGetGeneralizedActors()
    {
        return this.getGeneralizations();
    }

    /**
     * @see org.andromda.metafacades.uml.ActorFacadeLogic#getGeneralizedByActors()
     */
    protected java.util.Collection handleGetGeneralizedByActors()
    {
        return this.getSpecializations();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getPackage();
    }
}
