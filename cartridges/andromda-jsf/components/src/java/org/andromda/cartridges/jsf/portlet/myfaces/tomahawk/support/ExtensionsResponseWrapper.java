package org.andromda.cartridges.jsf.portlet.myfaces.tomahawk.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.xml.sax.InputSource;

/**
 * @author Sylvain Vieujot (latest modification by $Author: cwbrandon $)
 * @author Chad Brandon
 */
public class ExtensionsResponseWrapper
    extends HttpServletResponseWrapper
    implements RenderResponse
{
    private ByteArrayOutputStream stream = null;
    private PrintWriter printWriter = null;
    private String contentType;

    private RenderResponse response;

    public ExtensionsResponseWrapper(
        HttpServletResponse servletResponse,
        RenderResponse response)
    {
        super(servletResponse);
        this.response = response;
        stream = new ByteArrayOutputStream();
    }

    public byte[] getBytes()
    {
        return stream.toByteArray();
    }

    public String toString()
    {
        try
        {
            return this.stream.toString(getCharacterEncoding());
        }
        catch (UnsupportedEncodingException e)
        {
            // an attempt to set an invalid character encoding would have caused
            // this exception before
            throw new RuntimeException("Response accepted invalid character encoding "
                + getCharacterEncoding());
        }
    }

    /**
     * This method is used by Tomcat.
     *
     * @throws IOException
     */
    public PrintWriter getWriter()
    {
        if (printWriter == null)
        {
            OutputStreamWriter streamWriter = new OutputStreamWriter(this.stream, Charset.forName(getCharacterEncoding()));
            printWriter = new PrintWriter(streamWriter, true);
        }
        return printWriter;
    }

    /**
     * This method is used by Jetty.
     *
     * @throws IOException
     */
    public ServletOutputStream getOutputStream() throws IOException
    {
        return new MyServletOutputStream(this.stream);
    }

    public InputSource getInputSource()
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(stream.toByteArray());
        return new InputSource(bais);
    }

    /**
     * Prevent content-length being set as the page might be modified.
     */
    public void setContentLength(int contentLength)
    {
    }

    public void setContentType(String contentType)
    {
        super.setContentType(contentType);
        this.contentType = contentType;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void flushBuffer() throws IOException
    {
        this.stream.flush();
    }

    public void finishResponse()
    {
        try
        {
            if (printWriter != null)
            {
                printWriter.close();
            }
            else
            {
                if (this.stream != null)
                {
                    this.stream.close();
                }
            }
        }
        catch (IOException e)
        {
        }
    }

    /**
     * Used in the <code>getOutputStream()</code> method.
     */
    private class MyServletOutputStream
        extends ServletOutputStream
    {
        private OutputStream outputStream;

        public MyServletOutputStream(
            OutputStream outputStream)
        {
            this.outputStream = outputStream;
        }

        public void write(int b) throws IOException
        {
            outputStream.write(b);
        }

        public void write(byte[] bytes) throws IOException
        {
            outputStream.write(bytes);
        }

        public void write(byte[] bytes, int off, int len) throws IOException
        {
            outputStream.write(bytes, off, len);
        }
    }

    public PortletURL createActionURL()
    {
        return this.response.createActionURL();
    }

    public PortletURL createRenderURL()
    {
        return this.response.createRenderURL();
    }

    public int getBufferSize()
    {
        return this.response.getBufferSize();
    }

    public String getCharacterEncoding()
    {
        return this.response.getCharacterEncoding();
    }

    public Locale getLocale()
    {
        return this.response.getLocale();
    }

    public String getNamespace()
    {
        return this.response.getNamespace();
    }

    public OutputStream getPortletOutputStream() throws IOException
    {
        return this.stream;
    }

    public boolean isCommitted()
    {
        return this.response.isCommitted();
    }

    public void reset()
    {
        this.response.reset();
    }

    public void resetBuffer()
    {
        this.response.resetBuffer();
    }

    public void setBufferSize(int bufferSize)
    {
        this.response.setBufferSize(bufferSize);
    }

    public void setTitle(String title)
    {
        this.response.setTitle(title);
    }

    public void addProperty(String arg0, String arg1)
    {
        this.response.addProperty(arg0, arg1);
    }

    public String encodeURL(String arg0)
    {
        return this.response.encodeURL(arg0);
    }

    public void setProperty(String arg0, String arg1)
    {
        this.response.setProperty(arg0, arg1);
    }
}