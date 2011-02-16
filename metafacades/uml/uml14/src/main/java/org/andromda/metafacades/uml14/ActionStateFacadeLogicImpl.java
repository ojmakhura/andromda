package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.commonbehavior.Action;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
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
        ActionState metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml14.ActionStateFacadeLogic#handleGetEntry()
     */
    protected Action handleGetEntry()
    {
        return metaObject.getEntry();
    }
}