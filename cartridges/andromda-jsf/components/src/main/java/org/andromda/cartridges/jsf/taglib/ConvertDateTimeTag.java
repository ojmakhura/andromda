package org.andromda.cartridges.jsf.taglib;

import javax.servlet.jsp.PageContext;

import org.andromda.cartridges.jsf.converters.DateTimeConverter;


/**
 * Extends the default myfaces convert date time tag in order to use
 * the custom date time converer {@link org.andromda.cartridges.jsf.converters.DateTimeConverter}.
 *
 * @author Chad Brandon
 */
public class ConvertDateTimeTag
    extends org.apache.myfaces.taglib.core.ConvertDateTimeTag
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Overridden to set the converter id using out custom date time
     * converter: {@link org.andromda.cartridges.jsf.converters.DateTimeConverter}.
     *
     * @see javax.servlet.jsp.tagext.Tag#setPageContext(javax.servlet.jsp.PageContext)
     */
    @Override
    public void setPageContext(PageContext context)
    {
        super.setPageContext(context);
        setConverterId(DateTimeConverter.CONVERTER_ID);
    }
}