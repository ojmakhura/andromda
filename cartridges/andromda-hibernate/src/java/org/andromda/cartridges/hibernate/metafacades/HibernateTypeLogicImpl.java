package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.core.mapping.Mappings;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateType.
 * 
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateType
 */
public class HibernateTypeLogicImpl
    extends HibernateTypeLogic
    implements org.andromda.cartridges.hibernate.metafacades.HibernateType
{
    // ---------------- constructor -------------------------------

    public HibernateTypeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Override to provide retrieval of hibernate type mappings (if available).
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        String fullyQualifiedName = super.getFullyQualifiedName();
        Mappings mappings = this.getHibernateTypeMappings();
        if (mappings != null)
        {
            fullyQualifiedName = mappings.getTo(fullyQualifiedName);
        }
        return fullyQualifiedName;
    }

    private static final String HIBERNATE_TYPE_MAPPINGS = "hibernateTypeMappings";

    /**
     * Sets the <code>hibernateTypeMappingsUri</code> for this hibernate type.
     * 
     * @param hibernateTypeMappingsUri URI to the hibernate type mappings file.
     */
    public void setHibernateTypeMappingsUri(String hibernateTypeMappingsUri)
    {
        this.registerConfiguredProperty(HIBERNATE_TYPE_MAPPINGS, Mappings
            .getInstance(hibernateTypeMappingsUri));
    }

    /**
     * Gets the <code>hibernateTypeMappings</code> for this hibernate type.
     * 
     * @return the hibernate type Mappings.
     */
    protected Mappings getHibernateTypeMappings()
    {
        Mappings mappings = null;
        if (this.isConfiguredProperty(HIBERNATE_TYPE_MAPPINGS))
        {
            mappings = (Mappings)this
                .getConfiguredProperty(HIBERNATE_TYPE_MAPPINGS);
        }
        return mappings;
    }

}