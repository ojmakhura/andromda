package org.andromda.core.configuration;

import java.net.URL;
import java.util.Iterator;

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
        
        // properties
        assertFalse(configuration.getProperties().isEmpty());
        assertEquals(3, configuration.getProperties().size());
        final Iterator globalPropertyIterator = configuration.getProperties().iterator();
        final Property property1 = (Property)globalPropertyIterator.next();
        assertEquals("modelValidation",  property1.getName());
        assertEquals("true", property1.getValue());
        final Property property2 = (Property)globalPropertyIterator.next();
        assertEquals("cartridgeFilter",  property2.getName());
        assertEquals("${filter}", property2.getValue());
        
        // models
        assertFalse(configuration.getModels().isEmpty());
        assertEquals(2, configuration.getModels().size());
        final Iterator modelIterator = configuration.getModels().iterator();
        final URL model1Uri = (URL)modelIterator.next();
        assertNotNull(model1Uri);
        assertEquals("file:model1.xmi", model1Uri.toString());
        final URL model2Uri = (URL)modelIterator.next();
        assertNotNull(model2Uri);
        assertEquals("file:model2.xmi", model2Uri.toString());
        
        // transformations
        assertFalse(configuration.getTransformations().isEmpty());
        assertEquals(2, configuration.getTransformations().size());
        final Iterator transformationIterator = configuration.getTransformations().iterator();
        final Transformation transformation1 = (Transformation)transformationIterator.next();
        assertNotNull(transformation1);
        assertEquals("file:transformation1.xsl", transformation1.getUrl().toString());
        assertEquals("path/to/some/directory/transformed-model.xmi", transformation1.getOutputLocation());
        final Transformation transformation2 = (Transformation)transformationIterator.next();
        assertNotNull(transformation2);
        assertEquals("file:transformation2.xsl", transformation2.getUrl().toString());
        
        // modelPackages
        assertFalse(configuration.getModelPackages().shouldProcess("org::andromda::metafacades::uml"));
        assertTrue(configuration.getModelPackages().shouldProcess("org::andromda::cartridges::test"));;
        
        // namespaces
        final Namespace namespace1 = Namespaces.instance().findNamespace("default");
        final Property namespace1Property1 = namespace1.getProperty("languageMappingsUri");
        assertNotNull(namespace1Property1);
        assertEquals("Java", namespace1Property1.getValue());
        final Property namespace1Property2 = namespace1.getProperty("wrapperMappingsUri");
        assertEquals("JavaWrapper", namespace1Property2.getValue());
        assertNotNull(namespace1Property2);
        final Property namespace1Property3 = namespace1.getProperty("enumerationLiteralNameMask");
        assertNotNull(namespace1Property3);
        assertEquals("upperunderscore", namespace1Property3.getValue());
        final Namespace namespace2 = Namespaces.instance().findNamespace("spring");
        assertNotNull(namespace2);
        final Property namespace2Property1 = namespace2.getProperty("hibernateQueryUseNamedParameters");
        assertNotNull(namespace2Property1);
        assertEquals("true", namespace2Property1.getValue());
        
        // module search locations
        assertFalse(configuration.getModuleSearchLocations().isEmpty());
        assertEquals(2, configuration.getModuleSearchLocations().size());
        final Iterator moduleSearchLocationIterator = configuration.getModuleSearchLocations().iterator();
        assertEquals("/path/to/model/modules1", (String)moduleSearchLocationIterator.next());
        assertEquals("/path/to/model/modules2", (String)moduleSearchLocationIterator.next());
        
        // mappings search locations
        assertFalse(configuration.getMappingsSearchLocations().isEmpty());
        assertEquals(2, configuration.getMappingsSearchLocations().size());
        final Iterator mappingsSearchLocationIterator = configuration.getMappingsSearchLocations().iterator();
        assertEquals("/path/to/mappings/location1", (String)mappingsSearchLocationIterator.next());
        assertEquals("/path/to/mappings/location2", (String)mappingsSearchLocationIterator.next());
    }
}   
