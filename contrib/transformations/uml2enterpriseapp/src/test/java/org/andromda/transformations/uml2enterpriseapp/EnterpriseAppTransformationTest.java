package org.andromda.transformations.uml2enterpriseapp;

import java.net.URL;

import junit.framework.TestCase;

import org.andromda.transformers.atl.ATLTransformer;
import org.andromda.transformers.atl.Library;
import org.andromda.transformers.atl.Model;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;


/**
 * This test performs the transformation from a UML model to an EnterpriseApp model
 *
 * @author Chad Brandon
 * @author Matthias Bohlen
 */
public class EnterpriseAppTransformationTest
    extends TestCase
{
    Logger logger = Logger.getLogger(EnterpriseAppTransformationTest.class);

    public void testTransform()
        throws Exception
    {
        BasicConfigurator.configure();

        final ATLTransformer transformer = new ATLTransformer();

        final String property = System.getProperty("module.search.paths");
        final String[] moduleSearchPaths = property != null ? property.split(",") : null;

        // - the path of the package location of the test resources
        final URL testResourceUrl = EnterpriseAppTransformationTest.class.getResource("/atl");
        assertNotNull(testResourceUrl);
        final String testResourcePath = testResourceUrl.toString() + '/';

        final String atlPath = testResourcePath + "UML2EnterpriseApp.atl";

        final String mdrRepository = "MDR";

        // - set up the UML meta model (this is the input model's metamodel)
        final String umlMetamodelName = "UML";
        final Model umlMetamodel = new Model();
        final URL umlMetamodelUrl = EnterpriseAppTransformationTest.class.getResource("/M2_DiagramInterchangeModel.xml");
        assertNotNull(umlMetamodelUrl);
        final String umlMetamodelPath = umlMetamodelUrl.toString();
        logger.log(
            Priority.DEBUG,
            "umlMetamodelPath = " + umlMetamodelPath);
        umlMetamodel.setName(umlMetamodelName);
        umlMetamodel.setPath(umlMetamodelPath.toString());
        umlMetamodel.setRepository(mdrRepository);

        // - setup the source model.
        final Model sourceModel = new Model();
        sourceModel.setName("IN");
        sourceModel.setRepository(mdrRepository);
        final URL sourceModelUrl = EnterpriseAppTransformationTest.class.getResource("/EnterpriseAppTransformationTests.xml.zip");
        assertNotNull(sourceModelUrl);
        final String sourceModelPath = "jar:" + sourceModelUrl + "!/EnterpriseAppTransformationTests.xml";
        sourceModel.setPath(sourceModelPath.toString());
        sourceModel.setRepository(mdrRepository);

        // - set this first input model's meta model as UML
        sourceModel.setMetamodel(umlMetamodelName);

        // - setup the target metamodel (this is the output model's metamodel)
        final String enterpriseAppMetamodelName = "EnterpriseApp";
        final Model enterpriseAppMetamodel = new Model();
        enterpriseAppMetamodel.setName(enterpriseAppMetamodelName);
        final URL enterpriseAppMetamodelUrl = EnterpriseAppTransformationTest.class.getResource("/EnterpriseAppMetamodel.xml");
        assertNotNull(enterpriseAppMetamodelUrl);
        final String enterpriseAppMetamodelPath = enterpriseAppMetamodelUrl.toString();
        enterpriseAppMetamodel.setPath(enterpriseAppMetamodelPath);
        enterpriseAppMetamodel.setRepository(mdrRepository);

        // - setup the target model
        final Model targetModel = new Model();
        targetModel.setName("OUT");
        targetModel.setRepository(mdrRepository);

        // - this is the path of the transformed output model
        final String targetModelPath = testResourcePath + "transformed-output.xmi";
        targetModel.setPath(targetModelPath);
        targetModel.setMetamodel(enterpriseAppMetamodelName);
        targetModel.setRepository(mdrRepository);

        // - load up the metamodels and models
        Model[] metamodels = new Model[] {umlMetamodel, enterpriseAppMetamodel};
        Model[] sourceModels = new Model[] {sourceModel};
        Model[] targetModels = new Model[] {targetModel};

        // - add the libraries
        final URL librariesUrl = EnterpriseAppTransformationTest.class.getResource("/atl/UMLHelpers.atl");
        assertNotNull(librariesUrl);
        final String umlHelpersPath = librariesUrl.toString();
        Library library = new Library();
        library.setName("UMLHelpers");
        library.setPath(umlHelpersPath);

        // - perform the transformation
        transformer.transform(
            atlPath,
            new Library[] {library},
            metamodels,
            sourceModels,
            targetModels,
            moduleSearchPaths);
    }
}