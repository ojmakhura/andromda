package org.andromda.templateengines.velocity;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.templateengine.TemplateEngine;
import org.andromda.core.templateengine.TemplateEngineException;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogSystem;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * The TemplateEngine implementation for VelocityTemplateEngine template processor.
 * (@see http://jakarta.apache.org/velocity/)
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 */
public class VelocityTemplateEngine implements TemplateEngine {

	private static Logger logger = null;

    private List macrolibs = new ArrayList();
	
	/**
	 * Constructs an instance of VelocityTemplateEngine.
	 */
	public VelocityTemplateEngine() {}

	/**
	 *  the VelocityEngine instance to use
	 */
	private VelocityEngine ve;
    
    private VelocityContext velocityContext = null;
    
    /**
     * @see org.andromda.core.templateengine.TemplateEngine#init(java.lang.String, java.util.Properties)
     */
	public void init(String cartridgeName, Properties properties) throws Exception {
    
        initLogger(cartridgeName);
        
        if (ve == null) {
            ve = new VelocityEngine();

            ExtendedProperties ep = null;
            if (properties != null) {
                // Tell VelocityTemplateEngine it should also use the classpath when searching for templates
                ep = ExtendedProperties.convertProperties(properties);
            } else {
                ep = new ExtendedProperties();
            }
    
            ep.addProperty(
                VelocityEngine.RESOURCE_LOADER,
                "andromda.cartridges,file");
    
            ep.setProperty(
                "andromda.cartridges."
                    + VelocityEngine.RESOURCE_LOADER
                    + ".class",
                ClasspathResourceLoader.class.getName());
    
            // Tell VelocityTemplateEngine not to use its own logger but to use the logger
            // of this cartridge.
            ep.setProperty(
                VelocityEngine.RUNTIME_LOG_LOGSYSTEM,
                new VelocityLoggingReceiver());
    
            // Let VelocityTemplateEngine know about the macro libraries.
            for (Iterator iter = getMacroLibraries().iterator();
                iter.hasNext();
                )
            {
                String libraryName = (String) iter.next();
                ep.addProperty(VelocityEngine.VM_LIBRARY, libraryName);
            }
    
            ve.setExtendedProperties(ep);
            ve.init();
        }
	}

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#processTemplate(java.lang.String, java.util.Map, java.io.StringWriter)
     */
	public void processTemplate(
		String templateFile,
		Map templateObjects, 
		StringWriter output)
		throws Exception {
        
		final String methodName = "processTemplate";
		ExceptionUtils.checkEmpty(methodName, "templateFile", templateFile);
		ExceptionUtils.checkNull(methodName, "output", output);
		if (logger.isDebugEnabled())
			logger.debug("performing " + methodName 
				+ " with templateFile '" + templateFile 
				+ "' and output '"
				+ output + "'");
		
		this.velocityContext = new VelocityContext();

		//copy the templateObjects to the velocityContext
		if (templateObjects != null) {
			Iterator namesIt = templateObjects.keySet().iterator();
			while (namesIt.hasNext()) {
				String name = (String)namesIt.next();
				Object value = templateObjects.get(name);
				velocityContext.put(name, value);
			}
		}

		// Process the VSL template with the context and write out
		// the result as the ouput

		Template template = ve.getTemplate(templateFile);
		template.merge(velocityContext, output);
	}
    
    /**
     * @see org.andromda.core.templateengine.TemplateEngine#getEvaluatedExpression(java.lang.String)
     */
    public String getEvaluatedExpression(String expression) {
    	String evaluatedExpression = null;
        if (this.velocityContext != null && StringUtils.isNotEmpty(expression)) {
            
            try {
                StringWriter writer = new StringWriter();
     
                ve.evaluate(
                    this.velocityContext,
                    writer,
                    "mylogtag",
                    expression);
                evaluatedExpression = writer.toString();
            } catch (Throwable th) {
            	String errMsg = 
                    "Error performing VelocityTemplateEngine.getEvaluatedExpression";
                logger.error(errMsg, th);
                throw new TemplateEngineException(errMsg, th);
            }
        }
        return evaluatedExpression;
    }
    
    /**
     * @see org.andromda.cartridges.interfaces.CartridgeDescriptor#getMacroLibraries()
     */
    public List getMacroLibraries()
    {
        return macrolibs;
    }
    
    /**
     * @see org.andromda.cartridges.interfaces.CartridgeDescriptor#addMacroLibrary(java.lang.String)
     */
    public void addMacroLibrary(String libraryName)
    {
        this.macrolibs.add(libraryName);
    }
    
    /**
     * <p>This class receives log messages from VelocityTemplateEngine and
     * forwards them to the concrete logger that is configured
     * for this cartridge.</p>
     * 
     * <p>This avoids creation of one large VelocityTemplateEngine log file
     * where errors are difficult to find and track.</p>
     * 
     * <p>Error messages can now be traced to cartridge activities.</p>
     */
    private class VelocityLoggingReceiver implements LogSystem
    {
    	/**
    	 * @see org.apache.velocity.runtime.log.LogSystem#init(org.apache.velocity.runtime.RuntimeServices)
    	 */
        public void init(RuntimeServices arg0) throws Exception
        {
        }

        /**
         * @see org.apache.velocity.runtime.log.LogSystem#logVelocityMessage(int, java.lang.String)
         */
        public void logVelocityMessage(int level, String message)
        {
            switch (level)
            {
                case LogSystem.WARN_ID :
                    logger.warn(message);
                    break;
                case LogSystem.INFO_ID :
                    logger.info(message);
                    break;
                case LogSystem.DEBUG_ID :
                    logger.debug(message);
                    break;
                case LogSystem.ERROR_ID :
                    logger.error(message);
                    break;
                default :
                    logger.debug(message);
                    break;
            }
        }
    }
    
    /**
     * @see org.andromda.core.templateengine.TemplateEngine#shutdown()
     */
    public void shutdown()
    {
        this.shutdownLogger();
    }

    /**
     * Opens a log file for this cartridge.
     * @throws IOException if the file cannot be opened
     */
    private void initLogger(String cartridgeName) throws IOException
    {
        logger =
            Logger.getLogger("org.andromda.cartridges." + cartridgeName);
        logger.setAdditivity(false);
        logger.setLevel(Level.ALL);

        String logfile = "andromda-" + cartridgeName + ".log";
        FileAppender appender =
            new FileAppender(new PatternLayout("%d - %m%n"), logfile, true);
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
            Appender appender = (Appender) appenders.nextElement();
            if (appender.getName() != null) {
                appender.close();
            }
        }
    }

}
