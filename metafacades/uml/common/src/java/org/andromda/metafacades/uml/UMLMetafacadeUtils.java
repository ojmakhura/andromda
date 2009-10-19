package org.andromda.metafacades.uml;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
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
            // Match=true if the classifier name is in a different package than datatype::, i.e. PrimitiveTypes::
            // or the name is the same. Allows using Java, UML Standard types instead of AndroMDA types
            final String lastType = StringUtils.substringAfterLast(typeName, ":");
            // If FQN class name is the same as the mapped implementation Class Name
            name = StringUtils.trimToEmpty(classifier.getFullyQualifiedName(true));
            // IgnoreCase allows primitive and wrapped types to both return true
            if (lastType.equalsIgnoreCase(StringUtils.substringAfterLast(classifier.getFullyQualifiedName(), ":"))
                || lastType.equalsIgnoreCase(name))
            {
                isType = true;
            }
        }
        return isType;
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
                        UMLProfile.COLLECTION_TYPE_NAME.lastIndexOf(':')), 
                    UMLProfile.COLLECTION_IMPL_TYPE_NAME.substring(
                        UMLProfile.COLLECTION_IMPL_TYPE_NAME.lastIndexOf(':')));
                implCollection.put(UMLProfile.LIST_TYPE_NAME.substring(
                        UMLProfile.LIST_TYPE_NAME.lastIndexOf(':')), 
                    UMLProfile.LIST_IMPL_TYPE_NAME.substring(
                        UMLProfile.LIST_IMPL_TYPE_NAME.lastIndexOf(':')));
                implCollection.put(UMLProfile.MAP_TYPE_NAME.substring(
                        UMLProfile.MAP_TYPE_NAME.lastIndexOf(':')), 
                    UMLProfile.MAP_IMPL_TYPE_NAME.substring(
                        UMLProfile.MAP_IMPL_TYPE_NAME.lastIndexOf(':')));
                implCollection.put(UMLProfile.ORDERED_MAP_TYPE_NAME.substring(
                        UMLProfile.ORDERED_MAP_TYPE_NAME.lastIndexOf(':')), 
                    UMLProfile.ORDERED_MAP_IMPL_TYPE_NAME.substring(
                        UMLProfile.ORDERED_MAP_IMPL_TYPE_NAME.lastIndexOf(':')));
                implCollection.put(UMLProfile.ORDERED_SET_TYPE_NAME.substring(
                        UMLProfile.ORDERED_SET_TYPE_NAME.lastIndexOf(':')), 
                    UMLProfile.ORDERED_SET_IMPL_TYPE_NAME.substring(
                        UMLProfile.ORDERED_SET_IMPL_TYPE_NAME.lastIndexOf(':')));
                implCollection.put(UMLProfile.SET_TYPE_NAME.substring(
                        UMLProfile.SET_TYPE_NAME.lastIndexOf(':')), 
                    UMLProfile.SET_IMPL_TYPE_NAME.substring(
                        UMLProfile.SET_IMPL_TYPE_NAME.lastIndexOf(':')));
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
            }
            else
            {
                collectionType = input;
                genericType = "";
            }
            if (collectionType.indexOf('.') > 0)
            {
                collectionType = collectionType.substring(collectionType.lastIndexOf('.')+1);
                pkg = collectionType.substring(0, collectionType.lastIndexOf('.')+1);
            }
            else
            {
                pkg = "";
            }
            String implType = implCollection.get(collectionType);
            if (implType != null)
            {
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
                    pkg.getName().equals("PrimitiveTypes") ||
                    pkg.getName().equals("datatype")))
                {
                    rtn = false;
                }
            }
            catch (Exception ex)
            {
                // Output=true anyway just in case we want this output
                logger.error("UMLMetafacadeUtils.shouldOutput for " + mef.toString() + " " + ex.getClass().getName() + ": "+ ex.getMessage());
            }
        }
        return rtn;
    }

    private static final String namespaceScopeOperator = ".";
    private static final String COMMA = ", ";
    private static final String LT = "<";
    private static final String GT = ">";
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
            final StringBuffer buffer = new StringBuffer();

            // add the name we've constructed so far
            buffer.append(fullName);

            // start the parameter list
            buffer.append(LT);

            // loop over the parameters, we are so to have at least one (see
            // outer condition)
            final Collection<TemplateParameterFacade> templateParameters = facade.getTemplateParameters();
            for (Iterator<TemplateParameterFacade> parameterIterator = templateParameters.iterator(); parameterIterator.hasNext();)
            {
                final ModelElementFacade modelElement =
                    ((TemplateParameterFacade)parameterIterator.next()).getParameter();

                buffer.append(modelElement.getName());
                
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
        if (facade.isTemplateParametersPresent() && enableTemplating)
        {
            // we'll be constructing the parameter list in this buffer
            final StringBuffer buffer = new StringBuffer();

            // start the parameter list
            buffer.append(LT);

            // loop over the parameters, we are so to have at least one (see
            // outer condition)
            final Collection<TemplateParameterFacade> templateParameters = facade.getTemplateParameters();
            for (Iterator<TemplateParameterFacade> parameterIterator = templateParameters.iterator(); parameterIterator.hasNext();)
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
}