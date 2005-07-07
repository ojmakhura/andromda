package org.andromda.metafacades.uml14;

import java.lang.reflect.Method;

import org.andromda.metafacades.uml.FrontEndForward;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndEvent.
 *
 * @see org.andromda.metafacades.uml.FrontEndEvent
 */
public class FrontEndEventLogicImpl
    extends FrontEndEventLogic
{

    public FrontEndEventLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#isContainedInFrontEndUseCase()
     */
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getTransition() instanceof FrontEndForward;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#getControllerCall()
     */
    protected Object handleGetControllerCall()
    {
        // - hack until a solution is found to workaround the JMI multiple inheritance (through interfaces)
        try
        {
            final Method method = metaObject.getClass().getMethod("getOperation", (Class[])null);
            return method.invoke(metaObject, (Object[])null);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

}