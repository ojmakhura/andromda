package org.andromda.core.mdr;

import java.io.IOException;
import java.net.URL;
import junit.framework.TestCase;
import org.omg.uml.UmlPackage;

import org.andromda.core.TestModel;
import org.andromda.core.common.RepositoryReadException;

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
			modelURL =
				MDRepositoryFacadeTest.class.getResource(
					TestModel.XMI_FILE_NAME);
			repository = new MDRepositoryFacade();
		}

		assertNotNull(
			"file: "
				+ TestModel.XMI_FILE_NAME
				+ " not found in classpath",
			modelURL);
	}

	public void testReadModel()
	{
		try
		{
			repository.readModel(modelURL);
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

	public void testGetLastModified()
	{
		try
		{
			repository.readModel(modelURL);
			assertEquals(
				modelURL.openConnection().getLastModified(),
				repository.getLastModified());
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

	public void testGetModel()
	{

		try
		{
			repository.readModel(modelURL);
			assertTrue(repository.getModel() instanceof UmlPackage);
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

}
