package org.andromda.cartridges.xmlschema.metafacades;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEnd.
 * 
 * @see org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEnd
 */
public class XSDAssociationEndLogicImpl
    extends XSDAssociationEndLogic
    implements org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEnd
{
    // ---------------- constructor -------------------------------

    public XSDAssociationEndLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class XSDAssociationEnd ...
    /**
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEnd#getMaxOccurs()
     */
    public java.lang.String handleGetMaxOccurs()
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
    public java.lang.String handleGetMinOccurs()
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