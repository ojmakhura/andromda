package org.andromda.core.common;

import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Discovers and loads all available Plugin objects
 * from the current classpath.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 */
public class PluginDiscoverer {
	
	private static final Logger logger = 
		Logger.getLogger(PluginDiscoverer.class);
    
    /**
     * The properties that contain the plugin type information.
     */
    private static final Properties pluginResources = new Properties();
       
    static {
        String ANDROMDA_PLUGINS = "META-INF/andromda-plugins.properties";
        try {
            URL andromdaPluginsUri = ResourceUtils.getResource(ANDROMDA_PLUGINS);
            if (andromdaPluginsUri == null) {
                String errMsg = "Could not find --> '" + ANDROMDA_PLUGINS + "'";                
                logger.error(errMsg); 
                throw new PluginDiscovererException(errMsg);
            } 
            pluginResources.load(andromdaPluginsUri.openStream());    
        } catch (Throwable th) {
            String errMsg = "Error loading --> '" + ANDROMDA_PLUGINS + "'";
            logger.error(errMsg, th);
            throw new PluginDiscovererException(errMsg, th);
        }
    }
	
	/**
	 * The shared instance.
	 */
	private static final PluginDiscoverer instance = new PluginDiscoverer();
	
	/**
	 * Gets the default static instance of the PluginDicoverer.
	 * @return PluginDiscoverer the static instance.
	 */
	public static PluginDiscoverer instance() 
    {
		return instance;
	}
    
    /**
     * Discovers and initializes all <code>Plugin</code>
     * objects on the current classpath.  If the cartridge
     * with the given name is already registered in the 
     * ComponentContainer, it will not be registered again.
     */
    public void discoverPlugins()
    {
       this.discoverPlugins(true); 
    }

    /**
     * Discovers and initializes all <code>Plugin</code>
     * objects on the current classpath.  If the plugin
     * with the given name is already registered in the 
     * ComponentContainer, it will not be registered again.
     * 
     * @param showPlugins if true then the plugin found will
     *        logged, otherwise nothing will be shown.
     */
	public void discoverPlugins(boolean showPlugins) 
    {
		final String methodName = "PluginDiscoverer.discoverPlugins";
		if (logger.isDebugEnabled())
			logger.debug("performing " + methodName);
			
		try {
			
			Enumeration pluginEnum = pluginResources.keys();
			
			while (pluginEnum.hasMoreElements()) 
            {
				String pluginXmlUri = 
                    StringUtils.trimToEmpty((String)pluginEnum.nextElement());
				String pluginClassName = pluginResources.getProperty(pluginXmlUri);
				Class pluginClass = ClassUtils.loadClass(pluginClassName);
				if (!Plugin.class.isAssignableFrom(pluginClass)) 
                {
					throw new PluginDiscovererException(methodName
						+ " plugin class '" + pluginClassName + "' must implement --> '" 
						+ Plugin.class + "'");
				}
				
				URL[] pluginResources = ResourceFinder.findResources(pluginXmlUri);
		
				if (pluginResources != null && pluginResources.length > 0) 
                {
					for (int ctr = 0; ctr < pluginResources.length; ctr++) 
                    {
					
						URL pluginUri = pluginResources[ctr];
						Plugin plugin = (Plugin)XmlObjectFactory.getInstance(
                            pluginClass).getObject(pluginUri);
                        plugin.setResource(pluginUri);
                        
                        if (!ComponentContainer.instance().isRegistered(plugin.getName())) 
                        {
    						if (showPlugins && logger.isInfoEnabled())
    							logger.info(
    								"found " + 
    									plugin.getType() 
    									+ " --> '" + plugin.getName() + "'");										
    		                       
    						ComponentContainer.instance().registerComponent(plugin.getName(), plugin);
                        }
					}
				}		
			}	
		} catch (Exception ex) {
			String errMsg = "Error performing " + methodName;
			logger.error(errMsg, ex);
			throw new PluginDiscovererException(errMsg, ex);
		}
	}
    
    /**
     * Finds the plugins having the give <code>type</code>.
     * @param type the Plugin type.
     * @return Collection of all found Plugin instances of 
     *         the given <code>type</code>.
     */
    public Collection findPlugins(Class type) 
    {
        Collection plugins = ComponentContainer.instance().findComponentsOfType(type);
        if (plugins == null) {
            logger.error("ERROR! No plugins with type '" 
                + type + "' found");
        }
        return plugins;
    }
    
}
