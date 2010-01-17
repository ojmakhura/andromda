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

    private int maxSize;

    private int thresholdSize;

    private String repositoryPath;

    public MultipartPortletRequestWrapper(
        ActionRequest request,
        int maxSize,
        int thresholdSize,
        String repositoryPath)
    {
        this.request = request;
        this.maxSize = maxSize;
        this.thresholdSize = thresholdSize;
        this.repositoryPath = repositoryPath;
    }

    private void parseRequest()
    {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

        diskFileItemFactory.setSizeThreshold(thresholdSize);

        if (repositoryPath != null && repositoryPath.trim().length() > 0)
            diskFileItemFactory.setRepository(new File(repositoryPath));

        fileUpload = new PortletFileUpload();
        fileUpload.setFileItemFactory(diskFileItemFactory);
        fileUpload.setSizeMax(maxSize);

        String charset = request.getCharacterEncoding();
        fileUpload.setHeaderEncoding(charset);

        List requestParameters = null;
        try
        {
            requestParameters = fileUpload.parseRequest(request);
        }
        catch (FileUploadBase.SizeLimitExceededException e)
        {

            // TODO: find a way to notify the user about the fact that the
            // uploaded file exceeded size limit

            if (log.isInfoEnabled())
                log.info("user tried to upload a file that exceeded file-size limitations.", e);

            requestParameters = Collections.EMPTY_LIST;

        }
        catch (FileUploadException fue)
        {
            log.error("Exception while uploading file.", fue);
            requestParameters = Collections.EMPTY_LIST;
        }

        parametersMap = new HashMap(requestParameters.size());
        fileItems = new HashMap();

        for (Iterator iter = requestParameters.iterator(); iter.hasNext();)
        {
            FileItem fileItem = (FileItem)iter.next();

            if (fileItem.isFormField())
            {
                String name = fileItem.getFieldName();

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
                    catch (UnsupportedEncodingException e)
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
        for (Iterator it = request.getParameterMap().entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry)it.next();
            String[] valuesArray = (String[])entry.getValue();
            for (int i = 0; i < valuesArray.length; i++)
            {
                addTextParameter((String)entry.getKey(), valuesArray[i]);
            }
        }
    }

    private void addTextParameter(String name, String value)
    {
        if (!parametersMap.containsKey(name))
        {
            String[] valuesArray =
            {
                value
            };
            parametersMap.put(name, valuesArray);
        }
        else
        {
            String[] storedValues = (String[])parametersMap.get(name);
            int lengthSrc = storedValues.length;
            String[] valuesArray = new String[lengthSrc + 1];
            System.arraycopy(storedValues, 0, valuesArray, 0, lengthSrc);
            valuesArray[lengthSrc] = value;
            parametersMap.put(name, valuesArray);
        }
    }

    public Enumeration getParameterNames()
    {
        if (parametersMap == null)
            parseRequest();

        return Collections.enumeration(parametersMap.keySet());
    }

    public String getParameter(String name)
    {
        if (parametersMap == null)
            parseRequest();

        String[] values = (String[])parametersMap.get(name);
        if (values == null)
            return null;
        return values[0];
    }

    public String[] getParameterValues(String name)
    {
        if (parametersMap == null)
            parseRequest();

        return (String[])parametersMap.get(name);
    }

    public Map getParameterMap()
    {
        if (parametersMap == null)
            parseRequest();

        return parametersMap;
    }

    // Hook for the x:inputFileUpload tag.
    public FileItem getFileItem(String fieldName)
    {
        if (fileItems == null)
            parseRequest();

        return (FileItem)fileItems.get(fieldName);
    }

    /**
     * Not used internaly by MyFaces, but provides a way to handle the uploaded
     * files out of MyFaces.
     */
    public Map getFileItems()
    {
        if (fileItems == null)
            parseRequest();

        return fileItems;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getAttribute(java.lang.String)
     */
    public Object getAttribute(String arg0)
    {
        if (arg0.equals(MultipartRequestWrapper.UPLOADED_FILES_ATTRIBUTE))
        {
            return getFileItems();
        }
        return request.getAttribute(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getAttributeNames()
     */
    public Enumeration getAttributeNames()
    {
        return request.getAttributeNames();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getAuthType()
     */
    public String getAuthType()
    {
        return request.getAuthType();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getContextPath()
     */
    public String getContextPath()
    {
        return request.getContextPath();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getLocale()
     */
    public Locale getLocale()
    {
        return request.getLocale();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getLocales()
     */
    public Enumeration getLocales()
    {
        return request.getLocales();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getPortalContext()
     */
    public PortalContext getPortalContext()
    {
        return request.getPortalContext();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getPortletMode()
     */
    public PortletMode getPortletMode()
    {
        return request.getPortletMode();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getPortletSession()
     */
    public PortletSession getPortletSession()
    {
        return request.getPortletSession();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getPortletSession(boolean)
     */
    public PortletSession getPortletSession(boolean arg0)
    {
        return request.getPortletSession(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getPreferences()
     */
    public PortletPreferences getPreferences()
    {
        return request.getPreferences();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getProperties(java.lang.String)
     */
    public Enumeration getProperties(String arg0)
    {
        return request.getProperties(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getProperty(java.lang.String)
     */
    public String getProperty(String arg0)
    {
        return request.getProperty(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getPropertyNames()
     */
    public Enumeration getPropertyNames()
    {
        return request.getPropertyNames();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getRemoteUser()
     */
    public String getRemoteUser()
    {
        return request.getRemoteUser();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getRequestedSessionId()
     */
    public String getRequestedSessionId()
    {
        return request.getRequestedSessionId();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getResponseContentType()
     */
    public String getResponseContentType()
    {
        return request.getResponseContentType();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getResponseContentTypes()
     */
    public Enumeration getResponseContentTypes()
    {
        return request.getResponseContentTypes();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getScheme()
     */
    public String getScheme()
    {
        return request.getScheme();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getServerName()
     */
    public String getServerName()
    {
        return request.getServerName();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getServerPort()
     */
    public int getServerPort()
    {
        return request.getServerPort();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getUserPrincipal()
     */
    public Principal getUserPrincipal()
    {
        return request.getUserPrincipal();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#getWindowState()
     */
    public WindowState getWindowState()
    {
        return request.getWindowState();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#isPortletModeAllowed(javax.portlet.PortletMode)
     */
    public boolean isPortletModeAllowed(PortletMode arg0)
    {
        return request.isPortletModeAllowed(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#isRequestedSessionIdValid()
     */
    public boolean isRequestedSessionIdValid()
    {
        return request.isRequestedSessionIdValid();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#isSecure()
     */
    public boolean isSecure()
    {
        return request.isSecure();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#isUserInRole(java.lang.String)
     */
    public boolean isUserInRole(String arg0)
    {
        return request.isUserInRole(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#isWindowStateAllowed(javax.portlet.WindowState)
     */
    public boolean isWindowStateAllowed(WindowState arg0)
    {
        return request.isWindowStateAllowed(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String arg0)
    {
        request.removeAttribute(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletRequest#setAttribute(java.lang.String,
     *      java.lang.Object)
     */
    public void setAttribute(String arg0, Object arg1)
    {
        request.setAttribute(arg0, arg1);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.ActionRequest#getCharacterEncoding()
     */
    public String getCharacterEncoding()
    {
        return request.getCharacterEncoding();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.ActionRequest#getContentLength()
     */
    public int getContentLength()
    {
        return request.getContentLength();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.ActionRequest#getContentType()
     */
    public String getContentType()
    {
        return request.getContentType();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.ActionRequest#getPortletInputStream()
     */
    public InputStream getPortletInputStream() throws IOException
    {
        return request.getPortletInputStream();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.ActionRequest#getReader()
     */
    public BufferedReader getReader() throws UnsupportedEncodingException, IOException
    {
        return request.getReader();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.ActionRequest#setCharacterEncoding(java.lang.String)
     */
    public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException
    {
        request.setCharacterEncoding(arg0);
    }
}