package org.andromda.core.common;

import junit.framework.TestCase;

import org.andromda.core.common.Namespace;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.Property;

/**
 * JUnit test for org.andromda.core.common.Namespaces
 * 
 * @author Chad Brandon
 */
public class NamespacesTest
    extends TestCase
{

    private static final String TEST_LOCATION = "C:/some/directory/location";
    private static final String TEST_OUTLET = "test-outlet";
    private static final String TEST_NAMESPACE = "testNS";

    /**
     * Constructor for NamespacesTest.
     * 
     * @param arg0
     */
    public NamespacesTest(
        String arg0)
    {
        super(arg0);
    }

    public void testAddAndFindNamespaceProperty()
    {
        Namespace namespace = new Namespace();
        namespace.setName(TEST_NAMESPACE);
        Property outletLocation = new Property();
        outletLocation.setName(TEST_OUTLET);
        outletLocation.setValue(TEST_LOCATION);
        namespace.addProperty(outletLocation);
        Namespaces.instance().addNamespace(namespace);

        assertEquals(outletLocation, Namespaces.instance()
            .findNamespaceProperty(TEST_NAMESPACE, TEST_OUTLET));
    }
}