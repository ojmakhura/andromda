package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.State;


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
     * @see org.andromda.metafacades.uml.StateFacade#getDeferrableEvents()
     */
    @Override
    protected Collection<Behavior> handleGetDeferrableEvents()
    {
        // UML1.4 Events are mapped to UML2 Activity
        // We obtains them through entry, doactivity and exit
        Collection<Behavior> events = new ArrayList();
        Behavior entry = this.metaObject.getEntry();
        Behavior doActivity = this.metaObject.getDoActivity();
        Behavior onExit = this.metaObject.getExit();

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
