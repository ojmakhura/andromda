package org.andromda.transformers.atl;

import java.net.URL;

import junit.framework.TestCase;

import org.andromda.core.common.ResourceUtils;

public class ATLTransformerTest
    extends TestCase
{
    public void testTransform()
    {
        ATLTransformer transformer = new ATLTransformer();
        
        // uml2java test (this transforms a UML to a JAVA metamodel)
        
        // - the path of the package location of the test resources
        final URL testResourceUrl = ResourceUtils.getResource("/uml2java");
        assertNotNull(testResourceUrl);
        final String testResourcePath = testResourceUrl.toString() + '/';
            
        final String atlPath = testResourcePath + "UML2JAVA.atl";
        
        final String mdrRepository = "MDR";
        
        // - set up the UML meta model (this is the input model's metamodel)
        final String umlMetamodelName = "UML";
        final Model umlMetamodel = new Model();
        final String umlMetamodelPath = testResourcePath + "UMLDI-20030818.xmi";
        assertNotNull(umlMetamodelPath);
        umlMetamodel.setName(umlMetamodelName);
        umlMetamodel.setPath(umlMetamodelPath.toString());
        
        // - setup the source model.
        final Model sourceModel = new Model();
        sourceModel.setName("IN");
        sourceModel.setRepository(mdrRepository);
        final String sourceModelPath = testResourcePath + "ExampleUML.xmi";
        sourceModel.setPath(sourceModelPath.toString());
        // - set this first input model's meta model as UML
        sourceModel.setMetamodel(umlMetamodelName);
        
        // - setup the target metamodel (this is the output model's metamodel)
        final String javaMetamodelName = "JAVA";
        final Model javaMetamodel = new Model();
        javaMetamodel.setName(javaMetamodelName);
        final String javaMetamodelPath = testResourcePath + "Java-20040316.xmi";
        javaMetamodel.setPath(javaMetamodelPath);
        
        // - setup the target model
        final Model targetModel = new Model();
        targetModel.setName("OUT");
        targetModel.setRepository(mdrRepository);
        // - this is the path of the transformed output model
        final String targetModelPath = testResourcePath + "transformed-output.xmi";
        targetModel.setPath(targetModelPath);
        targetModel.setMetamodel(javaMetamodelName);
        
        // - load up the metamodels and models
        Model[] metamodels = new Model[] {umlMetamodel, javaMetamodel};
        Model[] sourceModels = new Model[] {sourceModel};
        Model[] targetModels = new Model[] {targetModel};
        
        // - perform the transformation
        transformer.transform(atlPath, (Library[])null, metamodels, sourceModels, targetModels);
    }
}
