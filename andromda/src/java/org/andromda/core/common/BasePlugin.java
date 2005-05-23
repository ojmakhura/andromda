package org.andromda.core.common;

import org.andromda.core.configuration.NamespaceProperties;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Property;
import org.andromda.core.templateengine.TemplateEngine;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Represents the base plugin of AndroMDA. All Plugin instances inherit from this class.
 *
 * @author Chad Brandon
 */
public abstract class BasePlugin
        implements Plugin
{
    /**
     * Property references made available to the plugin
     */
    private final Map propertyReferences = new HashMap();

    /**
     * The template objects made available to templates of this BasePlugin.
     */
    private final Collection templateObjects = new ArrayList();

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
     * @see org.andromda.core.common.Plugin#initialize()
     */
    public void initialize() throws Exception
    {
        // set the template engine merge location (this needs to be
        // set before the template engine is initialized) so that the
        // merge property can be set once on the template engine.
        Property mergeProperty = Namespaces.instance().findNamespaceProperty(this.getName(),
                NamespaceProperties.MERGE_LOCATION, false);
        this.mergeLocation = mergeProperty != null ? new File(mergeProperty.getValue()).toURL() : null;
        if (this.mergeLocation != null)
        {
            this.getTemplateEngine().setMergeLocation(this.getMergeLocation().getFile());
        }
        this.getTemplateEngine().initialize(this.getName());
    }

    /**
     * The current cartridge merge location.
     */
    private URL mergeLocation;

    /**
     * Gets the current merge location for this plugin.
     *
     * @return the merge location (a file path).
     */
    protected URL getMergeLocation()
    {
        return this.mergeLocation;
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
    public void setName(final String name)
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
    public void setResource(final URL resource)
    {
        this.resource = resource;
    }

    /**
     * Adds the <code>templateObject</code> to the collection of template objects that will be made available to the
     * plugin during processing.
     *
     * @param templateObject the TemplateObject to add.
     */
    public void addTemplateObject(final TemplateObject templateObject)
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
    public void addMacrolibrary(final String macrolibrary)
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

    private String templateEngineClass;

    /**
     * Sets the template engine class for this cartridge.
     *
     * @param templateEngineClass the Class of the template engine implementation.
     */
    public void setTemplateEngineClass(final String templateEngineClass)
    {
        this.templateEngineClass = templateEngineClass;
    }

    private TemplateEngine templateEngine = null;

    /**
     * @see org.andromda.core.common.Plugin#getTemplateEngine()
     */
    public TemplateEngine getTemplateEngine()
    {
        if (templateEngine == null)
        {
            templateEngine = (TemplateEngine)ComponentContainer.instance().newComponent(templateEngineClass,
                    TemplateEngine.class);
        }
        return templateEngine;
    }

    /**
     * @see org.andromda.core.common.Plugin#getPropertyReferences()
     */
    public Map getPropertyReferences()
    {
        return this.propertyReferences;
    }

    /**
     * Adds a property reference. Property references are those properties that are expected to be supplied by the
     * calling client. These supplied properties are made available to the template during processing.
     *
     * @param reference    the name of the reference.
     * @param defaultValue the default value of the property reference.
     */
    public void addPropertyReference(final String reference, final String defaultValue)
    {
        this.propertyReferences.put(reference, defaultValue);
    }

    /**
     * Populates the <code>templateContext</code> with the properties and template objects defined in the
     * <code>plugin</code>'s descriptor. If the <code>templateContext</code> is null, a new Map instance will be created
     * before populating the context.
     *
     * @param templateContext the context of the template to populate.
     */
    protected void populateTemplateContext(Map templateContext)
    {
        if (templateContext == null)
        {
            templateContext = new HashMap();
        }
        this.addTemplateObjectsToContext(templateContext);
        this.addPropertyReferencesToContext(templateContext);
    }

    /**
     * Takes all the template objects defined in the plugin's descriptor and places them in the
     * <code>templateContext</code>.
     *
     * @param templateContext the template context
     * @param properties      the user properties
     */
    private void addTemplateObjectsToContext(final Map templateContext)
    {
        // add all the TemplateObject objects to the template context
        final Collection templateObjects = this.getTemplateObjects();
        if (templateObjects != null && !templateObjects.isEmpty())
        {
            for (final Iterator templateObjectIterator = templateObjects.iterator(); templateObjectIterator.hasNext();)
            {
                final TemplateObject templateObject = (TemplateObject)templateObjectIterator.next();
                templateContext.put(templateObject.getName(), templateObject.getTemplateObject());
            }
        }
    }

    /**
     * Takes all the property references defined in the plugin's descriptor and looks up the corresponding values
     * supplied by the calling client and supplies them to the <code>templateContext</code>.
     *
     * @param templateContext the template context
     * @param properties      the user properties
     */
    private void addPropertyReferencesToContext(final Map templateContext)
    {
        final Map propertyReferences = this.getPropertyReferences();
        if (propertyReferences != null && !propertyReferences.isEmpty())
        {
            for (final Iterator referenceIterator = propertyReferences.keySet().iterator(); referenceIterator.hasNext();)
            {
                final String reference = (String)referenceIterator.next();
                final String defaultValue = (String)propertyReferences.get(reference);

                // if we have a default value, then don't warn
                // that we don't have a property, otherwise we'll
                // show the warning.
                boolean showWarning = false;
                if (defaultValue == null)
                {
                    showWarning = true;
                }
                // find the property from the namespace
                final Property property = Namespaces.instance().findNamespaceProperty(this.getName(), reference, showWarning);
                // if property isn't ignore, then add it to
                // the context
                if (property != null && !property.isIgnore())
                {
                    templateContext.put(property.getName(), property.getValue());
                }
                else if (defaultValue != null)
                {
                    templateContext.put(reference, defaultValue);
                }
            }
        }
    }

    /**
     * Stores the contents of the plugin.
     */
    private List contents = null;

    /**
     * @see org.andromda.core.common.Plugin#getContents()
     */
    public List getContents()
    {
        if (this.contents == null)
        {
            if (ResourceUtils.isArchive(this.getResource()))
            {
                this.contents = ResourceUtils.getClassPathArchiveContents(this.getResource());
                if (this.getMergeLocation() != null)
                {
                    Collection mergeContents = ResourceUtils.getDirectoryContents(this.getMergeLocation(), 0);
                    if (mergeContents != null && !mergeContents.isEmpty())
                    {
                        this.contents.addAll(mergeContents);
                    }
                }
            }
            else
            {
                // we step down 1 level if its a directory (instead of an
                // archive since we get the contents relative to the plugin
                // resource which is in the META-INF directory
                this.contents = ResourceUtils.getDirectoryContents(this.getResource(), 2);
            }

        }
        return contents;
    }

    /**
     * Retrieves the logger instance that should be used for logging output for the plugin sub classes.
     *
     * @return the logger.
     */
    protected Logger getLogger()
    {
        return AndroMDALogger.getNamespaceLogger(this.name);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}