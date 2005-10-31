package org.andromda.transformations.spring;

import java.net.URL;

import junit.framework.TestCase;

import org.andromda.core.configuration.Configuration;
import org.andromda.transformations.configuration.ConfigurationModelHandler;
import org.andromda.transformers.atl.ATLTransformer;
import org.andromda.transformers.atl.Library;
import org.andromda.transformers.atl.Model;
import org.andromda.transformers.atl.engine.ATLModelHandler;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * This test performs the transformation from a UML model to a spring model
 * 
 * @author Chad Brandon
 * @author Matthias Bohlen
 */
public class SpringTransformationTest extends TestCase {
	Logger logger = Logger.getLogger(SpringTransformationTest.class);

	public void testTransform() throws Exception {
		BasicConfigurator.configure();

        initializeAndroMDAConfiguration();

		final ATLTransformer transformer = new ATLTransformer();

		final String property = System.getProperty("module.search.paths");
		final String[] moduleSearchPaths = property != null ? property
				.split(",") : null;

		// - the path of the package location of the test resources
		final URL testResourceUrl = SpringTransformationTest.class
				.getResource("/atl");
		assertNotNull(testResourceUrl);
		final String testResourcePath = testResourceUrl.toString() + '/';

		final String atlPath = testResourcePath + "UML2Spring.atl";

		final String mdrRepository = "MDR";

		// - set up the UML meta model (this is the input model's metamodel)
		final String umlMetamodelName = "UML";
		final Model umlMetamodel = new Model();
		final URL umlMetamodelUrl = SpringTransformationTest.class
				.getResource("/M2_DiagramInterchangeModel.xml");
		assertNotNull(umlMetamodelUrl);
		final String umlMetamodelPath = umlMetamodelUrl.toString();
		logger.log(Priority.DEBUG, "umlMetamodelPath = " + umlMetamodelPath);
		umlMetamodel.setName(umlMetamodelName);
		umlMetamodel.setPath(umlMetamodelPath.toString());
		umlMetamodel.setRepository(mdrRepository);

		// - setup the source model.
		final Model sourceModel = new Model();
		sourceModel.setName("IN");
		sourceModel.setRepository(mdrRepository);
		final URL sourceModelUrl = SpringTransformationTest.class
				.getResource("/SpringTransformationTests.xml.zip");
		assertNotNull(sourceModelUrl);
		final String sourceModelPath = "jar:" + sourceModelUrl
				+ "!/SpringTransformationTests.xml";
		sourceModel.setPath(sourceModelPath.toString());
		sourceModel.setRepository(mdrRepository);
		// - set this first input model's meta model as UML
		sourceModel.setMetamodel(umlMetamodelName);

		// - setup the second source metamodel (for AndroMDA configuration
		// elements)
		final String configMetamodelName = "Configuration";
		final Model configMetamodel = new Model();
		configMetamodel.setName(configMetamodelName);
		final URL configMetamodelUrl = SpringTransformationTest.class
				.getResource("/ConfigurationMetamodel.xml");
		assertNotNull(configMetamodelUrl);
		final String configMetamodelPath = configMetamodelUrl.toString();
		configMetamodel.setPath(configMetamodelPath);
		configMetamodel.setRepository(mdrRepository);

		// register a dummy handler for our special kind of "model"
		ATLModelHandler.registerModelHandler(
				ConfigurationModelHandler.REPOSITORY_TYPE_NAME,
				new ConfigurationModelHandler());

		// - setup the second source model (for AndroMDA configuration elements)
		final Model configModel = new Model();
		configModel.setName("Config");
		configModel.setMetamodel(configMetamodelName);
		configModel.setRepository(ConfigurationModelHandler.REPOSITORY_TYPE_NAME);

		// - setup the target metamodel (this is the output model's metamodel)
		final String springMetamodelName = "Spring";
		final Model springMetamodel = new Model();
		springMetamodel.setName(springMetamodelName);
		final URL springMetamodelUrl = SpringTransformationTest.class
				.getResource("/SpringMetamodel.xml");
		assertNotNull(springMetamodelUrl);
		final String springMetamodelPath = springMetamodelUrl.toString();
		springMetamodel.setPath(springMetamodelPath);
		springMetamodel.setRepository(mdrRepository);

		// - setup the target model
		final Model targetModel = new Model();
		targetModel.setName("OUT");
		targetModel.setRepository(mdrRepository);
		// - this is the path of the transformed output model
		final String targetModelPath = testResourcePath
				+ "transformed-output.xmi";
		targetModel.setPath(targetModelPath);
		targetModel.setMetamodel(springMetamodelName);
		targetModel.setRepository(mdrRepository);

		// - load up the metamodels and models
		Model[] metamodels = new Model[] { umlMetamodel, configMetamodel,
				springMetamodel };
		Model[] sourceModels = new Model[] { sourceModel, configModel };
		Model[] targetModels = new Model[] { targetModel };

		// - add the libraries
		final URL librariesUrl = SpringTransformationTest.class
				.getResource("/atl/UMLHelpers.atl");
		assertNotNull(librariesUrl);
		final String umlHelpersPath = librariesUrl.toString();
		Library library = new Library();
		library.setName("UMLHelpers");
		library.setPath(umlHelpersPath);

		// - perform the transformation
		transformer.transform(atlPath, new Library[] { library }, metamodels,
				sourceModels, targetModels, moduleSearchPaths);
	}

    private void initializeAndroMDAConfiguration()
    {
        // find the XML file with the test configuration for AndroMDA
        URL uri = SpringTransformationTest.class
                .getResource("configuration.xml");
        assertNotNull(uri);
        Configuration configuration = Configuration.getInstance(uri);
        assertNotNull(configuration);

        // initialize the AndroMDA configuration
        configuration.initialize();
    }
}
