package org.andromda.repositories.mdr;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.omg.uml.UmlPackage;

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
        assertNotNull(repository.getModel());
        assertNotNull(repository.getModel().getModel());
        assertTrue(repository.getModel().getModel() instanceof UmlPackage);
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        this.repository.close();
        this.repository = null;
        super.tearDown();
    }

}