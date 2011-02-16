package org.andromda.metafacades.emf.uml2;

import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ExtendFacade.
 *
 * @see org.andromda.metafacades.uml.ExtendFacade
 */
public class ExtendFacadeLogicImpl
    extends ExtendFacadeLogic
{
    private static final long serialVersionUID = 1221602371642918570L;

    /**
     * @param metaObject
     * @param context
     */
    public ExtendFacadeLogicImpl(
        final org.eclipse.uml2.Extend metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return metaObject.getExtendedCase()
     * @see org.andromda.metafacades.uml.ExtendFacade#getBase()
     */
    protected Object handleGetBase()
    {
        return this.metaObject.getExtendedCase();
    }

    /**
     * @return metaObject.getExtensionLocations()
     * @see org.andromda.metafacades.uml.ExtendFacade#getExtensionPoints()
     */
    protected List handleGetExtensionPoints()
    {
        return this.metaObject.getExtensionLocations();
    }

    /**
     * @return metaObject.getExtension()
     * @see org.andromda.metafacades.uml.ExtendFacade#getExtension()
     */
    protected Object handleGetExtension()
    {
        return this.metaObject.getExtension();
    }
}