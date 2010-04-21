package org.andromda.cartridges.jsf.portlet.myfaces.tomahawk;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.andromda.cartridges.jsf.portlet.myfaces.tomahawk.support.ExtensionsResponseWrapper;
import org.andromda.cartridges.jsf.portlet.myfaces.tomahawk.support.HttpServletRequestWrapper;
import org.andromda.cartridges.jsf.portlet.myfaces.tomahawk.support.HttpServletResponseWrapper;
import org.andromda.cartridges.jsf.portlet.myfaces.tomahawk.support.MultipartPortletRequestWrapper;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.portals.bridges.portletfilter.PortletFilter;
import org.apache.portals.bridges.portletfilter.PortletFilterChain;
import org.apache.portals.bridges.portletfilter.PortletFilterConfig;

/**
 * This portlet filter supports Tomahawk's extended components, such as
 * inputHtml and fileUpload.
 *
 * @author <a href="mailto:shinsuke@yahoo.co.jp">Shinsuke Sugaya</a>
 * @author Chad Brandon
 */
public class ExtensionsPortletFilter
    implements PortletFilter
{
    private static final Log log = LogFactory.getLog(ExtensionsPortletFilter.class);

    private static final String UPLOAD_REPOSITORY_PATH = "uploadRepositoryPath";

    private static final String UPLOAD_THRESHOLD_SIZE = "uploadThresholdSize";

    private static final String UPLOAD_MAX_FILE_SIZE = "uploadMaxFileSize";

    private static final String MULTIPART_ENCODING = "multipartEncoding";

    private int uploadMaxFileSize = 100 * 1024 * 1024; // 10 MB

    private int uploadThresholdSize = 1 * 1024 * 1024; // 1 MB

    private String uploadRepositoryPath = null; // standard temp directory

    private String multipartEncoding = null;

    private PortletConfig portletConfig;

    /**
     * Called by init method of MyFacesFilterPortlet to initialize this portlet
     * filter.
     *
     * @param filterConfig
     * @throws PortletException
     */
    public void init(PortletFilterConfig filterConfig) throws PortletException
    {
        if (log.isDebugEnabled())
            log.debug("Initializing ExtensionsPortletFilter.");

        setPortletConfig(filterConfig.getPortletConfig());

        // for inputFileUpload
        String param = filterConfig.getInitParameter(UPLOAD_MAX_FILE_SIZE);

        uploadMaxFileSize = resolveSize(param, uploadMaxFileSize);

        param = filterConfig.getInitParameter(UPLOAD_THRESHOLD_SIZE);

        uploadThresholdSize = resolveSize(param, uploadThresholdSize);

        uploadRepositoryPath = filterConfig.getInitParameter(UPLOAD_REPOSITORY_PATH);

        multipartEncoding = filterConfig.getInitParameter(MULTIPART_ENCODING);

        if (log.isDebugEnabled())
        {
            log.debug("uploadMaxFileSize=" + uploadMaxFileSize);
            log.debug("uploadThresholdSize=" + uploadThresholdSize);
            log.debug("uploadRepositoryPath=" + uploadRepositoryPath);
            log.debug("multipartEncoding=" + multipartEncoding);
        }
    }

    /**
     * Called by render method of MyFacesFilterPortlet to put tags, such as
     * &lt;style&gt;, into &lt;head&gt;.
     *
     * @param request
     * @param response
     * @param chain PortletFilterChain instance
     * @throws PortletException
     */
    public void renderFilter(
        RenderRequest request,
        RenderResponse response,
        PortletFilterChain chain) throws PortletException, IOException
    {
        if (log.isDebugEnabled())
        {
            log.debug("called renderFilter.");
            log.debug("RenderRequest=" + request.getClass().getName());
            log.debug("RenderResponse=" + response.getClass().getName());
        }

        final HttpServletRequestWrapper extendedRequest = new HttpServletRequestWrapper(
            request,
            getPortletConfig().getPortletContext());

        // Serve resources
        AddResource addResource = null;
        try
        {
            addResource = AddResourceFactory.getInstance(extendedRequest);
        }
        catch (Throwable throwable)
        {
            log.error(throwable);
            throw new PortletException(throwable);
        }

        try
        {
            addResource.responseStarted();

            if (addResource.requiresBuffer())
            {
                final HttpServletResponseWrapper servletResponse = new HttpServletResponseWrapper(response);
                final ExtensionsResponseWrapper extendedResponse = new ExtensionsResponseWrapper(
                    servletResponse,
                    response);

                // call next rednerFilter
                chain.renderFilter(request, extendedResponse);

                extendedResponse.finishResponse();

                // only parse HTML responses
                if (extendedResponse.getContentType() != null
                    && isValidContentType(extendedResponse.getContentType()))
                {
                    addResource.parseResponse(
                        extendedRequest,
                        extendedResponse.toString(),
                        servletResponse);

                    addResource.writeMyFacesJavascriptBeforeBodyEnd(
                        extendedRequest,
                        servletResponse);

                    if (!addResource.hasHeaderBeginInfos())
                    {
                        // writes the response if no header info is needed
                        addResource.writeResponse(extendedRequest, servletResponse);
                        return;
                    }

                    // Some headerInfo has to be added
                    addResource.writeWithFullHeader(extendedRequest, servletResponse);

                    // writes the response
                    addResource.writeResponse(extendedRequest, servletResponse);
                }
                else
                {

                    byte[] responseArray = extendedResponse.getBytes();

                    if (responseArray.length > 0)
                    {
                        // When not filtering due to not valid content-type,
                        // deliver the byte-array instead of a charset-converted
                        // string.  Otherwise a binary stream gets corrupted.
                        servletResponse.getOutputStream().write(responseArray);
                    }
                }

            }
            else
            {
                chain.renderFilter(request, response);
            }
        }
        finally
        {
            addResource.responseFinished();
        }
    }

    /**
     * Called by render method of MyFacesFilterPortlet to wrap the request when
     * it has a multipart content.
     *
     * @param request
     * @param response
     * @param chain PortletFilterChain instance
     * @throws PortletException
     */
    public void processActionFilter(
        ActionRequest request,
        ActionResponse response,
        PortletFilterChain chain) throws PortletException, IOException
    {
        if (log.isDebugEnabled())
            log.debug("called processActionFilter.");

        // Check multipart/form-data
        if (PortletFileUpload.isMultipartContent(request))
        {
            if (log.isDebugEnabled())
                log.debug("ActionRequest is multipart content.");
            if (multipartEncoding != null)
            {
                if (log.isDebugEnabled())
                    log.debug("Mutlipart encoding is " + multipartEncoding);
                request.setCharacterEncoding(multipartEncoding);
            }
            request = new MultipartPortletRequestWrapper(
                request,
                uploadMaxFileSize,
                uploadThresholdSize,
                uploadRepositoryPath);
        }

        // call next processActionFilter
        chain.processActionFilter(request, response);
    }

    /**
     * Called by destroy method of MyFacesFilterPortlet to destroy this portlet
     * filter.
     */
    public void destroy()
    {}

    private int resolveSize(String param, int defaultValue)
    {
        int numberParam = defaultValue;

        if (param != null)
        {
            param = param.toLowerCase();
            int factor = 1;
            String number = param;

            if (param.endsWith("g"))
            {
                factor = 1024 * 1024 * 1024;
                number = param.substring(0, param.length() - 1);
            }
            else if (param.endsWith("m"))
            {
                factor = 1024 * 1024;
                number = param.substring(0, param.length() - 1);
            }
            else if (param.endsWith("k"))
            {
                factor = 1024;
                number = param.substring(0, param.length() - 1);
            }

            numberParam = Integer.parseInt(number) * factor;
        }
        return numberParam;
    }

    /**
     * @return Returns the portletConfig.
     */
    public PortletConfig getPortletConfig()
    {
        return portletConfig;
    }

    /**
     * @param portletConfig The portletConfig to set.
     */
    public void setPortletConfig(PortletConfig portletConfig)
    {
        this.portletConfig = portletConfig;
    }

    public boolean isValidContentType(String contentType)
    {
        return contentType != null
            && (contentType.startsWith("text/html") || contentType.startsWith("text/xml")
                || contentType.startsWith("application/xhtml+xml") || contentType
                .startsWith("application/xml"));
    }
}
