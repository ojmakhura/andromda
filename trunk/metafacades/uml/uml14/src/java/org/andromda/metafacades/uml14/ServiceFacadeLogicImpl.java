package org.andromda.metafacades.uml14;

import java.util.Collection;

import org.andromda.metafacades.uml.ActorFacade;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ServiceFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

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
                ModelElementFacade targetElement = ((DependencyFacade)object)
                    .getTargetElement();
                return targetElement != null
                    && EntityFacade.class.isAssignableFrom(targetElement
                        .getClass());
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
                ModelElementFacade targetElement = ((DependencyFacade)object)
                    .getTargetElement();
                return targetElement != null
                    && ServiceFacade.class.isAssignableFrom(targetElement
                        .getClass());
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.ServiceFacade#getRoles()
     */
    protected Collection handleGetRoles()
    {
        Collection roles = this.getAssociationEnds();
        CollectionUtils.filter(roles, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                AssociationEndFacade end = (AssociationEndFacade)object;
                return end != null
                    && end.getOtherEnd().getType() != null
                    && ActorFacade.class.isAssignableFrom(end.getOtherEnd()
                        .getType().getClass());
            }
        });
        CollectionUtils.transform(roles, new Transformer()
        {
            public Object transform(Object object)
            {
                return ((AssociationEndFacade)object).getOtherEnd().getType();
            }
        });
        return roles;
    }
}