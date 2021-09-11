package org.andromda.cartridges.jakarta.metafacades;

import org.andromda.cartridges.jakarta.JakartaGlobals;

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
}
