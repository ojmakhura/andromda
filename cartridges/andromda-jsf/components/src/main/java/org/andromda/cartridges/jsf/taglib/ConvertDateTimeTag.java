package org.andromda.cartridges.jsf.taglib;

import javax.el.ExpressionFactory;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.PageContext;
import org.andromda.cartridges.jsf.converters.JSFDateTimeConverter;

/**
 * Extends the default myfaces convert date time tag in order to use
 * the custom date time converter {@link org.andromda.cartridges.jsf.converters.JSFDateTimeConverter}.
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
     * converter: {@link org.andromda.cartridges.jsf.converters.JSFDateTimeConverter}.
     *
     * @see javax.servlet.jsp.tagext.Tag#setPageContext(javax.servlet.jsp.PageContext)
     */
    @Override
    public void setPageContext(PageContext context)
    {
        super.setPageContext(context);
        ExpressionFactory expressionFactory =
            FacesContext.getCurrentInstance().getApplication().getExpressionFactory();
        setConverterId(expressionFactory.createValueExpression(context.getELContext(), JSFDateTimeConverter.CONVERTER_ID, String.class));
    }
}