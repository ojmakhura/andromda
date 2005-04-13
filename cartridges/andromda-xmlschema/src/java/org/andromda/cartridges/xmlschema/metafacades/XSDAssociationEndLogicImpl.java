package org.andromda.cartridges.xmlschema.metafacades;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEnd.
 *
 * @see org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEnd
 */
public class XSDAssociationEndLogicImpl
        extends XSDAssociationEndLogic
{
    // ---------------- constructor -------------------------------

    public XSDAssociationEndLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEnd#getMaxOccurs()
     */
    protected java.lang.String handleGetMaxOccurs()
    {
        String maxOccurs = null;
        boolean isMany = this.isMany();
        if (isMany)
        {
            maxOccurs = "unbounded";
        }
        else
        {
            maxOccurs = "1";
        }
        return maxOccurs;
    }

    /**
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEnd#getMinOccurs()
     */
    protected java.lang.String handleGetMinOccurs()
    {
        String minOccurs = null;
        boolean isRequired = this.isRequired();
        if (isRequired)
        {
            minOccurs = "1";
        }
        else
        {
            minOccurs = "0";
        }
        return minOccurs;
    }

}