package org.andromda.cartridges.jsf.portlet.myfaces.tomahawk.support;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.portlet.PortletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class ServletContextWrapper implements ServletContext {

    private PortletContext portletContext;

    public ServletContextWrapper(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
     */
    public Object getAttribute(String arg0) {
        return portletContext.getAttribute(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getAttributeNames()
     */
    public Enumeration getAttributeNames() {
        return portletContext.getAttributeNames();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getContext(java.lang.String)
     */
    public ServletContext getContext(String arg0) {
        // TODO Portlet API does not have this method
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
     */
    public String getInitParameter(String arg0) {
        return portletContext.getInitParameter(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getInitParameterNames()
     */
    public Enumeration getInitParameterNames() {
        return portletContext.getInitParameterNames();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getMajorVersion()
     */
    public int getMajorVersion() {
        return portletContext.getMajorVersion();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
     */
    public String getMimeType(String arg0) {
        return portletContext.getMimeType(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getMinorVersion()
     */
    public int getMinorVersion() {
        return portletContext.getMinorVersion();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
     */
    public RequestDispatcher getNamedDispatcher(String arg0) {
        // TODO Portlet API does not have this method
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
     */
    public String getRealPath(String arg0) {
        return portletContext.getRealPath(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
     */
    public RequestDispatcher getRequestDispatcher(String arg0) {
        // TODO Portlet API does not have this method
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getResource(java.lang.String)
     */
    public URL getResource(String arg0) throws MalformedURLException {
        return portletContext.getResource(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
     */
    public InputStream getResourceAsStream(String arg0) {
        return portletContext.getResourceAsStream(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
     */
    public Set getResourcePaths(String arg0) {
        return portletContext.getResourcePaths(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getServerInfo()
     */
    public String getServerInfo() {
        return portletContext.getServerInfo();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getServlet(java.lang.String)
     */
    public Servlet getServlet(String arg0) throws ServletException {
        // TODO Portlet API does not have this method
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getServletContextName()
     */
    public String getServletContextName() {
        return portletContext.getPortletContextName();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getServletNames()
     */
    public Enumeration getServletNames() {
        // TODO Portlet API does not have this method
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#getServlets()
     */
    public Enumeration getServlets() {
        // TODO Portlet API does not have this method
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#log(java.lang.Exception,
     *      java.lang.String)
     */
    public void log(Exception arg0, String arg1) {
        portletContext.log(arg1, new Exception(arg0));
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#log(java.lang.String,
     *      java.lang.Throwable)
     */
    public void log(String arg0, Throwable arg1) {
        portletContext.log(arg0, arg1);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#log(java.lang.String)
     */
    public void log(String arg0) {
        portletContext.log(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String arg0) {
        portletContext.removeAttribute(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletContext#setAttribute(java.lang.String,
     *      java.lang.Object)
     */
    public void setAttribute(String arg0, Object arg1) {
        portletContext.setAttribute(arg0, arg1);
    }

}
