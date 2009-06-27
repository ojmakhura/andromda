package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ExtendFacade.
 *
 * @see org.andromda.metafacades.uml.ExtendFacade
 */
public class ExtendFacadeLogicImpl
    extends ExtendFacadeLogic
{
    public ExtendFacadeLogicImpl(
        final org.eclipse.uml2.Extend metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ExtendFacade#getBase()
     */
    protected java.lang.Object handleGetBase()
    {
        return this.metaObject.getExtendedCase();
    }

    /**
     * @see org.andromda.metafacades.uml.ExtendFacade#getExtensionPoints()
     */
    protected java.util.List handleGetExtensionPoints()
    {
        return this.metaObject.getExtensionLocations();
    }

    /**
     * @see org.andromda.metafacades.uml.ExtendFacade#getExtension()
     */
    protected java.lang.Object handleGetExtension()
    {
        return this.metaObject.getExtension();
    }
}