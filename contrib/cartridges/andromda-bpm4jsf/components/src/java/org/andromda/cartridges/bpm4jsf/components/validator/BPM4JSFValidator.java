package org.andromda.cartridges.bpm4jsf.components.validator;

import java.io.InputStream;
import java.io.Serializable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.andromda.cartridges.bpm4jsf.components.taglib.BPM4JSFValidatorTag;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorResources;


/**
 * A JSF validator that uses the apache commons-validator to perform either
 * client or server side validation.
 */
public class BPM4JSFValidator
    implements javax.faces.validator.Validator,
        Serializable
{
    /**
     * Validator type.
     */
    private String type;

    /**
     * The setter method for the <code>type</code> property. This property is
     * passed through to the commons-validator.
     *
     * @param type The new value for the <code>type</code> property.
     */
    public void setType(final String type)
    {
        this.type = type;
    }

    /**
     * The getter method for the <code>type</code> property. This property is
     * passed through to the commons-validator.
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * Enable client-side validation?
     */
    private Boolean client;

    /**
     * The setter method for the <code>client</code> property. This property
     * is passed through to the commons-validator.
     *
     * @param client The new value for the <code>client</code> property.
     */
    public void setClient(final Boolean client)
    {
        this.client = client;
    }

    /**
     * The getter method for the <code>client</code> property. This property
     * is passed through to the commons-validator.
     */
    public Boolean getClient()
    {
        return this.client;
    }

    /**
     * Enable server-side validation?
     */
    private Boolean server;

    /**
     * The setter method for the <code>server</code> property. This property
     * is passed through to the commons-validator.
     *
     * @param server The new value for the <code>server</code> property.
     */
    public void setServer(final Boolean server)
    {
        this.server = server;
    }

    /**
     * The getter method for the <code>server</code> property. This property
     * is passed through to the commons-validator.
     */
    public Boolean getServer()
    {
        return this.server;
    }

    /**
     * The <code>validate</code> method uses this as the text of an error
     * message it stores on the FacesContext when validation fails.
     */
    private String message;

    /**
     * The setter method for the <code>message</code> property. This property
     * is passed through to the commons-validator.
     *
     * @param message The new value for the <code>message</code> property.
     */
    public void setMessage(final String message)
    {
        this.message = message;
    }

    /**
     * Parameter for the error message. This parameter is passed through to the
     * commons-validator.
     */
    private String arg;

    /**
     * The setter method for the <code>arg</code> property. This property is
     * passed through to the commons-validator..
     *
     * @param arg The new value for the <code>arg</code> property.
     */
    public void setArg(final String arg)
    {
        this.arg = arg;
    }

    /**
     * The getter method for the <code>arg</code> property. This property is
     * passed through to the commons-validator.
     */
    public String getArg()
    {
        return this.arg;
    }

    /**
     * <p>
     * Minimum value.
     * </p>
     */
    private Double min;

    /**
     * The setter method for the <code>min</code> property. This property is
     * passed through to the commons-validator.
     *
     * @param min The new value for the <code>min</code> property.
     */
    public void setMin(final Double min)
    {
        this.min = min;
    }

    /**
     * The maximum value.
     */
    private Double max;

    /**
     * The setter method for the <code>max</code> property. This property is
     * passed through to the commons-validator.
     *
     * @param max The new value for the <code>max</code> property.
     */
    public void setMax(final Double max)
    {
        this.max = max;
    }

    /**
     * Minimum length, in characters. Use with type="minlength".
     */
    private Integer minlength;

    /**
     * The setter method for the <code>minlength</code> property. This
     * property is passed through to the commons-validator.
     *
     * @param minlength The new value for the <code>minlength</code> property.
     */
    public void setMinlength(final Integer minlength)
    {
        this.minlength = minlength;
    }

    /**
     * Maximum length, in characters. Use with type="minlength".
     */
    private Integer maxlength;

    /**
     * The setter method for the <code>maxlength</code> property. This
     * property is passed through to the commons-validator.
     *
     * @param maxlength The new value for the <code>maxlength</code> property.
     */
    public void setMaxlength(final Integer maxlength)
    {
        this.maxlength = maxlength;
    }

    /**
     * A regular expression that's applied to the value.
     */
    private String mask;

    /**
     * The setter method for the <code>mask</code> property. This property is
     * passed through to the commons-validator.
     *
     * @param mask The new value for the <code>mask</code> property.
     */
    public void setMask(final String mask)
    {
        this.mask = mask;
    }

    /**
     * Is date validation strict?
     */
    private String datePatternStrict;

    /**
     * The setter method for the <code>datePatternStrict</code> property. This
     * property is passed through to the commons-validator.
     *
     * @param datePatternStrict The new value for the <code>datePatternStrict</code>
     *        property.
     */
    public void setDatePatternStrict(final String datePatternStrict)
    {
        this.datePatternStrict = datePatternStrict;
    }

    /**
     * The <code>Object[]</code> returned from this method represents
     * parameters that were explicitly set for this validator. Typically, that
     * happens in the <code>bpm4jsf:validator</code> tag. See
     * {@link BPM4JSFValidatorTag} for more information about those
     * parameters.
     */
    public Object[] getParams()
    {
        final ArrayList parameters = new ArrayList();
        if (this.min != null)
        {
            parameters.add(this.min);
        }
        if (this.max != null)
        {
            parameters.add(this.max);
        }
        if (this.minlength != null)
        {
            parameters.add(this.minlength);
        }
        if (this.maxlength != null)
        {
            parameters.add(this.maxlength);
        }
        if (this.mask != null)
        {
            parameters.add(this.mask);
        }
        if (this.datePatternStrict != null)
        {
            parameters.add(this.datePatternStrict);
        }
        return parameters.toArray();
    }

    /**
     * The <code>String[]</code> returned from this method represents the
     * names of parameters that were explicitly set for this validator.
     * Typically, that happens in the <code>bpm4jsf:validator</code> tag. See
     * the <code>CommonsValidatorTag</code> class for more information about
     * those parameters.
     */
    public String[] getParamNames()
    {
        final ArrayList paramterNames = new ArrayList();
        if (this.min != null)
        {
            paramterNames.add("min");
        }
        if (this.max != null)
        {
            paramterNames.add("max");
        }
        if (this.minlength != null)
        {
            paramterNames.add("minlength");
        }
        if (this.maxlength != null)
        {
            paramterNames.add("maxlength");
        }
        if (this.mask != null)
        {
            paramterNames.add("mask");
        }
        if (this.datePatternStrict != null)
        {
            paramterNames.add("datePatternStrict");
        }
        return (String[])paramterNames.toArray(new String[paramterNames.size()]);
    }

    /**
     * Returns the commons-validator action that's appropriate for the validator
     * with the given <code>name</code>. This method lazily configures
     * validator resources by reading <code>/WEB-INF/validator-rules.xml</code>.
     *
     * @param name The name of the validator
     */
    public static ValidatorAction getValidatorAction(final String name)
    {
        final String VALIDATOR_RESOURCES_KEY = "org.andromda.bpm4jsf.validator.resources";
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext external = context.getExternalContext();
        Map applicationMap = external.getApplicationMap();
        ValidatorResources validatorResources = (ValidatorResources)applicationMap.get(VALIDATOR_RESOURCES_KEY);
        if (validatorResources == null)
        {
            final String rulesResource = "/WEB-INF/validator-rules.xml";
            final InputStream input = external.getResourceAsStream(rulesResource);
            try
            {
                if (input == null)
                {
                    throw new RuntimeException("Could not find rules file '" + rulesResource + "'");
                }
                validatorResources = new ValidatorResources(input);
                applicationMap.put(
                    VALIDATOR_RESOURCES_KEY,
                    validatorResources);
            }
            catch (final Throwable throwable)
            {
                throw new RuntimeException(throwable);
            }
        }
        System.out.println("the action name!!!!!:  " + validatorResources.getValidatorAction(name));
        return validatorResources.getValidatorAction(name);
    }

    /**
     * The commons-validator action, that carries out the actual validation.
     */
    private transient ValidatorAction validatorAction;

    /**
     * Returns the commons-validator action that's appropriate for this
     * validator.
     */
    public ValidatorAction getValidatorAction()
    {
        if (this.validatorAction == null)
        {
            this.validatorAction = getValidatorAction(this.type);
        }
        return this.validatorAction;
    }

    /**
     * The commons-validator method.
     */
    private transient Method validatorMethod;

    /**
     * An array of validator parameter types.
     */
    private transient Class[] parameterTypes;

    /**
     * The commons-validator instance.
     */
    private transient Object validator;

    /**
     * This <code>validate</code> method is called by JSF to verify the
     * component to which the validator is attached.
     *
     * @param context The faces context
     * @param component The component to validate
     */
    public void validate(
        final FacesContext context,
        final UIComponent component,
        final Object value)
    {
        if (!Boolean.FALSE.equals(this.server))
        {
            this.initValidation();
            Object[] parameters = this.getParams();
            Object[] params = new Object[parameters.length + 1];

            params[0] = BPM4JSFValidator.convert(
                    value,
                    parameterTypes[0],
                    component);
            for (int ctr = 1; ctr < params.length; ctr++)
            {
                params[ctr] = convert(
                        parameters[ctr - 1],
                        parameterTypes[ctr],
                        null);
            }

            try
            {
                final Boolean validatorResult = (Boolean)validatorMethod.invoke(
                        validator,
                        params);
                if (validatorResult.equals(Boolean.FALSE))
                {
                    Object errorValue = value;
                    if (component instanceof EditableValueHolder)
                    {
                        errorValue = ((EditableValueHolder)component).getSubmittedValue();
                    }
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            getErrorMessage(
                                errorValue,
                                context),
                            null));
                }
            }
            catch (final Throwable throwable)
            {
                if (throwable instanceof ValidatorException)
                {
                    throw (ValidatorException)throwable;
                }
                throw new RuntimeException(throwable);
            }
        }
    }

    /**
     * <p>
     * Initialize validation by invoking <code>getValidatorAction</code> and
     * setting up instance data.
     */
    public void initValidation()
    {
        if (this.validatorMethod == null)
        {
            this.getValidatorAction();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null)
            {
                classLoader = getClass().getClassLoader();
            }
            try
            {
                final String[] parameterTypeNames = this.validatorAction.getMethodParams().split(",");
                this.parameterTypes = new Class[parameterTypeNames.length];
                for (int ctr = 0; ctr < parameterTypeNames.length; ctr++)
                {
                    final String typeName = parameterTypeNames[ctr];
                    Class paramType = (Class)standardTypes.get(typeName);
                    if (paramType == null)
                    {
                        paramType = classLoader.loadClass(typeName);
                    }
                    parameterTypes[ctr] = paramType;
                }
                Class type = classLoader.loadClass(validatorAction.getClassname());

                this.validatorMethod = type.getMethod(
                        validatorAction.getMethod(),
                        parameterTypes);
                if (!Modifier.isStatic(validatorMethod.getModifiers()))
                {
                    validator = type.newInstance();
                }
            }
            catch (final Throwable throwable)
            {
                throw new RuntimeException(throwable);
            }
        }
    }

    /**
     * <p>
     * Retrieves an error message, using the validator's message combined with
     * the errant value.
     */
    public String getErrorMessage(
        final Object value,
        final FacesContext context)
    {
        final String DEFAULT_BUNDLE_NAME = "message-resources";
        final Locale locale = context.getViewRoot().getLocale();
        String message = this.message;
        if (message == null)
        {
            final String messageKey = this.validatorAction.getMsg();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null)
            {
                loader = getClass().getClassLoader();
            }
            final Application application = context.getApplication();
            final String applicationBundleName = application.getMessageBundle();
            if (applicationBundleName != null)
            {
                final ResourceBundle bundle = ResourceBundle.getBundle(
                        applicationBundleName,
                        locale,
                        loader);
                if (bundle != null)
                {
                    try
                    {
                        message = bundle.getString(messageKey);
                    }
                    catch (MissingResourceException ex)
                    {
                    }
                }
            }
            if (message == null)
            {
                ResourceBundle bundle = ResourceBundle.getBundle(
                        DEFAULT_BUNDLE_NAME,
                        locale,
                        loader);
                if (bundle != null)
                {
                    try
                    {
                        message = bundle.getString(messageKey);
                    }
                    catch (final MissingResourceException exception)
                    {
                        // - ignore
                    }
                }
            }
        }
        Object[] parameters = this.getParams();
        Object[] params = new Object[parameters.length + 1];
        params[0] = value;
        for (int ctr = 1; ctr < params.length; ctr++)
        {
            params[ctr] = parameters[ctr - 1];
        }
        message = new MessageFormat(
                message,
                locale).format(params);
        return message;
    }

    /**
     * <p>
     * A utility method that converts an object to an instance of a given class,
     * such as converting <code>"true"</code> for example, into
     * <code>Boolean.TRUE</code>.
     * </p>
     * If the component passed to this method is an instance of
     * <code>EditableValueHolder</code> and the object's class is
     * <code>String</code>, this method returns the component's submitted
     * value, without converting it to a string. The <code>component</code>
     * parameter can be <code>null</code>.
     *
     * @param object The object to convert
     * @param type The type of object to convert to
     * @param component The component whose value we are converting
     */
    private static Object convert(
        final Object object,
        final Class type,
        final UIComponent component)
    {
        if (type.isInstance(object))
        {
            return object;
        }
        if (type == String.class && component != null && component instanceof EditableValueHolder)
        {
            return ((EditableValueHolder)component).getSubmittedValue();
        }
        if (type == String.class)
        {
            return "" + object;
        }
        if (object instanceof String)
        {
            String str = (String)object;
            if (type == boolean.class)
            {
                return Boolean.valueOf(str);
            }
            if (type == byte.class)
            {
                return new Byte(str);
            }
            if (type == char.class)
            {
                return new Character(str.charAt(0));
            }
            if (type == double.class)
            {
                return new Double(str);
            }
            if (type == float.class)
            {
                return new Float(str);
            }
            if (type == int.class)
            {
                return new Integer(str);
            }
            if (type == long.class)
            {
                return new Long(str);
            }
            if (type == short.class)
            {
                return new Short(str);
            }
        }
        else if (object instanceof Number)
        {
            Number num = (Number)object;
            if (type == byte.class)
            {
                return new Byte(num.byteValue());
            }
            if (type == double.class)
            {
                return new Double(num.doubleValue());
            }
            if (type == float.class)
            {
                return new Float(num.floatValue());
            }
            if (type == int.class)
            {
                return new Integer(num.intValue());
            }
            if (type == long.class)
            {
                return new Long(num.longValue());
            }
            if (type == short.class)
            {
                return new Short(num.shortValue());
            }
        }
        return object;
    }

    // these two methods are referenced in validator-utils.xml

    /**
     * A utility method that returns <code>true</code> if the supplied string
     * has a length greater than zero.
     *
     * @param string The string
     */
    public static boolean isSupplied(final String string)
    {
        return string.trim().length() > 0;
    }

    /**
     * A utility method that returns <code>true</code> if the supplied string
     * represents a date.
     *
     * @param date The string representation of the date.
     * @param datePatternStrict commons-validator property
     */
    public static boolean isDate(
        final String date,
        final String datePatternStrict)
    {
        return GenericValidator.isDate(
            date,
            datePatternStrict,
            true);
    }

    /**
     * Map of standard types: boolean, byte, char, etc. and their corresponding
     * classes.
     */
    private static Map standardTypes;

    /**
     * Standard types for conversions
     */
    static
    {
        standardTypes = new HashMap();
        standardTypes.put(
            "boolean",
            boolean.class);
        standardTypes.put(
            "byte",
            byte.class);
        standardTypes.put(
            "char",
            char.class);
        standardTypes.put(
            "double",
            double.class);
        standardTypes.put(
            "float",
            float.class);
        standardTypes.put(
            "int",
            int.class);
        standardTypes.put(
            "long",
            long.class);
        standardTypes.put(
            "short",
            short.class);
        standardTypes.put(
            "java.lang.String",
            String.class);
    }
}