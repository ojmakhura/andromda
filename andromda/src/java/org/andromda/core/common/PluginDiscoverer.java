package org.andromda.core.common;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Discovers and loads all available Plugin objects from the current classpath.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 */
public class PluginDiscoverer
{
    private static final Logger logger = Logger.getLogger(PluginDiscoverer.class);

    /**
     * The properties that contain the plugin type information.
     */
    private static final Properties pluginResources = new Properties();

    static
    {
        final String pluginsUri = "META-INF/andromda-plugins.properties";
        try
        {
            final URL andromdaPluginsUri = ResourceUtils.getResource(pluginsUri);
            if (andromdaPluginsUri == null)
            {
                String errMsg = "Could not find --> '" + pluginsUri + "'";
                logger.error(errMsg);
                throw new PluginDiscovererException(errMsg);
            }
            InputStream stream = andromdaPluginsUri.openStream();
            pluginResources.load(stream);
            stream.close();
            stream = null;
        }
        catch (Throwable throwable)
        {
            String errMsg = "Error loading --> '" + pluginsUri + "'";
            throw new PluginDiscovererException(errMsg, throwable);
        }
    }

    /**
     * The shared instance.
     */
    private static final PluginDiscoverer instance = new PluginDiscoverer();

    /**
     * Gets the default static instance of the PluginDicoverer.
     *
     * @return PluginDiscoverer the static instance.
     */
    public final static PluginDiscoverer instance()
    {
        return instance;
    }

    /**
     * Discovers and initializes all <code>Plugin</code> objects on the current classpath. If the cartridge with the
     * given name is already registered in the ComponentContainer, it will not be registered again.
     */
    public void discoverPlugins()
    {
        this.discoverPlugins(true);
    }

    /**
     * Discovers and initializes all <code>Plugin</code> objects on the current classpath. If the plugin with the given
     * name is already registered in the ComponentContainer, it will not be registered again.
     *
     * @param showPlugins if true then the plugin found will be logged, otherwise nothing will be shown.
     */
    private void discoverPlugins(final boolean showPlugins)
    {
        final String methodName = "PluginDiscoverer.discoverPlugins";
        if (showPlugins)
        {
            AndroMDALogger.info("-- discovering plugins --");
        }
        try
        {
            for (final Enumeration plugins = pluginResources.keys(); plugins.hasMoreElements();)
            {
                final String pluginXmlUri = StringUtils.trimToEmpty((String)plugins.nextElement());
                final String pluginClassName = pluginResources.getProperty(pluginXmlUri);
                final Class pluginClass = ClassUtils.loadClass(pluginClassName);
                if (!Plugin.class.isAssignableFrom(pluginClass))
                {
                    throw new PluginDiscovererException(
                        methodName + " plugin class '" + pluginClassName + "' must implement --> '" + Plugin.class +
                        "'");
                }

                final URL[] pluginResources = ResourceFinder.findResources(pluginXmlUri);
                if (pluginResources != null && pluginResources.length > 0)
                {
                    final Set discoveredPlugins = new HashSet();
                    for (int ctr = 0; ctr < pluginResources.length; ctr++)
                    {
                        final URL pluginUri = pluginResources[ctr];
                        final XmlObjectFactory factory = XmlObjectFactory.getInstance(pluginClass);
                        Plugin plugin = (Plugin)factory.getObject(pluginUri);
                        final String pluginName = plugin.getName();

                        final ComponentContainer container = ComponentContainer.instance();
                        if (container.isRegistered(plugin.getName()))
                        {
                            plugin = (Plugin)container.findComponent(pluginName);
                        }
                        else
                        {
                            // perform the merge of the configuration file since
                            // we now know the namespace
                            final String pluginUriContents =
                                Merger.instance().getMergedString(
                                    ResourceUtils.getContents(pluginUri),
                                    plugin.getName());
                            plugin = (Plugin)factory.getObject(pluginUriContents);
                            plugin.setResource(pluginUri);
                            container.registerComponent(pluginName, plugin);
                        }
                        discoveredPlugins.add(plugin);
                    }
                    // list out the discovered plugins
                    for (final Iterator iterator = discoveredPlugins.iterator(); iterator.hasNext();)
                    {
                        final Plugin plugin = (Plugin)iterator.next();
                        if (showPlugins && logger.isInfoEnabled())
                        {
                            AndroMDALogger.info("found " + plugin.getType() + " --> '" + plugin.getName() + "'");
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            String errMsg = "Error performing " + methodName;
            logger.error(errMsg, ex);
            throw new PluginDiscovererException(errMsg, ex);
        }
    }

    /**
     * Finds the plugins having the give <code>type</code>.
     *
     * @param type the Plugin type.
     * @return Collection of all found Plugin instances of the given <code>type</code>.
     */
    public Collection findPlugins(final Class type)
    {
        final Collection plugins = ComponentContainer.instance().findComponentsOfType(type);
        if (plugins == null)
        {
            logger.error("ERROR! No plugins with type '" + type + "' found");
        }
        return plugins;
    }
}