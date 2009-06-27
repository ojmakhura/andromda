package org.andromda.maven.doxia.module.xdoc;

/*
 * Based on The Apache Software Foundation XdocSink
 */

import org.apache.maven.doxia.module.HtmlTools;
import org.apache.maven.doxia.module.apt.AptParser;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkAdapter;
import org.apache.maven.doxia.sink.StructureSink;
import org.apache.maven.doxia.util.LineBreaker;
import org.codehaus.plexus.util.StringUtils;

import java.io.Writer;

/**
 * A doxia Sink which produces an xdoc model.
 *
 * Based taken from Apache Foundation Doxia Project.
 * 
 * @version $Id: AndromdadocSink.java,v 1.1.2.5 2008-03-01 14:37:21 vancek Exp $
 * 
 * @plexus.component role="org.apache.maven.doxia.sink.Sink" role-hint="andromdadoc"
 * 
 * This sink is not being detected correctly at this stage.  All callbacks are invoked from
 * the xhtml doxia sink.
 */
public class AndromdadocSink extends SinkAdapter
{
    private static final String EOL = System.getProperty("line.separator");

    private LineBreaker out;

    private StringBuffer buffer = new StringBuffer();

    private boolean headFlag;

    private int itemFlag;

    private boolean boxedFlag;

    private boolean verbatimFlag;

    private int[] cellJustif;

    private int cellCount;

    private String section;

    private Sink xhtmlSink;

    public AndromdadocSink(Writer out)
    {
        this.out = new LineBreaker(out);
    }

    public AndromdadocSink(Sink xhtmlSink)
    {
        this.xhtmlSink = xhtmlSink;
    }

    protected void resetState()
    {
        headFlag = false;
        buffer = new StringBuffer();
        itemFlag = 0;
        boxedFlag = false;
        verbatimFlag = false;
        cellJustif = null;
        cellCount = 0;
    }

    public void head()
    {
        resetState();

        headFlag = true;

        markup("<?xml version=\"1.0\" ?>" + EOL);

        markup("<document>" + EOL);

        markup("<properties>" + EOL);
    }

    public void head_()
    {
        headFlag = false;

        markup("</properties>" + EOL);
    }

    public void title_()
    {
        if (buffer.length() > 0)
        {
            markup("<title>");
            content(buffer.toString());
            markup("</title>" + EOL);
            buffer = new StringBuffer();
        }
    }

    public void author_()
    {
        if (buffer.length() > 0)
        {
            markup("<author>");
            content(buffer.toString());
            markup("</author>" + EOL);
            buffer = new StringBuffer();
        }
    }

    public void date_()
    {
        if (buffer.length() > 0)
        {
            markup("<date>");
            content(buffer.toString());
            markup("</date>");
            buffer = new StringBuffer();
        }
    }

    public void body()
    {
        markup("<body>" + EOL);
    }

    public void body_()
    {
        markup("</body>" + EOL);

        markup("</document>" + EOL);

        out.flush();

        resetState();
    }

    public void div(String styleClass)
    {
        markup("<div class=\"" + styleClass + "\">");
    }
    
    public void div_()
    {
        markup("</div>");
    }
    
    public void section1()
    {
        this.div("section");
    }

    public void section2()
    {
        this.div("subsection");
    }

    public void section3()
    {
        this.div("subsection");
    }

    public void section4()
    {
       this.div("subsection");
    }

    public void section5()
    {
        this.div("subsection");
    }

    public void subsection(String name)
    {
        this.div("subsection");
        xhtmlSink.sectionTitle2();
        xhtmlSink.text(name);
        xhtmlSink.sectionTitle2_();
    }

    public void sectionTitle()
    {
        markup("<" + section + " name=\"");
    }

    public void sectionTitle_()
    {
        markup("\">");
    }

    public void section1_()
    {
        this.div_();
    }

    public void section2_()
    {
        this.div_();
    }

