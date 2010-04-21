package org.andromda.cartridges.jsf.portlet.myfaces.tomahawk.support;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * This class is a dummy HttpSessionWrapper.
 *
 * @author <a href="mailto:shinsuke@yahoo.co.jp">Shinsuke Sugaya</a>
 */
public class HttpSessionWrapper
    implements HttpSession
{

    private PortletSession portletSession;

    private PortletContext portletContext;

    public HttpSessionWrapper(
        PortletSession portletSession,
        PortletContext portletContext)
    {
        this.portletSession = portletSession;
        this.portletContext = portletContext;
    }

    public long getCreationTime()
    {
        return portletSession.getCreationTime();
    }

    public String getId()
    {
        return portletSession.getId();
    }

    public long getLastAccessedTime()
    {
        return portletSession.getLastAccessedTime();
    }

    public ServletContext getServletContext()
    {
        return new ServletContextWrapper(portletContext);
    }

    public void setMaxInactiveInterval(int arg0)
    {
        portletSession.setMaxInactiveInterval(arg0);
    }

    public int getMaxInactiveInterval()
    {
        return portletSession.getMaxInactiveInterval();
    }

    public HttpSessionContext getSessionContext()
    {
        // TODO Portlet API does not have this method
        return null;
    }

    public Object getAttribute(String arg0)
    {
        return portletSession.getAttribute(arg0);
    }

    public Object getValue(String arg0)
    {
        return portletSession.getAttribute(arg0);
    }

    public Enumeration getAttributeNames()
    {
        return portletSession.getAttributeNames();
    }

    public String[] getValueNames()
    {
        ArrayList objs = new ArrayList();
        for (Enumeration e = portletSession.getAttributeNames(); e.hasMoreElements();)
        {
            String key = (String)e.nextElement();
            objs.add(key);
        }
        String[] values = new String[objs.size()];
        for (int i = 0; i < objs.size(); i++)
        {
            values[i] = (String)objs.get(i);
        }
        return values;
    }

    public void setAttribute(String arg0, Object arg1)
    {
        portletSession.setAttribute(arg0, arg1);
    }

    public void putValue(String arg0, Object arg1)
    {
        portletSession.setAttribute(arg0, arg1);
    }

    public void removeAttribute(String arg0)
    {
        portletSession.removeAttribute(arg0);
    }

    public void removeValue(String arg0)
    {
        portletSession.removeAttribute(arg0);
    }

    public void invalidate()
    {
        portletSession.invalidate();
    }

    public boolean isNew()
    {
        return portletSession.isNew();
    }
}