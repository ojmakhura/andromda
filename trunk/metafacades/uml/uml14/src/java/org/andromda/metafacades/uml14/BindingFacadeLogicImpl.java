package org.andromda.metafacades.uml14;

import java.util.Collection;
import org.omg.uml.foundation.core.Binding;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.BindingFacade.
 *
 * @see org.andromda.metafacades.uml.BindingFacade
 * @author Bob Fields
 */
public class BindingFacadeLogicImpl
    extends BindingFacadeLogic
{

    /**
     * @param metaObject
     * @param context
     */
    public BindingFacadeLogicImpl (Binding metaObject, String context)
    {
        super (metaObject, context);
    }
    /**
     * @see org.andromda.metafacades.uml.BindingFacade#getArguments()
     */
    @Override
    protected Collection handleGetArguments()
    {
        return metaObject.getArgument();
    }
}