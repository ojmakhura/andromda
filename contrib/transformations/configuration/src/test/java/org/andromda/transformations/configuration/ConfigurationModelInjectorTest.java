package org.andromda.transformations.configuration;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.configuration.Configuration;
import org.andromda.core.configuration.Location;
import org.andromda.core.configuration.Namespace;
import org.andromda.core.configuration.Property;
import org.andromda.transformers.atl.engine.ATLModelHandler;
import org.apache.log4j.BasicConfigurator;
import org.atl.engine.vm.nativelib.ASMModel;

/**
 * JUnit test to load a {@link org.andromda.core.configuration.Configuration}
 * into the configuration metamodel.
 * 
 * @author Matthias Bohlen
 */
public class ConfigurationModelInjectorTest extends TestCase
{

    public void testLoadConfigurationModel() throws Exception
    {
        BasicConfigurator.configure();

        URL uri = ConfigurationModelInjectorTest.class
                .getResource("configuration.xml");
        assertNotNull(uri);
        Configuration configuration = Configuration.getInstance(uri);
        assertNotNull(configuration);

        // initialize the configuration
        configuration.initialize();

        Property[] globalProperties = configuration.getProperties();
        dumpProperties("Global properties:", globalProperties);

        Namespace namespaces[] = configuration.getNamespaces();
        for (int i = 0; i < namespaces.length; i++)
        {
            dumpNamespace(namespaces[i]);
        }

        dumpMappings(configuration.getMappingsSearchLocations());

        final ATLModelHandler handler = ATLModelHandler.getInstance("MDR");

        final ASMModel mofMetamodel = handler.getMOF();

        final URL configurationMetamodelUrl = ConfigurationModelInjectorTest.class
                .getResource("/ConfigurationMetamodel.xml");
        assertNotNull(configurationMetamodelUrl);
        final String configurationMetamodelPath = configurationMetamodelUrl
                .toString();

        final String moduleSearchPathProperty = System
                .getProperty("module.search.paths");
        final String[] moduleSearchPaths = moduleSearchPathProperty != null ? moduleSearchPathProperty
                .split(",")
                : null;

        ASMModel configurationMetaModel = handler.loadModel("Configuration",
                mofMetamodel, configurationMetamodelPath, moduleSearchPaths);

        ASMModel configurationModel = handler.newModel("androMDAConfig",
                configurationMetaModel);

        new ConfigurationInjector(configuration).inject(configurationModel,
                null, null);

        final String targetModelOutputPath = new File(
                "configurationModel.xmi.xml").getCanonicalPath().toString();
        AndroMDALogger.info("Output model: '" + targetModelOutputPath + "'");
        handler.writeModel(configurationModel, targetModelOutputPath);
    }

    /**
     * Dumps an array of mapping locations for debugging purposes.
     * 
     * @param mappingsSearchLocations the locations to dump
     * @throws IOException when something goes wrong
     */
    private void dumpMappings(Location[] mappingsSearchLocations)
    {
        for (int i = 0; i < mappingsSearchLocations.length; i++)
        {
            Location loc = mappingsSearchLocations[i];
            AndroMDALogger.debug("Mappings search location = " + loc.getPath());
            URL[] resources = loc.getResources();
            for (int k = 0; k < resources.length; k++)
            {
                AndroMDALogger.debug("  file: "
                        + resources[k].toString());
            }
        }
    }

    /**
     * Dumps a Namespace for debugging purposes.
     * 
     * @param ns the Namespace to dump
     */
    private void dumpNamespace(Namespace ns)
    {
        Object[] properties = ns.getProperties().toArray();
        dumpProperties("Namespace " + ns.getName() + ":", properties);
    }

    /**
     * Dumps an array of properties for debugging purposes.
     * 
     * @param headline a title to be written
     * @param properties the properties to be dumped
     */
    private void dumpProperties(String headline, Object[] properties)
    {
        AndroMDALogger.debug(headline);
        for (int i = 0; i < properties.length; i++)
        {
            Property prop = (Property) properties[i];
            AndroMDALogger.debug("  " + prop.getName() + " = "
                    + prop.getValue() + " (ignore=" + prop.isIgnore() + ")");
        }
    }
}