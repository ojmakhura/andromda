package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsPackage.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsPackage
 */
public class StrutsPackageLogicImpl
        extends StrutsPackageLogic
{
    public StrutsPackageLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected boolean handleIsContainsFrontEndUseCase()
    {
        return !getStrutsUseCases().isEmpty();
    }

    protected java.util.List handleGetStrutsUseCases()
    {
        final List useCases = new ArrayList();

        final Collection ownedElements = getOwnedElements();
        for (final Iterator elementIterator = ownedElements.iterator(); elementIterator.hasNext();)
        {
            final Object object = elementIterator.next();
            if (object instanceof StrutsUseCase)
            {
                useCases.add(object);
            }
        }

        return useCases;
    }

    protected java.util.List handleGetStrutsControllers()
    {
        final List useCases = new ArrayList();

        final Collection ownedElements = getOwnedElements();
        for (final Iterator elementIterator = ownedElements.iterator(); elementIterator.hasNext();)
        {
            final Object object = elementIterator.next();
            if (object instanceof StrutsController)
            {
                useCases.add(object);
            }
        }

        return useCases;
    }

}
