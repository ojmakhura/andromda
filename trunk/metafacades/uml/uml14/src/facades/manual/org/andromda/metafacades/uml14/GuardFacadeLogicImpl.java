package org.andromda.metafacades.uml14;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.GuardFacade
 */
public class GuardFacadeLogicImpl
       extends GuardFacadeLogic
       implements org.andromda.metafacades.uml.GuardFacade
{
    // ---------------- constructor -------------------------------
    
    public GuardFacadeLogicImpl (org.omg.uml.behavioralelements.statemachines.Guard metaObject, java.lang.String context)
    {
        super (metaObject, context);
    }
    // ------------- relations ------------------

    protected Object handleGetTransition()
    {
        return metaObject.getTransition();
    }
}
