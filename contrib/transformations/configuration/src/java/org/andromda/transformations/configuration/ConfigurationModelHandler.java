package org.andromda.transformations.configuration;

import org.andromda.transformers.atl.engine.ATLModelHandler;
import org.atl.engine.vm.nativelib.ASMModel;

/**
 * Model handler for the AndroMDA configuration model.
 *  
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 *
 */
public class ConfigurationModelHandler extends ATLModelHandler {
	
	/**
	 * The type of repository where this kind of model is stored.
	 */
	public static final String REPOSITORY_TYPE_NAME = "ANDROMDA_CONFIG";
	
	/* (non-Javadoc)
	 * @see org.andromda.transformers.atl.engine.ATLModelHandler#writeModel(org.atl.engine.vm.nativelib.ASMModel, java.lang.String)
	 */
	public void writeModel(ASMModel model, String uri) {
		throw new UnsupportedOperationException("method not implemented.");
	}

	/* (non-Javadoc)
	 * @see org.andromda.transformers.atl.engine.ATLModelHandler#getATL()
	 */
	public ASMModel getATL() {
		throw new UnsupportedOperationException("method not implemented.");
	}

	/* (non-Javadoc)
	 * @see org.andromda.transformers.atl.engine.ATLModelHandler#getMOF()
	 */
	public ASMModel getMOF() {
		throw new UnsupportedOperationException("method not implemented.");
	}

	/* (non-Javadoc)
	 * @see org.andromda.transformers.atl.engine.ATLModelHandler#loadModel(java.lang.String, org.atl.engine.vm.nativelib.ASMModel, java.lang.String, java.lang.String[])
	 */
	public ASMModel loadModel(String name, ASMModel metamodel, String uri,
			String[] loadSourceModels) {
		return new ConfigurationModel(name, metamodel, false, null);
	}

	/* (non-Javadoc)
	 * @see org.andromda.transformers.atl.engine.ATLModelHandler#newModel(java.lang.String, org.atl.engine.vm.nativelib.ASMModel)
	 */
	public ASMModel newModel(String name, ASMModel metamodel) {
		return new ConfigurationModel(name, metamodel, false, null);
	}

	/* (non-Javadoc)
	 * @see org.andromda.transformers.atl.engine.ATLModelHandler#getBuiltInMetaModel(java.lang.String)
	 */
	public ASMModel getBuiltInMetaModel(String name) {
		throw new UnsupportedOperationException("method not implemented.");
	}

}
