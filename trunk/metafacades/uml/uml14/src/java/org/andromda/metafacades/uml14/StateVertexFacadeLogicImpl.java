package org.andromda.metafacades.uml14;

import java.util.Collection;

import org.andromda.core.common.StringUtilsHelper;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.StateVertexFacade
 */
public class StateVertexFacadeLogicImpl
       extends StateVertexFacadeLogic
       implements org.andromda.metafacades.uml.StateVertexFacade
{
    // ---------------- constructor -------------------------------
    
    public StateVertexFacadeLogicImpl (org.omg.uml.behavioralelements.statemachines.StateVertex metaObject, java.lang.String context)
    {
        super (metaObject, context);
    }
    
    /**
     * This method is overridden to make sure the name 
     * will <strong>not</strong> result in uncompilable Java code.
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    public String getName()
    {
        return StringUtilsHelper.toJavaClassName(super.getName());
    }

    protected Object handleGetActivityGraph()
    {
        return metaObject.getContainer().getStateMachine();
    }

    protected Collection handleGetIncoming()
    {
        return metaObject.getIncoming();
    }

    protected Collection handleGetOutgoing()
    {
        return metaObject.getOutgoing();
    }
}
