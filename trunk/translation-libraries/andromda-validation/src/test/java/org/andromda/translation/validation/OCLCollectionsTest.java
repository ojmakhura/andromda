package org.andromda.translation.validation;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

/**
 * Tests the OCLCollections
 * 
 * @author Chad Brandon
 */
public class OCLCollectionsTest
    extends TestCase
{ 
    
    /**
     * Constructor for OCLCollectionsTest.
     * 
     * @param name the test name
     */
    public OCLCollectionsTest(
        String name)
    {
        super(name);
    }

    /**
     * Tests isUnique 
     */
    public void testIsUnique()
    {
        Collection collection = new ArrayList();
        OCLCollectionsTestObject testObject = new OCLCollectionsTestObject();
        testObject.setPropertyOne("propertyOne");
        collection.add(testObject);
        testObject = new OCLCollectionsTestObject();
        testObject.setPropertyOne("propertyOneAgain");
        collection.add(testObject);
        assertEquals(2, collection.size());
        assertTrue(OCLCollections.isUnique(collection, "propertyOne"));
        assertTrue(OCLCollections.isUnique((Object)collection, "propertyOne"));
        testObject = new OCLCollectionsTestObject();
        testObject.setPropertyOne("propertyOne");
        collection.add(testObject);
        assertEquals(3, collection.size());
        assertFalse(OCLCollections.isUnique(collection, "propertyOne"));
        assertFalse(OCLCollections.isUnique((Object)collection, "propertyOne"));
        collection.remove(testObject);
        assertEquals(2, collection.size());
        assertTrue(OCLCollections.isUnique(collection, "propertyOne"));
        assertTrue(OCLCollections.isUnique((Object)collection, "propertyOne"));
    }

}