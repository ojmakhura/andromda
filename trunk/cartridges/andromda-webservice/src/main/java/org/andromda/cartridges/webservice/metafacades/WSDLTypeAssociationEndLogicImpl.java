package org.andromda.cartridges.webservice.metafacades;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WSDLTypeAssociationEnd.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAssociationEnd
 * @author Bob Fields
 */
public class WSDLTypeAssociationEndLogicImpl
        extends WSDLTypeAssociationEndLogic
{
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public WSDLTypeAssociationEndLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return !this.isRequired()
     * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAssociationEnd#isNillable()
     */
    protected boolean handleIsNillable()
    {
        return !this.isRequired();
    }

}
