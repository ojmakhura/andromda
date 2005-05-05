package org.andromda.metafacades.uml14;



/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ExtendFacade.
 *
 * @see org.andromda.metafacades.uml.ExtendFacade
 */
public class ExtendFacadeLogicImpl
    extends ExtendFacadeLogic
{
    public ExtendFacadeLogicImpl (org.omg.uml.behavioralelements.usecases.Extend metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ExtendFacade#handleGetBase()
     */
    protected Object handleGetBase()
    {
        return metaObject.getBase();
    }

    /**
     * @see org.andromda.metafacades.uml.ExtendFacade#getExtensionPoints()
     */
    protected java.util.List handleGetExtensionPoints()
    {
        return metaObject.getExtensionPoint();
    }

    /**
     * @see org.andromda.metafacades.uml.ExtendFacade#handleGetExtension()
     */
    protected Object handleGetExtension()
    {
        return metaObject.getExtension();
    }
}