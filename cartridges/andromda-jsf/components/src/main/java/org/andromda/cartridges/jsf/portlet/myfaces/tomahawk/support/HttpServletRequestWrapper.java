package org.andromda.cartridges.jsf.portlet.myfaces.tomahawk.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.portlet.PortalContext;
import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class is a dummy HttpServletRequest.
 *
 * @author <a href="mailto:shinsuke@yahoo.co.jp">Shinsuke Sugaya</a>
 * @author Chad Brandon
 */
public class HttpServletRequestWrapper
implements HttpServletRequest
{
    private final PortletRequest portletRequest;
    private final PortletContext portletContext;

    /**
     * @param portletRequest
     * @param portletContext
     */
    public HttpServletRequestWrapper(
        final PortletRequest portletRequest,
        final PortletContext portletContext)
    {
        this.portletRequest = portletRequest;
        this.portletContext = portletContext;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getAuthType()
     */
    public String getAuthType()
    {
        return portletRequest.getAuthType();
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getContextPath()
     */
    public String getContextPath()
    {
        return portletRequest.getContextPath();
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getCookies()
     */
    public Cookie[] getCookies()
    {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getDateHeader(String)
     */
    public long getDateHeader(final String dateHeader)
    {
        return 0;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getHeader(String)
     */
    public String getHeader(final String header)
    {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
     */
    public Enumeration getHeaderNames()
    {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getHeaders(String)
     */
    public Enumeration getHeaders(final String arg0)
    {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getIntHeader(String)
     */
    public int getIntHeader(final String arg0)
    {
        return 0;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getMethod()
     */
    public String getMethod()
    {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getPathInfo()
     */
    public String getPathInfo()
    {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
     */
    public String getPathTranslated()
    {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getQueryString()
     */
    public String getQueryString()
    {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
     */
    public String getRemoteUser()
    {
        return portletRequest.getRemoteUser();
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
     */
    public String getRequestedSessionId()
    {
        return portletRequest.getRequestedSessionId();
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getRequestURI()
     */
    public String getRequestURI()
    {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getRequestURL()
     */
    public StringBuffer getRequestURL()
    {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getServletPath()
     */
    public String getServletPath()
    {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getSession()
     */
    public HttpSession getSession()
    {
        return new HttpSessionWrapper(portletRequest.getPortletSession(),
            portletContext);
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
     */
    public HttpSession getSession(final boolean create)
    {
        return new HttpSessionWrapper(portletRequest.getPortletSession(create),
            portletContext);
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
     */
    public Principal getUserPrincipal()
    {
        return portletRequest.getUserPrincipal();
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
     */
    public boolean isRequestedSessionIdFromCookie()
    {
        return false;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
     * @deprecated
     */
    public boolean isRequestedSessionIdFromUrl()
    {
        return false;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
     */
    public boolean isRequestedSessionIdFromURL()
    {
        return false;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
     */
    public boolean isRequestedSessionIdValid()
    {
        return portletRequest.isRequestedSessionIdValid();
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#isUserInRole(String)
     */
    public boolean isUserInRole(final String arg0)
    {
        return portletRequest.isUserInRole(arg0);
    }

    /**
     * @see javax.servlet.ServletRequest#getAttribute(String)
     */
    public Object getAttribute(final String arg0)
    {
        return portletRequest.getAttribute(arg0);
    }

    /**
     * @see javax.servlet.ServletRequest#getAttributeNames()
     */
    public Enumeration getAttributeNames()
    {
        return portletRequest.getAttributeNames();
    }

    /**
     * @see javax.servlet.ServletRequest#getCharacterEncoding()
     */
    public String getCharacterEncoding()
    {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getContentLength()
     */
    public int getContentLength()
    {
        return 0;
    }

    /**
     * @see javax.servlet.ServletRequest#getContentType()
     */
    public String getContentType()
    {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getInputStream()
     */
    public ServletInputStream getInputStream() throws IOException
    {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getLocale()
     */
    public Locale getLocale()
    {
        return portletRequest.getLocale();
    }

    /**
     * @see javax.servlet.ServletRequest#getLocales()
     */
    public Enumeration getLocales()
    {
        return portletRequest.getLocales();
    }

    /**
     * @see javax.servlet.ServletRequest#getParameter(String)
     */
    public String getParameter(final String arg0)
    {
        return portletRequest.getParameter(arg0);
    }

    /**
     * @see javax.servlet.ServletRequest#getParameterMap()
     */
    public Map getParameterMap()
    {
        return portletRequest.getParameterMap();
    }

    /**
     * @see javax.servlet.ServletRequest#getParameterNames()
     */
    public Enumeration getParameterNames()
    {
        return portletRequest.getParameterNames();
    }

    /**
     * @see javax.servlet.ServletRequest#getParameterValues(String)
     */
    public String[] getParameterValues(final String arg0)
    {
        return portletRequest.getParameterValues(arg0);
    }

    /**
     * @see javax.servlet.ServletRequest#getProtocol()
     */
    public String getProtocol()
    {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getReader()
     */
    public BufferedReader getReader() throws IOException
    {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getRealPath(String)
     * @deprecated
     */
    public String getRealPath(final String arg0)
    {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getRemoteAddr()
     */
    public String getRemoteAddr()
    {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getRemoteHost()
     */
    public String getRemoteHost()
    {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getRequestDispatcher(String)
     */
    public RequestDispatcher getRequestDispatcher(final String arg0)
    {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getScheme()
     */
    public String getScheme()
    {
        return portletRequest.getScheme();
    }

    /**
     * @see javax.servlet.ServletRequest#getServerName()
     */
    public String getServerName()
    {
        return portletRequest.getServerName();
    }

    /**
     * @see javax.servlet.ServletRequest#getServerPort()
     */
    public int getServerPort()
    {
        return portletRequest.getServerPort();
    }

    /**
     * @see javax.servlet.ServletRequest#isSecure()
     */
    public boolean isSecure()
    {
        return portletRequest.isSecure();
    }

    /**
     * @see javax.servlet.ServletRequest#removeAttribute(String)
     */
    public void removeAttribute(final String arg0)
    {
        portletRequest.removeAttribute(arg0);
    }

    /**
     * @see javax.servlet.ServletRequest#setAttribute(String, Object)
     */
    public void setAttribute(final String arg0, final Object arg1)
    {
        portletRequest.setAttribute(arg0, arg1);
    }

    /**
     * @see javax.servlet.ServletRequest#setCharacterEncoding(String)
     */
    public void setCharacterEncoding(final String encoding) throws UnsupportedEncodingException
    {
    }

    /**
     * @return portletRequest.getPortalContext()
     */
    public PortalContext getPortalContext()
    {
        return portletRequest.getPortalContext();
    }

    /**
     * @return portletRequest.getPortletMode()
     */
    public PortletMode getPortletMode()
    {
        return portletRequest.getPortletMode();
    }

    /**
     * @return portletRequest.getPortletSession()
     */
    public PortletSession getPortletSession()
    {
        return portletRequest.getPortletSession();
    }

    /**
     * @param create
     * @return portletRequest.getPortletSession(create)
     */
    public PortletSession getPortletSession(final boolean create)
    {
        return portletRequest.getPortletSession(create);
    }

    /**
     * @return portletRequest.getPreferences()
     */
    public PortletPreferences getPreferences()
    {
        return portletRequest.getPreferences();
    }

    /**
     * @param arg0
     * @return portletRequest.getProperties(arg0)
     */
    public Enumeration getProperties(final String arg0)
    {
        return portletRequest.getProperties(arg0);
    }

    /**
     * @param arg0
     * @return portletRequest.getProperty(arg0)
     */
    public String getProperty(final String arg0)
    {
        return portletRequest.getProperty(arg0);
    }

    /**
     * @return portletRequest.getPropertyNames()
     */
    public Enumeration getPropertyNames()
    {
        return portletRequest.getPropertyNames();
    }

    /**
     * @return portletRequest.getResponseContentType()
     */
    public String getResponseContentType()
    {
        return portletRequest.getResponseContentType();
    }

    /**
     * @return portletRequest.getResponseContentTypes()
     */
    public Enumeration getResponseContentTypes()
    {
        return portletRequest.getResponseContentTypes();
    }

    /**
     * @return portletRequest.getWindowState()
     */
    public WindowState getWindowState()
    {
        return portletRequest.getWindowState();
    }

    /**
     * @param portletMode
     * @return portletRequest.isPortletModeAllowed(portletMode)
     */
    public boolean isPortletModeAllowed(final PortletMode portletMode)
    {
        return portletRequest.isPortletModeAllowed(portletMode);
    }

    /**
     * @param windowState
     * @return portletRequest.isWindowStateAllowed(windowState)
     */
    public boolean isWindowStateAllowed(final WindowState windowState)
    {
        return portletRequest.isWindowStateAllowed(windowState);
    }

    /**
     * @see javax.servlet.ServletRequest#getRemotePort()
     */
    public int getRemotePort()
    {
        return 0;
    }

    /**
     * @see javax.servlet.ServletRequest#getLocalName()
     */
    public String getLocalName()
    {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getLocalAddr()
     */
    public String getLocalAddr()
    {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getLocalPort()
     */
    public int getLocalPort()
    {
        return 0;
    }
}