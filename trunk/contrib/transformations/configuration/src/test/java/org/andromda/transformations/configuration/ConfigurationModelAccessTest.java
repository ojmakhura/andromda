package org.andromda.transformations.configuration;

import java.net.URL;

import org.andromda.core.configuration.Configuration;
import org.andromda.transformers.atl.engine.ATLModelHandler;
import org.apache.log4j.BasicConfigurator;
import org.atl.engine.vm.nativelib.ASMModel;
import org.atl.engine.vm.nativelib.ASMModelElement;

import junit.framework.TestCase;

/**
 * Tests the basic functionality of a configuration model.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * 
 */
public class ConfigurationModelAccessTest
        extends TestCase
{

    private ASMModel configurationModel;

    protected void setUp() throws Exception
    {
        BasicConfigurator.configure();

        // find the XML file with the test configuration for AndroMDA
        URL uri = ConfigurationModelAccessTest.class
                .getResource("configuration.xml");
        assertNotNull(uri);
        Configuration configuration = Configuration.getInstance(uri);
        assertNotNull(configuration);

        // initialize the AndroMDA configuration
        configuration.initialize();

        // register a dummy handler for our special kind of "model"
        ATLModelHandler.registerModelHandler(
                ConfigurationModelHandler.REPOSITORY_TYPE_NAME,
                new ConfigurationModelHandler());

        // get the handler for MDR models
        ATLModelHandler handler = ATLModelHandler.getInstance("MDR");

        // get the MOF metametamodel
        final ASMModel mofMetamodel = handler.getMOF();

        // find the configuration metamodel as XMI
        final URL configurationMetamodelUrl = ConfigurationModelAccessTest.class
                .getResource("/ConfigurationMetamodel.xml");
        assertNotNull(configurationMetamodelUrl);
        final String configurationMetamodelPath = configurationMetamodelUrl
                .toString();

        // split the module search path locations
        final String moduleSearchPathProperty = System
                .getProperty("module.search.paths");
        final String[] moduleSearchPaths = moduleSearchPathProperty != null ? moduleSearchPathProperty
                .split(",")
                : null;

        // load the configuration metamodel
        ASMModel configurationMetaModel = handler.loadModel("Configuration",
                mofMetamodel, configurationMetamodelPath, moduleSearchPaths);

        // create a new configuration model based on that metamodel
        handler = ATLModelHandler
                .getInstance(ConfigurationModelHandler.REPOSITORY_TYPE_NAME);
        configurationModel = handler.newModel("CONFIG", configurationMetaModel);
    }

    /**
     * Finds a typical configuration model element and checks whether
     * it is connected to the correct metaobject.
     * 
     * @throws Exception
     */
    public void testFindModelElement() throws Exception
    {
        ASMModelElement namespacesElement = this.configurationModel
                .findModelElement("Configuration!Namespaces");
        assertNotNull(namespacesElement);
        assertEquals("Configuration!Namespaces", namespacesElement
                .getMetaobject().getName());
    }
}
