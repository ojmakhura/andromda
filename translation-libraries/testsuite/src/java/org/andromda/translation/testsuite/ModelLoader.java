package org.andromda.translation.testsuite;

import java.net.URL;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.Namespace;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.Property;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.repository.RepositoryFacade;
import org.andromda.core.repository.RepositoryFacadeException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
/**
 * Models are required for OCL translation. This class
 * loads  models so that translation tests may be performed
 * during development of a translation-library.
 */
public class ModelLoader {
	
	private static final Logger logger = Logger.getLogger(ModelLoader.class);
    
    private static ModelLoader loader;
	   
	private final String MODELFACADE = "modelFacade";
	private final String REPOSITORY = "repository";
    private final String LANGUAGE_MAPPINGS_URI = "languageMappingsUri";
	
	/**
	 * Specifies the location of the model xmi which to load
	 */
	private final String MODEL_XMI = "model.xmi";
		
	private ModelAccessFacade model = null;
	
	private RepositoryFacade repository = null;
	
	private void loadModel() {
		final String methodName = "ModelLoader.loadModel";
		try {
			this.repository = getRepository();
			this.repository.open();
			URL modelUrl = getModelResource();
			this.repository.readModel(modelUrl, null);
		} catch (Exception ex) {
			String errMsg = "Error performing " + methodName;
			logger.error(errMsg, ex);
			throw new RepositoryFacadeException(errMsg, ex);
		}
	}
	
	/**
	 * Retrieves the model resource location.
	 * 
	 * @return URL the URI of the model resource.
	 */
	private URL getModelResource() {
        final String methodName = "ModelLoader.getModelResource";
		String modelXmiProperty = System.getProperty(MODEL_XMI);
		URL modelXmiResource = null;
		try {
			if(StringUtils.isNotEmpty(modelXmiProperty)) {
                if(logger.isInfoEnabled()) {
                	logger.info("property '" 
                        + MODEL_XMI 
                        + "' set, finding model file --> '" 
                        + modelXmiProperty + "'");
                }
                //first look for the model as a resource
                modelXmiResource = ResourceUtils.getResource(modelXmiProperty);
                //if the model wasn't found, then we'll try it as a literal string
                if(modelXmiResource == null) {
                    modelXmiResource = new URL(modelXmiProperty);
                }
                if(logger.isInfoEnabled()) {
                	logger.info("using model file --> '" + modelXmiResource + "'");
                }
			} else {
				throw new TranslationTestProcessorException(methodName
                    + " no property '" 
                    + MODEL_XMI 
                    + "' was defined, please define this to specify the location of your model");
            }
		} catch (Exception ex) {
			String errMsg = "Error performing getModelResource";
            logger.error(errMsg);
            throw new TranslationTestProcessorException(errMsg, ex);
		}
		return modelXmiResource;
	}
    
    /**
     * Returns the shared instance of this ModelLoader.
     * 
     * @return ModelLoader the shared instance.
     */
    public static ModelLoader instance() {
    	if(loader == null) {
    		loader = new ModelLoader();
        }
        return loader;
    }

	/**
	 * Tests loading and retrieving the model from the repository.
	 * 
	 * @return ModelAccessFacade the facade of the loaded model
	 */
	public ModelAccessFacade getModel() {
        if(this.model == null) {
    		try {    
                this.loadModel();
                this.model = loader.repository.getModel();
                // set the model on the factory so we can start constucting
                // metafacades
                MetafacadeFactory factory = MetafacadeFactory.getInstance();
                factory.setModel(
                    loader.repository.getModel());
                this.intializeDefaultNamespace();
                factory.setActiveNamespace(
                    Namespaces.DEFAULT);
    		} catch (RepositoryFacadeException ex) {
    			String errMsg = "Error performing getModel";
                logger.error(errMsg, ex);
                throw new RepositoryFacadeException(errMsg, ex);
    		}
        }
        return this.model;
	}
    
    /**
     * Initializes the default namespace
     * with the required properties.
     * 
     * TODO: This needs to be handled in a more graceful way
     * since properties can change depending on metafacades.
     */
    private void intializeDefaultNamespace() {
        Namespace namespace = new Namespace();
        namespace.setName(Namespaces.DEFAULT);
        Property property = new Property();
        // set the required properties
        property.setName(LANGUAGE_MAPPINGS_URI);
        property.setValue(System.getProperty(LANGUAGE_MAPPINGS_URI));
        namespace.addProperty(property);
        property = new Property();
        property.setName("maxSqlNameLength");
        property.setIgnore(true);
        namespace.addProperty(property);
        Namespaces.instance().addNamespace(namespace);   
    }
	
	/**
	 * Gets the ModelAccessFacade implementation to use.
	 * Defaults to the implementation specified in the RespositoryConfiguration
	 * if one isn't specified
	 * @return ModelAccessFacade the ModelAccessFacade instance.
	 */
	protected ModelAccessFacade getModelAcessFacade() {
        return (ModelAccessFacade)ComponentContainer.instance().findComponent(
                    System.getProperty(MODELFACADE), ModelAccessFacade.class);        
	}
	
	/**
	 * Gets the Repository implementation to use.
	 * Defaults to the implementation specified in the RespositoryConfiguration
	 * if one isn't specified
	 * @return Repository the Repository instance.
	 * @throws Exception
	 */
	protected RepositoryFacade getRepository() {
		return (RepositoryFacade)ComponentContainer.instance().findComponent(
            System.getProperty(REPOSITORY), RepositoryFacade.class);
	}

}
