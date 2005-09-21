package org.andromda.cartridges.jsf.metafacades;

import org.andromda.cartridges.jsf.JSFGlobals;
import org.andromda.cartridges.jsf.JSFUtils;
import org.andromda.metafacades.uml.StateVertexFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFForward.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFForward
 */
public class JSFForwardLogicImpl
    extends JSFForwardLogic
{

    public JSFForwardLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    public String getName()
    {
        StringBuffer name = new StringBuffer(super.getName());
        final Object target = this.getTarget();
        if (target instanceof JSFFinalState)
        {
            name.append(JSFGlobals.USECASE_FORWARD_NAME_SUFFIX);
        }
        return JSFUtils.toWebResourceName(name.toString());
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFForward#getPath()
     */
    protected String handleGetPath()
    {
        String forwardPath = null;
        final StateVertexFacade target = getTarget();
        if (this.isEnteringView())
        {
            forwardPath = ((JSFView)target).getPath();
        }
        else if (this.isEnteringFinalState())
        {
            forwardPath = ((JSFFinalState)target).getPath();
        }

        return forwardPath;        
    }
}