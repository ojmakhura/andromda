package org.andromda.mdr.xmi.uml20.load.handlers;

import org.xml.sax.SAXException;

public class XMIVersionException
    extends SAXException
{
    public XMIVersionException(
        String message)
    {
        super(message);
    }
}