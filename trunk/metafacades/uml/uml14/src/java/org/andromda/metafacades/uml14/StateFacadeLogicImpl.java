package org.andromda.metafacades.uml14;

import java.util.Collection;
import org.omg.uml.behavioralelements.statemachines.State;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class StateFacadeLogicImpl
    extends StateFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public StateFacadeLogicImpl(State metaObject, String context)
    {
        super(metaObject, context);
    }

    protected Collection handleGetDeferrableEvents()
    {
        return metaObject.getDeferrableEvent();
    }
}