package org.andromda.metafacades.uml14;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.TransitionFacade;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndEvent.
 *
 * @see org.andromda.metafacades.uml.FrontEndEvent
 * @author Bob Fields
 */
public class FrontEndEventLogicImpl
    extends FrontEndEventLogic
{

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndEventLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#isContainedInFrontEndUseCase()
     */
    @Override
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getTransition() instanceof FrontEndForward;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#getControllerCall()
     */
    @Override
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
    
    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#getAction()
     */
    @Override
    protected FrontEndAction handleGetAction()
    {
        FrontEndAction action = null;
        TransitionFacade transition = getTransition();
        if (transition instanceof FrontEndAction)
        {
            action = (FrontEndAction)transition;
        }
        return action;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#getControllerCalls()
     */
    @Override
    protected List handleGetControllerCalls()
    {
        Object controllerCall = this.getControllerCall();
        return controllerCall == null ? Collections.emptyList() : Collections.singletonList(controllerCall);
    }

}