package org.andromda.cartridges.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.andromda.cartridges.spring.metafacades.SpringService;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.Service;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;


/**
 * Contains utilities used within the Spring cartridge.
 *
 * @author Chad Brandon
 * @author Joel Kozikowski
 */
public class SpringUtils
{
    /**
     * Retrieves all roles from the given <code>services</code> collection.
     *
     * @param services the collection services.
     * @return all roles from the collection.
     */
    public Collection getAllRoles(Collection services)
    {
        final Collection allRoles = new LinkedHashSet();
        CollectionUtils.forAllDo(
            services,
            new Closure()
            {
                public void execute(Object object)
                {
                    if (object != null && Service.class.isAssignableFrom(object.getClass()))
                    {
                        allRoles.addAll(((Service)object).getAllRoles());
                    }
                }
            });
        return allRoles;
    }

    /**
     * Indicates if any remote EJBs are present in the collection
     * of services.
     *
     * @param services the collection of services to check.
     * @return true/false.
     */
    public boolean remoteEjbsPresent(final Collection services)
    {
        boolean present = services != null && !services.isEmpty();
        if (present)
        {
            present =
                CollectionUtils.find(
                    services,
                    new Predicate()
                    {
                        public boolean evaluate(final Object object)
                        {
                            boolean valid = false;
                            if (object instanceof SpringService)
                            {
                                final SpringService service = (SpringService)object;
                                valid = service.isEjbRemoteView();
                            }
                            return valid;
                        }
                    }) != null;
        }
        return present;
    }

    /**
     * Indicates if any local EJBs are present in the collection
     * of services.
     *
     * @param services the collection of services to check.
     * @return true/false.
     */
    public boolean localEjbsPresent(final Collection services)
    {
        boolean present = services != null && !services.isEmpty();
        if (present)
        {
            present =
                CollectionUtils.find(
                    services,
                    new Predicate()
                    {
                        public boolean evaluate(final Object object)
                        {
                            boolean valid = false;
                            if (object instanceof SpringService)
                            {
                                final SpringService service = (SpringService)object;
                                valid = service.isEjbLocalView();
                            }
                            return valid;
                        }
                    }) != null;
        }
        return present;
    }

    /**
     * Indicates if any Spring remotable services are present.
     *
     * @param services the collection of services to check.
     * @return true/false.
     */
    public boolean remotableServicesPresent(final Collection services)
    {
        boolean present = services != null && !services.isEmpty();
        if (present)
        {
            present =
                CollectionUtils.find(
                    services,
                    new Predicate()
                    {
                        public boolean evaluate(final Object object)
                        {
                            boolean valid = false;
                            if (object instanceof SpringService)
                            {
                                final SpringService service = (SpringService)object;
                                valid = service.isRemotable();
                            }
                            return valid;
                        }
                    }) != null;
        }
        return present;
    }

    /**
     * Indicates if any remotable services using Lingo are present.
     *
     * @param services the collection of services to check.
     * @return true/false.
     */
    public boolean lingoRemotableServicesPresent(final Collection services)
    {
        boolean present = services != null && !services.isEmpty();
        if (present)
        {
            present =
                CollectionUtils.find(
                        services,
                        new Predicate()
                        {
                            public boolean evaluate(final Object object)
                            {
                                boolean valid = false;
                                if (object instanceof SpringService)
                                {
                                    final SpringService service = (SpringService)object;
                                    valid = service.isRemotingTypeLingo();
                                }
                                return valid;
                            }
                        }) != null;
        }
        return present;
    }

    /**
     * Indicates if any private services are present.
     *
     * @param services the collection of services to check.
     * @return true/false.
     */
    public boolean privateServicesPresent(final Collection services)
    {
        boolean present = services != null && !services.isEmpty();
        if (present)
        {
            present =
                CollectionUtils.find(
                    services,
                    new Predicate()
                    {
                        public boolean evaluate(final Object object)
                        {
                            boolean valid = false;
                            if (object instanceof SpringService)
                            {
                                final SpringService service = (SpringService)object;
                                valid = service.isPrivate();
                            }
                            return valid;
                        }
                    }) != null;
        }
        return present;
    }
    
    /**
     * Indicates if any public (non private) services are present.
     *
     * @param services the collection of services to check.
     * @return true/false.
     */
    public boolean publicServicesPresent(final Collection services)
    {
        boolean present = services != null && !services.isEmpty();
        if (present)
        {
            present =
                CollectionUtils.find(
                    services,
                    new Predicate()
                    {
                        public boolean evaluate(final Object object)
                        {
                            boolean valid = false;
                            if (object instanceof SpringService)
                            {
                                final SpringService service = (SpringService)object;
                                valid = !service.isPrivate();
                            }
                            return valid;
                        }
                    }) != null;
        }
        return present;
    }
    
