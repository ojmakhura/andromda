package org.andromda.core.common;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.andromda.core.configuration.NamespaceProperties;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Property;
import org.andromda.core.namespace.BaseNamespaceComponent;
import org.andromda.core.templateengine.TemplateEngine;
import org.apache.log4j.Logger;


/**
 * Represents the base plugin of AndroMDA. All Plugin instances inherit from this class.
 *
 * @author Chad Brandon
 */
public abstract class BasePlugin
    extends BaseNamespaceComponent
    implements Plugin
{
    /**
     * Property references made available to the plugin
     */
    private final Collection propertyReferences = new ArrayList();

    /**
     * The template objects made available to templates of this BasePlugin.
     */
    private final Collection templateObjects = new ArrayList();

    /**
     * @see org.andromda.core.common.Plugin#initialize()
     */
    public void initialize()
        throws Exception
    {
        // set the template engine merge location (this needs to be
        // set before the template engine is initialized) so that the
        // merge property can be set once on the template engine.
        final Property mergeProperty =
            Namespaces.instance().getProperty(
                this.getNamespace(),
                NamespaceProperties.MERGE_LOCATION,
                false);
        this.mergeLocation = mergeProperty != null ? new File(mergeProperty.getValue()).toURL() : null;
        if (this.mergeLocation != null)
        {
            this.getTemplateEngine().setMergeLocation(this.getMergeLocation().getFile());
        }
        this.getTemplateEngine().initialize(this.getNamespace());
        for (final Iterator iterator = this.templateObjects.iterator(); iterator.hasNext();)
        {
            final TemplateObject templateObject = (TemplateObject)iterator.next();
            templateObject.setResource(this.getResource());
            templateObject.setNamespace(this.getNamespace());
        }
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
     * Adds the <code>templateObject</code> to the collection of template objects that will be made available to the
     * plugin during processing.
     *
     * @param templateObject the TemplateObject to add.
     */
    public void addTemplateObject(final TemplateObject templateObject)
    {
        if (templateObject != null)
        {
            this.templateObjects.add(templateObject);
        }
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

    /**
     * The template engine that this plugin will use.
     */
    private TemplateEngine templateEngine = null;

    /**
     * @see org.andromda.core.common.Plugin#getTemplateEngine()
     */
    public TemplateEngine getTemplateEngine()
    {
        if (this.templateEngine == null)
        {
            this.templateEngine =
                (TemplateEngine)ComponentContainer.instance().newComponent(
                    this.templateEngineClass, TemplateEngine.class);
        }
        return this.templateEngine;
    }

    /**
     * @see org.andromda.core.common.Plugin#getPropertyReferences()
     */
    public String[] getPropertyReferences()
    {
        return (String[])this.propertyReferences.toArray(new String[0]);
    }

    /**
     * Adds a property reference. Property references are those properties that are expected to be supplied by the
     * calling client. These supplied properties are made available to the template during processing.
     *
     * @param reference the namespace of the reference.
     */
    public void addPropertyReference(final String reference)
    {
        this.propertyReferences.add(reference);
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
            templateContext = new LinkedHashMap();
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
            for (final Iterator iterator = templateObjects.iterator(); iterator.hasNext();)
            {
                final TemplateObject templateObject = (TemplateObject)iterator.next();
                templateContext.put(
                    templateObject.getName(),
                    templateObject.getObject());
            }
        }
    }

    /**
     * Takes all the property references defined in the plugin's descriptor and looks up the corresponding values
     * supplied by the calling client and supplies them to the <code>templateContext</code>.
     *
     * @param templateContext the template context
     */
    private final void addPropertyReferencesToContext(final Map templateContext)
    {
        final String[] propertyReferences = this.getPropertyReferences();
        if (propertyReferences != null && propertyReferences.length > 0)
        {
            final Namespaces namespaces = Namespaces.instance();
            for (int ctr = 0; ctr < propertyReferences.length; ctr++)
            {
                final String reference = propertyReferences[ctr];
                templateContext.put(
                    reference,
                    namespaces.getPropertyValue(
                        this.getNamespace(),
                        reference));
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
                    final Collection mergeContents = ResourceUtils.getDirectoryContents(
                            this.getMergeLocation(),
                            0);
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
                this.contents = ResourceUtils.getDirectoryContents(
                        this.getResource(),
                        2);
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
        return AndroMDALogger.getNamespaceLogger(this.getNamespace());
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return super.toString() + "[" + this.getNamespace() + "]";
    }
}