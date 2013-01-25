package org.andromda.cartridges.spring;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.andromda.cartridges.spring.metafacades.SpringService;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.EntityAttribute;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.EnumerationLiteralFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.Service;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Contains utilities used within the Spring cartridge.
 *
 * @author Chad Brandon
 * @author Joel Kozikowski
 */
public class SpringUtils
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(SpringUtils.class);

    /**
     * Retrieves all roles from the given <code>services</code> collection.
     *
     * @param services the collection services.
     * @return all roles from the collection.
     */
    public Collection<Role> getAllRoles(Collection<Service> services)
    {
        final Collection<Role> allRoles = new LinkedHashSet<Role>();
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
    public boolean remoteEjbsPresent(final Collection<Service> services)
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
    public boolean localEjbsPresent(final Collection<Service> services)
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
    public boolean remotableServicesPresent(final Collection<Service> services)
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
    public boolean lingoRemotableServicesPresent(final Collection<Service> services)
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
    public boolean privateServicesPresent(final Collection<Service> services)
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
    public boolean publicServicesPresent(final Collection<Service> services)
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
     * @param parameter String containing generics
     * @return String with generics stripped
     */
    public String removeGenerics(final String parameter)
    {
        int position = parameter.indexOf('<');
        String result = parameter;
        if(position != -1)
        {
            result = result.substring(0, position);
        }
        return result;
    }

    /**
     * Are we generating code for a rich client? false.
     */
    private boolean richClient = false;


    /**
     * Sets if code is being generated for a rich client.
     * @param richClientProperty
     */
    public void setRichClient(final boolean richClientProperty)
    {
        this.richClient = richClientProperty;
    }

    /**
     * Returns TRUE if code is being generated for a rich client environment
     * @return richClient
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
       String className = null;
       if (StringUtils.isNotBlank(fullyQualifiedName))
       {
           int lastDot = fullyQualifiedName.lastIndexOf('.');
           if (lastDot >= 0)
           {
               className = fullyQualifiedName.substring(lastDot+1);
           }
           else
           {
               className = fullyQualifiedName;
           }
       }
       else
       {
           className = "";
       }

       return className;
    }


    /**
     * Returns the package name part of a fully qualified name
     * @param fullyQualifiedName
     * @return just the "package" part of the fully qualified name
     */
    public String getPackageName(String fullyQualifiedName)
    {
       String packageName = null;
       if (StringUtils.isNotBlank(fullyQualifiedName))
       {
           int lastDot = fullyQualifiedName.lastIndexOf('.');
           packageName = (lastDot >= 0 ? fullyQualifiedName.substring(0, lastDot) : "");
       }
       else
       {
           packageName = "";
       }

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
    public List<ModelElementFacade> filterUniqueByName(Collection<ModelElementFacade> modelElements)
    {
        final Map<String, ModelElementFacade> filteredElements = new LinkedHashMap<String, ModelElementFacade>();

        for (final ModelElementFacade modelElement : modelElements)
        {
            /*final Object object = elementIterator.next();
            if (object instanceof ModelElementFacade)
            {*/
            if (!filteredElements.containsKey(modelElement.getName()))
            {
                filteredElements.put(modelElement.getName(), modelElement);
            }
            /*}*/
        }

        return new ArrayList<ModelElementFacade>(filteredElements.values());
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
                value = "new " + type.getWrapperName() + '(' + value + ')';
            }
        }
        return value;
    }

    /**
     * Takes the given <code>names</code> and concatenates them in camel case
     * form.
     *
     * @param names the names to concatenate.
     * @return the result of the concatenation
     */
    public static String concatNamesCamelCase(final Collection<String> names)
    {
        String result = null;
        if (names != null)
        {
            result = StringUtilsHelper.lowerCamelCaseName(StringUtils.join(names.iterator(), " "));
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
        final StringBuilder fullName = new StringBuilder(StringUtils.trimToEmpty(packageName));
        if (fullName.length() > 0)
        {
            fullName.append('.');
        }
        fullName.append(name);
        return fullName.toString();
    }

    /**
     * Constructs the fully qualified class definition from the facade. Used for
     * ValueObject, EmbeddedValue
     * @param facade the class to construct the roo field definition.
     * @return the Roo class definition.
     */
    public static List<String> getRooEnum(final EnumerationFacade facade)
    {
        List<String> results = new ArrayList<String>();
        String result = "enum type --class " + facade.getFullyQualifiedName() + " --permitReservedWords";
        results.add(result);
        for (AttributeFacade literal : facade.getLiterals())
        {
            result = "enum constant --name " + literal.getName();
            results.add(result);
        }
        return results;
    }

    /**
     * Constructs the fully qualified class definition from the facade. Used for
     * ValueObject, EmbeddedValue
     * @param facade the class to construct the roo field definition.
     * @return the Roo class definition.
     */
    public static List<String> getRooClass(final ClassifierFacade facade)
    {
        List<String> results = new ArrayList<String>();
        String result = null;
        if (facade.isEmbeddedValue() || facade.hasStereotype("ValueObject"))
        {
            if (facade.isEmbeddedValue())
            {
                result = "embeddable --class ";
            }
            else if (facade.hasStereotype("ValueObject"))
            {
                result = "class --class ";
            }
            result += facade.getFullyQualifiedName() + " --permitReservedWords";
            if (facade.isAbstract())
            {
                result += " --abstract";
            }
            if (facade.getGeneralization() != null)
            {
                result += " --extends " + facade.getGeneralization().getFullyQualifiedName();
            }
            results.add(result);
            for (AttributeFacade attr : facade.getAttributes())
            {
                results.add(getRooField(attr));
            }
            //results.add("");
        }
        // Old style Java 1.4 enumeration class
        /*else if (facade.hasStereotype("Enumeration"))
        {
            result = "enum type --class " + facade.getName() + " --permitReservedWords";
            results.add(result);
            for (AttributeFacade literal : facade.getAttributes())
            {
                result = "enum constant --name " + literal.getName();
                results.add(result);
            }
            results.add("");
        }*/
        return results;
    }

    /**
     * Constructs the fully qualified class name from the packageName and name.
     * Removes the words 'Test' and 'TestCase' because Roo cannot create tests for Entities
     * with those names.
     * @param entity the entity to construct the roo script definition.
     * @return the Roo field definition.
     */
    public static String getRooEntityName(final Entity entity)
    {
        return StringUtils.remove(entity.getFullyQualifiedName(), "Test");
    }

    /**
     * Constructs the fully qualified class name from the packageName and name.
     * @param entity the entity to construct the roo script definition.
     * @param recordType Either 'dao' or 'repository'
     * @return the Roo field definition.
     */
    public static List<String> getRooEntity(final Entity entity, String recordType)
    {
        List<String> results = new ArrayList<String>();
        Collection<ModelElementFacade> identifiers = entity.getIdentifiers(false);
        String identifierLine = null;
        // Keep track of entities already output, so that descendants are created after ancestors.
        if (entity.isCompositeIdentifier())
        {
            String line = "embeddable --class " + StringUtils.remove(entity.getFullyQualifiedIdentifierTypeName(), "Test") + " --serializable";
            //String line = "embeddable --class " + entity.getFullyQualifiedIdentifierTypeName() + " --serializable";
            results.add(line);
            for (AssociationEndFacade associationEnd : entity.getIdentifierAssociationEnds())
            {
                //results.add(SpringUtils.getRooField(associationEnd));
                if (associationEnd.isMany2One())
                {
                    line = "field other --fieldName " + associationEnd.getOtherEnd().getName() + " --type " + associationEnd.getOtherEnd().getType().getFullyQualifiedName();
                    results.add(line);
                }
            }
            for (ModelElementFacade identifier : identifiers)
            {
                logger.info("getRooField identifier: " + getRooField(identifier) + " for " + identifier);
                results.add(SpringUtils.getRooField(identifier));
            }
            identifierLine = " --identifierField " + entity.getIdentifierName() + " --identifierType " + StringUtils.remove(entity.getFullyQualifiedIdentifierTypeName(), "Test");
        }
        // Hibernate cartridge automatically adds default identifier if none, spring cartridge does not
        else if (entity.getIdentifiers(false).size()==0)
        {
            identifierLine = " --identifierField id --identifierType java.lang.Long --identifierColumn ID";
        }
        else if (entity.getIdentifiers(false).size()==1)
        {
            ModelElementFacade id = entity.getIdentifiers(false).iterator().next();
            String identifierType = entity.getFullyQualifiedIdentifierTypeName();
            // Identifier properties can be either attribute or associationEnd. If end, associated class identifiers are added to this class identifiers.
            if (id instanceof EntityAttribute)
            {
                ClassifierFacade type = ((EntityAttribute)id).getType();
                if (type.isPrimitive())
                {
                    // Primitive type not allowed for identifier in Spring Roo
                    identifierType = type.getWrapperName();
                }
            }
            // Some test models have 'Test' in entity/attribute names, conflicting with names created for test scaffolding
            identifierLine = " --identifierField " + entity.getIdentifierName() + " --identifierType " + StringUtils.remove(identifierType, "Test");
        }
        else
        {
            // Should never get to this place
        }
        // Identifiers: identifiers.size()
        String activeRecord = " --activeRecord true";
        // false = Use Spring Data JPA, no DAOs. True = Dao helpers
        if (recordType.equals("active"))
        {
            activeRecord = " --activeRecord false";
        }
        String mappedSuperclass = "";
        if (entity.hasStereotype("MappedSuperclass"))
        {
            mappedSuperclass = " --mappedSuperclass";
        }
        String extension = "";
        GeneralizableElementFacade general = entity.getGeneralization();
        if (general != null)
        {
            extension = " --extends " + general.getFullyQualifiedName();
        }
        String isAbstract = "";
        if (entity.isAbstract())
        {
            isAbstract = " --abstract";
        }
        String schema = "";
        if (StringUtils.isNotBlank(entity.getSchema()))
        {
            schema = " --schema " + entity.getSchema();
        }
        String version = "";
        String entityVersion = (String) entity.findTaggedValue("andromda_hibernate_version");
        for (AttributeFacade attr : entity.getAttributes())
        {
            if (attr.hasStereotype("Version"))
            {
                version = " --versionField " + attr.getName() + " --versionColumn " + ((EntityAttribute)attr).getColumnName() + " --versionType " + attr.getType().getFullyQualifiedName();
            }
        }
        // TODO Check for global configured property "versionProperty" for adding version property to all entities
        if (StringUtils.isNotBlank(entityVersion) && StringUtils.isBlank(version))
        {
            // Add automatic version identifier to entity definition
            version = " --versionField version --versionColumn VERSION --versionType java.lang.Integer";
        }
        // TODO version*, inheritanceType, persistenceUnit, entityName, sequenceName
        String line = "entity jpa --class " + StringUtils.remove(entity.getFullyQualifiedName(), "Test") + activeRecord + " --table " + entity.getTableName() + identifierLine + mappedSuperclass + extension + version + isAbstract + schema + " --equals --serializable --testAutomatically --permitReservedWords";
        results.add(StringUtils.replace(line, "  ", " "));
        return results;
    }

    /**
     * Constructs the fully qualified class name from the packageName and name.
     * @param facade the property (attribute or associationEnd) to construct the roo field definition.
     * @return the Roo field definition.
     */
    public static String getRooField(final ModelElementFacade facade)
    {
        String result = " --fieldName " + facade.getName();
        if (facade instanceof AssociationEndFacade)
        {
            AssociationEndFacade end = (AssociationEndFacade)facade;
            ClassifierFacade type = end.getOtherEnd().getType();
            String typeName = " --type " + type.getFullyQualifiedName();
            if (end.isMany2One())
            {
                result = "field reference " + result + typeName;
            }
            else if (end.isOne2Many())
            {
                result = "field set " + result + typeName;
            }
            else
            {
                result = "field other " + result + typeName;
            }
            //System.out.println(end.getBindedFullyQualifiedName(end) + " " + end.getQualifiedName());
            String owner = end.getFullyQualifiedName();
            if (owner.lastIndexOf('.') > 0)
            {
                owner = owner.substring(0, owner.lastIndexOf('.'));
                result += " --class " + owner;
            }
            else
            {
                logger.error("getRooField invalid owner: " + owner + " for " + facade.getFullyQualifiedName());
            }
        }
        else if (facade instanceof AttributeFacade)
        {
            AttributeFacade attribute = (AttributeFacade)facade;
            ClassifierFacade type = attribute.getType();
            String typeName = " --type " + type.getFullyQualifiedName();
            //logger.info("getRooField " + attribute.getFullyQualifiedName() + " type=" + type.getFullyQualifiedName() + " Integer=" + type.isIntegerType());
            if ((attribute.isMany() || attribute.getName().endsWith("[]")) && !type.isDataType())
            {
                // The Many side of M:1 relationship, as an Attribute instead of an AssociationEnd
                result = "field reference " + result + typeName;
            }
            // Version attribute is specified in the entity definition, skip this field definition
            else if (type.getFullyQualifiedName().endsWith("[]") || attribute.hasStereotype("Version"))
            {
                // TODO: Convert DataType * to column Map with association. Punt for now.
                result = "";
            }
            else
            {
                String primitive = "";
                if (type.isPrimitive())
                {
                    primitive = " --primitive ";
                }
                if (type.isIntegerType() || type.isLongType() || type.isDoubleType() || type.isFloatType())
                {
                    result = "field number " + result + primitive + typeName;
                }
                else if (type.isBooleanType())
                {
                    result = "field boolean " + result + primitive;
                }
                else if (type.isStringType() || type.isCharacterType())
                {
                    result = "field string " + result;
                }
                else if (type.isDateType())
                {
                    result = "field date " + result + typeName;
                }
                // EmbeddedValue cannot have column, notNull, comment options in Roo
                else if (type.isEmbeddedValue())
                {
                    result = "field embedded " + result + typeName;
                }
                else if (type.isEnumeration())
                {
                    result = "field enum " + result + typeName;
                }
                else if (type.isDateType())
                {
                    result = "field reference " + result + typeName;
                }
                else if (type.isDateType())
                {
                    result = "field set " + result + typeName;
                }
                else
                {
                    result = "field other " + result + typeName;
                }
                if (attribute instanceof EntityAttribute && !type.isEmbeddedValue())
                {
                    String column = ((EntityAttribute)attribute).getColumnName();
                    if (StringUtils.isNotBlank(column))
                    {
                        result += " --column " + column;
                    }
                }
                else
                {
                    //logger.error("getRooField facade should be EntityAttribute: " + attribute);
                }
                int lower = attribute.getLower();
                if (lower > 0 && !type.isEmbeddedValue())
                {
                    result += " --notNull ";
                }
                String comment = attribute.getDocumentation("", 9999, false);
                if (StringUtils.isNotBlank(comment) && !type.isEmbeddedValue())
                {
                    result += " --comment \"" + comment + "\"";
                }
            }
        }
        else
        {
            throw new RuntimeException("getRooField facade must be Attribute or AssociationEnd: " + facade);
        }
        if (StringUtils.isNotBlank(result))
        {
            result = StringUtils.replace(result + " --permitReservedWords", "  ", " ");
        }
        return result;
    }
    private static final SimpleDateFormat DF = new SimpleDateFormat("MM/dd/yyyy HH:mm:ssZ");
    /**
     * Returns the current Date in the specified format. $conversionUtils does not seem to work in vsl.
     *
     * @param format The format for the output date
     * @return the current date in the specified format.
     */
    public static String getDate(String format)
    {
        /*if (DF == null || !format.equals(DF.toLocalizedPattern()))
        {
            DF = new SimpleDateFormat(format);
        }*/
        return DF.format(new Date());
    }

    /**
     * Returns the current Date in the specified format.
     *
     * @return the current date with the default format .
     */
    public static String getDate()
    {
        return DF.format(new Date());
    }
}