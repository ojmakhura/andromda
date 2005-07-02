package org.andromda.core.repository;

import java.io.InputStream;
import java.net.URL;

import org.andromda.core.metafacade.ModelAccessFacade;

public class MockRepository
    implements RepositoryFacade
{

    public void open()
    {
    }

    public void close()
    {
    }

    public void readModel(URL modelUrl, String[] moduleSearchPath)
    {
    }

    public void readModel(String[] uris, String[] moduleSearchPath)
    {
    }

    public void readModel(InputStream stream, String uri, String[] moduleSearchPath)
    {
    }

    public void readModel(InputStream[] streams, String[] uris, String[] moduleSearchPath)
    {
    }

    public void writeModel(Object model, String outputLocation, String version, String encoding)
    {
    }

    public void writeModel(Object model, String outputLocation, String version)
    {
    }

    public ModelAccessFacade getModel()
    {
        return null;
    }

    public void clear()
    {
    }

}