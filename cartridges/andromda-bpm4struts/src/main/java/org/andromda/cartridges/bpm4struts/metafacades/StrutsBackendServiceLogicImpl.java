package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.Collection;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.bpm4struts.metafacades.StrutsBackendService.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsBackendService
 */
public class StrutsBackendServiceLogicImpl extends StrutsBackendServiceLogic {
    private static final long serialVersionUID = 34L;

    /**
     * @param metaObject
     * @param context
     */
    public StrutsBackendServiceLogicImpl(Object metaObject, String context) {
        super(metaObject, context);
    }

    /**
     * @return Bpm4StrutsGlobals.SERVICE_ACCESSOR_PATTERN) replace getName()
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsBackendService#getAccessorImplementation()
     */
    protected String handleGetAccessorImplementation() {
        String accessorImplementation = String
                .valueOf(getConfiguredProperty(Bpm4StrutsGlobals.SERVICE_ACCESSOR_PATTERN));
        return accessorImplementation.replaceAll("\\{0\\}", getPackageName()).replaceAll("\\{1\\}", getName());
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName() {
        String packageName = String.valueOf(getConfiguredProperty(Bpm4StrutsGlobals.SERVICE_PACKAGE_NAME_PATTERN));
        return packageName.replaceAll("\\{0\\}", super.getPackageName()) + '.' + this.getName();
    }
}
