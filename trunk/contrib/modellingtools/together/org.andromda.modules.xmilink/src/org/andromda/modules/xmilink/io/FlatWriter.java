package org.andromda.modules.xmilink.io;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 17.09.2004
 */
public class FlatWriter
        extends Writer
{

    private StringBuffer buffer = new StringBuffer();

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.io.Writer#writeOpeningElement(java.lang.String)
     */
    public void writeOpeningElement(String string)
    {
        writeOpeningElementStart(string);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.io.Writer#writeOpeningElementStart(java.lang.String)
     */
    public void writeOpeningElementStart(String name)
    {
        buffer.append("Element: " + name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.io.Writer#writeProperty(java.lang.String,
     *      java.lang.String)
     */
    public void writeProperty(String name, String value)
    {
        buffer.append(" " + name + "=" + value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.io.Writer#writeOpeningElementEnd()
     */
    public void writeOpeningElementEnd(boolean close)
    {
        buffer.append(" ;");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.io.Writer#writeText(java.lang.String)
     */
    public void writeText(String string)
    {
        buffer.append(string);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.io.Writer#writeClosingElement()
     */
    public void writeClosingElement(String name)
    {
        buffer.append("\n");
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
