package org.andromda.cartridges.jsf.portlet.myfaces.tomahawk.support;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * This class is a dummy HttpServletResponse.
 *
 * @author <a href="mailto:shinsuke@yahoo.co.jp">Shinsuke Sugaya</a>
 */
public class HttpServletResponseWrapper
    implements HttpServletResponse, RenderResponse
{

    private RenderResponse renderResponse;

    public HttpServletResponseWrapper(
        RenderResponse renderResponse)
    {
        this.renderResponse = renderResponse;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletResponseWrapper#getWriter()
     */
    public PrintWriter getWriter() throws IOException
    {
        return this.renderResponse.getWriter();
    }

    /**
     * Returns writer to which MyFaces' AddResource stores elements.
     *
     * @return writer which has elements, such as &lt;script&gt; and
     *         &lt;link&gt; public StringWriter getStringWriter() { return
     *         (StringWriter) writer; }
     */

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletResponse#flushBuffer()
     */
    public void flushBuffer() throws IOException
    {
        if (renderResponse != null)
        {
            renderResponse.flushBuffer();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletResponse#getBufferSize()
     */
    public int getBufferSize()
    {
        if (renderResponse != null)
        {
            return renderResponse.getBufferSize();
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletResponse#getCharacterEncoding()
     */
    public String getCharacterEncoding()
    {
        if (renderResponse != null)
        {
            return renderResponse.getCharacterEncoding();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletResponse#getLocale()
     */
    public Locale getLocale()
    {
        if (renderResponse != null)
        {
            return renderResponse.getLocale();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletResponse#getOutputStream()
     */
    public ServletOutputStream getOutputStream() throws IOException
    {
        if (renderResponse != null)
        {
            return new ServletOutputStreamWrapper(renderResponse.getPortletOutputStream());
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletResponse#isCommitted()
     */
    public boolean isCommitted()
    {
        if (renderResponse != null)
        {
            return renderResponse.isCommitted();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletResponse#reset()
     */
    public void reset()
    {
        if (renderResponse != null)
        {
            renderResponse.reset();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletResponse#resetBuffer()
     */
    public void resetBuffer()
    {
        if (renderResponse != null)
        {
            renderResponse.resetBuffer();
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletResponse#setBufferSize(int)
     */
    public void setBufferSize(int arg0)
    {
        renderResponse.setBufferSize(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletResponse#setContentLength(int)
     */
    public void setContentLength(int arg0)
    {


    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
     */
    public void setContentType(String arg0)
    {
        if (renderResponse != null)
        {
            renderResponse.setContentType(arg0);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
     */
    public void setLocale(Locale arg0)
    {


    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
     */
    public void addCookie(Cookie arg0)
    {


    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String,
     *      long)
     */
    public void addDateHeader(String arg0, long arg1)
    {


    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String,
     *      java.lang.String)
     */
    public void addHeader(String arg0, String arg1)
    {


    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String,
     *      int)
     */
    public void addIntHeader(String arg0, int arg1)
    {


    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
     */
    public boolean containsHeader(String arg0)
    {

        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
     */
    public String encodeRedirectUrl(String arg0)
    {

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
     */
    public String encodeRedirectURL(String arg0)
    {

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
     */
    public String encodeUrl(String arg0)
    {
        if (renderResponse != null)
        {
            return renderResponse.encodeURL(arg0);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
     */
    public String encodeURL(String arg0)
    {
        return renderResponse.encodeURL(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#sendError(int,
     *      java.lang.String)
     */
    public void sendError(int arg0, String arg1) throws IOException
    {


    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#sendError(int)
     */
    public void sendError(int arg0) throws IOException
    {


    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
     */
    public void sendRedirect(String arg0) throws IOException
    {


    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String,
     *      long)
     */
    public void setDateHeader(String arg0, long arg1)
    {


    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String,
     *      java.lang.String)
     */
    public void setHeader(String arg0, String arg1)
    {


    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String,
     *      int)
     */
    public void setIntHeader(String arg0, int arg1)
    {


    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#setStatus(int,
     *      java.lang.String)
     */
    public void setStatus(int arg0, String arg1)
    {


    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServletResponse#setStatus(int)
     */
    public void setStatus(int arg0)
    {


    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletResponse#addProperty(java.lang.String,
     *      java.lang.String)
     */
    public void addProperty(String arg0, String arg1)
    {
        renderResponse.addProperty(arg0, arg1);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.PortletResponse#setProperty(java.lang.String,
     *      java.lang.String)
     */
    public void setProperty(String arg0, String arg1)
    {
        renderResponse.setProperty(arg0, arg1);
    }

    public String getContentType()
    {
        if (renderResponse != null)
        {
            return renderResponse.getContentType();
        }
        return null;
    }

    public void setCharacterEncoding(String arg0)
    {


    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.RenderResponse#createActionURL()
     */
    public PortletURL createActionURL()
    {
        return this.renderResponse.createActionURL();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.RenderResponse#createRenderURL()
     */
    public PortletURL createRenderURL()
    {
        return this.createRenderURL();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.RenderResponse#getNamespace()
     */
    public String getNamespace()
    {
        return this.getNamespace();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.RenderResponse#getPortletOutputStream()
     */
    public OutputStream getPortletOutputStream() throws IOException
    {
        return this.getPortletOutputStream();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.portlet.RenderResponse#setTitle(java.lang.String)
     */
    public void setTitle(String title)
    {
        this.renderResponse.setTitle(title);
    }

    public RenderResponse getResponse()
    {
        return this.renderResponse;
    }
}