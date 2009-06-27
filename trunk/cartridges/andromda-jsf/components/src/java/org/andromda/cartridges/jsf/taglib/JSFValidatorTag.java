package org.andromda.cartridges.jsf.taglib;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import org.andromda.cartridges.jsf.component.JSFValidatorComponent;


/**
 * The tag class for the <code>s:validatorScript</code> tag.
 */
public class JSFValidatorTag
    extends UIComponentTag
{
    /**
     * Whether or not client side validation should be enabled
     */
    private String client;

    /**
     * Sets whether or not client side validation shall be enabled.
     *
     * @param client a true/false string.
     */
    public void setClient(final String client)
    {
        this.client = client;
    }

    /**
     * Sets properties for the component.
     *
     * @param component The component whose properties we're setting
     */
    public void setProperties(final UIComponent component)
    {
        super.setProperties(component);

        final String attributeName = JSFValidatorComponent.CLIENT;
        final String attributeValue = this.client;
        if (attributeValue != null)
        {
            if (UIComponentTag.isValueReference(this.client))
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
        }
        final String validatorId = this.getId();
        if (validatorId != null)
        {
            component.setId(validatorId);
        }
    }

    /**
     * Sets the <code>client</code> property to null.
     * 
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release()
    {
        super.release();
        this.client = null;
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
    private static final String COMPONENT_TYPE = JSFValidatorComponent.class.getName();

    /**
     * Returns the component type, which is
     * <code>org.andromda.cartridges.jsf.component.JSFValidatorScript</code>.
     */
    public String getComponentType()
    {
        return COMPONENT_TYPE;
    }
}