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
 * A utility object useful for reading an HTML string (originating from the
 * contents of an XMI documentation element) and for translating that string
 * into an HTML paragraph.
 * 
 * <p>
 * The list of paragraphs can be used in a VelocityTemplateEngine template to generate
 * JavaDoc documentation for a class, an attribute or a method.
 * </p>
 * 
 * <p>
 * This is a very simple HTML analyzer class that builds upon the Swing
 * HTMLEditor toolkit.
 * </p>
 * 
 * @author Matthias Bohlen
 * @author Chad Brandon
 *  
 */
public class HTMLAnalyzer
{
    
    /**
     * Specifes the line width to enforce.
     */
    private int lineLength;
    
    /**
     * The default line width used, if none is specified.
     */
    private static final int DEFAULT_LINE_WIDTH = 66;
    
    /**
     * Constructs a new instance of this HTMLAnalyzer
     * using the default line width.
     */
    public HTMLAnalyzer() 
    {
        this(DEFAULT_LINE_WIDTH);    
    }
    
    /**
     * Constructs a new instance of this HTMLAnalyzer
     * specifying the <code>lineLength</code> to enforce.
     * 
     * @param lineLength the width of the lines before they are wrapped.
     */
    public HTMLAnalyzer(int lineLength) 
    {
        this.lineLength = lineLength;
    }
    
    /**
     * <p>
     * Translates an HTML string into a list of HTMLParagraphs.
     * </p>
     * 
     * @param html the HTML string to be analyzed
     * @return Collection the list of paragraphs found in the HTML string
     * @throws IOException if something goes wrong
     */
    public Collection htmlToParagraphs(String html) throws IOException
    {
        ParserDelegator pd = new ParserDelegator();
        html = StringEscapeUtils.escapeHtml(html);
        pd.parse(new StringReader(html), new MyParserCallback(), true);
        return paragraphs;
    }
    
    private ArrayList paragraphs = new ArrayList();
    
    private class MyParserCallback extends ParserCallback
    {
        private HTMLParagraph currentParagraph = null;
        private HTMLParagraph nonHtmlParagraph = null;
        
        /**
         * @see javax.swing.text.html.HTMLEditorKit.ParserCallback#handleSimpleTag(javax.swing.text.html.HTML.Tag, javax.swing.text.MutableAttributeSet, int)
         */
        public void handleSimpleTag(Tag tag, MutableAttributeSet attribs, int pos)
        {
            appendWord("<" + tag + ">");
        }
        
        /**
         * @see javax.swing.text.html.HTMLEditorKit.ParserCallback#handleStartTag(javax.swing.text.html.HTML.Tag, javax.swing.text.MutableAttributeSet, int)
         */
        public void handleStartTag(Tag tag, MutableAttributeSet attribs, int pos)
        {
            if (tag.equals(Tag.P))
            {
                currentParagraph = new HTMLParagraph(lineLength);
            } else
            {
                appendWord("<" + tag + ">");
            }
        }
        
        /**
         * @see javax.swing.text.html.HTMLEditorKit.ParserCallback#handleEndTag(javax.swing.text.html.HTML.Tag, int)
         */
        public void handleEndTag(Tag tag, int pos)
        {
            if (tag.equals(Tag.P))
            {
                paragraphs.add(currentParagraph);
                currentParagraph = null;
            } else
            {
                appendWord("</" + tag + ">");
            }
        }
        
        /**
         * @see javax.swing.text.html.HTMLEditorKit.ParserCallback#handleText(char[], int)
         */
        public void handleText(char[] text, int pos)
        {
            //handle instances where we may not have html in the text.
            if (currentParagraph == null)
            {
                nonHtmlParagraph = new HTMLParagraph(lineLength);
                nonHtmlParagraph.appendText(new String(text));
                paragraphs.add(nonHtmlParagraph);
            } else
            {
                appendText(text);
            }
        }
        
        private void appendWord(String string)
        {
            if (currentParagraph != null)
            {
                currentParagraph.appendWord(string);
            }
        }
        
        private void appendText(char[] text)
        {
            if (currentParagraph != null)
            {
                currentParagraph.appendText(new String(text));
            }
        }
    }
    
}