    public void section3_()
    {
        this.div_();
    }

    public void section4_()
    {
        this.div_();
    }

    public void section5_()
    {
        this.div_();
    }

    public void list()
    {
        markup("<ul>" + EOL);
    }

    public void list(String styleClass)
    {
        if (StringUtils.isNotEmpty(styleClass))
        {
            markup("<ul class=\"" + styleClass + "\">");
        }
        else
        {
            this.list();
        }
    }

    public void list_()
    {
        markup("</ul>");
    }

    public void listItem()
    {
        markup("<li>");
        //itemFlag++;
        // What follows is at least a paragraph.
    }

    public void listItem(String styleClass)
    {
        if (StringUtils.isNotEmpty(styleClass))
        {
            markup("<li class=\"" + styleClass + "\">");
            //itemFlag++;
            // What follows is at least a paragraph
        }
        else
        {
            this.listItem();
        }
    }

    public void listItem_()
    {
        markup("</li>" + EOL);
        //itemFlag--;
    }

    public void numberedList(int numbering)
    {
        String style;
        switch (numbering)
        {
            case NUMBERING_UPPER_ALPHA:
                style = "upper-alpha";
                break;
            case NUMBERING_LOWER_ALPHA:
                style = "lower-alpha";
                break;
            case NUMBERING_UPPER_ROMAN:
                style = "upper-roman";
                break;
            case NUMBERING_LOWER_ROMAN:
                style = "lower-roman";
                break;
            case NUMBERING_DECIMAL:
            default:
                style = "decimal";
        }
        markup("<ol style=\"list-style-type: " + style + "\">" + EOL);
    }

    public void numberedList_()
    {
        markup("</ol>");
    }

    public void numberedListItem()
    {
        markup("<li>");
        //itemFlag++;
        // What follows is at least a paragraph.
    }

    public void numberedListItem_()
    {
        markup("</li>" + EOL);
    }

    public void definitionList()
    {
        markup("<dl compact=\"compact\">" + EOL);
    }

    public void definitionList_()
    {
        markup("</dl>");
    }

    public void definedTerm()
    {
        markup("<dt><b>");
    }

    public void definedTerm_()
    {
        markup("</b></dt>" + EOL);
    }

    public void definition()
    {
        markup("<dd>");
        //itemFlag++;
        // What follows is at least a paragraph.
    }

    public void definition_()
    {
        markup("</dd>" + EOL);
    }

    public void paragraph()
    {
        if (itemFlag == 0)
        {
            markup("<p>");
        }
    }

    public void paragraph(String styleClass)
    {
        if (itemFlag == 0)
        {
            if (styleClass != null)
            {
                markup("<p class=\"" + styleClass + "\">");
            }
            else
            {
                markup("<p>");
            }
        }
    }

    public void paragraph_()
    {
        if (itemFlag == 0)
        {
            markup("</p>");
        }
        else
        {
            itemFlag--;
        }

    }

    public void verbatim(boolean boxed)
    {
        verbatimFlag = true;
        boxedFlag = boxed;
        markup("<div class=\"source\"><pre>");
    }

    public void verbatim_()
    {
        markup("</pre></div>");
        verbatimFlag = false;
        boxedFlag = false;
    }

    public void horizontalRule()
    {
        markup("<hr />");
    }

    public void table()
    {
        markup("<table align=\"center\">" + EOL);
    }

    public void table(String styleClass)
    {
        if (StringUtils.isNotEmpty(styleClass))
        {
            markup("<table class=\"" + styleClass + "\">" + EOL);
        }
        else
        {
            this.table();
        }
    }
    
    public void table_()
    {
        markup("</table>" + EOL);
    }

    public void tableRows(int[] justification, boolean grid)

    {
        markup("<table align=\"center\" border=\"" + (grid ? 1 : 0) + "\">" + EOL);
        this.cellJustif = justification;
    }

