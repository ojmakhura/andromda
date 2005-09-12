package org.andromda.cartridges.jsf;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.ObjectUtils;


/**
 * Utilties for use within the JSF cartridge.
 *
 * @author Chad Brandon
 */
public class JSFUtils
{
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

    /**
     * Reads the validator arguments from the the given tagged value.
     *
     * @return returns a list of String instances or an empty list
     * @throws IllegalArgumentException when the input string does not match the required pattern
     */
    public static List parseValidatorArgs(String validatorTaggedValue)
    {
        if (validatorTaggedValue == null)
        {
            throw new IllegalArgumentException("Validator tagged value cannot be null");
        }

        // check if the input tagged value matches the required pattern
        if (!VALIDATOR_TAGGEDVALUE_PATTERN.matcher(validatorTaggedValue).matches())
        {
            throw new IllegalArgumentException(
                "Illegal validator tagged value (this tag is used to specify custom validators " +
                "and might look like myValidator(myVar=myArg,myVar2=myArg2), perhaps you wanted to use " +
                "@andromda.presentation.view.field.format?): " + validatorTaggedValue);
        }

        final List validatorArgs = new ArrayList();

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
        return validatorArgs;
    }

    /**
     * Reads the validator variable names from the the given tagged value.
     *
     * @return never null, returns a list of String instances
     * @throws IllegalArgumentException when the input string does not match the required pattern
     */
    public static List parseValidatorVars(String validatorTaggedValue)
    {
        if (validatorTaggedValue == null)
        {
            throw new IllegalArgumentException("Validator tagged value cannot be null");
        }

        // check if the input tagged value matches the required pattern
        if (!VALIDATOR_TAGGEDVALUE_PATTERN.matcher(validatorTaggedValue).matches())
        {
            throw new IllegalArgumentException("Illegal validator tagged value: " + validatorTaggedValue);
        }

        final List validatorVars = new ArrayList();

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
        return validatorVars;
    }

