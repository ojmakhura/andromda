package org.andromda.core.uml14;

import java.util.Iterator;
import java.util.Collection;
import java.net.URL;
import org.andromda.core.mdr.MDRepositoryFacade;
import org.andromda.core.TestModel;
import org.andromda.core.common.RepositoryReadException;
import java.io.IOException;
import junit.framework.TestCase;


/**
 * @author amowers
 *
 * Tests the basic UMLBaseScriptHelper operations by
 * verifying that an object model can be read and accessed.
 * 
 * The test selects one model element from a known test model and
 * checks that the package name and stereotype name are correct.
 * 
 */
public class UMLBaseHelperTest extends TestCase
{
    // file name containing the Test Model
	private final static String XMI_FILE_URL = 
        TestModel.XMI_FILE_URL;
    
    // name of model element to be used by the test
	private final static String CLASS_NAME = 
        TestModel.CLASSA_NAME;
    
    // package name containing test model element
	private final static String PACKAGE_NAME = 
        TestModel.CLASSA_PACKAGE_NAME;
    
    // stereotype name assigned to model element
	private final static String STEREOTYPE_NAME = 
        TestModel.CLASSA_STEREOTYPE_NAME;

    private UMLBaseHelper helper = null;
    
	protected URL modelURL = null;
	protected MDRepositoryFacade repository = null;
	protected Object modelElement = null;

	/**
	 * Constructor for UMLBaseHelperTest.
	 * @param arg0
	 */
	public UMLBaseHelperTest(String arg0)
	{
		super(arg0);
	}

	/**
	 * @see TestCase#setUp()
     * 
     * In this setup we will read the model in and then
     * find one model element from that model use for testing.
     * 
     * We look for ModelElement named CLASS_NAME.
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		if (modelURL != null)
		{
			// setup has already completed once 
			return;
		}

        // locate the TestModel file
		modelURL = new URL(XMI_FILE_URL);
		

        // create repository and script helper
		repository = new MDRepositoryFacade();

		try
		{
            // load the model and associate script helper
			repository.readModel(modelURL);
			getHelper().setModel(repository.getModel());
            
            // search the model for model elements to use
            // in this classes tests
			for (Iterator i = getHelper().getModelElements().iterator();
				i.hasNext();
				)
			{
				selectModelElement(i.next());
			}
		}
		catch (IOException ioe)
		{
			assertNull(ioe.getMessage(), ioe);
		}
		catch (RepositoryReadException rre)
		{
			assertNull(rre.getMessage(), rre);
		}
	}

	public void testGetName()
	{
		String message = "expected to find model element " + CLASS_NAME;
        
		assertNotNull(message, modelElement);
	}

	public void testGetPackageName()
	{
		String packagename = getHelper().getPackageName(modelElement);
		String message =
			"expected package name "
				+ PACKAGE_NAME
				+ " for class "
				+ CLASS_NAME
				+ " but got "
				+ packagename;

		assertTrue(message, PACKAGE_NAME.equals(packagename));
	}


	public void testGetStereotypeNames()
	{
		Collection stereotypeNames = getHelper().getStereotypeNames(modelElement);

		Iterator i = stereotypeNames.iterator();
        
		assertTrue(
			"expected at least one stereotype on class " + CLASS_NAME,
			i.hasNext());
            
		assertTrue(
			"expected stereotype name to be " + STEREOTYPE_NAME,
			STEREOTYPE_NAME.equals(i.next()));
	}

	protected void selectModelElement(Object object)
	{
		// look for a particular model element by name and
        // save that object for use in the tests
		if (CLASS_NAME.equals(getHelper().getName(object)))
		{
			modelElement = object;
		}
	}

    protected UMLBaseHelper getHelper()
    {
        if (helper == null)
        {
            helper = new UMLBaseHelper();
        }
        
        return helper;
    }
}
