package org.andromda.core.common;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;


/**
 * A utility object useful for reading an HTML string (originating 
 * from the contents of an XMI documentation element) and for translating
 * that string into an HTML paragraph.
 * 
 * <p> The list of paragraphs can be used in a Velocity template
 * to generate JavaDoc documentation for a class, an attribute or a 
 * method. </p>
 * 
 * <p> This is a very simple HTML analyzer class that builds upon the
 * Swing HTMLEditor toolkit. </p>
 *  
 * @author Matthias Bohlen
 *
 */
public class HTMLAnalyzer
{
    /**
     * <p>Translates an HTML string into a list of HTMLParagraphs.</p>
     * @param html the HTML string to be analyzed
     * @return Collection the list of paragraphs found in the HTML string
     * @throws IOException if something goes wrong
     */
    public Collection htmlToParagraphs(String html) throws IOException
    {
        ParserDelegator pd = new ParserDelegator();
        pd.parse(new StringReader(html), new MyParserCallback(), true);
        return paragraphs;
    }

    private ArrayList paragraphs = new ArrayList();

    private class MyParserCallback extends ParserCallback
    {
        private HTMLParagraph currentParagraph = null;

        public void handleSimpleTag(
            Tag tag,
            MutableAttributeSet attribs,
            int pos)
        {
            appendWord("<" + tag + ">");
        }

        public void handleStartTag(
            Tag tag,
            MutableAttributeSet attribs,
            int pos)
        {
            if (tag.equals(Tag.P))
            {
                currentParagraph = new HTMLParagraph(66);
            }
            else
            {
                appendWord("<" + tag + ">");
            }
        }

        public void handleEndTag(Tag tag, int pos)
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

        public void handleText(char[] text, int pos)
        {
            appendText(text);
        }
        
        private void appendWord(String string)
        {
            if (currentParagraph != null)
            {
                currentParagraph.appendWord(string);
            }
        }

        private void appendText(String string)
        {
            if (currentParagraph != null)
            {
                currentParagraph.appendText(string);
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
