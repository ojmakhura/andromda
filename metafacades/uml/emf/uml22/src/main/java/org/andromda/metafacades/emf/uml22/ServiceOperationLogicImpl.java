package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.Destination;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.Service;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ServiceOperation.
 *
 * @see org.andromda.metafacades.uml.ServiceOperation
 */
public class ServiceOperationLogicImpl
    extends ServiceOperationLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ServiceOperationLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceOperation#getRoles()
     */
    @Override
    protected Collection<Role> handleGetRoles()
    {
        // Taken from UML 1.4 Facade
        final Collection roles = new LinkedHashSet();
        if (this.getOwner() instanceof Service)
        {
            roles.addAll(((Service)this.getOwner()).getRoles());
        }
        final Collection<DependencyFacade> operationRoles = new ArrayList<DependencyFacade>(this.getTargetDependencies());
        CollectionUtils.filter(
            operationRoles,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    DependencyFacade dependency = (DependencyFacade)object;
                    return dependency != null && dependency.getSourceElement() != null &&
                    Role.class.isAssignableFrom(dependency.getSourceElement().getClass());
                }
            });
        CollectionUtils.transform(
            operationRoles,
            new Transformer()
            {
                public Object transform(final Object object)
                {
                    return ((DependencyFacade)object).getSourceElement();
                }
            });
        roles.addAll(operationRoles);
        final Collection allRoles = new LinkedHashSet(roles);

        // add all roles which are specializations of this one
        CollectionUtils.forAllDo(
            roles,
            new Closure()
            {
                public void execute(final Object object)
                {
                    if (object instanceof Role)
                    {
                        allRoles.addAll(((Role)object).getAllSpecializations());
                    }
                }
            });
        return allRoles;
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceOperation#getService()
     */
    @Override
    protected Service handleGetService()
    {
        Service owner = null;
        if (this.getOwner() instanceof Service)
        {
            owner = (Service)this.getOwner();
        }
        return owner;
    }
    
    /**
     * @see org.andromda.metafacades.uml.ServiceOperation#isMessageOperation()
     */
    @Override
    public boolean handleIsMessageOperation()
    {
        return this.isIncomingMessageOperation() || this.isOutgoingMessageOperation();
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceOperation#isIncomingMessageOperation()
     */
    @Override
    public boolean handleIsIncomingMessageOperation()
    {
        return this.getIncomingDestination() != null;
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceOperation#isOutgoingMessageOperation()
     */
    @Override
    public boolean handleIsOutgoingMessageOperation()
    {
        return this.getOutgoingDestination() != null;
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceOperation#getIncomingDestination()
     */
    @Override
    public ModelElementFacade handleGetIncomingDestination()
    {
        final Collection<DependencyFacade> dependencies = this.getTargetDependencies();
        final DependencyFacade dependency = (DependencyFacade)
            CollectionUtils.find(dependencies, 
                new Predicate() {
    
                    public boolean evaluate(Object object)
                    {
                        return ((DependencyFacade)object).getSourceElement() instanceof Destination;
                    }});
        return (dependency != null ? dependency.getSourceElement() : null);
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceOperation#getOutgoingDestination()
     */
    @Override
    public Destination handleGetOutgoingDestination()
    {
        final Collection<DependencyFacade> dependencies = this.getSourceDependencies();
        final DependencyFacade dependency = (DependencyFacade)
            CollectionUtils.find(dependencies, 
                new Predicate() {
    
                    public boolean evaluate(Object object)
                    {
                        return ((DependencyFacade)object).getTargetElement() instanceof Destination;
                    }});
        return (Destination)(dependency != null ? dependency.getTargetElement() : null);
    }
}
