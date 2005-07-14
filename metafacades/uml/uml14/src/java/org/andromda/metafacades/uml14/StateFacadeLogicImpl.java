package org.andromda.metafacades.uml14;

import java.util.Collection;


/**
 * Metaclass facade implementation.
 */
public class StateFacadeLogicImpl
    extends StateFacadeLogic
{
    public StateFacadeLogicImpl(org.omg.uml.behavioralelements.statemachines.State metaObject, String context)
    {
        super(metaObject, context);
    }

    protected Collection handleGetDeferrableEvents()
    {
        return metaObject.getDeferrableEvent();
    }
}