package org.andromda.cartridges.thymeleaf.metafacades;

import java.text.MessageFormat;

import org.andromda.cartridges.thymeleaf.ThymeleafGlobals;
import org.andromda.cartridges.thymeleaf.ThymeleafUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.thymeleaf.metafacades.ThymeleafBackendService.
 *
 * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafBackendService
 */
public class ThymeleafBackendServiceLogicImpl
    extends ThymeleafBackendServiceLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ThymeleafBackendServiceLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return accessorImplementation
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafBackendService#getAccessorImplementation()
     */
    protected String handleGetAccessorImplementation()
    {
        String accessorImplementation = String.valueOf(getConfiguredProperty(ThymeleafGlobals.SERVICE_ACCESSOR_PATTERN));
        return accessorImplementation.replaceAll("\\{0\\}",
            getPackageName()).replaceAll("\\{1\\}", getName());
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        String packageName = String.valueOf(getConfiguredProperty(ThymeleafGlobals.SERVICE_PACKAGE_NAME_PATTERN));
        return packageName.replaceAll(
            "\\{0\\}",
            super.getPackageName()) + "." + this.getName();
    }

    @Override
    protected String handleGetImplementationPackageName() {
        String implementationPackageName =
            MessageFormat.format(
                this.getImplemenationPackageNamePattern(),
                    StringUtils.trimToEmpty(this.getPackageName()));
        if (StringUtils.isBlank(this.getPackageName()))
        {
            implementationPackageName = implementationPackageName.replaceAll(
                    "^\\.",
                    "");
        }
        return implementationPackageName;
    }

    /**
     * Gets the <code>implementationPackageNamePattern</code> for this SpringService.
     *
     * @return the defined package pattern.
     */
    protected String getImplemenationPackageNamePattern()
    {
        return (String)this.getConfiguredProperty(ThymeleafGlobals.IMPLEMENTATION_PACKAGE_NAME_PATTERN);
    }

    @Override
    protected String handleGetFullyQualifiedImplementationName() {
        return ThymeleafUtils.getFullyQualifiedName(
            this.getImplementationPackageName(),
            this.getName(),
            ThymeleafGlobals.IMPLEMENTATION_SUFFIX);
    }

    @Override
    protected String handleGetImplementationName() {
        return this.getName() + ThymeleafGlobals.IMPLEMENTATION_SUFFIX;
    }

    @Override
    protected String handleGetImplementationPackagePath() {
        return this.getImplementationPackageName().replace(
                    '.',
                    '/');
    }

    @Override
    protected String handleGetBeanName() {
        return StringUtils.uncapitalize(this.getName());
    }

    @Override
    protected String handleGetBeanName(boolean targetSuffix) {
        StringBuilder beanName = new StringBuilder(String.valueOf(this.getConfiguredProperty(ThymeleafGlobals.BEAN_NAME_PREFIX)));
        beanName.append(StringUtils.uncapitalize(StringUtils.trimToEmpty(this.getName())));
        if (targetSuffix)
        {
            beanName.append(ThymeleafGlobals.BEAN_NAME_TARGET_SUFFIX);
        }
        return beanName.toString();
    }
}
