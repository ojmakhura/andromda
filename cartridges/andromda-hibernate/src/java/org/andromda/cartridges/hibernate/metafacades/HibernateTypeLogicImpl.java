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

    /**
     * Gets the <code>hibernateTypeMappings</code> for this hibernate type.
     * 
     * @return the hibernate type Mappings.
     */
    protected Mappings getHibernateTypeMappings()
    {
        Mappings mappings = null;
        final String propertyName = "hibernateTypeMappings";
        if (this.isConfiguredProperty(propertyName))
        {
            Object property = this.getConfiguredProperty(propertyName);
            String uri = null;
            if (String.class.isAssignableFrom(property.getClass()))
            {
                uri = (String)property;
                try
                {
                    mappings = Mappings.getInstance(uri);
                    this.setProperty(propertyName, mappings);
                }
                catch (Throwable th)
                {
                    String errMsg = "Error getting '" + propertyName
                        + "' --> '" + uri + "'";
                    logger.error(errMsg, th);
                    //don't throw the exception
                }
            }
            else
            {
                mappings = (Mappings)property;
            }
        }
        return mappings;
    }

}