    public void tableRows_()
    {
        markup("</table>" + EOL);
    }

    public void tableRow()
    {
        markup("<tr valign=\"top\">" + EOL);
        cellCount = 0;
    }

    public void tableRow(String styleClass)
    {
        if (StringUtils.isNotEmpty(styleClass))
        {
            markup("<tr class=\"" + styleClass + "\">" + EOL);
        }
        else
        {
            this.tableRow();
        }
        cellCount = 0;
    }
    
    public void tableRow_()
    {
        markup("</tr>" + EOL);
        cellCount = 0;
    }

    public void tableCell()
    {
        tableCell(false);
    }

    public void tableHeaderCell()
    {
        tableCell(true);
    }

    public void tableHeaderCell(String width, String styleClass)
    {
        if (StringUtils.isNotEmpty(styleClass) || StringUtils.isNotEmpty(width))
        {
            markup("<th");

            if (StringUtils.isNotEmpty(styleClass))
            {
                markup(" class=\"" + styleClass + "\"");
            }

            if (StringUtils.isNotEmpty(width))
            {
                markup(" width=\"" + width + "\"");
            }
            markup(">" + EOL);
        }
        else
        {
            this.tableHeaderCell();
        }
    }

    public void tableCell(boolean headerRow)
    {
        String justif = null;

        if (cellJustif != null)
        {
            switch (cellJustif[cellCount])
            {
                case AptParser.JUSTIFY_LEFT:
                    justif = "left";
                    break;
                case AptParser.JUSTIFY_RIGHT:
                    justif = "right";
                    break;
                case AptParser.JUSTIFY_CENTER:
                default:
                    justif = "center";
                    break;
            }
        }

        if (justif != null)
        {
            markup("<t" + (headerRow ? 'h' : 'd') + " align=\"" + justif + "\">" + EOL);
        }
        else
        {
            markup("<t" + (headerRow ? 'h' : 'd') + ">" + EOL);
        }
    }

    public void tableCell(String width, String styleClass, String colspan)
    {
        if (StringUtils.isNotEmpty(styleClass) || 
                StringUtils.isNotEmpty(width) || 
                (StringUtils.isNotEmpty(colspan) && StringUtils.isNumeric(colspan)))
        {
            markup("<td");
            if (StringUtils.isNotEmpty(styleClass))
            {
                markup(" class=\"" + styleClass + "\"");
            }
            if (StringUtils.isNotEmpty(width))
            {
                markup(" width=\"" + width + "\"");
            }
            if (StringUtils.isNotEmpty(colspan))
            {
                markup(" colspan=\"" + colspan + "\"");
            }
            markup(">" + EOL);
        }
        else
        {
            this.tableCell();
        }
    }

    public void tableCell_()
    {
        tableCell_(false);
    }

    public void tableHeaderCell_()
    {
        tableCell_(true);
    }

    public void tableCell_(boolean headerRow)
    {
        markup("</t" + (headerRow ? 'h' : 'd') + ">" + EOL);
        ++cellCount;
    }

    public void tableCaption()
    {
        markup("<p><i>");
    }

    public void tableCaption_()
    {
        markup("</i></p>");
    }

    public void anchor(String name)
    {
        if (!headFlag)
        {
            String id = StructureSink.linkToKey(name);
            markup("<a id=\"" + id + "\" name=\"" + id + "\">" + EOL);
        }
    }

    public void anchor(String styleClass, String name)
    {
        if (StringUtils.isNotEmpty(name))
        {
            markup("<a name=\"" + name + "\"");

            if (StringUtils.isNotEmpty(styleClass))
            {
                markup(" class=\"" + styleClass + "\"");
            }
            else
            {
                final String id = StructureSink.linkToKey(name);
                markup(" id=\"" + id + "\"");
            }
            markup(">" + EOL);
        }
        else
        {
            this.anchor(name);
        }
    }

