package org.andromda.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * A utility object used by the code generator when it needs to convert an object
 * from one data type to another.
 *
 * @author Joel Kozikowski
 * @author Bob Fields
 */
public class JavaTypeConverter
{
    private List<String> javaTypeConversionIgnoreList = new ArrayList<String>();
    
    /** 
     * Specifies a list of one or more fully qualified java types that should be ignored 
     * whenever a type conversion is done.  See Spring namespace property "javaTypeConversionIgnoreList" 
     * @param commaSeparatedIgnoreList comma separated list of types to be ignored
     */
    public void setJavaTypeConversionIgnoreList(String commaSeparatedIgnoreList)
    {
        javaTypeConversionIgnoreList = new ArrayList<String>();
        if (commaSeparatedIgnoreList != null)
        {
            final String[] typeList = commaSeparatedIgnoreList.split("\\s*,\\s*");
            javaTypeConversionIgnoreList.addAll(Arrays.asList(typeList));
        }
    }
    
    private static class ConversionEntry
    {
        // - not private to increase performance of
        //   inner class access
        final String sourceType;
        final String targetType;
        final String conversionPattern;

        public ConversionEntry(
            final String sourceType,
            final String targetType,
            final String conversionPattern)
        {
            this.sourceType = sourceType;
            this.targetType = targetType;
            this.conversionPattern = conversionPattern;
        }
    }

