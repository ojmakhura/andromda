package org.andromda.core.configuration;

import java.net.URL;

/**
 * Stores the information about a transformation. Transformations are applied to
 * model(s) before actual model processing occurs.
 * 
 * @author Chad Brandon
 */
public class Transformation
{
    /**
     * The URL location of the transformation.
     */
    private URL uri;
    
    /**
     * Sets the URL of the transformation.
     * 
     * @param uri the URL to the transformation.
     */
    public void setUri(final String uri)
    {
        try
        {
            this.uri = new URL(uri);
        }
        catch (final Throwable throwable)
        {
            throw new ConfigurationException(throwable);
        }
    }

    /**
     * The URL of the model.
     * 
     * @return Returns the uri.
     */
    public URL getUri()
    {
        return uri;
    }

    /**
     * Stores the optional output location.
     */
    private String outputLocation;

    /**
     * Sets the location to which the result of this transformation should be
     * written. This is optional, if this is unspecified then the result is not
     * written but just passed in memory to the processor.
     * 
     * @param outputLocation the location of the output to be written.
     */
    public void setOutputLocation(final String outputLocation)
    {
        this.outputLocation = outputLocation;
    }

    /**
     * Gets the location to which the output of the transformation result will
     * be written.
     * 
     * @return the output location.
     */
    public String getOutputLocation()
    {
        return this.outputLocation;
    }
}
