package org.andromda.maven.site.highlight.xml;

import org.andromda.maven.site.highlight.xml.analysis.DepthFirstAdapter;
import org.andromda.maven.site.highlight.xml.node.AAttributeName;
import org.andromda.maven.site.highlight.xml.node.AAttributeValue;
import org.andromda.maven.site.highlight.xml.node.AElementName;
import org.andromda.maven.site.highlight.xml.node.EOF;
import org.andromda.maven.site.highlight.xml.node.Node;
import org.andromda.maven.site.highlight.xml.node.TCdata;
import org.andromda.maven.site.highlight.xml.node.TComment;
import org.andromda.maven.site.highlight.xml.node.Token;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.Writer;

public class XmlHighlightAdapter extends DepthFirstAdapter
{
    private Writer writer = null;
    private XmlHighlightStyles styles = null;

    public XmlHighlightAdapter(Writer writer, XmlHighlightStyles styles)
    {
        this.writer = writer;
        this.styles = styles;
    }

    public void caseTComment(TComment node)
    {
        startHighlight(styles.getCommentClass());
        write(node.getText());
        endHighlight();
    }

    public void caseTCdata(TCdata node)
    {
        startHighlight(styles.getCdataClass());
        write(node.getText());
        endHighlight();
    }

    public void inAElementName(AElementName node)
    {
        startHighlight(styles.getElementClass());
    }
    public void outAElementName(AElementName node)
    {
        endHighlight();
    }


    public void inAAttributeName(AAttributeName node)
    {
        startHighlight(styles.getAttributeClass());
    }
    public void outAAttributeName(AAttributeName node)
    {
        endHighlight();
    }


    public void inAAttributeValue(AAttributeValue node)
    {
        startHighlight(styles.getLiteralClass());
    }
    public void outAAttributeValue(AAttributeValue node)
    {
        endHighlight();
    }

    public void defaultCase(Node node)
    {
        if ((node instanceof Token) && !(node instanceof EOF))
        {
            Token token = (Token)node;
            write(token.getText());
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
}
