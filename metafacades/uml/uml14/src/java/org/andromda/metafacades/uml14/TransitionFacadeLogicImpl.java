package org.andromda.metafacades.uml14;

/**
 * Metaclass facade implementation.
 */
public class TransitionFacadeLogicImpl
       extends TransitionFacadeLogic
       implements org.andromda.metafacades.uml.TransitionFacade
{
    // ---------------- constructor -------------------------------
    
    public TransitionFacadeLogicImpl (org.omg.uml.behavioralelements.statemachines.Transition metaObject, String context)
    {
        super (metaObject, context);
    }

    protected Object handleGetEffect()
    {
        return metaObject.getEffect();
    }

    protected Object handleGetSource()
    {
        return metaObject.getSource();
    }

    protected Object handleGetTarget()
    {
        return metaObject.getTarget();
    }

    protected Object handleGetTrigger()
    {
        return metaObject.getTrigger();
    }

    protected Object handleGetGuard()
    {
        return metaObject.getGuard();
    }
}
