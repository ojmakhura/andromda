package org.andromda.core.simpleuml;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.TestModel;
import org.andromda.core.uml14.UMLDefaultHelper;
import org.andromda.core.uml14.UMLScriptHelperTest;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.modelmanagement.UmlPackage;


/**
 * @author amowers
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SimpleOOHelperTest extends UMLScriptHelperTest {
    Operation operation = null;
    SimpleOOHelper helper = null;
    Attribute attribute = null;
    AssociationEnd associationEnd = null;
    
	/**
	 * Constructor for SimpleOOHelperTest.
	 * @param arg0
	 */
	public SimpleOOHelperTest(String arg0) {
		super(arg0);
	}

    protected void setUp() throws Exception
    {
        super.setUp();
    }
    
	public void testGetModel() {
       assertTrue(getHelper().getModel() instanceof UMLModel);
	}

	public void testGetModelElements() {
        
        Collection modelElements = getHelper().getModelElements();
        for (Iterator i=modelElements.iterator();i.hasNext();)
        {
            Object o = i.next();
            if (o instanceof Classifier) {
                assertTrue(o instanceof UMLClassifier);
            }
        }
	}

    public void testGetComponentInterfaceName()
    {
        assertNotNull(this.modelElement);
        assertEquals(
            helper.getComponentInterfaceName(this.modelElement),
            TestModel.CLASSA_NAME + "Local" );
    }
    
    public void testFindTagValue()
    {
        assertEquals(
            helper.findTagValue((ModelElement)modelElement,"testTagName"),
            "test tag value");
    }
    
    public void testGetPackages()
    {
        UMLModel umlModel = (UMLModel)(getHelper().getModel());
        
        Collection packages = umlModel.getPackages();
        for (Iterator i=packages.iterator();i.hasNext();)
        {
            Object o = i.next();
            assertTrue(o instanceof UmlPackage);
            assertTrue(o instanceof UMLPackage);
            UMLPackage p = (UMLPackage)o;
            for (Iterator j=p.getClasses().iterator();j.hasNext();)
            {
                Object c = j.next();
                assertTrue(c instanceof Classifier);
                assertTrue(c instanceof UMLClassifier);
                Classifier classifier = (Classifier)c;
                
            }
        }
    }
    
    public void testFindAttributeJDBCType()
    {
       assertNotNull(attribute);
       assertEquals(
        helper.findAttributeJDBCType(attribute),
        TestModel.ATTRIBUTEA_TYPE);
    }
    
    public void testGetOperationSignature()
    {
        assertNotNull(operation);
        UMLOperation umlOperation = 
            (UMLOperation)POperation.newInstance(helper,operation);
         
        assertEquals(
            umlOperation.getVisibility().toString(),
            TestModel.OPERATIONA_VISIBILITY);
            
        assertEquals(
            helper.getStereotype(umlOperation.getId()),
            TestModel.OPERATIONA_STEREOTYPE);
            
        assertEquals(
            helper.getOperationSignature(operation),
            TestModel.OPERATIONA_SIGNATURE);
    }
 
    public void testGetRoleName()
    {
        assertNotNull(associationEnd);
        UMLAssociationEnd umlAssociation =
            (UMLAssociationEnd)PAssociationEnd.newInstance(helper,associationEnd);
        assertEquals(
            umlAssociation.getRoleName(),
            "TheClassAF");
    }
    
    protected UMLDefaultHelper getHelper()
    {
        if (helper == null)
        {
            helper = new SimpleOOHelper();
        }
        
        return helper;
    }
    
    protected void selectModelElement(Object element)
    {
        super.selectModelElement(element);
        
        if (element instanceof Attribute)
        {
            attribute = (Attribute)element;
        }
        if (element instanceof Operation)
        {
            operation = (Operation)element;
        }
        if (element instanceof AssociationEnd)
        {
            associationEnd = (AssociationEnd)element;
        }
    }
}
