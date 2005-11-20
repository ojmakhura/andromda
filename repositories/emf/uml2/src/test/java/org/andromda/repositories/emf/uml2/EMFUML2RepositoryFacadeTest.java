package org.andromda.repositories.emf.uml2;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.eclipse.uml2.Model;
import org.eclipse.uml2.NamedElement;
import org.eclipse.uml2.Stereotype;
import org.eclipse.uml2.util.UML2Util;


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

    public Collection getStereotypeNames(Object modelElement)
    {
        NamedElement element = (NamedElement)modelElement;
        Set stereotypes = element.getAppliedStereotypes();
        ArrayList names = new ArrayList();
        for (final Iterator iterator = stereotypes.iterator(); iterator.hasNext();)
        {
            Stereotype stereotype = (Stereotype)iterator.next();
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
    protected void tearDown()
        throws Exception
    {
        this.repository.close();
        this.repository = null;
        super.tearDown();
    }

    public void testGetModel()
    {
        repository.readModel(
            new String[] {modelUrl.toString()},
            null);
        final ModelAccessFacade modelFacade = repository.getModel(null);
        assertNotNull(modelFacade);
        assertNotNull(modelFacade.getModel());
        assertTrue(modelFacade.getModel() instanceof Model);
        Model model = (Model)modelFacade.getModel();
        assertEquals(
            "Test Model",
            model.getName());
        Collection elements = UML2Util.findNamedElements(
                model.eResource(),
                "test model::com::cisco::test::Top",
                true);
        org.eclipse.uml2.Class umlClass = (org.eclipse.uml2.Class)elements.iterator().next();
        assertEquals(
            "Top",
            umlClass.getName());
        assertFalse(getStereotypeNames(umlClass).isEmpty());
    }
}