package org.andromda.cartridges.nspring;

import java.util.Collection;
import java.util.HashSet;

import org.andromda.cartridges.nspring.metafacades.SpringService;
import org.andromda.metafacades.uml.Service;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;


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
        CollectionUtils.forAllDo(
            services,
            new Closure()
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

    /**
     * Indicates if any remote EJBs are present in the collection
     * of services.
     *
     * @param services the collection of services to check.
     * @return true/false.
     */
    public boolean remoteEjbsPresent(final Collection services)
    {
        boolean present = services != null && !services.isEmpty();
        if (present)
        {
            present =
                CollectionUtils.find(
                    services,
                    new Predicate()
                    {
                        public boolean evaluate(final Object object)
                        {
                            boolean valid = false;
                            if (object instanceof SpringService)
                            {
                                final SpringService service = (SpringService)object;
                                valid = service.isEjbRemoteView();
                            }
                            return valid;
                        }
                    }) != null;
        }
        return present;
    }

    /**
     * Indicates if any local EJBs are present in the collection
     * of services.
     *
     * @param services the collection of services to check.
     * @return true/false.
     */
    public boolean localEjbsPresent(final Collection services)
    {
        boolean present = services != null && !services.isEmpty();
        if (present)
        {
            present =
                CollectionUtils.find(
                    services,
                    new Predicate()
                    {
                        public boolean evaluate(final Object object)
                        {
                            boolean valid = false;
                            if (object instanceof SpringService)
                            {
                                final SpringService service = (SpringService)object;
                                valid = !service.isEjbRemoteView();
                            }
                            return valid;
                        }
                    }) != null;
        }
        return present;
    }

    /**
     * Indicates if any Spring remotable services are present.
     *
     * @param services the collection of services to check.
     * @return true/false.
     */
    public boolean remotableServicesPresent(final Collection services)
    {
        boolean present = services != null && !services.isEmpty();
        if (present)
        {
            present =
                CollectionUtils.find(
                    services,
                    new Predicate()
                    {
                        public boolean evaluate(final Object object)
                        {
                            boolean valid = false;
                            if (object instanceof SpringService)
                            {
                                final SpringService service = (SpringService)object;
                                valid = service.isRemotable();
                            }
                            return valid;
                        }
                    }) != null;
        }
        return present;
    }

    /**
     * Based on the given <code>value</code>, this method will return
     * a formatted Spring property (including the handling of 'null').
     *
     * @param value the value from which to create the spring value.
     * @return the spring value.
     */
    public String getSpringPropertyValue(final String value)
    {
        String propertyValue = "";
        if (value != null)
        {
            if ("null".equalsIgnoreCase(value))
            {
                propertyValue = "<null/>";
            }
            else
            {
                propertyValue = "<value>" + value + "</value>";
            }
        }
        return propertyValue;
    }
}