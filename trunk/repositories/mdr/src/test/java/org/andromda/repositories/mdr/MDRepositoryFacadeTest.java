package org.andromda.repositories.mdr;

import java.net.URL;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.omg.uml.UmlPackage;

/**
 * Implements the JUnit test suite for
 * <code>org.andromda.repositories.MDRepositoryFacade</code>.
 * 
 * @author <A HREF="httplo://www.amowers.com">Anthony Mowers </A>
 * @author Chad Brandon
 */
public class MDRepositoryFacadeTest
    extends TestCase
{

    private static final Logger logger = 
        Logger.getLogger(MDRepositoryFacadeTest.class);
    private URL modelURL = null;
    private MDRepositoryFacade repository = null;

    /**
     * Constructor for MDRepositoryFacadeTest.
     * 
     * @param arg0
     */
    public MDRepositoryFacadeTest(
        String arg0)
    {
        super(arg0);
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
        }

    }

    public void testReadModel()
    {
        repository.readModel(modelURL, null);
    }

    public void testGetLastModified() throws Exception
    {
        repository.readModel(modelURL, null);
        assertEquals(modelURL.openConnection().getLastModified(), repository
            .getLastModified());
    }

    public void testGetModel()
    {
        assertNotNull(repository.getModel());
        assertNotNull(repository.getModel().getModel());
        assertTrue(repository.getModel().getModel() instanceof UmlPackage);
    }

}