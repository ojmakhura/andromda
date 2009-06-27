package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.commonbehavior.Action;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class ActionStateFacadeLogicImpl
    extends ActionStateFacadeLogic
{
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

    protected Action handleGetEntry()
    {
        return metaObject.getEntry();
    }
}