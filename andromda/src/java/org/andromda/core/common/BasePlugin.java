package org.andromda.core.common;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
     * Adds the <code>templateObject</code> to the collection of template
     * objects that will be made available to the plugin during processing.
     * 
     * @param templateObject the TemplateObject to add.
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
        return (TemplateEngine)ComponentContainer.instance().findComponent(
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
     * Populates the <code>templateContext</code> with the properties and
     * template objects defined in the <code>plugin</code>'s descriptor. If
     * the <code>templateContext</code> is null, a new Map instance will be
     * created before populating the context.
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
                TemplateObject templateObject = (TemplateObject)templateObjectIt
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
                String reference = (String)referenceIt.next();
                String defaultValue = (String)propertyReferences.get(reference);

                // if we have a default value, then don't warn
                // that we don't have a property, otherwise we'll
                // show the warning.
                boolean showWarning = false;
                if (defaultValue == null)
                {
                    showWarning = true;
                }
                // find the property from the namespace
                Property property = Namespaces.instance()
                    .findNamespaceProperty(
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
     * Stores the contents of the plugin.
     */
    private List contents = null;

    /**
     * @todo get contents needs to work with directories 
     *       the same as with archives.
     * @see org.andromda.core.common.Plugin#getContents()
     */
    public List getContents()
    {
        if (contents == null)
        {
            this.contents = new ArrayList();
            if (this.isArchive())
            {
	            ZipFile archive = this.getArchive();
	            if (archive != null)
	            {
	                Enumeration entries = archive.entries();
	                while (entries.hasMoreElements())
	                {
	                    ZipEntry entry = (ZipEntry)entries.nextElement();
	                    contents.add(entry.getName());
	                }
	            }
            }
            else if (this.getResource() != null)
            {
                URL resourceUrl = this.getResource();
                File fileResource = new File(resourceUrl.getFile());
                // we go two levels since descriptors reside in META-INF
                // and we want the parent of the META-INF directory
                String[] contentArray = 
                    fileResource.getParentFile().getParentFile().list();
                if (contentArray != null)
                {
                    contents.addAll(Arrays.asList(contentArray)); 
                }            
            }
        }
        return contents;
    }

    /**
     * All archive files start with this prefix.
     */
    private static final String ARCHIVE_PREFIX = "jar:";

    /**
     * Returns true/false on whether or not this plugin is an archive
     * or not.  If its not an archive, its a directory.
     * 
     * @return true if its an archive, false otherwise.
     */
    private boolean isArchive()
    {
        return this.getResource() != null
            && this.getResource().toString().startsWith(ARCHIVE_PREFIX);
    }

    /**
     * If this plugin's <code>resource</code> is found within a archive file,
     * this method gets the archive to which it belongs.
     * 
     * @return the archive as a ZipFile
     */
    protected ZipFile getArchive()
    {
        final String methodName = "BasePlugin.getArchive";
        try
        {
            ZipFile archive = null;
            URL resource = this.getResource();
            if (resource != null)
            {
                String resourceUrl = resource.toString();
                resourceUrl = resourceUrl.replaceFirst(ARCHIVE_PREFIX, "");
                int entryPrefixIndex = resourceUrl.indexOf('!');
                if (entryPrefixIndex != -1)
                {
                    resourceUrl = resourceUrl.substring(0, entryPrefixIndex);
                }
                archive = new ZipFile(new URL(resourceUrl).getFile());
            }
            return archive;
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing " + methodName;
            logger.error(errMsg, th);
            throw new PluginException(errMsg, th);
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
     * @param loggerName The name of the logger to set.
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