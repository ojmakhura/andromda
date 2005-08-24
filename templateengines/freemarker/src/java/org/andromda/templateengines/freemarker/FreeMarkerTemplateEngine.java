package org.andromda.templateengines.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.templateengine.TemplateEngine;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;

/**
 * The TemplateEngine implementation for FreeMarker template processor.
 * 
 * @author Chad Brandon
 * @see http://www.freemarker.org
 */
public class FreeMarkerTemplateEngine
    implements TemplateEngine
{
    /**
     * @see org.andromda.core.templateengine.TemplateEngine#init(java.lang.String)
     */
    public void initialize(String namespace) throws Exception
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
    public void processTemplate(String templateFile, Map templateObjects, Writer output)
        throws Exception
    {
        final String methodName = "processTranslation";
        ExceptionUtils.checkEmpty(methodName, "templateFile", templateFile);
        ExceptionUtils.checkNull(methodName, "output", output);

        if (this.configuration == null)
        {
            this.configuration = new Configuration();
            final Class mainClass = org.andromda.core.AndroMDA.class;
       
            // - tell FreeMarker it should use the classpath when searching for templates
            configuration.setClassForTemplateLoading(mainClass, "/");

            // - use Bean Wrapper, in order to get maximal reflection capabilities
            configuration.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
        } 

        // create the template
        final Template template = configuration.getTemplate(templateFile);

        if (templateObjects == null)
        {
            templateObjects = new HashMap();
        }

        // Merge data model with template
        template.process(templateObjects, output);
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
     * @see org.andromda.core.templateengine.TemplateEngine#getMacroLibraries()
     */
    public List getMacroLibraries()
    {
        return null;
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#addMacroLibrary(java.lang.String)
     */
    public void addMacroLibrary(String macroLibrary)
    {
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#setMergeLocation(java.lang.String)
     */
    public void setMergeLocation(String mergeLocation)
    {
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#getEvaluatedExpression(java.lang.String, java.util.Map)
     */
    public String getEvaluatedExpression(String expression, Map templateObjects)
    {
        return null;
    }

    protected static Logger logger = null;

    /**
     * Opens a log file for this plugin.
     * 
     * @throws IOException if the file cannot be opened
     */
    private void initLogger(String pluginName) throws IOException
    {
        logger = AndroMDALogger.getNamespaceLogger(pluginName);
        logger.setAdditivity(false);
        FileAppender appender = new FileAppender(
            new PatternLayout("%-5p %d - %m%n"),
            AndroMDALogger.getNamespaceLogFileName(pluginName),
            true);
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