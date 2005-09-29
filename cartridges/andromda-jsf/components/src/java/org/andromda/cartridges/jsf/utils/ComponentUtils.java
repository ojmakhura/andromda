package org.andromda.cartridges.jsf.utils;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentTag;


/**
 * Utilities for dealing with the JSF components.
 *
 * @author Chad Brandon
 */
public class ComponentUtils
{
    /**
     * Sets the value property of a component.
     * 
     * @param context the current faces context.
     * @param component the component.
     * @param value the value to set.
     */
    public static void setValueProperty(
        final FacesContext context,
        final UIComponent component,
        final String value)
    {
        if (value != null)
        {
            if (UIComponentTag.isValueReference(value))
            {
                final javax.faces.el.ValueBinding binding = context.getApplication().createValueBinding(value);
                component.setValueBinding(
                    "value",
                    binding);
            }
            else if (component instanceof UICommand)
            {
                ((UICommand)component).setValue(value);
            }
            else if (component instanceof UIParameter)
            {
                ((UIParameter)component).setValue(value);
            }
            else if (component instanceof UISelectBoolean)
            {
                ((UISelectBoolean)component).setValue(Boolean.valueOf(value));
            }
            else if (component instanceof UIGraphic)
            {
                ((UIGraphic)component).setValue(value);
            }
            else if (component instanceof ValueHolder)
            {
                ((ValueHolder)component).setValue(value);
            }
        }
    }
    
    /**
     * Sets the property with the given <code>name</code> of a component.
     * @param name the name of the component to set.
     * @param context the current faces context.
     * @param component the component.
     * @param value the value to set.
     */
    public static void setStringProperty(
        final String name,
        final FacesContext context,
        final UIComponent component,
        final String value)
    {
        if (value != null)
        {
            if (UIComponentTag.isValueReference(value))
            {
                final javax.faces.el.ValueBinding binding = context.getApplication().createValueBinding(value);
                component.setValueBinding(
                    name,
                    binding);
            }
            else
            {
                component.getAttributes().put(name, value);
            }
        }
    }
    
    /**
     * Sets the boolean value of property with the given <code>name</code> of a component.
     * @param name the name of the component to set.
     * @param context the current faces context.
     * @param component the component.
     * @param value the value to set.
     */
    public static void setBooleanProperty(
        final String name,
        final FacesContext context,
        final UIComponent component,
        final String value)
    {
        if (value != null)
        {
            if (UIComponentTag.isValueReference(value))
            {
                final javax.faces.el.ValueBinding binding = context.getApplication().createValueBinding(value);
                component.setValueBinding(
                    name,
                    binding);
            }
            else
            {
                component.getAttributes().put(name, Boolean.valueOf(value));
            }
        }
    }
}