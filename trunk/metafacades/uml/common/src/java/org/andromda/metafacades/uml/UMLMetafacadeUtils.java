package org.andromda.metafacades.uml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    /**
     * Determines if the class/package should be generated. Will not be generated if it has any
     * stereotypes: documentation, docOnly, Future, Ignore, analysis, perspective
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