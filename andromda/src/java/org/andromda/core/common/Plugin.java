package org.andromda.core.common;

import java.util.Collection;
import java.util.List;

import org.andromda.core.namespace.NamespaceComponent;
import org.andromda.core.templateengine.TemplateEngine;


/**
 * Interface between an AndroMDA code generator plugin and the core. All plug-ins (such as cartridges and
 * translation-libraries) that can be discovered and used by the framework must implement this interface.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 */
public interface Plugin
    extends NamespaceComponent
{
    /**
     * Initializes the plugin.
     */
    public void initialize()
        throws Exception;

    /**
     * Shuts down the plugin. The meaning of this is defined by the plugin itself. At least, it should close any
     * logfiles.
     */
    public void shutdown();

    /**
     * Returns all the TemplateObject objects that are available to this Plugin.
     *
     * @return a collection of TemplateObjects.
     */
    public Collection getTemplateObjects();

    /**
     * Gets the TemplateEngine which implements the template processing.
     *
     * @return TemplateEngine
     * @see org.andromda.core.templateengine.TemplateEngine
     */
    public TemplateEngine getTemplateEngine();

    /**
     * Gets all property references available for this cartridge. This is 
     * an array of names corresponding to property references.
     *
     * @return the Map of property references.
     */
    public String[] getPropertyReferences();

    /**
     * Returns a list containing the name of each resource contained within the plugin.
     */
    public List getContents();
}