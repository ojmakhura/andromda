package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3InterceptorFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3InterceptorFacade
 */
public class EJB3InterceptorFacadeLogicImpl
    extends EJB3InterceptorFacadeLogic
{
    /**
     * The property which stores the pattern defining the interceptor class name.
     */
    private static final String INTERCEPTOR_NAME_PATTERN = "interceptorNamePattern";

    public EJB3InterceptorFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3InterceptorFacade#getInterceptorName()
     */
    protected java.lang.String handleGetInterceptorName()
    {
        String interceptorNamePattern = (String)this.getConfiguredProperty(INTERCEPTOR_NAME_PATTERN);

        return MessageFormat.format(
                interceptorNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3InterceptorFacade#getFullyQualifiedInterceptorName()
     */
    protected java.lang.String handleGetFullyQualifiedInterceptorName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getInterceptorName(),
                null);
    }

}