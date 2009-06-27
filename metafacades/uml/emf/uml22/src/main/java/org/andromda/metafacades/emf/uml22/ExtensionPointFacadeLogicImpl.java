package org.andromda.metafacades.emf.uml22;


import org.eclipse.uml2.uml.ExtensionPoint;
import org.eclipse.uml2.uml.UseCase;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ExtensionPointFacade.
 *
 * @see org.andromda.metafacades.uml.ExtensionPointFacade
 */
public class ExtensionPointFacadeLogicImpl
    extends ExtensionPointFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ExtensionPointFacadeLogicImpl(
        final ExtensionPoint metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ExtensionPointFacade#getUseCase()
     */
    @Override
    protected UseCase handleGetUseCase()
    {
        return this.metaObject.getUseCase();
    }
}
