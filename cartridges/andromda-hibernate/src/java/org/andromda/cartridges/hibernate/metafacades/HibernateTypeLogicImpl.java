package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.core.mapping.Mappings;
import org.apache.commons.lang.StringUtils;

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
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateType#getFullyQualifiedHibernateType()
     */
    public String handleGetFullyQualifiedHibernateType()
    {
        String fullyQualifiedName = super.getFullyQualifiedName();
        Mappings mappings = this.getHibernateTypeMappings();
        if (mappings != null)
        {
            String fullyQualifiedModelName = super.getFullyQualifiedName(true);
            if (mappings.containsFrom(fullyQualifiedModelName))
            {    
                fullyQualifiedName = mappings.getTo(fullyQualifiedModelName);   
            }
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
        if (StringUtils.isNotBlank(hibernateTypeMappingsUri))
        {
            this.registerConfiguredProperty(HIBERNATE_TYPE_MAPPINGS, Mappings
                .getInstance(hibernateTypeMappingsUri));
        }
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