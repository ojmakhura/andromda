package org.andromda.core.metadecorators.uml14;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import junit.framework.TestCase;

import org.andromda.core.mdr.MDRepositoryFacade;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;

public class DecoratorSmallTest1 extends TestCase implements TestModel
{
    private UmlPackage model;
    private URL modelURL = null;
    private MDRepositoryFacade repository = null;

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
        assertEquals(5, packages.size());
        ArrayList expectedResults = new ArrayList();
        expectedResults.add("org");
        expectedResults.add("java");
        expectedResults.add("features");
        expectedResults.add("associations");
        expectedResults.add("dependencies");

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
        expectedResults.put("ClassDependencies", "dependencies");
        expectedResults.put("Class_2", "dependencies");
        expectedResults.put("Class_3", "dependencies");
        expectedResults.put("Class_4", "dependencies");
        expectedResults.put("SuperClass", "dependencies");

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

    /**
     * Tests the capabilities of an AssociationDecorator.
     * @throws Exception when something goes wrong
     */
    public void testAssociations() throws Exception
    {
        DecoratorFactory df = DecoratorFactory.getInstance();
        ModelDecorator md =
            (ModelDecorator) df.createDecoratorObject(model);
        ModelElement assClass =
            getModelElement(
                md.getRootPackage(),
                new String[] { "associations", "ClassAssociations" },
                0);
        assertNotNull(assClass);
        ClassifierDecorator clazz =
            (ClassifierDecorator) df.createDecoratorObject(assClass);
        for (Iterator i3 = clazz.getAssociationEnds().iterator();
            i3.hasNext();
            )
        {
            AssociationEndDecorator aed =
                (AssociationEndDecorator) i3.next();
            assertNotNull(aed);
            aed = aed.getOtherEnd();
            assertNotNull(aed);
            String role = aed.getRoleName();
            if (role.equals(ONE2ONE))
            {
                assertTrue(aed.isOne2One());
            }
            else if (role.equals(ONE2MANY))
            {
                assertTrue(aed.isOne2Many());
            }
            else if (role.equals(MANY2ONE))
            {
                assertTrue(aed.isMany2One());
            }
            else if (role.equals(MANY2MANY))
            {
                assertTrue(aed.isMany2Many());
            }
            else if (role.equals("aggregation"))
            {
                assertTrue(aed.isAggregation());
            }
            else if (role.equals("composition"))
            {
                assertTrue(aed.isComposition());
            }
        }
    }

    /**
     * Tests the capabilities of a DependencyDecorator.
     * @throws Exception when something goes wrong
     */
    public void testDependencies() throws Exception
    {
        DecoratorFactory df = DecoratorFactory.getInstance();
        ModelDecorator md =
            (ModelDecorator) df.createDecoratorObject(model);
        ModelElement depClass =
            getModelElement(
                md.getRootPackage(),
                new String[] { "dependencies", "ClassDependencies" },
                0);
        assertNotNull(depClass);
        ClassifierDecorator clazz =
            (ClassifierDecorator) df.createDecoratorObject(depClass);
        Collection dependencies = clazz.getDependencies();
        assertNotNull(dependencies);
        assertEquals(3, dependencies.size());
        HashMap expectedResults = new HashMap();
        expectedResults.put("Class_2", "ok");
        expectedResults.put("Class_3", "ok");
        expectedResults.put("Class_4", "ok");
        for (Iterator i3 = dependencies.iterator(); i3.hasNext();)
        {
            DependencyDecorator dd = (DependencyDecorator) i3.next();
            assertNotNull(dd);
            String targetName = dd.getTargetType().getName();
            assertNotNull(
                "Unexpected class name: " + targetName,
                expectedResults.get(targetName));
        }
    }

    private static ModelElement getModelElement(
        Namespace namespace,
        String[] fqn,
        int pos)
    {

        if ((namespace == null) || (fqn == null) || (pos > fqn.length))
        {
            return null;
        }

        if (pos == fqn.length)
        {
            return namespace;
        }

        Collection elements = namespace.getOwnedElement();
        for (Iterator i = elements.iterator(); i.hasNext();)
        {
            ModelElement element = (ModelElement) i.next();
            assertNotNull(element);
            if (element.getName() != null
                && element.getName().equals(fqn[pos]))
            {
                int nextPos = pos + 1;

                if (nextPos == fqn.length)
                {
                    return element;
                }

                if (element instanceof Namespace)
                {
                    return getModelElement(
                        (Namespace) element,
                        fqn,
                        nextPos);
                }

                return null;
            }
        }

        return null;
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

}
