package org.andromda.metafacades.uml14;


/**
 * Metaclass facade implementation.
 */
public class ActorFacadeLogicImpl
        extends ActorFacadeLogic
{
    // ---------------- constructor -------------------------------

    public ActorFacadeLogicImpl(org.omg.uml.behavioralelements.usecases.Actor metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getPackage();
    }
}
