package org.andromda.modules.xmilink.io;

/**
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 * 
 * @author U402101
 */
public class XMLWriter
        extends Writer
{

    private int indentationLevel = -1;

    private char[] indentArray = new char[255];

    private StringBuffer buffer = new StringBuffer();

    public XMLWriter()
    {
        // fill indentation array
        for (int i = 0; i < indentArray.length; i++)
        {
            indentArray[i] = '\t';
        }
    }

    private void indent()
    {
        indentationLevel++;
    }

    private String getIndent()
    {
        String result = new String(indentArray, 0, indentationLevel);
        return result;
    }

    private void outdent()
    {
        indentationLevel--;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.io.Writer#writeOpeningElement(java.lang.String)
     */
    public void writeOpeningElement(String string)
    {
        writeOpeningElementStart(string);
        writeOpeningElementEnd(false);
    }

    /**
     * 
     */
    private void writeLineBreak()
    {
        buffer.append("\n");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.io.Writer#writeOpeningElementStart(java.lang.String)
     */
    public void writeOpeningElementStart(String name)
    {
        indent();
        buffer.append(getIndent() + "<" + name);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.io.Writer#writeProperty(java.lang.String,
     *      java.lang.String)
     */
    public void writeProperty(String name, String value)
    {
        buffer.append(" " + name + " = '" + escape(value) + "'");

    }

    /**
     * @param value
     * @return
     */
    private String escape(String value)
    {
        if (value != null)
        {
            return Encoder.htmlescape(value);
        }
        else
        {
            return "";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.io.Writer#writeOpeningElementEnd()
     */
    public void writeOpeningElementEnd(boolean close)
    {
        if (close)
        {
            buffer.append("/>");
            outdent();
        }
        else
        {
            buffer.append(">");
        }
        writeLineBreak();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.io.Writer#writeText(java.lang.String)
     */
    public void writeText(String string)
    {
        indent();
        buffer.append(getIndent() + string);
        writeLineBreak();
        outdent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.io.Writer#writeClosingElement(java.lang.String)
     */
    public void writeClosingElement(String name)
    {
        buffer.append(getIndent() + "</" + name + ">");
        writeLineBreak();
        outdent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.io.Writer#getContents()
     */
    public String getContents()
    {
        return buffer.toString();
    }

}
