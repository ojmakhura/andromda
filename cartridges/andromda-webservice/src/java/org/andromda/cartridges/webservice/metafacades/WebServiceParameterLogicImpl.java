package org.andromda.cartridges.webservice.metafacades;

import org.andromda.metafacades.uml.UMLProfile;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WebServiceParameter.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WebServiceParameter
 */
public class WebServiceParameterLogicImpl
       extends WebServiceParameterLogic
       implements org.andromda.cartridges.webservice.metafacades.WebServiceParameter
{
    // ---------------- constructor -------------------------------

    public WebServiceParameterLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceParameter#isNillable()
     */
    protected boolean handleIsNillable()
    {
        return !this.isRequired();
    }
}
