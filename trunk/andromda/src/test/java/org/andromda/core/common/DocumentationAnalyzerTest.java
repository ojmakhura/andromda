package org.andromda.core.common;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;


/**
 * JUnit test for {@link org.andromda.core.common.DocumentationAnalyzer}
 *
 * @author Matthias Bohlen
 */
public class DocumentationAnalyzerTest
    extends TestCase
{
    /**
     * Constructor for HTMLAnalyzerTest.
     *
     * @param name
     */
    public DocumentationAnalyzerTest(String name)
    {
        super(name);
    }

    public void testHtmlToParagraphs()
        throws Throwable
    {
        DocumentationAnalyzer ha = new DocumentationAnalyzer();
        Collection c =
            ha.toParagraphs(
                "<p>This is a very simple HTML analyzer class that builds upon the" +
                " Swing <CODE>HTMLEditor</CODE> toolkit.</p>" +
                "<p>The purpose of this class is to read an HTML string from the" +
                " contents of an XMI documentation element and translate it to" + " a list of paragraphs.</p>" +
                "The list of paragraphs can be used in a VelocityTemplateEngine template" +
                " to generate JavaDoc documentation for a class, an attribute or a" + " method.</p>", true);

        for (final Iterator it = c.iterator(); it.hasNext();)
        {
            Paragraph par = (Paragraph)it.next();
            System.out.println("<p>");
            System.out.print(par);
            System.out.println("</p>");
        }
    }
}