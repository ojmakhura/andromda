package org.andromda.core.anttasks;

import java.net.URL;

/**
 * This class implements the <code>&lt;model&gt;</code> element which is
 * used with the Ant  <code>&lt;andromda&gt;</code> task to configure a 
 * <code>org.andromda.core.Model</code> instance for processing.
 * 
 * @author Chad Brandon
 */
public class ModelConfiguration
{
    private URL url = null;
    
    /**
     * Gets the URL to the model.
     * 
     * @return Returns the url of the model.
     */
    public URL getUrl()
    {
        return url;
    }
    
    /**
     * Sets the URL to the model.
     * 
     * @param url The url to set.
     */
    public void setUrl(URL url)
    {
        this.url = url;
    }
}