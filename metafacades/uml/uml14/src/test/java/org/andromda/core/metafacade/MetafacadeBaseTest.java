package org.andromda.core.metafacade;

import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.repository.RepositoryFacade;
import org.andromda.metafacades.uml14.TestModel;
import org.apache.log4j.Logger;

public class MetafacadeBaseTest extends TestCase implements TestModel
{
    private URL modelURL = null;
    private RepositoryFacade repository = null;
    
    private static final Logger logger = Logger.getLogger(MetafacadeBaseTest.class);

    public MetafacadeBaseTest(String arg0)
    {
        super(arg0);
    }
    
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        if (modelURL == null)
        {
            modelURL = new URL(TestModel.XMI_FILE_URL);
            repository = 
                (RepositoryFacade)ComponentContainer.instance().findComponent(
                     RepositoryFacade.class);
            repository.readModel(modelURL, null);
            // set the XmlObjectFactory's validation to false 
            // since the libraries used with JUnit for some reason
            // the parser doesn't support schema validation.
            XmlObjectFactory.setDefaultValidating(false);
            MetafacadeFactory factory = MetafacadeFactory.getInstance();
            factory.setModel( repository.getModel());
        }
    }
    
    public void testContextWith2LevelsOfInheritance() {
        
           MetafacadeFactory factory = MetafacadeFactory.getInstance();
           
           TestMetaobject2 metaobject2 = new TestMetaobject2();
           TestMetafacade2Logic metafacade2 = (TestMetafacade2Logic)factory.createMetafacade(metaobject2);
           logger.info("created metafacade2 --> '" + metafacade2 + "'");
           
           String expectedContext = TestMetafacade2Logic.CONTEXT_NAME;
           
           logger.info("expected --> '" + expectedContext + "'");
           logger.info("actual   --> '" + metafacade2.getContext() + "'");
           
           // the context should be the context set in the TestMetafacade2 constructor
           TestCase.assertEquals(expectedContext, metafacade2.getContext());
           
       }
    
    public void testContextWith3LevelsOfInheritance() {
     
        MetafacadeFactory factory = MetafacadeFactory.getInstance();
        
        TestMetaobject3 metaobject3 = new TestMetaobject3();
        TestMetafacade3Logic metafacade3 = (TestMetafacade3Logic)factory.createMetafacade(metaobject3);
        logger.info("created metafacade3 --> '" + metafacade3 + "'");
        
        String expectedContext = TestMetafacade3Logic.CONTEXT_NAME;
        
        // the context should be the context set in the TestMetafacade3 constructor
        logger.info("expected --> '" + expectedContext + "'");
        logger.info("actual   --> '" + metafacade3.getContext() + "'");
        TestCase.assertEquals(expectedContext, metafacade3.getContext());
        
    }
    
    /**
     * Allows the test to be run by JUnit as a suite.
     */
    public static Test suite() {
        return new TestSuite(MetafacadeBaseTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.main(new String[] {MetafacadeBaseTest.class.getName()});
    }

}
