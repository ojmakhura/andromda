package org.andromda.maven.site.highlight.velocity;

import junit.framework.TestCase;

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.lang.StringEscapeUtils;

public class VelocityHighlighterTest extends TestCase
{
    private VelocityHighlighter highlighter = null;
    private final VelocityHighlightStyles styles = new TestStyles();

    public VelocityHighlighterTest(String name)
    {
        super(name);
        this.highlighter = new VelocityHighlighter(styles);
    }

    public void testText() throws Exception
    {
        assertEquals("abc", internalTest("abc"));
        assertEquals(" abc ", internalTest(" abc "));
        assertEquals(" a bb ccc ", internalTest(" a bb ccc "));
    }

    public void testNumericLiterals() throws Exception
    {
        assertEquals("<div class=\""+styles.getNumericLiteralClass()+"\">.234</div>",
                internalTest(".234"));
        assertEquals("<div class=\""+styles.getNumericLiteralClass()+"\">12.234</div>",
                internalTest("12.234"));
        assertEquals("<div class=\""+styles.getNumericLiteralClass()+"\">0x32Ab</div>",
                internalTest("0x32Ab"));
    }

    public void testStringLiterals() throws Exception
    {
        assertEquals("<div class=\""+styles.getStringLiteralClass()+"\">\"1\"</div>," +
                     "<div class=\""+styles.getStringLiteralClass()+"\">\"2\"</div>," +
                     "<div class=\""+styles.getStringLiteralClass()+"\">\"3\"</div>," +
                     "<div class=\""+styles.getStringLiteralClass()+"\">\"4\"</div>",
                internalTest("\"1\",\"2\",\"3\",\"4\""));
        assertEquals("<div class=\""+styles.getStringLiteralClass()+"\">'test'</div>",
                internalTest("'test'"));
        assertEquals("<div class=\""+styles.getStringLiteralClass()+"\">\"test * /\"</div>",
                internalTest("\"test * /\""));
    }

    public void testKeywords() throws Exception
    {
        assertEquals("<div class=\""+styles.getKeywordClass()+"\">#foreach</div>", internalTest("#foreach"));
        assertEquals("<div class=\""+styles.getKeywordClass()+"\">#end</div>", internalTest("#end"));
        assertEquals("<div class=\""+styles.getKeywordClass()+"\">#if</div>", internalTest("#if"));
        assertEquals("<div class=\""+styles.getKeywordClass()+"\">#else</div>", internalTest("#else"));
        assertEquals("<div class=\""+styles.getKeywordClass()+"\">#elseif</div>", internalTest("#elseif"));
        assertEquals("<div class=\""+styles.getKeywordClass()+"\">#parse</div>", internalTest("#parse"));
        assertEquals("<div class=\""+styles.getKeywordClass()+"\">#macro</div>", internalTest("#macro"));
        assertEquals("<div class=\""+styles.getKeywordClass()+"\">#set</div>", internalTest("#set"));
        assertEquals("<div class=\""+styles.getKeywordClass()+"\">true</div>", internalTest("true"));
        assertEquals("<div class=\""+styles.getKeywordClass()+"\">false</div>", internalTest("false"));
    }

    private String internalTest(String s) throws Exception
    {
        final StringWriter writer = new StringWriter();
        highlighter.highlight(new StringReader(s), writer);
        return StringEscapeUtils.unescapeXml(writer.getBuffer().toString());
    }

    public final class TestStyles implements VelocityHighlightStyles
    {
        public String getCommentClass()
        {
            return "comment";
        }

        public String getKeywordClass()
        {
            return "keyword";
        }

        public String getStringLiteralClass()
        {
            return "string";
        }

        public String getNumericLiteralClass()
        {
            return "numeric";
        }
    }

}
