/*
 * Created on Jul 27, 2003
 *
 */
package org.andromda.repositories.mdr;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.andromda.core.repository.RepositoryFacadeException;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.ModelManagementPackage;

/**
 * @author amowers
 *
 * 
 */
public class MDRepositoryTransformationTest extends TestCase {
	private URL modelURL = null;
	private MDRepositoryFacade repository = null;

	/**
	 * Constructor for MDRepositoryTransformationTest.
	 * @param arg0
	 */
	public MDRepositoryTransformationTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		if (modelURL == null) {
			modelURL = TestModel.MODEL_URI;
			repository = new MDRepositoryFacade();
		}
	}

	/**
     * Demonstrates how to dynamically add an attribute onto a class in
     * a model.
     * 
     * It loads a model from XMI file, looks a class with a particular
     * fully qualified name and adds an attribute onto that class.
     * 
	 * @throws Exception
	 */
	public void testTransformModel() throws Exception {
		try {
			repository.readModel(modelURL, null);
			UmlPackage umlPackage = (UmlPackage) repository.getModel().getModel();
			ModelManagementPackage modelManagementPackage =
				umlPackage.getModelManagement();
                
            // A given XMI file can contain multiptle models.
            // Use the first model in the XMI file
			Model model =
				(Model) (modelManagementPackage
					.getModel()
					.refAllOfType()
					.iterator()
					.next());
                    
            // look for a class with the name 'org.EntityBean'
			String[] fqn = { "org", "andromda", "ClassA" };

            UmlClass umlClass = (UmlClass) getModelElement(model, fqn, 0);
            
            // create an attribute
			Attribute attribute =
				umlPackage.getCore().getAttribute().createAttribute();
			attribute.setName("attributeAA");
            
            // assign the attribute to the class
			attribute.setOwner(umlClass);

		} catch (IOException ioe) {
			assertNull(ioe.getMessage(), ioe);
		} catch (RepositoryFacadeException rre) {
			assertNull(rre.getMessage(), rre);
		}
	}

	private static ModelElement getModelElement(
		Namespace namespace,
		String[] fqn,
		int pos) {

		if ((namespace == null) || (fqn == null) || (pos > fqn.length)) {
			return null;
		}

		if (pos == fqn.length) {
			return namespace;
		}

		Collection elements = namespace.getOwnedElement();
		for (Iterator i = elements.iterator(); i.hasNext();) {
			ModelElement element = (ModelElement) i.next();
			if (element.getName().equals(fqn[pos])) {
				int nextPos = pos + 1;

				if (nextPos == fqn.length) {
					return element;
				}

				if (element instanceof Namespace) {
					return getModelElement((Namespace) element, fqn, nextPos);
				}

				return null;
			}
		}

		return null;
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
