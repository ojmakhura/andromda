package org.andromda.metafacades.emf.uml2;

import org.andromda.metafacades.uml.Service;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ServiceOperation.
 *
 * @see org.andromda.metafacades.uml.ServiceOperation
 */
public class ServiceOperationLogicImpl
    extends ServiceOperationLogic
{
    public ServiceOperationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceOperation#getRoles()
     */
    protected java.util.Collection handleGetRoles()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceOperation#getService()
     */
    protected java.lang.Object handleGetService()
    {
        Service owner = null;
        if (this.getOwner() instanceof Service)
        {
            owner = (Service)this.getOwner();
        }
        return owner;
    }
}