package org.andromda.metafacades.emf.uml22;

import java.util.Collection;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateParameterSubstitution;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.BindingFacade.
 *
 * @see org.andromda.metafacades.uml.BindingFacade
 */
public class BindingFacadeLogicImpl
    extends BindingFacadeLogic
{
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
     * @see org.andromda.metafacades.uml.BindingFacade#getArguments()
     */
    @Override
    protected Collection<TemplateParameterSubstitution> handleGetArguments()
    {
        return this.metaObject.getParameterSubstitutions();
    }
}
