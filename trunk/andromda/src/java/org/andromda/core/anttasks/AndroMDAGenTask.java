package org.andromda.core.anttasks;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.cartridge.AndroMDACartridge;
import org.andromda.core.cartridge.CartridgeFinder;
import org.andromda.core.common.CodeGenerationContext;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ModelPackage;
import org.andromda.core.common.ModelPackages;
import org.andromda.core.common.Namespace;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.StdoutLogger;
import org.andromda.core.repository.RepositoryFacade;
import org.apache.log4j.Logger;
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
 * @author    Chad Brandon
 */
public class AndroMDAGenTask extends MatchingTask
{

    /**
     * Set the context class loader so that any classes using it (the 
     * contextClassLoader) have access to the correct loader.
     */
    static {
        Thread.currentThread().setContextClassLoader(
            AndroMDAGenTask.class.getClassLoader());
    }

    private static final Logger logger =
        Logger.getLogger(AndroMDAGenTask.class);

    /**
     *  the base directory
     */
    private File baseDir = null;

    /**
     *  check the last modified date on files. defaults to true
     */
    private boolean lastModifiedCheck = true;

    /**
     * A Packages object which specify
     * whether or not packages should be processed.
     */
    private ModelPackages packages = new ModelPackages();

    private RepositoryConfiguration repositoryConfiguration = null;

    /**
     * Temporary list of properties from the &lt;namespace&gt; subtask.
     * Will be transferred to the Namespaces instance before execution starts.
     */
    private Collection namespaces = new ArrayList();

    /**
     * An optional URL to a model
     */
    private URL modelURL = null;
    
    /**
     * <p>
     * Creates a new <code>AndroMDAGenTask</code> instance.
     * </p>
     */
    public AndroMDAGenTask()
    {
        StdoutLogger.configure();
    }

    public void setModelURL(URL modelURL)
    {
        this.modelURL = modelURL;
    }

