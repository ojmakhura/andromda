package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;

import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.cartridges.ejb3.EJB3ScriptHelper;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacade
 */
public class EJB3WebServiceFacadeLogicImpl
    extends EJB3WebServiceFacadeLogic
{
    
    /**
     * The property which stores the pattern defining the web service interface name.
     */
    private static final String WEB_SERVICE_INTERFACE_NAME_PATTERN = "webServiceInterfaceNamePattern";
    
    /**
     * The property defining the default style to give the web services.
     */
    private static final String PROPERTY_DEFAULT_STYLE = "webServiceDefaultStyle";

    /**
     * Represents a "document" style.
     */
    private static final String STYLE_DOCUMENT = "document";

    /**
     * Represents a "rpc" style.
     */
    private static final String STYLE_RPC = "rpc";

    /**
     * The property defining the default style to give the web services.
     */
    private static final String PROPERTY_DEFAULT_USE = "webServiceDefaultUse";
    
    /**
     * Represents a "literal" use.
     */
    private static final String USE_LITERAL = "literal";

    /**
     * Represents an "encoded" use.
     */
    private static final String USE_ENCODED = "encoded";
    
    /**
     * Represents the default parameter encoding style
     */
    private static final String PROPERTY_DEFAULT_PARAMETER_STYLE = "webServiceDefaultParameterStyle";
    
    /**
     * Represents a "wrapped" parameter style.
     */
    private static final String PARAMETER_STYLE_WRAPPED = "wrapped";
    
    /**
     * Represents a "bare" parameter style.
     */
    private static final String PARAMETER_STYLE_BARE = "bare";
    
    /**
     * Represents the qualified name local part pattern
     */
    private static final String QNAME_LOCAL_PART_PATTERN = "webServiceQualifiedNameLocalPartPattern";
    
    /**
     * Determine if the namespace should be reversed
     */
    private static final String REVERSE_NAMESPACE = "webServiceReverseNamespace";
    
    /**
     * Retrieve the namespace pattern used to generate the namespace
     */
    private static final String NAMESPACE_PATTERN = "webServiceNamespacePattern";
    
    /**
     * 
     * @param metaObject
     * @param context
     */
    public EJB3WebServiceFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacade#getFullyQualifiedWebServiceInterfaceName()
     */
    protected java.lang.String handleGetFullyQualifiedWebServiceInterfaceName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getWebServiceInterfaceName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacade#getWebServiceInterfaceName()
     */
    protected java.lang.String handleGetWebServiceInterfaceName()
    {
        String webServiceInterfaceNamePattern = 
            String.valueOf(this.getConfiguredProperty(WEB_SERVICE_INTERFACE_NAME_PATTERN));

        return MessageFormat.format(
                webServiceInterfaceNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacade#getStyle()
     */
    protected java.lang.String handleGetStyle()
    {
        String style = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_WEBSERVICE_STYLE);
        if (StringUtils.isEmpty(style))
        {
            style = String.valueOf(this.getConfiguredProperty(PROPERTY_DEFAULT_STYLE));
        }
        return style;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacade#getUse()
     */
    protected java.lang.String handleGetUse()
    {
        String use = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_WEBSERVICE_USE);
        if (StringUtils.isEmpty(use))
        {
            use = String.valueOf(this.getConfiguredProperty(PROPERTY_DEFAULT_USE));
        }
        return use;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacade#isRpcStyle()
     */
    protected boolean handleIsRpcStyle()
    {
        return this.getStyle().equalsIgnoreCase(STYLE_RPC);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacade#isDocumentStyle()
     */
    protected boolean handleIsDocumentStyle()
    {
        return this.getStyle().equalsIgnoreCase(STYLE_DOCUMENT);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacade#isEncodedUse()
     */
    protected boolean handleIsEncodedUse()
    {
        return this.getStyle().equalsIgnoreCase(USE_ENCODED);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacade#isLiteralUse()
     */
    protected boolean handleIsLiteralUse()
    {
        return this.getStyle().equalsIgnoreCase(USE_LITERAL);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacadeLogic#handleIsWebServiceOperationsExist()
     */
    protected boolean handleIsWebServiceOperationsExist()
    {
        return CollectionUtils.find(
            this.getOperations(),
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    boolean isWebService = false;
                    final OperationFacade operation = (OperationFacade)object;
                    if (operation.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION))
                    {
                        isWebService = true;
                    }
                    return isWebService;
                }
            }) != null;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacadeLogic#handleGetParameterStyle()
     */
    protected String handleGetParameterStyle()
    {
        String parameterStyle = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_WEBSERVICE_PARAMETER_STYLE);
        if (StringUtils.isEmpty(parameterStyle))
        {
            parameterStyle = String.valueOf(this.getConfiguredProperty(PROPERTY_DEFAULT_PARAMETER_STYLE));
        }
        return parameterStyle;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacadeLogic#handleIsWrappedParameterStyle()
     */
    protected boolean handleIsWrappedParameterStyle()
    {
        return this.getParameterStyle().equalsIgnoreCase(PARAMETER_STYLE_WRAPPED);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacadeLogic#handleIsBareParameterStyle()
     */
    protected boolean handleIsBareParameterStyle()
    {
        return this.getParameterStyle().equalsIgnoreCase(PARAMETER_STYLE_BARE);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacadeLogic#handleGetQName()
     */
    protected String handleGetQName()
    {
        String qnameLocalPartPattern = String.valueOf(this.getConfiguredProperty(QNAME_LOCAL_PART_PATTERN));
        return MessageFormat.format(
                qnameLocalPartPattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceFacadeLogic#handleGetNamespace()
     */
    protected String handleGetNamespace()
    {
        String packageName = this.getPackageName();
        if (this.isReverseNamespace())
        {
            packageName = EJB3ScriptHelper.reversePackage(packageName);
        }
        String namespacePattern = String.valueOf(this.getConfiguredProperty(NAMESPACE_PATTERN));
        return MessageFormat.format(
            namespacePattern,
            new Object[] {StringUtils.trimToEmpty(packageName), StringUtils.trimToEmpty(this.getQName())});
    }
    
    /**
     * Gets whether or not <code>reverseNamespace</code> is true/false for this type.
     *
     * @return boolean true/false
     */
    protected boolean isReverseNamespace()
    {
        return Boolean.valueOf(String.valueOf(this.getConfiguredProperty(REVERSE_NAMESPACE))).booleanValue();
    }
}