package org.andromda.core.templateengine;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * The interface that all templates engines used within AndroMDA must implement.
 * It allows us to plug-in the template engine to use for processing
 * of templates used by the system.
 * 
 * @author Chad Brandon
 */
public interface TemplateEngine {
	
	/**
	 * Initializes the TempateEngine.
	 * 
     * @param cartridgeName The name of 
     *        the cartridge this can be used for whatever the
     *        template engine implementation likes.  For example, 
     *        it can help determine the  name of the log file 
     *        to which output is logged.
	 * @param properties any initialization properties
	 *        to pass to the TemplateEngine. 
	 */
	public void init(String cartridgeName, Properties properties) throws Exception;
	
	/**
	 * Processes a template.
	 * 
	 * @param templateFile the path to the template file that will be processed.
	 * @param templateObjects any additional objects we wish to make available to the
	 *        translation template that is processed
	 * @param output the Writer to which to write the output of the processing.
	 * @throws Exception any exception that may occur
	 */
	public void processTemplate(
		String templateFile, 
		Map templateObjects, 
		StringWriter output) throws Exception;
    
    /**
     * Shuts down the template engine. The meaning of this is defined
     * by the template engine itself. At least, it should close any logfiles.
     */    
    public void shutdown();
    
    /**
     * Returns the list of macro libraries used within
     * this template engine.
     * 
     * @return List the list of macros
     */
    public List getMacroLibraries();
    
    /**
     * Adds a a macro library for use within this 
     * template engine.
     * 
     * @param macroLibrary
     */
    public void addMacroLibrary(String macroLibrary);
    
    /**
     * Evaluates the <code>expression</code>
     * contained within the template being processed
     * and returns the result.
     * 
     * @return the evaluated expression String.
     */
    public String getEvaluatedExpression(String expression);

}
