package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsPackage.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsPackage
 */
public class StrutsPackageLogicImpl
        extends StrutsPackageLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsPackage
{
    // ---------------- constructor -------------------------------

    public StrutsPackageLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected java.util.Collection handleGetStrutsUseCases()
    {
        Collection useCases = new ArrayList();

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

    protected java.util.Collection handleGetStrutsControllers()
    {
        Collection useCases = new ArrayList();

        Collection ownedElements = getOwnedElements();
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
