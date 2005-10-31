package org.andromda.cartridges.nhibernate.metafacades;

import org.andromda.core.common.ExceptionRecorder;
import org.andromda.metafacades.uml.TypeMappings;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.nhibernate.metafacades.HibernateType.
 *
 * @see org.andromda.cartridges.nhibernate.metafacades.HibernateType
 */
public class HibernateTypeLogicImpl
    extends HibernateTypeLogic
{
    // ---------------- constructor -------------------------------
    public HibernateTypeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.nhibernate.metafacades.HibernateType#getFullyQualifiedHibernateType()
     */
    protected String handleGetFullyQualifiedHibernateType()
    {
        String fullyQualifiedName = super.getFullyQualifiedName();
        final TypeMappings mappings = this.getHibernateTypeMappings();
        if (mappings != null)
        {
            final String fullyQualifiedModelName = super.getFullyQualifiedName(true);
            if (mappings.getMappings().containsFrom(fullyQualifiedModelName))
            {
                fullyQualifiedName = mappings.getTo(fullyQualifiedModelName);
            }
        }
        return fullyQualifiedName;
    }

    /**
     * Gets the <code>hibernateTypeMappings</code> for this hibernate type.
     *
     * @return the hibernate type TypeMappings.
     */
    protected TypeMappings getHibernateTypeMappings()
    {
        TypeMappings mappings = null;
        final String propertyName = "hibernateTypeMappingsUri";
        if (this.isConfiguredProperty(propertyName))
        {
            final Object property = this.getConfiguredProperty(propertyName);
            String uri = null;
            if (property instanceof String)
            {
                uri = (String)property;
                if (StringUtils.isNotBlank(uri))
                {
                    try
                    {
                        mappings = TypeMappings.getInstance((String)property);
                        this.setProperty(propertyName, mappings);
                    }
                    catch (final Throwable throwable)
                    {
                        final String message = "Error getting '" + propertyName + "' --> '" + uri + "'";
                        logger.error(message);

                        // don't throw the exception
                        ExceptionRecorder.instance().record(message, throwable);
                    }
                }
            }
            else
            {
                mappings = (TypeMappings)property;
            }
        }
        return mappings;
    }
}