package org.andromda.core.common;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.andromda.core.cartridge.Cartridge;
import org.andromda.core.templateengine.TemplateEngine;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * Represents the base plugin of AndroMDA.  All Plugin
 * instances inherit from this class.
 * 
 * @author Chad Brandon
 */
public abstract class BasePlugin implements Plugin {
    
    protected Logger logger = null;
    
    /**
     * Default constructor which must be
     * called by super classes.
     */
    protected BasePlugin() 
    {
        this.resetLogger();
    }
    
    /**
     * Property references made available to the plugin
     */
    private Collection propertyReferences = new ArrayList();
    
    /**
     * The template objects made available to templates
     * of this BasePlugin.
     */
    private Collection templateObjects = new ArrayList();
    
    /**
     * Stores the name of this plugin.
     */
    private String name;
    
    /**
     * The resource that configured this BasePlugin.
     */
    private URL resource;
    
    /**
     * Returns a new configured instance of this BasePlugin 
     * from the pluginXml URL.
     * 
     * @param pluginXml the URI to the plugin XML configuration document.
     * @param pluginClass the plugin in class for which to get the instance.
     * @return Plugin the configured Plugin instance.
     */
    protected static Plugin getInstance(URL pluginXml, Class pluginClass) 
    {
        final String methodName = "BasePlugin.getInstance";
        ExceptionUtils.checkNull(methodName, "pluginXml", pluginXml);
        BasePlugin plugin = 
            (BasePlugin)XmlObjectFactory.getInstance(
                pluginClass).getObject(pluginXml);
        plugin.resource = pluginXml;
        return plugin;
    }
    
    /**
     * Returns the name of this Library.
     * 
     * @return String
     */
    public String getName() 
    {
        return this.name;
    }
    
    /**
     * @see org.andromda.core.common.Plugin#init()
     */
    public void init() throws Exception
    {
        this.getTemplateEngine().init(this.getName());
    }
    
    /**
     * @see org.andromda.core.common.Plugin#shutdown()
     */
    public void shutdown()
    {
        this.getTemplateEngine().shutdown();
    }
    
    /**
     * Sets the name of this Library.
     * 
     * @param name
     */
    public void setName(String name) 
    {
        this.name = name;
    }
    
    /**
     * @see org.andromda.core.common.Plugin#getResource()
     */
    public URL getResource() 
    {
        return this.resource;
    }
    
    /**
     * @see org.andromda.core.common.Plugin#setResource(java.net.URL)
     */
    public void setResource(URL resource) 
    {
        this.resource = resource;   
    }

    /**
     * @see org.andromda.core.common.Plugin#addTemplateObject(org.andromda.core.common.TemplateObject)
     */
    public void addTemplateObject(TemplateObject templateObject) 
    {
        final String methodName = "BasePlugin.addTemplateObjects";
        ExceptionUtils.checkNull(methodName, "templateObject", templateObject);
        templateObject.setResource(this.getResource());
        templateObject.setNamespace(this.getName());
        this.templateObjects.add(templateObject);
    }
    
    /**
     * Adds a macro library to the TemplateEngine
     * used by this BasePlugin.
     * 
     * @param macrolibrary
     */
    public void addMacrolibrary(String macrolibrary) 
    {
        this.getTemplateEngine().addMacroLibrary(macrolibrary);   
    }
    
    /**
     * @see org.andromda.core.common.Plugin#getTemplateObjects()
     */
    public Collection getTemplateObjects() 
    {
        return this.templateObjects;
    }
    
    /**
     * Sets the template engine class for this cartridge.
     * 
     * @param templateEngineClass the Class of the template engine
     *        implementation.
     */
    public void setTemplateEngineClass(String templateEngineClass) 
    {
        if (StringUtils.isNotBlank(templateEngineClass)) {
            ComponentContainer.instance().registerDefaultComponent(
                TemplateEngine.class,
                templateEngineClass);
        }
    }
    
    /**
     * @see org.andromda.core.common.Plugin#getTemplateEngine()
     */
    public TemplateEngine getTemplateEngine() 
    {
        return (TemplateEngine)ComponentContainer.instance().findComponent(
            TemplateEngine.class);
    }
    
    /**
     * @see org.andromda.core.common.Plugin#getPropertyReferences()
     */
    public Collection getPropertyReferences() 
    {
        return this.propertyReferences;
    }

    /**
     * Adds a property reference. Property references
     * are those properties that are expected to be 
     * supplied by the calling client.  These supplied
     * properties are made available to the template
     * during processing.
     * 
     * @param reference
     */
    public void addPropertyReference(String reference) 
    {
        this.propertyReferences.add(reference);
    }
    
    /**
     * Resets the logger to the default name.
     */
    protected void resetLogger() 
    {
        this.setLogger(Cartridge.class.getName());      
    }
    
    /**
     * Sets the logger to be used
     * with this Cartridge
     * 
     * @param logger The logger to set.
     */
    protected void setLogger(String loggerName) 
    {
        this.logger = Logger.getLogger(loggerName);
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() 
    {
        return ToStringBuilder.reflectionToString(this);
    }
    
}
