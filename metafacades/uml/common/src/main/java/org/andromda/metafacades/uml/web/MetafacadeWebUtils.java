package org.andromda.metafacades.uml.web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndAttribute;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

public class MetafacadeWebUtils {
    
    /**
     * Converts the argument into a web resource name, this means: all lowercase
     * characters and words are separated with dashes.
     *
     * @param string any string
     * @return the string converted to a value that would be well-suited for a
     *         web file name
     */
    public static String toWebResourceName(final String string)
    {
        return StringUtilsHelper.separate(
            string,
            "-").toLowerCase();
    }

    private static final Pattern VALIDATOR_TAGGEDVALUE_PATTERN =
        Pattern.compile("\\w+(\\(\\w+=[^,)]*(,\\w+=[^,)]*)*\\))?");

    private static final String ANNOTATION_VALIDATOR_PREFIX = "@";

    /**
     * Reads the validator arguments from the the given tagged value.
     * @param validatorTaggedValue
     * @return returns a list of String instances or an empty list
     * @throws IllegalArgumentException when the input string does not match the required pattern
     */
    public static List<String> parseValidatorArgs(String validatorTaggedValue)
    {
        if (validatorTaggedValue == null)
        {
            throw new IllegalArgumentException("Validator tagged value cannot be null");
        }

        final List<String> validatorArgs = new ArrayList<String>();

        //isn't it an annotation ?
        if(!StringUtils.startsWith(validatorTaggedValue,ANNOTATION_VALIDATOR_PREFIX))
        {

            // check if the input tagged value matches the required pattern
            if (!VALIDATOR_TAGGEDVALUE_PATTERN.matcher(validatorTaggedValue).matches())
            {
                throw new IllegalArgumentException(
                    "Illegal validator tagged value (this tag is used to specify custom validators " +
                    "and might look like myValidator(myVar=myArg,myVar2=myArg2), perhaps you wanted to use " +
                    "andromda_presentation_view_field_format?): " + validatorTaggedValue);
            }

            // only keep what is between parentheses (if any)
            int left = validatorTaggedValue.indexOf('(');
            if (left > -1)
            {
                final int right = validatorTaggedValue.indexOf(')');
                validatorTaggedValue = validatorTaggedValue.substring(
                        left + 1,
                        right);

                final String[] pairs = validatorTaggedValue.split(",");
                for (int i = 0; i < pairs.length; i++)
                {
                    final String pair = pairs[i];
                    final int equalsIndex = pair.indexOf('=');

                    // it's possible the argument is the empty string
                    if (equalsIndex < pair.length() - 1)
                    {
                        validatorArgs.add(pair.substring(equalsIndex + 1));
                    }
                    else
                    {
                        validatorArgs.add("");
                    }
                }
            }
        }
        return validatorArgs;
    }

