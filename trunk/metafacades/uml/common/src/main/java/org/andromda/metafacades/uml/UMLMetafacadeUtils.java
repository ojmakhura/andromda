package org.andromda.metafacades.uml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.Logger;

/**
 * Contains utilities that are common to the UML metafacades.
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public class UMLMetafacadeUtils
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(UMLMetafacadeUtils.class);

    /**
     * Returns true or false depending on whether or not this Classifier or any of its specializations is of the given
     * type having the specified <code>typeName</code>
     * @param classifier
     * @param typeName the name of the type (i.e. datatype::Collection)
     * @return true/false
     */
    public static boolean isType(ClassifierFacade classifier, String typeName)
    {
        boolean isType = false;
        if (classifier != null && typeName != null)
        {
            final String type = StringUtils.trimToEmpty(typeName);
            String name = StringUtils.trimToEmpty(classifier.getFullyQualifiedName(true));
            isType = name.equals(type);
            // if this isn't a type defined by typeName, see if we can find any
            // types that inherit from the type.
            if (!isType)
            {
                isType = CollectionUtils.find(classifier.getAllGeneralizations(), new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        String name = StringUtils.trimToEmpty(
                                ((ModelElementFacade)object).getFullyQualifiedName(true));
                        return name.equals(type);
                    }
                }) != null;
            }
            if (!isType)
            {
                // Match=true if the classifier name is in a different package than datatype::, i.e. PrimitiveTypes::
                // or the name is the same. Allows using Java, UML Standard types instead of AndroMDA types
                final String lastType = StringUtils.substringAfterLast(typeName, ":");
                // If FQN class name is the same as the mapped implementation Class Name
                name = StringUtils.trimToEmpty(classifier.getFullyQualifiedName(true));
                // IgnoreCase allows primitive and wrapped types to both return true
                if (lastType.equalsIgnoreCase(StringUtils.substringAfterLast(classifier.getFullyQualifiedName(), ":"))
                    || lastType.equalsIgnoreCase(name) || lastType.equalsIgnoreCase(classifier.getFullyQualifiedName()))
                {
                    isType = true;
                }
            }
        }
        return isType;
    }

    // TODO: Move this to an external configuration. Distinguish between Java, C# reserved words.
    private static List<String> reservedWords = new ArrayList<String>();
    private static void populateReservedWords()
    {
        synchronized (reservedWords)
        {
            if (reservedWords.isEmpty())
            {
                reservedWords.add("abstract");
                reservedWords.add("as");
                reservedWords.add("assert");
                reservedWords.add("auto");
                reservedWords.add("bool");
                reservedWords.add("boolean");
                reservedWords.add("break");
                reservedWords.add("byte");
                reservedWords.add("case");
                reservedWords.add("catch");
                reservedWords.add("char");
                reservedWords.add("checked");
                reservedWords.add("class");
                reservedWords.add("const");
                reservedWords.add("continue");
                reservedWords.add("decimal");
                reservedWords.add("default");
                reservedWords.add("delegate");
                reservedWords.add("delete");
                reservedWords.add("deprecated");
                reservedWords.add("do");
                reservedWords.add("double");
                reservedWords.add("else");
                reservedWords.add("enum");
                reservedWords.add("event");
                reservedWords.add("explicit");
                reservedWords.add("export");
                reservedWords.add("extends");
                reservedWords.add("extern");
                reservedWords.add("false");
                reservedWords.add("final");
                reservedWords.add("finally");
                reservedWords.add("fixed");
                reservedWords.add("float");
                reservedWords.add("foreach");
                reservedWords.add("for");
                reservedWords.add("function");
                reservedWords.add("goto");
                reservedWords.add("if");
                reservedWords.add("implements");
                reservedWords.add("implicit");
                reservedWords.add("import");
                reservedWords.add("in");
                reservedWords.add("inline");
                reservedWords.add("instanceof");
                reservedWords.add("int");
                reservedWords.add("interface");
                reservedWords.add("internal");
                reservedWords.add("is");
                reservedWords.add("lock");
                reservedWords.add("long");
                reservedWords.add("namespace");
                reservedWords.add("native");
                reservedWords.add("new");
                reservedWords.add("null");
                reservedWords.add("object");
                reservedWords.add("operator");
                reservedWords.add("out");
                reservedWords.add("override");
                reservedWords.add("package");
                reservedWords.add("params");
                reservedWords.add("private");
                reservedWords.add("property");
                reservedWords.add("protected");
                reservedWords.add("public");
                reservedWords.add("readonly");
                reservedWords.add("ref");
                reservedWords.add("register");
                reservedWords.add("return");
                reservedWords.add("sbyte");
                reservedWords.add("sealed");
                reservedWords.add("short");
                reservedWords.add("signed");
                reservedWords.add("sizeof");
                reservedWords.add("static");
                reservedWords.add("strictfp");
                reservedWords.add("shring");
                reservedWords.add("struct");
                reservedWords.add("super");
                reservedWords.add("switch");
                reservedWords.add("synchronized");
                reservedWords.add("this");
                reservedWords.add("thread");
                reservedWords.add("throw");
                reservedWords.add("throws");
                reservedWords.add("transient");
                reservedWords.add("true");
                reservedWords.add("try");
                reservedWords.add("typedef");
                reservedWords.add("typeof");
                reservedWords.add("uint");
                reservedWords.add("ulong");
                reservedWords.add("unchecked");
                reservedWords.add("union");
                reservedWords.add("unsafe");
                reservedWords.add("unsigned");
                reservedWords.add("ushort");
                reservedWords.add("using");
                reservedWords.add("virtual");
                reservedWords.add("union");
                reservedWords.add("unsigned");
                reservedWords.add("uuid");
                reservedWords.add("var");
                reservedWords.add("void");
                reservedWords.add("volatile");
                reservedWords.add("while");
            }
        }
    }

    /**
     * Returns true if the value is a reserved keyword in Java or C#, or cannot be used as a name
     * @param name the String to check if a keyword
     * @return true/false
     */
    public static boolean isReservedWord(String name)
    {
        boolean reserved = false;
        populateReservedWords();
        if (StringUtils.isNotBlank(name) && reservedWords.contains(name.toLowerCase()))
        {
            reserved = true;
        }
        return reserved;
    }

    /**
     * Gets the getter prefix for a getter operation given the <code>type</code>.
     *
     * @param type the type from which to determine the prefix.
     * @return the getter prefix.
     */
    public static String getGetterPrefix(final ClassifierFacade type)
    {
        return type != null && type.isBooleanType() && type.isPrimitive() ? "is" : "get";
    }

    /**
     * Gets the getter prefix for a getter operation given the <code>type</code>,
     * taking multiplicity into account for booleans
     *
     * @param type the type from which to determine the prefix.
     * @param lowerBound If > 0 then type is not optional, thus primitive isBoolean()
     * @return the getter prefix.
     */
    public static String getGetterPrefix(final ClassifierFacade type, int lowerBound)
    {
        // Automatically converted to primitive type or wrapped type based on lowerBound
        return type != null && type.isBooleanType() && (lowerBound > 0 || type.isPrimitive()) ? "is" : "get";
    }

    /**
     * Returns true if the passed in constraint <code>expression</code> is of type <code>kind</code>, false otherwise.
     *
     * @param expression the expression to check.
     * @param kind       the constraint kind (i.e. <em>inv</em>,<em>pre</em>, <em>body</em>, etc).
     * @return true/false
     */
    public static boolean isConstraintKind(String expression, String kind)
    {
        Pattern pattern = Pattern.compile(".*\\s*" + StringUtils.trimToEmpty(kind) + "\\s*\\w*\\s*:.*", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(StringUtils.trimToEmpty(expression));
        return matcher.matches();
    }

    // TODO extract the mappings into the configurable metafacade namespace in UMLProfile
    private static Map<String, String> implCollection = new HashMap<String, String>();
    /**
     * Transforms the declared type to implementation type for a declared Collection.
     * Default: Collection=LinkedList, List=ArrayList, Set=HashSet, SortedSet=TreeSet.
     * Retains the generics and package in the template declaration, if any
     *
     * @param input the declared Collection type to be transformed into an implementation type
     * @return the Collection implementation declaration.
     */
    public static String getImplCollection(final String input)
    {
        synchronized (implCollection)
        {
            // Populate collection map based on profile.xml settings
            // TODO Use mapped implementation type instead of model types
            if (implCollection.isEmpty())
            {
                // Put all mappings into Map, removing the initial 'datatype::'
                //implCollection.put("List", "ArrayList");
                //implCollection.put("Set", "HashSet");
                //implCollection.put("SortedSet", "TreeSet");
                //implCollection.put("Map", "HashMap");
                //implCollection.put("SortedMap", "TreeMap");
                implCollection.put(UMLProfile.COLLECTION_TYPE_NAME.substring(
                        UMLProfile.COLLECTION_TYPE_NAME.lastIndexOf(':')+1),
                    UMLProfile.COLLECTION_IMPL_TYPE_NAME.substring(
                        UMLProfile.COLLECTION_IMPL_TYPE_NAME.lastIndexOf(':')+1));
                implCollection.put(UMLProfile.LIST_TYPE_NAME.substring(
                        UMLProfile.LIST_TYPE_NAME.lastIndexOf(':')+1),
                    UMLProfile.LIST_IMPL_TYPE_NAME.substring(
                        UMLProfile.LIST_IMPL_TYPE_NAME.lastIndexOf(':')+1));
                implCollection.put(UMLProfile.MAP_TYPE_NAME.substring(
                        UMLProfile.MAP_TYPE_NAME.lastIndexOf(':')+1),
                    UMLProfile.MAP_IMPL_TYPE_NAME.substring(
                        UMLProfile.MAP_IMPL_TYPE_NAME.lastIndexOf(':')+1));
                implCollection.put(UMLProfile.ORDERED_MAP_TYPE_NAME.substring(
                        UMLProfile.ORDERED_MAP_TYPE_NAME.lastIndexOf(':')+1),
                    UMLProfile.ORDERED_MAP_IMPL_TYPE_NAME.substring(
                        UMLProfile.ORDERED_MAP_IMPL_TYPE_NAME.lastIndexOf(':')+1));
                implCollection.put(UMLProfile.ORDERED_SET_TYPE_NAME.substring(
                        UMLProfile.ORDERED_SET_TYPE_NAME.lastIndexOf(':')+1),
                    UMLProfile.ORDERED_SET_IMPL_TYPE_NAME.substring(
                        UMLProfile.ORDERED_SET_IMPL_TYPE_NAME.lastIndexOf(':')+1));
                implCollection.put(UMLProfile.SET_TYPE_NAME.substring(
                        UMLProfile.SET_TYPE_NAME.lastIndexOf(':')+1),
                    UMLProfile.SET_IMPL_TYPE_NAME.substring(
                        UMLProfile.SET_IMPL_TYPE_NAME.lastIndexOf(':')+1));
            }
        }
        String collectionImpl = input;
        // No transformation if no package on fullyQualifiedName
        if (input.indexOf('.') > 0)
        {
            String collectionType = null;
            String genericType = null;
            String pkg = null;
            if (input.indexOf('<') > 0)
            {
                collectionType = input.substring(0, input.indexOf('<'));
                genericType = input.substring(input.indexOf('<'));
                if (genericType.startsWith("<? extends "))
                {
                    // Implementation collection type cannot declare 'extends'
                    genericType = '<' + genericType.substring(11);
                }
            }
            else
            {
                collectionType = input;
                genericType = "";
            }
            if (collectionType.indexOf('.') > 0)
            {
                pkg = collectionType.substring(0, collectionType.lastIndexOf('.')+1);
                collectionType = collectionType.substring(collectionType.lastIndexOf('.')+1);
            }
            else
            {
                pkg = "java.util.";
                logger.warn("UMLMetafacadeUtils pkg not found for " + collectionType);
            }
            String implType = implCollection.get(collectionType);
            if (implType == null)
            {
                logger.warn("UMLMetafacadeUtils colectionImpl not found for " + collectionType);
                collectionImpl = pkg + "ArrayList" + genericType;
            }
            else
            {
                //logger.info("UMLMetafacadeUtils translated from " + collectionType + " to " + implType);
                collectionImpl = pkg + implType + genericType;
            }
        }
        return collectionImpl;
    }

    /**
     * Determines if the class/package should be generated. Will not be generated if it has any
     * stereotypes: documentation, docOnly, Future, Ignore, analysis, perspective,
     * or any invalid package identifier characters ` ~!@#%^&*()-+={}[]:;<>,?/|
     * @param mef ModelElementFacade class to check for stereotypes.
     * @return false if it has any Stereotypes DocOnly, Future, Ignore configured in UMLProfile
     */
    public static boolean shouldOutput(ModelElementFacade mef)
    {
        boolean rtn = true;
        if (mef!=null)
        {
            try
            {
                PackageFacade pkg = (PackageFacade) mef.getPackage();
                if (mef instanceof PackageFacade)
                {
                    pkg = (PackageFacade) mef;
                }
                if (mef.hasStereotype(UMLProfile.STEREOTYPE_DOC_ONLY) ||
                    mef.hasStereotype(UMLProfile.STEREOTYPE_FUTURE) ||
                    mef.hasStereotype(UMLProfile.STEREOTYPE_IGNORE))
                {
                    rtn = false;
                }
                if (pkg != null &&
                    ( pkg.hasStereotype(UMLProfile.STEREOTYPE_DOC_ONLY) ||
                    pkg.hasStereotype(UMLProfile.STEREOTYPE_FUTURE) ||
                    pkg.hasStereotype(UMLProfile.STEREOTYPE_IGNORE) ||
                    pkg.hasStereotype("analysis") ||
                    pkg.hasStereotype("perspective") ||
                    // Verify package does not have any Java disallowed characters
                    StringUtils.containsAny(pkg.getName(), " `~!@#%^&*()-+={}[]:;<>,?/|") ||
                            "PrimitiveTypes".equals(pkg.getName()) ||
                            "datatype".equals(pkg.getName())))
                {
                    rtn = false;
                }
            }
            catch (Exception ex)
            {
                // Output=true anyway just in case we want this output
                logger.error("UMLMetafacadeUtils.shouldOutput for " + mef.toString() + ' ' + ex.getClass().getName() + ": "+ ex.getMessage());
            }
        }
        return rtn;
    }
    /**
     * Get the classname without the package name and without additional template<> parameters.
     *
     * @param facade
     * @param enableTemplating
     * @return getNameWithoutPackage
     */
    // TODO This should really be a method on ModelElementFacade
    public static String getClassDeclaration(ModelElementFacade facade, boolean enableTemplating)
    {
        return UMLMetafacadeUtils.getClassDeclaration(facade, facade.getName(), enableTemplating);
    }

    private static final String namespaceScopeOperator = ".";
    private static final String COMMA = ", ";
    private static final String LT = "<";
    private static final String GT = ">";
    /**
     * Get the classname without the package name and without additional template<> parameters.
     *
     * @param facade
     * @param className Class name to use in the class declaration, overrides facade.getName()
     * @param enableTemplating Whether template declaration should be created.
     * @return getNameWithoutPackage
     */
    // TODO This should really be a method on ModelElementFacade
    public static String getClassDeclaration(ModelElementFacade facade, String className, boolean enableTemplating)
    {
        if (StringUtils.isBlank(className))
    {
            className = facade.getName();
        }
        String fullName = StringUtils.trimToEmpty(className);
        final String packageName = facade.getPackageName(true);
        final String metafacadeNamespaceScopeOperator = MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR;
        if (StringUtils.isNotBlank(packageName))
        {
            fullName = packageName + metafacadeNamespaceScopeOperator + fullName;
        }
            final TypeMappings languageMappings = facade.getLanguageMappings();
            if (languageMappings != null)
            {
                fullName = StringUtils.trimToEmpty(languageMappings.getTo(fullName));

                // now replace the metafacade scope operators
                // with the mapped scope operators
                fullName = StringUtils.replace(
                        fullName,
                        metafacadeNamespaceScopeOperator,
                        namespaceScopeOperator);
            }
        // remove the package qualifier
        if (fullName.indexOf('.')>-1)
        {
            fullName = fullName.substring(fullName.lastIndexOf('.')+1);
        }

        if (facade.isTemplateParametersPresent() && enableTemplating)
        {
            // we'll be constructing the parameter list in this buffer
            final StringBuilder buffer = new StringBuilder();

            // add the name we've constructed so far
            buffer.append(fullName);

            // start the parameter list
            buffer.append(LT);

            // loop over the parameters, we are so to have at least one (see
            // outer condition)
            int size = facade.getTemplateParameters().size();
            int i = 1;
            for (TemplateParameterFacade parameter : facade.getTemplateParameters())
            {
                if (parameter != null)
                {
                    /*String name = parameter.getValidationName();
                    if (name==null && parameter.getParameter() != null)
                    {
                        name = parameter.getParameter().getName();
                    }
                    buffer.append(name);*/
                    buffer.append(parameter.getName());
                    if (i < size)
                    {
                        buffer.append(COMMA);
                        i++;
                    }
                }
            }

            // we're finished listing the parameters
            buffer.append(GT);

            // we have constructed the full name in the buffer
            fullName = buffer.toString();
        }

        return fullName;
    }

    private static final String QUESTION = "?";
    /**
     * Get the generic template<?, ?> declaration.
     *
     * @param facade
     * @param enableTemplating
     * @return getGenericTemplate
     */
    // TODO This should really be a method on ModelElementFacade
    public static String getGenericTemplate(ModelElementFacade facade, boolean enableTemplating)
    {
        String fullName = "";
        if (facade != null && facade.isTemplateParametersPresent() && enableTemplating)
        {
            // we'll be constructing the parameter list in this buffer
            final StringBuilder buffer = new StringBuilder();

            // start the parameter list
            buffer.append(LT);

            // loop over the parameters, we are so to have at least one (see
            // outer condition)
           for (Iterator<TemplateParameterFacade> parameterIterator =
               facade.getTemplateParameters().iterator(); parameterIterator.hasNext();)
           {
                parameterIterator.next();
                buffer.append(QUESTION);
                if (parameterIterator.hasNext())
                {
                    buffer.append(COMMA);
                }
            }

            // we're finished listing the parameters
            buffer.append(GT);

            // we have constructed the full name in the buffer
            fullName = buffer.toString();
        }

        return fullName;
    }

    /**
     * Get the fully-qualified classname without the additional template<> parameters.
     *
     * @param facade
     * @return getFQNameWithoutTemplate
     */
    // TODO This should really be a method on ModelElementFacade
    public static String getFQNameWithoutTemplate(ModelElementFacade facade)
    {
        String fullName = StringUtils.trimToEmpty(facade.getName());
        final String packageName = facade.getPackageName(true);
        final String metafacadeNamespaceScopeOperator = MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR;
        if (StringUtils.isNotBlank(packageName))
        {
            fullName = packageName + metafacadeNamespaceScopeOperator + fullName;
        }
        final TypeMappings languageMappings = facade.getLanguageMappings();
        if (languageMappings != null)
        {
            fullName = StringUtils.trimToEmpty(languageMappings.getTo(fullName));
            fullName = StringUtils.replace(
                fullName,
                metafacadeNamespaceScopeOperator,
                namespaceScopeOperator);
        }
        return fullName;
    }

    /**
     * Returns the number of methods without stereotypes or with SimpleClass stereotype. .
     * @param mef ModelElementFacade class to check for stereotypes.
     * @param outletFile Name of output file currently being processed. How do we get this in template?
     * @param refOutput Should .ref files be output?
     * @return false if it has any Stereotypes DocOnly, Future, Ignore configured in UMLProfile
     */
    public static boolean shouldOutput(ModelElementFacade mef, String outletFile, boolean refOutput)
    {
        boolean rtn = true;
        if (outletFile==null)
        {
            return rtn;
        }
        if (outletFile.endsWith(".ref") && !refOutput)
        {
            rtn = false;
        }
        else
        {
            rtn = shouldOutput(mef);
        }
        return rtn;
    }

    /**
     * Supplies a result for type = <new value>; initialization for all types
     * @param facade Type to create default object for
     * @return Constructor String with facade name
     */
    public String createConstructor(ModelElementFacade facade)
    {
        return createConstructor(facade, false);
    }

    /**
     * Supplies a result for type = <new value>; initialization for all types
     * @param facade Type to create default object for
     * @param useMany Return constructor with multiplicity type instead of underlying type
     * @return Constructor String with facade name
     */
    public String createConstructor(ModelElementFacade facade, boolean useMany)
    {
        return createConstructor(facade, useMany, null);
    }

    /**
     * Supplies a result for type = <new value>; initialization for all types
     * @param facade Type to create default object for
     * @param useMany Return constructor with multiplicity type instead of underlying type
     * @param parent Object containing this facade, which may have an attribute named dependency to a different type
     * @return Constructor String with facade name
     */
    @SuppressWarnings("null")
    public String createConstructor(ModelElementFacade facade, boolean useMany, ModelElementFacade parent)
    {
        if (facade==null)
        {
            return "facade was null";
        }
        String rtn = "";
        String toString = "";
        ClassifierFacade type = null;
        String typeName = facade.getFullyQualifiedName();
        String name = facade.getName();
        String defaultValue = "";
        // TODO: Default collection type from properties
        String collectionType = "java.util.ArrayList";
        Boolean isMany = null;
        boolean isEnumeration = false;
        int maxLength = 9999;
        /*if (parent != null)
        {
            // See if a named dependency exists with the same facadeName
            for (final DependencyFacade dependency : parent.getSourceDependencies())
            {
                if (dependency.getName().equals(facade.getName()) && dependency instanceof DependencyFacade)
                {
                    facade = ((DependencyFacade)dependency).getTargetElement();
                    toString = ".toString()";
                    break;
                }
            }
        }*/
        try {
            if (logger.isDebugEnabled())
            {
                logger.debug("name=" + name + " typeName=" + typeName + " useMany=" + useMany + " facade=" + facade + " parent=" + parent);
            }
            // Use persistence or validation annotations to limit the created value length
            String length = (String)facade.findTaggedValue("andromda_persistence_column_length");
            if (length != null && length.length()>0 && StringUtils.isNumeric(length))
            {
                maxLength = Integer.parseInt(length);
            }
            else
            {
                length = (String)facade.findTaggedValue("andromda_persistence_column_precision");
                if (length != null && length.length()>0 && StringUtils.isNumeric(length))
                {
                    maxLength = Integer.parseInt(length);
                }
                else
                {
                    length = (String)facade.findTaggedValue("andromda_validation_length");
                    if (length != null && length.length()>0 && StringUtils.isNumeric(length))
                    {
                        maxLength = Integer.parseInt(length);
                    }
                    else
                    {
                        length = (String)facade.findTaggedValue("andromda_validation_precision");
                        if (length != null && length.length()>0 && StringUtils.isNumeric(length))
                        {
                            maxLength = Integer.parseInt(length);
                        }
                    }
                }
            }
            if (facade instanceof ClassifierFacade)
            {
                ClassifierFacade classifier = (ClassifierFacade) facade;
                type = classifier;
                typeName = classifier.getFullyQualifiedName();
            }
            if (facade instanceof AttributeFacade)
            {
                AttributeFacade attr = (AttributeFacade) facade;
                defaultValue = attr.getDefaultValue();
                type = attr.getType();
                if (useMany)
                {
                    typeName = attr.getGetterSetterTypeName();
                }
                else
                {
                    typeName = type.getFullyQualifiedName();
                }
                if (attr.getUpper()>1 || attr.getUpper()==-1)
                {
                    isMany = true;
                }
            }
            else if (facade instanceof ParameterFacade)
            {
                ParameterFacade attr = (ParameterFacade) facade;
                defaultValue = attr.getDefaultValue();
                type = attr.getType();
                typeName = type.getFullyQualifiedName();
                if (type.isEnumeration())
                {
                    facade = type;
                }
                else if (useMany)
                {
                    typeName = collectionType + '<' + type.getFullyQualifiedName() + '>';
                }
                else
                {
                    typeName = type.getFullyQualifiedName();
                }
                if (attr.getUpper()>1 || attr.getUpper()==-1)
                {
                    isMany = true;
                }
            }
            if (facade instanceof AssociationEndFacade)
            {
                AssociationEndFacade attr = (AssociationEndFacade) facade;
                type = attr.getType();
                if (useMany)
                {
                    typeName = attr.getGetterSetterTypeName();
                }
                else
                {
                    typeName = type.getFullyQualifiedName();
                }
                if (attr.getUpper()>1 || attr.getUpper()==-1)
                {
                    isMany = true;
                }
                facade = attr.getType();
            }
            // TODO: Make this work for attribute types other than String.
            if (parent != null && StringUtils.isEmpty(defaultValue) && ("String".equals(typeName) || "java.lang.String".equals(typeName)))
            {
                // See if a named dependency exists with the same facadeName
                for (final DependencyFacade dependency : parent.getSourceDependencies())
                {
                    if (dependency.getName().equals(facade.getName()))
                    {
                        facade = dependency.getTargetElement();
                        // DependencyFacade type comes back empty for UML2::Integer
                        // Need to get metaObject Name property and verify it is not null.
                        if (facade instanceof ClassifierFacade)
                        {
                            type = (ClassifierFacade) facade;
                        }
                        typeName = facade.getFullyQualifiedName();
                        toString = ".toString()";
                        if (logger.isDebugEnabled())
                        {
                            logger.debug(parent + " " + facade + " = "
                                    + dependency + " type=" + type + " typeName="
                                    + typeName);
                        }
                        break;
                    }
                }
            }
            if (type instanceof EnumerationFacade)
            {
                EnumerationFacade enumer = (EnumerationFacade) type;
                //type = enumer.getLiteralType().getFullyQualifiedName();
                Collection<AttributeFacade> literals = enumer.getLiterals();
                if (StringUtils.isEmpty(defaultValue) && !literals.isEmpty())
                {
                    // Just get the first enumeration literal
                    Object literal = literals.iterator().next();
                    if (literal instanceof EnumerationLiteralFacade)
                    {
                        EnumerationLiteralFacade enumLiteral = (EnumerationLiteralFacade) literal;
                        defaultValue = enumLiteral.getValue();
                    }
                    else if (literal instanceof AttributeFacade)
                    {
                        AttributeFacade attrib = (AttributeFacade) literal;
                        defaultValue = attrib.getEnumerationValue();
                        if (defaultValue==null)
                        {
                            defaultValue = attrib.getDefaultValue();
                        }
                    }
                    // Literal value is always a String. Remove quotes if part of default (i.e. class attribute).
                    defaultValue = StringUtils.remove(defaultValue, "\"");
                    defaultValue = enumer.getFullyQualifiedName() + ".fromValue(\"" + defaultValue + "\")";
                }
                else
                {
                    defaultValue = enumer.getName() + '.' + defaultValue;
                }
                isEnumeration = true;
                if (logger.isDebugEnabled())
                {
                    logger.debug("EnumerationFacade=" + facade + " type=" + type + " literals=" + literals.size() + " default=" + defaultValue);
                }
            }
            if (type != null && type.findTaggedValue("andromda_persistence_lob_type")!=null)
            {
                typeName = String.valueOf(type.findTaggedValue("andromda_persistence_lob_type"));
                // LOB Types have a different datatype than the underlying declared type
            }
            if (useMany && (isMany==null || isMany.booleanValue()) && !typeName.endsWith("[]"))
            {
                typeName = UMLMetafacadeUtils.getImplCollection(typeName);
                if (!typeName.startsWith("java.util") && type != null)
                {
                    if (type.equals("java.util.Collection") || typeName.equals("java.util.List"))
                    {
                        rtn = "new " + collectionType + "<" + typeName + ">()";
                    }
                    else if (typeName.equals("java.util.Set"))
                    {
                        rtn = "new java.util.HashSet<" + typeName + ">()";
                    }
                    else if (typeName.equals("java.util.Map"))
                    {
                        rtn = "new java.util.HashMap<" + typeName + ">()";
                    }
                    else
                    {
                        rtn = "new " + collectionType + '<' + typeName + ">()";
                    }
                }
                else
                {
                    // Assume array or type Collection<type>
                    rtn = "new " + typeName + "()";
                }
            }
            else if ("String".equals(typeName) || "java.lang.String".equals(typeName))
            {
                if (defaultValue != null && defaultValue.trim().length() > 0)
                {
                    if (defaultValue.startsWith("\"") || defaultValue.startsWith("'"))
                    {
                        defaultValue = defaultValue.substring(1, defaultValue.length()-1);
                    }
                    if (defaultValue.endsWith("\"") || defaultValue.endsWith("'"))
                    {
                        defaultValue = defaultValue.substring(0, defaultValue.length()-2);
                    }
                    if (defaultValue.trim().length() > maxLength)
                    {
                        logger.warn("Attribute default for " + facade.getFullyQualifiedName() + " is longer than max column length " + maxLength);
                        defaultValue = defaultValue.substring(0, maxLength-1);
                    }
                }
                rtn = '\"' + (StringUtils.isNotBlank(defaultValue) ? defaultValue : name) + '\"';
            }
            else if ("Boolean".equals(typeName) || "java.lang.Boolean".equals(typeName))
            {
                rtn = (StringUtils.isNotEmpty(defaultValue) ? "Boolean." + defaultValue.toUpperCase() : "Boolean.TRUE");
            }
            else if ("boolean".equals(typeName))
            {
                rtn = (StringUtils.isNotEmpty(defaultValue) ? defaultValue : "true");
            }
            else if ("int".equals(typeName) || "short".equals(typeName) || "long".equals(typeName)
                    || "byte".equals(typeName) || "float".equals(typeName) || "double".equals(typeName))
            {
                rtn = (StringUtils.isNotEmpty(defaultValue) ? defaultValue : "1");
            }
            else if ("java.util.Date".equals(typeName))
            {
                rtn = "new " + typeName + "()";
            }
            else if ("java.sql.Timestamp".equals(typeName))
            {
                rtn = "new java.sql.Timestamp(System.currentTimeMillis())";
            }
            else if ("java.util.Calendar".equals(typeName))
            {
                rtn = "java.util.Calendar.getInstance()";
            }
            else if ("char".equals(typeName))
            {
                rtn = "'" + (StringUtils.isNotEmpty(defaultValue) ? defaultValue : name.substring(0, 1)) + "'";
            }
            else if ("Character".equals(typeName))
            {
                rtn = "new Character('" + (StringUtils.isNotEmpty(defaultValue) ? "new Character(" + defaultValue : name.substring(0, 1)) + "')";
            }
            else if ("Byte".equals(typeName) || "java.lang.Byte".equals(typeName))
            {
                rtn = "new Byte(\"" + facade.getName() + "\")";
            }
            else if ("Short".equals(typeName) || "java.lang.Short".equals(typeName)
                    || "Integer".equals(typeName) || "java.lang.Integer".equals(typeName)
                    || "Long".equals(typeName) || "java.lang.Long".equals(typeName)
                    || "Float".equals(typeName) || "java.lang.Float".equals(typeName)
                    || "Double".equals(typeName) || "java.lang.Double".equals(typeName)
                    || "java.math.BigDecimal".equals(typeName))
            {
                rtn = (!StringUtils.isEmpty(defaultValue) ? typeName + ".valueOf(" + defaultValue + ")" : typeName + ".valueOf(1)");
            }
            else if ("java.math.BigInteger".equals(typeName))
            {
                rtn = (!StringUtils.isEmpty(defaultValue) ? "java.math.BigInteger.valueOf(" + defaultValue + ')' : "java.math.BigInteger.valueOf(1)");
            }
            else if ("byte[]".equals(typeName))
            {
                rtn = (StringUtils.isNotBlank(defaultValue) ? defaultValue : '\"' + name + '\"') + ".getBytes()";
            }
            else if ("char[]".equals(typeName))
            {
                String value = StringUtils.isNotBlank(defaultValue) ? defaultValue : name;
                if (!value.startsWith("\""))
                {
                    value = "\"" + value;
                }
                if (!value.endsWith("\""))
                {
                    value = value + "\"";
                }
                rtn = value + ".toCharArray()";
            }
            else if ("String[]".equals(typeName))
            {
                rtn = "new String[] { " + (StringUtils.isNotBlank(defaultValue) ? defaultValue : '\"' + name + '\"') + " }";
            }
            else if (isEnumeration)
            {
                if (useMany)
                {
                    rtn = collectionType + '<' + defaultValue + '>';
                }
                else
                {
                    rtn = defaultValue;
                }
            }
            else if (!StringUtils.isEmpty(defaultValue))
            {
                rtn = "new " + typeName + '(' + defaultValue + ')';
            }
            else if (type != null && type.hasStereotype("EmbeddedValue"))
            {
                // EmbeddedValue classes will always be abstract with Impl generated classes.
                rtn = "new " + typeName + "Impl()";
            }
            else if (type instanceof GeneralizableElementFacade)
            {
                // If type has a descendant with name <typeName>Impl, assume typeNameImpl must be instantiated instead of typeName
                if (typeName.endsWith("[]"))
                {
                    rtn = "{ new " + typeName.substring(0, typeName.length()-2) + "() }";
                }
                else
                {
                    rtn = "new " + typeName + "()";
                }
                //if (facade instanceof ClassifierFacade)
                //{
                    //ClassifierFacade classifier = (ClassifierFacade)facade;
                    // If type is abstract, choose Impl descendant if exists, or the last descendant
                    if (type.isAbstract())
                    {
                        // Can't instantiate abstract class - pick some descendant
                        for (GeneralizableElementFacade spec : type.getSpecializations())
                        {
                            if (spec.getName().equals(type.getName() + "Impl"))
                            {
                                rtn = '(' + type.getName() + ")new " + typeName + "Impl()";
                                break;
                            }
                            rtn = '(' + type.getName() + ")new " + spec.getFullyQualifiedName() + "()";
                        }
                    }
                //}
                GeneralizableElementFacade generalization = (GeneralizableElementFacade)type;
                for (GeneralizableElementFacade spec : generalization.getSpecializations())
                {
                    if (spec.getName().equals(type.getName() + "Impl"))
                    {
                        rtn = '(' + type.getName() + ")new " + spec.getFullyQualifiedName() + "Impl()";
                    }
                }
            }
            else if (typeName.endsWith("[]"))
            {
                rtn = "new " + typeName + " { new " + typeName.substring(0, typeName.length()-2) + "() }";
            }
            else
            {
                rtn = "new " + typeName + "()";
            }
            rtn = StringUtils.replace(rtn, "java.util.Collection", "java.util.ArrayList") + toString;
            rtn = StringUtils.replace(rtn, "java.util.Set", "java.util.HashSet") + toString;
            if (logger.isDebugEnabled())
            {
                logger.debug("facade=" + facade + " facadeName=" + facade.getName() + " type=" + type + " typeName=" + typeName + " name=" + name + " isMany=" + isMany + " defaultValue=" + defaultValue + " rtn=" + rtn);
            }
        } catch (RuntimeException e) {
            logger.error(e + " facade=" + facade + " facadeName=" + facade.getName() + " parent=" + parent + " type=" + type + " typeName=" + typeName + " name=" + name + " isMany=" + isMany + " defaultValue=" + defaultValue);
            e.printStackTrace();
        }
        return rtn;
    }

    /**
     * TODO Reference this logic from AssociationEnd
     * Determine if this association end owns the relationship. i.e. if the associationEnd property
     * belonging to the Entity on the opposite end owns the relationship. Based on tagged value,
     * multiplicity, aggregation/composition. If all else fails, the longest name owns the
     * association, or else the alphabetically first name. One side of a relationship must
     * always own the association and be created and deleted first.
     * @param associationEnd the association end
     * @return true if the associationEnd (property of the entity on the other end) is owned by the entity on the other end
     */
    public static boolean isOwningEnd(AssociationEndFacade associationEnd)
    {
        boolean owning = false;
        AssociationEndFacade otherEnd = associationEnd.getOtherEnd();
        //String assoc = ((Entity)otherEnd.getValidationOwner()).getName() + "." + associationEnd.getName() + " -> " + associationEnd.getType().getName() + " ";
        if (BooleanUtils.toBoolean(
                ObjectUtils.toString(otherEnd.findTaggedValue(
                    "andromda_persistence_associationEnd_primary"))))
        {
            owning = true;
        }
        // See if this end or the other end is tagged as the association owner
        else if (BooleanUtils.toBoolean(
            ObjectUtils.toString(otherEnd.findTaggedValue(
                "andromda_persistence_associationEnd_primary"))))
        {
            owning = false;
        }
        // Navigable side always owns the relationship
        else if (associationEnd.isNavigable() && !otherEnd.isNavigable())
        {
            owning = true;
            //LOGGER.info("Owning=true: " + assoc + "nav=" + associationEnd.isNavigable() + " Onav=" + otherEnd.isNavigable());
        }
        else if (!associationEnd.isNavigable() && otherEnd.isNavigable())
        {
            owning = false;
            //LOGGER.info("Owning=false: " + assoc + "nav=" + associationEnd.isNavigable() + " Onav=" + otherEnd.isNavigable());
        }
        // Other side: aggregation/composition side does not own the bidirectional relationship
        else if (otherEnd.isAggregation() || otherEnd.isComposition())
        {
            owning = false;
            //LOGGER.info("Owning=true: " + assoc + "Oagg=" + otherEnd.isAggregation() + " Ocomp=" + otherEnd.isComposition());
        }
        else if (associationEnd.isAggregation() || associationEnd.isComposition())
        {
            owning = true;
            //LOGGER.info("Owning=false: " + assoc + "Oagg=" + associationEnd.isAggregation() + " Ocomp=" + otherEnd.isComposition());
        }
        // The many side of 1:M owns the bidirectional relationship
        else if (!associationEnd.isMany() && otherEnd.isMany())
        {
            owning = true;
            //LOGGER.info("Owning=true: " + assoc + "many=" + associationEnd.isMany() + " Omany=" + otherEnd.isMany());
        }
        // Other side: the many side of 1:M owns the bidirectional relationship if no composition/aggregation
        else if (associationEnd.isMany() && !otherEnd.isMany())
        {
            owning = false;
            //LOGGER.info("Owning=false: " + assoc + "many=" + associationEnd.isMany() + " Omany=" + otherEnd.isMany());
        }
        // The optional side of 1:1 or M:M owns the bidirectional relationship
        else if (associationEnd.getLower() > 0 && otherEnd.getLower() == 0)
        {
            owning = true;
            //LOGGER.info("Owning=true: " + assoc + "many=" + associationEnd.isMany() + " Omany=" + otherEnd.isMany());
        }
        // If bidirectional 1:1 or M:M, choose the side with the longest type name because it typically indicates a composition relationship
        /*else if (this.getOtherEnd().getType().getName().length()
                > this.getType().getName().length())*/
        else if (associationEnd.getName().length()
            < otherEnd.getName().length())
        {
            owning = true;
            //LOGGER.info("Owning=true: " + assoc + "endLength=" + associationEnd.getName().length() + " Olength=" + otherEnd.getName().length());
        }
        // If length is the same, alphabetically earliest is the owner
        else if (associationEnd.getName().compareTo(
            otherEnd.getName()) < 0)
        {
            owning = true;
            //LOGGER.info("Owning=true: " + assoc + "name=" + associationEnd.getName() + " < OName=" + otherEnd.getName());
        }
        /*LOGGER.info(((Entity)associationEnd.getOtherEnd().getValidationOwner()).getName()
            + "." + associationEnd.getName() +" IsOwningEnd=" + owning + " for "
            + ((Entity)associationEnd.getOtherEnd().getValidationOwner()).getName()
            + " OName=" + otherEnd.getName() + " Aggregation=" + associationEnd.isAggregation()
            + " Composition=" + associationEnd.isComposition() + " Navigable=" + associationEnd.isNavigable()
            + " !Navigable=" + !otherEnd.isNavigable() + " Many=" + associationEnd.isMany()
            + " OMany=" + otherEnd.isMany() + " Upper=" + associationEnd.getUpper()
            + " OUpper=" + otherEnd.getUpper() + " OAggregation=" + otherEnd.isAggregation()
            + " OComposition=" + otherEnd.isComposition() + " ONavigable=" + otherEnd.isNavigable()
            + " otherEnd=" + otherEnd.getFullyQualifiedName());*/
        return owning;
    }

    private static FastDateFormat df = FastDateFormat.getInstance("MM/dd/yyyy HH:mm:ss");

    /**
     * Returns the current Date in the specified format.
     *
     * @param format The format for the output date
     * @return the current date in the specified format.
     */
    public static String getDate(String format)
    {
        if (df == null || !format.equals(df.getPattern()))
        {
            df = FastDateFormat.getInstance(format);
        }
        return df.format(new Date());
    }

    /**
     * Returns the current Date in the specified format.
     *
     * @return the current date with the default format .
     */
    public static String getDate()
    {
        return df.format(new Date());
    }
}