    private static ConversionEntry[] conversionTable =
        {
            new ConversionEntry("int", "java.lang.Integer", "new java.lang.Integer({0})"),
            new ConversionEntry("int", "java.lang.String", "java.lang.String.valueOf({0})"),
            new ConversionEntry("int", "long", "(long){0}"), 
            new ConversionEntry("int", "double", "(double){0}"),
            new ConversionEntry("int", "float", "(float){0}"), 
            new ConversionEntry("int", "boolean", "({0} != 0)"),
            new ConversionEntry("java.lang.Integer", "int", "({0} == null ? 0 : {0}.intValue())"),
            new ConversionEntry("java.lang.Integer", "java.lang.String", "({0} == null ? null : {0}.toString())"),
            new ConversionEntry("java.lang.String", "int", "({0} == null ? 0 : Integer.valueOf({0}).intValue())"),
            new ConversionEntry("java.lang.String", "java.lang.Integer", "({0} == null ? null : Integer.valueOf({0}))"),
            
            new ConversionEntry("long", "java.lang.Long", "new java.lang.Long({0})"),
            new ConversionEntry("long", "java.lang.String", "java.lang.String.valueOf({0})"),
            new ConversionEntry("long", "int", "(int){0}"), 
            new ConversionEntry("long", "double", "(double){0}"),
            new ConversionEntry("long", "float", "(float){0}"), 
            new ConversionEntry("long", "boolean", "({0} != 0)"),
            new ConversionEntry("java.lang.Long", "long", "({0} == null ? 0 : {0}.longValue())"),
            new ConversionEntry("java.lang.Long", "java.lang.String", "({0} == null ? null : {0}.toString())"),
            new ConversionEntry("java.lang.String", "long", "({0} == null ? 0 : Long.valueOf({0}).longValue())"),
            new ConversionEntry("java.lang.String", "java.lang.Long", "({0} == null ? null : Long.valueOf({0}))"),
            
            new ConversionEntry("double", "java.lang.Double", "new java.lang.Double({0})"),
            new ConversionEntry("double", "java.lang.String", "java.lang.String.valueOf({0})"),
            new ConversionEntry("double", "int", "(int){0}"), 
            new ConversionEntry("double", "long", "(long){0}"),
            new ConversionEntry("double", "float", "(float){0}"),
            new ConversionEntry("double", "boolean", "({0} != 0.0)"),
            new ConversionEntry("java.lang.Double", "double", "({0} == null ? 0.0 : {0}.doubleValue())"),
            new ConversionEntry("java.lang.Double", "java.lang.String", "({0} == null ? null : {0}.toString())"),
            new ConversionEntry("java.lang.String", "double", "({0} == null ? 0.0 : Double.valueOf({0}).doubleValue())"),
            new ConversionEntry("java.lang.String", "java.lang.Double", "({0} == null ? null : Double.valueOf({0}))"),
            
            new ConversionEntry("float", "java.lang.Float", "new java.lang.Float({0})"),
            new ConversionEntry("float", "java.lang.String", "java.lang.String.valueOf({0})"),
            new ConversionEntry("float", "int", "(int){0}"), 
            new ConversionEntry("float", "long", "(long){0}"),
            new ConversionEntry("float", "double", "(double){0}"),
            new ConversionEntry("float", "boolean", "({0} != 0.0f)"),
            new ConversionEntry("java.lang.Float", "float", "({0} == null ? 0.0f : {0}.floatValue())"),
            new ConversionEntry("java.lang.Float", "java.lang.String", "({0} == null ? null : {0}.toString())"),
            new ConversionEntry("java.lang.String", "float", "({0} == null ? 0.0f : Float.valueOf({0}).floatValue())"),
            new ConversionEntry("java.lang.String", "java.lang.Float", "({0} == null ? null : Float.valueOf({0}))"),

            new ConversionEntry("int", "java.math.BigInteger", "java.math.BigInteger.valueOf((long){0})"),
            new ConversionEntry("java.math.BigInteger", "int", "({0} == null ? 0 : {0}.intValue())"),
            new ConversionEntry("long", "java.math.BigInteger", "java.math.BigInteger.valueOf({0})"),
            new ConversionEntry("java.math.BigInteger", "long", "({0} == null ? 0 : {0}.longValue())"),
            new ConversionEntry("java.math.BigInteger", "float", "({0} == null ? 0.0f : {0}.floatValue())"),
            new ConversionEntry("float", "java.math.BigInteger", "new java.math.BigDecimal((double){0}).toBigInteger()"),
            new ConversionEntry("java.math.BigInteger", "double", "({0} == null ? 0.0 : {0}.doubleValue())"),
            new ConversionEntry("double", "java.math.BigInteger", "new java.math.BigDecimal({0}).toBigInteger()"),
            new ConversionEntry("java.lang.String", "java.math.BigInteger", "new java.math.BigInteger({0})"),
            new ConversionEntry("java.math.BigInteger", "java.lang.String", "({0} == null ? null : {0}.toString())"),
            new ConversionEntry("java.math.BigDecimal", "java.math.BigInteger", "({0} == null ? null : {0}.toBigInteger())"),
            new ConversionEntry("java.lang.Integer", "java.math.BigInteger", "({0} == null ? null : java.math.BigInteger.valueOf((long){0}.intValue()))"),
            new ConversionEntry("java.math.BigInteger", "java.lang.Integer", "({0} == null ? null : new java.lang.Integer({0}.intValue()))"),
            new ConversionEntry("java.lang.Long", "java.math.BigInteger", "({0} == null ? null : java.math.BigInteger.valueOf({0}.longValue()))"),
            new ConversionEntry("java.math.BigInteger", "java.lang.Long", "({0} == null ? null : new java.lang.Long({0}.longValue()))"),
            new ConversionEntry("java.lang.Float", "java.math.BigInteger", "({0} == null ? null : new java.math.BigDecimal((double){0}.floatValue()).toBigInteger())"),
            new ConversionEntry("java.lang.Double", "java.math.BigInteger", "({0} == null ? null : new java.math.BigDecimal({0}.doubleValue()).toBigInteger())"),
            
            
            new ConversionEntry("int", "java.math.BigDecimal", "java.math.BigDecimal.valueOf((long){0})"),
            new ConversionEntry("java.math.BigDecimal", "int", "({0} == null ? 0 : {0}.intValue())"),
            new ConversionEntry("long", "java.math.BigDecimal", "java.math.BigDecimal.valueOf({0})"),
            new ConversionEntry("java.math.BigDecimal", "long", "({0} == null ? 0 : {0}.longValue())"),
            new ConversionEntry("java.math.BigDecimal", "float", "({0} == null ? 0.0f : {0}.floatValue())"),
            new ConversionEntry("float", "java.math.BigDecimal", "new java.math.BigDecimal((double){0})"),
            new ConversionEntry("java.math.BigDecimal", "double", "({0} == null ? 0.0 : {0}.doubleValue())"),
            new ConversionEntry("double", "java.math.BigDecimal", "new java.math.BigDecimal({0})"),
            new ConversionEntry("java.lang.String", "java.math.BigDecimal", "new java.math.BigDecimal({0})"),
            new ConversionEntry("java.math.BigDecimal", "java.lang.String", "({0} == null ? null : {0}.toString())"),
            new ConversionEntry("java.math.BigInteger", "java.math.BigDecimal", "new java.math.BigDecimal({0})"),
            new ConversionEntry("java.lang.Integer", "java.math.BigDecimal", "({0} == null ? null : java.math.BigDecimal.valueOf((long){0}.intValue()))"),
            new ConversionEntry("java.math.BigDecimal", "java.lang.Integer", "({0} == null ? null : new java.lang.Integer({0}.intValue()))"),
            new ConversionEntry("java.lang.Long", "java.math.BigDecimal", "({0} == null ? null : java.math.BigDecimal.valueOf({0}.longValue()))"),
            new ConversionEntry("java.math.BigDecimal", "java.lang.Long", "({0} == null ? null : new java.lang.Long({0}.longValue()))"),
            new ConversionEntry("java.lang.Float", "java.math.BigDecimal", "({0} == null ? null : new java.math.BigDecimal((double){0}.floatValue()))"),
            new ConversionEntry("java.lang.Double", "java.math.BigDecimal", "({0} == null ? null : new java.math.BigDecimal({0}.doubleValue()))"),

            new ConversionEntry("boolean", "java.lang.Boolean", "new java.lang.Boolean({0})"),
            new ConversionEntry("boolean", "java.lang.String", "java.lang.String.valueOf({0})"),
            new ConversionEntry("boolean", "int", "({0} ? 1 : 0)"),
            new ConversionEntry("boolean", "long", "({0} ? 1 : 0)"),
            new ConversionEntry("boolean", "double", "({0} ? 1.0 : 0.0)"),
            new ConversionEntry("boolean", "float", "({0} ? 1.0f : 0.0f)"),
            new ConversionEntry("java.lang.Boolean", "boolean", "({0} == null ? false : {0}.booleanValue())"),
            new ConversionEntry("java.lang.Boolean", "java.lang.String", "({0} == null ? null : {0}.toString())"),
            new ConversionEntry("java.lang.String", "boolean", "({0} == null ? false : Boolean.valueOf({0}).booleanValue())"),
            new ConversionEntry("java.lang.String", "java.lang.Boolean", "({0} == null ? null : Boolean.valueOf({0}))"),

            new ConversionEntry("string", "java.lang.String", "{0}"),
            new ConversionEntry("java.lang.String", "string", "{0}"),
            
            new ConversionEntry("java.util.Date", "java.lang.String",
                "({0} == null ? null : new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ssZ\").format({0}))"),
            new ConversionEntry("java.sql.Timestamp", "java.lang.String",
                "({0} == null ? null : new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ssZ\").format({0}))"),
            new ConversionEntry("java.sql.Time", "java.lang.String",
                    "({0} == null ? null : new java.text.SimpleDateFormat(\"HH:mm:ssZ\").format({0}))"),
            new ConversionEntry("java.sql.Date", "java.lang.String",
                "({0} == null ? null : new java.text.SimpleDateFormat(\"yyyy-MM-dd\").format({0}))"),
            new ConversionEntry("java.lang.String", "java.util.Date",
                "({0} == null ? null : new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ssZ\").parse({0}))"),
            new ConversionEntry("java.lang.String", "java.sql.Timestamp",
                "({0} == null ? null : new java.sql.Timestamp(new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ssZ\").parse({0}).getTime()))"),
            new ConversionEntry("java.lang.String", "java.sql.Time",
                "({0} == null ? null : new java.sql.Time(new java.text.SimpleDateFormat(\"HH:mm:ssZ\").parse({0}).getTime()))"),
            new ConversionEntry("java.lang.String", "java.sql.Date",
                "({0} == null ? null : new java.sql.Date(new java.text.SimpleDateFormat(\"yyyy-MM-dd\").parse({0}).getTime()))"),
            new ConversionEntry("java.sql.Timestamp", "java.util.Date", "{0}"),
            new ConversionEntry("java.sql.Timestamp", "java.sql.Time", "({0} == null ? null : new java.sql.Time({0}.getTime()))"),
            new ConversionEntry("java.sql.Timestamp", "java.sql.Date", "({0} == null ? null : new java.sql.Date({0}.getTime()))"),
            new ConversionEntry("java.sql.Time", "java.util.Date", "{0}"),
            new ConversionEntry("java.sql.Time", "java.sql.Timestamp", "({0} == null ? null : new java.sql.Timestamp({0}.getTime()))"),
            new ConversionEntry("java.sql.Date", "java.util.Date", "{0}"),
            new ConversionEntry("java.sql.Date", "java.sql.Timestamp", "({0} == null ? null : new java.sql.Timestamp({0}.getTime()))"),
            new ConversionEntry("java.util.Date", "java.sql.Timestamp", "({0} == null ? null : new java.sql.Timestamp({0}.getTime()))"),
            new ConversionEntry("java.util.Date", "java.sql.Time", "({0} == null ? null : new java.sql.Time({0}.getTime()))"),
            new ConversionEntry("java.util.Date", "java.sql.Date", "({0} == null ? null : new java.sql.Date({0}.getTime()))"),
            new ConversionEntry("java.util.Date", "long", "({0} == null ? 0 : {0}.getTime())"),
            new ConversionEntry("java.sql.Timestamp", "long", "({0} == null ? 0 : {0}.getTime())"),
            new ConversionEntry("java.sql.Time", "long", "({0} == null ? 0 : {0}.getTime())"),
            new ConversionEntry("java.sql.Date", "long", "({0} == null ? 0 : {0}.getTime())"),
            new ConversionEntry("long", "java.sql.Date", "new java.sql.Date({0})"),
            new ConversionEntry("long", "java.sql.Time", "new java.sql.Time({0})"),
            new ConversionEntry("long", "java.sql.Timestamp", "new java.sql.Timestamp({0})"),
            new ConversionEntry("long", "java.util.Date", "new java.util.Date({0})"),

            new ConversionEntry("java.lang.Character", "java.lang.String", "({0} == null ? null : {0}.toString())"),
            new ConversionEntry("java.lang.String", "java.lang.Character", "({0} == null || {0}.length() == 0 ? null : java.lang.Character.valueOf({0}.charAt(0)))"),
            
            new ConversionEntry("java.lang.Object", "java.lang.String", "{0} == null ? null : {0}.toString()"),
            new ConversionEntry("java.lang.Object", "java.lang.Integer", "{0} == null ? null : {0}.toString()"),
            new ConversionEntry("java.lang.Object", "java.lang.Long", "{0} == null ? null : {0}.toString()"),
            new ConversionEntry("java.lang.Object", "java.lang.Float", "{0} == null ? null : {0}.toString()"),
            new ConversionEntry("java.lang.Object", "java.lang.Double", "{0} == null ? null : {0}.toString()"),
            new ConversionEntry("java.lang.Object", "java.lang.Character", "({0} == null || {0}.toString().length() == 0 ? null : java.lang.Character.valueOf({0}.toString().charAr(0)))"),
            new ConversionEntry("java.lang.String", "java.lang.Object", "{0}"),
            new ConversionEntry("java.lang.Integer", "java.lang.Object", "{0}"),
            new ConversionEntry("java.lang.Long", "java.lang.Object", "{0}"),
            new ConversionEntry("java.lang.Float", "java.lang.Object", "{0}"),
            new ConversionEntry("java.lang.Double", "java.lang.Object", "{0}"),
            new ConversionEntry("java.lang.Character", "java.lang.Object", "{0}"),
            new ConversionEntry("java.lang.Character", "char", "({0} == null ? 0 : {0}.charValue())"),
            new ConversionEntry("char", "java.lang.Character", "java.lang.Character.valueOf({0})"),
        };

