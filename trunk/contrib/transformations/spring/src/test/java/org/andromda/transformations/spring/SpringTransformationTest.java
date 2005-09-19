package org.andromda.transformations.spring;


import java.net.URL;

import junit.framework.TestCase;

import org.andromda.transformers.atl.ATLTransformer;
import org.andromda.transformers.atl.Library;
import org.andromda.transformers.atl.Model;
import org.apache.log4j.BasicConfigurator;

public class SpringTransformationTest
    extends TestCase
{
    public void testTransform()
    {
    	BasicConfigurator.configure();
    	
        final ATLTransformer transformer = new ATLTransformer();
        
        final String property = System.getProperty("module.search.paths");
        final String[] moduleSearchPaths = property != null ? property.split(",") : null;
        
        // uml2java test (this transforms a UML to a JAVA metamodel)
        
        // - the path of the package location of the test resources
        final URL testResourceUrl = SpringTransformationTest.class.getResource("/atl");
        assertNotNull(testResourceUrl);
        final String testResourcePath = testResourceUrl.toString() + '/';
            
        final String atlPath = testResourcePath + "UML2Spring.atl";
        
        final String mdrRepository = "MDR";
        
        // - set up the UML meta model (this is the input model's metamodel)
        final String umlMetamodelName = "UML";
        final Model umlMetamodel = new Model();
        final URL umlMetamodelUrl = SpringTransformationTest.class.getResource("/M2_DiagramInterchangeModel.xml");
        assertNotNull(umlMetamodelUrl);
        final String umlMetamodelPath = umlMetamodelUrl.toString();
        umlMetamodel.setName(umlMetamodelName);
        umlMetamodel.setPath(umlMetamodelPath.toString());
        
        // - setup the source model.
        final Model sourceModel = new Model();
        sourceModel.setName("IN");
        sourceModel.setRepository(mdrRepository);
        final URL sourceModelUrl = SpringTransformationTest.class.getResource("/SpringTransformationTests.xml.zip");
        assertNotNull(sourceModelUrl);
        final String sourceModelPath = "jar:" + sourceModelUrl + "!/SpringTransformationTests.xml";
        sourceModel.setPath(sourceModelPath.toString());
        // - set this first input model's meta model as UML
        sourceModel.setMetamodel(umlMetamodelName);
        
        // - setup the target metamodel (this is the output model's metamodel)
        final String springMetamodelName = "SPRING";
        final Model javaMetamodel = new Model();
        javaMetamodel.setName(springMetamodelName);
        final URL springMetamodelUrl = SpringTransformationTest.class.getResource("/SpringMetamodel.xml");
        assertNotNull(springMetamodelUrl);
        final String springMetamodelPath = springMetamodelUrl.toString();
        javaMetamodel.setPath(springMetamodelPath);
        
        // - setup the target model
        final Model targetModel = new Model();
        targetModel.setName("OUT");
        targetModel.setRepository(mdrRepository);
        // - this is the path of the transformed output model
        final String targetModelPath = testResourcePath + "transformed-output.xmi";
        targetModel.setPath(targetModelPath);
        targetModel.setMetamodel(springMetamodelName);
        
        // - load up the metamodels and models
        Model[] metamodels = new Model[] {umlMetamodel, javaMetamodel};
        Model[] sourceModels = new Model[] {sourceModel};
        Model[] targetModels = new Model[] {targetModel};
        
        // - add the libraries
        final URL librariesUrl = SpringTransformationTest.class.getResource("/atl/UMLHelpers.atl");
        assertNotNull(librariesUrl);
        final String umlHelpersPath = librariesUrl.toString();
        Library library = new Library();
        library.setName("UMLHelpers");
        library.setPath(umlHelpersPath);
        
        // - perform the transformation
        transformer.transform(atlPath, new Library[]{library}, metamodels, sourceModels, targetModels, moduleSearchPaths);
    }
}
