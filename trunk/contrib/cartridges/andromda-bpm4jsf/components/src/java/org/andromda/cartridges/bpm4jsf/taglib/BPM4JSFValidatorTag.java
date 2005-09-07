package org.andromda.cartridges.bpm4jsf.taglib;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import org.andromda.cartridges.bpm4jsf.component.BPM4JSFValidatorComponent;


/**
 * The tag class for the <code>s:validatorScript</code> tag.
 */
public class BPM4JSFValidatorTag
    extends UIComponentTag
{
    /**
     * The function name for validating the enclosing form.
     */
    private String functionName;

    /**
     * Sets the function name.
     *
     * @param functionName The new value for the function name.
     */
    public void setFunctionName(final String functionName)
    {
        this.functionName = functionName;
    }

    /**
     * Sets properties for the component.
     *
     * @param component The component whose properties we're setting
     */
    public void setProperties(final UIComponent component)
    {
        super.setProperties(component);

        final String attributeName = "functionName";
        final String attributeValue = this.functionName;
        if (attributeValue != null)
        {
            if (UIComponentTag.isValueReference(this.functionName))
            {
                final FacesContext context = FacesContext.getCurrentInstance();
                final Application application = context.getApplication();
                final ValueBinding binding = application.createValueBinding(attributeValue);
                component.setValueBinding(
                    attributeName,
                    binding);
            }
            else
            {
                component.getAttributes().put(
                    attributeName,
                    attributeValue);
            }
            component.setId(attributeValue);
        }
    }

    /**
     * Sets the <code>functionName</code> property to null.
     */
    public void release()
    {
        this.functionName = null;
    }

    /**
     * Returns the renderer type, which is null.
     */
    public String getRendererType()
    {
        return null;
    }

    /**
     * The component type.
     */
    private static final String COMPONENT_TYPE = BPM4JSFValidatorComponent.class.getName();

    /**
     * Returns the component type, which is
     * <code>org.andromda.cartridges.bpm4jsf.component.BPM4JSFValidatorScript</code>.
     */
    public String getComponentType()
    {
        return COMPONENT_TYPE;
    }
}