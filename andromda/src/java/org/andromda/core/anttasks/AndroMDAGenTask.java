package org.andromda.core.anttasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.andromda.cartridges.interfaces.CartridgeException;
import org.andromda.cartridges.interfaces.IAndroMDACartridge;
import org.andromda.cartridges.interfaces.OutletDictionary;
import org.andromda.cartridges.mgmt.CartridgeDictionary;
import org.andromda.cartridges.mgmt.CartridgeFinder;

import org.andromda.core.common.CodeGenerationContext;
import org.andromda.core.common.DbMappingTable;
import org.andromda.core.common.RepositoryFacade;
import org.andromda.core.common.RepositoryReadException;
import org.andromda.core.common.ScriptHelper;
import org.andromda.core.common.StdoutLogger;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;

/**
 * This class represents the <code>&lt;andromda&gt;</code> custom task which can
 * be called from an ant script. 
 * 
 * The &lt;andromda&gt; task facilitates Model Driven Architecture by enabling
 * the generation of source code, configuration files, and other such artifacts
 * from a UML model.
 * 
 * @author    <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author    <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public class AndroMDAGenTask extends MatchingTask
{
    private static final String DEFAULT_DBMAPPING_TABLE_CLASSNAME =
        "org.andromda.core.dbmapping.DigesterDbMappingTable";

    /**
     *  the base directory
     */
    private File baseDir = null;

    /**
     *  check the last modified date on files. defaults to true
     */
    private boolean lastModifiedCheck = true;

    /**
     *  the mappings from java data types to JDBC and SQL datatypes.
     */
    private DbMappingTable typeMappings = null;

    /**
     *  the file to get the velocity properties file
     */
    private File velocityPropertiesFile = null;

    /**
     *  User properties that were specified by nested tags in the ant script.
     */
    private ArrayList userProperties = new ArrayList();

    private RepositoryConfiguration repositoryConfiguration = null;

    /**
     * An optional URL to a model
     */
    private URL modelURL = null;

    /**
     * Dictionary of defined outlets. An outlet is a symbolic alias name
     * for a physical directory.
     */
    private OutletDictionary outletDictionary = new OutletDictionary();

    /**
     * Temporary list of mappings from the &lt;outlet&gt; subtask.
     * Will be transferred to the outletDictionary before execution starts.
     */
    private ArrayList outletMappingList = new ArrayList();

    /**
     * Default properties for the Velocity scripting engine.
     */
    private Properties velocityProperties;

    /**
     * Dictionary of installed cartridges, searchable by stereotype.
     */
    private CartridgeDictionary cartridgeDictionary;

    /**
     * <p>
     * Creates a new <code>AndroMDAGenTask</code> instance.
     * </p>
     */
    public AndroMDAGenTask()
    {
    }

    public void setModelURL(URL modelURL)
    {
        this.modelURL = modelURL;
    }

    /**
     * Adds a mapping for a cartridge outlet to a physical directory.
     * Example from a build.xml file:
     * &lt;outlet cartridge="ejb" outlet="beans" dir="${my.beans.dir}" /&gt;
     * 
     * @param om the outlet mapping javabean supplied by Ant
     */
    public void addOutlet(OutletMapping om)
    {
        outletMappingList.add(om);
    }

    /**
     *  <p>
     *
     *  Sets the base directory from which the object model files are read. This
     *  defaults to the base directory of the ant project if not provided.</p>
     *
     *@param  dir  a <code>File</code> with the path to the base directory
     */
    public void setBasedir(File dir)
    {
        baseDir = dir;
    }

    /**
     *  <p>
     *
     *  Reads the configuration file for mappings of Java types to JDBC and SQL
     *  types.</p>
     *
     *@param  dbMappingConfig  XML file with type to database mappings
     *@throws  BuildException  if the file is not accessible
     */
    public void setTypeMappings(File dbMappingConfig)
    {
        try
        {
            Class mappingClass =
                Class.forName(DEFAULT_DBMAPPING_TABLE_CLASSNAME);
            typeMappings = (DbMappingTable) mappingClass.newInstance();

            typeMappings.read(dbMappingConfig);
        }
        catch (IllegalAccessException iae)
        {
            throw new BuildException(iae);
        }
        catch (ClassNotFoundException cnfe)
        {
            throw new BuildException(cnfe);
        }
        catch (RepositoryReadException rre)
        {
            throw new BuildException(rre);
        }
        catch (IOException ioe)
        {
            throw new BuildException(ioe);
        }
        catch (InstantiationException ie)
        {
            throw new BuildException(ie);
        }
    }

    /**
     *  <p>
     *
     *  Allows people to set the path to the <code>velocity.properties</code> file.
     *  </p> <p>
     *
     *  This file is found relative to the path where the JVM was run. For example,
     *  if <code>build.sh</code> was executed in the <code>./build</code>
     *  directory, then the path would be relative to this directory.</p> <p>
     *
     *
     *@param  velocityPropertiesFile  a <code>File</code> with the path to the
     *      velocity properties file
     */
    public void setVelocityPropertiesFile(File velocityPropertiesFile)
    {
        this.velocityPropertiesFile = velocityPropertiesFile;
    }

    /**
     *  <p>
     *
     *  Turns on/off last modified checking for generated files. If checking is
     *  turned on, overwritable files are regenerated only when the model is newer
     *  than the file to be generated. By default, it is on.</p>
     *
     *@param  lastmod  set the modified check, yes or no?
     */
    public void setLastModifiedCheck(boolean lastmod)
    {
        this.lastModifiedCheck = lastmod;
    }

    /**
     *  <p>
     *
     *  Add a user property specified as a nested tag in the ant build script.</p>
     *
     *@param  up  the UserProperty that ant already constructed for us
     */
    public void addUserProperty(UserProperty up)
    {
        userProperties.add(up);
    }

    /**
     *  <p>
     *
     *  Starts the generation of source code from an object model. 
     * 
     *  This is the main entry point of the application. It is called by ant whenever 
     *  the surrounding task is executed (which could be multiple times).</p>
     *
     *@throws  BuildException  if something goes wrong
     */
    public void execute() throws BuildException
    {
        DirectoryScanner scanner;
        String[] list;
        String[] dirs;

        if (baseDir == null)
        {
            // We directly change the user variable, because it
            // shouldn't lead to problems
            baseDir = project.resolveFile(".");
        }

        if (typeMappings == null)
        {
            throw new BuildException("The typeMappings attribute of <andromda> has not been set - it is needed for class attribute to database column mapping.");
        }

        initOutletDictionary();
        initVelocityProperties();
        List cartridges = initCartridges();

        // log("Transforming into: " + destDir.getAbsolutePath(), Project.MSG_INFO);

        createRepository().createRepository().open();

        if (modelURL == null)
        {
            // find the files/directories
            scanner = getDirectoryScanner(baseDir);

            // get a list of files to work on
            list = scanner.getIncludedFiles();

            if (list.length > 0)
            {
                for (int i = 0; i < list.length; ++i)
                {
                    URL modelURL = null;
                    File inFile = new File(baseDir, list[i]);

                    try
                    {
                        modelURL = inFile.toURL();
                        process(modelURL);
                    }
                    catch (MalformedURLException mfe)
                    {
                        throw new BuildException(
                            "Malformed model file URL: " + modelURL);
                    }
                }
            }
            else
            {
                throw new BuildException("Couldn't find any input xmi.");
            }
        }
        else
        {
            // get the model via URL
            process(modelURL);
        }

        for (Iterator iter = cartridges.iterator(); iter.hasNext();)
        {
            IAndroMDACartridge cart = (IAndroMDACartridge) iter.next();
            cart.shutdown();
        }

        createRepository().createRepository().close();
    }

    /**
     * Initializes the Velocity properties. This will tell
     * Velocity that the AndroMDA templates can be found using the classpath.
     * 
     * @see org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
     * @throws BuildException
     */
    private void initVelocityProperties() throws BuildException
    {
        boolean hasProperties = false;
        velocityProperties = new Properties();

        if (velocityPropertiesFile == null)
        {
            // We directly change the user variable, because it
            // shouldn't lead to problems
            velocityPropertiesFile = new File("velocity.properties");
        }

        FileInputStream fis = null;
        try
        {
            // We have to reload the properties every time in the
            // (unlikely?) case that another task has changed them.
            fis = new FileInputStream(velocityPropertiesFile);
            velocityProperties.load(fis);
            hasProperties = true;
        }
        catch (FileNotFoundException fnfex)
        {
            // We ignore the exception and only complain later if we
            // don't have a template path as well
        }
        catch (IOException ioex)
        {
            // We ignore the exception and only complain later if we
            // don't have a template path as well
        }
        finally
        {
            if (null != fis)
            {
                try
                {
                    fis.close();
                }
                catch (IOException ioex)
                {
                    // Not much that can be done
                }
            }
        }
    }

    /**
     * This method would normally be unnecessary. It is here because of a bug in
     * ant. Ant calls addOutlet() before the OutletMapping javabean is fully
     * initialized. So we kept the javabeans in an ArrayList that we have to
     * copy into the dictionary now.
     */
    private void initOutletDictionary()
    {
        for (Iterator iter = outletMappingList.iterator(); iter.hasNext();)
        {
            OutletMapping om = (OutletMapping) iter.next();
            outletDictionary.addOutletMapping(
                om.getCartridge(),
                om.getOutlet(),
                om.getDir());
        }
        outletMappingList = null;
    }

    /**
     * Initialize the cartridge system. Discover all installed cartridges and
     * register them in the cartridge dictionary.
     */
    private List initCartridges() throws BuildException
    {
        CartridgeFinder.initClasspath(getClass());
        try
        {
            List cartridges = CartridgeFinder.findCartridges();

            if (cartridges.size() <= 0)
            {
                StdoutLogger.error(
                    "Warning: No cartridges found, check configuration!");
            }
            else
            {
                cartridgeDictionary = new CartridgeDictionary();
                for (Iterator cartridgeIterator = cartridges.iterator();
                    cartridgeIterator.hasNext();
                    )
                {
                    IAndroMDACartridge cartridge =
                        (IAndroMDACartridge) cartridgeIterator.next();
                    
                    cartridge.init(velocityProperties);
                    
                    List stereotypes =
                        cartridge.getDescriptor().getSupportedStereotypes();
                    for (Iterator stereotypeIterator =
                        stereotypes.iterator();
                        stereotypeIterator.hasNext();
                        )
                    {
                        String stType = (String) stereotypeIterator.next();
                        cartridgeDictionary.addCartridge(stType, cartridge);
                    }
                }
            }
            return cartridges;
        }
        catch (IOException e)
        {
            throw new BuildException(e);
        }
        catch (Exception e)
        {
            throw new BuildException(e);
        }

    }

    private void process(URL url) throws BuildException
    {
        CodeGenerationContext context = null;

        try
        {
            //-- command line status
            StdoutLogger.info("Input:  " + url);

            // configure repository
            RepositoryFacade repository =
                createRepository().createRepository();
            repository.open();
            repository.readModel(url);

            // configure script helper
            ScriptHelper scriptHelper =
                createRepository().createTransform();
            scriptHelper.setModel(repository.getModel());
            scriptHelper.setTypeMappings(typeMappings);
            // @TODO: why does scripthelper have to know typeMappings?
            
            context =
                new CodeGenerationContext(
                    repository,
                    scriptHelper,
                    typeMappings,
                    outletDictionary,
                    lastModifiedCheck,
                    userProperties);
        }
        catch (FileNotFoundException fnfe)
        {
            throw new BuildException("Model file not found: " + modelURL);
        }
        catch (IOException ioe)
        {
            throw new BuildException(
                "Exception encountered while processing: " + modelURL);
        }
        catch (RepositoryReadException mdre)
        {
            throw new BuildException(mdre);
        }

        // process all model elements
        Collection elements = context.getScriptHelper().getModelElements();
        for (Iterator it = elements.iterator(); it.hasNext();)
        {
            processModelElement(context, it.next());
        }
        context.getRepository().close();

    }

    /**
     * <p>Processes one type (e.g. class, interface or datatype) but possibly
     * with several templates.</p>
     *
     *@param  mdr              Description of the Parameter
     *@param  modelElement     Description of the Parameter
     *@throws  BuildException  if something goes wrong
     */
    private void processModelElement(
        CodeGenerationContext context,
        Object modelElement)
        throws BuildException
    {
        String name = context.getScriptHelper().getName(modelElement);
        Collection stereotypeNames =
            context.getScriptHelper().getStereotypeNames(modelElement);

        for (Iterator i = stereotypeNames.iterator(); i.hasNext();)
        {
            String stereotypeName = (String) i.next();

            processModelElementStereotype(
                context,
                modelElement,
                stereotypeName);
        }

    }

    /**
     * Generate code from a model element, using exactly one of its stereotypes.
     * 
     * @param context the context for the code generation
     * @param modelElement the model element
     * @param stereotypeName the name of the stereotype
     * @throws BuildException if something goes wrong
     */
    private void processModelElementStereotype(
        CodeGenerationContext context,
        Object modelElement,
        String stereotypeName)
        throws BuildException
    {
        Collection suitableCartridges =
            cartridgeDictionary.lookupCartridges(stereotypeName);
        // @todo: lookup cartridges not only by stereotype 
        // but also by properties which come from the tagged 
        // values of the model element. This is to find those
        // cartridges that support the proper architectural aspect.

        if (suitableCartridges == null)
        {
            return;
        }

        for (Iterator iter = suitableCartridges.iterator();
            iter.hasNext();
            )
        {
            IAndroMDACartridge cartridge = (IAndroMDACartridge) iter.next();

            try
            {
                cartridge.processModelElement(
                    context,
                    modelElement,
                    stereotypeName);
            }
            catch (CartridgeException e)
            {
                throw new BuildException(e);
            }
        }
    }

    /**
     * Creates and returns a repsository configuration object.  
     * 
     * This enables an ANT build script to use the &lt;repository&gt; ant subtask
     * to configure the model repository used by ANDROMDA during code
     * generation.
     * 
     * @return RepositoryConfiguration
     * @throws BuildException
     */
    public RepositoryConfiguration createRepository() throws BuildException
    {
        if (repositoryConfiguration == null)
        {
            repositoryConfiguration = new RepositoryConfiguration();
        }

        return repositoryConfiguration;
    }

}
