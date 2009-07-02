package org.andromda.core.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;


/**
 * A utility object useful for formatting paragraph output.
 * <p/>
 * Represents a paragraph, made of lines. The whole paragraph has a limit for the line length. Words can be added, the
 * class will reformat the paragraph according to max. line length. </p>
 *
 * @author Matthias Bohlen
 * @author Chad Brandon
 */
public class Paragraph
{
    private StringBuffer currentLine = new StringBuffer();
    private int maxLineWidth;

    /**
     * <p/>
     * Constructs an HtmlParagraph with a specified maximum line length. </p>
     *
     * @param lineLength maximum line length
     */
    public Paragraph(final int lineLength)
    {
        this.maxLineWidth = lineLength;
    }

    /**
     * <p/>
     * Appends another word to this paragraph. </p>
     *
     * @param word the word
     */
    public void appendWord(final String word)
    {
        if ((currentLine.length() + word.length() + 1) > maxLineWidth)
        {
            nextLine();
        }
        currentLine.append(" ");
        currentLine.append(word);
    }

    /**
     * <p/>
     * Appends a bunch of words to the paragraph. </p>
     *
     * @param text the text to add to the paragraph
     */
    public void appendText(final String text)
    {
        if ((currentLine.length() + text.length() + 1) <= maxLineWidth)
        {
            currentLine.append(" ");
            currentLine.append(text);
            return;
        }
        StringTokenizer tokenizer = new StringTokenizer(text);
        while (tokenizer.hasMoreTokens())
        {
            appendWord(tokenizer.nextToken());
        }
    }

    private final Collection<String> lines = new ArrayList<String>();

    /**
     * <p/>
     * Returns the lines in this paragraph. </p>
     *
     * @return Collection the lines as collection of Strings
     */
    public Collection<String> getLines()
    {
        if (currentLine.length()>0)
        {
            nextLine();
        }
        return lines;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        final StringBuffer buffer = new StringBuffer();
        for (final String line : this.getLines())
        {
            buffer.append(line);
            buffer.append("\n");
        }
        return buffer.toString();
    }

    private void nextLine()
    {
        lines.add(currentLine.toString());
        currentLine = new StringBuffer();
    }
}