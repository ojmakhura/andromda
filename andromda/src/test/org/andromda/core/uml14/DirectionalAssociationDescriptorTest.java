package org.andromda.core.uml14;

import org.omg.uml.foundation.core.AssociationEnd;
import org.andromda.core.TestModel;

/**
 * @author amowers
 *
 * 
 */
public class DirectionalAssociationDescriptorTest
	extends UMLBaseHelperTest
	implements TestModel
{
    private UMLStaticHelper helper;
    
	protected AssociationEnd one2many;
	protected AssociationEnd many2many;
	protected AssociationEnd many2one;
	protected AssociationEnd one2one;

	/**
	 * Constructor for DirectionalAssociationDescriptorTest.
	 * @param arg0
	 */
	public DirectionalAssociationDescriptorTest(String arg0)
	{
		super(arg0);
	}

	/**
	 * @see UMLBaseHelperTest#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	public void testGetName()
	{
		String message = "";

		message = "unable to find assocationEnd: " + ONE2ONE;
		assertNotNull(message, one2one);

		message = "unable to find assocationEnd: " + ONE2MANY;
		assertNotNull(message, one2many);

		message = "unable to find assocationEnd: " + MANY2MANY;
		assertNotNull(message, many2many);

		message = "unable to find assocationEnd: " + MANY2ONE;
		assertNotNull(message, many2one);
	}

	public void testIsOne2Many()
	{
	}

	public void testIsMany2Many()
	{
	}

	public void testIsOne2One()
	{
	}

	public void testIsMany2One()
	{
	}

	protected void selectModelElement(Object object)
	{
		super.selectModelElement(object);

		if (object instanceof AssociationEnd)
		{
			AssociationEnd ae = (AssociationEnd) object;
			String name = helper.getName(object);

			if (ONE2ONE.equals(name))
				one2one = ae;
			else if (ONE2MANY.equals(name))
				one2many = ae;
			else if (MANY2MANY.equals(name))
				many2many = ae;
			else if (MANY2ONE.equals(name))
				many2one = ae;
			else
			{
			};
		}

	}
    
    protected UMLDefaultHelper getHelper()
    {
        if (helper == null)
        {
            helper = new UMLStaticHelper();
        }
        
        return helper;
    }
}
