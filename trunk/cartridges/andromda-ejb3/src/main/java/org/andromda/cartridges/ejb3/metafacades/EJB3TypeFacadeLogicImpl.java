package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.core.common.ExceptionRecorder;
import org.andromda.metafacades.uml.TypeMappings;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3TypeFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3TypeFacade
 */
public class EJB3TypeFacadeLogicImpl
    extends EJB3TypeFacadeLogic
{

    public EJB3TypeFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3TypeFacade#getFullyQualifiedEJB3Type()
     */
    protected java.lang.String handleGetFullyQualifiedEJB3Type()
    {
        String fullyQualifiedName = super.getFullyQualifiedName();
        final TypeMappings mappings = this.getEJB3TypeMappings();
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
     * Gets the <code>hibernateTypeMappings</code> for this ejb3/hibernate type.
     *
     * @return the hibernate type TypeMappings.
     */
    protected TypeMappings getEJB3TypeMappings()
    {
        TypeMappings mappings = null;
        final String propertyName = "ejb3TypeMappingsUri";
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