    /**
     * Parses the validator name for a tagged value.
     *
     * @throws IllegalArgumentException when the input string does not match the required pattern
     */
    public static String parseValidatorName(final String validatorTaggedValue)
    {
        if (validatorTaggedValue == null)
        {
            throw new IllegalArgumentException("Validator tagged value cannot be null");
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
        final StringBuffer array = new StringBuffer("new Object[] {");
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
     * @return this field's date format
     */
    public static String getDateFormat(String format)
    {
        return isStrictDateFormat(format) ? getToken(
            format,
            1,
            2) : getToken(
            format,
            0,
            1);
    }

    /**
     * @return <code>true</code> if this field's value needs to conform to a strict date format, <code>false</code> otherwise
     */
    public static boolean isStrictDateFormat(String format)
    {
        return "strict".equalsIgnoreCase(getToken(
                format,
                0,
                2));
    }
    
    /**
     * Indicates if the given <code>format</code> is an email format.
     * 
     * @return <code>true</code> if this field is to be formatted as an email address, <code>false</code> otherwise
     */
    public static boolean isEmailFormat(String format)
    {
        return "email".equalsIgnoreCase(JSFUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * Indicates if the given <code>format</code> is a creditcard format.
     * 
     * @return <code>true</code> if this field is to be formatted as a credit card, <code>false</code> otherwise
     */
    public static boolean isCreditCardFormat(final String format)
    {
        return "creditcard".equalsIgnoreCase(JSFUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * Indicates if the given <code>format</code> is a pattern format.
     * 
     * @return <code>true</code> if this field's value needs to respect a certain pattern, <code>false</code> otherwise
     */
    public static boolean isPatternFormat(final String format)
    {
        return "pattern".equalsIgnoreCase(JSFUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * Indicates if the given <code>format</code> is a minlength format.
     * 
     * @return <code>true</code> if this field's value needs to consist of at least a certain 
     *         number of characters, <code>false</code> otherwise
     */
    public static boolean isMinLengthFormat(final String format)
    {
        return "minlength".equalsIgnoreCase(JSFUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * Indicates if the given <code>format</code> is a maxlength format.
     * 
     * @return <code>true</code> if this field's value needs to consist of at maximum a certain 
     *         number of characters, <code>false</code> otherwise
     */
    public static boolean isMaxLengthFormat(String format)
    {
        return "maxlength".equalsIgnoreCase(JSFUtils.getToken(
                format,
                0,
                2));
    }

    /**
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
        final Object value = element.findTaggedValue(JSFProfile.TAGGEDVALUE_INPUT_FORMAT);
        final String format = value == null ? null : String.valueOf(value);
        return format == null ? null : format.trim();
    }

    /**
     * Indicates if the given <code>format</code> is a range format.
     *
     * @return <code>true</code> if this field's value needs to be in a specific range, <code>false</code> otherwise
     */
    public static boolean isRangeFormat(final String format)
    {
        return "range".equalsIgnoreCase(JSFUtils.getToken(
                format,
                0,
                2));
    }
    
    /**
     * @return <code>true</code> if the type of this field is a byte, <code>false</code> otherwise
     */
    public static boolean isByte(final ClassifierFacade type)
    {
        return UMLMetafacadeUtils.isType(
            type,
            JSFProfile.BYTE_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a short, <code>false</code> otherwise
     */
    public static boolean isShort(final ClassifierFacade type)
    {
        return UMLMetafacadeUtils.isType(
            type,
            JSFProfile.SHORT_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is an integer, <code>false</code> otherwise
     */
    public static boolean isInteger(final ClassifierFacade type)
    {
        return UMLMetafacadeUtils.isType(
            type,
            JSFProfile.INTEGER_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a long integer, <code>false</code> otherwise
     */
    public static boolean isLong(final ClassifierFacade type)
    {
        return UMLMetafacadeUtils.isType(
            type,
            JSFProfile.LONG_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a floating point, <code>false</code> otherwise
     */
    public static boolean isFloat(final ClassifierFacade type)
    {
        return UMLMetafacadeUtils.isType(
            type,
            JSFProfile.FLOAT_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a double precision floating point, <code>false</code> otherwise
     */
    public static boolean isDouble(final ClassifierFacade type)
    {
        return UMLMetafacadeUtils.isType(
            type,
            JSFProfile.DOUBLE_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a date, <code>false</code> otherwise
     */
    public static boolean isDate(final ClassifierFacade type)
    {
        return type != null && type.isDateType();
    }

    /**
     * @return <code>true</code> if the type of this field is a time, <code>false</code> otherwise
     */
    public static boolean isTime(final ClassifierFacade type)
    {
        return UMLMetafacadeUtils.isType(
            type,
            JSFProfile.TIME_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a URL, <code>false</code> otherwise
     */
    public static boolean isUrl(final ClassifierFacade type)
    {
        return UMLMetafacadeUtils.isType(
            type,
            JSFProfile.URL_TYPE_NAME);
    }

    /**
     * @return <code>true</code> if the type of this field is a String, <code>false</code> otherwise
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
            final Object value = element.findTaggedValue(JSFProfile.TAGGEDVALUE_INPUT_READONLY);
            readOnly = Boolean.valueOf(ObjectUtils.toString(value)).booleanValue();
        }
        return readOnly;
    }
    
    /**
     * Retrieves the "validwhen" value from the given element (if one is present).
     * 
     * @param element the element from which to retrieve the validwhen value.
     * @return the "validwhen" value.
     */
    public static java.lang.String getValidWhen(final ModelElementFacade element)
    {
        String validWhen = null;
        if (element != null)
        {
            final Object value = element.findTaggedValue(JSFProfile.TAGGEDVALUE_INPUT_VALIDWHEN);
            validWhen = value == null ? null : '(' + value.toString() + ')';           
        }
        return validWhen;
    }
    
    /**
     * @return the lower limit for this field's value's range
     */
    public static String getRangeStart(final String format)
    {
        return JSFUtils.getToken(
            format,
            1,
            3);
    }

    /**
     * @return the upper limit for this field's value's range
     */
    public static String getRangeEnd(final String format)
    {
        return JSFUtils.getToken(
            format,
            2,
            3);
    }

    /**
     * @return the minimum number of characters this field's value must consist of
     */
    public static String getMinLengthValue(final String format)
    {
        return JSFUtils.getToken(
            format,
            1,
            2);
    }

    /**
     * @return the maximum number of characters this field's value must consist of
     */
    public static String getMaxLengthValue(final String format)
    {
        return JSFUtils.getToken(
            format,
            1,
            2);
    }

    /**
     * @return the pattern this field's value must respect
     */
    public static String getPatternValue(final String format)
    {
        return '^' + JSFUtils.getToken(
            format,
            1,
            2) + '$';
    }
}