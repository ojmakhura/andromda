package org.andromda.core.anttasks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.andromda.cartridges.interfaces.IAndroMDACartridge;
import org.andromda.cartridges.interfaces.OutletDictionary;
import org.andromda.cartridges.interfaces.TemplateConfiguration;
import org.andromda.cartridges.mgmt.CartridgeDictionary;
import org.andromda.cartridges.mgmt.CartridgeFinder;

import org.andromda.core.common.DbMappingTable;
import org.andromda.core.common.RepositoryFacade;
import org.andromda.core.common.RepositoryReadException;
import org.andromda.core.common.ScriptHelper;
import org.andromda.core.common.StringUtilsHelper;

import org.apache.commons.collections.ExtendedProperties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

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
        "org.andromda.core.dbmapping.CastorDbMappingTable";

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
     *  the VelocityEngine instance to use
     */
    private VelocityEngine ve;

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
        initCartridges();
        initVelocityPropertiesAndEngine();

        // log("Transforming into: " + destDir.getAbsolutePath(), Project.MSG_INFO);

        createRepository().createRepository().open();

        if (modelURL == null)
        {
            // find the files/directories
            scanner = getDirectoryScanner(baseDir);

            // get a list of files to work on
            list = scanner.getIncludedFiles();
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
            // get the model via URL
            process(modelURL);
        }

        createRepository().createRepository().close();
    }

    /**
     * Initializes the Velocity properties and the Velocity engine itself. Tells
     * Velocity that the AndroMDA templates can be found using the classpath.
     * 
     * @see org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
     * @throws BuildException
     */
    private void initVelocityPropertiesAndEngine() throws BuildException
    {
        ve = new VelocityEngine();

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

        try
        {
            // Tell Velocity it should also use the classpath when searching for templates
            ExtendedProperties ep =
                ExtendedProperties.convertProperties(velocityProperties);

            ep.addProperty(
                RuntimeConstants.RESOURCE_LOADER,
                "andromda.cartridges,file");

            ep.setProperty(
                "andromda.cartridges."
                    + RuntimeConstants.RESOURCE_LOADER
                    + ".class",
                ClasspathResourceLoader.class.getName());

            // This is important - Torsten Juergeleit
            // reported that Velocity does not re-load the macros from the template
            // file and sometimes uses a macro from one template file when
            // processing another template file that contains a macro with the
            // same name. This setting forces inline macros to be local, not global.
            ep.setProperty(RuntimeConstants.VM_PERM_INLINE_LOCAL, "true");

            ve.setExtendedProperties(ep);
            ve.init();
        }
        catch (Exception e)
        {
            log("Error: " + e.toString(), Project.MSG_INFO);
            throw new BuildException(e);
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
    private void initCartridges() throws BuildException
    {
        CartridgeFinder.initClasspath(getClass());
        try
        {
            List cartridges = CartridgeFinder.findCartridges();
            cartridgeDictionary = new CartridgeDictionary();
            for (Iterator cartridgeIterator = cartridges.iterator();
                cartridgeIterator.hasNext();
                )
            {
                IAndroMDACartridge cartridge =
                    (IAndroMDACartridge) cartridgeIterator.next();
                List stereotypes =
                    cartridge.getDescriptor().getSupportedStereotypes();
                for (Iterator stereotypeIterator = stereotypes.iterator();
                    stereotypeIterator.hasNext();
                    )
                {
                    String stType = (String) stereotypeIterator.next();
                    cartridgeDictionary.addCartridge(stType, cartridge);
                }
            }
        }
        catch (IOException e)
        {
            throw new BuildException(e);
        }

    }

    private void process(URL url) throws BuildException
    {
        Context context = new Context();

        try
        {
            //-- command line status
            log("Input:  " + url, Project.MSG_INFO);

            // configure repository
            context.repository = createRepository().createRepository();
            context.repository.open();
            context.repository.readModel(url);

            // configure script helper
            context.scriptHelper = createRepository().createTransform();
            context.scriptHelper.setModel(context.repository.getModel());
            context.scriptHelper.setTypeMappings(typeMappings);

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
        Collection elements = context.scriptHelper.getModelElements();
        for (Iterator it = elements.iterator(); it.hasNext();)
        {
            processModelElement(context, it.next());
        }
        context.repository.close();

    }

    /**
     * <p>Processes one type (e.g. class, interface or datatype) but possibly
     * with several templates.</p>
     *
     *@param  mdr              Description of the Parameter
     *@param  modelElement     Description of the Parameter
     *@throws  BuildException  if something goes wrong
     */
    private void processModelElement(Context context, Object modelElement)
        throws BuildException
    {
        String name = context.scriptHelper.getName(modelElement);
        Collection stereotypeNames =
            context.scriptHelper.getStereotypeNames(modelElement);

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
        Context context,
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
            IAndroMDACartridge c = (IAndroMDACartridge) iter.next();

            processModelElementWithCartridge(
                context,
                modelElement,
                c,
                stereotypeName);
        }
    }

    private void processModelElementWithCartridge(
        Context context,
        Object modelElement,
        IAndroMDACartridge cartridge,
        String stereotypeName)
        throws BuildException
    {
        String name = context.scriptHelper.getName(modelElement);
        String packageName =
            context.scriptHelper.getPackageName(modelElement);
        long modelLastModified = context.repository.getLastModified();

        List templates =
            cartridge.getDescriptor().getTemplateConfigurations();
        for (Iterator it = templates.iterator(); it.hasNext();)
        {
            TemplateConfiguration tc = (TemplateConfiguration) it.next();
            if (tc.getStereotype().equals(stereotypeName))
            {
                ScriptHelper scriptHelper = context.scriptHelper;

                if (tc.getTransformClass() != null)
                {
                    // template has its own custom script helper
                    try
                    {
                        context.scriptHelper =
                            (ScriptHelper) tc
                                .getTransformClass()
                                .newInstance();
                        context.scriptHelper.setModel(
                            context.repository.getModel());
                        context.scriptHelper.setTypeMappings(typeMappings);
                    }
                    catch (IllegalAccessException iae)
                    {
                        throw new BuildException(iae);
                    }
                    catch (InstantiationException ie)
                    {
                        throw new BuildException(ie);
                    }
                }

                File outFile =
                    tc.getFullyQualifiedOutputFile(
                        name,
                        packageName,
                        outletDictionary);

                if (outFile != null)
                {
                    try
                    {
                        // do not overwrite already generated file,
                        // if that is a file that the user wants to edit.
                        boolean writeOutputFile =
                            !outFile.exists() || tc.isOverwrite();
                        // only process files that have changed
                        if (writeOutputFile
                            && (lastModifiedCheck == false
                                || modelLastModified > outFile.lastModified()
                            /*
                        *  || styleSheetLastModified > outFile.lastModified()
                        */
                            ))
                        {
                            processModelElementWithOneTemplate(
                                context,
                                modelElement,
                                tc.getSheet(),
                                outFile);
                        }
                    }
                    catch (ClassTemplateProcessingException e)
                    {
                        outFile.delete();
                        throw new BuildException(e);
                    }
                }

                // restore original script helper in case we were
                // using a custom template script helper
                context.scriptHelper = scriptHelper;
            }
        }
    }

    /**
     * <p>
     * Processes  one type (that is class, interface or datatype) with exactly
     * one  template script.
     * </p>
     *
     * @param  context         context for code generation
     * @param  modelElement    the model element for which code should be
     *                         generated
     * @param  styleSheetName  name of the Velocity style sheet
     * @param  outFile         file to which to write the output
     * @throws  ClassTemplateProcessingException  if something goes wrong
     */
    private void processModelElementWithOneTemplate(
        Context context,
        Object modelElement,
        String styleSheetName,
        File outFile)
        throws ClassTemplateProcessingException
    {
        Writer writer = null;

        ensureDirectoryFor(outFile);
        String encoding = getTemplateEncoding();
        try
        {
            writer =
                new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(outFile),
                        encoding));
        }
        catch (Exception e)
        {
            throw new ClassTemplateProcessingException(
                "Error opening output file " + outFile.getName(),
                e);
        }

        try
        {
            VelocityContext velocityContext = new VelocityContext();

            // put some objects into the velocity context
            velocityContext.put("model", context.scriptHelper.getModel());
            velocityContext.put("transform", context.scriptHelper);
            velocityContext.put("str", new StringUtilsHelper());
            velocityContext.put("class", modelElement);
            velocityContext.put("date", new java.util.Date());

            addUserPropertiesToContext(velocityContext);

            // Process the VSL template with the context and write out
            // the result as the outFile.
            // get the template to process
            // the template name is dependent on the class's stereotype
            // e.g. if the class is an "EntityBean", the template name
            // is "EntityBean.vsl".

            Template template = ve.getTemplate(styleSheetName);
            template.merge(velocityContext, writer);

            writer.flush();
            writer.close();
        }
        catch (Exception e)
        {
            try
            {
                writer.flush();
                writer.close();
            }
            catch (Exception e2)
            {
            }

            throw new ClassTemplateProcessingException(
                "Error processing velocity script on " + outFile.getName(),
                e);
        }

        log("Output: " + outFile, Project.MSG_INFO);
    }

    /**
     *  Takes all the UserProperty values that were defined in the ant build.xml
     *  file and adds them to the Velocity context.
     *
     *@param  context  the Velocity context
     */
    private void addUserPropertiesToContext(VelocityContext context)
    {
        for (Iterator it = userProperties.iterator(); it.hasNext();)
        {
            UserProperty up = (UserProperty) it.next();
            context.put(up.getName(), up.getValue());
        }
    }

    /**
     *  Gets the templateEncoding attribute of the AndroMDAGenTask object
     *
     *@return    The templateEncoding value
     */
    private String getTemplateEncoding()
    {
        /*
         *  get the property TEMPLATE_ENCODING
         *  we know it's a string...
         */
        String encoding =
            (String) ve.getProperty(RuntimeConstants.OUTPUT_ENCODING);
        if (encoding == null
            || encoding.length() == 0
            || encoding.equals("8859-1")
            || encoding.equals("8859_1"))
        {
            encoding = "ISO-8859-1";
        }
        return encoding;
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

    /**
     *  <p>
     *
     *  Creates directories as needed.</p>
     *
     *@param  targetFile          a <code>File</code> whose parent directories need
     *      to exist
     *@exception  BuildException  if the parent directories couldn't be created
     */
    private void ensureDirectoryFor(File targetFile) throws BuildException
    {
        File directory = new File(targetFile.getParent());
        if (!directory.exists())
        {
            if (!directory.mkdirs())
            {
                throw new BuildException(
                    "Unable to create directory: "
                        + directory.getAbsolutePath());
            }
        }
    }

    /**
     * Context used for doing code generation
     */
    private static class Context
    {
        RepositoryFacade repository = null;
        ScriptHelper scriptHelper = null;
    }

}
