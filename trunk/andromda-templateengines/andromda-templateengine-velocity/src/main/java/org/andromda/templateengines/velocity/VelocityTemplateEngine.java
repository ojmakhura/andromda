package org.andromda.templateengines.velocity;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.Constants;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Merger;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.ResourceWriter;
import org.andromda.core.templateengine.TemplateEngine;
import org.andromda.core.templateengine.TemplateEngineException;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.Log4JLogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;
import org.apache.velocity.tools.generic.EscapeTool;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * The TemplateEngine implementation for VelocityTemplateEngine template processor.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 * @see "http://jakarta.apache.org/velocity/"
 */
public class VelocityTemplateEngine
    implements TemplateEngine
{
    /**
     * Log4J logger
     */
    protected static Logger logger = null;

    /**
     * Log4J appender
     */
    protected FileAppender appender = null;

    /**
     * The directory we look in to find velocity properties.
     */
    private static final String PROPERTIES_DIR = "META-INF/";

    /**
     * The suffix for the the velocity properties.
     */
    private static final String PROPERTIES_SUFFIX = "-velocity.properties";

    /**
     * The location to which temporary templates are written
     */
    private static final String TEMPORARY_TEMPLATE_LOCATION = Constants.TEMPORARY_DIRECTORY + "velocity/merged";

    /**
     * The location of external templates
     */
    private String mergeLocation;

    /**
     * The current namespace this template engine is running within.
     */
    private String namespace;

    /**
     * The VelocityEngine instance to use
     */
    private VelocityEngine velocityEngine;
    /**
     * The VelocityContext instance to use
     */
    private VelocityContext velocityContext;
    /**
     * The Macro Libraries
     */
    private final List<String> macroLibraries = new ArrayList<String>();

    /**
     * Stores a collection of templates that have already been
     * discovered by the velocity engine
     */
    private final Map<String, Template> discoveredTemplates = new HashMap<String, Template>();

    /**
     * Stores the merged template files that are deleted at shutdown.
     */
    private final Collection<File> mergedTemplateFiles = new ArrayList<File>();

    /**
     * Initialized the engine
     * @param namespace
     * @throws Exception 
     * @see org.andromda.core.templateengine.TemplateEngine#initialize(String)
     */
    public void initialize(final String namespace)
        throws Exception
    {
        this.namespace = namespace;
        this.initLogger(namespace);

        ExtendedProperties engineProperties = new ExtendedProperties();

        // Tell VelocityTemplateEngine it should also use the
        // classpath when searching for templates
        // IMPORTANT: file,andromda.plugins the ordering of these
        // two things matters, the ordering allows files to override
        // the resources found on the classpath.
        engineProperties.setProperty(VelocityEngine.RESOURCE_LOADER, "file,classpath");

        engineProperties.setProperty(
            "file." + VelocityEngine.RESOURCE_LOADER + ".class",
            FileResourceLoader.class.getName());

        engineProperties.setProperty(
            "classpath." + VelocityEngine.RESOURCE_LOADER + ".class",
            ClasspathResourceLoader.class.getName());

        // Configure Velocity logger
        engineProperties.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
              "org.apache.velocity.runtime.log.Log4JLogChute" );                                    
        engineProperties.setProperty(Log4JLogChute.RUNTIME_LOG_LOG4J_LOGGER, logger.getName());

        // Let this template engine know about the macro libraries.
        for (String macroLibrary : getMacroLibraries())
        {
            engineProperties.addProperty(
                    VelocityEngine.VM_LIBRARY,
                    macroLibrary);
        }

        this.velocityEngine = new VelocityEngine();
        this.velocityEngine.setExtendedProperties(engineProperties);

        if (this.mergeLocation != null)
        {
            // set the file resource path (to the merge location)
            velocityEngine.addProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, this.mergeLocation);
        }

        // if the namespace requires a merge add the temporary template
        // location to which merged templates are written
        if (Merger.instance().requiresMerge(this.namespace))
        {
            velocityEngine.addProperty(
                VelocityEngine.FILE_RESOURCE_LOADER_PATH,
                this.getMergedTemplatesLocation());
        }

        this.addProperties(namespace);
        this.velocityEngine.init();
    }

    /**
     * Adds any properties found within META-INF/'plugin name'-velocity.properties
     * @param pluginName name of the plugin
     * @throws java.io.IOException if resource could not be found
     */
    private void addProperties(String pluginName)
        throws IOException
    {
        // see if the velocity properties exist for the current plugin
        URL propertiesUri =
            ResourceUtils.getResource(PROPERTIES_DIR + StringUtils.trimToEmpty(pluginName) + PROPERTIES_SUFFIX);

        if (propertiesUri != null)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("loading properties from --> '" + propertiesUri + '\'');
            }

            Properties properties = new Properties();
            properties.load(propertiesUri.openStream());

            for (Map.Entry entry : properties.entrySet())
            {
                final String property = (String) entry.getKey();
                final String value = (String)entry.getValue();
                if (logger.isDebugEnabled())
                {
                    logger.debug("setting property '" + property + "' with --> '" + value + '\'');
                }
                this.velocityEngine.setProperty(property, value);
            }
        }
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#processTemplate(String, java.util.Map,
     *      java.io.Writer)
     */
    public void processTemplate(
        final String templateFile,
        final Map<String, Object> templateObjects,
        final Writer output)
        throws Exception
    {
        final String methodName = "VelocityTemplateEngine.processTemplate";

        if (logger.isDebugEnabled())
        {
            logger.debug(
                "performing " + methodName + " with templateFile '" + templateFile + "' and templateObjects '" +
                templateObjects + '\'');
        }
        ExceptionUtils.checkEmpty("templateFile", templateFile);
        ExceptionUtils.checkNull("output", output);
        this.velocityContext = new VelocityContext();
        this.loadVelocityContext(templateObjects);

        Template template = this.discoveredTemplates.get(templateFile);
        if (template == null)
        {
            template = this.velocityEngine.getTemplate(templateFile);

            // We check to see if the namespace requires a merge, and if so 
            final Merger merger = Merger.instance();
            if (merger.requiresMerge(this.namespace))
            {
                final String mergedTemplateLocation = this.getMergedTemplateLocation(templateFile);
                final InputStream resource = template.getResourceLoader().getResourceStream(templateFile);
                ResourceWriter.instance().writeStringToFile(
                    merger.getMergedString(resource, this.namespace),
                    mergedTemplateLocation);
                template = this.velocityEngine.getTemplate(templateFile);
                this.mergedTemplateFiles.add(new File(mergedTemplateLocation));
            }
            this.discoveredTemplates.put(templateFile, template);
        }
        template.merge(velocityContext, output);
    }
    
    /**
     * Loads the internal {@link #velocityContext} from the 
     * given Map of template objects.
     * 
     * @param templateObjects Map containing objects to add to the template context.
     */
    private void loadVelocityContext(final Map<String, Object> templateObjects)
    {
        if (templateObjects != null)
        {    
            // copy the templateObjects to the velocityContext
            for (Map.Entry<String, Object> entry : templateObjects.entrySet())
            {
                this.velocityContext.put(entry.getKey(), entry.getValue());
            }
        }
        // add velocity tools (Escape tool)
        this.velocityContext.put("esc", new EscapeTool());
    }

    /**
     * Gets location to which the given <code>templateName</code>
     * has its merged output written.
     * @param templatePath the relative path to the template.
     * @return the complete merged template location.
     */
    private String getMergedTemplateLocation(String templatePath)
    {
        return this.getMergedTemplatesLocation() + '/' + templatePath;
    }

    /**
     * Gets the location to which merge templates are written.  These
     * must be written in order to replace the unmerged ones when Velocity
     * performs its template search.
     *
     * @return the merged templates location.
     */
    private String getMergedTemplatesLocation()
    {
        return TEMPORARY_TEMPLATE_LOCATION + '/' + this.namespace;
    }

    /**
     * The log tag used for evaluation (this can be any abitrary name).
     */
    private static final String LOG_TAG = "logtag";
    
    /**
     * @see org.andromda.core.templateengine.TemplateEngine#getEvaluatedExpression(java.lang.String, java.util.Map)
     */
    public String getEvaluatedExpression(final String expression, final Map<String, Object> templateObjects)
    {
        String evaluatedExpression = null;
        if (StringUtils.isNotBlank(expression))
        {
            // reuse last created context, need it for processing $generateFilename
            if (this.velocityContext == null)
            {
                this.velocityContext = new VelocityContext();
                this.loadVelocityContext(templateObjects);
            }
            
            try
            {
                final StringWriter writer = new StringWriter();
                this.velocityEngine.evaluate(this.velocityContext, writer, LOG_TAG, expression);
                evaluatedExpression = writer.toString();
            }
            catch (final Throwable throwable)
            {
                throw new TemplateEngineException(throwable);
            }
        }
        return evaluatedExpression;
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#getMacroLibraries()
     */
    public List<String> getMacroLibraries()
    {
        return this.macroLibraries;
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#addMacroLibrary(java.lang.String)
     */
    public void addMacroLibrary(String libraryName)
    {
        this.macroLibraries.add(libraryName);
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#setMergeLocation(java.lang.String)
     */
    public void setMergeLocation(String mergeLocation)
    {
        this.mergeLocation = mergeLocation;
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#shutdown()
     */
    public void shutdown()
    {
        //  Deletes the merged templates location (these are the templates that were created just for merging
        //  purposes and so therefore are no longer needed after the engine is shutdown).
        FileUtils.deleteQuietly(new File(TEMPORARY_TEMPLATE_LOCATION));
        this.discoveredTemplates.clear();
        this.velocityEngine = null;
        if(null!=logger && null!=appender)
        {
            logger.removeAppender(appender);
        }
    }
        
    /**
     * Opens a log file for this namespace.
     *
     * @param pluginName  Name of this plugin
     * @throws IOException if the file cannot be opened
     */
    private void initLogger(final String pluginName)
        throws IOException
    {
        logger = AndroMDALogger.getNamespaceLogger(pluginName);
        logger.setAdditivity(false);

        appender =
            new FileAppender(
                new PatternLayout("%-5p %d - %m%n"),
                AndroMDALogger.getNamespaceLogFileName(pluginName),
                true);
        logger.addAppender(appender);
    }
}