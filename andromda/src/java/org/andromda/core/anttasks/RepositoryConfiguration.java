package org.andromda.core.anttasks;

import org.apache.tools.ant.BuildException;
import org.andromda.core.simpleoo.JAXBRepositoryFacade;
import org.andromda.core.simpleoo.JAXBScriptHelper;
import org.andromda.core.common.RepositoryFacade;
import org.andromda.core.common.ScriptHelper;

/**
 * @author amowers
 *
 * 
 */
public class RepositoryConfiguration
{
	private Class repositoryClass = JAXBRepositoryFacade.class;
    private Class scriptHelperClass = JAXBScriptHelper.class;

	public void setClassname(Class repositoryClass)
	{
		this.repositoryClass = repositoryClass;
	}

	public RepositoryFacade createRepository()
	{
		RepositoryFacade instance = null;

		try
		{
			instance = (RepositoryFacade) repositoryClass.newInstance();
		}
		catch (InstantiationException ie)
		{
			throw new BuildException(
				"could not instantiate repository " + repositoryClass);
		}
		catch (IllegalAccessException iae)
		{
			throw new BuildException(
				"unable to access repository constructor "
					+ repositoryClass
					+ "()");
		}
		
		return instance;
	}
	


	public void setTransformClassname(Class scriptHelperClass)
	{
		this.scriptHelperClass = scriptHelperClass;
	}

	public ScriptHelper createTransform()
	{
		ScriptHelper instance = null;

		try
		{
			instance = (ScriptHelper)scriptHelperClass.newInstance();
		}
		catch (InstantiationException ie)
		{
			throw new BuildException(
				"could not instantiate repository " + scriptHelperClass);
		}
		catch (IllegalAccessException iae)
		{
			throw new BuildException(
				"unable to access repository constructor "
					+ scriptHelperClass
					+ "()");
		}
		
		return instance;
	}
	
	
}
