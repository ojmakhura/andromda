package org.andromda.cartridges.jsf.converters;

import java.util.Calendar;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

/**
 * Overrides the default DateTimeConverter to include conversion of Calendar
 * instances as well as Date instances.
 * 
 * @author Chad Brandon
 */
public class DateTimeConverter
    extends javax.faces.convert.DateTimeConverter
{
   
    /**
     * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
     */
    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException
    {
        if (value instanceof Calendar)
        {
            value = ((Calendar)value).getTime();
        }
        return super.getAsString(context, component, value);
    }

    /**
     * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
     */
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException
    {
        Object asObject = super.getAsObject(context, component, value);
        if (asObject instanceof Date)
        {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date)asObject);
            asObject = calendar;
        }
        return asObject;
    }
    
    public static final String CONVERTER_ID = "andromda.faces.DateTime";
}