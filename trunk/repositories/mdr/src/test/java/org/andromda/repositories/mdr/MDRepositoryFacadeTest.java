package org.andromda.repositories.mdr;

import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;

import org.andromda.core.repository.RepositoryFacadeException;
import org.omg.uml.UmlPackage;

/**
 * @author amowers
 *
 * 
 */
public class MDRepositoryFacadeTest extends TestCase
{
	private URL modelURL = null;
	private MDRepositoryFacade repository = null;

	/**
	 * Constructor for MDRepositoryFacadeTest.
	 * @param arg0
	 */
	public MDRepositoryFacadeTest(String arg0)
	{
		super(arg0);
	}

	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		if (modelURL == null)
		{
			modelURL = TestModel.MODEL_URI;
			repository = new MDRepositoryFacade();
		}

	}

	public void testReadModel()
	{
		try
		{
			repository.readModel(modelURL, null);
		}
		catch (IOException ioe)
		{
			assertNull(ioe.getMessage(), ioe);
		}
		catch (RepositoryFacadeException rre)
		{
			assertNull(rre.getMessage(), rre);
		}
	}

	public void testGetLastModified()
	{
		try
		{
			repository.readModel(modelURL, null);
			assertEquals(
				modelURL.openConnection().getLastModified(),
				repository.getLastModified());
		}
		catch (IOException ioe)
		{
			assertNull(ioe.getMessage(), ioe);
		}
		catch (RepositoryFacadeException rre)
		{
			assertNull(rre.getMessage(), rre);
		}

	}

	public void testGetModel()
	{

		try
		{
			repository.readModel(modelURL, null);
			assertTrue(repository.getModel().getModel() instanceof UmlPackage);
		}
		catch (IOException ioe)
		{
			assertNull(ioe.getMessage(), ioe);
		}
		catch (RepositoryFacadeException rre)
		{
			assertNull(rre.getMessage(), rre);
		}
	}

}
