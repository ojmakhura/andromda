package org.andromda.ant.task;

import java.net.URL;

/**
 * This class implements the <code>&lt;transformation&gt;</code> element which
 * is used with the Ant <code>&lt;andromda&gt;</code> task to configure an
 * XSLT transformation that will transform the model before procesing occurs.
 * 
 * @author Chad Brandon
 */
public class TransformationConfiguration
{
    private URL url = null;

    /**
     * Gets the URL to the transformation.
     * 
     * @return Returns the url of the transformation.
     */
    public URL getUrl()
    {
        return url;
    }

    /**
     * Sets the URL to the transformation.
     * 
     * @param url The url to set.
     */
    public void setUrl(URL url)
    {
        this.url = url;
    }
}