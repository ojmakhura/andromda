package org.andromda.repositories.mdr;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.omg.uml.UmlPackage;
import org.andromda.core.metafacade.ModelAccessFacade;

import java.net.URL;

/**
 * Implements the JUnit test case for <code>org.andromda.repositories.MDRepositoryFacade</code>.
 *
 * @author <A HREF="httplo://www.amowers.com">Anthony Mowers </A>
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
    protected void setUp() throws Exception
    {
        super.setUp();
        if (modelURL == null)
        {
            this.modelURL = TestModel.getModel();
            if (logger.isInfoEnabled())
                logger.info("found model --> '" + modelURL + "'");
            repository = new MDRepositoryFacade();
            repository.open();
        }

    }

    public void testGetModel()
    {
        repository.readModel(new String[]{modelURL.toString()}, null);
        final ModelAccessFacade model = repository.getModel(null);
        assertNotNull(model);
        assertNotNull(model.getModel());
        assertTrue(model.getModel() instanceof UmlPackage);
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        this.repository.clear();
        this.repository = null;
        super.tearDown();
    }

}