    /**
     * Gets the validator variables for the given <code>element</code> (if they can
     * be retrieved).
     *
     * @param element the element from which to retrieve the variables
     * @param type the type of the element.
     * @param ownerParameter the optional owner parameter (if the element is an attribute for example).
     * @return the collection of validator variables.
     */
    public static Collection<List<String>> getValidatorVars(
        final ModelElementFacade element,
        final ClassifierFacade type,
        final ParameterFacade ownerParameter)
    {
        final Map<String, List<String>> vars = new LinkedHashMap<String, List<String>>();
        if (element != null && type != null)
        {
            final String format = MetafacadeWebUtils.getInputFormat(element);
            if (format != null)
            {
                final boolean isRangeFormat = MetafacadeWebUtils.isRangeFormat(format);

                if (isRangeFormat)
                {
                    final String min = "min";
                    final String max = "max";
                    vars.put(
                        min,
                        Arrays.asList(min, MetafacadeWebUtils.getRangeStart(format)));
                    vars.put(
                        max,
                        Arrays.asList(max, MetafacadeWebUtils.getRangeEnd(format)));
                }
                else
                {
                    final Collection formats = element.findTaggedValues(MetafacadeWebProfile.TAGGEDVALUE_INPUT_FORMAT);
                    for (final Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
                    {
                        final String additionalFormat = String.valueOf(formatIterator.next());
                        final String minlength = "minlength";
                        final String maxlength = "maxlength";
                        final String mask = "mask";
                        if (MetafacadeWebUtils.isMinLengthFormat(additionalFormat))
                        {
                            vars.put(
                                minlength,
                                Arrays.asList(minlength, MetafacadeWebUtils.getMinLengthValue(additionalFormat)));
                        }
                        else if (MetafacadeWebUtils.isMaxLengthFormat(additionalFormat))
                        {
                            vars.put(
                                maxlength,
                                Arrays.asList(maxlength, MetafacadeWebUtils.getMaxLengthValue(additionalFormat)));
                        }
                        else if (MetafacadeWebUtils.isPatternFormat(additionalFormat))
                        {
                            vars.put(
                                mask,
                                Arrays.asList(mask, MetafacadeWebUtils.getPatternValue(additionalFormat)));
                        }
                    }
                }
            }
            String inputFormat;
            if (element instanceof FrontEndAttribute)
            {
                inputFormat = ((FrontEndAttribute)element).getFormat();
            }
            else if (element instanceof FrontEndParameter)
            {
                inputFormat = ((FrontEndParameter)element).getFormat();
            }
            //else if (element instanceof MetafacadeWebManageableEntityAttribute)
            //{
            //    inputFormat = ((MetafacadeWebManageableEntityAttribute)element).getFormat();
            //}
            else
            {
                throw new RuntimeException("'element' is an invalid type, it must be either an instance of '" +
                    FrontEndAttribute.class.getName() + "' or '" + FrontEndParameter.class.getName() + "'");
            }
            if (MetafacadeWebUtils.isDate(type))
            {
                final String datePatternStrict = "datePatternStrict";
                if (format != null && MetafacadeWebUtils.isStrictDateFormat(format))
                {
                    vars.put(
                        datePatternStrict,
                        Arrays.asList(datePatternStrict, inputFormat));
                }
                else
                {
                    final String datePattern = "datePattern";
                    vars.put(
                        datePattern,
                        Arrays.asList(datePattern, inputFormat));
                }
            }
            if (MetafacadeWebUtils.isTime(type))
            {
                final String timePattern = "timePattern";
                vars.put(
                    timePattern,
                    Arrays.asList(timePattern, inputFormat));
            }

            final String validWhen = MetafacadeWebUtils.getValidWhen(element);
            if (validWhen != null)
            {
                final String test = "test";
                vars.put(
                    test,
                    Arrays.asList(test, validWhen));
            }

            final String equal = MetafacadeWebUtils.getEqual(element, ownerParameter);
            if (equal != null)
            {
                final String fieldName = "fieldName";
                vars.put(
                    fieldName,
                    Arrays.asList(fieldName, equal));
            }

            // - custom (parameterized) validators are allowed here
            //   in this case we will reuse the validator arg values
            final Collection taggedValues = element.findTaggedValues(MetafacadeWebProfile.TAGGEDVALUE_INPUT_VALIDATORS);
            for (final Object value : taggedValues)
            {
                final String validator = String.valueOf(value);

                // - guaranteed to be of the same length
                final List<String> validatorVars = MetafacadeWebUtils.parseValidatorVars(validator);
                final List<String> validatorArgs = MetafacadeWebUtils.parseValidatorArgs(validator);

                for (int ctr = 0; ctr < validatorVars.size(); ctr++)
                {
                    vars.put(validatorVars.get(ctr),
                        Arrays.asList(validatorVars.get(ctr), validatorArgs.get(ctr)));
                }
            }
        }
        return vars.values();
    }

    /**
     * Reads the validator variable names from the the given tagged value.
     * @param validatorTaggedValue
     * @return never null, returns a list of String instances
     * @throws IllegalArgumentException when the input string does not match the required pattern
     */
    public static List<String> parseValidatorVars(String validatorTaggedValue)
    {
        if (validatorTaggedValue == null)
        {
            throw new IllegalArgumentException("Validator tagged value cannot be null");
        }

        final List<String> validatorVars = new ArrayList<String>();

        //isn't it an annotation ?
        if(!StringUtils.startsWith(validatorTaggedValue,ANNOTATION_VALIDATOR_PREFIX))
        {

            // check if the input tagged value matches the required pattern
            if (!VALIDATOR_TAGGEDVALUE_PATTERN.matcher(validatorTaggedValue).matches())
            {
                throw new IllegalArgumentException("Illegal validator tagged value: " + validatorTaggedValue);
            }

            // only keep what is between parentheses (if any)
            int left = validatorTaggedValue.indexOf('(');
            if (left > -1)
            {
                int right = validatorTaggedValue.indexOf(')');
                validatorTaggedValue = validatorTaggedValue.substring(
                        left + 1,
                        right);

                final String[] pairs = validatorTaggedValue.split(",");
                for (int i = 0; i < pairs.length; i++)
                {
                    final String pair = pairs[i];
                    final int equalsIndex = pair.indexOf('=');
                    validatorVars.add(pair.substring(
                            0,
                            equalsIndex));
                }
            }
        }
        return validatorVars;
    }

    /**
     * Parses the validator name for a tagged value.
     * @param validatorTaggedValue
     * @return validatorTaggedValue
     * @throws IllegalArgumentException when the input string does not match the required pattern
     */
    public static String parseValidatorName(final String validatorTaggedValue)
    {
        if (validatorTaggedValue == null)
        {
            throw new IllegalArgumentException("Validator tagged value cannot be null");
        }

        //isn't it an annotation ?
        if(StringUtils.startsWith(validatorTaggedValue, ANNOTATION_VALIDATOR_PREFIX))
        {
            return validatorTaggedValue;
        }

        // check if the input tagged value matches the required pattern
        if (!VALIDATOR_TAGGEDVALUE_PATTERN.matcher(validatorTaggedValue).matches())
        {
            throw new IllegalArgumentException("Illegal validator tagged value: " + validatorTaggedValue);
        }

        final int leftParen = validatorTaggedValue.indexOf('(');
        return (leftParen == -1) ? validatorTaggedValue : validatorTaggedValue.substring(
            0,
            leftParen);
    }

    /**
     * Constructs a string representing an array initialization in Java.
     *
     * @param name the name to give the array.
     * @param count the number of items to give the array.
     * @return A String representing Java code for the initialization of an array.
     */
    public static String constructDummyArrayDeclaration(
        final String name,
        final int count)
    {
        final StringBuilder array = new StringBuilder("new Object[] {");
        for (int ctr = 1; ctr <= count; ctr++)
        {
            array.append("\"" + name + "-" + ctr + "\"");
            if (ctr != count)
            {
                array.append(", ");
            }
        }
        array.append("}");
        return array.toString();
    }

    /**
     * @param format
     * @return this field's date format
     */
    public static String getDateFormat(String format)
    {
        format = StringUtils.trimToEmpty(format);
        return format.endsWith(STRICT) ? getToken(format, 1, 2) : getToken(format, 0, 1);
    }

    private static String defaultDateFormat = "MM/dd/yyyy HH:mm:ssZ";
    private static FastDateFormat df = FastDateFormat.getInstance(defaultDateFormat);

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
     * Returns the current Date
     *
     * @return the current date in the default format.
     */
    public static String getDate()
    {
        return getDate(defaultDateFormat);
    }

    private static final String STRICT = "strict";

    /**
     * @param format
     * @return <code>true</code> if this field's value needs to conform to a
     * strict date format, <code>false</code> otherwise
     */
    public static boolean isStrictDateFormat(String format)
    {
        return strictDateTimeFormat ? strictDateTimeFormat : STRICT.equalsIgnoreCase(getToken(
                format,
                0,
                2));
    }

    /**
     * Indicates if the given <code>format</code> is an email format.
     * @param format
     * @return <code>true</code> if this field is to be formatted as an email
     * address, <code>false</code> otherwise
     */
    public static boolean isEmailFormat(String format)
    {
        return "email".equalsIgnoreCase(MetafacadeWebUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * Indicates if the given <code>format</code> is an equal format.
     * @param format
     * @return <code>true</code> if this field is to be formatted as an
     * email address, <code>false</code> otherwise
     */
    public static boolean isEqualFormat(String format)
    {
        return "equal".equalsIgnoreCase(MetafacadeWebUtils.getToken(format, 0, 2));
    }

    /**
     * Indicates if the given <code>format</code> is a credit card format.
     * @param format
     * @return <code>true</code> if this field is to be formatted as a credit card, <code>false</code> otherwise
     */
    public static boolean isCreditCardFormat(final String format)
    {
        return "creditcard".equalsIgnoreCase(MetafacadeWebUtils.getToken(format, 0, 2));
    }

    /**
     * Indicates if the given <code>format</code> is a pattern format.
     * @param format
     * @return <code>true</code> if this field's value needs to respect a certain pattern, <code>false</code> otherwise
     */
    public static boolean isPatternFormat(final String format)
    {
        return "pattern".equalsIgnoreCase(MetafacadeWebUtils.getToken(format, 0, 2));
    }

    /**
     * Indicates if the given <code>format</code> is a minlength format.
     * @param format
     * @return <code>true</code> if this field's value needs to consist of at least a certain
     *         number of characters, <code>false</code> otherwise
     */
    public static boolean isMinLengthFormat(final String format)
    {
        return "minlength".equalsIgnoreCase(MetafacadeWebUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * Indicates if the given <code>format</code> is a maxlength format.
     * @param format
     * @return <code>true</code> if this field's value needs to consist of at maximum a certain
     *         number of characters, <code>false</code> otherwise
     */
    public static boolean isMaxLengthFormat(String format)
    {
        return "maxlength".equalsIgnoreCase(MetafacadeWebUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * @param string
     * @param index
     * @param limit
     * @return the i-th space delimited token read from the argument String, where i does not exceed the specified limit
     */
    public static String getToken(
        String string,
        int index,
        int limit)
    {
        String token = null;
        if (string != null && string.length() > 0)
        {
            final String[] tokens = string.split(
                    "[\\s]+",
                    limit);
            token = index >= tokens.length ? null : tokens[index];
        }
        return token;
    }

    /**
     * Retrieves the input format (if one is defined), for the given
     * <code>element</code>.
     * @param element the model element for which to retrieve the input format.
     * @return the input format.
     */
    public static String getInputFormat(final ModelElementFacade element)
    {
        final Object value = element.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_INPUT_FORMAT);
        final String format = value == null ? null : String.valueOf(value);
        return format == null ? null : format.trim();
    }

    /**
     * Indicates if the given <code>format</code> is a range format.
     * @param format
     * @return <code>true</code> if this field's value needs to be in a specific range, <code>false</code> otherwise
     */
    public static boolean isRangeFormat(final String format)
    {
        return "range".equalsIgnoreCase(MetafacadeWebUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a byte, <code>false</code> otherwise
     */
    public static boolean isByte(final ClassifierFacade type)
    {
        return isType(
            type,
            MetafacadeWebProfile.BYTE_TYPE_NAME);
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a short, <code>false</code> otherwise
     */
    public static boolean isShort(final ClassifierFacade type)
    {
        return isType(
            type,
            MetafacadeWebProfile.SHORT_TYPE_NAME);
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is an integer, <code>false</code> otherwise
     */
    public static boolean isInteger(final ClassifierFacade type)
    {
        return isType(
            type,
            MetafacadeWebProfile.INTEGER_TYPE_NAME);
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a long integer, <code>false</code> otherwise
     */
    public static boolean isLong(final ClassifierFacade type)
    {
        return isType(
            type,
            MetafacadeWebProfile.LONG_TYPE_NAME);
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a floating point, <code>false</code> otherwise
     */
    public static boolean isFloat(final ClassifierFacade type)
    {
        return isType(
            type,
            MetafacadeWebProfile.FLOAT_TYPE_NAME);
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a double precision floating point,
     * <code>false</code> otherwise
     */
    public static boolean isDouble(final ClassifierFacade type)
    {
        return isType(
            type,
            MetafacadeWebProfile.DOUBLE_TYPE_NAME);
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a date, <code>false</code> otherwise
     */
    public static boolean isDate(final ClassifierFacade type)
    {
        return type != null && type.isDateType();
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a time, <code>false</code> otherwise
     */
    public static boolean isTime(final ClassifierFacade type)
    {
        return isType(
            type,
            MetafacadeWebProfile.TIME_TYPE_NAME);
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a URL, <code>false</code> otherwise
     */
    public static boolean isUrl(final ClassifierFacade type)
    {
        return isType(
            type,
            MetafacadeWebProfile.URL_TYPE_NAME);
    }

    private static boolean isType(final ClassifierFacade type, String typeName)
    {
        boolean isType = UMLMetafacadeUtils.isType(
            type,
            typeName);
        if (!isType)
        {
            // - handle abstract types that are mapped to java types
            if (type.getLanguageMappings() != null)
            {
                final String javaTypeName = type.getLanguageMappings()
                    .getTo(type.getFullyQualifiedName(true));
                if (javaTypeName != null)
                {
                    isType = javaTypeName.replaceAll(".*\\.", "").equalsIgnoreCase(
                        type.getLanguageMappings().getTo(typeName));
                }
            }
        }
        return isType;
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a String,
     * <code>false</code> otherwise
     */
    public static boolean isString(final ClassifierFacade type)
    {
        return type != null && type.isStringType();
    }

    /**
     * Indicates if the given element is read-only or not.
     *
     * @param element the element to check.
     * @return true/false
     */
    public static boolean isReadOnly(final ModelElementFacade element)
    {
        boolean readOnly = false;
        if (element != null)
        {
            final Object value = element.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_INPUT_READONLY);
            readOnly = Boolean.valueOf(ObjectUtils.toString(value)).booleanValue();
        }
        return readOnly;
    }

    /**
     * Retrieves the "equal" value from the given element (if one is present).
     *
     * @param element the element from which to retrieve the equal value.
     * @return the "equal" value.
     */
    public static String getEqual(final ModelElementFacade element)
    {
        String equal = null;
        if (element != null)
        {
            final Object value = element.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_INPUT_EQUAL);
            equal = value == null ? null : value.toString();
        }
        return equal;
    }

    /**
     * Retrieves the "equal" value from the given element (if one is present).
     *
     * @param element the element from which to retrieve the equal value.
     * @param ownerParameter the optional owner parameter (specified if the element is an attribute).
     * @return the "equal" value.
     */
    public static String getEqual(final ModelElementFacade element, final ParameterFacade ownerParameter)
    {
        String equal = null;
        if (element != null)
        {
            final Object value = element.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_INPUT_EQUAL);
            equal = value == null ? null : value.toString();
            if (StringUtils.isNotBlank(equal) && ownerParameter != null)
            {
                equal = ownerParameter.getName() + StringUtilsHelper.upperCamelCaseName(equal);
            }
        }
        return equal;
    }

    /**
     * Retrieves the "validwhen" value from the given element (if one is present).
     *
     * @param element the element from which to retrieve the validwhen value.
     * @return the "validwhen" value.
     */
    public static String getValidWhen(final ModelElementFacade element)
    {
        String validWhen = null;
        if (element != null)
        {
            final Object value = element.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_INPUT_VALIDWHEN);
            validWhen = value == null ? null : '(' + value.toString() + ')';
        }
        return validWhen;
    }

    /**
     * @param format
     * @return the lower limit for this field's value's range
     */
    public static String getRangeStart(final String format)
    {
        return MetafacadeWebUtils.getToken(
            format,
            1,
            3);
    }

    /**
     * @param format
     * @return the upper limit for this field's value's range
     */
    public static String getRangeEnd(final String format)
    {
        return MetafacadeWebUtils.getToken(
            format,
            2,
            3);
    }

    /**
     * @param format
     * @return the minimum number of characters this field's value must consist of
     */
    public static String getMinLengthValue(final String format)
    {
        return MetafacadeWebUtils.getToken(
            format,
            1,
            2);
    }

    /**
     * @param format
     * @return the maximum number of characters this field's value must consist of
     */
    public static String getMaxLengthValue(final String format)
    {
        return MetafacadeWebUtils.getToken(
            format,
            1,
            2);
    }

    /**
     * @param format
     * @return the pattern this field's value must respect
     */
    public static String getPatternValue(final String format)
    {
        return '^' + MetafacadeWebUtils.getToken(
            format,
            1,
            2) + '$';
    }

    //validator strings
    /** "required" */
    public static final String VT_REQUIRED="required";
    /** "url" */
    public static final String VT_URL="url";
    /** "intRange" */
    public static final String VT_INT_RANGE="intRange";
    /** "floatRange" */
    public static final String VT_FLOAT_RANGE="floatRange";
    /** "doubleRange" */
    public static final String VT_DOUBLE_RANGE="doubleRange";
    /** "email" */
    public static final String VT_EMAIL="email";
    /** "creditCard" */
    public static final String VT_CREDIT_CARD="creditCard";
    /** "minlength" */
    public static final String VT_MIN_LENGTH="minlength";
    /** "maxlength" */
    public static final String VT_MAX_LENGTH="maxlength";
    /** "mask" */
    public static final String VT_MASK="mask";
    /** "validwhen" */
    public static final String VT_VALID_WHEN="validwhen";
    /** "equal" */
    public static final String VT_EQUAL="equal";

    /**
     * Retrieves the validator types as a collection from the given
     * <code>element</code> (if any can be retrieved).
     *
     * @param element the element from which to retrieve the types.
     * @param type the type of the element.
     * @return the collection of validator types.
     */
    public static Collection<String> getValidatorTypes(
        final ModelElementFacade element,
        final ClassifierFacade type)
    {
        final Collection<String> validatorTypesList = new ArrayList<String>();
        if (element != null && type != null)
        {
            final String format = MetafacadeWebUtils.getInputFormat(element);
            final boolean isRangeFormat = format != null && isRangeFormat(format);
            if (element instanceof AttributeFacade)
            {
                if (((AttributeFacade)element).isRequired())
                {
                    validatorTypesList.add(VT_REQUIRED);
                }
            }
            else if (element instanceof ParameterFacade)
            {
                if (((ParameterFacade)element).isRequired())
                {
                    validatorTypesList.add(VT_REQUIRED);
                }
            }
            if (MetafacadeWebUtils.isByte(type))
            {
                validatorTypesList.add("byte");
            }
            else if (MetafacadeWebUtils.isShort(type))
            {
                validatorTypesList.add("short");
            }
            else if (MetafacadeWebUtils.isInteger(type))
            {
                validatorTypesList.add("integer");
            }
            else if (MetafacadeWebUtils.isLong(type))
            {
                validatorTypesList.add("long");
            }
            else if (MetafacadeWebUtils.isFloat(type))
            {
                validatorTypesList.add("float");
            }
            else if (MetafacadeWebUtils.isDouble(type))
            {
                validatorTypesList.add("double");
            }
            else if (MetafacadeWebUtils.isDate(type))
            {
                validatorTypesList.add("date");
            }
            else if (MetafacadeWebUtils.isTime(type))
            {
                validatorTypesList.add("time");
            }
            else if (MetafacadeWebUtils.isUrl(type))
            {
                validatorTypesList.add(VT_URL);
            }

            if (isRangeFormat)
            {
                if (MetafacadeWebUtils.isInteger(type) || MetafacadeWebUtils.isShort(type) || MetafacadeWebUtils.isLong(type))
                {
                    validatorTypesList.add(VT_INT_RANGE);
                }
                if (MetafacadeWebUtils.isFloat(type))
                {
                    validatorTypesList.add(VT_FLOAT_RANGE);
                }
                if (MetafacadeWebUtils.isDouble(type))
                {
                    validatorTypesList.add(VT_DOUBLE_RANGE);
                }
            }

            if (format != null)
            {
                if (MetafacadeWebUtils.isString(type) && MetafacadeWebUtils.isEmailFormat(format))
                {
                    validatorTypesList.add(VT_EMAIL);
                }
                else if (MetafacadeWebUtils.isString(type) && MetafacadeWebUtils.isCreditCardFormat(format))
                {
                    validatorTypesList.add(VT_CREDIT_CARD);
                }
                else
                {
                    Collection formats = element.findTaggedValues(MetafacadeWebProfile.TAGGEDVALUE_INPUT_FORMAT);
                    for (final Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
                    {
                        String additionalFormat = String.valueOf(formatIterator.next());
                        if (MetafacadeWebUtils.isMinLengthFormat(additionalFormat))
                        {
                            validatorTypesList.add(VT_MIN_LENGTH);
                        }
                        else if (MetafacadeWebUtils.isMaxLengthFormat(additionalFormat))
                        {
                            validatorTypesList.add(VT_MAX_LENGTH);
                        }
                        else if (MetafacadeWebUtils.isPatternFormat(additionalFormat))
                        {
                            validatorTypesList.add(VT_MASK);
                        }
                    }
                }
            }
            if (MetafacadeWebUtils.getValidWhen(element) != null)
            {
                validatorTypesList.add(VT_VALID_WHEN);
            }
            if (MetafacadeWebUtils.getEqual(element) != null)
            {
                validatorTypesList.add(VT_EQUAL);
            }

            // - custom (paramterized) validators are allowed here
            final Collection taggedValues = element.findTaggedValues(MetafacadeWebProfile.TAGGEDVALUE_INPUT_VALIDATORS);
            for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
            {
                String validator = String.valueOf(iterator.next());
                validatorTypesList.add(MetafacadeWebUtils.parseValidatorName(validator));
            }
        }
        return validatorTypesList;
    }

    /**
     * Gets the validator args for the <code>element</code> and the given <code>validatorType</code>.
     *
     * @param element the element for which to retrieve the arguments.
     * @param validatorType the validator type name.
     * @return the validator args as a collection.
     */
    public static java.util.Collection getValidatorArgs(
        final ModelElementFacade element,
        final String validatorType)
    {
        final Collection<Object> args = new ArrayList<Object>();
        if ("intRange".equals(validatorType) || "floatRange".equals(validatorType) ||
            "doubleRange".equals(validatorType))
        {
            args.add("${var:min}");
            args.add("${var:max}");
        }
        else if ("minlength".equals(validatorType))
        {
            args.add("${var:minlength}");
        }
        else if ("maxlength".equals(validatorType))
        {
            args.add("${var:maxlength}");
        }
        else if ("date".equals(validatorType))
        {
            final String validatorFormat = MetafacadeWebUtils.getInputFormat(element);
            if (MetafacadeWebUtils.isStrictDateFormat(validatorFormat))
            {
                args.add("${var:datePatternStrict}");
            }
            else
            {
                args.add("${var:datePattern}");
            }
        }
        else if ("time".equals(validatorType))
        {
            args.add("${var:timePattern}");
        }
        else if ("equal".equals(validatorType))
        {
            ModelElementFacade equalParameter = null;
            final String equal = MetafacadeWebUtils.getEqual(element);
            if (element instanceof ParameterFacade)
            {
                final FrontEndParameter parameter = (FrontEndParameter)element;
                final OperationFacade operation = parameter.getOperation();
                if (operation != null)
                {
                    equalParameter = operation.findParameter(equal);
                }
                if (equalParameter == null)
                {
                    final FrontEndAction action = parameter.getAction();
                    if (action != null)
                    {
                        equalParameter = action.findParameter(equal);
                    }
                }
            }
            else if (element instanceof AttributeFacade)
            {
                final AttributeFacade attribute = (AttributeFacade)element;
                final ClassifierFacade owner = attribute.getOwner();
                if (owner != null)
                {
                    equalParameter = owner.findAttribute(equal);
                }
            }
            args.add(equalParameter);
            args.add("${var:fieldName}");
        }

        // custom (paramterized) validators are allowed here
        final Collection taggedValues = element.findTaggedValues(MetafacadeWebProfile.TAGGEDVALUE_INPUT_VALIDATORS);
        for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
        {
            final String validator = String.valueOf(iterator.next());
            if (validatorType.equals(MetafacadeWebUtils.parseValidatorName(validator)))
            {
                args.addAll(MetafacadeWebUtils.parseValidatorArgs(validator));
            }
        }
        return args;
    }

    /**
     * Whether or not date patterns should be treated as strict.
     */
    private static boolean strictDateTimeFormat;

    /**
     * Sets whether or not the date patterns should be treated as strict.
     *
     * @param strictDateTimeFormat
     */
    public void setStrictDateTimeFormat(final boolean strictDateTimeFormat)
    {
        MetafacadeWebUtils.strictDateTimeFormat = strictDateTimeFormat;
    }

    /**
     * Indicates whether or not the format for this element is a strict date
     * format.
     * @param element
     * @return true/false
     */
    public static boolean isStrictDateFormat(final ModelElementFacade element)
    {
        final String format = MetafacadeWebUtils.getInputFormat(element);
        return MetafacadeWebUtils.isStrictDateFormat(format);
    }

    /**
     * Gets the format string for the given <code>element</code>.
     *
     * @param element the element for which to retrieve the format.
     * @param type the type of the element.
     * @param defaultDateFormat
     * @param defaultTimeFormat
     * @return the format string (if one is present otherwise null).
     */
    public static String getFormat(
        final ModelElementFacade element,
        final ClassifierFacade type,
        final String defaultDateFormat,
        final String defaultTimeFormat)
    {
        String format = null;
        if (element != null && type != null)
        {
            format = MetafacadeWebUtils.getInputFormat(element);
            if (format == null)
            {
                if(type.isDateType() && type.isTimeType())
                {
                    format = defaultDateFormat+" "+defaultTimeFormat;
                }
                else if (type.isTimeType())
                {
                    format = defaultTimeFormat;
                }
                else if (type.isDateType())
                {
                    format = defaultDateFormat;
                }
            }
            else if (type.isDateType())
            {
                format = MetafacadeWebUtils.getDateFormat(format);
            }
        }
        return format;
    }

    //TODO remover
    private boolean isPortlet()
    {
        return false;
    }

    /**
     * @param buffer
     * @return the calculated SerialVersionUID
     */
    public static String calcSerialVersionUID(StringBuilder buffer)
    {
        final String signature = buffer.toString();
        String serialVersionUID = String.valueOf(0L);
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] hashBytes = md.digest(signature.getBytes());

            long hash = 0;
            for (int ctr = Math.min(
                        hashBytes.length,
                        8) - 1; ctr >= 0; ctr--)
            {
                hash = (hash << 8) | (hashBytes[ctr] & 0xFF);
            }
            serialVersionUID = String.valueOf(hash);
        }
        catch (final NoSuchAlgorithmException exception)
        {
            throw new RuntimeException("Error performing AndromdaWebAction.getFormSerialVersionUID",exception);
        }

        return serialVersionUID;
    }

    /**
     * @param string
     * @return Integer.valueOf(string) * 6000
     */
    public int calculateIcefacesTimeout(String string)
    {
        return string != null ? Integer.valueOf(string) * 6000 : 0;
    }

    
    
    /**
     * Creates a fully qualified name from the given <code>packageName</code>, <code>name</code>, and
     * <code>suffix</code>.
     *
     * @param packageName the name of the model element package.
     * @param name        the name of the model element.
     * @param suffix      the suffix to append.
     * @return the new fully qualified name.
     */
    public static String getFullyQualifiedName(String packageName, String name, String suffix)
    {
        StringBuilder fullyQualifiedName = new StringBuilder(StringUtils.trimToEmpty(packageName));
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append('.');
        }
        fullyQualifiedName.append(StringUtils.trimToEmpty(name));
        if (StringUtils.isNotBlank(suffix))
        {
            fullyQualifiedName.append(StringUtils.trimToEmpty(suffix));
        }
        return fullyQualifiedName.toString();
    }

    public static String getInputAction(ModelElementFacade modelElement) {

        return StringUtils.trimToNull((String) modelElement.findTaggedValue(MetafacadeWebGlobals.INPUT_ACTION));

    }

    public static String getEnctype(ModelElementFacade modelElement) {

        System.out.println("=====================================" + modelElement);
        System.out.println(modelElement.getTaggedValues());

        String enctype = StringUtils.stripToNull(((String) modelElement.findTaggedValue(UMLProfile.TAGGEDVALUE_PRESENTATION_FORM_ENCTYPE)));

        if(enctype == null) {
            return enctype;
        }

        if(enctype.equals("plain")) {
            return MetafacadeWebGlobals.ENCTYPE_PLAIN;
        } else if(enctype.equals("form_data")) {
            return MetafacadeWebGlobals.ENCTYPE_FORM_DATA;
        } else if(enctype.equals("urlencoded")) {
            return MetafacadeWebGlobals.ENCTYPE_URLENCODED;
        }

        return enctype;
    }
}

