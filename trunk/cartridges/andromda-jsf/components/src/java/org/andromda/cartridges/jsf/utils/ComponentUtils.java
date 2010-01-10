package org.andromda.cartridges.jsf.utils;

import java.lang.reflect.Method;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import org.apache.commons.lang.ObjectUtils;


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
                final ValueBinding binding = context.getApplication().createValueBinding(value);
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
                final ValueBinding binding = context.getApplication().createValueBinding(value);
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
                final ValueBinding binding = context.getApplication().createValueBinding(value);
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

    /**
     * Gets the attribute from the given object.  The object can be either a context, request
     * or response (HttpServletContext/PortletContext, HttpServletRequest/PortletRequest, etc).
     *
     * @param object the object from which to retrieve the attribute.
     * @param attributeName the attribute name.
     * @return the value of the attribute if one is present.
     */
    public static Object getAttribute(final Object object, final String attributeName)
    {
        try
        {
            Object attribute = null;
            if (object != null)
            {
                try
                {
                    final Method method = object.getClass().getMethod("getAttribute", new Class[]{String.class});
                    attribute = method.invoke(object, attributeName);
                }
                catch (NoSuchMethodException exception)
                {
                    // Swallow Exception
                }
            }
            return attribute;
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Sets the attribute on the given object.  The object can be either a context, request
     * or response (HttpServletContext/PortletContext, HttpServletRequest/PortletRequest, etc).
     *
     * @param object the object on which to set the attribute.
     * @param attributeName the attribute name.
     * @param attributeValue the value of the attribute to set.
     */
    public static void setAttribute(final Object object, final String attributeName, final Object attributeValue)
    {
        try
        {
            if (object != null)
            {
                try
                {
                    final Method method = object.getClass().getMethod("setAttribute", new Class[]{String.class, Object.class});
                    method.invoke(object, attributeName, attributeValue);
                }
                catch (NoSuchMethodException ignore)
                {
                    // Swallow exception
                }
            }
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Gets the context path from the given request object (PortletRequest/HttpServletRequest)
     *
     * @param request the request object from which to retrieve the context path.
     * @return the context path.
     */
    public static String getContextPath(final Object request)
    {
        try
        {
            String contextPath = null;
            if (request != null)
            {
                contextPath = ObjectUtils.toString(request.getClass().getMethod("getContextPath", new Class[0]).invoke(request));
            }
            return contextPath;
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
    }
}