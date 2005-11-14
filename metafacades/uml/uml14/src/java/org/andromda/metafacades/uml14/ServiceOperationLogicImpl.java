package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.Service;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ServiceOperationFacade.
 *
 * @see org.andromda.metafacades.uml.ServiceOperationFacade
 */
public class ServiceOperationLogicImpl
    extends ServiceOperationLogic
{
    // ---------------- constructor -------------------------------
    public ServiceOperationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceOperationFacade#getRoles()
     */
    public java.util.Collection handleGetRoles()
    {
        final Collection roles = new LinkedHashSet();
        if (this.getOwner() instanceof Service)
        {
            roles.addAll(((Service)this.getOwner()).getRoles());
        }
        Collection operationRoles = this.getTargetDependencies();
        CollectionUtils.filter(
            operationRoles,
            new Predicate()
            {
                public boolean evaluate(Object object)
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
                public Object transform(Object object)
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
                public void execute(Object object)
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
     * @see org.andromda.metafacades.uml.ServiceOperationFacade#handleGetService()
     */
    protected Object handleGetService()
    {
        Service owner = null;
        if (this.getOwner() instanceof Service)
        {
            owner = (Service)this.getOwner();
        }
        return owner;
    }
}