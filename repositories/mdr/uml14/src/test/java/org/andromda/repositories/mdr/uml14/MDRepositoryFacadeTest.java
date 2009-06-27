package org.andromda.repositories.mdr.uml14;

import java.net.URL;

import junit.framework.TestCase;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.namespace.NamespaceComponents;
import org.andromda.core.repository.Repositories;
import org.andromda.repositories.mdr.MDRepositoryFacade;
import org.apache.log4j.Logger;
import org.omg.uml.UmlPackage;


/**
 * Implements the JUnit test case for <code>org.andromda.repositories.MDRepositoryFacade</code>
 * using the UML 1.4 metamodel.
 *
 * @author <A HREF="http://www.amowers.com">Anthony Mowers </A>
 * @author Chad Brandon
 */
public class MDRepositoryFacadeTest
    extends TestCase
{
    private static final Logger logger = Logger.getLogger(MDRepositoryFacadeTest.class);
    private URL modelURL = null;
    private MDRepositoryFacade repository = null;

    /**
     * Constructor for MDRepositoryFacadeTest.
     *
     * @param name the test name
     */
    public MDRepositoryFacadeTest(String name)
    {
        super(name);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
        AndroMDALogger.initialize();
        if (modelURL == null)
        {
            this.modelURL = TestModel.getModel();
            if (logger.isInfoEnabled())
            {
                logger.info("found model --> '" + modelURL + "'");
            }
            
            NamespaceComponents.instance().discover();
            Repositories.instance().initialize();
            repository = (MDRepositoryFacade)Repositories.instance().getImplementation("netBeansMDR");
            repository.open();
        }
    }

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
     * @see TestCase#tearDown()
     */
    protected void tearDown()
        throws Exception
    {
        this.repository.clear();
        this.repository = null;
        super.tearDown();
    }
}