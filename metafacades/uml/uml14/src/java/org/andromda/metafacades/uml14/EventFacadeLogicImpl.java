package org.andromda.metafacades.uml14;

import java.util.Collection;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.EventFacade
 */
public class EventFacadeLogicImpl
       extends EventFacadeLogic
       implements org.andromda.metafacades.uml.EventFacade
{
    // ---------------- constructor -------------------------------
    
    public EventFacadeLogicImpl (org.omg.uml.behavioralelements.statemachines.Event metaObject, java.lang.String context)
    {
        super (metaObject, context);
    }

    public String getName()
    {
        return metaObject.getName();
    }

    public Collection handleGetParameters()
    {
        return metaObject.getParameter();
    }
}
