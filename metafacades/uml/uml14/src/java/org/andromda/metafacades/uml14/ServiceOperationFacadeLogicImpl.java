package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.HashSet;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.RoleFacade;
import org.andromda.metafacades.uml.ServiceFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ServiceOperationFacade.
 * 
 * @see org.andromda.metafacades.uml.ServiceOperationFacade
 */
public class ServiceOperationFacadeLogicImpl
    extends ServiceOperationFacadeLogic
{
    // ---------------- constructor -------------------------------

    public ServiceOperationFacadeLogicImpl(
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
        Collection roles = new HashSet();
        if (this.getOwner() != null
            && ServiceFacade.class.isAssignableFrom(this.getOwner().getClass()))
        {
            roles.addAll(((ServiceFacade)this.getOwner()).getRoles());
        }
        Collection operationRoles = this.getTargetDependencies();
        CollectionUtils.filter(operationRoles, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                DependencyFacade dependency = (DependencyFacade)object;
                return dependency != null
                    && dependency.getSourceElement() != null
                    && RoleFacade.class.isAssignableFrom(dependency
                        .getSourceElement().getClass());
            }
        });
        CollectionUtils.transform(operationRoles, new Transformer()
        {
            public Object transform(Object object)
            {
                return ((DependencyFacade)object).getSourceElement();
            }
        });
        roles.addAll(operationRoles);
        final Collection allRoles = new HashSet(roles);
        // add all roles which are specializations of this one
        CollectionUtils.forAllDo(
            roles,
            new Closure()
            {
                public void execute(Object object)
                {
                    allRoles.addAll(((RoleFacade)object).getSpecializations());
                }
            });
        return allRoles;
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceOperationFacade#getTransactionType()
     */
    public String handleGetTransactionType()
    {
        Object value = this
            .findTaggedValue(UMLProfile.TAGGEDVALUE_TRANSACTION_TYPE);
        return (String)value;
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceOperationFacade#handleGetService()
     */
    protected Object handleGetService()
    {
        ServiceFacade owner = null;
        if (this.getOwner() instanceof ServiceFacade)
        {
            owner = (ServiceFacade)this.getOwner();
        }
        return owner;
    }

}