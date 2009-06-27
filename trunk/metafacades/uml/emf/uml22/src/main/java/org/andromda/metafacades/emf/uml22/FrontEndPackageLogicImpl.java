package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.ModelElementFacade;

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
     * @see org.andromda.metafacades.uml.FrontEndPackage#getFrontEndUseCases()
     */
    @Override
    protected List<FrontEndUseCase> handleGetFrontEndUseCases()
    {
        final List<FrontEndUseCase> useCases = new ArrayList<FrontEndUseCase>();
        final Collection<ModelElementFacade> ownedElements = this.getOwnedElements();
        for (final Iterator<ModelElementFacade> elementIterator = ownedElements.iterator(); elementIterator.hasNext();)
        {
            final Object object = elementIterator.next();
            if (object instanceof FrontEndUseCase)
            {
                useCases.add((FrontEndUseCase)object);
            }
        }
        return useCases;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndPackage#getFrontEndControllers()
     */
    @Override
    protected List<FrontEndController> handleGetFrontEndControllers()
    {
        final List<FrontEndController> useCases = new ArrayList<FrontEndController>();
        final Collection<ModelElementFacade> ownedElements = this.getOwnedElements();
        for (final Iterator<ModelElementFacade> elementIterator = ownedElements.iterator(); elementIterator.hasNext();)
        {
            final Object object = elementIterator.next();
            if (object instanceof FrontEndController)
            {
                useCases.add((FrontEndController)object);
            }
        }
        return useCases;
    }
}
