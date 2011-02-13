package org.andromda.metafacades.emf.uml22;

import org.eclipse.uml2.uml.Action;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.ConnectableElement;
import org.eclipse.uml2.uml.State;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ActionStateFacade.
 *
 * @see org.andromda.metafacades.uml.ActionStateFacade
 */
public class ActionStateFacadeLogicImpl
    extends ActionStateFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ActionStateFacadeLogicImpl(
        final State metaObject,
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
    @Override
    protected Action handleGetEntry()
    {
        Behavior activity = this.metaObject.getEntry();
        if (activity != null)
        {
            for (ConnectableElement nextNode : activity.getRoles())
            {
                if (nextNode instanceof Action)
                {
                    return (Action) nextNode;
                }
            }
        }

        // No action has been found.
        return null;
    }
}
