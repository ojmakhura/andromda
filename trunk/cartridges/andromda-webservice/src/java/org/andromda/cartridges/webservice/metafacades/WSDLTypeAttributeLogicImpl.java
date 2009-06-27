package org.andromda.cartridges.webservice.metafacades;

import org.andromda.cartridges.webservice.WebServiceGlobals;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WSDLTypeAttribute.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAttribute
 * @author Bob Fields
 */
public class WSDLTypeAttributeLogicImpl
        extends WSDLTypeAttributeLogic
{
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public WSDLTypeAttributeLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return !this.isRequired()
     * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAttribute#isNillable()
     */
    protected boolean handleIsNillable()
    {
        return !this.isRequired();
    }

    /**
     * The property defining the web service XML Adapter for Jaxb for the @XmlAdapter annotation.
     */
    private static final String PROPERTY_XML_ADAPTER = "schemaValidation";

    @Override
    protected String handleGetXmlAdapter()
    {
        String adapter = (String)this.findTaggedValue(WebServiceGlobals.XML_ADAPTER);
        if (StringUtils.isEmpty(adapter))
        {
            adapter = String.valueOf(this.getConfiguredProperty(PROPERTY_XML_ADAPTER));
        }
        return adapter;
    }

    /**
     * The property defining the web service XML Adapter for Jaxb.
     */
    private static final String PROPERTY_XML_TYPE = "schemaValidation";

    @Override
    protected String handleGetXmlSchemaType()
    {
        String mode = (String)this.findTaggedValue(WebServiceGlobals.XML_TYPE);
        if (StringUtils.isEmpty(mode))
        {
            mode = String.valueOf(this.getConfiguredProperty(PROPERTY_XML_TYPE));
        }
        if (StringUtils.isEmpty(mode))
        {
            //mode = "false";
        }
        return mode;
    }

    /**
     * The property defining the web service XML Adapter for Jaxb.
     */
    private static final String USE_ATTRIBUTES = "useAttributes";

    @Override
    protected boolean handleIsAttribute()
    {
        boolean isAttribute = this.hasStereotype(WebServiceGlobals.STEREOTYPE_XML_ATTRIBUTE);
        if (!isAttribute)
        {
            String attributes = String.valueOf(this.getConfiguredProperty(USE_ATTRIBUTES));
            if (StringUtils.isEmpty(attributes))
            {
                attributes = "true";
            }
            isAttribute = Boolean.parseBoolean(attributes);
        }
        return isAttribute;
    }

    @Override
    protected boolean handleIsElement()
    {
        boolean isAttribute = this.hasStereotype(WebServiceGlobals.STEREOTYPE_XML_ELEMENT);
        if (!isAttribute)
        {
            String attributes = String.valueOf(this.getConfiguredProperty(USE_ATTRIBUTES));
            if (StringUtils.isEmpty(attributes))
            {
                attributes = "true";
            }
            isAttribute = Boolean.parseBoolean(attributes);
        }
        return !isAttribute;
    }

}