    /**
     * Based on the given <code>value</code>, this method will return
     * a formatted Spring property (including the handling of 'null').
     *
     * @param value the value from which to create the spring value.
     * @return the spring value.
     */
    public String getSpringPropertyValue(final String value)
    {
        String propertyValue = "";
        if (value != null)
        {
            if ("null".equalsIgnoreCase(value))
            {
                propertyValue = "<null/>";
            }
            else
            {
                propertyValue = "<value>" + value + "</value>";
            }
        }
        return propertyValue;
    }

    /**
     * Removes generics from string. Currently used to strip generics
     * from ejb-jar.xml method parameters.
     *
     * @param String containing generics
     * @return String with generics stripped
     */
    public String removeGenerics(final String parameter)
    {
        int position = parameter.indexOf("<");
        String result = parameter;
        if(position != -1)
        {
            result = result.substring(0, position);
        }
        return result;
    }

    /**
     * Are we generating code for a rich client?
     */
    private boolean richClient = false;


    /**
     * Sets if code is being generated for a rich client.
     */
    public void setRichClient(final boolean richClientProperty)
    {
        this.richClient = richClientProperty;
    }

    /**
     * Returns TRUE if code is being generated for a rich client environment
     */
    public boolean isRichClient()
    {
        return this.richClient;
    }

    /**
     * Returns the class name part of a fully qualified name
     * @param fullyQualifiedName
     * @return just the "class name" part of the fully qualified name
     */
    public String getClassName(String fullyQualifiedName)
    {
       String className;
       if (StringUtils.isNotEmpty(fullyQualifiedName))
       {
           int lastDot = fullyQualifiedName.lastIndexOf('.');
           if (lastDot >= 0)
               className = fullyQualifiedName.substring(lastDot+1);
           else
               className = fullyQualifiedName;
       }
       else
          className = "";

       return className;
    }


    /**
     * Returns the package name part of a fully qualified name
     * @param fullyQualifiedName
     * @return just the "package" part of the fully qualified name
     */
    public String getPackageName(String fullyQualifiedName)
    {
       String packageName;
       if (StringUtils.isNotEmpty(fullyQualifiedName))
       {
           int lastDot = fullyQualifiedName.lastIndexOf('.');
           if (lastDot >= 0)
               packageName = fullyQualifiedName.substring(0, lastDot);
           else
               packageName = "";
       }
       else
          packageName = "";

       return packageName;
    }

    /**
     * Returns an ordered set containing the argument model elements, model elements with a name that is already
     * used by another model element in the argument collection will not be returned.
     * The first operation with a name not already encountered will be returned, the order inferred by the
     * argument's iterator will determine the order of the returned list.
     *
     * @param modelElements a collection of model elements, elements that are not model elements will be ignored
     * @return the argument model elements without, elements with a duplicate name will only be recorded once
     */
    public List filterUniqueByName(Collection modelElements)
    {
        final Map filteredElements = new LinkedHashMap();

        for (final Iterator elementIterator = modelElements.iterator(); elementIterator.hasNext();)
        {
            final Object object = elementIterator.next();
            if (object instanceof ModelElementFacade)
            {
                final ModelElementFacade modelElement = (ModelElementFacade)object;
                if (!filteredElements.containsKey(modelElement.getName()))
                {
                    filteredElements.put(modelElement.getName(), modelElement);
                }
            }
        }

        return new ArrayList(filteredElements.values());
    }

    /**
     * Formats the given type to the appropriate Hibernate query parameter value.
     *
     * @param type the type of the Hibernate query parameter.
     * @param value the current value to format.
     * @return the formatted value.
     */
    public String formatHibernateQueryParameterValue(final ClassifierFacade type, String value)
    {
        if (type != null)
        {
            if (type.isPrimitive())
            {
                value = "new " + type.getWrapperName() + "(" + value + ")";
            }
            else if (type.isEnumeration())
            {
                value = value + ".getValue()";
            }
        }
        return value;
    }

    /**
     * Takes the given <code>names</code> and concatinates them in camel case
     * form.
     *
     * @param names the names to concatinate.
     * @return the result of the concatination
     */
    public static String concatNamesCamelCase(final Collection names)
    {
        String result = null;
        if (names != null)
        {
            result = StringUtilsHelper.lowerCamelCaseName(StringUtilsHelper.join(names.iterator(), " "));
        }
        return result;
    }
    
    /**
     * Constructs the fully qualified class name from the packageName and name.
     * @param packageName the package name to which the class belongs.
     * @param name the name of the class.
     * @return the fully qualified name.
     */
    public static String getFullyQualifiedClassName(final String packageName, final String name)
    {
        final StringBuffer fullName = new StringBuffer(StringUtils.trimToEmpty(packageName));
        if (fullName.length() > 0)
        {
            fullName.append(".");
        }
        fullName.append(name);
        return fullName.toString();
    }
}