package org.andromda.core.metafacade;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.common.Introspector;
import org.apache.log4j.Logger;


/**
 * Contains static utility methods for dealing with metafacade instances.
 *
 * @author Chad Brandon
 */
final class MetafacadeUtils
{
    /**
     * Indicates whether or not the mapping properties (present on the mapping, if any) are valid on the
     * <code>metafacade</code>.
     *
     * @param metafacade the metafacade instance on which the properties will be validated.
     * @param mapping    the MetafacadeMapping instance that contains the properties.
     * @return true/false
     */
    static final boolean propertiesValid(
        final MetafacadeBase metafacade,
        final MetafacadeMapping mapping)
    {
        boolean valid = false;
        final Collection propertyGroups = mapping.getMappingPropertyGroups();
        if (propertyGroups != null && !propertyGroups.isEmpty())
        {
            try
            {
                if (getLogger().isDebugEnabled())
                {
                    getLogger().debug(
                        "evaluating " + propertyGroups.size() + " property groups(s) on metafacade '" + metafacade +
                        "'");
                }
                for (Iterator propertyGroupIterator = propertyGroups.iterator(); propertyGroupIterator.hasNext();)
                {
                    final MetafacadeMapping.PropertyGroup propertyGroup =
                        (MetafacadeMapping.PropertyGroup)propertyGroupIterator.next();
                    for (Iterator propertyIterator = propertyGroup.getProperties().iterator();
                        propertyIterator.hasNext();)
                    {
                        final MetafacadeMapping.Property property = (MetafacadeMapping.Property)propertyIterator.next();
                        valid =
                            Introspector.instance().containsValidProperty(
                                metafacade,
                                property.getName(),
                                property.getValue());
                        if (getLogger().isDebugEnabled())
                        {
                            getLogger().debug(
                                "property '" + property.getName() + "', with value '" + property.getValue() +
                                "' on metafacade '" + metafacade + "', evaluated to --> '" + valid + "'");
                        }

                        // if the property is invalid, we break out
                        // of the loop (since we're evaluating with 'AND')
                        if (!valid)
                        {
                            break;
                        }
                    }

                    // we break on the first true value since
                    // property groups are evaluated as 'OR'
                    if (valid)
                    {
                        break;
                    }
                }
            }
            catch (Throwable throwable)
            {
                if (getLogger().isDebugEnabled())
                {
                    getLogger().debug(
                        "An error occured while " + "evaluating properties on metafacade '" + metafacade +
                        "', setting valid to 'false'", throwable);
                }
                valid = false;
            }
            if (getLogger().isDebugEnabled())
            {
                getLogger().debug(
                    "completed evaluating " + propertyGroups.size() + " properties on metafacade '" + metafacade +
                    "' with a result of --> '" + valid + "'");
            }
        }
        return valid;
    }

    /**
     * Constructs a new <code>metafacade</code> from the given
     * <code>metafacadeClass</code> and <code>mappingObject</code>.
     *
     * @param metafacadeClass the metafacade class.
     * @param mappingObject the object to which the metafacade is mapped.
     * @return the new metafacade.
     * @throws Exception if any error occurs during metafacade creation
     */
    static final MetafacadeBase constructMetafacade(
        final Class metafacadeClass,
        final Object mappingObject,
        final String context)
        throws Exception
    {
        if (getLogger().isDebugEnabled())
        {
            getLogger().debug(
                "constructing metafacade from class '" + metafacadeClass + "' mapping object '" + mappingObject +
                "', and context '" + context + "'");
        }
        final Constructor constructor = metafacadeClass.getDeclaredConstructors()[0];
        return (MetafacadeBase)constructor.newInstance(new Object[] {mappingObject, context});
    }

    /**
     * Gets the logger instance which should be used for logging output within this class.
     *
     * @return the logger instance.
     */
    private static final Logger getLogger()
    {
        return MetafacadeFactory.getInstance().getLogger();
    }
}