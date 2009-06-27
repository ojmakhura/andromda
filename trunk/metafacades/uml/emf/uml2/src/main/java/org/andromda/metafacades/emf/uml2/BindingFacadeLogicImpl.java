package org.andromda.metafacades.emf.uml2;

import org.eclipse.uml2.TemplateBinding;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.BindingFacade.
 *
 * @see org.andromda.metafacades.uml.BindingFacade
 */
public class BindingFacadeLogicImpl
    extends BindingFacadeLogic
{
    public BindingFacadeLogicImpl(
        final TemplateBinding metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.BindingFacade#getArguments()
     */
    protected java.util.Collection handleGetArguments()
    {
        return this.metaObject.getParameterSubstitutions();
    }
}