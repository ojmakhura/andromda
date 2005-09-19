package org.andromda.cartridges.jsf.component;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.andromda.cartridges.jsf.validator.JSFValidator;
import org.andromda.cartridges.jsf.validator.JSFValidatorException;
import org.andromda.cartridges.jsf.validator.ValidatorMessages;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorResources;


/**
 * A JSF component that enabled the commons-validator server side validation, as well
 * as encodes JavaScript for all client-side validations
 * specified in the same JSP page (with <code>jsf:validator</code>.
 */
public class JSFValidatorComponent
    extends UIComponentBase
{
    private static final Log logger = LogFactory.getLog(JSFValidatorComponent.class);

    /**
     * A map of validators, representing all of the Commons Validators attached
     * to components in the current component hierarchy. The keys of the map are
     * validator type names. The values are maps from IDs to JSFValidator
     * objects.
     */
    private final Map validators = new LinkedHashMap();

    /**
     * The component renders itself; therefore, this method returns null.
     */
    public String getRendererType()
    {
        return null;
    }

    /**
     * Returns the component's family. In this case, the component is not
     * associated with a family, so this method returns null.
     */
    public String getFamily()
    {
        return null;
    }

    /**
     * Registers a validator according to type and id.
     *
     * @param type The type of the validator
     * @param id The validator's identifier
     * @param validator The JSF validator associated with the id and type
     */
    private void addValidator(
        final String type,
        final String id,
        final JSFValidator validator)
    {
        Map map = (Map)this.validators.get(type);
        if (map == null)
        {
            map = new LinkedHashMap();
            this.validators.put(
                type,
                map);
        }
        if (id != null)
        {
            map.put(
                id,
                validator);
        }
    }

    private HttpServletRequest getRequest()
    {
        return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    /**
     * <p>
     * Recursively finds all Commons validators for the all of the components in
     * a component hierarchy and adds them to a map.
     * </p>
     * If a validator's type is required, this method sets the associated
     * component's required property to true. This is necessary because JSF does
     * not validate empty fields unless a component's required property is true.
     *
     * @param component The component at the root of the component tree
     * @param context The FacesContext for this request
     */
    private void findValidators(
        final UIComponent component,
        final FacesContext context)
    {
        if (component instanceof EditableValueHolder)
        {
            final EditableValueHolder valueHolder = (EditableValueHolder)component;
            final UIForm form = JSFValidator.findForm(component);
            if (form != null)
            {
                final String componentId = component.getId();
                final ValidatorResources resources = JSFValidator.getValidatorResources();
                if (resources != null)
                {
                    final Form validatorForm = resources.getForm(
                            Locale.getDefault(),
                            form.getId());
                    if (validatorForm != null)
                    {
                        final java.util.List validatorFields = validatorForm.getFields();
                        for (final Iterator iterator = validatorFields.iterator(); iterator.hasNext();)
                        {
                            final Field field = (Field)iterator.next();

                            // we need to make it match the name of the id on the jsf components (if its nested).
                            final String fieldProperty = StringUtilsHelper.lowerCamelCaseName(field.getProperty());
                            if (componentId.equals(fieldProperty))
                            {
                                for (final Iterator dependencyIterator = field.getDependencyList().iterator();
                                    dependencyIterator.hasNext();)
                                {
                                    final String dependency = (String)dependencyIterator.next();
                                    final ValidatorAction action = JSFValidator.getValidatorAction(dependency);
                                    if (action != null)
                                    {
                                        final JSFValidator validator = new JSFValidator(action);
                                        final Arg[] args = field.getArgs(dependency);
                                        if (args != null)
                                        {
                                            for (final Iterator varIterator = field.getVars().keySet().iterator();
                                                varIterator.hasNext();)
                                            {
                                                final String name = (String)varIterator.next();
                                                validator.addParameter(
                                                    name,
                                                    field.getVarValue(name));
                                            }
                                            validator.setArgs(ValidatorMessages.getArgs(
                                                    dependency,
                                                    field));
                                            valueHolder.addValidator(validator);
                                            this.addValidator(
                                                dependency,
                                                component.getClientId(context),
                                                validator);
                                        }
                                    }
                                    else
                                    {
                                        logger.error(
                                            "No validator action with name '" + dependency +
                                            "' registered in rules files '" + JSFValidator.RULES_LOCATION + "'");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (final Iterator iterator = component.getFacetsAndChildren(); iterator.hasNext();)
        {
            final UIComponent childComponent = (UIComponent)iterator.next();
            this.findValidators(
                childComponent,
                context);
        }
    }

    private static final String JAVASCRIPT_UTILITIES = "javascriptUtilities";

    /**
     * Write the start of the script for client-side validation.
     *
     * @param writer A response writer
     */
    private final void writeScriptStart(final ResponseWriter writer)
        throws IOException
    {
        writer.startElement(
            "script",
            this);
        writer.writeAttribute(
            "type",
            "text/javascript",
            null);
        writer.writeAttribute(
            "language",
            "Javascript1.1",
            null);
        writer.write("\n<!--\n");
    }

    /**
     * Write the end of the script for client-side validation.
     *
     * @param writer A response writer
     */
    private void writeScriptEnd(ResponseWriter writer)
        throws IOException
    {
        writer.write("\n-->\n");
        writer.endElement("script");
    }

    /**
     * Returns the name of the JavaScript function, specified in the JSP page that validates this JSP page's form.
     *
     * @param action the validation action from which to retrieve the function name.
     */
    private String getJavaScriptFunctionName(final ValidatorAction action)
    {
        String functionName = null;
        final String javascript = action.getJavascript();
        if (StringUtils.isNotBlank(javascript))
        {
            final String function = "function ";
            int functionIndex = javascript.indexOf(function);
            functionName = javascript.substring(functionIndex + 9);
            functionName = functionName.substring(
                    0,
                    functionName.indexOf('(')).replaceAll(
                    "[\\s]+",
                    " ");
        }
        return functionName;
    }

    /**
     * The function name attribute storing the function name to render
     * when using client side validation.
     */
    public static final String FUNCTION_NAME = "functionName";

    /**
     * The key for the current validator id.
     */
    public static final String VALIDATOR_ID = "validatorId";

    /**
     * writes the javascript functions to the response.
     *
     * @param writer A response writer
     * @param context The FacesContext for this request
     */
    private final void writeValidationFunctions(
        final UIForm form,
        final ResponseWriter writer,
        final FacesContext context)
        throws IOException
    {
        writer.write("var bCancel = false;\n");
        writer.write("function ");
        writer.write(this.getAttributes().get(FUNCTION_NAME).toString());
        writer.write("(form) { return bCancel || true\n");

        // - for each validator type, write "&& fun(form);
        final Collection validatorTypes = new ArrayList(this.validators.keySet());

        // - remove any validators that don't have javascript functions defined.
        for (final Iterator iterator = validatorTypes.iterator(); iterator.hasNext();)
        {
            final String type = (String)iterator.next();
            final ValidatorAction action = JSFValidator.getValidatorAction(type);
            final String functionName = this.getJavaScriptFunctionName(action);
            if (StringUtils.isBlank(functionName))
            {
                iterator.remove();
            }
        }

        for (final Iterator iterator = validatorTypes.iterator(); iterator.hasNext();)
        {
            final String type = (String)iterator.next();
            final ValidatorAction action = JSFValidator.getValidatorAction(type);
            if (!JAVASCRIPT_UTILITIES.equals(type))
            {
                writer.write("&& ");
                writer.write(this.getJavaScriptFunctionName(action));
                writer.write("(form)\n");
            }
        }
        writer.write(";}\n");

        // - for each validator type, write callback
        for (final Iterator iterator = validatorTypes.iterator(); iterator.hasNext();)
        {
            final String type = (String)iterator.next();
            final ValidatorAction action = JSFValidator.getValidatorAction(type);
            String callback = action.getJsFunctionName();
            if (StringUtils.isBlank(callback))
            {
                callback = type;
            }
            writer.write("function ");
            writer.write(form.getId() + "_" + callback);
            writer.write("() { \n");

            // for each field validated by this type, add configuration object
            final Map map = (Map)this.validators.get(type);
            int ctr = 0;
            for (final Iterator idIterator = map.keySet().iterator(); idIterator.hasNext(); ctr++)
            {
                final String id = (String)idIterator.next();
                final JSFValidator validator = (JSFValidator)map.get(id);
                writer.write("this[" + ctr + "] = ");
                this.writeJavaScriptParams(
                    writer,
                    context,
                    id,
                    validator);
                writer.write(";\n");
            }
            writer.write("}\n");
        }

        // - for each validator type, write code
        for (final Iterator iterator = validatorTypes.iterator(); iterator.hasNext();)
        {
            final String type = (String)iterator.next();
            final ValidatorAction action = JSFValidator.getValidatorAction(type);
            writer.write(action.getJavascript());
            writer.write("\n");
        }
    }

    /**
     * Writes the JavaScript parameters for the client-side validation code.
     *
     * @param writer A response writer
     * @param context The FacesContext for this request
     * @param validator The Commons validator
     */
    private void writeJavaScriptParams(
        final ResponseWriter writer,
        final FacesContext context,
        final String id,
        final JSFValidator validator)
        throws IOException
    {
        writer.write("new Array(\"");
        writer.write(id);
        writer.write("\", \"");
        writer.write(validator.getErrorMessage(context));
        writer.write("\", new Function(\"x\", \"return {");

        final Map parameters = validator.getParameters();
        for (final Iterator iterator = parameters.keySet().iterator(); iterator.hasNext();)
        {
            final String name = (String)iterator.next();
            if (iterator.hasNext())
            {
                writer.write(",");
            }
            writer.write(name);
            writer.write(":");

            boolean mask = name.equals("mask");

            // - mask validator does not construct regular expression
            if (mask)
            {
                writer.write("/");
            }
            else
            {
                writer.write("'");
            }
            final Object parameter = parameters.get(name);
            writer.write(parameter.toString());
            if (mask)
            {
                writer.write("/");
            }
            else
            {
                writer.write("'");
            }
        }
        writer.write("}[x];\"))");
    }

    /**
     * Stores all forms found within this view.
     */
    private final Collection forms = new ArrayList();

    private UIForm findForm(final String id)
    {
        UIForm form = null;
        final UIComponent validator = this.findComponent(id);
        if (validator instanceof JSFValidatorComponent)
        {
            final UIComponent parent = validator.getParent();
            if (parent instanceof UIForm)
            {
                form = (UIForm)parent;
            }
        }
        return form;
    }

    /**
     * Used to keep track of whether or not the validation rules are present or not.
     */
    private static final String RULES_NOT_PRESENT = "validationRulesNotPresent";

    /**
     * Begin encoding for this component. This method finds all Commons
     * validators attached to components in the current component hierarchy and
     * writes out JavaScript code to invoke those validators, in turn.
     *
     * @param context The FacesContext for this request
     */
    public void encodeBegin(final FacesContext context)
        throws IOException
    {
        final ServletContext servletContext = this.getRequest().getSession().getServletContext();
        boolean validationResourcesPresent = servletContext.getAttribute(RULES_NOT_PRESENT) == null;
        if (validationResourcesPresent && JSFValidator.getValidatorResources() == null)
        {
            servletContext.setAttribute(
                RULES_NOT_PRESENT,
                "true");
            validationResourcesPresent = false;
        }
        if (validationResourcesPresent)
        {
            try
            {
                this.validators.clear();
                this.forms.clear();

                // - include the javascript utilities (only once per request)
                if (this.getRequest().getAttribute(JAVASCRIPT_UTILITIES) == null)
                {
                    this.addValidator(
                        JAVASCRIPT_UTILITIES,
                        null,
                        null);
                    this.getRequest().setAttribute(
                        JAVASCRIPT_UTILITIES,
                        "present");
                }
                final String validatorId = (String)this.getAttributes().get(VALIDATOR_ID);
                final UIForm form = this.findForm(validatorId);
                if (form != null)
                {
                    this.findValidators(
                        form,
                        context);
                    final String functionName = (String)this.getAttributes().get(FUNCTION_NAME);
                    if (functionName != null)
                    {
                        final ResponseWriter writer = context.getResponseWriter();
                        this.writeScriptStart(writer);
                        this.writeValidationFunctions(
                            form,
                            writer,
                            context);
                        this.writeScriptEnd(writer);
                    }
                }
            }
            catch (final JSFValidatorException exception)
            {
                logger.error(exception);
            }
        }
    }
}