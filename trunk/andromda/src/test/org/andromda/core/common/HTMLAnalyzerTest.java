package org.andromda.core.common;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

/**
 * @author Matthias Bohlen
 *
 */
public class HTMLAnalyzerTest extends TestCase
{

    /**
     * Constructor for HTMLAnalyzerTest.
     * @param arg0
     */
    public HTMLAnalyzerTest(String name)
    {
        super(name);
    }

    public void testHtmlToParagraphs() throws Throwable
    {
        HTMLAnalyzer ha = new HTMLAnalyzer();
        Collection c = ha.htmlToParagraphs("<p>This is a very simple HTML analyzer class that builds upon the"
                            + " Swing <CODE>HTMLEditor</CODE> toolkit.</p>"
                            + "<p>The purpose of this class is to read an HTML string from the"
                            + " contents of an XMI documentation element and translate it to"
                            + " a list of paragraphs.</p>"
                            + "The list of paragraphs can be used in a Velocity template"
                            + " to generate JavaDoc documentation for a class, an attribute or a"
                            + " method.</p>");
        
        for (Iterator it = c.iterator();  it.hasNext(); )
        {
            HTMLParagraph par = (HTMLParagraph) it.next();
            System.out.println ("<p>");
            System.out.print(par);
            System.out.println ("</p>");
        }
    }

}
