package org.andromda.cartridges.webservice.metafacades;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WSDLTypeAttribute.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAttribute
 */
public class WSDLTypeAttributeLogicImpl
        extends WSDLTypeAttributeLogic
{
    // ---------------- constructor -------------------------------

    public WSDLTypeAttributeLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAttribute#isNillable()
     */
    protected boolean handleIsNillable()
    {
        return !this.isRequired();
    }

}
