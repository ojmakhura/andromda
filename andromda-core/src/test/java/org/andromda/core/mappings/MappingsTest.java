package org.andromda.core.mappings;

import junit.framework.TestCase;
import org.andromda.core.mapping.Mapping;
import org.andromda.core.mapping.Mappings;
import org.andromda.core.mapping.MappingsException;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;


/**
 * Tests {@link org.andromda.core.mapping.Mappings)
 *
 * @author Chad Brandon
 * @author Wouter Zoons
 */
public class MappingsTest
    extends TestCase
{
    /**
     * Constructor for MappingsTest.
     *
     * @param name the name for this test case
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
    private static final String TO_3 = "Class<? extends ToType>";
    private static final String FROM_6 = "Class<? extends FromType>";

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

        Mappings mappings3 = new Mappings();
        Mapping mapping3 = new Mapping();
        mapping3.setTo(TO_3);
        mapping3.addFrom(FROM_6);
        mappings3.addMapping(mapping3);
        // make sure whitespace isn't deleted, only trimmed (Java generics would fail compilation otherwise for example)
        assertEquals(
            TO_3,
            mappings3.getTo(FROM_6));
    }

    public void testMappingsInheritance()
    {
        final URL testMappingsParentUri = MappingsTest.class.getResource("TestMappingsParent.xml");
        assertNotNull(testMappingsParentUri);
        final Mappings testMappingsParent = Mappings.getInstance(testMappingsParentUri);
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

        final URL testMappingsUri = MappingsTest.class.getResource("TestMappings.xml");
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

    public void testEmptyMappings()
    {
        final URL testEmptyMappingsUri = MappingsTest.class.getResource("TestMappingsEmpty.xml");
        assertNotNull(testEmptyMappingsUri);

        final Mappings mappings = Mappings.getInstance(testEmptyMappingsUri);
        assertNotNull(mappings);

        final Collection mappingCollection = mappings.getMappings();
        assertEquals(0, mappingCollection.size());
    }

    public void testTransitivelyExtendingLogicalMappings()
    {
        // the order has been mixed up on purpose
        Mappings.addLogicalMappings(MappingsTest.class.getResource("TestMappingsExtendsLevelA.xml"));
        Mappings.addLogicalMappings(MappingsTest.class.getResource("TestMappingsExtendsLevelD.xml"));
        Mappings.addLogicalMappings(MappingsTest.class.getResource("TestMappingsExtendsLevelC.xml"));
        Mappings.addLogicalMappings(MappingsTest.class.getResource("TestMappingsExtendsLevelB.xml"));

        Mappings.initializeLogicalMappings();

        final Mappings mappings = Mappings.getInstance("TestMappingsExtendsLevelD");
        assertNotNull(mappings);

        final Mapping aaa = mappings.getMapping("datatype::aaa");
        assertNotNull(aaa);
        assertEquals("AAA", aaa.getTo());

        final Mapping bbb = mappings.getMapping("datatype::bbb");
        assertNotNull(bbb);
        assertEquals("BBB", bbb.getTo());

        final Mapping ccc = mappings.getMapping("datatype::ccc");
        assertNotNull(ccc);
        assertEquals("CCC", ccc.getTo());

        final Mapping ddd = mappings.getMapping("datatype::ddd");
        assertNotNull(ddd);
        assertEquals("DDD", ddd.getTo());
    }

    public void testCyclicInheritanceLogicalMappingsException()
    {
        Mappings.addLogicalMappings(MappingsTest.class.getResource("TestMappingsCyclicA.xml"));
        Mappings.addLogicalMappings(MappingsTest.class.getResource("TestMappingsCyclicB.xml"));

        try
        {
            Mappings.initializeLogicalMappings();
            fail("Expected exception");
        }
        catch (MappingsException mappingsException)
        {
            final String message = mappingsException.getMessage();
            assertTrue(message.startsWith("Logical mappings cannot be initialized due to invalid inheritance"));
            assertTrue(message.indexOf("TestMappingsCyclicA") != -1);
            assertTrue(message.indexOf("TestMappingsCyclicB") != -1);
        }
        finally
        {
            // clear out the cached entries so that the other tests won't fail because if the invalid
            // ones we have entered here
            Mappings.clearLogicalMappings();
        }
    }
}