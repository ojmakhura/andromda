package org.andromda.cartridges.bpm4jsf.component;

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

import org.andromda.cartridges.bpm4jsf.validator.BPM4JSFValidator;
import org.andromda.cartridges.bpm4jsf.validator.BPM4JSFValidatorException;
import org.andromda.cartridges.bpm4jsf.validator.ValidatorMessages;
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
 * specified in the same JSP page (with <code>bpm4jsf:validator</code>.
 */
public class BPM4JSFValidatorComponent
    extends UIComponentBase
{
    private static final Log logger = LogFactory.getLog(BPM4JSFValidatorComponent.class);

    /**
     * A map of validators, representing all of the Commons Validators attached
     * to components in the current component hierarchy. The keys of the map are
     * validator type names. The values are maps from IDs to BPM4JSFValidator
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
     * @param validator The BPM4JSF validator associated with the id and type
     */
    private void addValidator(
        final String type,
        final String id,
        final BPM4JSFValidator validator)
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
    private void findBpm4JsfValidators(
        final UIComponent component,
        final FacesContext context)
    {
        if (component instanceof EditableValueHolder)
        {
            final EditableValueHolder valueHolder = (EditableValueHolder)component;
            final UIForm form = BPM4JSFValidator.findForm(component);
            if (form != null)
            {
                final String componentId = component.getId();
                final ValidatorResources resources = BPM4JSFValidator.getValidatorResources();
                if (resources != null)
                {
                    final Form validatorForm = resources.getForm(
                            Locale.getDefault(),
                            form.getId());
                    final java.util.List validatorFields = validatorForm.getFields();
                    for (final Iterator iterator = validatorFields.iterator(); iterator.hasNext();)
                    {
                        final Field field = (Field)iterator.next();
                        if (componentId.equals(field.getProperty()))
                        {
                            for (final Iterator dependencyIterator = field.getDependencyList().iterator();
                                dependencyIterator.hasNext();)
                            {
                                final String dependency = (String)dependencyIterator.next();
                                final ValidatorAction action = BPM4JSFValidator.getValidatorAction(dependency);
                                if (action != null)
                                {
                                    final BPM4JSFValidator validator = new BPM4JSFValidator(action);
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
                                        "' registered in rules files '" + BPM4JSFValidator.RULES_LOCATION + "'");
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
            this.findBpm4JsfValidators(
                childComponent,
                context);
        }
    }

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
    private static final String FUNCTION_NAME = "functionName";

    /**
     * writes the javascript functions to the response.
     *
     * @param writer A response writer
     * @param context The FacesContext for this request
     */
    private final void writeValidationFunctions(
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
            final ValidatorAction action = BPM4JSFValidator.getValidatorAction(type);
            final String functionName = this.getJavaScriptFunctionName(action);
            if (StringUtils.isBlank(functionName))
            {
                iterator.remove();
            }
        }

        for (final Iterator iterator = validatorTypes.iterator(); iterator.hasNext();)
        {
            final String type = (String)iterator.next();
            final ValidatorAction action = BPM4JSFValidator.getValidatorAction(type);
            writer.write("&& ");
            writer.write(this.getJavaScriptFunctionName(action));
            writer.write("(form)\n");
        }
        writer.write(";}\n");

        // - for each validator type, write callback
        for (final Iterator iterator = validatorTypes.iterator(); iterator.hasNext();)
        {
            final String type = (String)iterator.next();
            final ValidatorAction action = BPM4JSFValidator.getValidatorAction(type);
            writer.write("function ");
            String callback = action.getJsFunctionName();
            if (callback == null)
            {
                callback = type;
            }
            writer.write(callback);
            writer.write("() { \n");

            // for each field validated by this type, add configuration object
            final Map map = (Map)this.validators.get(type);
            int ctr = 0;
            for (final Iterator idIterator = map.keySet().iterator(); idIterator.hasNext(); ctr++)
            {
                final String id = (String)idIterator.next();
                final BPM4JSFValidator validator = (BPM4JSFValidator)map.get(id);
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
        //   Must always include integer and required because
        //   they contain shared helper functions
        this.addValidator(
            "integer",
            null,
            null);
        this.addValidator(
            "required",
            null,
            null);

        for (final Iterator iterator = validatorTypes.iterator(); iterator.hasNext();)
        {
            final String type = (String)iterator.next();
            final ValidatorAction action = BPM4JSFValidator.getValidatorAction(type);
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
        final BPM4JSFValidator validator)
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
     * Begin encoding for this component. This method finds all Commons
     * validators attached to components in the current component hierarchy and
     * writes out JavaScript code to invoke those validators, in turn.
     *
     * @param context The FacesContext for this request
     */
    public void encodeBegin(final FacesContext context)
        throws IOException
    {
        try
        {
            final ResponseWriter writer = context.getResponseWriter();
            this.validators.clear();
            this.findBpm4JsfValidators(
                context.getViewRoot(),
                context);
            if (this.getAttributes().get(FUNCTION_NAME) != null)
            {
                this.writeScriptStart(writer);
                this.writeValidationFunctions(
                    writer,
                    context);
                this.writeScriptEnd(writer);
            }
        }
        catch (final BPM4JSFValidatorException exception)
        {
            logger.error(exception);
        }
    }

    public static void main(String[] args)
    {
        System.out.println("maxi value: " + Long.MAX_VALUE);
        System.out.println("min value: " + Long.MIN_VALUE);
    }
}