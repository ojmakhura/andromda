package org.andromda.maven.site.highlight;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.lang.StringEscapeUtils;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Writer;

public abstract class AbstractHighlightTag extends TagSupport
{
    private String value = null;
    private Writer writer = null;

    protected abstract void highlight(String text);

    public void doTag(XMLOutput xmlOutput) throws MissingAttributeException, JellyTagException
    {
        this.writer = new XMLOutputWriter(xmlOutput);

        String bodyText = getBodyText(true);
        highlight(StringEscapeUtils.unescapeXml(bodyText));
    }

    public Writer getWriter()
    {
        return writer;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public class XMLOutputWriter extends Writer
    {
        private XMLOutput output = null;

        public XMLOutputWriter(XMLOutput output)
        {
            this.output = output;
        }

        public void close() throws IOException
        {
            output.close();
        }

        public void flush() throws IOException
        {
            output.flush();
        }

        public void write(char cbuf[], int off, int len) throws IOException
        {
            try
            {
                output.write(new String(cbuf, off, len));
            }
            catch (SAXException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
