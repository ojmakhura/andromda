package org.andromda.maven.site.highlight.xml;

import org.andromda.maven.site.highlight.xml.lexer.Lexer;
import org.andromda.maven.site.highlight.xml.node.Start;
import org.andromda.maven.site.highlight.xml.parser.Parser;

import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;

public class XmlHighlighter
{
    private XmlHighlightStyles styles = null;

    public XmlHighlighter(XmlHighlightStyles styles)
    {
        this.styles = styles;
    }

    public void highlight(Reader reader, Writer writer)
    {
        try
        {
            final PushbackReader pushbackReader = (reader instanceof PushbackReader)
                    ? (PushbackReader) reader
                    : new PushbackReader(reader, 8);

            Lexer lexer = new Lexer(pushbackReader);
            Parser parser = new Parser(lexer);
            Start startNode = parser.parse();
            startNode.apply(new XmlHighlightAdapter(writer, styles));
        }
        catch (Exception ex)
        {
            System.out.println("Error highlighting XML: " + ex);
        }
    }
}
