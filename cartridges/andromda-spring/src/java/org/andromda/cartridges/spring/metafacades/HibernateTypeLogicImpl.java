package org.andromda.cartridges.spring.metafacades;

import org.andromda.core.mapping.Mappings;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.spring.metafacades.HibernateType.
 * 
 * @see org.andromda.cartridges.spring.metafacades.HibernateType
 */
public class HibernateTypeLogicImpl
    extends HibernateTypeLogic
    implements org.andromda.cartridges.spring.metafacades.HibernateType
{
    // ---------------- constructor -------------------------------

    public HibernateTypeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.HibernateType#getFullyQualifiedHibernateType()
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
        final String propertyName = "hibernateTypeMappingsUri";
        if (this.isConfiguredProperty(propertyName))
        {
            Object property = this.getConfiguredProperty(propertyName);
            String uri = null;
            if (String.class.isAssignableFrom(property.getClass()))
            {
                uri = (String)property;
                if (StringUtils.isNotBlank(uri))
                {
	                try
	                {
	                    mappings = Mappings.getInstance((String)property);
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
            }
            else
            {
                mappings = (Mappings)property;
            }
        }
        return mappings;
    }

}