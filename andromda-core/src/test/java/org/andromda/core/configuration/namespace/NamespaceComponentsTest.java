package org.andromda.core.configuration.namespace;

import junit.framework.TestCase;

import org.andromda.core.configuration.Namespaces;
import org.andromda.core.namespace.NamespaceComponents;

/**
 * Tests {@link NamespaceComponents}
 * 
 * @author Chad Brandon
 */
public class NamespaceComponentsTest
    extends TestCase
{
    public void testInstance()
    {
        final NamespaceComponents componentRegistry = NamespaceComponents.instance();
        componentRegistry.discover();
        assertEquals("3", Namespaces.instance().getPropertyValue("test", "definitionOne"));
        assertEquals("two", Namespaces.instance().getPropertyValue("test", "definitionTwo"));
    }
}

