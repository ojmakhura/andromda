package org.andromda.maven.site.highlight.java;

import junit.framework.TestCase;

import java.io.StringWriter;
import java.io.StringReader;

import org.apache.commons.lang.StringEscapeUtils;

public class JavaHighlighterTest extends TestCase
{
    private JavaHighlighter highlighter = null;
    private final JavaHighlightStyles styles = new TestStyles();

    public JavaHighlighterTest(String name)
    {
        super(name);
        this.highlighter = new JavaHighlighter(styles);
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
        assertEquals("Object[] values = {<div class=\""+styles.getStringLiteralClass()+"\">\"1\"</div>," +
                     "<div class=\""+styles.getStringLiteralClass()+"\">\"2\"</div>," +
                     "<div class=\""+styles.getStringLiteralClass()+"\">\"3\"</div>," +
                     "<div class=\""+styles.getStringLiteralClass()+"\">\"4\"</div>};",
                internalTest("Object[] values = {\"1\",\"2\",\"3\",\"4\"};"));
        assertEquals("<div class=\""+styles.getStringLiteralClass()+"\">'test'</div>",
                internalTest("'test'"));
        assertEquals("<div class=\""+styles.getStringLiteralClass()+"\">\"test * /\"</div>",
                internalTest("\"test * /\""));
    }

    public void testKeywords() throws Exception
    {
        assertEquals("<div class=\""+styles.getKeywordClass()+"\">public</div> ",
                internalTest("public "));
        assertEquals(" <div class=\""+styles.getKeywordClass()+"\">public</div> ",
                internalTest(" public "));
    }

    public void testKeywordsLookalikes() throws Exception
    {
        assertEquals(" form ", internalTest(" form "));
        assertEquals(" xfor ", internalTest(" xfor "));
        assertEquals(" xform ", internalTest(" xform "));
    }

    public void testComments() throws Exception
    {
        assertEquals("<div class=\""+styles.getCommentClass()+"\">/* block comment */</div>",
                internalTest("/* block comment */"));
        assertEquals("<div class=\""+styles.getCommentClass()+"\">/* block \n comment */</div>",
                internalTest("/* block \n comment */"));
        assertEquals("<div class=\""+styles.getCommentClass()+"\">// line comment</div>",
                internalTest("// line comment"));
        assertEquals("<div class=\""+styles.getCommentClass()+"\">// line comment</div>\nno comment",
                internalTest("// line comment\nno comment"));
    }

    public void testTextKeywordCommentMixed() throws Exception
    {
        assertEquals("<div class=\""+styles.getKeywordClass()+"\">do</div> not go <div class=\""+
                     styles.getKeywordClass()+"\">public</div> with <div class=\""+
                     styles.getKeywordClass()+"\">this</div>, " +
                     "Jack <div class=\""+styles.getCommentClass()+"\">// line comment</div>",
                internalTest("do not go public with this, Jack // line comment"));
        assertEquals("what is <div class=\""+styles.getKeywordClass()+"\">this</div> " +
                     "<div class=\""+styles.getCommentClass()+"\">/* no comment */</div> " +
                     "doing <div class=\""+styles.getKeywordClass()+"\">for</div> you ?",
                internalTest("what is this /* no comment */ doing for you ?"));
    }

    private String internalTest(String s) throws Exception
    {
        final StringWriter writer = new StringWriter();
        highlighter.highlight(new StringReader(s), writer);
        return StringEscapeUtils.unescapeXml(writer.getBuffer().toString());
    }

    public final class TestStyles implements JavaHighlightStyles
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
