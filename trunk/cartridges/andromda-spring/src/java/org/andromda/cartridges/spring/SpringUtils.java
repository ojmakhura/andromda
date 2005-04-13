package org.andromda.cartridges.spring;

import org.andromda.metafacades.uml.Service;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;

/**
 * Contains utilities used within the Spring cartridge.
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
        final Collection allRoles = new HashSet();
        CollectionUtils.forAllDo(services, new Closure()
        {
            public void execute(Object object)
            {
                if (object != null && Service.class.isAssignableFrom(object.getClass()))
                {
                    allRoles.addAll(((Service)object).getAllRoles());
                }
            }
        });
        return allRoles;
    }
}
