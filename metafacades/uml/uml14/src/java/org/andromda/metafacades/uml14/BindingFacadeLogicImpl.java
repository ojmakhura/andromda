package org.andromda.metafacades.uml14;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.BindingFacade.
 *
 * @see org.andromda.metafacades.uml.BindingFacade
 */
public class BindingFacadeLogicImpl
    extends BindingFacadeLogic
{

    public BindingFacadeLogicImpl (org.omg.uml.foundation.core.Binding metaObject, String context)
    {
        super (metaObject, context);
    }
    /**
     * @see org.andromda.metafacades.uml.BindingFacade#getArguments()
     */
    protected java.util.Collection handleGetArguments()
    {
        return metaObject.getArgument();
    }
}