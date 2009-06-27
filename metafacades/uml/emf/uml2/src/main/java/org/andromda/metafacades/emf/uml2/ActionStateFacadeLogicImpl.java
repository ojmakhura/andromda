package org.andromda.metafacades.emf.uml2;

import java.util.Iterator;

import org.eclipse.uml2.Action;
import org.eclipse.uml2.Activity;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ActionStateFacade.
 *
 * @see org.andromda.metafacades.uml.ActionStateFacade
 */
public class ActionStateFacadeLogicImpl
    extends ActionStateFacadeLogic
{
    public ActionStateFacadeLogicImpl(
        final org.eclipse.uml2.State metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * UML1.4 Entry is an Action, where as in UML2 it's an activity. We have
     * then to return the first action of the activity.
     *
     * @see org.andromda.metafacades.uml.ActionStateFacade#getEntry()
     */
    protected java.lang.Object handleGetEntry()
    {
        Activity activity = this.metaObject.getEntry();
        if (activity != null)
        {
            for (Iterator nodesIt = activity.getNodes().iterator(); nodesIt.hasNext();)
            {
                Object nextNode = nodesIt.next();
                if (nextNode instanceof Action)
                {
                    return nextNode;
                }
            }
        }

        // No action has been found.
        return null;
    }
}