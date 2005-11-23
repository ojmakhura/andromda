package org.andromda.core.metafacade;

import java.lang.reflect.Constructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.Introspector;
import org.andromda.core.configuration.Namespaces;
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
     * @param mapping the MetafacadeMapping instance that contains the properties.
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
                final Introspector introspector = Introspector.instance();
                for (final Iterator tterator = propertyGroups.iterator(); tterator.hasNext();)
                {
                    final MetafacadeMapping.PropertyGroup propertyGroup =
                        (MetafacadeMapping.PropertyGroup)tterator.next();
                    for (final Iterator propertyIterator = propertyGroup.getProperties().iterator();
                        propertyIterator.hasNext();)
                    {
                        final MetafacadeMapping.Property property = (MetafacadeMapping.Property)propertyIterator.next();
                        valid = introspector.containsValidProperty(
                                metafacade,
                                property.getName(),
                                property.getValue());
                        if (getLogger().isDebugEnabled())
                        {
                            getLogger().debug(
                                "property '" + property.getName() + "', with value '" + property.getValue() +
                                "' on metafacade '" + metafacade + "', evaluated to --> '" + valid + "'");
                        }

                        // - if the property is invalid, we break out
                        //   of the loop (since we're evaluating with 'AND')
                        if (!valid)
                        {
                            break;
                        }
                    }

                    // - we break on the first true value since
                    //   property groups are evaluated as 'OR'
                    if (valid)
                    {
                        break;
                    }
                }
            }
            catch (final Throwable throwable)
            {
                if (getLogger().isDebugEnabled())
                {
                    getLogger().debug(
                        "An error occured while " + "evaluating properties on metafacade '" + metafacade +
                        "', setting valid to 'false'",
                        throwable);
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
     * Retrieves the inherited mapping class name for the given <code>mapping</code> by traveling 
     * up the inheritance hiearchy to find the first one that has the mapping class name declared.
     *
     * @param mapping the {@link MetafacadeMapping} instance for which we'll retrieve it's mapping class.
     * @return the name of the mapping class.
     */
    public static String getInheritedMappingClassName(final MetafacadeMapping mapping)
    {
        final Class metafacadeClass = mapping.getMetafacadeClass();
        final Collection interfaces = ClassUtils.getAllInterfaces(metafacadeClass);
        final MetafacadeImpls metafacadeImpls = MetafacadeImpls.instance();
        final Map mappingInstances = MetafacadeMappings.getAllMetafacadeMappingInstances();
        String className = null;
        for (final Iterator iterator = interfaces.iterator(); iterator.hasNext() && className == null;)
        {
            final String metafacadeInterface = ((Class)iterator.next()).getName();
            final Class metafacadeImplClass = metafacadeImpls.getMetafacadeImplClass(metafacadeInterface);
            className = (String)mappingInstances.get(metafacadeImplClass);
        }
        if (className == null)
        {
            throw new MetafacadeMappingsException("No mapping class could be found for '" + metafacadeClass.getName() +
                "'");
        }
        return className;
    }
    
    /**
     * Indicates whether or not a metafacade model facade is present within the 
     * given namespace
     * 
     * @param namespace the namespace to check.
     * @return true/false
     */
    public static boolean isMetafacadeModelPresent(final String namespace)
    {
        boolean  present = false;
        if (ClassUtils.isClassOfTypePresent(
            Namespaces.instance().getResourceRoots(namespace),
            ModelAccessFacade.class))
        {
            present = true;
        }
        return present;
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