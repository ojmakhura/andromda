package org.andromda.cartridges.jsf.converters;

import java.util.Calendar;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.custom.calendar.HtmlCalendarRenderer.DateConverter;


/**
 * Overrides the default DateTimeConverter to include conversion of Calendar
 * instances as well as Date instances.
 *
 * <p>
 * Unfortunately because of poor design in myfaces's calendar component, we have to implement
 * DateConverter so that we can correctly convert to a date in the inputCalendar implementation.
 * </p>
 *
 * @author Chad Brandon
 */
public class DateTimeConverter
    extends javax.faces.convert.DateTimeConverter
    implements DateConverter
{
    /**
     * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
     */
    public String getAsString(
        FacesContext context,
        UIComponent component,
        Object value)
        throws ConverterException
    {
        if (value instanceof Calendar)
        {
            value = ((Calendar)value).getTime();
        }
        final String result = super.getAsString(
                context,
                component,
                value);
        return result;
    }

    /**
     * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
     */
    public Object getAsObject(
        FacesContext context,
        UIComponent component,
        String value)
        throws ConverterException
    {
        Object asObject = null;
        final Class componentType = this.getComponentType(
                context,
                component);
        if (componentType != null)
        {
            asObject = super.getAsObject(
                    context,
                    component,
                    value);
            if (Calendar.class.isAssignableFrom(componentType) && asObject instanceof Date)
            {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime((Date)asObject);
                asObject = calendar;
            }
        }
        return asObject;
    }

    /**
     * Gets the component type for the given <code>component</code>.
     * @param context the current faces context.
     * @param component the component from which to retrieve the type.
     * @return true/false
     */
    private Class getComponentType(
        final FacesContext context,
        final UIComponent component)
    {
        Class type = null;
        final ValueBinding binding = component.getValueBinding("value");
        if (binding != null)
        {
            type = binding.getType(context);
        }
        return type;
    }

    /**
     * Gets the component Value for the given <code>component</code>.
     * @param context the current faces context.
     * @param component the component from which to retrieve the value.
     * @return true/false
     */
    private Object getComponentValue(
        final FacesContext context,
        final UIComponent component)
    {
        Object value = null;
        final ValueBinding binding = component.getValueBinding("value");
        if (binding != null)
        {
            value = binding.getValue(context);
        }
        return value;
    }

    public static final String CONVERTER_ID = "andromda.faces.DateTime";

    /**
     * @see org.apache.myfaces.custom.calendar.HtmlCalendarRenderer.DateConverter#getAsDate(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public Date getAsDate(
        FacesContext context,
        UIComponent component)
    {
        Object value = this.getComponentValue(
                context,
                component);
        if (value instanceof Calendar)
        {
            value = ((Calendar)value).getTime();
        }
        return (Date)value;
    }
}