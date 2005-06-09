package org.andromda.cartridges.hibernate;

import org.andromda.metafacades.uml.Service;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;

/**
 * Contains utilities used within the Hibernate cartridge.
 *
 * @author Chad Brandon
 */
public class HibernateUtils
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

    public String getHibernatePackage(String version){
        if (version.equals("3"))
        {
            return "org.hibernate";
        }
        else
        {
            return "net.sf.hibernate";
        }
    }
}