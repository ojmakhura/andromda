package org.andromda.core.repository;

import java.io.InputStream;
import java.net.URL;
import org.andromda.core.metafacade.ModelAccessFacade;

/**
 *
 */
public class MockRepository
    implements RepositoryFacade
{
    /**
     * @see org.andromda.core.repository.RepositoryFacade#open()
     */
    public void open()
    {
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#close()
     */
    public void close()
    {
    }

    /**
     * @param modelUrl
     * @param moduleSearchPath
     */
    public void readModel(URL modelUrl, String[] moduleSearchPath)
    {
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#readModel(String[], String[])
     */
    public void readModel(String[] uris, String[] moduleSearchPath)
    {
    }

    /**
     * @param stream
     * @param uri
     * @param moduleSearchPath
     */
    public void readModel(InputStream stream, String uri, String[] moduleSearchPath)
    {
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#readModel(java.io.InputStream[], String[], String[])
     */
    public void readModel(InputStream[] streams, String[] uris, String[] moduleSearchPath)
    {
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#writeModel(Object, String, String, String)
     */
    public void writeModel(Object model, String outputLocation, String version, String encoding)
    {
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#writeModel(Object, String, String)
     */
    public void writeModel(Object model, String outputLocation, String version)
    {
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#getModel()
     */
    public ModelAccessFacade getModel()
    {
        return null;
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#clear()
     */
    public void clear()
    {
    }

    /**
     * @param type
     * @return model
     */
    public ModelAccessFacade getModel(Class type)
    {
        return null;
    }

}