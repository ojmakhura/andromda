package org.andromda.cartridges.webservice.metafacades;

import org.andromda.metafacades.uml.UMLProfile;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.webservice.metafacades.WebServiceOperation.
 * 
 * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation
 */
public class WebServiceOperationLogicImpl
    extends WebServiceOperationLogic
    implements
    org.andromda.cartridges.webservice.metafacades.WebServiceOperation
{
    // ---------------- constructor -------------------------------

    public WebServiceOperationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceOperation#isExposed()
     */
    protected boolean handleIsExposed()
    {
        return this.getOwner().hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE)
            || this.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION);
    }
}
