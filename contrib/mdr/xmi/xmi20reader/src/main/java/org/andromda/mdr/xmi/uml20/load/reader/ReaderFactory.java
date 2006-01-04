package org.andromda.mdr.xmi.uml20.load.reader;

import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;

public class ReaderFactory
    extends XMIReaderFactory
{
    public XMIReader createXMIReader()
    {
        return new XmiReader();
    }

    public XMIReader createXMIReader(XMIInputConfig configuration)
    {
        return new XmiReader(configuration);
    }
}