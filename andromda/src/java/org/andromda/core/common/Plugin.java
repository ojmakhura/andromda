package org.andromda.core.common;

import java.net.URL;
import java.util.Collection;
import java.util.Map;

import org.andromda.core.templateengine.TemplateEngine;



/**
 * Interface between an AndroMDA code generator plugin
 * and the core.  All plug-ins (such as cartridges and 
 * translation-libraries) that can be discovered and used by 
 * the framework must implement this interface.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 */
public interface Plugin {
    
    /**
     * Initializes the plugin.
     */
    public void init() throws Exception;

    /**
     * Shuts down the plugin. The meaning of this is defined
     * by the plugin itself. At least, it should close any logfiles.
     */
    public void shutdown();
	
	/**
	 * Returns the name of this Plugin.  This name must
	 * be unique amoung those Plugins of the same type.
	 * 
	 * @return String the name of this Plugin
	 */
	public String getName();
    
    /**
     * The entire path to the resource the Plugin
     * instance is configured from.
     * 
     * @return URL the path to the resource from which this 
     *         Plugin was configured.
     */
    public URL getResource();
	
    /**
     * Sets the path of the resource from which this
     * plugin is configured.
     * 
     * @param resource the resource URL.
     */
    public void setResource(URL resource);
    
	/**
	 * Returns the type of this plugin (i.e. <code>cartridge</code>, 
     * <code>translation-library</code>).
	 * 
	 * @return String the type name.
	 */
	public String getType();
    
    /**
     * Returns all the TemplateObject objects that
     * are available to this Plugin.
     * 
     * @return a collection of TemplateObjects.
     */
    public Collection getTemplateObjects();
    
    /**
     * Gets the TemplateEngine which implements the template 
     * processing.
     * 
     * @see org.andromda.core.templateengine.TemplateEngine
     * 
     * @return TemplateEngine
     */
    public TemplateEngine getTemplateEngine();
    
    /**
     * Gets all property references available for this cartridge.
     * Returns a Map that contains all property references and 
     * their default values (if any).
     * 
     * @return the Map of property references.
     */
    public Map getPropertyReferences();

}
