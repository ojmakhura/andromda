package org.andromda.cartridges.bpm4struts.metafacades;




/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsBackendService.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsBackendService
 */
public class StrutsBackendServiceLogicImpl
       extends StrutsBackendServiceLogic
       implements org.andromda.cartridges.bpm4struts.metafacades.StrutsBackendService
{
    // ---------------- constructor -------------------------------

    public StrutsBackendServiceLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsBackendService#getAccessorImplementation()
     */
    public java.lang.String handleGetAccessorImplementation() 
    {
        String accessorImplementation = String.valueOf(getConfiguredProperty("serviceAccessorPattern"));
        return accessorImplementation.replaceAll("\\{0\\}", getPackageName()).replaceAll("\\{1\\}", getName());
    }
    
}
