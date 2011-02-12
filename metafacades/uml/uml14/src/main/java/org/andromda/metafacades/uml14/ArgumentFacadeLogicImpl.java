package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.commonbehavior.Argument;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.ArgumentFacade
 * @author Bob Fields
 */
public class ArgumentFacadeLogicImpl
    extends ArgumentFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ArgumentFacadeLogicImpl(
        Argument metaObject,
        String context)
    {
        super(metaObject, context);
    }
}