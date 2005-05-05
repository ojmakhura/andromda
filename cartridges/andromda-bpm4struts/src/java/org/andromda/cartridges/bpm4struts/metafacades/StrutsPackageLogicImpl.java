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
    // ---------------- constructor -------------------------------

    public StrutsPackageLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected java.util.List handleGetStrutsUseCases()
    {
        final List useCases = new ArrayList();

        Collection ownedElements = getOwnedElements();
        for (Iterator elementIterator = ownedElements.iterator(); elementIterator.hasNext();)
        {
            Object object = elementIterator.next();
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
        for (Iterator elementIterator = ownedElements.iterator(); elementIterator.hasNext();)
        {
            Object object = elementIterator.next();
            if (object instanceof StrutsController)
            {
                useCases.add(object);
            }
        }

        return useCases;
    }

}
