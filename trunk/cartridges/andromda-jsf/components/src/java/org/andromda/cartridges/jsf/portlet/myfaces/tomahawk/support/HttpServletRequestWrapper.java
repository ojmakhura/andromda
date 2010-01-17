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
    private PortletRequest portletRequest;
    private PortletContext portletContext;

    public HttpServletRequestWrapper(
        PortletRequest portletRequest,
        PortletContext portletContext)
    {
        this.portletRequest = portletRequest;
        this.portletContext = portletContext;
    }

    public String getAuthType()
    {
        return portletRequest.getAuthType();
    }

    public String getContextPath()
    {
        return portletRequest.getContextPath();
    }

    public Cookie[] getCookies()
    {
        return null;
    }

    public long getDateHeader(String dateHeader)
    {

        return 0;
    }

    public String getHeader(String header)
    {
        return null;
    }

    public Enumeration getHeaderNames()
    {
        return null;
    }

    public Enumeration getHeaders(String arg0)
    {
        return null;
    }

    public int getIntHeader(String arg0)
    {
        return 0;
    }

    public String getMethod()
    {
        return null;
    }

    public String getPathInfo()
    {
        return null;
    }

    public String getPathTranslated()
    {
        return null;
    }

    public String getQueryString()
    {
        return null;
    }

    public String getRemoteUser()
    {
        return portletRequest.getRemoteUser();
    }

    public String getRequestedSessionId()
    {
        return portletRequest.getRequestedSessionId();
    }

    public String getRequestURI()
    {
        return null;
    }

    public StringBuffer getRequestURL()
    {
        return null;
    }

    public String getServletPath()
    {
        return null;
    }

    public HttpSession getSession()
    {
        return new HttpSessionWrapper(portletRequest.getPortletSession(),
            this.portletContext);
    }

    public HttpSession getSession(boolean create)
    {
        return new HttpSessionWrapper(portletRequest.getPortletSession(create),
            this.portletContext);
    }

    public Principal getUserPrincipal()
    {
        return portletRequest.getUserPrincipal();
    }

    public boolean isRequestedSessionIdFromCookie()
    {
        return false;
    }

    public boolean isRequestedSessionIdFromUrl()
    {
        return false;
    }

    public boolean isRequestedSessionIdFromURL()
    {
        return false;
    }

    public boolean isRequestedSessionIdValid()
    {
        return portletRequest.isRequestedSessionIdValid();
    }

    public boolean isUserInRole(String arg0)
    {
        return portletRequest.isUserInRole(arg0);
    }

    public Object getAttribute(String arg0)
    {
        return portletRequest.getAttribute(arg0);
    }

    public Enumeration getAttributeNames()
    {
        return portletRequest.getAttributeNames();
    }

    public String getCharacterEncoding()
    {
        return null;
    }

    public int getContentLength()
    {
        return 0;
    }

    public String getContentType()
    {
        return null;
    }

    public ServletInputStream getInputStream() throws IOException
    {
        return null;
    }

    public Locale getLocale()
    {
        return portletRequest.getLocale();
    }

    public Enumeration getLocales()
    {
        return portletRequest.getLocales();
    }

    public String getParameter(String arg0)
    {
        return portletRequest.getParameter(arg0);
    }

    public Map getParameterMap()
    {
        return portletRequest.getParameterMap();
    }

    public Enumeration getParameterNames()
    {
        return portletRequest.getParameterNames();
    }

    public String[] getParameterValues(String arg0)
    {
        return portletRequest.getParameterValues(arg0);
    }

    public String getProtocol()
    {
        return null;
    }

    public BufferedReader getReader() throws IOException
    {
        return null;
    }

    public String getRealPath(String arg0)
    {
        return null;
    }

    public String getRemoteAddr()
    {
        return null;
    }

    public String getRemoteHost()
    {
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String arg0)
    {
        return null;
    }

    public String getScheme()
    {
        return portletRequest.getScheme();
    }

    public String getServerName()
    {
        return portletRequest.getServerName();
    }

    public int getServerPort()
    {
        return portletRequest.getServerPort();
    }

    public boolean isSecure()
    {
        return portletRequest.isSecure();
    }

    public void removeAttribute(String arg0)
    {
        portletRequest.removeAttribute(arg0);
    }

    public void setAttribute(String arg0, Object arg1)
    {
        portletRequest.setAttribute(arg0, arg1);
    }

    public void setCharacterEncoding(String encoding) throws UnsupportedEncodingException
    {
    }

    public PortalContext getPortalContext()
    {
        return portletRequest.getPortalContext();
    }

    public PortletMode getPortletMode()
    {
        return portletRequest.getPortletMode();
    }

    public PortletSession getPortletSession()
    {
        return portletRequest.getPortletSession();
    }

    public PortletSession getPortletSession(boolean create)
    {
        return portletRequest.getPortletSession(create);
    }

    public PortletPreferences getPreferences()
    {
        return portletRequest.getPreferences();
    }

    public Enumeration getProperties(String arg0)
    {
        return portletRequest.getProperties(arg0);
    }

    public String getProperty(String arg0)
    {
        return portletRequest.getProperty(arg0);
    }

    public Enumeration getPropertyNames()
    {
        return portletRequest.getPropertyNames();
    }

    public String getResponseContentType()
    {
        return portletRequest.getResponseContentType();
    }

    public Enumeration getResponseContentTypes()
    {
        return portletRequest.getResponseContentTypes();
    }

    public WindowState getWindowState()
    {
        return portletRequest.getWindowState();
    }

    public boolean isPortletModeAllowed(PortletMode portletMode)
    {
        return portletRequest.isPortletModeAllowed(portletMode);
    }

    public boolean isWindowStateAllowed(WindowState windowState)
    {
        return portletRequest.isWindowStateAllowed(windowState);
    }

    public int getRemotePort()
    {
        return 0;
    }

    public String getLocalName()
    {
        return null;
    }

    public String getLocalAddr()
    {
        return null;
    }

    public int getLocalPort()
    {
        return 0;
    }
}