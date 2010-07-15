package org.andromda.cartridges.jsf.portlet.myfaces.tomahawk.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.webapp.filter.MultipartRequestWrapper;

/**
 * This class handles multipart/form-date request for Portlet. It will be called
 * if the request is multipart/form-data.
 *
 * @author <a href="mailto:shinsuke@yahoo.co.jp">Shinsuke Sugaya</a>
 * @author Sylvain Vieujot
 */
public class MultipartPortletRequestWrapper
implements ActionRequest, MultipartRequest
{
    private static Log log = LogFactory.getLog(MultipartPortletRequestWrapper.class);

    private ActionRequest request = null;

    private Map parametersMap = null;

    private PortletFileUpload fileUpload = null;

    private Map fileItems = null;

    private final int maxSize;

    private final int thresholdSize;

    private final String repositoryPath;

    /**
     * @param request
     * @param maxSize
     * @param thresholdSize
     * @param repositoryPath
     */
    public MultipartPortletRequestWrapper(
        final ActionRequest request,
        final int maxSize,
        final int thresholdSize,
        final String repositoryPath)
    {
        this.request = request;
        this.maxSize = maxSize;
        this.thresholdSize = thresholdSize;
        this.repositoryPath = repositoryPath;
    }

    private void parseRequest()
    {
        final DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

        diskFileItemFactory.setSizeThreshold(thresholdSize);

        if (repositoryPath != null && repositoryPath.trim().length() > 0)
        {
            diskFileItemFactory.setRepository(new File(repositoryPath));
        }

        fileUpload = new PortletFileUpload();
        fileUpload.setFileItemFactory(diskFileItemFactory);
        fileUpload.setSizeMax(maxSize);

        final String charset = request.getCharacterEncoding();
        fileUpload.setHeaderEncoding(charset);

        List requestParameters = null;
        try
        {
            requestParameters = fileUpload.parseRequest(request);
        }
        catch (final FileUploadBase.SizeLimitExceededException e)
        {
            // TODO: find a way to notify the user about the fact that the
            // uploaded file exceeded size limit

            if (MultipartPortletRequestWrapper.log.isInfoEnabled())
            {
                MultipartPortletRequestWrapper.log.info("user tried to upload a file that exceeded file-size limitations.", e);
            }

            requestParameters = Collections.EMPTY_LIST;

        }
        catch (final FileUploadException fue)
        {
            MultipartPortletRequestWrapper.log.error("Exception while uploading file.", fue);
            requestParameters = Collections.EMPTY_LIST;
        }

        parametersMap = new HashMap(requestParameters.size());
        fileItems = new HashMap();

        for (final Iterator iter = requestParameters.iterator(); iter.hasNext();)
        {
            final FileItem fileItem = (FileItem)iter.next();

            if (fileItem.isFormField())
            {
                final String name = fileItem.getFieldName();

                // The following code avoids commons-fileupload charset problem.
                // After fixing commons-fileupload, this code should be
                //
                // String value = fileItem.getString();
                //
                String value = null;
                if (charset == null)
                {
                    value = fileItem.getString();
                }
                else
                {
                    try
                    {
                        value = new String(fileItem.get(), charset);
                    }
                    catch (final UnsupportedEncodingException e)
                    {
                        value = fileItem.getString();
                    }
                }

                addTextParameter(name, value);
            }
            else
            { // fileItem is a File
                if (fileItem.getName() != null)
                {
                    fileItems.put(fileItem.getFieldName(), fileItem);
                }
            }
        }

        // Add the query string paramters
        for (final Iterator it = request.getParameterMap().entrySet().iterator(); it.hasNext();)
        {
            final Map.Entry entry = (Map.Entry)it.next();
            final String[] valuesArray = (String[])entry.getValue();
            for (final String element : valuesArray)
            {
                addTextParameter((String)entry.getKey(), element);
            }
        }
    }

    private void addTextParameter(final String name, final String value)
    {
        if (!parametersMap.containsKey(name))
        {
            final String[] valuesArray =
            {
                value
            };
            parametersMap.put(name, valuesArray);
        }
        else
        {
            final String[] storedValues = (String[])parametersMap.get(name);
            final int lengthSrc = storedValues.length;
            final String[] valuesArray = new String[lengthSrc + 1];
            System.arraycopy(storedValues, 0, valuesArray, 0, lengthSrc);
            valuesArray[lengthSrc] = value;
            parametersMap.put(name, valuesArray);
        }
    }

    /**
     * @see javax.portlet.PortletRequest#getParameterNames()
     */
    public Enumeration getParameterNames()
    {
        if (parametersMap == null)
        {
            parseRequest();
        }

        return Collections.enumeration(parametersMap.keySet());
    }

    /**
     * @see javax.portlet.PortletRequest#getParameter(String)
     */
    public String getParameter(final String name)
    {
        if (parametersMap == null)
        {
            parseRequest();
        }

        final String[] values = (String[])parametersMap.get(name);
        if (values == null)
        {
            return null;
        }
        return values[0];
    }

    /**
     * @see javax.portlet.PortletRequest#getParameterValues(String)
     */
    public String[] getParameterValues(final String name)
    {
        if (parametersMap == null)
        {
            parseRequest();
        }

        return (String[])parametersMap.get(name);
    }

    /**
     * @see javax.portlet.PortletRequest#getParameterMap()
     */
    public Map getParameterMap()
    {
        if (parametersMap == null)
        {
            parseRequest();
        }

        return parametersMap;
    }

    // Hook for the x:inputFileUpload tag.
    /**
     * @see org.andromda.cartridges.jsf.portlet.myfaces.tomahawk.support.MultipartRequest#getFileItem(String)
     */
    public FileItem getFileItem(final String fieldName)
    {
        if (fileItems == null)
        {
            parseRequest();
        }

        return (FileItem)fileItems.get(fieldName);
    }

    /**
     * Not used internally by MyFaces, but provides a way to handle the uploaded
     * files out of MyFaces.
     * @return fileItems
     */
    public Map getFileItems()
    {
        if (fileItems == null)
        {
            parseRequest();
        }

        return fileItems;
    }

    /**
     * @see javax.portlet.PortletRequest#getAttribute(String)
     */
    public Object getAttribute(final String arg0)
    {
        if (arg0.equals(MultipartRequestWrapper.UPLOADED_FILES_ATTRIBUTE))
        {
            return getFileItems();
        }
        return request.getAttribute(arg0);
    }

    /**
     * @see javax.portlet.PortletRequest#getAttributeNames()
     */
    public Enumeration getAttributeNames()
    {
        return request.getAttributeNames();
    }

    /**
     * @see javax.portlet.PortletRequest#getAuthType()
     */
    public String getAuthType()
    {
        return request.getAuthType();
    }

    /**
     * @see javax.portlet.PortletRequest#getContextPath()
     */
    public String getContextPath()
    {
        return request.getContextPath();
    }

    /**
     * @see javax.portlet.PortletRequest#getLocale()
     */
    public Locale getLocale()
    {
        return request.getLocale();
    }

    /**
     * @see javax.portlet.PortletRequest#getLocales()
     */
    public Enumeration getLocales()
    {
        return request.getLocales();
    }

    /**
     * @see javax.portlet.PortletRequest#getPortalContext()
     */
    public PortalContext getPortalContext()
    {
        return request.getPortalContext();
    }

    /**
     * @see javax.portlet.PortletRequest#getPortletMode()
     */
    public PortletMode getPortletMode()
    {
        return request.getPortletMode();
    }

    /**
     * @see javax.portlet.PortletRequest#getPortletSession()
     */
    public PortletSession getPortletSession()
    {
        return request.getPortletSession();
    }

    /**
     * @see javax.portlet.PortletRequest#getPortletSession(boolean)
     */
    public PortletSession getPortletSession(final boolean arg0)
    {
        return request.getPortletSession(arg0);
    }

    /**
     * @see javax.portlet.PortletRequest#getPreferences()
     */
    public PortletPreferences getPreferences()
    {
        return request.getPreferences();
    }

    /**
     * @see javax.portlet.PortletRequest#getProperties(String)
     */
    public Enumeration getProperties(final String arg0)
    {
        return request.getProperties(arg0);
    }

    /**
     * @see javax.portlet.PortletRequest#getProperty(String)
     */
    public String getProperty(final String arg0)
    {
        return request.getProperty(arg0);
    }

    /**
     * @see javax.portlet.PortletRequest#getPropertyNames()
     */
    public Enumeration getPropertyNames()
    {
        return request.getPropertyNames();
    }

    /**
     * @see javax.portlet.PortletRequest#getRemoteUser()
     */
    public String getRemoteUser()
    {
        return request.getRemoteUser();
    }

    /**
     * @see javax.portlet.PortletRequest#getRequestedSessionId()
     */
    public String getRequestedSessionId()
    {
        return request.getRequestedSessionId();
    }

    /**
     * @see javax.portlet.PortletRequest#getResponseContentType()
     */
    public String getResponseContentType()
    {
        return request.getResponseContentType();
    }

    /**
     * @see javax.portlet.PortletRequest#getResponseContentTypes()
     */
    public Enumeration getResponseContentTypes()
    {
        return request.getResponseContentTypes();
    }

    /**
     * @see javax.portlet.PortletRequest#getScheme()
     */
    public String getScheme()
    {
        return request.getScheme();
    }

    /**
     * @see javax.portlet.PortletRequest#getServerName()
     */
    public String getServerName()
    {
        return request.getServerName();
    }

    /**
     * @see javax.portlet.PortletRequest#getServerPort()
     */
    public int getServerPort()
    {
        return request.getServerPort();
    }

    /**
     * @see javax.portlet.PortletRequest#getUserPrincipal()
     */
    public Principal getUserPrincipal()
    {
        return request.getUserPrincipal();
    }

    /**
     * @see javax.portlet.PortletRequest#getWindowState()
     */
    public WindowState getWindowState()
    {
        return request.getWindowState();
    }

    /**
     * @see javax.portlet.PortletRequest#isPortletModeAllowed(javax.portlet.PortletMode)
     */
    public boolean isPortletModeAllowed(final PortletMode arg0)
    {
        return request.isPortletModeAllowed(arg0);
    }

    /**
     * @see javax.portlet.PortletRequest#isRequestedSessionIdValid()
     */
    public boolean isRequestedSessionIdValid()
    {
        return request.isRequestedSessionIdValid();
    }

    /**
     * @see javax.portlet.PortletRequest#isSecure()
     */
    public boolean isSecure()
    {
        return request.isSecure();
    }

    /**
     * @see javax.portlet.PortletRequest#isUserInRole(String)
     */
    public boolean isUserInRole(final String arg0)
    {
        return request.isUserInRole(arg0);
    }

    /**
     * @see javax.portlet.PortletRequest#isWindowStateAllowed(javax.portlet.WindowState)
     */
    public boolean isWindowStateAllowed(final WindowState arg0)
    {
        return request.isWindowStateAllowed(arg0);
    }

    /**
     * @see javax.portlet.PortletRequest#removeAttribute(String)
     */
    public void removeAttribute(final String arg0)
    {
        request.removeAttribute(arg0);
    }

    /**
     * @see javax.portlet.PortletRequest#setAttribute(String,
     *      Object)
     */
    public void setAttribute(final String arg0, final Object arg1)
    {
        request.setAttribute(arg0, arg1);
    }

    /**
     * @see javax.portlet.ActionRequest#getCharacterEncoding()
     */
    public String getCharacterEncoding()
    {
        return request.getCharacterEncoding();
    }

    /**
     * @see javax.portlet.ActionRequest#getContentLength()
     */
    public int getContentLength()
    {
        return request.getContentLength();
    }

    /**
     * @see javax.portlet.ActionRequest#getContentType()
     */
    public String getContentType()
    {
        return request.getContentType();
    }

    /**
     * @see javax.portlet.ActionRequest#getPortletInputStream()
     */
    public InputStream getPortletInputStream() throws IOException
    {
        return request.getPortletInputStream();
    }

    /**
     * @see javax.portlet.ActionRequest#getReader()
     */
    public BufferedReader getReader() throws UnsupportedEncodingException, IOException
    {
        return request.getReader();
    }

    /**
     * @see javax.portlet.ActionRequest#setCharacterEncoding(String)
     */
    public void setCharacterEncoding(final String arg0) throws UnsupportedEncodingException
    {
        request.setCharacterEncoding(arg0);
    }
}