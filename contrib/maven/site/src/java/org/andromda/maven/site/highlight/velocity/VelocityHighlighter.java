package org.andromda.maven.site.highlight.velocity;

import org.andromda.maven.site.highlight.velocity.lexer.Lexer;
import org.andromda.maven.site.highlight.velocity.node.Start;
import org.andromda.maven.site.highlight.velocity.parser.Parser;

import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;

public class VelocityHighlighter
{
    private VelocityHighlightStyles styles = null;

    public VelocityHighlighter(VelocityHighlightStyles styles)
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
            startNode.apply(new VelocityHighlightAdapter(writer, styles));
        }
        catch (Exception ex)
        {
            System.out.println("Error highlighting Velocity: " + ex);
        }
    }
}
