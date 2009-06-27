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
    // ---------------- constructor -------------------------------

    public XSDAttributeLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute#getMaxOccurs()
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
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute#getMinOccurs()
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

    /**
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute#isXsdAttribute()
     */
    protected boolean handleIsXsdAttribute()
    {
        return this.hasStereotype(XmlSchemaProfile.STEREOTYPE_XML_ATTRIBUTE);
    }

    /**
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute#isXsdElement()
     */
    protected boolean handleIsXsdElement()
    {
        return !this.isXsdAttribute();
    }

    /**
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute#isOwnerSchemaType()
     */
    protected boolean handleIsOwnerSchemaType()
    {
        final Object owner = this.getOwner();
        return owner instanceof XSDComplexType || owner instanceof XSDEnumerationType;
    }

}