package org.andromda.core.metadecorators.uml14;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.andromda.core.common.RepositoryReadException;
import org.andromda.core.mdr.MDRepositoryFacade;
import org.omg.uml.UmlPackage;

public class DecoratorSmallTest1 extends TestCase
{
    private URL modelURL = null;
    private MDRepositoryFacade repository = null;

    /**
     * Constructor for MDRepositoryTransformationTest.
     * @param arg0
     */
    public DecoratorSmallTest1(String arg0)
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
            repository = new MDRepositoryFacade();
        }
    }

    /**
     * Tests the first instantiation of a decorator.
     * 
     * @throws Exception
     */
    public void testTransformModel() throws Exception
    {
        try
        {
            repository.readModel(modelURL);
            UmlPackage model = (UmlPackage) repository.getModel();
            DecoratorFactory df = DecoratorFactory.getInstance();
            df.setModel(model);
            df.setActiveNamespace("core");
            ModelDecorator md =
                (ModelDecorator) df.createDecoratorObject(model);
            Collection packages = md.getRootPackage().getPackages();
            assertEquals(4, packages.size());
            for (Iterator iter = packages.iterator(); iter.hasNext();)
            {
                PackageDecorator element = (PackageDecorator) iter.next();
                assertNotNull(element);
                System.out.println("package: " + element.getName());
            }
        }
        catch (IOException ioe)
        {
            assertNull(ioe.getMessage(), ioe);
        }
        catch (RepositoryReadException rre)
        {
            assertNull(rre.getMessage(), rre);
        }
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

}
