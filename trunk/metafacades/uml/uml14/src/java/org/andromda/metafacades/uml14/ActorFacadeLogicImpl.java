package org.andromda.metafacades.uml14;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class ActorFacadeLogicImpl
       extends ActorFacadeLogic
       implements org.andromda.metafacades.uml.ActorFacade
{
    // ---------------- constructor -------------------------------
    
    public ActorFacadeLogicImpl (org.omg.uml.behavioralelements.usecases.Actor metaObject, String context)
    {
        super (metaObject, context);
    }
}
