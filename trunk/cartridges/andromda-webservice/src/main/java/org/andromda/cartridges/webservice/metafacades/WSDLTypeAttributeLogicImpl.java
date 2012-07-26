package org.andromda.cartridges.webservice.metafacades;

import java.util.Collection;
import org.andromda.cartridges.webservice.WebServiceGlobals;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.ModelValidationMessage;
import org.andromda.translation.ocl.validation.OCLIntrospector;
import org.andromda.utils.StringUtilsHelper;
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
    private static final long serialVersionUID = 34L;
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

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAttributeLogic#handleGetXmlAdapter()
     */
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

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAttributeLogic#handleGetXmlSchemaType()
     */
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

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAttributeLogic#handleIsAttribute()
     */
    @Override
    protected boolean handleIsAttribute()
    {
        boolean isAttribute = this.hasStereotype(WebServiceGlobals.STEREOTYPE_XML_ATTRIBUTE);
        boolean isElement = this.hasStereotype(WebServiceGlobals.STEREOTYPE_XML_ELEMENT);
        if (isElement)
        {
            if (isAttribute)
            {
                logger.error("Attribute has both XmlAttribute and XmlElement stereotypes, it cannot be both! " + this.getFullyQualifiedName());
                // leave attribute = true anyway, because it has the stereotype
            }
            // else: Attribute = false because it is an element, not an attribute
        }
        else if (!isAttribute)
        {
            String attributes = String.valueOf(this.getConfiguredProperty(USE_ATTRIBUTES));
            if (StringUtils.isEmpty(attributes))
            {
                attributes = "true";
            }
            isAttribute = Boolean.parseBoolean(attributes);
        }
        // else: Attribute = true because of stereotype
        return isAttribute;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAttributeLogic#handleIsElement()
     */
    @Override
    protected boolean handleIsElement()
    {
        boolean isAttribute = this.hasStereotype(WebServiceGlobals.STEREOTYPE_XML_ELEMENT);
        boolean isElement = this.hasStereotype(WebServiceGlobals.STEREOTYPE_XML_ELEMENT);
        if (isElement)
        {
            if (isAttribute)
            {
                logger.error("Attribute has both XmlAttribute and XmlElement stereotypes, it cannot be both! " + this.getFullyQualifiedName());
                // leave element = true anyway, because it has the stereotype
            }
            // else: Element = true because it is an element, not an attribute
        }
        else if (!isAttribute)
        {
            String attributes = String.valueOf(this.getConfiguredProperty(USE_ATTRIBUTES));
            if (StringUtils.isEmpty(attributes))
            {
                attributes = "true";
            }
            // useAttributes = true by default
            isElement = !Boolean.parseBoolean(attributes);
        }
        // else: Attribute = true because of stereotype
        return isElement;
    }

    /**
     * <p><b>Constraint:</b> org::andromda::cartridges::webservice::metafacades::WSDLTypeAttribute::attribute must start with a lowercase letter</p>
     * <p><b>Error:</b> Attribute name must start with a lowercase letter.</p>
     * @param validationMessages Collection<ModelValidationMessage>
     * @see org.andromda.core.metafacade.MetafacadeBase#validateInvariants(Collection validationMessages)
     */
    @Override
    public void validateInvariants(Collection<ModelValidationMessage> validationMessages)
    {
        super.validateInvariants(validationMessages);
        try
        {
            final Object contextElement = this.THIS();
            final String name = (String)OCLIntrospector.invoke(contextElement,"name");
            final boolean isStatic = this.isStatic() && this.isLeaf();
            if (!isStatic && name != null && name.length()>0 && !StringUtilsHelper.startsWithLowercaseLetter(name))
            {
                validationMessages.add(
                    new ModelValidationMessage(
                        (MetafacadeBase)contextElement ,
                        "org::andromda::cartridges::webservice::metafacades::WSDLTypeAttribute::attribute must start with a lowercase letter",
                        "Attribute name must start with a lowercase letter."));
            }
        }
        catch (Throwable th)
        {
            Throwable cause = th.getCause();
            int depth = 0; // Some throwables have infinite recursion
            while (cause != null && depth < 7)
            {
                th = cause;
                depth++;
            }
            logger.error("Error validating constraint 'org::andromda::cartridges::webservice::WSDLTypeAttribute::attribute must start with a lowercase letter' ON "
                + this.THIS().toString() + ": " + th.getMessage(), th);
        }
    }
}
