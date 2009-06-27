package org.andromda.metafacades.emf.uml22;


import java.util.List;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.ExtensionPoint;
import org.eclipse.uml2.uml.UseCase;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ExtendFacade.
 *
 * @see org.andromda.metafacades.uml.ExtendFacade
 */
public class ExtendFacadeLogicImpl
    extends ExtendFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ExtendFacadeLogicImpl(
        final Extend metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ExtendFacade#getBase()
     */
    @Override
    protected UseCase handleGetBase()
    {
        return this.metaObject.getExtendedCase();
    }

    /**
     * @see org.andromda.metafacades.uml.ExtendFacade#getExtensionPoints()
     */
    @Override
    protected List<ExtensionPoint> handleGetExtensionPoints()
    {
        return this.metaObject.getExtensionLocations();
    }

    /**
     * @see org.andromda.metafacades.uml.ExtendFacade#getExtension()
     */
    @Override
    protected UseCase handleGetExtension()
    {
        return this.metaObject.getExtension();
    }
}
