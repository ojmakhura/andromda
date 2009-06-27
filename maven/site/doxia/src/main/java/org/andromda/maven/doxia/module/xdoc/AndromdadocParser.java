package org.andromda.maven.doxia.module.xdoc;

/*
 * Based on The Apache Software Foundation XdocParser
 */

import org.apache.maven.doxia.macro.MacroRequest;
import org.apache.maven.doxia.parser.AbstractParser;
import org.apache.maven.doxia.parser.ParseException;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.StructureSink;
import org.codehaus.plexus.util.xml.pull.MXParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.StringUtils;

import java.io.Reader;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Parse an xdoc model and emit events into the specified doxia
 * Sink.
 * 
 * Based taken from Apache Foundation Doxia Project.
 *
 * @version $Id: AndromdadocParser.java,v 1.1.2.5 2008-03-01 14:37:21 vancek Exp $
 * 
 * @plexus.component role="org.apache.maven.doxia.parser.Parser" role-hint="andromdadoc"
 */
public class AndromdadocParser extends AbstractParser
{
    public void parse(Reader reader, Sink sink) 
        throws ParseException
    {
        try
        {
            XmlPullParser parser = new MXParser();

            parser.setInput(reader);

            parseXdoc(parser, sink);
        }
        catch (Exception ex)
        {
            throw new ParseException("Error parsing the model.", ex);
        }
    }

