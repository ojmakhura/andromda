package org.andromda.cartridges.spring;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.andromda.metafacades.uml.ServiceFacade;
import org.andromda.metafacades.uml.ServiceOperationFacade;

/**
 * Contains utilities used within the WebService cartridge.
 * 
 * @author Chad Brandon
 */
public class SpringUtils
{
    /**
     * Retrieves all roles from the given <code>services</code> collection.
     * 
     * @param services the collection services.
     * @return all roles from the collection.
     */
    public Collection getAllRoles(Collection services)
    {
        Collection allRoles = new HashSet();
        for (Iterator serviceIt = services.iterator(); serviceIt.hasNext();)
        {
            Object element = serviceIt.next();
            if (ServiceFacade.class.isAssignableFrom(element.getClass()))
            {
                ServiceFacade service = (ServiceFacade)element;
                allRoles.addAll(service.getRoles());
                for (Iterator operationIt = service.getOperations().iterator(); operationIt
                    .hasNext();)
                {
                    Object operation = operationIt.next();
                    if (ServiceOperationFacade.class.isAssignableFrom(operation
                        .getClass()))
                    {
                        allRoles.addAll(((ServiceOperationFacade)operation)
                            .getRoles());
                    }
                }
            }
        }
        return allRoles;
    }
}
