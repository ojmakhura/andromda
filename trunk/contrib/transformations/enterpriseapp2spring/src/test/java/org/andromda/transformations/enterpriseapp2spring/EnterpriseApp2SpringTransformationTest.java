package org.andromda.transformations.enterpriseapp2spring;

import java.net.URL;

import junit.framework.TestCase;

import org.andromda.transformers.atl.ATLTransformer;
import org.andromda.transformers.atl.Library;
import org.andromda.transformers.atl.Model;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;


/**
 * This test performs the transformation from an EnterpriseApp model
 * to a Spring model.
 *
 * @author Matthias Bohlen
 */
public class EnterpriseApp2SpringTransformationTest
    extends TestCase
{
    Logger logger = Logger.getLogger(EnterpriseApp2SpringTransformationTest.class);

    public void testTransform()
        throws Exception
    {
        BasicConfigurator.configure();

        final ATLTransformer transformer = new ATLTransformer();

        // - the path of the package location of the test resources
        final URL testResourceUrl = EnterpriseApp2SpringTransformationTest.class.getResource("/atl");
        assertNotNull(testResourceUrl);
        final String testResourcePath = testResourceUrl.toString() + '/';

        final String atlPath = testResourcePath + "EnterpriseApp2Spring.atl";

        final String mdrRepository = "MDR";

        // - set up the UML meta model (this is the input model's metamodel)
        final String inputMetamodelName = "EnterpriseApp";
        final Model inputMetamodel = new Model();
        final URL inputMetamodelUrl = EnterpriseApp2SpringTransformationTest.class.getResource("/EnterpriseAppMetamodel.xml");
        assertNotNull(inputMetamodelUrl);
        final String inputMetamodelPath = inputMetamodelUrl.toString();
        logger.log(
            Priority.DEBUG,
            "inputMetamodelPath = " + inputMetamodelPath);
        inputMetamodel.setName(inputMetamodelName);
        inputMetamodel.setPath(inputMetamodelPath);
        inputMetamodel.setRepository(mdrRepository);

        // - setup the source model.
        final Model sourceModel = new Model();
        sourceModel.setName("IN");
        sourceModel.setRepository(mdrRepository);
        final URL sourceModelUrl = EnterpriseApp2SpringTransformationTest.class.getResource("/enterpriseapp.xmi");
        assertNotNull(sourceModelUrl);
        final String sourceModelPath = sourceModelUrl.toString();
        logger.log(
                Priority.DEBUG,
                "sourceModelPath = " + sourceModelPath);
        sourceModel.setPath(sourceModelPath);
        sourceModel.setRepository(mdrRepository);

        // - set this first input model's meta model as UML
        sourceModel.setMetamodel(inputMetamodelName);

        // - setup the target metamodel (this is the output model's metamodel)
        final String targetMetamodelName = "Spring";
        final Model targetMetamodel = new Model();
        targetMetamodel.setName(targetMetamodelName);
        final URL targetMetamodelUrl = EnterpriseApp2SpringTransformationTest.class.getResource("/SpringMetamodel.xml");
        assertNotNull(targetMetamodelUrl);
        final String targetMetamodelPath = targetMetamodelUrl.toString();
        logger.log(
                Priority.DEBUG,
                "targetMetamodelPath = " + targetMetamodelPath);
        targetMetamodel.setPath(targetMetamodelPath);
        targetMetamodel.setRepository(mdrRepository);

        // - setup the target model
        final Model targetModel = new Model();
        targetModel.setName("OUT");
        targetModel.setRepository(mdrRepository);

        // - this is the path of the transformed output model
        final String targetModelPath = testResourcePath + "transformed-output.xmi";
        targetModel.setPath(targetModelPath);
        targetModel.setMetamodel(targetMetamodelName);
        targetModel.setRepository(mdrRepository);

        // - load up the metamodels and models
        Model[] metamodels = new Model[] {inputMetamodel, targetMetamodel};
        Model[] sourceModels = new Model[] {sourceModel};
        Model[] targetModels = new Model[] {targetModel};

        // - add the libraries

        // - perform the transformation
        transformer.transform(
            atlPath,
            new Library[] {},
            metamodels,
            sourceModels,
            targetModels,
            null);
    }
}