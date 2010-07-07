package org.andromda.repositories.mdr.uml14;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.net.URL;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.namespace.NamespaceComponents;
import org.andromda.core.repository.Repositories;
import org.andromda.repositories.mdr.MDRepositoryFacade;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.omg.uml.UmlPackage;

/**
 * Implements the JUnit test case for <code>org.andromda.repositories.MDRepositoryFacade</code>
 * using the UML 1.4 metamodel.
 *
 * @author <A HREF="http://www.amowers.com">Anthony Mowers </A>
 * @author Chad Brandon
 */
public class MDRepositoryFacadeTest
{
    private static final Logger logger = Logger.getLogger(MDRepositoryFacadeTest.class);
    private URL modelURL = null;
    private MDRepositoryFacade repository = null;

    /**
     * init for test
     */
    @Before
    public void setUp()
    {
        AndroMDALogger.initialize();
        if (modelURL == null)
        {
            this.modelURL = TestModel.getModel();
            if (logger.isInfoEnabled())
            {
                logger.info("found model --> '" + modelURL + '\'');
            }
            
            NamespaceComponents.instance().discover();
            Repositories.instance().initialize();
            repository = (MDRepositoryFacade)Repositories.instance().getImplementation("netBeansMDR");
            repository.open();
        }
    }

    /**
     * 
     */
    @Test
    public void testGetModel()
    {
        repository.readModel(
            new String[] {modelURL.toString()},
            null);
        final ModelAccessFacade model = repository.getModel();
        assertNotNull(model);
        assertNotNull(model.getModel());
        assertTrue(model.getModel() instanceof UmlPackage);
    }

    /**
     * clean up after test
     */
    @After
    public void tearDown()
    {
        this.repository.clear();
        this.repository = null;
    }
}