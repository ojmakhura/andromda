package org.andromda.cartridges.jsf.renderkit;

import java.io.IOException;
import java.io.OutputStream;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import javax.servlet.http.HttpServletResponse;

import org.andromda.cartridges.jsf.component.BinaryFile;


/**
 * A custom renderer for rendering a popup frame.
 */
public class BinaryFileRenderer
    extends Renderer
{
    /**
     * Gets the current response instance.
     *
     * @return the current response.
     */
    private HttpServletResponse getResponse(final FacesContext context)
    {
        return (HttpServletResponse)context.getExternalContext().getResponse();
    }

    /**
     * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeBegin(
        FacesContext context,
        UIComponent component)
        throws IOException
    {
        final BinaryFile fileComponent = (BinaryFile)component;
        if (fileComponent.isRendered())
        {
            final HttpServletResponse response = this.getResponse(context);
            final OutputStream stream = response.getOutputStream();

            // - reset the reponse to clear out any any headers (i.e. so
            //   the user doesn't get "unable to open..." when using IE.
            response.reset();
            final String fileName = fileComponent.getFileName();
            if (fileComponent.isPrompt() && fileName != null && fileName.trim().length() > 0)
            {
                response.addHeader(
                    "Content-disposition",
                    "attachment; filename=\"" + fileName + '"');
            }
            byte[] file = (byte[])fileComponent.getValue();

            // - for IE we need to set the content type, content length and buffer size and 
            //   then the flush the response right away because it seems as if there is any lag time
            //   IE just displays a blank page. With mozilla based clients reports display correctly regardless.
            final String contentType = fileComponent.getContentType();
            if (contentType != null && contentType.length() > 0)
            {
                response.setContentType(contentType);
            }
            if (file != null)
            {
                response.setBufferSize(file.length);
                response.setContentLength(file.length);
                response.flushBuffer();
                stream.write(file);
            }
        }
    }

    /**
     * @see javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeEnd(
        final FacesContext context,
        final UIComponent component)
        throws IOException
    {
        final BinaryFile fileComponent = (BinaryFile)component;
        if (fileComponent.isRendered())
        {
            final HttpServletResponse response = this.getResponse(context);
            final OutputStream stream = response.getOutputStream();
            stream.flush();
            stream.close();
        }
    }
}