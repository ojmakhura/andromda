package org.andromda.transformations.spring2thirdgl;

import java.net.URL;

import junit.framework.TestCase;

import org.andromda.transformers.atl.ATLTransformer;
import org.andromda.transformers.atl.Library;
import org.andromda.transformers.atl.Model;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;


/**
 * This test performs the transformation from an Spring model
 * to a 3GL (third generation language) model.
 *
 * @author Matthias Bohlen
 */
public class Spring2ThirdGLTransformationTest
    extends TestCase
{
    Logger logger = Logger.getLogger(Spring2ThirdGLTransformationTest.class);

    public void testTransform()
        throws Exception
    {
        BasicConfigurator.configure();

        final ATLTransformer transformer = new ATLTransformer();

        // - the path of the package location of the test resources
        final URL testResourceUrl = Spring2ThirdGLTransformationTest.class.getResource("/atl");
        assertNotNull(testResourceUrl);
        final String testResourcePath = testResourceUrl.toString() + '/';

        final String atlPath = testResourcePath + "Spring2ThirdGL.atl";

        final String mdrRepository = "MDR";

        // - set up the input meta model
        final String inputMetamodelName = "Spring";
        final Model inputMetamodel = new Model();
        final URL inputMetamodelUrl = Spring2ThirdGLTransformationTest.class.getResource("/SpringMetamodel.xml");
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
        final URL sourceModelUrl = Spring2ThirdGLTransformationTest.class.getResource("/spring.xmi");
        assertNotNull(sourceModelUrl);
        final String sourceModelPath = sourceModelUrl.toString();
        logger.log(
                Priority.DEBUG,
                "sourceModelPath = " + sourceModelPath);
        sourceModel.setPath(sourceModelPath);
        sourceModel.setRepository(mdrRepository);

        // - set this first input model's meta model
        sourceModel.setMetamodel(inputMetamodelName);

        // - setup the target metamodel
        final String targetMetamodelName = "X3GL";
        final Model targetMetamodel = new Model();
        targetMetamodel.setName(targetMetamodelName);
        final URL targetMetamodelUrl = Spring2ThirdGLTransformationTest.class.getResource("/3GLMetamodel.xml");
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