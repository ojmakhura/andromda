package org.andromda.cartridges.spring;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.andromda.cartridges.spring.metafacades.SpringCriteriaAttributeLogic;
import org.andromda.cartridges.spring.metafacades.SpringCriteriaSearch;
import org.andromda.cartridges.spring.metafacades.SpringCriteriaSearchLogicImpl;
import org.andromda.cartridges.spring.metafacades.SpringService;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.EntityAttribute;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.EnumerationLiteralFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.Service;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * Contains utilities used within the Spring cartridge.
 *
 * @author Chad Brandon
 * @author Joel Kozikowski
 */
public class SpringUtils
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(SpringUtils.class);


    /**
     * Indicates if any remote EJBs are present in the collection
     * of services.
     *
     * @param services the collection of services to check.
     * @return true/false.
     */
    public boolean remoteEjbsPresent(final Collection<Service> services)
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
    public boolean localEjbsPresent(final Collection<Service> services)
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
                                valid = service.isEjbLocalView();
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
    public boolean remotableServicesPresent(final Collection<Service> services)
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
     * Indicates if any remotable services using Lingo are present.
     *
     * @param services the collection of services to check.
     * @return true/false.
     */
    public boolean lingoRemotableServicesPresent(final Collection<Service> services)
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
                                    valid = service.isRemotingTypeLingo();
                                }
                                return valid;
                            }
                        }) != null;
        }
        return present;
    }

    /**
     * Indicates if any private services are present.
     *
     * @param services the collection of services to check.
     * @return true/false.
     */
    public boolean privateServicesPresent(final Collection<Service> services)
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
                                valid = service.isPrivate();
                            }
                            return valid;
                        }
                    }) != null;
        }
        return present;
    }

    /**
     * Indicates if any public (non private) services are present.
     *
     * @param services the collection of services to check.
     * @return true/false.
     */
    public boolean publicServicesPresent(final Collection<Service> services)
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
                                valid = !service.isPrivate();
                            }
                            return valid;
                        }
                    }) != null;
        }
        return present;
    }

    public boolean isCriteria(ClassifierFacade facade) { 
        return facade instanceof SpringCriteriaSearch;
    }
}