    public void parseXdoc(XmlPullParser parser, Sink sink) 
        throws Exception
    {
        /**
         * Because the AndromdadocSink is not currently detected, explicity instantiate it here
         */
        final AndromdadocSink andromdaSink = new AndromdadocSink(sink);
        
        String sourceLanguage = null;

        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            if (eventType == XmlPullParser.START_TAG)
            {
                if (parser.getName().equals("document"))
                {
                    //Do nothing
                }
                else if (parser.getName().equals("title"))
                {
                    sink.title();
                }
                else if (parser.getName().equals("author"))
                {
                    sink.author();
                }
                else if (parser.getName().equals("body"))
                {
                    sink.body();
                }
                else if (parser.getName().equals("section"))
                {
                    final String name = parser.getAttributeValue(null, "name");
                    sink.anchor(name);
                    sink.anchor_();
                    sink.section1();
                    sink.sectionTitle1();
                    sink.text(name);
                    sink.sectionTitle1_();
                }
                else if (parser.getName().equals("subsection"))
                {
                    /**
                     * sink.section2 invokes the callback in the xhtml sink - part of 
                     * the doxia modules package - instead we override that locally.
                     */
                    final String name = parser.getAttributeValue(null, "name");
                    sink.anchor(name);
                    sink.anchor_();
                    andromdaSink.subsection(name);
                }
                else if (parser.getName().equals("p"))
                {
                    final String styleClass = parser.getAttributeValue(null, "class");
                    andromdaSink.paragraph(styleClass);
                }
                else if (parser.getName().equals("source"))
                {
                    sourceLanguage = parser.getAttributeValue(null, "language");
                    andromdaSink.verbatim(true);
                }
                else if (parser.getName().equals("ul"))
                {
                    final String styleClass = parser.getAttributeValue(null, "class");
                    andromdaSink.list(styleClass);
                }
                else if (parser.getName().equals("ol"))
                {
                    andromdaSink.numberedList(Sink.NUMBERING_DECIMAL);
                }
                else if (parser.getName().equals("li"))
                {
                    final String styleClass = parser.getAttributeValue(null, "class");
                    andromdaSink.listItem(styleClass);
                }
                else if (parser.getName().equals("properties"))
                {
                    sink.head();
                }
                else if (parser.getName().equals("b"))
                {
                    sink.bold();
                }
                else if (parser.getName().equals("i"))
                {
                    sink.italic();
                }
                else if (parser.getName().equals("a"))
                {
                    final String styleClass = parser.getAttributeValue(null, "class");
                    final String target = parser.getAttributeValue(null, "target");
                    final String href = parser.getAttributeValue(null, "href");
                    final String name = parser.getAttributeValue(null, "name");

                    if (StringUtils.isNotEmpty(styleClass)
                            || StringUtils.isNotEmpty(target)
                            || StringUtils.isNotEmpty(href))
                    {
                        andromdaSink.link(styleClass, target, href, name);
                    }
                    else
                    {
                        if (StringUtils.isNotEmpty(name))
                        {
                            andromdaSink.anchor(styleClass, name);
                        }
                        else
                        {
                            handleRawText(sink, parser);
                        }
                    }
                }
                else if (parser.getName().equals("macro"))
                {
                    String macroId = parser.getAttributeValue(null, "id");

                    int count = parser.getAttributeCount();

                    Map parameters = new HashMap();

                    for (int i = 1; i < count; i++)
                    {
                        parameters.put(parser.getAttributeName(i), parser.getAttributeValue(i));
                    }

                    MacroRequest request = new MacroRequest(parameters);

                    executeMacro(macroId, request, sink);
                }

                // ----------------------------------------------------------------------
                // Tables
                // ----------------------------------------------------------------------

                else if (parser.getName().equals("table"))
                {
                    final String styleClass = parser.getAttributeValue(null, "class");
                    andromdaSink.table(styleClass);
                }
                else if (parser.getName().equals("tr"))
                {
                    final String styleClass = parser.getAttributeValue(null, "class");
                    andromdaSink.tableRow(styleClass);
                }
                else if (parser.getName().equals("th"))
                {
                    final String width = parser.getAttributeValue(null, "width");
                    final String styleClass = parser.getAttributeValue(null, "class");
                    andromdaSink.tableHeaderCell(width, styleClass);
                }
                else if (parser.getName().equals("td"))
                {
                    String width = parser.getAttributeValue(null, "width");
                    String styleClass = parser.getAttributeValue(null, "class");
                    String colspan = parser.getAttributeValue(null, "colspan");
                    andromdaSink.tableCell(width, styleClass, colspan);
                }
                else
                {
                    handleRawText(sink, parser);
                }
            }
            else if (eventType == XmlPullParser.END_TAG)
            {
                if (parser.getName().equals("document"))
                {
                    //Do nothing
                }
                else if (parser.getName().equals("title"))
                {
                    sink.title_();
                }
                else if (parser.getName().equals("author"))
                {
                    sink.author_();
                }
                else if (parser.getName().equals("body"))
                {
                    sink.body_();
                }
                else if (parser.getName().equals("p"))
                {
                    // sink.paragraph_();
                    andromdaSink.paragraph_();
                }
                else if (parser.getName().equals("source"))
                {
                    andromdaSink.verbatim_();
                    sourceLanguage = null;
                }
                else if (parser.getName().equals("ul"))
                {
                    andromdaSink.list_();
                }
                else if (parser.getName().equals("ol"))
                {
                    andromdaSink.numberedList_();
                }
                else if (parser.getName().equals("li"))
                {
                    andromdaSink.listItem_();
                }
                else if (parser.getName().equals("properties"))
                {
                    sink.head_();
                }
                else if (parser.getName().equals("b"))
                {
                    sink.bold_();
                }
                else if (parser.getName().equals("i"))
                {
                    sink.italic_();
                }
                else if (parser.getName().equals("a"))
                {
                    // TODO: Note there will be badness if link_ != anchor != </a>
                    andromdaSink.link_();
                }

                // ----------------------------------------------------------------------
                // Tables
                // ----------------------------------------------------------------------

                else if (parser.getName().equals("table"))
                {
                    andromdaSink.table_();
                }
                else if (parser.getName().equals("tr"))
                {
                    andromdaSink.tableRow_();
                }
                else if (parser.getName().equals("th"))
                {
                    andromdaSink.tableHeaderCell_();
                }
                else if (parser.getName().equals("td"))
                {
                    andromdaSink.tableCell_();
                }

                // ----------------------------------------------------------------------
                // Sections
                // ----------------------------------------------------------------------

                else if (parser.getName().equals("section"))
                {
                    sink.section1_();
                }
                else if (parser.getName().equals("subsection"))
                {
                    sink.section2_();
                }
                else
                {
                    sink.rawText("</");

                    sink.rawText(parser.getName());

                    sink.rawText(">");
                }

                // ----------------------------------------------------------------------
                // Sections
                // ----------------------------------------------------------------------
            }
            else if (eventType == XmlPullParser.TEXT)
            {
                if (StringUtils.isNotEmpty(sourceLanguage))
                {
                    if ("xml".equalsIgnoreCase(sourceLanguage.trim()))
                    {
                        new HighlightXmlSink().highlight(andromdaSink, parser.getText());
                    }
                    else if ("java".equalsIgnoreCase(sourceLanguage.trim()))
                    {
                        new HighlightJavaSink().highlight(andromdaSink, parser.getText());
                    }
                    else if ("velocity".equalsIgnoreCase(sourceLanguage.trim()))
                    {
                        new HighlightVelocitySink().highlight(andromdaSink, parser.getText());
                    }
                }
                else
                {
                    sink.text(parser.getText());
                }
            }

            eventType = parser.next();
        }
    }

    private void handleRawText(Sink sink, XmlPullParser parser)
    {
        sink.rawText("<");

        sink.rawText(parser.getName());

        int count = parser.getAttributeCount();

        for (int i = 0; i < count; i++)
        {
            sink.rawText(" ");

            sink.rawText(parser.getAttributeName(i));

            sink.rawText("=");

            sink.rawText("\"");

            sink.rawText(parser.getAttributeValue(i));

            sink.rawText("\"");
        }

        sink.rawText(">");
    }
}
