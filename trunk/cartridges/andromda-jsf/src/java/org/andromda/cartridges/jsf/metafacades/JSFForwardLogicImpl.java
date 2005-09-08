package org.andromda.cartridges.jsf.metafacades;

import org.andromda.cartridges.jsf.metafacades.JSFFinalState;
import org.andromda.cartridges.jsf.metafacades.JSFForwardLogic;
import org.andromda.cartridges.jsf.metafacades.JSFView;
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
        return JSFUtils.toWebResourceName(super.getName());
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