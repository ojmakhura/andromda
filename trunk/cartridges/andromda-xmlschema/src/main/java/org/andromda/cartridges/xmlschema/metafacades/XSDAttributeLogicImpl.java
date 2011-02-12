package org.andromda.cartridges.xmlschema.metafacades;

import org.andromda.cartridges.xmlschema.XmlSchemaProfile;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.xmlschema.metafacades.XSDAttribute.
 *
 * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute
 */
public class XSDAttributeLogicImpl
        extends XSDAttributeLogic
{
    private static final long serialVersionUID = 34L;
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public XSDAttributeLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttributeLogic#handleGetMaxOccurs()
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute#getMaxOccurs()
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
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttributeLogic#handleGetMinOccurs()
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute#getMinOccurs()
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
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttributeLogic#handleIsXsdAttribute()
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute#isXsdAttribute()
     */
    protected boolean handleIsXsdAttribute()
    {
        return this.hasStereotype(XmlSchemaProfile.STEREOTYPE_XML_ATTRIBUTE);
    }

    /**
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute#isXsdElement()
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttributeLogic#handleIsXsdElement()
     */
    protected boolean handleIsXsdElement()
    {
        return !this.isXsdAttribute();
    }

    /**
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttributeLogic#handleIsOwnerSchemaType()
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute#isOwnerSchemaType()
     */
    protected boolean handleIsOwnerSchemaType()
    {
        final Object owner = this.getOwner();
        return owner instanceof XSDComplexType || owner instanceof XSDEnumerationType;
    }

}