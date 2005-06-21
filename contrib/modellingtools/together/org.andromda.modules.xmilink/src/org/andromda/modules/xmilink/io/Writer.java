package org.andromda.modules.xmilink.io;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 17.09.2004
 */
public abstract class Writer
{

    public abstract void writeOpeningElement(String string);

    public abstract void writeOpeningElementStart(String name);

    public abstract void writeProperty(String name, String value);

    public abstract void writeOpeningElementEnd(boolean close);

    public abstract void writeClosingElement(String name);

    public abstract String getContents();

    public abstract void writeText(String string);

}
