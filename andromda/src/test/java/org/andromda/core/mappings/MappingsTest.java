package org.andromda.core.mappings;

import java.net.URL;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.andromda.core.mapping.Mapping;
import org.andromda.core.mapping.Mappings;


/**
 * Tests {@link org.andromda.core.mappings.Mappings)
 *
 * @author Chad Brandon
 */
public class MappingsTest
    extends TestCase
{
    /**
     * Constructor for MappingsTest.
     *
     * @param name
     */
    public MappingsTest(String name)
    {
        super(name);
    }

    private static final String TO_1 = "base64Binary";
    private static final String FROM_1 = "datatype.Blob";
    private static final String FROM_2 = "datatype.byte[]";
    private static final String FROM_3 = "datatype.Byte[]";
    private static final String FROM_4 = "datatype.Clob";
    private static final String TO_2 = "java.lang.String";
    private static final String FROM_5 = "datatype.String";
    private static final String NOT_MAPPED_1 = "datatype.byte";
    private static final String NOT_MAPPED_2 = "datatype.String[]";

    public void testGetTo()
    {
        Mappings mappings1 = new Mappings();
        Mapping mapping1 = new Mapping();
        mapping1.setTo(TO_1);
        mapping1.addFrom(FROM_1);
        mapping1.addFrom(FROM_2);
        mapping1.addFrom(FROM_3);
        mapping1.addFrom(FROM_4);
        mappings1.addMapping(mapping1);

        // make sure the to == from when passing in a mappings1 that don't exist
        assertNotNull(mappings1.getTo(NOT_MAPPED_1));
        assertEquals(
            NOT_MAPPED_1,
            mappings1.getTo(NOT_MAPPED_1));
        assertEquals(
            mappings1.getTo(NOT_MAPPED_2),
            mappings1.getTo(NOT_MAPPED_2));

        // make sure we can retrieve the to using a from array.
        assertNotNull(mappings1.getTo(FROM_1));
        assertEquals(
            TO_1,
            mappings1.getTo(FROM_1));

        // make sure we can retrieve the to using a from non array.
        assertEquals(
            TO_1,
            mappings1.getTo(FROM_4));

        Mappings mappings2 = new Mappings();
        Mapping mapping2 = new Mapping();
        mapping2.setTo(TO_2);
        mapping2.addFrom(FROM_5);
        mappings2.addMapping(mapping2);
        assertEquals(
            TO_2,
            mappings2.getTo(FROM_5));
    }

    public void testMappingsInheritance()
    {
        URL testMappingsParentUri = MappingsTest.class.getResource("TestMappingsParent.xml");
        assertNotNull(testMappingsParentUri);
        Mappings testMappingsParent = Mappings.getInstance(testMappingsParentUri);
        assertNotNull(testMappingsParent);
        final Collection mappings1 = testMappingsParent.getMappings();
        assertEquals(
            3,
            mappings1.size());
        final Iterator mappings1Iterator = mappings1.iterator();
        Mapping mapping1 = (Mapping)mappings1Iterator.next();
        assertEquals(
            "datatype::typeOne",
            mapping1.getFroms().iterator().next());
        assertEquals(
            "Type_One",
            mapping1.getTo());
        Mapping mapping2 = (Mapping)mappings1Iterator.next();
        assertEquals(
            "datatype::typeTwo",
            mapping2.getFroms().iterator().next());
        assertEquals(
            "Type_Two",
            mapping2.getTo());
        Mapping mapping3 = (Mapping)mappings1Iterator.next();
        assertEquals(
            "datatype::typeThree",
            mapping3.getFroms().iterator().next());
        assertEquals(
            "Type_Three",
            mapping3.getTo());

        URL testMappingsUri = MappingsTest.class.getResource("TestMappings.xml");
        assertNotNull(testMappingsUri);
        Mappings testMappings = Mappings.getInstance(testMappingsUri);
        assertNotNull(testMappings);
        final Collection mappings2 = testMappings.getMappings();
        assertEquals(
            4,
            mappings2.size());
        final Iterator mappings2Iterator = mappings2.iterator();
        mapping1 = (Mapping)mappings2Iterator.next();
        assertEquals(
            "datatype::typeOne",
            mapping1.getFroms().iterator().next());
        assertEquals(
            "Type_One",
            mapping1.getTo());
        mapping2 = (Mapping)mappings2Iterator.next();
        assertEquals(
            "datatype::typeTwo",
            mapping2.getFroms().iterator().next());
        assertEquals(
            "Overridden",
            mapping2.getTo());
        mapping3 = (Mapping)mappings2Iterator.next();
        assertEquals(
            "datatype::typeThree",
            mapping3.getFroms().iterator().next());
        assertEquals(
            "Type_Three",
            mapping3.getTo());
        Mapping mapping4 = (Mapping)mappings2Iterator.next();
        assertEquals(
            "datatype::typeFour",
            mapping4.getFroms().iterator().next());
        assertEquals(
            "Type_Four",
            mapping4.getTo());
    }
}