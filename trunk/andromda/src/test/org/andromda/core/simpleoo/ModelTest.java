package org.andromda.core.simpleoo;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import junit.framework.TestCase;

import org.andromda.core.simpleoo.DirectionalAssociationDescriptor;
import org.andromda.core.simpleoo.JAXBScriptHelper;
import org.andromda.core.xml.AssociationLink;
import org.andromda.core.xml.Attribute;
import org.andromda.core.xml.Klass;
import org.andromda.core.xml.Model;
import org.andromda.core.xml.Operation;
import org.andromda.core.xml.Pakkage;

/**
 * Some unit tests for the XML based model classes.
 * 
 * @author Matthias Bohlen
 */
public class ModelTest extends TestCase
{

    /**
     * Constructor for ModelTest.
     * @param name
     */
    public ModelTest(String name)
    {
        super(name);
    }

    private Model testModel = null;
    private Klass testPerson = null;
    private Klass testAdresse = null;
    private JAXBScriptHelper testTransform = null;

    public void testUnmarshalModel() throws Exception
    {
        // assert various things
        assertEquals("Person", testPerson.getName());
        assertEquals("Adresse", testAdresse.getName());

        assertEquals(3, testPerson.getAttributes().size());
        assertEquals(4, testPerson.getOperations().size());

        assertEquals("xmi.3", testPerson.getId());
        assertEquals("xmi.28", testAdresse.getId());

        AssociationLink al =
            (AssociationLink) (testPerson.getAssociationLinks().get(0));
        assertEquals("xmi.99", al.getAssocid());
        assertEquals("xmi.100", al.getThisend());

        al = (AssociationLink) (testAdresse.getAssociationLinks().get(0));
        assertEquals("xmi.99", al.getAssocid());
        assertEquals("xmi.103", al.getThisend());
    }

    public void testAssociationHelper()
    {
        JAXBScriptHelper ah = new JAXBScriptHelper();
        ah.setModel(testModel);
        ah.setTypeMappings(null);

        AssociationLink al1 =
            (AssociationLink) (testPerson.getAssociationLinks().get(0));
        DirectionalAssociationDescriptor ad = ah.getAssociationData(al1);
        assertNotNull(ad);
        assertEquals("One:Many", ad.getMultiplicities());
        assertEquals("thePerson", ad.getSource().getRoleName());
        assertEquals("adressen", ad.getTarget().getRoleName());
        assertEquals("false", ad.getSource().getNavigable());
        assertEquals("true", ad.getTarget().getNavigable());
        assertEquals("xmi.99", ad.getId());

        AssociationLink al2 =
            (AssociationLink) (testAdresse.getAssociationLinks().get(0));
        DirectionalAssociationDescriptor ad2 = ah.getAssociationData(al2);
        assertNotNull(ad2);
        assertEquals("Many:One", ad2.getMultiplicities());
        assertEquals("adressen", ad2.getSource().getRoleName());
        assertEquals("thePerson", ad2.getTarget().getRoleName());
        assertEquals("true", ad2.getSource().getNavigable());
        assertEquals("false", ad2.getTarget().getNavigable());
        assertEquals("xmi.99", ad2.getId());

    }

    public void testOperationSignature()
    {
        JAXBScriptHelper sh = new JAXBScriptHelper();
        sh.setModel(testModel);
        sh.setTypeMappings(null);
        
        Operation o = (Operation) testPerson.getOperations().get(0);
        String sig = sh.getOperationSignature(o);
        assertEquals("void addAdresse (String typ, Adresse a)", sig);

        o = (Operation) testPerson.getOperations().get(1);
        sig = sh.getOperationSignature(o);
        assertEquals("void getAdresse (String typ)", sig);

        o = (Operation) testPerson.getOperations().get(2);
        sig = sh.getOperationSignature(o);
        assertEquals("String getAnschrift ()", sig);

        o = (Operation) testPerson.getOperations().get(3);
        sig = sh.getOperationSignature(o);
        assertEquals(
            "void create (String id, String name, String vorName)",
            sig);
    }

    public void testAttributeStereoTypes()
    {
        assertEquals(
            "id",
            testTransform.getPrimaryKeyAttribute(testPerson).getName());
        assertEquals(
            "id",
            testTransform.getPrimaryKeyAttribute(testAdresse).getName());

        Attribute idAttribute =
            (Attribute) testPerson.getAttributes().get(0);
        String stereoType =
            testTransform.getStereotype(idAttribute.getId());
        assertEquals("PrimaryKey", stereoType);

        idAttribute = (Attribute) testAdresse.getAttributes().get(0);
        stereoType = testTransform.getStereotype(idAttribute.getId());
        assertEquals("PrimaryKey", stereoType);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        // the current input file relative to the baseDir
        File inFile = new File("SimpleOO/build", "testPoseidon.soo");

        // let JAXB read the XML file and unmarshal it into a model tree
        JAXBContext jc =
            JAXBContext.newInstance("de.mbohlen.tools.uml2ejb.xml");

        Unmarshaller u = jc.createUnmarshaller();

        testModel = (Model) u.unmarshal(new FileInputStream(inFile));

        // get the package from the model
        Pakkage p = (Pakkage) testModel.getPackages().get(0);

        // get the two classes from the package
        testPerson = (Klass) p.getClasses().get(0);
        testAdresse = (Klass) p.getClasses().get(1);

        // setup a test transformer
        testTransform = new JAXBScriptHelper();
        testTransform.setModel(testModel);
        testTransform.setTypeMappings(null);
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        testModel = null;
    }

}
