package org.andromda.ant.task;

import java.net.URL;

import org.andromda.core.configuration.Transformation;

/**
 * This class implements the <code>&lt;transformation&gt;</code> element which
 * is used with the Ant <code>&lt;andromda&gt;</code> task to configure an
 * XSLT transformation that will transform the model before procesing occurs.
 * 
 * @author Chad Brandon
 */
public class TransformationConfiguration
{
    /**
     * Gets the transformation
     * 
     * @return Returns the url of the transformation.
     */
    public Transformation getTransformation()
    {
        final Transformation transformation = new Transformation();
        transformation.setUri(this.url != null ? this.url.toString() : null);
        transformation.setOutputLocation(this.outputLocation);
        return transformation;
    }

    /**
     * The URL to the transformation.
     */
    private URL url = null;
    
    /**
     * Sets the URL to the transformation.
     * 
     * @param url The url to set.
     */
    public void setUrl(URL url)
    {
        this.url = url;
    }
    
    /**
     * The location ot the output.
     */
    private String outputLocation;
    
    /**
     * Sets the location to which the result of the transformation
     * shall be written.
     * 
     * @param outputLocation the output location.
     */
    public void setOutputLocation(String outputLocation)
    {
        this.outputLocation = outputLocation;
    }
}