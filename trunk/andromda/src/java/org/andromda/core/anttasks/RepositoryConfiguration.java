package org.andromda.core.anttasks;

import org.andromda.core.common.RepositoryFacade;
import org.andromda.core.common.ScriptHelper;
import org.apache.tools.ant.BuildException;

/**
 * 
 * This class implements the <code>&lt;repository&gt;</code> tag
 * which can used within the ant <code>&lt;andromda&gt;</code> tag
 * to configure androMDA to use a different object model repository.
 * 
 * <p> One application of this tag is that it supports the
 * possibility of loading an object model from a source other
 * than an XMI file. It could implemented by: <p>
 * 
 * <ul>
 * <li> introducing a new class that extends 
 * org.andromda.core.mdr.MDRespositoryFacade </li>
 * <li> overriding the readModel(URL) method so
 * that it reads the object model from the new object model source </li>
 * <li> storing the results of the object model
 * read into the MDR UML v1.4 repository </li>
 * </ul>
 * 
 * @author Anthony Mowers
 *  
 */
public class RepositoryConfiguration
{
	private static final String DEFAULT_REPOSITORY_CLASSNAME =
		"org.andromda.core.mdr.MDRepositoryFacade";
    private static final String DEFAULT_SCRIPT_HELPER_CLASSNAME =
        "org.andromda.core.simpleuml.SimpleOOHelper";
	private Class repositoryClass = null;
	private Class scriptHelperClass = null;


	/**
	 * Sets the name of the class to use as the repository.  The
     * class must implement the RepositoryFacade interface.
     * 
     * @see org.andromda.core.common.RepositoryFacade
     * 
	 * @param repositoryClassName
	 */
	public void setClassname(String repositoryClassName)
	{
		try
		{
			repositoryClass = Class.forName(repositoryClassName);
		}
		catch (ClassNotFoundException cnfe)
		{
			throw new BuildException(cnfe);
		}

	}
    
	/**
	 * Sets the name of the class that is used by AndroMDA to
     * access object model elements from within he repository.  The
     * class must implement the ScriptHelper interface.
     * 
     * <p> Unless specified otherwise by use of the
     * <code>&lt;template&gt;</code> tag this transformer
     * object will be the object used the code generation scripts
     * to access the object model. </p>
     * 
     * @see TemplateConfiguration
     * @see org.andromda.core.common.ScriptHelper
     * 
	 * @param scriptHelperClassName
	 */
    public void setTransformClassname(String scriptHelperClassName)
    {
        try
        {
            scriptHelperClass = Class.forName(scriptHelperClassName);
        }
        catch (ClassNotFoundException cnfe)
        {
            throw new BuildException(cnfe);
        }
    }

	/**
	 * Creates an instance of the repository.
     * 
	 * @return RepositoryFacade
	 */
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



	/**
	 * Creates an instance of the object model Transfomer.
     * 
	 * @return ScriptHelper
	 */
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
				"unable to access transform constructor "
					+ scriptHelperClass
					+ "()");
		}

		return instance;
	}

}
