package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.core.common.ExceptionRecorder;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.utils.JavaTypeConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateType.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateType
 */
public class HibernateTypeLogicImpl
    extends HibernateTypeLogic
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(HibernateTypeLogicImpl.class);
    
    // ---------------- constructor -------------------------------
    /**
     * @param metaObject
     * @param context
     */
    public HibernateTypeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    @Override
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateType#getFullyQualifiedHibernateType()
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
        fullyQualifiedName = new JavaTypeConverter().getJavaLangTypeName(fullyQualifiedName);
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