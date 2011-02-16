package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ExtensionPointFacade.
 *
 * @see org.andromda.metafacades.uml.ExtensionPointFacade
 */
public class ExtensionPointFacadeLogicImpl
    extends ExtensionPointFacadeLogic
{
    private static final long serialVersionUID = -1642749781557802009L;

    /**
     * @param metaObject
     * @param context
     */
    public ExtensionPointFacadeLogicImpl(
        final org.eclipse.uml2.ExtensionPoint metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return metaObject.getUseCase()
     * @see org.andromda.metafacades.uml.ExtensionPointFacade#getUseCase()
     */
    protected Object handleGetUseCase()
    {
        return this.metaObject.getUseCase();
    }
}