package org.andromda.core.repository;

import java.util.Collection;

import junit.framework.TestCase;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.namespace.NamespaceComponents;


/**
 * Tests {@link Repository}
 *
 * @author Chad Brandon
 */
public class RepositoryTest
    extends TestCase
{
    /**
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp()
    {
        NamespaceComponents.instance().discover();
    }
    
    public void testFindRepositoryByNamespace()
    {
        Repository repository = (Repository)ComponentContainer.instance().findComponentByNamespace("test", Repository.class);
        assertNotNull(repository);
        repository = (Repository)ComponentContainer.instance().findComponentByNamespace("bogus", Repository.class);
        assertNull(repository);
    }
    
    public void testGetImplementation()
    {
        final Collection repositories = ComponentContainer.instance().findComponentsOfType(Repository.class);
        assertFalse(repositories.isEmpty());
        Repository repository = (Repository)repositories.iterator().next();
        assertNotNull(repository.getImplementation());
        assertEquals(
            MockRepository.class,
            repository.getImplementation().getClass());
    }
}