package org.andromda.metafacades.emf.uml2;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.Service;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.Service.
 *
 * @see org.andromda.metafacades.uml.Service
 */
public class ServiceLogicImpl
    extends ServiceLogic
{
    public ServiceLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.Service#getServiceReferences()
     */
    protected java.util.Collection handleGetServiceReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
            {
                public boolean evaluate(Object object)
                {
                    ModelElementFacade targetElement = ((DependencyFacade)object).getTargetElement();
                    return targetElement != null && Service.class.isAssignableFrom(targetElement.getClass());
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.Service#getEntityReferences()
     */
    protected java.util.Collection handleGetEntityReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
            {
                public boolean evaluate(Object object)
                {
                    ModelElementFacade targetElement = ((DependencyFacade)object).getTargetElement();
                    return targetElement != null && Entity.class.isAssignableFrom(targetElement.getClass());
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.Service#getRoles()
     */
    protected java.util.Collection handleGetRoles()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.Service#getAllRoles()
     */
    protected java.util.Collection handleGetAllRoles()
    {
        // TODO: add your implementation here!
        return null;
    }
}