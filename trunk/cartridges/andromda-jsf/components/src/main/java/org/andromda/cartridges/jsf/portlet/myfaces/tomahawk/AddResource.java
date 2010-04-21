package org.andromda.cartridges.jsf.portlet.myfaces.tomahawk;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.myfaces.renderkit.html.util.HtmlBufferResponseWriterWrapper;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlResponseWriterImpl;

/**
 * Extends the default MyFaces Tomahawk {DefaultAddResource} class in order to
 * allow injection of the required content into portlets.
 *
 * @author Chad Brandon
 */
public class AddResource
    extends org.apache.myfaces.renderkit.html.util.DefaultAddResource
{
    /**
     * Overridden to allow injection of the required content for portlets (i.e. which have no <head/>, <body/>, etc tags).
     *
     * @see org.apache.myfaces.renderkit.html.util.DefaultAddResource#writeWithFullHeader(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void writeWithFullHeader(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        if (!parserCalled)
        {
            throw new IOException("Method parseResponse has to be called first");
        }

        final ResponseWriter writer = new HtmlResponseWriterImpl(
            response.getWriter(),
            HtmlRendererUtils.selectContentType(request.getHeader("accept")),
            response.getCharacterEncoding());

        // - insert anything that was supposed to go into the <head/>
        headerInsertPosition = 0;
        final HtmlBufferResponseWriterWrapper headContents = HtmlBufferResponseWriterWrapper
            .getInstance(writer);
        for (Iterator iterator = getHeaderBeginInfos().iterator(); iterator.hasNext();)
        {
            headContents.write("\n");

            final PositionedInfo positionedInfo = (PositionedInfo)iterator.next();

            if (!(positionedInfo instanceof WritablePositionedInfo))
                throw new IllegalStateException("positionedInfo of type : "
                    + positionedInfo.getClass().getName());
            ((WritablePositionedInfo)positionedInfo).writePositionedInfo(
                response,
                headContents);
        }
        originalResponse.insert(headerInsertPosition, headContents.toString());

        // TODO: fix this
        /*StringBuffer buf = new StringBuffer();
        Set bodyInfos = getBodyOnloadInfos();
        if (bodyInfos.size() > 0)
        {
            int i = 0;
            for (Iterator it = getBodyOnloadInfos().iterator(); it.hasNext();)
            {
                AttributeInfo positionedInfo = (AttributeInfo) it.next();
                if (i == 0)
                {
                    buf.append(positionedInfo.getAttributeName());
                    buf.append("=\"");
                }
                buf.append(positionedInfo.getAttributeValue());

                i++;
            }

            buf.append("\"");
            originalResponse.insert(bodyInsertPosition - 1, " " + buf.toString());
        }
         */

        // insert all the items that want to go immediately after the <body> tag.
        final HtmlBufferResponseWriterWrapper afterBodyContents = HtmlBufferResponseWriterWrapper.getInstance(writer);

        for (Iterator i = getBodyEndInfos().iterator(); i.hasNext();)
        {
            afterBodyContents.write("\n");
            final PositionedInfo positionedInfo = (PositionedInfo)i.next();

            if (!(positionedInfo instanceof WritablePositionedInfo))
                throw new IllegalStateException("positionedInfo of type : "
                    + positionedInfo.getClass().getName());
            ((WritablePositionedInfo)positionedInfo).writePositionedInfo(response, afterBodyContents);
        }

        originalResponse.insert(headContents.toString().length() + 1, afterBodyContents.toString());
    }

    public void writeResponse(HttpServletRequest request,
        HttpServletResponse response) throws IOException
    {
        ResponseWriter writer = new HtmlResponseWriterImpl(response.getWriter(), HtmlRendererUtils.selectContentType(null),
        response.getCharacterEncoding());
        writer.write(originalResponse.toString());
    }
}
