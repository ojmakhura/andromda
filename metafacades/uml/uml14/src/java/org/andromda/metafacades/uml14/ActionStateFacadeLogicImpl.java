package org.andromda.metafacades.uml14;

import java.util.Collection;

/**
 * Metaclass facade implementation.
 */
public class ActionStateFacadeLogicImpl
    extends ActionStateFacadeLogic
    implements org.andromda.metafacades.uml.ActionStateFacade
{
    // ---------------- constructor -------------------------------

    public ActionStateFacadeLogicImpl(
        org.omg.uml.behavioralelements.activitygraphs.ActionState metaObject,
        String context)
    {
        super(metaObject, context);
    }

    // ------------- relations ------------------

    protected Object handleGetEntry()
    {
        return metaObject.getEntry();
    }

    protected Collection handleGetDeferrableEvents()
    {
        return metaObject.getDeferrableEvent();
    }
}
