package org.andromda.cartridges.webservice.metafacades;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WSDLTypeAttribute.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAttribute
 */
public class WSDLTypeAttributeLogicImpl
       extends WSDLTypeAttributeLogic
       implements org.andromda.cartridges.webservice.metafacades.WSDLTypeAttribute
{
    // ---------------- constructor -------------------------------

    public WSDLTypeAttributeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAttribute#isNillable()
     */
    public boolean handleIsNillable() 
    {
        return !this.isRequired();
    }
    
}
