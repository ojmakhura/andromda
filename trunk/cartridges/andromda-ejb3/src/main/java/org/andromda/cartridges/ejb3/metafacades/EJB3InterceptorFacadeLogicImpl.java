package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.BooleanUtils;
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

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3InterceptorFacadeLogic#handleGetInterceptorReferences()
     */
    protected Collection handleGetInterceptorReferences()
    {
        Collection references = this.getSourceDependencies();
        CollectionUtils.filter(
            references, 
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    DependencyFacade dependency = (DependencyFacade)object;
                    ModelElementFacade targetElement = dependency.getTargetElement();
                    return (targetElement != null && targetElement.hasStereotype(EJB3Profile.STEREOTYPE_INTERCEPTOR));
                }
            });
        CollectionUtils.transform(
            references, 
            new Transformer()
            {
                public Object transform(final Object object)
                {
                    return ((DependencyFacade)object).getTargetElement();
                }
            });
        final Collection interceptors = new LinkedHashSet(references);
        CollectionUtils.forAllDo(
            references,
            new Closure()
            {
                public void execute(Object object)
                {
                    if (object instanceof EJB3InterceptorFacade)
                    {
                        interceptors.addAll(((EJB3InterceptorFacade)object).getInterceptorReferences());
                    }
                }
            });
        return interceptors;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3InterceptorFacadeLogic#handleIsDefaultInterceptor()
     */
    protected boolean handleIsDefaultInterceptor()
    {
        boolean isDefaultInterceptor = false;
        String isDefaultInterceptorStr = 
            String.valueOf(this.findTaggedValue(EJB3Profile.TAGGEDVALUE_DEFAULT_INTERCEPTOR));
        if (StringUtils.isNotBlank(isDefaultInterceptorStr))
        {
            isDefaultInterceptor = BooleanUtils.toBoolean(isDefaultInterceptorStr);
        }
        return isDefaultInterceptor;
    }

}