    /**
     * Attempts to code a type conversion from the specified source value to the target
     * type that can be used on the right hand side of an assignment. If such a conversion
     * exists, the Java code fragment to do that will be returned. If no such conversion exists,
     * null is returned.
     * @param sourceType The fully qualified data type of the source value
     * @param sourceValue The actual source value (usually a variable name)
     * @param targetType The fully qualified data type of the target value
     * @return The converted value, or null if no conversion can be done.
     */
    public String typeConvert(
        final String sourceType,
        final String sourceValue,
        final String targetType)
    {
        String convertedValue;

        if (StringUtils.isBlank(sourceType) || StringUtils.isBlank(targetType) ||
            javaTypeConversionIgnoreList.contains(sourceType) || javaTypeConversionIgnoreList.contains(targetType))
        {
            convertedValue = null;
        }
        else if (sourceType.equals(targetType))
        {
            convertedValue = sourceValue;
        }
        else
        {
            convertedValue = null;

            for (int i = 0; i < conversionTable.length && convertedValue == null; i++)
            {
                if (conversionTable[i].sourceType.equals(sourceType) &&
                    conversionTable[i].targetType.equals(targetType))
                {
                    convertedValue =
                        MessageFormat.format(
                            conversionTable[i].conversionPattern,
                                sourceValue);
                } // if
            } // for

            if (convertedValue == null && sourceType.startsWith("java.lang.") && !sourceType.endsWith("[]"))
            {
                // If source is a primitive wrapper, try to convert
                // to the base primitive first...
                String primitiveSource = sourceType.substring(10).toLowerCase();
                if ("integer".equals(primitiveSource))
                {
                    primitiveSource = "int";
                }
                else if ("character".equals(primitiveSource)) {
                    primitiveSource = "char";
                }

                String interimValue = typeConvert(
                        sourceType,
                        sourceValue,
                        primitiveSource);
                
                if (interimValue != null)
                {
                    convertedValue = typeConvert(
                            primitiveSource,
                            interimValue,
                            targetType);
                }
            }

            if (convertedValue == null && targetType.startsWith("java.lang.") && !targetType.endsWith("[]"))
            {
                // One last try - if target is a primitive wrapper, try to convert
                // to the base primitive first...
                String primitiveTarget = targetType.substring(10).toLowerCase();
                if ("integer".equals(primitiveTarget))
                {
                    primitiveTarget = "int";
                }
                else if ("character".equals(primitiveTarget)) {
                    primitiveTarget = "char";
                }
                
                String interimValue = typeConvert(
                        sourceType,
                        sourceValue,
                        primitiveTarget);
                if (interimValue != null)
                {
                    
                    convertedValue = typeConvert(
                            primitiveTarget,
                            interimValue,
                            targetType);
                }
            }
        }

        return convertedValue;
    }

