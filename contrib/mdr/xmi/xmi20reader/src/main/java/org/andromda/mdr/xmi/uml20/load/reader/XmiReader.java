package org.andromda.mdr.xmi.uml20.load.reader;

import java.io.IOException;
import java.io.InputStream;

import java.util.Collection;

import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;

import org.andromda.mdr.xmi.uml20.load.handlers.XMIVersionException;
import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.lib.jmi.xmi.SAXReader;

/**
 * Responsible for parsing xmi files. It is responsible also for loading
 * dependent modules and establish the references.
 */
public class XmiReader
    extends XMIReader
{
    private Xmi2Reader mReader;

    public XmiReader()
    {
        System.out.println("trying to read!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        mReader = new Xmi2Reader();
    }

    public XmiReader(
        XMIInputConfig configuration)
    {
        this();
        mReader.setConfiguratio(configuration);
    }

    public XMIInputConfig getConfiguration()
    {
        return mReader.getConfiguration();
    }

    public Collection read(InputStream stream, String URI, RefPackage extent)
        throws IOException,
            MalformedXMIException
    {
        try
        {
            return mReader.read(stream, URI, extent);
        }
        catch (XMIVersionException e)
        {
            return new SAXReader(getConfiguration()).read(stream, URI, extent);
        }
    }

    public Collection read(String URI, RefPackage extent) throws IOException, MalformedXMIException
    {
        try
        {
            return mReader.read(URI, extent);
        }
        catch (XMIVersionException e)
        {
            return new SAXReader(getConfiguration()).read(URI, extent);
        }
    }
}