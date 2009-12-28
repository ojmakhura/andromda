package org.andromda.core.metafacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import org.andromda.core.metafacade.MetafacadeMapping.PropertyGroup;
import org.andromda.core.namespace.NamespaceComponents;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * Tests {@link org.andromda.core.metafacade.MetafacadeMappings}
 * 
 * @author Chad Brandon
 */
public class MetafacadeMappingsTest
    extends TestCase
{
    /**
     * Constructor for MappingsTest.
     * 
     * @param name
     */
    public MetafacadeMappingsTest(
        String name)
    {
        super(name);
    }
    
    /**
     * @see TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        NamespaceComponents.instance().discover();
    }


    private static final String METAFACADE_1 = "org.andromda.core.metafacade.Metafacade1";
    private static final String METAFACADE_IMPL_1 = "org.andromda.core.metafacade.Metafacade1Impl";
    private static final Object MAPPING_OBJECT_1 = new MappingObject1();
    private static final List STEREOTYPES_1;

    private static final String METAFACADE_2 = "org.andromda.core.metafacade.Metafacade2";
    private static final String METAFACADE_IMPL_2 = "org.andromda.core.metafacade.Metafacade2Impl";
    private static final Object MAPPING_OBJECT_2 = new MappingObject2();
    private static final List STEREOTYPES_2;

    private static final String METAFACADE_3 = "org.andromda.core.metafacade.Metafacade3";
    private static final String METAFACADE_IMPL_3 = "org.andromda.core.metafacade.Metafacade3Impl";
    private static final Object MAPPING_OBJECT_3 = new MappingObject3();
    private static final List STEREOTYPES_3;

    private static final String METAFACADE_IMPL_4 = "org.andromda.core.metafacade.Metafacade4Impl";
    private static final Object MAPPING_OBJECT_4 = new MappingObject4();

    private static final String METAFACADE_5 = "org.andromda.core.metafacade.Metafacade5";
    private static final String METAFACADE_IMPL_5 = "org.andromda.core.metafacade.Metafacade5Impl";
    private static final Object MAPPING_OBJECT_5 = new MappingObject5();

    private static final String METAFACADE_IMPL_6 = "org.andromda.core.metafacade.Metafacade6Impl";
    private static final Object MAPPING_OBJECT_6 = new MappingObject6();

    private static final Object MAPPING_OBJECT_7 = new MappingObject7();

    private static final String METAFACADE_IMPL_8 = "org.andromda.core.metafacade.Metafacade8Impl";
    private static final Object MAPPING_OBJECT_8 = new MappingObject8();

    private static final String METAFACADE_IMPL_9 = "org.andromda.core.metafacade.Metafacade9Impl";
    private static final Object MAPPING_OBJECT_9 = new MappingObject9();

    private static final String METAFACADE_IMPL_10 = "org.andromda.core.metafacade.Metafacade10Impl";
    private static final Object MAPPING_OBJECT_10 = new MappingObject10();
    
    private static final Object MAPPING_OBJECT_11 = new MappingObject11();

    private static final String MAPPING_PROPERTY = "mappingProperty";
    private static final String PROPERTY = "property";
    private static final String PROPERTY_ONE = "propertyOne";
    private static final String PROPERTY_TWO = "propertyTwo";

    private static final String STEREOTYPE_FINDER_METHOD = "FINDER_METHOD";
    private static final String STEREOTYPE_ENUMERATION = "ENUMERATION";
    private static final String STEREOTYPE_APPLICATION_EXCEPTION = "APPLICATION_EXCEPTION";
    private static final String STEREOTYPE_UNEXPECTED_EXCEPTION = "UNEXPECTED_EXCEPTION";
    private static final String STEREOTYPE_QUERY_METHOD = "QUERY_METHOD";

    static
    {
        STEREOTYPES_1 = new ArrayList();
        STEREOTYPES_1.add(STEREOTYPE_FINDER_METHOD);
        STEREOTYPES_2 = new ArrayList();
        STEREOTYPES_2.add(STEREOTYPE_ENUMERATION);
        STEREOTYPES_3 = new ArrayList();
        STEREOTYPES_3.add(STEREOTYPE_APPLICATION_EXCEPTION);
        STEREOTYPES_3.add(STEREOTYPE_UNEXPECTED_EXCEPTION);
    }

    /**
     * 
     */
    public void testGetMetafacadeMapping()
    {

        MetafacadeMappings mappings = MetafacadeMappings.newInstance();
        mappings.initialize();
        final String modelTypeNamespace = "test";
        mappings = mappings.getModelMetafacadeMappings(modelTypeNamespace);
        final String namespace = mappings.getNamespace();
        final MetafacadeFactory factory = MetafacadeFactory.getInstance();
        factory.setModel(new Model(), modelTypeNamespace);
        factory.setNamespace(mappings.getNamespace());

        // verify the property references
        Collection propertyReferences = mappings.getPropertyReferences();
        // test retrieval of the namespace properties
        assertEquals(2, propertyReferences.size());
        Iterator referenceIterator = propertyReferences.iterator();
        assertEquals("definitionOne", referenceIterator.next());
        assertEquals("definitionTwo", referenceIterator.next());

        // test the default metafacade mapping
        assertNotNull(mappings.getDefaultMetafacadeClass(namespace));
        assertEquals(METAFACADE_IMPL_1, mappings.getDefaultMetafacadeClass(
            namespace).getName());

        // test a mapping having a single stereotype with property references
        MetafacadeMapping mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_1,
            namespace,
            null,
            STEREOTYPES_1);
        assertNotNull(mapping);
        // assertNull(mapping.getContext());
        assertTrue(mapping.getMetafacadeClass().getName().equals(
            METAFACADE_IMPL_1));
        propertyReferences = mapping.getPropertyReferences();
        assertNotNull(propertyReferences);
        assertEquals(2, propertyReferences.size());
        assertNotNull(CollectionUtils.find(propertyReferences, 
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return "metafacadeProperteryOne".equals(object);
                }
            }));
        assertNotNull(CollectionUtils.find(propertyReferences, 
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return "metafacadeProperteryTwo".equals(object);
                }
            }));

        // test that we can get a mapping to the same metafacade with a
        // different stereotype
        // (an 'OR' scenario)
        List stereotypes = new ArrayList();
        stereotypes.add(STEREOTYPE_QUERY_METHOD);
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_1,
            namespace,
            null,
            STEREOTYPES_1);
        assertNotNull(mapping);
        // assertNull(mapping.getContext());
        assertTrue(mapping.getMetafacadeClass().getName().equals(
            METAFACADE_IMPL_1));

        // test a mapping having a context
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_2,
            namespace,
            METAFACADE_1,
            null);
        assertNotNull(mapping);
        assertEquals(METAFACADE_IMPL_4, mapping.getMetafacadeClass().getName());
        assertTrue(mapping.getPropertyReferences().isEmpty());

        // test a mapping having a context (with using an inherited context)
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_2,
            namespace,
            METAFACADE_2,
            null);
        assertNotNull(mapping);
        assertEquals(METAFACADE_IMPL_4, mapping.getMetafacadeClass().getName());
        assertTrue(mapping.getPropertyReferences().isEmpty());

        // test a mapping having 2 required stereotypes
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_2,
            namespace,
            null,
            STEREOTYPES_3);
        assertNotNull(mapping);
        assertFalse(mapping.hasContext());
        assertEquals(METAFACADE_IMPL_3, mapping.getMetafacadeClass().getName());
        assertTrue(mapping.getPropertyReferences().isEmpty());

        // make sure we can't get the mapping that requires 2 stereotypes with
        // ONLY one of the stereotypes (an 'AND' scenario)
        stereotypes = new ArrayList();
        stereotypes.add(STEREOTYPE_UNEXPECTED_EXCEPTION);
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_2,
            namespace,
            null,
            stereotypes);
        assertNull(mapping);
        stereotypes = new ArrayList();
        stereotypes.add(STEREOTYPE_APPLICATION_EXCEPTION);
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_2,
            namespace,
            null,
            stereotypes);
        assertNull(mapping);

        // test a mapping having a context AND a stereotype
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_2,
            namespace,
            METAFACADE_3,
            STEREOTYPES_2);
        assertNotNull(mapping);
        assertEquals(METAFACADE_IMPL_2, mapping.getMetafacadeClass().getName());

        // test a mapping having a context and multiple stereotypes
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_2,
            namespace,
            METAFACADE_3,
            STEREOTYPES_3);
        assertNotNull(mapping);
        assertEquals(METAFACADE_IMPL_4, mapping.getMetafacadeClass().getName());

        // make sure we can't get the mapping that requires 2 stereotypes and
        // one context
        // with only one of the stereotypes.
        stereotypes = new ArrayList();
        stereotypes.add(STEREOTYPE_APPLICATION_EXCEPTION);
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_2,
            namespace,
            METAFACADE_3,
            stereotypes);
        assertNull(mapping);

        // try a plain mapping (no contexts or stereotypes)
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_7,
            namespace,
            null,
            null);
        assertNotNull(mapping);
        assertEquals(METAFACADE_IMPL_3, mapping.getMetafacadeClass().getName());

        // make sure we CAN'T get the mapping having the single property
        // since the mapping object doesn't contain the property
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_2,
            namespace,
            null,
            null);
        assertNull(mapping);

        // make sure we CAN get the mapping having the single property
        // since the mapping has the property and it's set to true.
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_5,
            namespace,
            null,
            null);
        assertNotNull(mapping);
        assertEquals(METAFACADE_IMPL_5, mapping.getMetafacadeClass().getName());
        PropertyGroup group = mapping.getMappingProperties();
        Collection mappingProperties = group.getProperties();
        assertNotNull(mappingProperties);
        assertEquals(1, mappingProperties.size());
        assertEquals(
            PROPERTY,
            ((MetafacadeMapping.Property)mappingProperties.iterator().next())
                .getName());
        assertEquals("false", ((MetafacadeMapping.Property)mappingProperties
            .iterator().next()).getValue());

        // get a property that has a value defined
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_4,
            namespace,
            null,
            null);
        assertNotNull(mapping);
        assertEquals(METAFACADE_IMPL_6, mapping.getMetafacadeClass().getName());
        group = mapping.getMappingProperties();
        mappingProperties = group.getProperties();
        assertNotNull(mappingProperties);
        assertEquals(1, mappingProperties.size());
        assertEquals(
            MAPPING_PROPERTY,
            ((MetafacadeMapping.Property)mappingProperties.iterator().next())
                .getName());
        assertEquals("true", ((MetafacadeMapping.Property)mappingProperties
            .iterator().next()).getValue());

        // get a metafacade that has no ancestors
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_3,
            namespace,
            null,
            null);
        assertNotNull(mapping);
        assertEquals(METAFACADE_IMPL_5, mapping.getMetafacadeClass().getName());
        assertEquals(1, mappingProperties.size());
        assertEquals(
            MAPPING_PROPERTY,
            ((MetafacadeMapping.Property)mappingProperties.iterator().next())
                .getName());
        assertEquals("true", ((MetafacadeMapping.Property)mappingProperties
            .iterator().next()).getValue());

        // get a mapping by context and property
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_6,
            namespace,
            METAFACADE_5,
            null);
        assertNotNull(mapping);
        assertEquals(MAPPING_OBJECT_6.getClass().getName(), mapping
            .getMappingClassName());
        assertEquals(METAFACADE_5, mapping.getContext());
        group = mapping.getMappingProperties();
        mappingProperties = group.getProperties();
        assertNotNull(mappingProperties);
        assertEquals(1, mappingProperties.size());
        assertEquals(
            MAPPING_PROPERTY,
            ((MetafacadeMapping.Property)mappingProperties.iterator().next())
                .getName());
        assertEquals("", ((MetafacadeMapping.Property)mappingProperties
            .iterator().next()).getValue());

        // attempt to get a mapping that has 2 properties with one being invalid
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_8,
            namespace,
            null,
            null);
        assertNull(mapping);

        // attempt to get a mapping that has 2 properties with both being valid
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_9,
            namespace,
            null,
            null);
        assertNotNull(mapping);
        assertEquals(METAFACADE_IMPL_8, mapping.getMetafacadeClass().getName());
        group = mapping.getMappingProperties();
        mappingProperties = group.getProperties();
        assertNotNull(mappingProperties);
        assertEquals(2, mappingProperties.size());
        Iterator propertyIterator = mappingProperties.iterator();
        MetafacadeMapping.Property propertyOne = (MetafacadeMapping.Property)propertyIterator
            .next();
        assertEquals(PROPERTY_ONE, propertyOne.getName());
        assertEquals("", propertyOne.getValue());
        MetafacadeMapping.Property propertyTwo = (MetafacadeMapping.Property)propertyIterator
            .next();
        assertEquals(PROPERTY_TWO, propertyTwo.getName());
        assertEquals("SomeValue", propertyTwo.getValue());

        // attempt to get a mapping that is mapped twice 
        // each one using a different property of the 
        // metafacade
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_10,
            namespace,
            null,
            null);
        assertNotNull(mapping);
        assertEquals(METAFACADE_IMPL_9, mapping.getMetafacadeClass().getName());
        group = mapping.getMappingProperties();
        mappingProperties = group.getProperties();
        assertNotNull(mappingProperties);
        assertEquals(1, mappingProperties.size());
        propertyIterator = mappingProperties.iterator();
        propertyOne = (MetafacadeMapping.Property)propertyIterator.next();
        assertEquals(PROPERTY_ONE, propertyOne.getName());
        assertEquals("", propertyOne.getValue());
        
        // attempt with two mappings pointing to the metafacade with
        // mutually exclusive properties (the first one listed should
        // be retrieved).
        mapping = mappings.getMetafacadeMapping(
            MAPPING_OBJECT_11,
            namespace,
            null,
            null);
        assertNotNull(mapping);
        assertEquals(METAFACADE_IMPL_10, mapping.getMetafacadeClass().getName());
        group = mapping.getMappingProperties();
        mappingProperties = group.getProperties();
        assertNotNull(mappingProperties);
        assertEquals(1, mappingProperties.size());
        propertyIterator = mappingProperties.iterator();
        propertyOne = (MetafacadeMapping.Property)propertyIterator.next();
        assertEquals(PROPERTY_TWO, propertyOne.getName());
        assertEquals("", propertyOne.getValue());
    }
}
