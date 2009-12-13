package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndUseCase;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndPackage.
 *
 * @see org.andromda.metafacades.uml.FrontEndPackage
 */
public class FrontEndPackageLogicImpl
    extends FrontEndPackageLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public FrontEndPackageLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getOwnedElements() instanceof FrontEndUseCase
     * @see org.andromda.metafacades.uml.FrontEndPackage#getFrontEndUseCases()
     */
    protected List handleGetFrontEndUseCases()
    {
        final List useCases = new ArrayList();
        final Collection ownedElements = this.getOwnedElements();
        for (final Iterator elementIterator = ownedElements.iterator(); elementIterator.hasNext();)
        {
            final Object object = elementIterator.next();
            if (object instanceof FrontEndUseCase)
            {
                useCases.add(object);
            }
        }
        return useCases;
    }

    /**
     * @return getOwnedElements() instanceof FrontEndController
     * @see org.andromda.metafacades.uml.FrontEndPackage#getFrontEndControllers()
     */
    protected List handleGetFrontEndControllers()
    {
        final List useCases = new ArrayList();
        final Collection ownedElements = this.getOwnedElements();
        for (final Iterator elementIterator = ownedElements.iterator(); elementIterator.hasNext();)
        {
            final Object object = elementIterator.next();
            if (object instanceof FrontEndController)
            {
                useCases.add(object);
            }
        }
        return useCases;
    }
}