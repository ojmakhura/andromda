package org.andromda.cartridges.spring.metafacades;

import org.andromda.metafacades.uml.UMLProfile;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.spring.metafacades.SpringServiceOperation.
 * 
 * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation
 */
public class SpringServiceOperationLogicImpl
    extends SpringServiceOperationLogic
    implements
    org.andromda.cartridges.spring.metafacades.SpringServiceOperation
{
    // ---------------- constructor -------------------------------

    public SpringServiceOperationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#isWebserviceExposed()
     */
    public boolean handleIsWebserviceExposed()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION);
    }

}
