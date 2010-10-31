package org.andromda.core.metafacade;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.Introspector;
import org.andromda.core.configuration.Namespaces;
import org.apache.log4j.Logger;

/**
 * Contains static utility methods for dealing with metafacade instances.
 *
 * @author Chad Brandon
 * @author Bob Fields
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
    static boolean propertiesValid(
        final MetafacadeBase metafacade,
        final MetafacadeMapping mapping)
    {
        boolean valid = false;
        final Collection<MetafacadeMapping.PropertyGroup> propertyGroups = mapping.getMappingPropertyGroups();
        if (propertyGroups != null && !propertyGroups.isEmpty())
        {
            try
            {
                if (getLogger().isDebugEnabled())
                {
                    getLogger().debug(
                        "evaluating " + propertyGroups.size() + " property groups(s) on metafacade '" + metafacade +
                                '\'');
                }
                final Introspector introspector = Introspector.instance();
                for (final MetafacadeMapping.PropertyGroup propertyGroup : propertyGroups)
                {
                    for (final MetafacadeMapping.Property property : propertyGroup.getProperties())
                    {
                        valid = introspector.containsValidProperty(
                                metafacade,
                                property.getName(),
                                property.getValue());
                        if (getLogger().isDebugEnabled())
                        {
                            getLogger().debug(
                                "property '" + property.getName() + "', with value '" + property.getValue() +
                                "' on metafacade '" + metafacade + "', evaluated to --> '" + valid + '\'');
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
                    "' with a result of --> '" + valid + '\'');
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
     * @param context
     * @return the new metafacade.
     * @throws Exception if any error occurs during metafacade creation
     */
    static MetafacadeBase constructMetafacade(
        final Class metafacadeClass,
        final Object mappingObject,
        final String context)
        throws Exception
    {
        if (getLogger().isDebugEnabled())
        {
            getLogger().debug(
                "constructing metafacade from class '" + metafacadeClass + "' mapping object '" + mappingObject +
                "', and context '" + context + '\'');
        }
        final Constructor constructor = metafacadeClass.getDeclaredConstructors()[0];
        return (MetafacadeBase)constructor.newInstance(mappingObject, context);
    }

    /**
     * Compute the set of UML implementation class names that are in both parameters sets. If class A1 is in set1 and class A2 is in set2 with A1 extending A2, A1
     * must be on the result set.
     *
     * @param nameSet1 the first set
     * @param nameSet2 the second set
     * @return the intersection of the 2 sets.
     */
    private static Set<String> intersection(Set<String> nameSet1, Set<String> nameSet2)
    {
       Set<String> results = new HashSet<String>();
       if(nameSet1 == null || nameSet2 == null)
       {
           return results;
       }

       Map<Class<?>, String> classesToName = new HashMap<Class<?>, String>();
       Set<Class<?>> classesSet1 = getClassesFromNames(nameSet1, classesToName);
       Set<Class<?>> classesSet2 = getClassesFromNames(nameSet2, classesToName);

       for(Class<?> classToCheck : classesSet1)
       {
           for(Class<?> expectedResult : classesSet2)
           {
               if(classToCheck.isAssignableFrom(expectedResult))
               {
                   results.add(classesToName.get(expectedResult));
               }
               else if(expectedResult.isAssignableFrom(classToCheck))
               {
                   results.add(classesToName.get(classToCheck));
               }
           }
       }
       return results;
    }

    /**
     * Convert a set of class names into a set of class objects. The mapping between the class object and the class name is kept into the classesToName map. If the class name cannot
     * be directly converted in a class, try to remove a prefix starting with a $. This is necessary because implementation classes for UML 1.4 are created dynamically and are not
     * available at the time this method is called, one must then instantiate the class object for the interface of the implementation class.
     *
     * @param nameSet the set of class names.
     * @param classesToName the map that must keep the relation between class object and class names.
     * @return the set of class objects.
     */
    private static Set<Class<?>> getClassesFromNames(Set<String> nameSet, Map<Class<?>, String> classesToName)
    {
        final Set<Class<?>> classesSet = new HashSet<Class<?>>();
        for(final String name : nameSet)
        {
            try
            {
                Class<?> cl = Class.forName(name);
                classesToName.put(cl, name);
                classesSet.add(cl);
            }
            catch (ClassNotFoundException e1)
            {
                // Workaround for UML 1.4, where implementation class, ending with $Impl are created dynamically and are not available at the time this method is called.
                try
                {
                    String instanciatedName = name;
                    if(instanciatedName.endsWith("$Impl"))
                    {
                        instanciatedName = name.substring(0, name.lastIndexOf("$Impl"));
                    }
                    Class<?> cl = Class.forName(instanciatedName);
                    classesToName.put(cl, name);
                    classesSet.add(cl);
                }
                catch (ClassNotFoundException e2)
                {
                    throw new RuntimeException(e2);
                }
            }
        }
        return classesSet;
    }

    /**
     * Retrieves the inherited mapping class names for the given metafacade class by traveling
     * up the inheritance hierarchy to find the ones that have the mapping class name declared.
     *
     * @param metafacadeClass the metafacade class to retrieve the mapping class names.
     * @param metafacadeImpls lookup for metafacade implementation names.
     * @param mappingInstances mapping classes for common metafacades.
     * @return the inherited mapping class names for the given metafacade class.
     */
    private static Set<String> getInheritedMappingClassNames(final Class metafacadeClass, final MetafacadeImpls metafacadeImpls, final Map<Class, Set<String>> mappingInstances )
    {
        Set<String> result = null;
        if(metafacadeClass != null)
        {
            if(metafacadeClass.isInterface())
            {
                result = mappingInstances.get(metafacadeImpls.getMetafacadeImplClass(metafacadeClass.getName()));
            }
            if(result == null)
            {
                for(Class currentInterface : metafacadeClass.getInterfaces())
                {
                    Set<String> subInheritedMappingClassNames = getInheritedMappingClassNames(currentInterface, metafacadeImpls, mappingInstances);
                    if(subInheritedMappingClassNames != null)
                    {
                        if(result == null)
                        {
                            result = subInheritedMappingClassNames;
                        }
                        else
                        {
                            result = intersection(result, subInheritedMappingClassNames);
                        }
                    }
                }
            }
            if(result == null)
            {
                result = getInheritedMappingClassNames(metafacadeClass.getSuperclass(), metafacadeImpls, mappingInstances);
            }
        }
        return result;
    }

    /**
     * Retrieves the inherited mapping class names for the given <code>mapping</code> by traveling
     * up the inheritance hierarchy to find the ones that have the mapping class name declared.
     *
     * @param mapping the {@link MetafacadeMapping} instance for which we'll retrieve its mapping classes.
     * @return the names of the mapping classes.
     */
    public static Set<String> getInheritedMappingClassNames(final MetafacadeMapping mapping)
    {
        final Class metafacadeClass = mapping.getMetafacadeClass();
        final MetafacadeImpls metafacadeImpls = MetafacadeImpls.instance();
        final Map<Class, Set<String>> mappingInstances = MetafacadeMappings.getAllMetafacadeMappingInstances();
        final Set<String> className = getInheritedMappingClassNames(metafacadeClass, metafacadeImpls, mappingInstances);
        if (className == null || className.isEmpty())
        {
            throw new MetafacadeMappingsException("No mapping class could be found for '" + metafacadeClass.getName() +
                    '\'');
        }
        MetafacadeUtils.getLogger().debug("inheritedMappingClassName " + metafacadeClass.getName() + "=" + className);
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
    private static Logger getLogger()
    {
        return MetafacadeFactory.getInstance().getLogger();
    }
}