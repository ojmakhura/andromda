package org.andromda.core.metadecorators.uml14;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import junit.framework.TestCase;

import org.andromda.core.mdr.MDRepositoryFacade;
import org.omg.uml.UmlPackage;

public class DecoratorSmallTest1 extends TestCase
{
    private UmlPackage model;
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
            repository.readModel(modelURL);
            model = (UmlPackage) repository.getModel();
            DecoratorFactory df = DecoratorFactory.getInstance();
            df.setModel(model);
            df.setActiveNamespace("core");
        }
    }

    /**
     * Tests the first instantiation of a decorator.
     * 
     * @throws Exception
     */
    public void testFindModelAndPackages() throws Exception
    {
        DecoratorFactory df = DecoratorFactory.getInstance();
        ModelDecorator md =
            (ModelDecorator) df.createDecoratorObject(model);
        Collection packages = md.getRootPackage().getPackages();
        assertEquals(4, packages.size());
        ArrayList expectedResults = new ArrayList();
        expectedResults.add("org");
        expectedResults.add("java");
        expectedResults.add("features");
        expectedResults.add("associations");

        for (Iterator iter = packages.iterator(); iter.hasNext();)
        {
            PackageDecorator element = (PackageDecorator) iter.next();
            assertNotNull("package decorator is null", element);
            assertTrue(
                "expected package not found",
                expectedResults.contains(element.getName()));
            System.out.println("package: " + element.getName());
        }
    }

    /**
     * Tests ClassifierDecorators.
     * @throws Exception
     */
    public void testFindClasses() throws Exception
    {
        DecoratorFactory df = DecoratorFactory.getInstance();
        ModelDecorator md =
            (ModelDecorator) df.createDecoratorObject(model);
        Collection packages = md.getRootPackage().getPackages();

        HashMap expectedResults = new HashMap();
        expectedResults.put("ClassAA", "associations");
        expectedResults.put("ClassAB", "associations");
        expectedResults.put("ClassAC", "associations");
        expectedResults.put("ClassAD", "associations");
        expectedResults.put("ClassAE", "associations");
        expectedResults.put("ClassAF", "associations");
        expectedResults.put("ClassAssociations", "associations");
        expectedResults.put("ClassFeatures", "features");

        for (Iterator iter = packages.iterator(); iter.hasNext();)
        {
            PackageDecorator pakkage = (PackageDecorator) iter.next();
            assertNotNull("package decorator is null", pakkage);
            System.out.println("package: " + pakkage.getName());
            for (Iterator iter2 = pakkage.getClasses().iterator();
                iter2.hasNext();
                )
            {
                ClassifierDecorator clazz =
                    (ClassifierDecorator) iter2.next();
                assertNotNull(clazz);
                assertTrue(
                    "expected class " + clazz.getName() + " not found",
                    expectedResults.get(clazz.getName()).equals(
                        pakkage.getName()));
            }
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
