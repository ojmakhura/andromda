package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.Destination;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.Service;
import org.andromda.metafacades.uml.ServiceOperation;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.Service.
 *
 * @see org.andromda.metafacades.uml.Service
 */
public class ServiceLogicImpl extends ServiceLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ServiceLogicImpl(final Object metaObject, final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.Service#getServiceReferences()
     */
    @Override
    protected Collection<DependencyFacade> handleGetServiceReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
            {
                @Override
                public boolean evaluate(final Object object)
                {
                    ModelElementFacade targetElement = ((DependencyFacade)object).getTargetElement();
                    return targetElement != null && Service.class.isAssignableFrom(targetElement.getClass());
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.Service#getEntityReferences()
     */
    @Override
    protected Collection<DependencyFacade> handleGetEntityReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
            {
                @Override
                public boolean evaluate(final Object object)
                {
                    ModelElementFacade targetElement = ((DependencyFacade)object).getTargetElement();
                    return targetElement != null && Entity.class.isAssignableFrom(targetElement.getClass());
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.Service#getAllServiceReferences()
     */
    @Override
    public Collection<DependencyFacade> handleGetAllServiceReferences()
    {
        final Collection<DependencyFacade> result = new ArrayList<DependencyFacade>();

        // get references of the service itself
        result.addAll(this.getServiceReferences());

        // get references of all super classes
        CollectionUtils.forAllDo(
            this.getAllGeneralizations(),
            new Closure()
            {
                public void execute(final Object object)
                {
                    Service service = (Service)object;
                    result.addAll(service.getServiceReferences());
                }
            });
        return result;
    }

    /**
     * @see org.andromda.metafacades.uml.Service#getRoles()
     */
    @Override
    protected Collection<Role> handleGetRoles()
    {
        final Collection<DependencyFacade> roles = new ArrayList<DependencyFacade>(this.getTargetDependencies());
        CollectionUtils.filter(
            roles,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    DependencyFacade dependency = (DependencyFacade)object;
                    return dependency != null && dependency.getSourceElement() instanceof Role;
                }
            });
        CollectionUtils.transform(
            roles,
            new Transformer()
            {
                public Object transform(final Object object)
                {
                    return ((DependencyFacade)object).getSourceElement();
                }
            });

        final Collection allRoles = new LinkedHashSet(roles);

        // add all roles which are generalizations of this one
        CollectionUtils.forAllDo(
            roles,
            new Closure()
            {
                public void execute(final Object object)
                {
                    allRoles.addAll(((Role)object).getAllSpecializations());
                }
            });
        return allRoles;
    }

    /**
     * @see org.andromda.metafacades.uml.Service#getAllRoles()
     */
    @Override
    protected Collection<Role> handleGetAllRoles()
    {
        final Collection<Role> roles = new LinkedHashSet<Role>(this.getRoles());
        CollectionUtils.forAllDo(
            this.getOperations(),
            new Closure()
            {
                public void execute(final Object object)
                {
                    if (object instanceof ServiceOperation)
                    {
                        roles.addAll(((ServiceOperation)object).getRoles());
                    }
                }
            });
        return roles;
    }
    
    /**
     * @see org.andromda.metafacades.uml.Service#getMessagingDestinations()
     */
    @Override
    protected Collection<Destination> handleGetMessagingDestinations()
    {
        final Set<Destination> destinations = new LinkedHashSet<Destination>();
        CollectionUtils.forAllDo(this.getOperations(), new Closure()
        {
            public void execute(Object object)
            {
                if (object instanceof ServiceOperation)
                {
                    final ServiceOperation operation = (ServiceOperation)object;
                    if (operation.isIncomingMessageOperation())
                    {
                        destinations.add(operation.getIncomingDestination());
                    }
                    else if (operation.isOutgoingMessageOperation())
                    {
                        destinations.add(operation.getOutgoingDestination());
                    }
                }
            }
        });
        return destinations;
    }
    
    /**
     * @see org.andromda.metafacades.uml.Service#getAllEntityReferences()
     */
    @Override
    protected Collection<DependencyFacade> handleGetAllEntityReferences()
    {
        final Collection<DependencyFacade> result = new LinkedHashSet<DependencyFacade>();

        // get references of the service itself
        result.addAll(this.getEntityReferences());

        // get references of all super classes
        CollectionUtils.forAllDo(this.getAllGeneralizations(), new Closure()
        {
            public void execute(Object object)
            {
                if (object instanceof Service)
                {
                    final Service service = (Service)object;
                    result.addAll(service.getEntityReferences());
                }
            }
        });
        return result;
    }

    /**
     * @see org.andromda.metafacades.uml.Service#getAllMessagingDestinations()
     */
    @Override
    protected Collection<Destination> handleGetAllMessagingDestinations()
    {
        final Collection<Destination> result = new LinkedHashSet<Destination>();

        // get references of the service itself
        result.addAll(this.getMessagingDestinations());

        // get references of all super classes
        CollectionUtils.forAllDo(this.getAllGeneralizations(), new Closure()
        {
            public void execute(Object object)
            {
                if (object instanceof Service)
                {
                    final Service service = (Service)object;
                    result.addAll(service.getMessagingDestinations());
                }
            }

        });
        return result;
    }
}
