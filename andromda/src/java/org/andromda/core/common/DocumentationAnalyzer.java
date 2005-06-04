package org.andromda.core.common;

import java.io.IOException;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import org.apache.commons.lang.StringEscapeUtils;


/**
 * A utility object useful for reading an documentation string (originating from the contents of an XMI documentation
 * element) and for translating that string into a paragraph.
 * <p/>
 * The list of paragraphs can be used in a VelocityTemplateEngine template to generate JavaDoc documentation for a
 * class, an attribute or a method. </p>
 *
 * @author Matthias Bohlen
 * @author Chad Brandon
 */
public class DocumentationAnalyzer
{
    /**
     * Specifes the line width to enforce. Note: this member is protected to improve access performance within inner
     * class {@link HTMLParserCallback}
     */
    protected int lineLength;

    /**
     * The default line width used, if none is specified.
     */
    private static final int DEFAULT_LINE_WIDTH = 66;

    /**
     * Constructs a new instance of this DocumentationAnalyzer using the default line width.
     */
    public DocumentationAnalyzer()
    {
        this(DEFAULT_LINE_WIDTH);
    }

    /**
     * Constructs a new instance of this DocumentationAnalyzer specifying the <code>lineLength</code> to enforce.
     *
     * @param lineLength the width of the lines before they are wrapped.
     */
    public DocumentationAnalyzer(final int lineLength)
    {
        this.lineLength = lineLength;
    }

    /**
     * <p/>
     * Translates a string into a list of Paragraphs. </p>
     *
     * @param line the line of documentation to be analyzed
     * @param html whether or not HTML should be handled.
     * @return Collection the list of paragraphs found in the text string
     * @throws IOException if something goes wrong
     */
    public Collection toParagraphs(
        String line,
        final boolean html)
        throws IOException
    {
        final ParserDelegator delegator = new ParserDelegator();
        if (html)
        {
            line = StringEscapeUtils.escapeHtml(line);
        }
        delegator.parse(
            new StringReader(line),
            new HTMLParserCallback(),
            true);
        return paragraphs;
    }

    // protected to increase performance of inner class
    // access
    protected ArrayList paragraphs = new ArrayList();

    /**
     * The the call back.
     */
    private final class HTMLParserCallback
        extends ParserCallback
    {
        private Paragraph currentParagraph = null;
        private Paragraph nonHtmlParagraph = null;

        /**
         * @see javax.swing.text.html.HTMLEditorKit.ParserCallback#handleSimpleTag(javax.swing.text.html.HTML.Tag,
         *      javax.swing.text.MutableAttributeSet, int)
         */
        public void handleSimpleTag(
            Tag tag,
            MutableAttributeSet attribs,
            int pos)
        {
            appendWord("<" + tag + ">");
        }

        /**
         * @see javax.swing.text.html.HTMLEditorKit.ParserCallback#handleStartTag(javax.swing.text.html.HTML.Tag,
         *      javax.swing.text.MutableAttributeSet, int)
         */
        public void handleStartTag(
            Tag tag,
            MutableAttributeSet attribs,
            int pos)
        {
            if (tag.equals(Tag.P))
            {
                currentParagraph = new Paragraph(lineLength);
            }
            else
            {
                appendWord("<" + tag + ">");
            }
        }

        /**
         * @see javax.swing.text.html.HTMLEditorKit.ParserCallback#handleEndTag(javax.swing.text.html.HTML.Tag, int)
         */
        public void handleEndTag(
            Tag tag,
            int pos)
        {
            if (tag.equals(Tag.P))
            {
                paragraphs.add(currentParagraph);
                currentParagraph = null;
            }
            else
            {
                appendWord("</" + tag + ">");
            }
        }

        /**
         * @see javax.swing.text.html.HTMLEditorKit.ParserCallback#handleText(char[], int)
         */
        public void handleText(
            char[] text,
            int pos)
        {
            // handle instances where we may not have html in the text.
            if (currentParagraph == null)
            {
                nonHtmlParagraph = new Paragraph(lineLength);
                nonHtmlParagraph.appendText(new String(text));
                paragraphs.add(nonHtmlParagraph);
            }
            else
            {
                appendText(text);
            }
        }

        private final void appendWord(String string)
        {
            if (currentParagraph != null)
            {
                currentParagraph.appendWord(string);
            }
        }

        private final void appendText(char[] text)
        {
            if (currentParagraph != null)
            {
                currentParagraph.appendText(new String(text));
            }
        }
    }
}