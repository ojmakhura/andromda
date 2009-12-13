package org.andromda.cartridges.jsf;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * A servlet allowing us to load resources from a jar and make them available to
 * the web application.
 */
public class JSFServlet
    extends HttpServlet
{
    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException
    {
        final String uri = request.getRequestURI();
        final String path =
            '/' + uri.substring(uri.indexOf(Constants.RESOURCE_CONTEXT) + Constants.RESOURCE_CONTEXT.length() + 1);
        final InputStream resource = JSFServlet.class.getResourceAsStream(path);
        if (resource == null)
        {
            throw new ServletException("Could not load resource from path '" + path + '\'');
        }

        // - write resource to the output stream
        final OutputStream out = response.getOutputStream();
        final byte[] buffer = new byte[2048];
        BufferedInputStream inputStream = new BufferedInputStream(resource);
        int read = 0;
        read = inputStream.read(buffer);
        while (read != -1)
        {
            out.write(
                buffer,
                0,
                read);
            read = inputStream.read(buffer);
        }
        inputStream.close();
        out.flush();
        out.close();
    }
}