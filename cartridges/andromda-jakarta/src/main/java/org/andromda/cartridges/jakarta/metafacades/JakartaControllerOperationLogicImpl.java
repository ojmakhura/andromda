package org.andromda.cartridges.jakarta.metafacades;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import org.andromda.cartridges.jakarta.JakartaGlobals;
import org.andromda.cartridges.jakarta.JakartaUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation.
 *
 * @see org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation
 */
public class JakartaControllerOperationLogicImpl
    extends JakartaControllerOperationLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JakartaControllerOperationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return formName
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation#getFormName()
     */
    protected String handleGetFormName()
    {
        final String pattern = Objects.toString(this.getConfiguredProperty(JakartaGlobals.FORM_PATTERN), "");
        return pattern.replaceFirst("\\{0\\}", StringUtils.capitalize(this.getName()));
    }

    /**
     * @return fullyQualifiedFormName
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation#getFullyQualifiedFormName()
     */
    protected String handleGetFullyQualifiedFormName()
    {
        final StringBuilder fullyQualifiedName = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append(packageName + '.');
        }
        return fullyQualifiedName.append(StringUtils.capitalize(this.getFormName())).toString();
    }

    /**
     * @return getFullyQualifiedFormName().replace('.', '/')
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation#getFullyQualifiedFormPath()
     */
    protected String handleGetFullyQualifiedFormPath()
    {
        return this.getFullyQualifiedFormName().replace('.', '/');
    }

    /**
     * @return formCall
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation#getFormCall()
     */
    protected String handleGetFormCall()
    {
        final StringBuilder call = new StringBuilder();
        call.append(this.getName());
        call.append("(");
        if (!this.getFormFields().isEmpty())
        {
            call.append("form");
        }
        call.append(")");
        return call.toString();
    }

    /**
     * @return getFormSignature(false)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation#getImplementationFormSignature()
     */
    protected String handleGetImplementationFormSignature()
    {
        return this.getFormSignature(false);
    }

    /**
     * @return getFormSignature(true)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation#getFormSignature()
     */
    protected String handleGetFormSignature()
    {
        return this.getFormSignature(true);
    }

    /**
     * Constructs the signature that takes the form for this operation.
     *
     * @param isAbstract whether or not the signature is abstract.
     * @return the appropriate signature.
     */
    private String getFormSignature(boolean isAbstract)
    {
        final StringBuilder signature = new StringBuilder();
        signature.append(this.getVisibility() + ' ');
        if (isAbstract)
        {
            signature.append("abstract ");
        }
        final ModelElementFacade returnType = this.getReturnType();
        signature.append(returnType != null ? returnType.getFullyQualifiedName() : null);
        signature.append(" " + this.getName() + "(");
        if (!this.getFormFields().isEmpty())
        {
            signature.append(this.getFormName() + " form");
        }
        signature.append(")");
        return signature.toString();
    }

    /**
     * The property defining the default style to give the web services.
     */
    private static final String PROPERTY_DEFAULT_PARAMETER_STYLE = "defaultParameterStyle";
    private static final String DEFAULT = "default";
    private static final String EMPTY_STRING = "";
    private static final String BOOLEAN_FALSE = "false";
    
    private static final String SLASH = "/";
    private static final String QUOTE = "\"";
    private static final String LBRACKET = "{";
    private static final String RBRACKET = "}";

    /**
     * Returns map of ProviderMediaType enumeration values to Provider/Consumer text
     */
    private static Map<String, String> getMediaTranslation()
    {
        final Map<String, String> mediaMap = new HashMap<String, String>();
        mediaMap.put("default", QUOTE + "application/xml" + QUOTE);
        mediaMap.put("ApplicationAtom", QUOTE + "application/atom+xml" + QUOTE);
        mediaMap.put("ApplicationAtomEntry", QUOTE + "application/atom+xml;type=entry" + QUOTE);
        mediaMap.put("ApplicationAtomXML", QUOTE + "application/atom+xml" + QUOTE);
        mediaMap.put("ApplicationFastinfoset", QUOTE + "application/fastinfoset" + QUOTE);
        mediaMap.put("ApplicationFormURLEncoded", QUOTE + "application/x-www-form-urlencoded" + QUOTE);
        mediaMap.put("ApplicationJSON", QUOTE + "application/json" + QUOTE);
        mediaMap.put("ApplicationOctetStream", QUOTE + "application/octet-stream" + QUOTE);
        mediaMap.put("ApplicationSvgXML", QUOTE + "application/svg+xml" + QUOTE);
        mediaMap.put("ApplicationXhtmlXML", QUOTE + "application/xhtml+xml" + QUOTE);
        mediaMap.put("ApplicationXML", QUOTE + "application/xml" + QUOTE);
        mediaMap.put("ApplicationXMLAll", QUOTE + "application/*+xml" + QUOTE);
        mediaMap.put("ApplicationYaml", QUOTE + "application/yaml" + QUOTE);
        mediaMap.put("MultipartAlternative", QUOTE + "multipart/alternative" + QUOTE);
        mediaMap.put("MultipartFixed", QUOTE + "multipart/fixed" + QUOTE);
        mediaMap.put("MultipartFormData", QUOTE + "multipart/form-data" + QUOTE);
        mediaMap.put("MultipartMixed", QUOTE + "multipart/mixed" + QUOTE);
        mediaMap.put("MultipartRelated", QUOTE + "multipart/related" + QUOTE);
        mediaMap.put("TextPlain", QUOTE + "text/plain" + QUOTE);
        mediaMap.put("TextXML", QUOTE + "text/xml" + QUOTE);
        mediaMap.put("TextXYaml", QUOTE + "text/xyaml" + QUOTE);
        mediaMap.put("TextYaml", QUOTE + "text/xml" + QUOTE);
        mediaMap.put("Wildcard", QUOTE + "*/*" + QUOTE);
        return mediaMap;
    }

    /**
     * Translates Media Enumeration Type into string for produces/consumes annotation
     * @param input ProviderMediaType Enumeration value to be translated for Annotation
     * @return getMediaTranslation().get(input)
     */
    public static String translateMediaType(String input)
    {
        String output = null;
        if (StringUtils.isBlank(input) || input.equals(DEFAULT) || !getMediaTranslation().containsKey(input))
        {
            output = getMediaTranslation().get(DEFAULT);
        }
        else
        {
            output = getMediaTranslation().get(input);
        }
        return output;
    }

    @Override
    protected String handleGetRestTestPath() {
        // String path = (String)this.findTaggedValue(JakartaGlobals.REST_PATH);
        // StringBuilder pathBuffer = new StringBuilder();
        // JakartaControllerLogic service = (JakartaControllerLogic)this.getService();
        // String servicePath = service.getRestPath();
        // JakartaUtils wsutils = new JakartaUtils();
        // if (!this.isRest() || StringUtils.isBlank(path) || path.equals(DEFAULT))
        // {
        //     pathBuffer.append(SLASH).append(this.getName().toLowerCase()).append(SLASH);
        //     Iterator<ParameterFacade> parameters = this.getArguments().iterator();
        //     while (parameters.hasNext())
        //     {
        //         ParameterFacade param = parameters.next();
        //         if (JakartaUtils.isSimpleType(param))
        //         {
        //             String paramValue = wsutils.createConstructor(param);
        //             // Only use the value if constructor returns new Class()
        //             if (paramValue.indexOf('(') > 0)
        //             {
        //                 paramValue = paramValue.substring(paramValue.indexOf('(')+1, paramValue.indexOf(')'));
        //             }
        //             pathBuffer.append(param.getName()).append(SLASH).append(paramValue).append(SLASH);
        //         }
        //     }
        //     path = pathBuffer.toString();
        // }
        // else
        // {
        //     if (StringUtils.isBlank(path))
        //     {
        //         path = EMPTY_STRING;
        //     }
        //     // StringBuffer doesn't have replace(String, String) API
        //     path = pathBuffer.append(path).toString();
        //     Iterator<ParameterFacade> parameters = this.getArguments().iterator();
        //     while (parameters.hasNext())
        //     {
        //         ParameterFacade param = parameters.next();
        //         if (WebServiceUtils.isSimpleType(param))
        //         {
        //             String paramValue = wsutils.createConstructor(param).replace("\"", "");
        //             if (paramValue.indexOf('(') > 0)
        //             {
        //                 paramValue = paramValue.substring(paramValue.indexOf('(')+1, paramValue.indexOf(')'));
        //             }
        //             path = StringUtils.replace(path, LBRACKET + param.getName() + RBRACKET, paramValue);
        //         }
        //     }
        // }
        // path = servicePath + path;
        // path = path.replaceAll("\"", "");
        // path = path.replaceAll("//", "/");
        // return path;
        return null;
    }

    @Override
    protected String handleGetRestResponseStatus() {
        String responseStatus = (String) this.findTaggedValue(JakartaGlobals.REST_RESPONSE_STATUS);
        if(responseStatus == null) {
            return "";
        }
        return "@jakarta.ws.rs.core.Response.Status(code = jakarta.ws.rs.core.Response.Status." + responseStatus + ")";
    }

    @Override
    protected boolean handleIsRestAtom() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsRest() {
        String rest = (String)this.findTaggedValue(JakartaGlobals.REST);
        if (StringUtils.isBlank(rest) || rest.equals(DEFAULT))
        {
            rest = (String)this.getOwner().findTaggedValue(JakartaGlobals.REST);
            if (StringUtils.isBlank(rest) || rest.equals(DEFAULT))
            {
                rest = BOOLEAN_FALSE;
            }
        }
        return Boolean.valueOf(rest);
    }

    @Override
    protected String handleGetRolesAllowed() {
        String rolesAllowed = (String)this.findTaggedValue(JakartaGlobals.REST_ROLES_ALLOWED);
        if (!this.isRest() || StringUtils.isBlank(rolesAllowed) || rolesAllowed.equals(DEFAULT))
        {
            rolesAllowed = EMPTY_STRING;
        }
        return rolesAllowed;
    }

    @Override
    protected int handleGetRestSuspend() {
        String suspend = (String)this.findTaggedValue(JakartaGlobals.REST_SUSPEND);
        if (!this.isRest() || StringUtils.isBlank(suspend) || suspend.equals(DEFAULT) || !StringUtils.isNumeric(suspend))
        {
            return 0;
        }
        return Integer.parseInt(suspend);
    }

    @Override
    protected String handleGetRestPartType() {
        
        String partType = (String)this.findTaggedValue(JakartaGlobals.REST_PART_TYPE);
        if (!this.isRest() || StringUtils.isBlank(partType) || partType.equals(DEFAULT))
        {
            partType = EMPTY_STRING;
        }
        return partType;
    }

    @Override
    protected String handleGetRestProvider() {
        String provider = (String)this.findTaggedValue(JakartaGlobals.REST_PROVIDER);
        if (!this.isRest() || StringUtils.isBlank(provider) || provider.equals(DEFAULT))
        {
            provider = EMPTY_STRING;
        }
        return provider;
    }

    @Override
    protected String handleGetRestProduces() {
        String provider = (String)this.findTaggedValue(JakartaGlobals.REST_PROVIDER);
        if (!this.isRest() || StringUtils.isBlank(provider) || provider.equals(DEFAULT))
        {
            provider = EMPTY_STRING;
        }
        return provider;
    }

    private static final String POST = "@jakarta.ws.rs.POST";
    private static final String AT = "@jakarta.ws.rs.";

    @Override
    protected String handleGetRestConsumes() {

        String consumes = (String)this.findTaggedValue(JakartaGlobals.REST_CONSUMES);
        if (!this.isRest() || StringUtils.isBlank(consumes) || consumes.equals(DEFAULT))
        {
            consumes = EMPTY_STRING;
        }
        else
        {
            consumes = translateMediaType(consumes);
        }
        return consumes;
    }

    @Override
    protected boolean handleIsRestEncoded() {
        String restEncoded = (String)this.findTaggedValue(JakartaGlobals.REST_ENCODED);
        if (!this.isRest() || StringUtils.isBlank(restEncoded) || restEncoded.equals(DEFAULT))
        {
            restEncoded = BOOLEAN_FALSE;
        }
        return Boolean.valueOf(restEncoded);
    }

    @Override
    protected String handleGetRestCacheType() {
        String cacheType = (String)this.findTaggedValue(JakartaGlobals.CACHE_TYPE);
        if (!this.isRest() || StringUtils.isBlank(cacheType) || cacheType.equals(DEFAULT))
        {
            cacheType = EMPTY_STRING;
        }
        return cacheType;
    }

    @Override
    protected String handleGetRestRequestType() {
        String requestType = (String)this.findTaggedValue(JakartaGlobals.REST_REQUEST_TYPE);
        if (!this.isRest() || StringUtils.isBlank(requestType) || requestType.equals(DEFAULT))
        {
            requestType = POST;
        }
        else if (!requestType.startsWith(AT))
        {
            requestType = AT + requestType;
        }
        return requestType;
    }

    @Override
    protected String handleGetRestPath() {
        String path = (String)this.findTaggedValue(JakartaGlobals.REST_PATH);
        StringBuilder pathBuffer = new StringBuilder();
        if (!this.isRest() || StringUtils.isBlank(path) || path.equals(DEFAULT))
        {
            path = this.getName().toLowerCase();
        }
        
        if(path.startsWith("/")) {
            path = path.substring(1);
        }
        
        if(path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        if(path.length() > 0) {
            pathBuffer.append(path);
        }

        String type = this.getRestRequestType().toLowerCase();

        if(type.contains("get") || type.contains("delete")) {
            for(ParameterFacade param : this.getArguments()) {
                    
                String paramName = param.getName();
                if (!JakartaUtils.isSimpleType(param)) {
                    if(param instanceof JakartaParameter) {
                        
                        JakartaParameter p = (JakartaParameter)param;                        
                        paramName = p.getRestPathParam();
                    }
                }
                if(pathBuffer.length() > 0) {
                    pathBuffer.append(SLASH);
                }

                //pathBuffer.append(paramName).append(SLASH).append(LBRACKET).append(paramName).append(RBRACKET);
                pathBuffer.append(LBRACKET).append(paramName).append(RBRACKET);
            }
        }

        if(pathBuffer.length() > 0) {
            pathBuffer.insert(0, QUOTE);
            pathBuffer.insert(1, SLASH);
            pathBuffer.append(QUOTE);
        }

        return pathBuffer.toString();
    }
}
