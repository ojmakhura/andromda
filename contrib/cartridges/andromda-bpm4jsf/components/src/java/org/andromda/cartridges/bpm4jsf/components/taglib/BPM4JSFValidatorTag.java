package org.andromda.cartridges.bpm4jsf.components.taglib;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.webapp.UIComponentTag;

import javax.servlet.jsp.JspException;

import org.andromda.cartridges.bpm4jsf.components.validator.BPM4JSFValidator;


/**
 * The class for the <code>bpm4jsf:validator</code> tag.
 */
public class BPM4JSFValidatorTag
    extends javax.faces.webapp.ValidatorTag
{
    /**
     * This constructor obtains a reference to the tag utility object, which is
     * a JSF managed bean.
     */
    public BPM4JSFValidatorTag()
    {
        this.setValidatorId(BPM4JSFValidator.class.getName());
    }

    /**
     * The <code>type</code> attribute.
     */
    private String type;

    /**
     * Sets the <code>type</code> attribute.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * The <code>min</code> attribute.
     */
    private String min;

    /**
     * Sets the <code>min</code> attribute.
     */
    public void setMin(String min)
    {
        this.min = min;
    }

    /**
     * The <code>max</code> attribute.
     */
    private String max;

    /**
     * Sets the <code>max</code> attribute.
     */
    public void setMax(String max)
    {
        this.max = max;
    }

    /**
     * Sets the <code>minlength</code> attribute.
     */
    public void setMinlength(String minlength)
    {
        this.minlength = minlength;
    }

    /**
     * The <code>maxlength</code> attribute.
     */
    private String maxlength;

    /**
     * Sets the <code>maxlength</code> attribute.
     */
    public void setMaxlength(String maxlength)
    {
        this.maxlength = maxlength;
    }

    /**
     * The <code>datePatternStrict</code> attribute.
     */
    private String datePatternStrict;

    /**
     * Sets the <code>setDatePatternStrict</code> attribute.
     */
    public void setDatePatternStrict(String dateaPatternStrict)
    {
        this.datePatternStrict = dateaPatternStrict;
    }

    /**
     * The <code>mask</code> attribute.
     */
    private String mask;

    /**
     * Sets the <code>mask</code> attribute.
     */
    public void setMask(String mask)
    {
        this.mask = mask;
    }

    /**
     * The <code>message</code> attribute.
     */
    private String message;

    /**
     * Sets the <code>message</code> attribute.
     */
    public void setMessage(String message)
    {
        this.message = message;
    }

    /**
     * The <code>arg</code> attribute.
     */
    private String arg;

    /**
     * Sets the <code>arg</code> attribute.
     */
    public void setArg(String arg)
    {
        this.arg = arg;
    }

    /**
     * The <code>client</code> attribute.
     */
    private String client;

    /**
     * Sets the <code>client</code> attribute.
     */
    public void setClient(String client)
    {
        this.client = client;
    }

    /**
     * The <code>server</code> attribute.
     */
    private String server;

    /**
     * Sets the <code>server</code> attribute.
     */
    public void setServer(String service)
    {
        this.server = service;
    }

    /**
     * The <code>minlength</code> attribute.
     */
    private String minlength;

    /**
     * Create a validaotor by calling <code>super.createValidator()</code>.
     * This method initializes that validator with the tag's attribute values.
     */
    public Validator createValidator()
        throws JspException
    {
        final BPM4JSFValidator validator = (BPM4JSFValidator)super.createValidator();
        validator.setType(this.evaluate(this.type));
        validator.setMin(this.evaluateDouble(this.min));
        validator.setMax(this.evaluateDouble(this.max));
        validator.setMinlength(this.evaluateInteger(this.minlength));
        validator.setMaxlength(this.evaluateInteger(this.maxlength));
        validator.setDatePatternStrict(this.evaluate(this.datePatternStrict));
        validator.setMask(this.evaluate(this.mask));
        validator.setMessage(this.evaluate(this.message));
        validator.setArg(this.evaluate(this.arg));
        validator.setClient(this.evaluateBoolean(this.client));
        validator.setServer(this.evaluateBoolean(this.server));
        return validator;
    }

    /**
     * Evaluate the <code>expression</code>. If it's a value reference, get
     * the reference's value. Otherwise, return the <code>expression</code>.
     *
     * @param expression The expression
     */
    public String evaluate(String expression)
    {
        if (expression != null)
        {
            if (UIComponentTag.isValueReference(expression))
            {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                expression = "" + app.createValueBinding(expression).getValue(context);
            }
        }
        return expression;
    }

    /**
     * Evaluates the <code>expression</code> and return an <code>Integer</code>.
     *
     * @param expression The expression
     */
    public Integer evaluateInteger(final String expression)
    {
        Integer result = null;
        if (expression != null)
        {
            if (UIComponentTag.isValueReference(expression))
            {
                final FacesContext context = FacesContext.getCurrentInstance();
                final Application application = context.getApplication();
                final Object value = application.createValueBinding(expression).getValue(context);
                if (value != null)
                {
                    if (value instanceof Integer)
                    {
                        result = (Integer)value;
                    }
                    else
                    {
                        result = Integer.valueOf(value.toString());
                    }
                }
            }
            else
            {
                result = Integer.valueOf(expression);
            }
        }
        return result;
    }

    /**
     * Evaluates the <code>expression</code> and return a <code>Double</code>.
     *
     * @param expression The expression
     */
    public Double evaluateDouble(final String expression)
    {
        Double result = null;
        if (expression != null)
        {
            if (UIComponentTag.isValueReference(expression))
            {
                final FacesContext context = FacesContext.getCurrentInstance();
                final Application application = context.getApplication();
                final Object value = application.createValueBinding(expression).getValue(context);
                if (value != null)
                {
                    if (value instanceof Double)
                    {
                        result = (Double)value;
                    }
                    else
                    {
                        result = Double.valueOf(value.toString());
                    }
                }
            }
            else
            {
                result = Double.valueOf(expression);
            }
        }
        return result;
    }

    /**
     * Evaluates the <code>expression</code> and return a <code>Boolean</code>.
     *
     * @param expression The expression
     */
    public Boolean evaluateBoolean(final String expression)
    {
        Boolean result = null;
        if (expression != null)
        {
            if (UIComponentTag.isValueReference(expression))
            {
                final FacesContext context = FacesContext.getCurrentInstance();
                final Application application = context.getApplication();
                final Object value = application.createValueBinding(expression).getValue(context);
                if (value != null)
                {
                    if (value instanceof Boolean)
                    {
                        result = (Boolean)value;
                    }
                    else
                    {
                        result = Boolean.valueOf(value.toString());
                    }
                }
            }
            else
            {
                result = Boolean.valueOf(expression);
            }
        }
        return result;
    }

    /**
     * Sets all instance objects representing tag attribute values to
     * <code>null</code>.
     */
    public void release()
    {
        this.type = null;
        this.min = null;
        this.max = null;
        this.minlength = null;
        this.maxlength = null;
        this.datePatternStrict = null;
        this.mask = null;
        this.message = null;
        this.arg = null;
        this.client = null;
        this.server = null;
    }
}