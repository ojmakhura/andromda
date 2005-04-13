package org.andromda.metafacades.uml14;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.SubactivityStateFacade.
 *
 * @see org.andromda.metafacades.uml.SubactivityStateFacade
 */
public class SubactivityStateFacadeLogicImpl
        extends SubactivityStateFacadeLogic
{
    // ---------------- constructor -------------------------------

    public SubactivityStateFacadeLogicImpl(org.omg.uml.behavioralelements.activitygraphs.SubactivityState metaObject,
                                           String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.SubactivityStateFacade#getSubmachine()
     */
    protected java.lang.Object handleGetSubmachine()
    {
        return metaObject.getSubmachine();
    }

    /**
     * @see org.andromda.metafacades.uml.SubactivityStateFacade#isDynamic()
     */
    protected boolean handleIsDynamic()
    {
        return metaObject.isDynamic();
    }
}
