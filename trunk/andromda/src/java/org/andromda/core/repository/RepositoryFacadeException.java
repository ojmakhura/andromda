package org.andromda.core.repository;

import java.lang.reflect.Method;

/**
 * An exception thrown whenever an error is encountered while performing
 * processing RepositoryFacade processing.
 * 
 * @author    <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public final class RepositoryFacadeException extends RuntimeException
{

	/**
	 *  Constructor for the MetaDataReadException object
	 */
	public RepositoryFacadeException()
	{
		super();
	}

	/**
	 *  Constructor for the MetaDataReadException object
	 *
	 *@param  message  describes cause of the exception
	 */
	public RepositoryFacadeException(String message)
	{
		super(message);
	}

	/**
	 *  Constructor for the MetaDataReadException object
	 *
	 *@param  message  describes cause of the exception
	 *@param  cause  original exception that caused this exception
	 */
	public RepositoryFacadeException(String message, Throwable cause)
	{
		super(message + ": " + cause.getMessage());
		myInitCause(cause);
	}

	/**
	 *  Description of the Method
	 *
	 *@param  cause  chained this exception to the original cause
	 */
	private void myInitCause(Throwable cause)
	{
		if (null != initCauseMethod)
		{
			try
			{
				initCauseMethod.invoke(this, new Object[] { cause });
			}
			catch (Exception ex)
			{
				// We're probably running in a pre-1.4 JRE
				// Ignore the exception
			}
		}
	}

	private static Method initCauseMethod = null;

	static {
		try
		{
			Class myClass = RepositoryFacadeException.class;
			initCauseMethod =
				myClass.getMethod("initCause", new Class[] { Throwable.class });
		}
		catch (Exception ex)
		{
			// We're probably running in a pre-1.4 JRE
			// Ignore the exception
		}
	}
}
