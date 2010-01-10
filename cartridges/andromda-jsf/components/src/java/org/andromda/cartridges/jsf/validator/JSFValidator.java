package org.andromda.cartridges.jsf.validator;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorResources;

/**
 * A JSF validator that uses the apache commons-validator to perform either
 * client or server side validation.
 */
public class JSFValidator
    implements Validator,
        Serializable
{
    private static final Log logger = LogFactory.getLog(JSFValidator.class);

    /**
     * Constructs a new instance of this class with the given <code>form</code> and
     * <code>validatorAction</code>.
     *
     * @param formIdIn
     * @param validatorActionIn
     */
    public JSFValidator(final String formIdIn, final ValidatorAction validatorActionIn)
    {
        this.formId = formIdIn;
        this.validatorAction = validatorActionIn;
    }

    private String formId;

    /**
     * Default constructor for faces-config.xml
     */
    public JSFValidator()
    {
        // - default constructor for faces-config.xml
    }

    /**
     * Validator type.
     */
    private String type;

    /**
     * The setter method for the <code>type</code> property. This property is
     * passed through to the commons-validator.
     *
     * @param typeIn The new value for the <code>type</code> property.
     */
    public void setType(final String typeIn)
    {
        this.type = typeIn;
    }

    /**
     * The getter method for the <code>type</code> property. This property is
     * passed through to the commons-validator.
     * @return  this.type
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * Parameter for the error message. This parameter is passed through to the
     * commons-validator.
     */
    private String[] args;

    /**
     * The setter method for the <code>args</code> property. This property is
     * passed through to the commons-validator..
     *
     * @param argsIn The new value for the <code>args</code> property.
     */
    public void setArgs(final String[] argsIn)
    {
        this.args = argsIn;
    }

    /**
     * The parameters for this validator.
     */
    private final Map parameters = new HashMap();

    /**
     * Gets the parameters for this validator (keyed by name).
     *
     * @return a map containing all parameters.
     */
    public Map getParameters()
    {
        return this.parameters;
    }

    /**
     * Adds the parameter with the given <code>name</code> and the given
     * <code>value</code>.
     *
     * @param name the name of the parameter.
     * @param value the parameter's value
     */
    public void addParameter(
        final String name,
        final Object value)
    {
        this.parameters.put(
            name,
            value);
    }

    /**
     * Returns the commons-validator action that's appropriate for the validator
     * with the given <code>name</code>. This method lazily configures
     * validator resources by reading <code>/WEB-INF/validator-rules.xml</code>
     * and <code>/WEB-INF/validation.xml</code>.
     *
     * @param name The name of the validator
     * @return getValidatorResources().getValidatorAction(name)
     */
    public static ValidatorAction getValidatorAction(final String name)
    {
        return getValidatorResources().getValidatorAction(name);
    }

    /**
     * The commons-validator action, that carries out the actual validation.
     */
    private ValidatorAction validatorAction;

    /**
     * The location of the validator rules.
     */
    public static final String RULES_LOCATION = "/WEB-INF/validator-rules.xml";

    /**
     * The key that stores the validator resources
     */
    private static final String VALIDATOR_RESOURCES_KEY = "org.andromda.jsf.validator.resources";

    /**
     * Returns the commons-validator resources. This method lazily configures
     * validator resources by reading <code>/WEB-INF/validator-rules.xml</code>
     * and <code>/WEB-INF/validation.xml</code>.
     *
     * @return the commons-validator resources
     */
    public static ValidatorResources getValidatorResources()
    {
        final FacesContext context = FacesContext.getCurrentInstance();
        final ExternalContext external = context.getExternalContext();
        final Map applicationMap = external.getApplicationMap();
        ValidatorResources validatorResources = (ValidatorResources)applicationMap.get(VALIDATOR_RESOURCES_KEY);
        if (validatorResources == null)
        {
            final String rulesResource = RULES_LOCATION;
            final String validationResource = "/WEB-INF/validation.xml";
            final InputStream rulesInput = external.getResourceAsStream(rulesResource);
            if (rulesInput == null)
            {
                throw new JSFValidatorException("Could not find rules file '" + rulesResource + '\'');
            }
            final InputStream validationInput = external.getResourceAsStream(validationResource);
            if (validationInput != null)
            {
                final InputStream[] inputs = new InputStream[] {rulesInput, validationInput};
                try
                {
                    validatorResources = new ValidatorResources(inputs);
                    applicationMap.put(
                        VALIDATOR_RESOURCES_KEY,
                        validatorResources);
                }
                catch (final Throwable throwable)
                {
                    throw new JSFValidatorException(throwable);
                }
            }
            else
            {
                logger.info(
                    "No validation rules could be loaded from --> '" + validationResource +
                    ", validation not configured");
            }
        }
        return validatorResources;
    }

    /**
     * This <code>validate</code> method is called by JSF to verify the
     * component to which the validator is attached.
     *
     * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, Object)
     */
    public void validate(
        final FacesContext context,
        final UIComponent component,
        final Object value)
    {
        if (this.formId != null)
        {
            try
            {
                final Form validatorForm = getValidatorResources().getForm(
                    Locale.getDefault(),
                    this.formId);
                if (validatorForm != null)
                {
                    final Field field = this.getFormField(validatorForm, component.getId());
                    if (field != null)
                    {
                        final Collection errors = new ArrayList();
                        this.getValidatorMethod().invoke(
                            this.getValidatorClass(),
                                context, value, this.getParameters(), errors, this.validatorAction,
                                field);
                        if (!errors.isEmpty())
                        {
                            throw new ValidatorException(new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    (String)errors.iterator().next(),
                                    null));
                        }
                    }
                    else
                    {
                        logger.error("No field with id '" + component.getId() + "' found on form '" + this.formId + '\'');
                    }
                }
                else
                {
                    logger.error("No validator form could be found with id '" + this.formId + '\'');
                }
            }
            catch (final ValidatorException exception)
            {
                throw exception;
            }
            catch (final Exception exception)
            {
                logger.error(
                    exception.getMessage(),
                    exception);
            }
        }
    }

    /**
     * Gets the validator class from the underlying <code>validatorAction</code>.
     * @return the validator class
     * @throws ClassNotFoundException
     */
    private Class getValidatorClass()
        throws ClassNotFoundException
    {
        final FacesContext context = FacesContext.getCurrentInstance();
        final ExternalContext external = context.getExternalContext();
        final Map applicationMap = external.getApplicationMap();
        final String validatorClassName = this.validatorAction.getClassname();
        Class validatorClass = (Class)applicationMap.get(validatorClassName);
        if (validatorClass == null)
        {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null)
            {
                classLoader = getClass().getClassLoader();
            }
            validatorClass = classLoader.loadClass(validatorClassName);
            applicationMap.put(
                validatorClassName,
                validatorClass);
        }
        return validatorClass;
    }

    /**
     * Gets the validator method for the underlying <code>validatorAction</code>.
     *
     * @return the validator method.
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    private Method getValidatorMethod()
        throws ClassNotFoundException, NoSuchMethodException
    {
        Class[] parameterTypes =
            new Class[]
            {
                javax.faces.context.FacesContext.class, Object.class, java.util.Map.class,
                java.util.Collection.class, org.apache.commons.validator.ValidatorAction.class,
                org.apache.commons.validator.Field.class
            };
        return this.getValidatorClass().getMethod(
            this.validatorAction.getMethod(),
            parameterTypes);
    }

    /**
     * Attempts to retrieve the form field from the form with the given <code>formName</code>
     * and the field with the given <code>fieldName</code>.  If it can't be retrieved, null
     * is returned.
     * @param form the form to validate.
     * @param fieldName the name of the field.
     * @return the found field or null if it could not be found.
     */
    private Field getFormField(
        final Form form,
        final String fieldName)
    {
        Field field = null;
        if (form != null)
        {
            field = form.getField(fieldName);
        }
        return field;
    }

    /**
     * Retrieves an error message, using the validator's message combined with
     * the errant value.
     * @param context 
     * @return ValidatorMessages.getMessage
     */
    public String getErrorMessage(final FacesContext context)
    {
        return ValidatorMessages.getMessage(
            this.validatorAction,
            this.args,
            context);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return super.toString() + ":formId=" + this.formId + ", validatorAction="
            + (this.validatorAction != null ? this.validatorAction.getName() : null);
    }

    private static final long serialVersionUID = 1;
}