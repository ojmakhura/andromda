package org.andromda.cartridges.bpm4jsf.components.validator;

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
import javax.faces.component.UIForm;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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
public class BPM4JSFValidator
    implements javax.faces.validator.Validator,
        Serializable
{
    private static final Log logger = LogFactory.getLog(BPM4JSFValidator.class);

    /**
     * Constructs a new instance of this class with the given
     * <code>validatorAction</code>.
     *
     * @param validatorAction
     */
    public BPM4JSFValidator(final ValidatorAction validatorAction)
    {
        this.validatorAction = validatorAction;
    }

    public BPM4JSFValidator()
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
     * Parameter for the error message. This parameter is passed through to the
     * commons-validator.
     */
    private String[] args;

    /**
     * The setter method for the <code>arg</code> property. This property is
     * passed through to the commons-validator..
     *
     * @param arg The new value for the <code>arg</code> property.
     */
    public void setArgs(final String[] args)
    {
        this.args = args;
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
     */
    public static ValidatorAction getValidatorAction(final String name)
    {
        final ValidatorAction action = getValidatorResources().getValidatorAction(name);
        if (action == null)
        {
            throw new BPM4JSFValidatorException("No validator action with name '" + name + "' registered in rules files '" + RULES_LOCATION + "'");
        }
        return action;
    }

    /**
     * The commons-validator action, that carries out the actual validation.
     */
    private ValidatorAction validatorAction;
    
    /**
     * The location of the validator rules.
     */
    private static final String RULES_LOCATION = "/WEB-INF/validator-rules.xml";

    /**
     * Initializes the validator.
     */
    public static void initialize()
    {
        final FacesContext context = FacesContext.getCurrentInstance();
        final ExternalContext external = context.getExternalContext();
        final String rulesResource = RULES_LOCATION;
        final String validationResource = "/WEB-INF/validation.xml";
        final InputStream rulesInput = external.getResourceAsStream(rulesResource);
        if (rulesInput == null)
        {
            throw new BPM4JSFValidatorException("Could not find rules file '" + rulesResource + "'");
        }
        final InputStream validationInput = external.getResourceAsStream(validationResource);
        if (validationInput == null)
        {
            throw new BPM4JSFValidatorException("Could not find validation file '" + validationResource + "'");
        }
        final InputStream[] inputs = new InputStream[] {rulesInput, validationInput};
        try
        {
            ValidatorResources validatorResources = new ValidatorResources(inputs);
            final Map applicationMap = external.getApplicationMap();
            applicationMap.put(
                VALIDATOR_RESOURCES_KEY,
                validatorResources);
        }
        catch (final Throwable throwable)
        {
            throw new BPM4JSFValidatorException(throwable);
        }
    }

    private static final String VALIDATOR_RESOURCES_KEY = "org.andromda.bpm4jsf.validator.resources";

    /**
     * Returns the commons-validator resources. This method lazily configures
     * validator resources by reading <code>/WEB-INF/validator-rules.xml</code>
     * and <code>/WEB-INF/validation.xml</code>.
     *
     * @param name The name of the validator
     */
    public static ValidatorResources getValidatorResources()
    {
        final FacesContext context = FacesContext.getCurrentInstance();
        final ExternalContext external = context.getExternalContext();
        final Map applicationMap = external.getApplicationMap();
        return (ValidatorResources)applicationMap.get(VALIDATOR_RESOURCES_KEY);
    }

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
        UIForm form = findForm(component);
        if (form != null)
        {
            try
            {
                final Collection errors = new ArrayList();
                this.getValidatorMethod().invoke(
                    this.getValidatorClass(),
                    new Object[]
                    {
                        context, value, this.getParameters(), errors, this.validatorAction,
                        this.getFormField(
                            form.getId(),
                            component.getId())
                    });
                if (!errors.isEmpty())
                {
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            (String)errors.iterator().next(),
                            null));
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
                javax.faces.context.FacesContext.class, java.lang.Object.class, java.util.Map.class,
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
     * @param formName the name of the form.
     * @param fieldName the name of the field.
     * @return the found field or null if it could not be found.
     */
    private Field getFormField(
        final String formName,
        final String fieldName)
    {
        Field field = null;
        final Form form = getValidatorResources().getForm(
                Locale.getDefault(),
                formName);
        if (form != null)
        {
            field = form.getField(fieldName);
        }
        return field;
    }

    /**
     * Retrieves an error message, using the validator's message combined with
     * the errant value.
     */
    public String getErrorMessage(final FacesContext context)
    {
        return ValidatorMessages.getMessage(
            this.validatorAction,
            this.args,
            context);
    }

    /**
     * Recursively finds the valueHolder's form (if the valueHolder is nested within a form).
     *
     * @param component the valueHolder for which to find the form.
     * @return the form or null if there is no form for the valueHolder.
     */
    public static UIForm findForm(final UIComponent component)
    {
        UIForm form = null;
        if (component != null)
        {
            if (component instanceof UIForm)
            {
                form = (UIForm)component;
            }
            else
            {
                form = findForm(component.getParent());
            }
        }
        return form;
    }
}