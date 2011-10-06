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
    private static final long serialVersionUID = 34L;
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
        // We obtain them through entry, doActivity and exit
        final Collection<Behavior> events = new ArrayList<Behavior>();
        final Behavior entry = this.metaObject.getEntry();
        final Behavior doActivity = this.metaObject.getDoActivity();
        final Behavior onExit = this.metaObject.getExit();

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
