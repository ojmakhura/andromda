package org.andromda.core.translation.library;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.TemplateObject;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.templateengine.TemplateEngine;
import org.andromda.core.translation.Translator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * The LibraryTranslation object which is the intermediary
 * object between the Library and the child Translation instances.
 * 
 * @author Chad Brandon
 */
public class LibraryTranslation {

	private static Logger logger = Logger.getLogger(LibraryTranslation.class);

	/**
	 * The parent library to which this LibraryTranslation belongs.
	 */
	private Library library;

	private String name;

	private String template;

	/**
	 * The Translator implementation to use. This is required.
	 */
	private String translatorClass;

	/**
	 * After processing by the CartridgeTemplate engine, will contain the
	 * processed translation.
	 */
	private Translation translation;

	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
     * Sets the name.
     * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return String
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * Returns the Library that this LibraryTranslation
	 * belongs too.
	 * 
	 * @return Library
	 */
	public Library getLibrary() {
		return library;
	}

	/**
	 * @param library
	 */
	public void setLibrary(Library library) {
		this.library = library;
	}

	/**
	 * Sets the Translator class that will perform the translation processing.
	 * 
	 * @param translatorClass
	 */
	public void setTranslator(String translatorClass) {
        this.translatorClass = translatorClass;
        ComponentContainer.instance().registerComponentType(translatorClass);
	}

	/**
	 * Gets the Translator instance that will perform processing of the template.
	 * 
	 * @return Translator
	 */  
	public Translator getTranslator() {
        final String methodName = "LibraryTranslation.getTranslator";
        Translator translator = 
            (Translator)ComponentContainer.instance().findComponent(
                this.translatorClass, Translator.class);
        if (translator == null) {
            throw new LibraryException(
                methodName
                + " - a translator implementation must be defined, "
                + " please check your translator library --> '"
                + this.library.getResource() + "'");
        }
        return translator;
	}

	/**
	 * Calls the handlerMethod from a translation fragment.  Each
     * handle method must take a java.lang.String as the first argument 
     * (the body of the fragment from the translation template) and a 
     * java.lang.Object for the second argument (the node being parsed that
     * we may need to retrieve any additional information from).
	 * 
	 * @param name the name of the fragment to retrieve.
	 * @param node the node Object which from the parsed expression.
     * @param kind the kind of the translation fragment to handle.
	 */
	public void handleTranslationFragment(
		String name,
        String kind,
		Object node) {
		final String methodName = "LibraryTranslation.handleTranslationFragment";
        ExceptionUtils.checkNull(methodName, "node", node);
		if (this.translation != null && this.getTranslator() != null) {
            
            String translation = this.getTranslationFragment(name, kind);
            
            //only handle the fragment if we can find the fragment in the 
            //translation template
            if (StringUtils.isNotEmpty(translation)) {
            
    			String handlerMethod =
    				this.translation.getFragment(name).getHandlerMethod();
    			if (StringUtils.isNotEmpty(handlerMethod)) {
                    
                    Class[] argTypes = new Class[] {
                        java.lang.String.class, 
                        java.lang.Object.class
                    };                
                    
    				try {
        
    					Method method =
    						this.getTranslator().getClass().getMethod(
    							handlerMethod,
    							argTypes);               
                        
                        //add the translation as the first arg
                        Object[] args = new Object[] {
                            translation,
                        	node
                        };
                        
                        method.invoke(this.getTranslator(), args);
    				} catch (NoSuchMethodException ex) {
    					String errMsg =
    						"the translator '"
    							+ this.getTranslator().getClass()
    							+ "' must implement the method '"
    							+ handlerMethod
    							+ "'"
    							+ StringUtils.join(argTypes, ",")
    							+ "'"
    							+ " in order to handle processing of the fragment --> '"
    							+ name
    							+ "'";
    					logger.error(errMsg);
    				} catch (Exception ex) {
    					String errMsg = "Error performing " + methodName;
    					logger.error(errMsg, ex);
    					throw new LibraryException(errMsg, ex);
    				}
    			}
            }
		}
	}

	/**
	 * Gets the current "translated" value of this fragmentName for resulting
	 * from the last processTranslation method
	 * 
	 * @param name the name of the fragment to retrieve.
     * @param kind the kind or type of fragment to retrieve 
     * (this is the based on the expression type: body, inv, post, pre, etc).
	 * 
	 * @return String the value of the translated fragment or null of one
	 *         wasn't found with the specified name.
	 */
	public String getTranslationFragment(String name, String kind) {
		String fragment = null;
		if (this.translation != null) {
			fragment = this.translation.getTranslated(name, kind);
		}
		return fragment;
	}

	/**
	 * The processed translation template as a Reader.
	 * 
	 * @param translationInput
	 */
	protected void setTranslation(Reader translationInput) {
		final String methodName = "LibraryTranslation.setTranslation";
		ExceptionUtils.checkNull(
			methodName,
			"translationInput",
			translationInput);
		try {
			this.translation = 
                (Translation)XmlObjectFactory.getInstance(Translation.class).getObject(
                	translationInput);
			this.translation.setLibraryTranslation(this);
		} catch (Exception ex) {
			String errMsg = "Error performing " + methodName;
			logger.error(errMsg, ex);
			throw new LibraryException(errMsg, ex);
		}
	}

	/**
	 * Processes the template belonging to this LibraryTranslation
     * and returns the Translation objects.  If template hasn't
     * been set (i.e. is null, then this method won't do anything
     * but return a null value).
	 * 
	 * @param templateObjects
	 *            any key/value pairs that should be passed to the
	 *            TemplateEngine while processing the translation template.
	 * 
	 * @return Translation the Translation created from the processing the
	 *         translation template.
	 */
	public Translation processTranslation(Map templateObjects) {
		final String methodName = "LibraryTranslation.processTranslation";
		logger.debug(
			"processing translation template --> '"
				+ this.getTemplate()+ "'"
				+ "' with templateObjects --> '"
				+ templateObjects + "'");
        if (this.getTemplate() != null) {
    		if (templateObjects == null) {
    			templateObjects = new HashMap();
    		}
    
            Collection libraryObjects = this.getLibrary().getTemplateObjects();
            if (libraryObjects != null && !libraryObjects.isEmpty()) {
                Iterator libraryObjectIt = libraryObjects.iterator();
                while (libraryObjectIt.hasNext()) {
                     TemplateObject templateObject = 
                        (TemplateObject)libraryObjectIt.next();
                     templateObjects.put(
                         templateObject.getName(), 
                         templateObject.getTemplateObject());
                }
            }
    
    		try {
    			TemplateEngine engine = this.getLibrary().getTemplateEngine();
    
    			StringWriter output = new StringWriter();
    			engine.processTemplate(this.getTemplate(), templateObjects, output);
    			String outputString = output.toString();
    			BufferedReader input =
    				new BufferedReader(new StringReader(outputString));
    			if (logger.isDebugEnabled()) {
    				logger.debug("processed output --> '" + outputString + "'");
    			}
    			//load Reader into the translation
    			this.setTranslation(input);
    		} catch (Exception ex) {
    			String errMsg = "Error performing " + methodName;
    			logger.error(errMsg, ex);
    			throw new LibraryException(errMsg, ex);
    		}
        }
		return this.translation;
	}
    
    /**
     * @see java.lang.Object#toString()
     */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}