    /**
     * Adds a namespace for a Plugin.  Namespace objects
     * are used to configure Plugins.
     * 
     * @param namespace a Namespace to add to this
     */
    public void addNamespace(Namespace namespace)
    {
        namespaces.add(namespace);
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
     *  Starts the generation of source code from an object model. 
     * 
     *  This is the main entry point of the application. It is called by ant whenever 
     *  the surrounding task is executed (which could be multiple times).</p>
     *
     *@throws  BuildException  if something goes wrong
     */
    public void execute() throws BuildException
    {
        try
        {
            long startTime = System.currentTimeMillis();

            this.initNamespaces();

            DirectoryScanner scanner;
            String[] list;
            
            if (baseDir == null)
            {
                // We directly change the user variable, because it
                // shouldn't lead to problems
                baseDir = this.getProject().resolveFile(".");
            }
            
            CartridgeFinder.instance().discoverCartridges();
            
            Collection cartridges = 
                CartridgeFinder.instance().getCartridges();

            if (cartridges.size() <= 0)
            {
                StdoutLogger.warn(
                    "WARNING! No cartridges found, check your classpath!");
            }

            createRepository().createRepository().open();

            if (modelURL != null) 
            {
                // process the model via the explicit modelURL
                process(modelURL, cartridges);    
            }
            else
            {
                // find the files/directories
                scanner = getDirectoryScanner(baseDir);

                // get a list of files to work on
                list = scanner.getIncludedFiles();

                if (list.length > 0)
                {
                    for (int i = 0; i < list.length; ++i)
                    {
                        URL url = null;
                        File inFile = new File(baseDir, list[i]);

                        try
                        {
                            url = inFile.toURL();
                            process(url, cartridges);
                        }
                        catch (MalformedURLException mfe)
                        {
                            throw new BuildException(
                                "Malformed model URI --> '" + url + "'");
                        }
                    }
                }
                else
                {
                    throw new BuildException("Could not find any model input!");
                }
            }

            createRepository().createRepository().close();
            StdoutLogger.info(
                "completed model processing, TIME --> "
                    + ((System.currentTimeMillis() - startTime) / 1000.0)
                    + "[s]");
            
        }
        finally
        {
            // Set the context class loader back ot its system class loaders
            // so that any processes running after (i.e. XDoclet, etc) won't be trying to use
            // the ClassLoader for this class.
            Thread.currentThread().setContextClassLoader(
                ClassLoader.getSystemClassLoader());
        }
    }

    private void process(URL url, Collection cartridges) throws BuildException
    {
        final String methodName = "AndroMDAGenTask.process";
        ExceptionUtils.checkNull(methodName, "url", url);
        
        CodeGenerationContext context = null;

        try
        {
            //-- command line status
            StdoutLogger.info("Input model --> '" + url + "'");

            // configure repository
            RepositoryConfiguration rc = createRepository();
            RepositoryFacade repository = rc.createRepository();
            repository.open();
            repository.readModel(url, rc.createModuleSearchPath().list());
            
            context =
                new CodeGenerationContext(
                    repository,
                    lastModifiedCheck,
                    packages);
            
            Namespace defaultNamespace = 
            	Namespaces.instance().findNamespace(Namespaces.DEFAULT);
            
            for (Iterator cartridgeIt = cartridges.iterator(); cartridgeIt.hasNext();) 
            {
                AndroMDACartridge cartridge = (AndroMDACartridge)cartridgeIt.next();
                
                String cartridgeName = 
                    cartridge.getDescriptor().getCartridgeName();
                
                Namespace namespace = 
                    Namespaces.instance().findNamespace(cartridgeName);
                
                // make sure we ignore the cartridge if the namespace
                // is set to 'ignore'
                if (defaultNamespace != null || (namespace != null && !namespace.isIgnore())) 
                {
                    cartridge.init();
                    cartridge.processModelElements(context);
                    cartridge.shutdown();
                } 
                else 
                {
                	StdoutLogger.info("namespace for '" + cartridgeName
                        + "' cartridge is either not defined, or has the ignore "
                        + "attribute set to 'true' --> skipping processing");
                }
            }
            repository.close();
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing " + methodName + 
                " with model --> '" 
                + url + "'";
            logger.error(errMsg, th);
            throw new BuildException(
                errMsg, th);
            
        }
    }

    /**
     * This method would normally be unnecessary. It is here because of a bug in
     * ant. Ant calls addNamespace() before the Namespace javabean is fully
     * initialized. So we kept the javabeans in an ArrayList that we have to
     * copy into the Namespaces instance.
     */
    private void initNamespaces()
    {
        for (Iterator iter = namespaces.iterator(); iter.hasNext();)
        {
            Namespace namespace = (Namespace) iter.next();
            if (logger.isDebugEnabled())
                logger.debug("adding namespace --> '" + namespace + "'");
            Namespaces.instance().addNamespace(namespace);
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
            repositoryConfiguration =
                new RepositoryConfiguration(getProject());
        }

        return repositoryConfiguration;
    }

    /**
     * Specifies whether or not AndroMDA should process
     * all packages. If this is set to true, then package elements
     * should be specified if you want to keep certain packages
     * from being processed.  If this is set to false, then you would want
     * to define package elements to specify which packages SHOULD BE
     * processed.  This is useful if you need to reference stereotyped model
     * elements from other packages but you don't want
     * to perform any generation from them. The default is true.
     *
     * @param processAllModelPackages
     * @see addPackage(java.lang.String, boolean)
     */
    public void setProcessAllModelPackages(boolean processAllModelPackages)
    {
        this.packages.setProcessAllPackages(processAllModelPackages);
    }

    /**
     * Adds the <code>packageName</code>.  If processAllModelPackages
     * is set to true, then all packageNames added will be 
     * skipped during processing.  If processAllModelPackages is
     * set to false, then all packages specified by package names
     * are the only packages that will be processed.
     * 
     * @param processPackage the Package that should/shouldn't be processed.
     * 
     * @see setProcessAllModelPackages(boolean)
     */
    public void addModelPackage(ModelPackage modelPackage)
    {
        this.packages.addPackage(modelPackage);
    }
}
