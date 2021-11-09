package org.andromda.cartridges.jakarta.metafacades;

import java.text.MessageFormat;

import org.andromda.cartridges.jakarta.JakartaGlobals;
import org.andromda.cartridges.jakarta.JakartaUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jakarta.metafacades.JakartaBackendService.
 *
 * @see org.andromda.cartridges.jakarta.metafacades.JakartaBackendService
 */
public class JakartaBackendServiceLogicImpl
    extends JakartaBackendServiceLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JakartaBackendServiceLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return accessorImplementation
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaBackendService#getAccessorImplementation()
     */
    protected String handleGetAccessorImplementation()
    {
        String accessorImplementation = String.valueOf(getConfiguredProperty(JakartaGlobals.SERVICE_ACCESSOR_PATTERN));
        return accessorImplementation.replaceAll("\\{0\\}",
            getPackageName()).replaceAll("\\{1\\}", getName());
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        String packageName = String.valueOf(getConfiguredProperty(JakartaGlobals.SERVICE_PACKAGE_NAME_PATTERN));
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
        return (String)this.getConfiguredProperty(JakartaGlobals.IMPLEMENTATION_PACKAGE_NAME_PATTERN);
    }

    @Override
    protected String handleGetFullyQualifiedImplementationName() {
        return JakartaUtils.getFullyQualifiedName(
            this.getImplementationPackageName(),
            this.getName(),
            JakartaGlobals.IMPLEMENTATION_SUFFIX);
    }

    @Override
    protected String handleGetImplementationName() {
        return this.getName() + JakartaGlobals.IMPLEMENTATION_SUFFIX;
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
        StringBuilder beanName = new StringBuilder(String.valueOf(this.getConfiguredProperty(JakartaGlobals.BEAN_NAME_PREFIX)));
        beanName.append(StringUtils.uncapitalize(StringUtils.trimToEmpty(this.getName())));
        if (targetSuffix)
        {
            beanName.append(JakartaGlobals.BEAN_NAME_TARGET_SUFFIX);
        }
        return beanName.toString();
    }
}
