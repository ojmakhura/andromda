package org.andromda.maven.site.highlight.xml;

import junit.framework.TestCase;

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.lang.StringEscapeUtils;

public class XmlHighlighterTest extends TestCase
{
    private XmlHighlighter highlighter = null;
    private final XmlHighlightStyles styles = new TestStyles();

    public XmlHighlighterTest(String name)
    {
        super(name);
        highlighter = new XmlHighlighter(styles);
    }

    public void testText() throws Exception
    {
        assertEquals("abc", internalTest("abc"));
        assertEquals(" abc ", internalTest(" abc "));
        assertEquals(" a bb ccc ", internalTest(" a bb ccc "));
    }

    public void testElement() throws Exception
    {
        assertEquals("<<div class=\""+styles.getElementClass()+"\">test</div>>", internalTest("<test>"));
        assertEquals("< <div class=\""+styles.getElementClass()+"\">test</div> >", internalTest("< test >"));
    }

    public void testTextAndSimpleElement() throws Exception
    {
        assertEquals("abc <<div class=\""+styles.getElementClass()+"\">test</div>>",
               internalTest("abc <test>"));
        assertEquals("abc <<div class=\""+styles.getElementClass()+"\">test</div>> xyz",
               internalTest("abc <test> xyz"));
        assertEquals("<<div class=\""+styles.getElementClass()+"\">test</div>>content",
               internalTest("<test>content"));
        assertEquals("<<div class=\""+styles.getElementClass()+"\">test</div>>content</<div class=\""+
                        styles.getElementClass()+"\">test</div>>",
               internalTest("<test>content</test>"));
        assertEquals("<<div class=\""+styles.getElementClass()+"\">test</div>/>content<<div class=\""+
                        styles.getElementClass()+"\">test</div>/>",
               internalTest("<test/>content<test/>"));
        assertEquals("<<div class=\""+styles.getElementClass()+"\">test</div>> con tent </<div class=\""+
                        styles.getElementClass()+"\">test</div>>",
               internalTest("<test> con tent </test>"));
    }

    public void testAttribute() throws Exception
    {
        assertEquals("<<div class=\""+styles.getElementClass()+"\">test</div> <div class=\""+
                    styles.getAttributeClass()+"\">name=</div><div class=\""+
                    styles.getLiteralClass()+"\">\"value\"</div>>",
                internalTest("<test name=\"value\">"));
        assertEquals("<<div class=\""+styles.getElementClass()+"\">test</div> <div class=\""+
                    styles.getAttributeClass()+"\">name =</div> <div class=\""+
                    styles.getLiteralClass()+"\">\"value\"</div>>",
                internalTest("<test name = \"value\">"));
        assertEquals("<<div class=\""+styles.getElementClass()+"\">test</div> \n<div class=\""+
                    styles.getAttributeClass()+"\">name\n =</div> <div class=\""+
                    styles.getLiteralClass()+"\">\"value\"</div>>",
                internalTest("<test \nname\n = \"value\">"));
    }

    public void testCdata() throws Exception
    {
        assertEquals("<div class=\""+styles.getCdataClass()+"\"><![CDATA[abc]]></div>",
                internalTest("<![CDATA[abc]]>"));
        assertEquals(" <div class=\""+styles.getCdataClass()+"\"><![CDATA[abc]]></div> ",
                internalTest(" <![CDATA[abc]]> "));
    }

    public void testCommentInCdata() throws Exception
    {
        assertEquals("<div class=\""+styles.getCdataClass()+"\"><![CDATA[ abc <!-- comment --> ]]></div>",
                       internalTest("<![CDATA[ abc <!-- comment --> ]]>"));
        assertEquals("<div class=\""+styles.getCommentClass()+"\"><!-- <![CDATA[cdata]]> --></div>",
                       internalTest("<!-- <![CDATA[cdata]]> -->"));
    }

/*
    public void testRealSamples() throws Exception
    {
              System.out.println(
                internalTest("<global-forwards>\n" +
                "            ...\n" +
                "            <forward\n" +
                "                name=\"purchase.items\"\n" +
                "                path=\"/PurchaseItems/PurchaseItems.do\"\n" +
                "                redirect=\"false\" />\n" +
                "            ...\n" +
                "        </global-forwards>"));
    }
*/

    private String internalTest(String s) throws Exception
    {
        final StringWriter writer = new StringWriter();
        highlighter.highlight(new StringReader(s), writer);
        return StringEscapeUtils.unescapeXml(writer.getBuffer().toString());
    }

    public final class TestStyles implements XmlHighlightStyles
    {
        public String getCommentClass()
        {
            return "comment";
        }

        public String getCdataClass()
        {
            return "cdata";
        }

        public String getElementClass()
        {
            return "element";
        }

        public String getAttributeClass()
        {
            return "attribute";
        }

        public String getLiteralClass()
        {
            return "literal";
        }
    }
}
