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
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogSystem;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * The TemplateEngine implementation for VelocityTemplateEngine template processor.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 * @see http://jakarta.apache.org/velocity/
 */
public class VelocityTemplateEngine
    implements TemplateEngine
{
    protected static Logger logger = null;

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
    private static final String TEMPORARY_TEMPLATE_LOCATION =
        Constants.TEMPORARY_DIRECTORY + "velocity/merged";

    /**
     * The location of external templates
     */
    private String mergeLocation;

    /**
     * Stores additional properties specified within the plugin within the file META-INF/'plugin
     * name'-velocity.properties
     */
    private Properties properties = null;

    /**
     * The current namespace this template engine is running within.
     */
    private String namespace;

    /**
     * the VelocityEngine instance to use
     */
    private VelocityEngine velocityEngine;
    private VelocityContext velocityContext = null;
    private final List macroLibraries = new ArrayList();

    /**
     * Stores a collection of templates that have already been
     * discovered by the velocity engine
     */
    private final Map discoveredTemplates = new HashMap();

    /**
     * Stores the merged template files that are deleted at shutdown.
     */
    private final Collection mergedTemplateFiles = new ArrayList();

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#init(java.lang.String)
     */
    public void init(final String namespace)
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

        // Tell VelocityTemplateEngine not to use its own logger
        // the logger but to use of this plugin.
        engineProperties.setProperty(
            VelocityEngine.RUNTIME_LOG_LOGSYSTEM,
            new VelocityLoggingReceiver());

        // Let this template engine know about the macro libraries.
        for (final Iterator iterator = getMacroLibraries().iterator(); iterator.hasNext();)
        {
            engineProperties.addProperty(
                VelocityEngine.VM_LIBRARY,
                iterator.next());
        }

        this.velocityEngine = new VelocityEngine();
        this.velocityEngine.setExtendedProperties(engineProperties);

        if (this.mergeLocation != null)
        {
            // set the file resource path (to the merge location)
            velocityEngine.addProperty(
                VelocityEngine.FILE_RESOURCE_LOADER_PATH, this.mergeLocation);
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
     */
    private void addProperties(String pluginName)
        throws IOException
    {
        // reset any properties from previous processing
        this.properties = null;

        // see if the velocity properties exist for the current
        // plugin
        URL propertiesUri =
            ResourceUtils.getResource(
                PROPERTIES_DIR + StringUtils.trimToEmpty(pluginName) + PROPERTIES_SUFFIX);

        if (propertiesUri != null)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("loading properties from --> '" + propertiesUri + "'");
            }

            this.properties = new Properties();
            this.properties.load(propertiesUri.openStream());

            for (final Iterator iterator = this.properties.keySet().iterator(); iterator.hasNext();)
            {
                final String property = (String)iterator.next();
                final String value = this.properties.getProperty(property);
                if (logger.isDebugEnabled())
                {
                    logger.debug("setting property '" + property + "' with --> '" + value + "'");
                }
                this.velocityEngine.setProperty(property, value);
            }
        }
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#processTemplate(java.lang.String, java.util.Map,
     *      java.io.StringWriter)
     */
    public void processTemplate(
        final String templateFile,
        final Map templateObjects,
        final Writer output)
        throws Exception
    {
        final String methodName = "VelocityTemplateEngine.processTemplate";

        if (logger.isDebugEnabled())
        {
            logger.debug(
                "performing " + methodName + " with templateFile '" + templateFile +
                "' and templateObjects '" + templateObjects + "'");
        }

        ExceptionUtils.checkEmpty(methodName, "templateFile", templateFile);
        ExceptionUtils.checkNull(methodName, "output", output);
        this.velocityContext = new VelocityContext();

        // copy the templateObjects to the velocityContext
        if (templateObjects != null)
        {
            for (
                final Iterator namesIterator = templateObjects.keySet().iterator();
                namesIterator.hasNext();)
            {
                final String name = (String)namesIterator.next();
                final Object value = templateObjects.get(name);
                this.velocityContext.put(name, value);
            }
        }

        Template template = (Template)this.discoveredTemplates.get(templateFile);
        if (template == null)
        {
            template = this.velocityEngine.getTemplate(templateFile);

            // We check to see if the namespace requires a merge, and if so 
            final Merger merger = Merger.instance();
            if (merger.requiresMerge(this.namespace))
            {
                final String mergedTemplateLocation = this.getMergedTemplateLocation(templateFile);
                final InputStream resource =
                    template.getResourceLoader().getResourceStream(templateFile);
                ResourceWriter.instance().writeStringToFile(
                    merger.getMergedString(resource, this.namespace),
                    mergedTemplateLocation);
                template = this.velocityEngine.getTemplate(templateFile);
                this.mergedTemplateFiles.add(new File(mergedTemplateLocation));
            }
            this.discoveredTemplates.put(templateFile, template);
        }
        template.merge(this.velocityContext, output);
    }

    /**
     * Gets location to which the given <code>templateName</code>
     * has its merged output written.
     * @param templatePath the relative path to the template.
     * @return the complete merged template location.
     */
    private String getMergedTemplateLocation(String templatePath)
    {
        return this.getMergedTemplatesLocation() + "/" + templatePath;
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
        return TEMPORARY_TEMPLATE_LOCATION + "/" + this.namespace;
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#getEvaluatedExpression(java.lang.String)
     */
    public String getEvaluatedExpression(String expression)
    {
        String evaluatedExpression = null;
        if ((this.velocityContext != null) && StringUtils.isNotEmpty(expression))
        {
            try
            {
                StringWriter writer = new StringWriter();
                this.velocityEngine.evaluate(this.velocityContext, writer, "logtag", expression);
                evaluatedExpression = writer.toString();
            }
            catch (Throwable th)
            {
                String errMsg = "Error performing VelocityTemplateEngine.getEvaluatedExpression";
                logger.error(errMsg, th);
                throw new TemplateEngineException(errMsg, th);
            }
        }
        return evaluatedExpression;
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#getMacroLibraries()
     */
    public List getMacroLibraries()
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
        this.deleteMergedTemplatesLocation();
        this.discoveredTemplates.clear();
        this.shutdownLogger();
        this.velocityEngine = null;
    }

    /**
     * Deletes the merged templates location (these
     * are the templates that were created just for merging
     * purposes and so therefore are no longer needed after
     * the engine is shutdown).
     */
    private final void deleteMergedTemplatesLocation()
    {
        File directory = new File(TEMPORARY_TEMPLATE_LOCATION);
        if (directory.getParentFile().isDirectory())
        {
            directory = directory.getParentFile();
            ResourceUtils.deleteDirectory(directory);
            directory.delete();
        }
    }

    /**
     * Opens a log file for this namespace.
     *
     * @throws IOException if the file cannot be opened
     */
    private final void initLogger(String pluginName)
        throws IOException
    {
        logger = AndroMDALogger.getNamespaceLogger(pluginName);
        logger.setAdditivity(false);

        final FileAppender appender =
            new FileAppender(
                new PatternLayout("%-5p %d - %m%n"),
                AndroMDALogger.getNamespaceLogFileName(pluginName),
                true);
        logger.addAppender(appender);
    }

    /**
     * Shutdown the associated logger.
     */
    private final void shutdownLogger()
    {
        for (final Enumeration appenders = logger.getAllAppenders(); appenders.hasMoreElements();)
        {
            final Appender appender = (Appender)appenders.nextElement();
            if (appender.getName() != null)
            {
                appender.close();
            }
        }
    }

    /**
     * <p/>
     * This class receives log messages from VelocityTemplateEngine and forwards them to the concrete logger that is
     * configured for this cartridge. </p>
     * <p/>
     * This avoids creation of one large VelocityTemplateEngine log file where errors are difficult to find and track.
     * </p>
     * <p/>
     * Error messages can now be traced to plugin activities. </p>
     */
    private static class VelocityLoggingReceiver
        implements LogSystem
    {
        /**
         * @see org.apache.velocity.runtime.log.LogSystem#init(org.apache.velocity.runtime.RuntimeServices)
         */
        public void init(RuntimeServices services)
            throws Exception
        {
        }

        /**
         * @see org.apache.velocity.runtime.log.LogSystem#logVelocityMessage(int, java.lang.String)
         */
        public void logVelocityMessage(
            int level,
            String message)
        {
            switch (level)
            {
            case LogSystem.WARN_ID:
                logger.info(message);
                break;
            case LogSystem.INFO_ID:
                logger.info(message);
                break;
            case LogSystem.DEBUG_ID:
                logger.debug(message);
                break;
            case LogSystem.ERROR_ID:
                logger.info(message);
                break;
            default:
                logger.debug(message);
                break;
            }
        }
    }
}