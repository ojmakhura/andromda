package org.andromda.cartridges.bpm4jsf.components.validator;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;


/**
 * <p>
 * This class contains the default validations that are used in the
 * validator-rules.xml file.
 * </p>
 * <p>
 * In general passing in a null or blank will return a null Object or a false
 * boolean. However, nulls and blanks do not result in an error being added to
 * the errors.
 * </p>
 */
public class ParameterChecks
    implements Serializable
{
    /**
     * Commons Logging instance.
     */
    private static final Log logger = LogFactory.getLog(ParameterChecks.class);
    public static final String FIELD_TEST_NULL = "NULL";
    public static final String FIELD_TEST_NOTNULL = "NOTNULL";
    public static final String FIELD_TEST_EQUAL = "EQUAL";

    /**
     * Checks if the field isn't null and length of the field is greater than
     * zero not including whitespace.
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters validation is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @param request Current request object.
     * @return true if meets stated requirements, false otherwise.
     */
    public static boolean validateRequired(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        boolean valid = false;
        String value = null;
        if (object instanceof String)
        {
            value = (String)object;
        }
        if (object == null || StringUtils.isBlank(value))
        {
            System.out.println("The errors!!!!!!: " + errors);
            errors.add(ValidatorMessages.getMessage(
                    action,
                    field));
        }
        else
        {
            valid = true;
        }
        return valid;
    }

    /**
     * Checks if the parameter isn't null based on the values of other fields.
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters validation is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     *        other field values.
     * @return true if meets stated requirements, false otherwise.
     */
    public static void validateRequiredIf(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        boolean required = false;

        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }

        int i = 0;
        String fieldJoin = "AND";
        if (!StringUtils.isBlank(field.getVarValue("fieldJoin")))
        {
            fieldJoin = field.getVarValue("fieldJoin");
        }

        if (fieldJoin.equalsIgnoreCase("AND"))
        {
            required = true;
        }

        while (!StringUtils.isBlank(field.getVarValue("field[" + i + "]")))
        {
            String dependProp = field.getVarValue("field[" + i + "]");
            String dependTest = field.getVarValue("fieldTest[" + i + "]");
            String dependTestValue = field.getVarValue("fieldValue[" + i + "]");
            String dependIndexed = field.getVarValue("fieldIndexed[" + i + "]");

            if (dependIndexed == null)
            {
                dependIndexed = "false";
            }

            String dependVal = null;
            boolean thisRequired = false;
            if (field.isIndexed() && dependIndexed.equalsIgnoreCase("true"))
            {
                String key = field.getKey();
                if ((key.indexOf("[") > -1) && (key.indexOf("]") > -1))
                {
                    String ind = key.substring(
                            0,
                            key.indexOf(".") + 1);
                    dependProp = ind + dependProp;
                }
            }

            dependVal = (String)parameters.get(dependProp);
            if (dependTest.equals(FIELD_TEST_NULL))
            {
                if ((dependVal != null) && (dependVal.length() > 0))
                {
                    thisRequired = false;
                }
                else
                {
                    thisRequired = true;
                }
            }

            if (dependTest.equals(FIELD_TEST_NOTNULL))
            {
                if ((dependVal != null) && (dependVal.length() > 0))
                {
                    thisRequired = true;
                }
                else
                {
                    thisRequired = false;
                }
            }

            if (dependTest.equals(FIELD_TEST_EQUAL))
            {
                thisRequired = dependTestValue.equalsIgnoreCase(dependVal);
            }

            if (fieldJoin.equalsIgnoreCase("AND"))
            {
                required = required && thisRequired;
            }
            else
            {
                required = required || thisRequired;
            }

            i++;
        }

        if (required)
        {
            if (StringUtils.isBlank(value))
            {
                errors.add(ValidatorMessages.getMessage(
                        action,
                        field));
            }
        }
    }

    /**
     * Checks if the parameter matches the regular expression in the field's
     * mask attribute.
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters validation is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur. lse otherwise.
     */
    public static void validateMask(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        String mask = field.getVarValue("mask");
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }

        try
        {
            if (!StringUtils.isBlank(value) && !GenericValidator.matchRegexp(
                    value,
                    mask))
            {
                errors.add(ValidatorMessages.getMessage(
                        action,
                        field));
            }
        }
        catch (Exception e)
        {
            logger.error(
                e.getMessage(),
                e);
        }
    }

    /**
     * Checks if the field can safely be converted to a byte primitive.
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters validation is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @return true if valid, false otherwise.
     */
    public static void validateByte(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        Byte result = null;
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }
        if (StringUtils.isNotBlank(value))
        {
            result = GenericTypeValidator.formatByte(value);

            if (result == null)
            {
                errors.add(ValidatorMessages.getMessage(
                        action,
                        field));
            }
        }
    }

    /**
     * Checks if the field can safely be converted to a short primitive.
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters validation is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @return true if valid, false otherwise.
     */
    public static void validateShort(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        Short result = null;
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }
        if (StringUtils.isNotBlank(value))
        {
            result = GenericTypeValidator.formatShort(value);

            if (result == null)
            {
                errors.add(ValidatorMessages.getMessage(
                        action,
                        field));
            }
        }
    }

    /**
     * Checks if the field can safely be converted to an int primitive.
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @return true if valid, false otherwise.
     */
    public static void validateInteger(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        Integer result = null;
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }

        if (StringUtils.isNotBlank(value))
        {
            result = GenericTypeValidator.formatInt(value);
            if (result == null)
            {
                errors.add(ValidatorMessages.getMessage(
                        action,
                        field));
            }
        }
    }

    /**
     * Checks if the field can safely be converted to a long primitive.
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @return true if valid, false otherwise.
     */
    public static void validateLong(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        Long result = null;
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }

        if (StringUtils.isNotBlank(value))
        {
            result = GenericTypeValidator.formatLong(value);

            if (result == null)
            {
                errors.add(ValidatorMessages.getMessage(
                        action,
                        field));
            }
        }
    }

    /**
     * Checks if the field can safely be converted to a float primitive.
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @return true if valid, false otherwise.
     */
    public static void validateFloat(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        Float result = null;
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }
        if (StringUtils.isNotBlank(value))
        {
            result = GenericTypeValidator.formatFloat(value);

            if (result == null)
            {
                errors.add(ValidatorMessages.getMessage(
                        action,
                        field));
            }
        }
    }

    /**
     * Checks if the field can safely be converted to a double primitive.
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @return true if valid, false otherwise.
     */
    public static void validateDouble(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        Double result = null;
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }
        if (StringUtils.isNotBlank(value))
        {
            result = GenericTypeValidator.formatDouble(value);

            if (result == null)
            {
                errors.add(ValidatorMessages.getMessage(
                        action,
                        field));
            }
        }
    }

    /**
     * Checks if the field is a valid date. If the field has a datePattern
     * variable, that will be used to format
     * <code>java.text.SimpleDateFormat</code>. If the field has a
     * datePatternStrict variable, that will be used to format
     * <code>java.text.SimpleDateFormat</code> and the length will be checked
     * so '2/12/1999' will not pass validation with the format 'MM/dd/yyyy'
     * because the month isn't two digits. If no datePattern variable is
     * specified, then the field gets the DateFormat.SHORT format for the
     * locale. The setLenient method is set to <code>false</code> for all
     * variations.
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @return true if valid, false otherwise.
     */
    public static void validateDate(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        Date result = null;
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }

        String datePattern = field.getVarValue("datePattern");
        String datePatternStrict = field.getVarValue("datePatternStrict");
        Locale locale = Locale.getDefault();

        if (StringUtils.isNotBlank(value))
        {
            try
            {
                if (datePattern != null && datePattern.length() > 0)
                {
                    result = GenericTypeValidator.formatDate(
                            value,
                            datePattern,
                            false);
                }
                else if (datePatternStrict != null && datePatternStrict.length() > 0)
                {
                    result = GenericTypeValidator.formatDate(
                            value,
                            datePatternStrict,
                            true);
                }
                else
                {
                    result = GenericTypeValidator.formatDate(
                            value,
                            locale);
                }
            }
            catch (Exception exception)
            {
                logger.error(
                    exception.getMessage(),
                    exception);
            }

            if (result == null)
            {
                errors.add(ValidatorMessages.getMessage(
                        action,
                        field));
            }
        }
    }

    /**
     * Checks if a fields value is within a range (min &amp; max specified in
     * the vars attribute).
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @return True if in range, false otherwise.
     */
    public static void validateLongRange(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }

        if (StringUtils.isNotBlank(value))
        {
            try
            {
                long intValue = Long.parseLong(value);
                long min = Long.parseLong(field.getVarValue("min"));
                long max = Long.parseLong(field.getVarValue("max"));

                if (!GenericValidator.isInRange(
                        intValue,
                        min,
                        max))
                {
                    errors.add(ValidatorMessages.getMessage(
                            action,
                            field));
                }
            }
            catch (Exception e)
            {
                errors.add(ValidatorMessages.getMessage(
                        action,
                        field));
            }
        }
    }

    /**
     * Checks if a fields value is within a range (min &amp; max specified in
     * the vars attribute).
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @return True if in range, false otherwise.
     */
    public static void validateDoubleRange(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }

        if (StringUtils.isNotBlank(value))
        {
            try
            {
                double doubleValue = Double.parseDouble(value);
                double min = Double.parseDouble(field.getVarValue("min"));
                double max = Double.parseDouble(field.getVarValue("max"));

                if (!GenericValidator.isInRange(
                        doubleValue,
                        min,
                        max))
                {
                    errors.add(ValidatorMessages.getMessage(
                            action,
                            field));
                }
            }
            catch (Exception e)
            {
                errors.add(ValidatorMessages.getMessage(
                        action,
                        field));
            }
        }
    }

    /**
     * Checks if a fields value is within a range (min &amp; max specified in
     * the vars attribute).
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @return True if in range, false otherwise.
     */
    public static void validateFloatRange(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }

        if (StringUtils.isNotBlank(value))
        {
            try
            {
                float floatValue = Float.parseFloat(value);
                float min = Float.parseFloat(field.getVarValue("min"));
                float max = Float.parseFloat(field.getVarValue("max"));

                if (!GenericValidator.isInRange(
                        floatValue,
                        min,
                        max))
                {
                    errors.add(ValidatorMessages.getMessage(
                            action,
                            field));
                }
            }
            catch (Exception e)
            {
                errors.add(ValidatorMessages.getMessage(
                        action,
                        field));
            }
        }
    }

    /**
     * Checks if the field is a valid credit card number.
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @return true if valid, false otherwise.
     */
    public static void validateCreditCard(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        Long result = null;
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }

        if (StringUtils.isNotBlank(value))
        {
            result = GenericTypeValidator.formatCreditCard(value);

            if (result == null)
            {
                errors.add(ValidatorMessages.getMessage(
                        action,
                        field));
            }
        }
    }

    /**
     * Checks if a field has a valid e-mail address.
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @return True if valid, false otherwise.
     */
    public static void validateEmail(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }
        if (StringUtils.isNotBlank(value) && !GenericValidator.isEmail(value))
        {
            errors.add(ValidatorMessages.getMessage(
                    action,
                    field));
        }
    }

    /**
     * Checks if the field's length is less than or equal to the maximum value.
     * A <code>Null</code> will be considered an error.
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @param request Current request object.
     * @return True if stated conditions met.
     */
    public static void validateMaxLength(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }

        if (value != null)
        {
            try
            {
                int max = Integer.parseInt(field.getVarValue("maxlength"));

                if (!GenericValidator.maxLength(
                        value,
                        max))
                {
                    errors.add(ValidatorMessages.getMessage(
                            action,
                            field));
                }
            }
            catch (Exception e)
            {
                errors.add(ValidatorMessages.getMessage(
                        action,
                        field));
            }
        }
    }

    /**
     * Checks if the field's length is greater than or equal to the minimum
     * value. A <code>Null</code> will be considered an error.
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @return True if stated conditions met.
     */
    public static void validateMinLength(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }

        if (!StringUtils.isBlank(value))
        {
            try
            {
                int min = Integer.parseInt(field.getVarValue("minlength"));

                if (!GenericValidator.minLength(
                        value,
                        min))
                {
                    errors.add(ValidatorMessages.getMessage(
                            action,
                            field));
                }
            }
            catch (Exception exception)
            {
                errors.add(ValidatorMessages.getMessage(
                        action,
                        field));
            }
        }
    }

    /**
     * Checks if a field has a valid url.
     *
     * @param report the ReportConfig were're validating against.
     * @param parameters The report parameters is being performed on.
     * @param action The <code>ValidatorAction</code> that is currently being
     *        performed.
     * @param field The <code>Field</code> object associated with the current
     *        field being validated.
     * @param errors The <code>Map</code> object to add errors to if any
     *        validation errors occur.
     * @return True if valid, false otherwise.
     */
    public static void validateUrl(
        FacesContext context,
        Object object,
        Map parameters,
        Collection errors,
        ValidatorAction action,
        Field field)
    {
        String value = null;
        if (object != null)
        {
            value = String.valueOf(parameters.get(field.getProperty()));
        }

        if (!StringUtils.isBlank(value) && !GenericValidator.isUrl(value))
        {
            errors.add(ValidatorMessages.getMessage(
                    action,
                    field));
        }
    }

    /**
     * Return <code>true</code> if the specified object is a String or a
     * <code>null</code> value.
     *
     * @param object Object to be tested
     * @return The string value
     */
    protected static boolean isString(Object object)
    {
        return object == null ? true : String.class.isInstance(object);
    }
}