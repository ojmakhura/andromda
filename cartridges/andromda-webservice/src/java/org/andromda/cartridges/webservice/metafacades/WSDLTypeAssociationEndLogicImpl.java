package org.andromda.cartridges.webservice.metafacades;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WSDLTypeAssociationEnd.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAssociationEnd
 */
public class WSDLTypeAssociationEndLogicImpl
        extends WSDLTypeAssociationEndLogic
{
    // ---------------- constructor -------------------------------

    public WSDLTypeAssociationEndLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAssociationEnd#isNillable()
     */
    protected boolean handleIsNillable()
    {
        return !this.isRequired();
    }

}
