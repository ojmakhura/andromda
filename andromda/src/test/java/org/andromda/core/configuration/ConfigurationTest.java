package org.andromda.core.configuration;

import java.net.URL;

import junit.framework.TestCase;


/**
 * JUnit test for {@link org.andromda.core.configuration.Configuration}
 *
 * @author Chad Brandon
 */
public class ConfigurationTest
    extends TestCase
{
    public void testGetInstance()
    {
        URL uri = ConfigurationTest.class.getResource("configuration.xml");
        assertNotNull(uri);
        Configuration configuration = Configuration.getInstance(uri);
        assertNotNull(configuration);

        // initialize the configuration
        configuration.initialize();

        // properties
        assertEquals(2, configuration.getProperties().length);
        final Property property1 = configuration.getProperties()[0];
        assertEquals(
            "modelValidation",
            property1.getName());
        assertEquals(
            "true",
            property1.getValue());
        final Property property2 = configuration.getProperties()[1];
        assertEquals(
            "cartridgeFilter",
            property2.getName());
        assertEquals(
            "${filter}",
            property2.getValue());

        // server
        final Server server = configuration.getServer();
        assertNotNull(server);
        assertEquals(
            "localhost",
            server.getHost());
        assertEquals(
            4444,
            server.getPort());
        assertEquals(
            5000,
            server.getLoadInterval());
        assertEquals(
            50,
            server.getMaximumFailedLoadAttempts());
        
        // repositories
        assertEquals(1, configuration.getRepositories().length);
        final Repository repository = configuration.getRepositories()[0];
        assertNotNull(repository);
        assertEquals("test", repository.getName());

        // models
        assertEquals(3, repository.getModels().length);
        final Model model1 = repository.getModels()[0];
        assertNotNull(model1);
        assertEquals(2, model1.getUris().length);
        assertEquals(
            "file:model1Uri1.xmi",
            model1.getUris()[0]);
        assertEquals(
            "file:model1Uri2.xmi",
            model1.getUris()[1]);
        assertTrue(model1.isLastModifiedCheck());
        assertEquals(2, model1.getModuleSearchLocations().length);

        // module search locations        
        final Location[] moduleLocations = model1.getModuleSearchLocations();
        assertNotNull(moduleLocations);
        assertEquals(2, moduleLocations.length);
        assertEquals("/path/to/model/modules1", moduleLocations[0].getPath());
        assertEquals("*.xmi, *.xml.zip", moduleLocations[0].getPatterns());
        assertEquals("/path/to/model/modules2", moduleLocations[1].getPath());
        assertNull(moduleLocations[1].getPatterns());
        assertEquals(2, model1.getModuleSearchLocationPaths().length);
        assertEquals("/path/to/model/modules1", model1.getModuleSearchLocationPaths()[0]);
        assertEquals("/path/to/model/modules2", model1.getModuleSearchLocationPaths()[1]);

        // modelPackages
        assertNotNull(model1.getPackages());
        assertFalse(model1.getPackages().isProcess("some::package"));
        assertFalse(model1.getPackages().isProcess("org::andromda::metafacades::uml"));
        assertTrue(model1.getPackages().isProcess("org::andromda::cartridges::test"));
        
        // transformations
        assertEquals(2, model1.getTransformations().length);
        final Transformation transformation1 = model1.getTransformations()[0];
        assertNotNull(transformation1);
        assertEquals(
            "file:transformation1.xsl",
            transformation1.getUri());
        assertEquals(
            "path/to/some/directory/transformed-model.xmi",
            transformation1.getOutputLocation());
        final Transformation transformation2 = model1.getTransformations()[1];
        assertNotNull(transformation2);
        assertEquals(
            "file:transformation2.xsl",
            transformation2.getUri());

        final Model model2 = repository.getModels()[1];
        assertNotNull(model2);
        assertEquals(1, model2.getUris().length);
        assertEquals(
            "file:model2.xmi",
            model2.getUris()[0]);
        assertEquals(0, model2.getModuleSearchLocations().length);
        assertFalse(model2.isLastModifiedCheck());

        final Model model3 = repository.getModels()[2];
        assertNotNull(model3);
        assertEquals(1, model3.getUris().length);
        assertEquals(
            "file:model3.xmi",
            model3.getUris()[0]);
        assertNotNull(model3.getPackages());
        assertTrue(model3.getPackages().isProcess("some::package"));
        assertFalse(model3.getPackages().isProcess("org::andromda::metafacades::uml"));

        // namespaces
        final Namespace namespace1 = Namespaces.instance().getNamespace("default");
        final Property namespace1Property1 = namespace1.getProperty("languageMappingsUri");

        assertNotNull(namespace1Property1);
        assertEquals(
            "Java",
            namespace1Property1.getValue());
        assertFalse(namespace1Property1.isIgnore());
        final Property namespace1Property2 = namespace1.getProperty("wrapperMappingsUri");
        assertEquals(
            "JavaWrapper",
            namespace1Property2.getValue());
        assertNotNull(namespace1Property2);
        final Property namespace1Property3 = namespace1.getProperty("enumerationLiteralNameMask");
        assertNotNull(namespace1Property3);
        assertEquals(
            "upperunderscore",
            namespace1Property3.getValue());
        final Property namespace1Property4 = namespace1.getProperty("maxSqlNameLength");
        assertNotNull(namespace1Property4);
        assertTrue(namespace1Property4.isIgnore());
        final Namespace namespace2 = Namespaces.instance().getNamespace("spring");
        assertNotNull(namespace2);
        final Property namespace2Property1 = namespace2.getProperty("hibernateQueryUseNamedParameters");
        assertNotNull(namespace2Property1);
        assertEquals(
            "true",
            namespace2Property1.getValue());

        // mappings search locations
        assertEquals(2, configuration.getMappingsSearchLocations().length);
        assertEquals("/path/to/mappings/location1", configuration.getMappingsSearchLocations()[0].getPath());
        assertEquals("/path/to/mappings/location2", configuration.getMappingsSearchLocations()[1].getPath());
    }
}