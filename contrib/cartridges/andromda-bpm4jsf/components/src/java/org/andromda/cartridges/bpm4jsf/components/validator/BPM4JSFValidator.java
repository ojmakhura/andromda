package org.andromda.cartridges.bpm4jsf.components.validator;

import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.andromda.cartridges.bpm4jsf.components.Messages;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Validator;
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
    public void addParameter(final String name, final Object value)
    {
        this.parameters.put(name, value);
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
        return BPM4JSFValidator.getValidatorResources().getValidatorAction(name);
    }

    /**
     * The commons-validator action, that carries out the actual validation.
     */
    private transient ValidatorAction validatorAction;
    
    /**
     * Returns the commons-validator resources. This method lazily configures
     * validator resources by reading <code>/WEB-INF/validator-rules.xml</code>
     * and <code>/WEB-INF/validation.xml</code>.
     *
     * @param name The name of the validator
     */
    public static ValidatorResources getValidatorResources()
    {
        final String VALIDATOR_RESOURCES_KEY = "org.andromda.bpm4jsf.validator.resources";
        final FacesContext context = FacesContext.getCurrentInstance();
        final ExternalContext external = context.getExternalContext();
        final Map applicationMap = external.getApplicationMap();
        ValidatorResources validatorResources = (ValidatorResources)applicationMap.get(VALIDATOR_RESOURCES_KEY);
        if (validatorResources == null)
        {
            final String rulesResource = "/WEB-INF/validator-rules.xml";
            final String validationResource = "/WEB-INF/validation.xml";
            final InputStream rulesInput = external.getResourceAsStream(rulesResource);
            if (rulesInput == null)
            {
                throw new RuntimeException("Could not find rules file '" + rulesResource + "'");
            }
            final InputStream validationInput = external.getResourceAsStream(validationResource);
            if (validationInput == null)
            {
                throw new RuntimeException("Could not find validation file '" + validationResource + "'");
            }
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
                throw new RuntimeException(throwable);
            }
        }
        return validatorResources;
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
        System.out.println("validating component>>>>>>>>>>>>>>>: " + component.getId() + "this: " +this);
        UIForm form = findForm(component);
        if (form != null)
        {
            System.out.println("Validating form: " + form);
            Validator validator = new Validator(getValidatorResources(), form.getId());
            validator.setUseContextClassLoader(true);
            validator.setParameter("javax.faces.context.FacesContext", context);
            validator.setParameter("java.lang.Object", value);
            validator.setParameter("java.util.Map", this.parameters);
            final Collection errors = new ArrayList();
            validator.setParameter("java.util.Collection", errors);
                try
                {
                    validator.validate();
                    if (!errors.isEmpty())
                    {
                        throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            ((String)errors.iterator().next()),
                            null));
                    }
                }
                catch (final org.apache.commons.validator.ValidatorException exception)
                {
                    exception.printStackTrace();
                    logger.error(exception.getMessage(), exception);
                }
        }
    }

    /**
     * Retrieves an error message, using the validator's message combined with
     * the errant value.
     */
    public String getErrorMessage(
        Object[] args,
        final FacesContext context)
    {
        if (args == null)
        {
            args = this.args;
        }
        final Locale locale = context.getViewRoot().getLocale();
        String message = null;
        final String messageKey = this.validatorAction.getMsg();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null)
        {
            loader = this.getClass().getClassLoader();
        }
        if (message == null)
        {
            try
            {
                message = Messages.get(
                        messageKey,
                        args);
            }
            catch (final MissingResourceException exception)
            {
                message = messageKey;
            }
        }
        message = new MessageFormat(
                message,
                locale).format(args);
        System.out.println("the message!!!!!!!!!!!!!!!!!!!!!: " + message);
        return message;
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