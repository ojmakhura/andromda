package org.andromda.cartridges.jsf.metafacades;

import org.andromda.cartridges.jsf.metafacades.JSFBackendServiceLogic;
import org.andromda.cartridges.jsf.JSFGlobals;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFBackendService.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFBackendService
 */
public class JSFBackendServiceLogicImpl
    extends JSFBackendServiceLogic
{
    public JSFBackendServiceLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFBackendService#getAccessorImplementation()
     */
    protected java.lang.String handleGetAccessorImplementation()
    {
        String accessorImplementation = String.valueOf(getConfiguredProperty(JSFGlobals.SERVICE_ACCESSOR_PATTERN));
        return accessorImplementation.replaceAll(
            "\\{0\\}",
            getPackageName()).replaceAll(
            "\\{1\\}",
            getName());
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName()
     */
    public java.lang.String getFullyQualifiedName()
    {
        String packageName = String.valueOf(getConfiguredProperty(JSFGlobals.SERVICE_PACKAGE_NAME_PATTERN));
        return packageName.replaceAll(
            "\\{0\\}",
            super.getPackageName()) + "." + this.getName();
    }
}