    public void anchor_()
    {
        if (!headFlag)
        {
            markup("</a>" + EOL);
        }
    }

    public void link(String name)
    {
        if (!headFlag)
        {
            markup("<a href=\"" + name + "\">" + EOL);
        }
    }

    public void link(
            String styleClass, 
            String target, 
            String href,
            String name)
    {
        if (StringUtils.isNotEmpty(styleClass)
                || StringUtils.isNotEmpty(target)
                || StringUtils.isNotEmpty(href)
                || StringUtils.isNotEmpty(name))
        {
            markup("<a");

            if (StringUtils.isNotEmpty(styleClass))
            {
                markup(" class=\"" + styleClass + "\"");
            }
            if (StringUtils.isNotEmpty(target))
            {
                markup(" target=\"" + target + "\"");

                /**
                 * Set the class style to newWindow if it's a new window and no style is specified
                 */
                if (target.equalsIgnoreCase("_blank")
                        && StringUtils.isEmpty(styleClass))
                {
                    markup(" class=\"newWindow\" title=\"New Window\"");
                }
            }
            if (StringUtils.isNotEmpty(name))
            {
                markup(" name=\"" + name + "\"");
            }
            if (StringUtils.isNotEmpty(href))
            {
                markup(" href=\"" + href + "\"");
            }
            markup(">" + EOL);
        }
        else
        {
            this.link(href);
        }
    }

    public void link_()
    {
        if (!headFlag)
        {
            markup("</a>" + EOL);
        }
    }

    public void italic()
    {
        if (!headFlag)
        {
            markup("<i>");
        }
    }

    public void italic_()
    {
        if (!headFlag)
        {
            markup("</i>");
        }
    }

    public void bold()
    {
        if (!headFlag)
        {
            markup("<b>");
        }
    }

    public void bold_()
    {
        if (!headFlag)
        {
            markup("</b>");
        }
    }

    public void monospaced()
    {
        if (!headFlag)
        {
            markup("<tt>");
        }
    }

    public void monospaced_()
    {
        if (!headFlag)
        {
            markup("</tt>");
        }
    }

    public void lineBreak()
    {
        if (headFlag)
        {
            buffer.append(EOL);
        }
        else
        {
            markup("<br />");
        }
    }

    public void nonBreakingSpace()
    {
        if (headFlag)
        {
            buffer.append(' ');
        }
        else
        {
            markup("&#160;");
        }
    }

    public void text(String text)
    {
        if (headFlag)
        {
            buffer.append(text);
        }
        else
        {
            if (verbatimFlag)
            {
                verbatimContent(text);
            }
            else
            {
                content(text);
            }
        }
    }

    // ----------------------------------------------------------------------
    //
    // ----------------------------------------------------------------------

    protected void markup(String text)
    {
        if (xhtmlSink != null)
        {
            xhtmlSink.rawText(text);
        }
        else
        {
            out.write(text, true);
        }
    }

    protected void content(String text)
    {
        if (xhtmlSink != null)
        {
            xhtmlSink.rawText(escapeHTML(text));
        }
        else
        {
            out.write(escapeHTML(text), false);
        }
    }

    protected void verbatimContent(String text)
    {
        if (xhtmlSink != null)
        {
            xhtmlSink.rawText(escapeHTML(text));
        }
        else
        {
            out.write(escapeHTML(text), true);
        }
    }

    public static String escapeHTML(String text)
    {
        return HtmlTools.escapeHTML(text);
    }

    public static String encodeURL(String text)
    {
        return HtmlTools.encodeURL(text);
    }

    public void flush()
    {
        if (xhtmlSink != null)
        {
            xhtmlSink.flush();
        }
        else
        {
            out.flush();
        }
    }

    public void close()
    {
        if (xhtmlSink != null)
        {
            xhtmlSink.close();
        }
        else
        {
            out.close();
        }
    }
}
