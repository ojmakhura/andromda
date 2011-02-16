package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
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
    private static final long serialVersionUID = -261065287055863331L;

    /**
     * @param metaObject
     * @param context
     */
    public BindingFacadeLogicImpl(
        final TemplateBinding metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return metaObject.getParameterSubstitutions()
     * @see org.andromda.metafacades.uml.BindingFacade#getArguments()
     */
    protected Collection handleGetArguments()
    {
        return this.metaObject.getParameterSubstitutions();
    }
}