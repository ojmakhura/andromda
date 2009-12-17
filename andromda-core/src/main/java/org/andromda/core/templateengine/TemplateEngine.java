package org.andromda.core.templateengine;

import java.io.Writer;

import java.util.List;
import java.util.Map;


/**
 * The interface that all templates engines used within AndroMDA must implement.
 * It allows us to plug-in the template engine to use for processing of
 * templates used by the system.
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public interface TemplateEngine
{
    /**
     * Initializes the TempateEngine.
     *
     * @param namespace The name of a namespace this can be used for whatever the
     *        template engine implementation likes. For example, it can help
     *        determine the name of the log file to which output is logged.
     * @throws Exception 
     */
    public void initialize(String namespace)
        throws Exception;

    /**
     * Processes a template.
     *
     * @param templateFile the path to the template file that will be processed.
     * @param templateObjects any additional objects we wish to make available
     *        to the translation template that is processed
     * @param output the Writer to which to write the output of the processing.
     * @throws Exception any exception that may occur
     */
    public void processTemplate(
        String templateFile,
        Map<String, Object> templateObjects,
        Writer output)
        throws Exception;

    /**
     * Shuts down the template engine. The meaning of this is defined by the
     * template engine itself. At least, it should close any logfiles.
     */
    public void shutdown();

    /**
     * Returns the list of macro libraries used within this template engine.
     *
     * @return List the list of macros
     */
    public List<String> getMacroLibraries();

    /**
     * Adds a a macro library for use within this template engine.
     *
     * @param macroLibrary
     */
    public void addMacroLibrary(String macroLibrary);

    /**
     * Sets the location of <code>merge</code> templates. These are templates
     * that will be merged into cartridges during processing from an external
     * location. This allows the ability to define templates external to plugins
     * so that these templates can override plugin templates in order to provide
     * customization.
     *
     * @param mergeLocation the location of the merge files.
     */
    public void setMergeLocation(String mergeLocation);
    
    /**
     * Evaluates the <code>expression</code> contained within the template
     * being processed and returns the result.
     *  
     * @param expression the expression to evaluate.
     * @param templateObjects any additional objects we wish to make available
     *        to the template engine when the expression is evaluated.  It this is null
     *        there will be nothing to be evaluated and therefore this operation will return
     *        null.
     * @return the result of the evaluated expression as a String.
     */
    public String getEvaluatedExpression(String expression, Map<String, Object> templateObjects);
}