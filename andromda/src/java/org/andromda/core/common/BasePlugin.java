package org.andromda.core.common;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.cartridge.Cartridge;
import org.andromda.core.templateengine.TemplateEngine;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * Represents the base plugin of AndroMDA. All Plugin instances inherit from
 * this class.
 * 
 * @author Chad Brandon
 */
public abstract class BasePlugin
    implements Plugin
{

    protected Logger logger = null;

    /**
     * Default constructor which must be called by super classes.
     */
    protected BasePlugin()
    {
        this.resetLogger();
    }

    /**
     * Property references made available to the plugin
     */
    private Map propertyReferences = new HashMap();

    /**
     * The template objects made available to templates of this BasePlugin.
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
     * Adds a macro library to the TemplateEngine used by this BasePlugin.
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
        if (StringUtils.isNotBlank(templateEngineClass))
        {
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
        return (TemplateEngine) ComponentContainer.instance().findComponent(
            TemplateEngine.class);
    }

    /**
     * @see org.andromda.core.common.Plugin#getPropertyReferences()
     */
    public Map getPropertyReferences()
    {
        return this.propertyReferences;
    }

    /**
     * Adds a property reference. Property references are those properties that
     * are expected to be supplied by the calling client. These supplied
     * properties are made available to the template during processing.
     * 
     * @param reference the name of the reference.
     * @param defaultValue the default value of the property reference.
     */
    public void addPropertyReference(String reference, String defaultValue)
    {
        this.propertyReferences.put(reference, defaultValue);
    }
    
    /**
     * Populates the <code>templateContext</code> with the 
     * properties and template objects defined in the <code>plugin</code>'s
     * descriptor.  If the <code>templateContext</code> is null, a new
     * Map instance will be created before populating the context.
     * 
     * @param templateContext the context of the template to populate.
     */
    protected void populateTemplateContext(Map templateContext) 
    {
        if (templateContext == null) {
            templateContext = new HashMap();    
        }
        this.addTemplateObjectsToContext(templateContext);
        this.addPropertyReferencesToContext(templateContext);
    }

    /**
     * Takes all the template objects defined in the plugin's descriptor and
     * places them in the <code>templateContext</code>.
     * 
     * @param templateContext the template context
     * @param properties the user properties
     */
    private void addTemplateObjectsToContext(Map templateContext)
    {
        // add all the TemplateObject objects to the template context
        Collection templateObjects = this.getTemplateObjects();
        if (templateObjects != null && !templateObjects.isEmpty())
        {
            Iterator templateObjectIt = templateObjects.iterator();
            while (templateObjectIt.hasNext())
            {
                TemplateObject templateObject = (TemplateObject) templateObjectIt
                    .next();
                templateContext.put(templateObject.getName(), templateObject
                    .getTemplateObject());
            }
        }
    }

    /**
     * Takes all the property references defined in the plugin's descriptor and
     * looks up the corresponding values supplied by the calling client and
     * supplies them to the <code>templateContext</code>.
     * 
     * @param templateContext the template context
     * @param properties the user properties
     */
    private void addPropertyReferencesToContext(Map templateContext)
    {
        Map propertyReferences = this.getPropertyReferences();
        if (propertyReferences != null && !propertyReferences.isEmpty())
        {
            Iterator referenceIt = propertyReferences.keySet().iterator();
            while (referenceIt.hasNext())
            {
                String reference = (String) referenceIt.next();
                String defaultValue = (String) propertyReferences
                    .get(reference);

                // if we have a default value, then don't warn
                // that we don't have a property, otherwise we'll
                // show the warning.
                boolean showWarning = false;
                if (defaultValue == null)
                {
                    showWarning = true;
                }
                // find the property from the namespace
                Property property = Namespaces.instance().findNamespaceProperty(
                    this.getName(),
                    reference,
                    showWarning);
                // if property isn't ignore, then add it to
                // the context
                if (property != null && !property.isIgnore())
                {
                    templateContext
                        .put(property.getName(), property.getValue());
                }
                else if (defaultValue != null)
                {
                    templateContext.put(reference, defaultValue);
                }
            }
        }
    }

    /**
     * Resets the logger to the default name.
     */
    protected void resetLogger()
    {
        this.setLogger(Cartridge.class.getName());
    }

    /**
     * Sets the logger to be used with this Cartridge
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