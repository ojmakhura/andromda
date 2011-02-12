package org.andromda.cartridges.xmlschema.metafacades;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEnd.
 *
 * @see org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEnd
 */
public class XSDAssociationEndLogicImpl
        extends XSDAssociationEndLogic
{
    private static final long serialVersionUID = 34L;
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public XSDAssociationEndLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEndLogic#handleGetMaxOccurs()
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEnd#getMaxOccurs()
     */
    protected String handleGetMaxOccurs()
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
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEndLogic#handleGetMinOccurs()
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEnd#getMinOccurs()
     */
    protected String handleGetMinOccurs()
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
    
    /**
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEndLogic#handleIsOwnerSchemaType()
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAssociationEnd#isOwnerSchemaType()
     */
    protected boolean handleIsOwnerSchemaType()
    {
        final Object owner = this.getType();
        return owner instanceof XSDComplexType || owner instanceof XSDEnumerationType;
    }

}