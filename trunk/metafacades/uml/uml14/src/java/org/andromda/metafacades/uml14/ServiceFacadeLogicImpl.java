package org.andromda.metafacades.uml14;

import java.util.Collection;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.UMLProfile;

/**
 * Metaclass facade implementation.
 */
public class ServiceFacadeLogicImpl
    extends ServiceFacadeLogic
    implements org.andromda.metafacades.uml.ServiceFacade
{
    // ---------------- constructor -------------------------------

    public ServiceFacadeLogicImpl(
        java.lang.Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceFacade#getEntityReferences()
     */
    public Collection handleGetEntityReferences()
    {
        return new FilteredCollection(this.getDependencies())
        {
            public boolean evaluate(Object object)
            {
                return ((DependencyFacade)object)
                    .hasStereotype(UMLProfile.STEREOTYPE_ENTITY_REF);
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceFacade#getServiceReferences()
     */
    public Collection handleGetServiceReferences()
    {
        return new FilteredCollection(this.getDependencies())
        {
            public boolean evaluate(Object object)
            {
                return ((DependencyFacade)object)
                    .hasStereotype(UMLProfile.STEREOTYPE_SERVICE_REF);
            }
        };
    }
}