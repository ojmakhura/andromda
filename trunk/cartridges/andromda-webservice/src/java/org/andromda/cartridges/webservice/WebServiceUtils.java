package org.andromda.cartridges.webservice;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.andromda.metafacades.uml.ServiceFacade;

/**
 * Contains utilities used within the WebService cartridge.
 * 
 * @author Chad Brandon
 */
public class WebServiceUtils
{
    /**
     * Retrieves all roles from the given <code>services</code>
     * collection.
     * @param services the collection services.
     * @return all roles from the collection.
     */
    public Collection getAllRoles(Collection services)
    {
        Collection allRoles = new HashSet();
        for (Iterator serviceIt = services.iterator(); serviceIt.hasNext();)
        {
            Object service = serviceIt.next();
            if (ServiceFacade.class.isAssignableFrom(service.getClass()))
            {
                allRoles.addAll(((ServiceFacade)service).getRoles());
            }
        }
        return allRoles;
    }
}
