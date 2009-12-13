package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.uml2.Activity;
import org.eclipse.uml2.State;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.StateFacade.
 *
 * @see org.andromda.metafacades.uml.StateFacade
 */
public class StateFacadeLogicImpl
    extends StateFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public StateFacadeLogicImpl(
        final State metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getDeferrableEvents
     * @see org.andromda.metafacades.uml.StateFacade#getDeferrableEvents()
     */
    protected Collection handleGetDeferrableEvents()
    {
        // UML1.4 Evetns are mapped to UML2 Activity
        // We obtains them through entry, doactivity and exit
        Collection events = new ArrayList();
        Activity entry = this.metaObject.getEntry();
        Activity doActivity = this.metaObject.getDoActivity();
        Activity onExit = this.metaObject.getExit();

        if (entry != null)
        {
            events.add(entry);
        }
        if (doActivity != null)
        {
            events.add(doActivity);
        }
        if (onExit != null)
        {
            events.add(onExit);
        }
        return events;
    }
}