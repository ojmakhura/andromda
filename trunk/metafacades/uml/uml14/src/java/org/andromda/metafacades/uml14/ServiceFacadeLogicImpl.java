package org.andromda.metafacades.uml14;

import java.util.Collection;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
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
        Collection refs = new FilteredCollection(this.getDependencies())
        {
            public boolean evaluate(Object object)
            {
                return ((DependencyFacade)object)
                    .hasStereotype(UMLProfile.STEREOTYPE_ENTITY_REF);
            }
        };
        CollectionUtils.transform(refs, new Transformer()
        {
            public Object transform(Object object)
            {
                return ((DependencyFacade)object).getTargetElement();
            }
        });
        return refs;
    }
}