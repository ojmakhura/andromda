package org.andromda.repositories.emf.uml2;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.Class;
import org.eclipse.uml2.Model;
import org.eclipse.uml2.NamedElement;
import org.eclipse.uml2.Stereotype;
import org.eclipse.uml2.util.UML2Resource;
import org.eclipse.uml2.util.UML2Util;


/**
 * Implements the JUnit test case for
 * <code>org.andromda.repositories.eclipseUml2.EMFRepositoryFacade</code>.
 *
 * @author Steve Jerman
 * @author Chad Brandon
 * @author Bob Fields
 */
public class EMFUML2RepositoryFacadeTest
    extends TestCase
{
    /**
     * Constructor for EMFUML2RepositoryFacadeTest.
     *
     * @param name the test name
     */
    public EMFUML2RepositoryFacadeTest(String name)
    {
        super(name);
        AndroMDALogger.initialize();
    }

    /**
     * @param modelElement
     * @return StereotypeNames
     */
    public Collection getStereotypeNames(Object modelElement)
    {
        NamedElement element = (NamedElement)modelElement;
        System.out.println("NamedElement: " + element.getName());
        Set stereotypes = element.getAppliedStereotypes();
        System.out.println("Applied Stereotype Names: " + stereotypes.size());
        if (stereotypes.isEmpty())
        {
            stereotypes = element.getApplicableStereotypes();
            System.out.println("Applicable Stereotype Names: " + stereotypes.size());
        }
        ArrayList<String> names = new ArrayList<String>();
        for (final Iterator iterator = stereotypes.iterator(); iterator.hasNext();)
        {
            Stereotype stereotype = (Stereotype)iterator.next();
            names.add(stereotype.getName());
            System.out.println("Stereotype: " + stereotype.getName());
        }
        return names;
    }

    /**
     * The URL to the model
     */
    private URL modelUrl = null;

    /**
     * The repository instance.
     */
    private EMFUML2RepositoryFacade repository = null;

    /**
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();
        if (this.modelUrl == null)
        {
            this.modelUrl = TestModel.getModel();
            assertNotNull(this.modelUrl);

            this.repository = new EMFUML2RepositoryFacade();
            this.repository.open();
        }
    }

    /**
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown()
        throws Exception
    {
        this.repository.close();
        this.repository = null;
        super.tearDown();
    }

    /**
     * 
     */
    public void testGetModel()
    {
        long now = System.currentTimeMillis();
        // Load from org.eclipse.uml2.resources jar.
        URL url = this.getClass().getClassLoader().getResource("profiles/Standard.profile.uml");
        if (url!=null)
        {
            this.repository.readModel(
                new String[] {this.modelUrl.toString()},
                new String[] {url.toString(), 
                    this.getClass().getClassLoader().getResource("libraries/UMLPrimitiveTypes.library.uml").toString(),
                    this.getClass().getClassLoader().getResource("libraries/JavaPrimitiveTypes.library.uml").toString(),
                    this.getClass().getClassLoader().getResource("metamodels/UML.metamodel.uml").toString()});
        }
        else
        {
            this.repository.readModel(
                new String[] {this.modelUrl.toString()},
                null);
        }
        long now2 = System.currentTimeMillis();
        final ModelAccessFacade modelFacade = this.repository.getModel();
        long now3 = System.currentTimeMillis();
        System.out.println("read=" + (now2-now) + "ms getModel=" + (now3-now2) + "ms");
        assertNotNull(modelFacade);
        assertNotNull(modelFacade.getModel());
        assertTrue(modelFacade.getModel() instanceof List);
        for (Object umlmodel : (List)modelFacade.getModel())
        {
            assertTrue(umlmodel instanceof UML2Resource);
            /*Model model = (Model)EcoreUtil.getObjectByType(
                ((UML2Resource)this.repository.getModel().getModel()).getContents(),
                EcorePackage.eINSTANCE.getEObject());*/
            Model model = (Model)EcoreUtil.getObjectByType(
                ((UML2Resource)umlmodel).getContents(), EcorePackage.eINSTANCE.getEObject());
            System.out.println("Model: " + model.getName());
            assertEquals(
                "Test Model",
                model.getName());
            Collection elements = UML2Util.findNamedElements(
                    model.eResource(),
                    "Test Model::testPackage::Top",
                    true);
            Class umlClass = (Class)elements.iterator().next();
            System.out.println("umlClass: " + umlClass.getName());
            assertEquals(
                "Top",
                umlClass.getName());
            getStereotypeNames(umlClass).isEmpty();
            //assertFalse(getStereotypeNames(umlClass).isEmpty());

            elements = UML2Util.findNamedElements(
                model.eResource(),
                "Test Model::testPackage::SubClass1",
                true);
            umlClass = (Class)elements.iterator().next();
            getStereotypeNames(umlClass).isEmpty();
            elements = UML2Util.findNamedElements(
                model.eResource(),
                "Test Model::testPackage::SubClass2",
                true);
            umlClass = (Class)elements.iterator().next();
            getStereotypeNames(umlClass).isEmpty();
            //assertFalse(getStereotypeNames(umlClass).isEmpty());
        }
    }
}