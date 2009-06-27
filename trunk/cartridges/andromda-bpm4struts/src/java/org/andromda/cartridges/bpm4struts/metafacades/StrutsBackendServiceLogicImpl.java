package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsBackendService.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsBackendService
 */
public class StrutsBackendServiceLogicImpl
    extends StrutsBackendServiceLogic
{
    public StrutsBackendServiceLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsBackendService#getAccessorImplementation()
     */
    protected java.lang.String handleGetAccessorImplementation()
    {
        String accessorImplementation = String.valueOf(
            getConfiguredProperty(Bpm4StrutsGlobals.SERVICE_ACCESSOR_PATTERN));
        return accessorImplementation.replaceAll("\\{0\\}", getPackageName()).replaceAll("\\{1\\}", getName());
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName()
     */
    public java.lang.String getFullyQualifiedName()
    {
        String packageName = String.valueOf(
            getConfiguredProperty(Bpm4StrutsGlobals.SERVICE_PACKAGE_NAME_PATTERN));
        return packageName.replaceAll("\\{0\\}", super.getPackageName()) + "." + this.getName();
    }

}
