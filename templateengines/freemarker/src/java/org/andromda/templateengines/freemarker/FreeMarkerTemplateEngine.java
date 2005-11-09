package org.andromda.templateengines.freemarker;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.templateengine.TemplateEngine;
import org.andromda.core.templateengine.TemplateEngineException;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;


/**
 * The TemplateEngine implementation for the FreeMarker template processor.
 *
 * @author Chad Brandon
 * @author Olaf Muliawan
 * @see http://www.freemarker.org
 */
public class FreeMarkerTemplateEngine
    implements TemplateEngine
{
    /**
     * @see org.andromda.core.templateengine.TemplateEngine#init(java.lang.String)
     */
    public void initialize(String namespace)
        throws Exception
    {
        this.initLogger(namespace);
    }

    /**
     * The main Configuration object of Freemarker.
     */
    protected Configuration configuration = null;

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#processTemplate(java.lang.String,
     *      java.util.Map, java.io.Writer)
     */
    public void processTemplate(
        String templateFile,
        Map templateObjects,
        Writer output)
        throws Exception
    {
        ExceptionUtils.checkEmpty(
            "templateFile",
            templateFile);
        ExceptionUtils.checkNull(
            "output",
            output);

        if (this.configuration == null)
        {
            this.configuration = new Configuration();

            // - tell FreeMarker it should use the classpath when searching for templates
            this.configuration.setClassForTemplateLoading(
                org.andromda.core.AndroMDA.class,
                "/");

            // - use Bean Wrapper, in order to get maximal reflection capabilities
            this.configuration.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
        }

        // - create the template
        final Template template = this.configuration.getTemplate(templateFile);

        if (templateObjects == null)
        {
            templateObjects = new HashMap();
        }

        // - merge data model with template
        template.process(
            templateObjects,
            output);
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#shutdown()
     */
    public void shutdown()
    {
        this.shutdownLogger();
        this.configuration = null;
    }

    /**
     * Stores the macro libraries the template.
     */
    private List macroLibraries = new ArrayList();

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
    public void addMacroLibrary(String macroLibrary)
    {
        this.macroLibraries.add(macroLibrary);
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#setMergeLocation(java.lang.String)
     */
    public void setMergeLocation(String mergeLocation)
    {
    }
    
    /**
     * The name of the temporary string template.
     */
    private static final String STRING_TEMPLATE = "stringTemplate";

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#getEvaluatedExpression(java.lang.String, java.util.Map)
     */
    public String getEvaluatedExpression(
        final String expression,
        Map templateObjects)
    {
        try
        {
            // - create the template
            final Template template = new Template(STRING_TEMPLATE, new StringReader(expression), new Configuration());

            if (templateObjects == null)
            {
                templateObjects = new HashMap();
            }

            final StringWriter output = new StringWriter();

            // - merge data model with template
            template.process(
                templateObjects,
                output);

            return output.toString();
        }
        catch (final Throwable throwable)
        {
            throw new TemplateEngineException(throwable);
        }
    }

    protected static Logger logger = null;

    /**
     * Opens a log file for this plugin.
     *
     * @throws IOException if the file cannot be opened
     */
    private void initLogger(String pluginName)
        throws IOException
    {
        logger = AndroMDALogger.getNamespaceLogger(pluginName);
        logger.setAdditivity(false);
        FileAppender appender =
            new FileAppender(new PatternLayout("%-5p %d - %m%n"),
                AndroMDALogger.getNamespaceLogFileName(pluginName), true);
        logger.addAppender(appender);
    }

    /**
     * Shutdown the associated logger.
     */
    private void shutdownLogger()
    {
        Enumeration appenders = logger.getAllAppenders();
        while (appenders.hasMoreElements())
        {
            Appender appender = (Appender)appenders.nextElement();
            if (appender.getName() != null)
            {
                appender.close();
            }
        }
    }
}