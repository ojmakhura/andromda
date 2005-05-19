package org.andromda.core.configuration;

import junit.framework.TestCase;

import org.andromda.core.configuration.Namespace;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Property;

/**
 * JUnit test for {@link org.andromda.core.configuration.Namespaces}
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
     * @param name
     */
    public NamespacesTest(
        String name)
    {
        super(name);
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