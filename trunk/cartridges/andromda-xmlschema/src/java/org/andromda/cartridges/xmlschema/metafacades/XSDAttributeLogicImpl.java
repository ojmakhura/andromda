package org.andromda.cartridges.xmlschema.metafacades;

import org.andromda.cartridges.xmlschema.XmlSchemaProfile;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.xmlschema.metafacades.XSDAttribute.
 * 
 * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute
 */
public class XSDAttributeLogicImpl
    extends XSDAttributeLogic
    implements org.andromda.cartridges.xmlschema.metafacades.XSDAttribute
{
    // ---------------- constructor -------------------------------

    public XSDAttributeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute#getMaxOccurs()
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
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute#getMinOccurs()
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

    /**
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute#isXsdAttribute()
     */
    public boolean handleIsXsdAttribute()
    {
        return this.hasStereotype(XmlSchemaProfile.STEREOTYPE_XML_ATTRIBUTE);
    }

    /**
     * @see org.andromda.cartridges.xmlschema.metafacades.XSDAttribute#isXsdElement()
     */
    public boolean handleIsXsdElement()
    {
        return !this.isXsdAttribute();
    }

}