    private final static List<String> javaLangList = new ArrayList<String>();
    /**
     * Creates a java.lang. fully qualified name from the given <code>name</code>,
     * inserting 'java.lang.' in front if needed. Many EJB3 and Hibernate configuration files
     * require java.lang. in the type name, where Java code does not (gives compiler warning).
     * This allows UML Primitive types to be modeled and mapped without java.lang. implementation.
     *
     * @param name the name of the model element.
     * @return the java.lang. fully qualified name, if needed.
     */
    public static String getJavaLangTypeName(String name)
    {
        if (StringUtils.isBlank(name))
        {
            return name;
        }
        synchronized (javaLangList)
        {
            if (javaLangList.isEmpty())
            {
                javaLangList.add("String");
                javaLangList.add("Boolean");
                javaLangList.add("Integer");
                javaLangList.add("Long");
                javaLangList.add("Double");
                javaLangList.add("Object");
                javaLangList.add("Short");
                javaLangList.add("Character");
                javaLangList.add("Byte");
                javaLangList.add("Float");
                javaLangList.add("Number");
                javaLangList.add("Math");
                javaLangList.add("Class");
                javaLangList.add("Exception");
                javaLangList.add("Enum");
            }
        }
        // If type has no package and is not the same as lowercase (i.e. primitive type)...
        if (!(name.indexOf('.')>-1) && !(name.equals(name.toLowerCase())))
        {
            if (javaLangList.contains(name) 
                || (name.endsWith("[]") && javaLangList.contains(name.substring(0, name.length()-2))))
            {
                name = "java.lang." + name;
            }
        }
        return name;
    }
}