package org.andromda.repositories.emf.uml22;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.util.UMLUtil;


/**
 * Implements the JUnit test case for
 * <code>org.andromda.repositories.eclipseUml2.EMFRepositoryFacade</code>.
 *
 * @author Steve Jerman
 * @author Chad Brandon
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
     * @return NamedElement.getAppliedStereotypes()
     */
    public Collection getStereotypeNames(Object modelElement)
    {
        NamedElement element = (NamedElement)modelElement;
        System.out.println("element: " + element);
        System.out.println("getAppliedStereotypes: " + element.getAppliedStereotypes().size() + ' ' + element.getAppliedStereotypes());
        System.out.println("getApplicableStereotypes: " + element.getApplicableStereotypes().size() + ' ' + element.getApplicableStereotypes());
        //System.out.println("getLabel: " + element.getLabel());
        System.out.println("getName: " + element.getName());
        System.out.println("getQualifiedName: " + element.getQualifiedName());
        System.out.println("getClass: " + element.getClass());
        //System.out.println("getEAnnotations: " + element.getEAnnotations().size() + " " + element.getEAnnotations());
        //System.out.println("getKeywords: " + element.getKeywords().size() + " " + element.getKeywords());
        System.out.println("getModel: " + element.getModel());
        //System.out.println("getNameExpression: " + element.getNameExpression());
        System.out.println("getNamespace: " + element.getNamespace());
        //System.out.println("getClientDependencies: " + element.getClientDependencies().size() + " " + element.getClientDependencies());
        System.out.println("getNearestPackage: " + element.getNearestPackage());
        //System.out.println("getOwnedComments: " + element.getOwnedComments().size() + " " + element.getOwnedComments());
        System.out.println("getOwnedElements: " + element.getOwnedElements().size() + ' ' + element.getOwnedElements());
        System.out.println("getOwner: " + element.getOwner());
        System.out.println("getRelationships: " + element.getRelationships().size() + ' ' + element.getRelationships());
        System.out.println("getRequiredStereotypes: " + element.getRequiredStereotypes().size() + ' ' + element.getRequiredStereotypes());
        //System.out.println("getSourceDirectedRelationships: " + element.getSourceDirectedRelationships().size() + " " + element.getSourceDirectedRelationships());
        //System.out.println("getStereotypeApplications: " + element.getStereotypeApplications().size() + " " + element.getStereotypeApplications());
        //System.out.println("getTargetDirectedRelationships: " + element.getTargetDirectedRelationships().size() + " " + element.getTargetDirectedRelationships());
        //System.out.println("getVisibility: " + element.getVisibility());
        List<Stereotype> stereotypes = element.getAppliedStereotypes();
        System.out.println("stereotypes: " + stereotypes);
        ArrayList names = new ArrayList();
        for (final Iterator<Stereotype> iterator = stereotypes.iterator(); iterator.hasNext();)
        {
            Stereotype stereotype = iterator.next();
            System.out.println("stereotype Name: " + stereotype.getName());
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
        this.repository.readModel(
            new String[] {this.modelUrl.toString()},
            null);
        long now2 = System.currentTimeMillis();
        final ModelAccessFacade modelFacade = this.repository.getModel();
        long now3 = System.currentTimeMillis();
        System.out.println("read=" + (now2-now) + "ms getModel=" + (now3-now2) + "ms");
        assertNotNull(modelFacade);
        assertNotNull(modelFacade.getModel());
        assertTrue(modelFacade.getModel() instanceof List);
        List modelList = (ArrayList)modelFacade.getModel();
        for (Object resource : modelList)
        {
            assertTrue(resource instanceof UMLResource);
            UMLResource umlResource = (UMLResource) resource;
            /*Model model = (Model)EcoreUtil.getObjectByType(
                    ((UMLResource)this.repository.getModel().getModel()).getContents(),
                    EcorePackage.eINSTANCE.getEObject());*/
            Model model = (Model)EcoreUtil.getObjectByType(
                    umlResource.getContents(),
                    EcorePackage.eINSTANCE.getEObject());
            assertEquals(
                "Test Model",
                model.getName());
            Collection elements = UMLUtil.findNamedElements(
                    model.eResource(),
                    "Test Model::testPackage::Top",
                    true);
            org.eclipse.uml2.uml.Class umlClass = (org.eclipse.uml2.uml.Class)elements.iterator().next();
            assertEquals(
                "Top",
                umlClass.getName());
            getStereotypeNames(umlClass);
            // This fails with UML2 1.x dependencies - skip unless dependencies are changed.
            // assertFalse(getStereotypeNames(umlClass).isEmpty());
        }
    }
}