package org.andromda.maven.site.highlight.java;

import org.andromda.maven.site.highlight.java.analysis.DepthFirstAdapter;
import org.andromda.maven.site.highlight.java.node.ANumericLiteral;
import org.andromda.maven.site.highlight.java.node.AStringLiteral;
import org.andromda.maven.site.highlight.java.node.EOF;
import org.andromda.maven.site.highlight.java.node.Node;
import org.andromda.maven.site.highlight.java.node.TBlockComment;
import org.andromda.maven.site.highlight.java.node.TEndOfLineComment;
import org.andromda.maven.site.highlight.java.node.Token;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.Writer;

public class JavaHighlightAdapter extends DepthFirstAdapter
{
    private Writer writer = null;
    private JavaHighlightStyles styles = null;

    public JavaHighlightAdapter(Writer writer, JavaHighlightStyles styles)
    {
        this.writer = writer;
        this.styles = styles;
    }

    public void caseTBlockComment(TBlockComment node)
    {
        startHighlight(styles.getCommentClass());
        write(node.getText());
        endHighlight();
    }

    public void caseTEndOfLineComment(TEndOfLineComment node)
    {
        startHighlight(styles.getCommentClass());
        write(node.getText());
        endHighlight();
    }

    public void inAStringLiteral(AStringLiteral node)
    {
        startHighlight(styles.getStringLiteralClass());
    }
    public void outAStringLiteral(AStringLiteral node)
    {
        endHighlight();
    }

    public void inANumericLiteral(ANumericLiteral node)
    {
        startHighlight(styles.getNumericLiteralClass());
    }
    public void outANumericLiteral(ANumericLiteral node)
    {
        endHighlight();
    }

    public void defaultCase(Node node)
    {
        if ((node instanceof Token) && !(node instanceof EOF))
        {
            Token token = (Token)node;
            String text = token.getText();

            for (int i = 0; i < keywords.length; i++)
            {
                // @todo: the div should not we written out escaped
                String keyword = keywords[i];
                text = text.replaceAll(
                        "\\b"+keyword+"\\b",
                        "<div class=\""+styles.getKeywordClass()+"\">" + keyword + "</div>");
            }


            write(text);
        }
    }

    private void write(String string)
    {
        write(string, true);
    }

    private void write(String string, boolean escapeXml)
    {
        try
        {
            if (escapeXml)
            {
                string = StringEscapeUtils.escapeXml(string);
            }
            writer.write(string);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private void startHighlight(String cssClass)
    {
        write("<div class=\"", false);
        write(cssClass, false);
        write("\">", false);
    }

    private void endHighlight()
    {
        write("</div>", false);
    }

    private final String[] keywords = new String[]
            {
                "assert", "abstract", "boolean", "break", "byte",
                "case", "catch", "char", "class", "const", "continue",
                "default", "do", "double", "else", "extends", "final",
                "finally", "float", "for", "goto", "if", "implements",
                "import", "instanceof", "int", "interface", "long",
                "native", "new", "package", "private", "protected",
                "public", "return", "short", "static", "strictfp",
                "super", "switch", "synchronized", "this", "throw",
                "throws", "transient", "try", "void", "volatile", "while",
                "true", "false", "null"
            };
}
