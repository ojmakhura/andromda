package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.Collection;
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
 * @see EJB3InterceptorFacade
 */
public class EJB3InterceptorFacadeLogicImpl
    extends EJB3InterceptorFacadeLogic
{
    /**
     * The property which stores the pattern defining the interceptor class name.
     */
    private static final String INTERCEPTOR_NAME_PATTERN = "interceptorNamePattern";

    /**
     * @param metaObject
     * @param context
     */
    public EJB3InterceptorFacadeLogicImpl(final Object metaObject, final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see EJB3InterceptorFacade#getInterceptorName()
     */
    @Override
    protected String handleGetInterceptorName()
    {
        String interceptorNamePattern = (String)this.getConfiguredProperty(INTERCEPTOR_NAME_PATTERN);

        return MessageFormat.format(
                interceptorNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3InterceptorFacade#getFullyQualifiedInterceptorName()
     */
    @Override
    protected String handleGetFullyQualifiedInterceptorName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getInterceptorName(),
                null);
    }

    /**
     * @see EJB3InterceptorFacadeLogic#handleGetInterceptorReferences()
     */
    @Override
    protected Collection<DependencyFacade> handleGetInterceptorReferences()
    {
        Collection<DependencyFacade> references = this.getSourceDependencies();
        CollectionUtils.filter(
            references,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    DependencyFacade dependency = (DependencyFacade)object;
                    ModelElementFacade targetElement = dependency.getTargetElement();
                    return targetElement != null && targetElement.hasStereotype(EJB3Profile.STEREOTYPE_INTERCEPTOR);
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
        final Collection<DependencyFacade> interceptors = new LinkedHashSet(references);
        CollectionUtils.forAllDo(
            references,
            new Closure()
            {
                public void execute(final Object object)
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
     * @see EJB3InterceptorFacadeLogic#handleIsDefaultInterceptor()
     */
    @Override
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
