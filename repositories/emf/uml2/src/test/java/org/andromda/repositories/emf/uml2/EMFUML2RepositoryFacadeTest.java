package org.andromda.repositories.emf.uml2;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
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
        Set stereotypes = element.getAppliedStereotypes();
        if (stereotypes.isEmpty())
        {
            stereotypes = element.getApplicableStereotypes();
        }
        ArrayList<String> names = new ArrayList<String>();
        for (final Stereotype stereotype : stereotypes)
        {
            names.add(stereotype.getName());
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
            assertEquals(
                "Test Model",
                model.getName());
            Collection elements = UML2Util.findNamedElements(
                    model.eResource(),
                    "Test Model::testPackage::Top",
                    true);
            Class umlClass = (Class)elements.iterator().next();
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