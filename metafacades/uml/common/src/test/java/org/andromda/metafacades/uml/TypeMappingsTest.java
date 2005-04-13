package org.andromda.metafacades.uml;

import junit.framework.TestCase;
import org.andromda.core.mapping.Mapping;
import org.andromda.core.mapping.Mappings;

/**
 * Tests {@link org.andromda.metafacades.uml.TypeMappings)
 *
 * @author Chad Brandon
 */
public class TypeMappingsTest
        extends TestCase
{
    /**
     * Constructor for MappingsTest.
     *
     * @param name
     */
    public TypeMappingsTest(String name)
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

    private static final String ARRAY_SUFFIX = "[]";

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
        TypeMappings typeMappings1 = TypeMappings.getInstance(mappings1);

        // make sure the to == from when passing in a mappings1 that don't exist
        assertNotNull(typeMappings1.getTo(NOT_MAPPED_1));
        assertEquals(NOT_MAPPED_1, typeMappings1.getTo(NOT_MAPPED_1));
        assertEquals(typeMappings1.getTo(NOT_MAPPED_2), typeMappings1.getTo(NOT_MAPPED_2));

        // make sure we can retrieve the to using a from array.
        assertNotNull(typeMappings1.getTo(FROM_1));
        assertEquals(TO_1, typeMappings1.getTo(FROM_1));

        // make sure we can retrieve the to using a from non array.
        assertEquals(TO_1, typeMappings1.getTo(FROM_4));

        Mappings mappings2 = new Mappings();
        Mapping mapping2 = new Mapping();
        mapping2.setTo(TO_2);
        mapping2.addFrom(FROM_5);
        mappings2.addMapping(mapping2);
        TypeMappings typeMappings2 = TypeMappings.getInstance(mappings2);
        typeMappings2.setArraySuffix(ARRAY_SUFFIX);

        assertEquals(TO_2, typeMappings2.getTo(FROM_5));
        assertEquals(TO_2 + ARRAY_SUFFIX, typeMappings2.getTo(FROM_5 + ARRAY_SUFFIX));
    }
}
