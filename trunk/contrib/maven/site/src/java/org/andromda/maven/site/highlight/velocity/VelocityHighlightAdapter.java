package org.andromda.maven.site.highlight.velocity;

import org.andromda.maven.site.highlight.velocity.analysis.DepthFirstAdapter;
import org.andromda.maven.site.highlight.velocity.node.ANumericLiteral;
import org.andromda.maven.site.highlight.velocity.node.AStringLiteral;
import org.andromda.maven.site.highlight.velocity.node.EOF;
import org.andromda.maven.site.highlight.velocity.node.Node;
import org.andromda.maven.site.highlight.velocity.node.TBlockComment;
import org.andromda.maven.site.highlight.velocity.node.TEndOfLineComment;
import org.andromda.maven.site.highlight.velocity.node.Token;
import org.andromda.maven.site.highlight.velocity.node.TKeyword;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.Writer;

public class VelocityHighlightAdapter extends DepthFirstAdapter
{
    private Writer writer = null;
    private VelocityHighlightStyles styles = null;

    public VelocityHighlightAdapter(Writer writer, VelocityHighlightStyles styles)
    {
        this.writer = writer;
        this.styles = styles;
    }

    public void caseTKeyword(TKeyword node)
    {
        startHighlight(styles.getKeywordClass());
        write(node.getText());
        endHighlight();
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
                "#foreach", "#end", "#if", "#else", "#elseif",
                "#parse", "#macro", "#set", "true", "false"
            };
}

