package org.andromda.maven.site.highlight.java;

import org.andromda.maven.site.highlight.java.lexer.Lexer;
import org.andromda.maven.site.highlight.java.node.Start;
import org.andromda.maven.site.highlight.java.parser.Parser;

import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;

public class JavaHighlighter
{
    private JavaHighlightStyles styles = null;

    public JavaHighlighter(JavaHighlightStyles styles)
    {
        this.styles = styles;
    }

    public void highlight(Reader reader, Writer writer)
    {
        try
        {
            final PushbackReader pushbackReader = (reader instanceof PushbackReader)
                    ? (PushbackReader) reader
                    : new PushbackReader(reader, 16);

            Lexer lexer = new Lexer(pushbackReader);
            Parser parser = new Parser(lexer);
            Start startNode = parser.parse();
            startNode.apply(new JavaHighlightAdapter(writer, styles));
        }
        catch (Exception ex)
        {
            System.out.println("Error highlighting Java: " + ex);
        }
    }
}
