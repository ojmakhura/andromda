package org.andromda.core.anttasks;

import org.andromda.core.common.RepositoryFacade;
import org.andromda.core.common.ScriptHelper;
import org.apache.tools.ant.BuildException;

/**
 * 
 * Objects of this class are instantiated when
 * the <code>&lt;repository&gt;</code> tag occurs in the build.xml file
 * as a nested tag within the <code>&lt;andromda&gt;</code> tag.
 * 
 * <p>This tag enables an ANT user to configure the object model 
 * repository that ANDROMDA uses during code generation.  It also provides
 * the ability to customize the API used by the code generation templates to
 * access the repository.</p>
 * 
 * @author Anthony Mowers
 *  
 */
public class RepositoryConfiguration
{
	private static final String DEFAULT_REPOSITORY_CLASSNAME =
		"org.andromda.core.simpleoo.JAXBRepositoryFacade";
    private static final String DEFAULT_SCRIPT_HELPER_CLASSNAME =
        "org.andromda.core.simpleoo.JAXBScriptHelper";
	private Class repositoryClass = null;
	private Class scriptHelperClass = null;


	public void setClassname(String repositoryClassName)
	{
		try
		{
			repositoryClass = Class.forName(repositoryClassName);
		}
		catch (ClassNotFoundException cnfe)
		{
			new BuildException(cnfe);
		}

	}
    
    public void setTransformClassname(String scriptHelperClassName)
    {
        try
        {
       
            scriptHelperClass = Class.forName(scriptHelperClassName);
        }
        catch (ClassNotFoundException cnfe)
        {
            new BuildException(cnfe);
        }
    }

	public RepositoryFacade createRepository()
	{
		RepositoryFacade instance = null;

		try
		{
			if (repositoryClass == null)
			{
				// use the default repository implementation
				repositoryClass =
					Class.forName(DEFAULT_REPOSITORY_CLASSNAME);
			}
			instance = (RepositoryFacade) repositoryClass.newInstance();
		}
		catch (ClassNotFoundException cnfe)
		{
			throw new BuildException(
				DEFAULT_REPOSITORY_CLASSNAME + " class could not be found",
				cnfe);
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



	public ScriptHelper createTransform()
	{
		ScriptHelper instance = null;

		try
		{
            if (scriptHelperClass == null)
            {
                // use the default script helper implementation
                scriptHelperClass =
                    Class.forName(DEFAULT_SCRIPT_HELPER_CLASSNAME);
            }
			instance = (ScriptHelper) scriptHelperClass.newInstance();
		}
        catch (ClassNotFoundException cnfe)
        {
            throw new BuildException(
                DEFAULT_SCRIPT_HELPER_CLASSNAME + " class could not be found",
                cnfe);
        }
		catch (InstantiationException ie)
		{
			throw new BuildException(
				"could not instantiate transform " + scriptHelperClass);
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
