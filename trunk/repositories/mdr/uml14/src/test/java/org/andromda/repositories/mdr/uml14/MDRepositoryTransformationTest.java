package org.andromda.repositories.mdr.uml14;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.namespace.NamespaceComponents;
import org.andromda.core.repository.Repositories;
import org.andromda.repositories.mdr.MDRepositoryFacade;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.ModelManagementPackage;


/**
 * @author <A HREF="httplo://www.amowers.com">Anthony Mowers</A>
 * @author Chad Brandon
 */
public class MDRepositoryTransformationTest
    extends TestCase
{
    private URL modelURL = null;
    private MDRepositoryFacade repository = null;

    /**
     * Constructor for MDRepositoryTransformationTest.
     *
     * @param name
     */
    public MDRepositoryTransformationTest(String name)
    {
        super(name);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
        AndroMDALogger.initialize();
        if (modelURL == null)
        {
            modelURL = TestModel.getModel();
            NamespaceComponents.instance().discover();
            Repositories.instance().initialize();
            repository = (MDRepositoryFacade)Repositories.instance().getImplementation("netBeansMDR");
            repository.open();
        }
    }

    /**
     * Demonstrates how to dynamically add an attribute onto a class in a model.
     * <p/>
     * It loads a model from XMI file, looks a class with a particular fully qualified name and adds an attribute onto
     * that class.
     *
     * @throws Exception
     */
    public void testTransformModel()
        throws Exception
    {
        repository.readModel(
            new String[] {modelURL.toString()},
            null);
        final ModelAccessFacade modelFacade = repository.getModel();
        UmlPackage umlPackage = (UmlPackage)modelFacade.getModel();
        ModelManagementPackage modelManagementPackage = umlPackage.getModelManagement();

        // A given XMI file can contain multiptle models.
        // Use the first model in the XMI file
        Model model = (Model)(modelManagementPackage.getModel().refAllOfType().iterator().next());

        // look for a class with the name 'org.EntityBean'
        String[] fullyQualifiedName = {"org", "andromda", "ClassA"};

        UmlClass umlClass = (UmlClass)getModelElement(
                model,
                fullyQualifiedName,
                0);

        // create an attribute
        Attribute attribute = umlPackage.getCore().getAttribute().createAttribute();
        attribute.setName("attributeAA");

        // assign the attribute to the class
        attribute.setOwner(umlClass);
    }

    private static ModelElement getModelElement(
        Namespace namespace,
        String[] fullyQualifiedName,
        int pos)
    {
        if ((namespace == null) || (fullyQualifiedName == null) || (pos > fullyQualifiedName.length))
        {
            return null;
        }

        if (pos == fullyQualifiedName.length)
        {
            return namespace;
        }

        Collection elements = namespace.getOwnedElement();
        for (final Iterator iterator = elements.iterator(); iterator.hasNext();)
        {
            ModelElement element = (ModelElement)iterator.next();
            if (element.getName().equals(fullyQualifiedName[pos]))
            {
                int nextPos = pos + 1;

                if (nextPos == fullyQualifiedName.length)
                {
                    return element;
                }

                if (element instanceof Namespace)
                {
                    return getModelElement(
                        (Namespace)element,
                        fullyQualifiedName,
                        nextPos);
                }

                return null;
            }
        }

        return null;
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown()
        throws Exception
    {
        this.repository.clear();
        this.repository = null;
        super.tearDown();
    }
}