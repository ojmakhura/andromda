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
import java.util.Properties;

import org.andromda.core.common.DbMappingTable;
import org.andromda.core.common.RepositoryFacade;
import org.andromda.core.common.RepositoryReadException;
import org.andromda.core.common.ScriptHelper;
import org.andromda.core.common.StringUtilsHelper;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

/**
 * This class represents the <code>&lt;andromda&gt;</code> custom task which can
 * be called from an ant script. 
 * 
 * The &lt;andromda&gt; task facilitates Model Driven Architecture by enabling
 * the generation of source code, configuration files, and other such artifacts
 * from a UML model.
 * 
 *
 *@author    Matthias Bohlen
 *@author    Anthony Mowers
 */
public class AndroMDAGenTask extends MatchingTask {
	private static final String DEFAULT_DBMAPPING_TABLE_CLASSNAME =
		"org.andromda.core.dbmapping.CastorDbMappingTable";

	/**
	 *  the destination directory
	 */
	private File genDestDir = null;
	/**
	 *  the destination directory
	 */
	private File implDestDir = null;

	/**
	 *  the base directory
	 */
	private File baseDir = null;

	/**
	 *  check the last modified date on files. defaults to true
	 */
	private boolean lastModifiedCheck = true;

	/**
	 *  the template path
	 */
	private String templatePath = null;

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
	private VelocityEngine ve = new VelocityEngine();

	/**
	 *  whether to use the default template configuration or not
	 */
	private boolean useDefaultTemplateConfig = true;

	/**
	 *  All templates that will be used
	 */
	private ArrayList templates = new ArrayList();

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
	 *  <p>
	 *
	 *  Simple structure to combine default template initialization data.</p>
	 *
	 *@author    tony
	 */
	private static class TemplateDesc {
		/**
		 *  Description of the Field
		 */
		public String stereotype;
		/**
		 *  Description of the Field
		 */
		public String stylesheetName;
		/**
		 *  Description of the Field
		 */
		public String outFileNamePattern;
		/**
		 *  Description of the Field
		 */
		public boolean overWriteOutFile;
		/**
		 *  Description of the Field
		 */
		public int whichDest;

		/**
		 *  Description of the Field
		 */
		public final static int GENDEST = 1;
		/**
		 *  Description of the Field
		 */
		public final static int IMPLDEST = 2;

		/**
		 *  Constructor for the TemplateDesc object
		 *
		 *@param  stereotype          Description of the Parameter
		 *@param  stylesheetName      Description of the Parameter
		 *@param  outFileNamePattern  Description of the Parameter
		 *@param  whichDest           Description of the Parameter
		 *@param  overWriteOutFile    Description of the Parameter
		 */
		public TemplateDesc(
			String stereotype,
			String stylesheetName,
			String outFileNamePattern,
			int whichDest,
			boolean overWriteOutFile) {
			this.stereotype = stereotype;
			this.stylesheetName = stylesheetName;
			this.outFileNamePattern = outFileNamePattern;
			this.whichDest = whichDest;
			this.overWriteOutFile = overWriteOutFile;
		}
	}

	/**
	 *  the default template configuration
	 */
	private TemplateDesc[] defaultTemplateConfig =
		{
			new TemplateDesc(
				"EntityBean",
				"EntityBean.vsl",
				"{0}/{1}Bean.java",
				TemplateDesc.GENDEST,
				true),
			new TemplateDesc(
				"EntityBean",
				"EntityBeanImpl.vsl",
				"{0}/{1}BeanImpl.java",
				TemplateDesc.IMPLDEST,
				false),
			new TemplateDesc(
				"EntityBean",
				"EntityBeanCMP.vsl",
				"{0}/{1}BeanCMP.java",
				TemplateDesc.GENDEST,
				true),
			new TemplateDesc(
				"StatelessSessionBean",
				"StatelessSessionBean.vsl",
				"{0}/{1}Bean.java",
				TemplateDesc.GENDEST,
				true),
			new TemplateDesc(
				"StatelessSessionBean",
				"StatelessSessionBeanImpl.vsl",
				"{0}/{1}BeanImpl.java",
				TemplateDesc.IMPLDEST,
				false),
			new TemplateDesc(
				"StatefulSessionBean",
				"StatefulSessionBean.vsl",
				"{0}/{1}Bean.java",
				TemplateDesc.GENDEST,
				true),
			new TemplateDesc(
				"StatefulSessionBean",
				"StatefulSessionBeanImpl.vsl",
				"{0}/{1}BeanImpl.java",
				TemplateDesc.IMPLDEST,
				false)};

	/**
	 *  <p>
	 *
	 *  Creates a new <code>AndroMDAGenTask</code> instance.</p>
	 */
	public AndroMDAGenTask() {
	}

	public void setModelURL(URL modelURL) {
		this.modelURL = modelURL;
	}

	/**
	 *  <p>
	 *
	 *  Sets the base directory from which the object model files are read. This
	 *  defaults to the base directory of the ant project if not provided.</p>
	 *
	 *@param  dir  a <code>File</code> with the path to the base directory
	 */
	public void setBasedir(File dir) {
		baseDir = dir;
	}

	/**
	 *  <p>
	 *
	 *  Sets the destination directory in which most of the resulting GEN files
	 *  should be created.</p>
	 *
	 *@param  dir  a <code>File</code> pointing to the destination directory for
	 *      GEN files
	 */
	public void setGenDestdir(File dir) {
		genDestDir = dir;
	}

	/**
	 *  <p>
	 *
	 *  Sets the destination directory in which the bean implementation files
	 *  should be created.</p>
	 *
	 *@param  dir  a <code>File</code> pointing to the destination directory for
	 *      GEN implementation files
	 */
	public void setImplDestdir(File dir) {
		implDestDir = dir;
	}

	/**
	 *  <p>
	 *
	 *  Sets the directory where the <code>*.vsl</code> template files are located.
	 *  This can be defined in <code>velocity.properties</code> or it can be
	 *  defined here. It is an optional argument if it is defined in the Velocity
	 *  properties file already. However, if defined, this value will override the
	 *  path defined in the <code>velocity.properties</code> file.</p>
	 *
	 *@param  templatePath     a <code>File</code> with the path to the *.vsl
	 *      templates
	 *@throws  BuildException  if the path couldn't be accessed
	 */
	public void setTemplatePath(File templatePath) {
		try {
			this.templatePath = templatePath.getCanonicalPath();
		} catch (java.io.IOException ioe) {
			throw new BuildException(ioe);
		}
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
	public void setTypeMappings(File dbMappingConfig) {
		try {
			Class mappingClass =
				Class.forName(DEFAULT_DBMAPPING_TABLE_CLASSNAME);
			typeMappings = (DbMappingTable) mappingClass.newInstance();

			typeMappings.read(dbMappingConfig);
		} catch (IllegalAccessException iae) {
			throw new BuildException(iae);
		} catch (ClassNotFoundException cnfe) {
			throw new BuildException(cnfe);
		} catch (RepositoryReadException rre) {
			throw new BuildException(rre);
		} catch (IOException ioe) {
			throw new BuildException(ioe);
		} catch (InstantiationException ie) {
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
	 *  If the path to the templates is the only velocity property that needs to be
	 *  defined, one can use <code>setTemplatePath()</code> instead.</p>
	 *
	 *@param  velocityPropertiesFile  a <code>File</code> with the path to the
	 *      velocity properties file
	 */
	public void setVelocityPropertiesFile(File velocityPropertiesFile) {
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
	public void setLastModifiedCheck(boolean lastmod) {
		this.lastModifiedCheck = lastmod;
	}

	/**
	 *  <p>
	 *
	 *  Adds a template defined in a nested <code>&lt;template&gt;</code> tag.</p>
	 *
	 *@param  t  a <code>TemplateConfiguration</code> giving the details of the new
	 *      template
	 */
	public void addTemplate(TemplateConfiguration t) {
		t.setProject(project);
		templates.add(t);
	}

	/**
	 *  <p>
	 *
	 *  Add a user property specified as a nested tag in the ant build script.</p>
	 *
	 *@param  up  the UserProperty that ant already constructed for us
	 */
	public void addUserProperty(UserProperty up) {
		userProperties.add(up);
	}

	/**
	 *  <p>
	 *
	 *  Tells us whether the user wants to have the default template configuration
	 *  for entity and session beans.</p>
	 *
	 *@param  useDefaultTemplateConfig  yes/no
	 */
	public void setUseDefaultTemplateConfig(boolean useDefaultTemplateConfig) {
		this.useDefaultTemplateConfig = useDefaultTemplateConfig;
	}

	/**
	 *  <p>
	 *
	 *  Adds the default templates to our list of templates to use.</p>
	 *
	 *@throws  BuildException  if a required output directory is not specified
	 */
	private void initDefaultTemplateConfig() throws BuildException {
		for (int i = 0; i < defaultTemplateConfig.length; i++) {
			TemplateDesc td = defaultTemplateConfig[i];
			File destDir =
				(td.whichDest == TemplateDesc.GENDEST)
					? genDestDir
					: implDestDir;
			if (null == destDir) {
				throw new BuildException(
					((td.whichDest == TemplateDesc.GENDEST)
						? "GEN"
						: "Implementation")
						+ " destination directory is not defined.");
			}

			templates.add(
				new TemplateConfiguration(
					td.stereotype,
					td.stylesheetName,
					td.outFileNamePattern,
					destDir,
					td.overWriteOutFile));
		}
	}

	/**
	 *  <p>
	 *
	 *  Assembles a Velocity template path from several template configuration
	 *  entries. The paths from the template configuration entries are prepended to
	 *  the base paths based on the assumption that they should take precedence
	 *  over the general ones.</p>
	 *
	 *@param  basePath  a <code>String</code> with the base path
	 *@return           a <code>String</code> with the assembled template path
	 */
	private String assembleTemplatePath(String basePath) {
		String separator = "";
		StringBuffer result = new StringBuffer();

		for (Iterator it = templates.iterator(); it.hasNext();) {
			TemplateConfiguration tc = (TemplateConfiguration) it.next();
			String path = tc.getTemplatePath();
			if (path != null) {
				result.append(separator);
				result.append(path);
				separator = ",";
			}
		}
		if (null != basePath && !"".equals(basePath)) {
			result.append(separator);
			result.append(basePath);
		}

		log("Assembled template path: " + result.toString(), Project.MSG_INFO);
		return result.toString();
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
	public void execute() throws BuildException {
		DirectoryScanner scanner;
		String[] list;
		String[] dirs;
		Properties properties = new Properties();
		boolean hasProperties = false;
		String baseTemplatePath;

		if (baseDir == null) {
			// We directly change the user variable, because it
			// shouldn't lead to problems
			baseDir = project.resolveFile(".");
		}

		if (useDefaultTemplateConfig) {
			initDefaultTemplateConfig();
		}
		
		if (typeMappings == null)
		{
			throw new BuildException("The typeMappings attribute of <andromda> has not been set - it is needed for class attribute to database column mapping.");
		}
		
		if (velocityPropertiesFile == null) {
			// We directly change the user variable, because it
			// shouldn't lead to problems
			velocityPropertiesFile = new File("velocity.properties");
		}

		FileInputStream fis = null;
		try {
			// We have to reload the properties every time in the
			// (unlikely?) case that another task has changed them.
			fis = new FileInputStream(velocityPropertiesFile);
			properties.load(fis);
			hasProperties = true;
		} catch (FileNotFoundException fnfex) {
			// We ignore the exception and only complain later if we
			// don't have a template path as well
		} catch (IOException ioex) {
			// We ignore the exception and only complain later if we
			// don't have a template path as well
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException ioex) {
					// Not much that can be done
				}
			}
		}

		// Get the base path from velocity.properties
		baseTemplatePath =
			properties.getProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH);
		if (null != templatePath && !"".equals(templatePath)) {
			// Override with the user specified one
			baseTemplatePath = templatePath;

			/*
			 *  Another option, maybe better than override?
			 */
			/*
			 *  / Put the template path the user specified in front of
			 *  / anything specified in velocities.properties
			 *  baseTemplatePath = templatePath +
			 *  ((null == baseTemplatePath) || ("".equals(baseTemplatePath))
			 *  ? "" : ',' + baseTemplatePath);
			 */
		}

		// prepend any paths specified in template configuration
		// entries to our current ones
		baseTemplatePath = assembleTemplatePath(baseTemplatePath);

		// If all our efforts result in no template path, throw a
		// build exception.
		if (!hasProperties
			&& (null == baseTemplatePath || "".equals(baseTemplatePath))) {
			throw new BuildException(
				"No velocity template path specified."
					+ " Please use the attributes templatePath or velocityPropertiesFile.");
		}

		properties.setProperty(
			RuntimeConstants.FILE_RESOURCE_LOADER_PATH,
			baseTemplatePath);
		// log("Transforming into: " + destDir.getAbsolutePath(), Project.MSG_INFO);

		try {
			ve.init(properties);

			// get the last modification of the VSL stylesheet
			// styleSheetLastModified = ve.getTemplate(style).getLastModified();

		} catch (Exception e) {
			log("Error: " + e.toString(), Project.MSG_INFO);
			throw new BuildException(e);
		}

        createRepository().createRepository().open();
        
		if (modelURL == null) {
			// find the files/directories
			scanner = getDirectoryScanner(baseDir);

			// get a list of files to work on
			list = scanner.getIncludedFiles();
			for (int i = 0; i < list.length; ++i) {
				URL modelURL = null;
				File inFile = new File(baseDir, list[i]);

				try {
					modelURL = inFile.toURL();
					process(modelURL);
				} catch (MalformedURLException mfe) {
					throw new BuildException(
						"Malformed model file URL: " + modelURL);
				}

			}
		} else {
            // get the model via URL
			process(modelURL);
		}

		createRepository().createRepository().close();
	}

	private void process(URL url) throws BuildException {

		Context context = new Context();

		try {
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

		} catch (FileNotFoundException fnfe) {
			throw new BuildException("Model file not found: " + modelURL);
		} catch (IOException ioe) {
			throw new BuildException(
				"Exception encountered while processing: " + modelURL);
		} catch (RepositoryReadException mdre) {
			throw new BuildException(mdre);
		}

		// process all model elements
		Collection elements = context.scriptHelper.getModelElements();
		for (Iterator it = elements.iterator(); it.hasNext();) {
			processModelElement(context, it.next());
		}
		context.repository.close();

	}

	/**
	 *  <p>
	 *
	 *  Processes one type (e.g. class, interface or datatype) but possibly with
	 *  several templates.</p>
	 *
	 *@param  mdr              Description of the Parameter
	 *@param  modelElement     Description of the Parameter
	 *@throws  BuildException  if something goes wrong
	 */
	private void processModelElement(Context context, Object modelElement)
		throws BuildException {
		String name = context.scriptHelper.getName(modelElement);
		Collection stereotypeNames =
			context.scriptHelper.getStereotypeNames(modelElement);

		for (Iterator i = stereotypeNames.iterator(); i.hasNext();) {
			String stereotypeName = (String) i.next();

			processModelElementStereotype(
				context,
				modelElement,
				stereotypeName);
		}

	}

	/**
	 *  Description of the Method
	 *
	 *@param  mdr                 Description of the Parameter
	 *@param  modelElement        Description of the Parameter
	 *@param  stereotypeName      Description of the Parameter
	 *@exception  BuildException  Description of the Exception
	 */
	private void processModelElementStereotype(
		Context context,
		Object modelElement,
		String stereotypeName)
		throws BuildException {
		String name = context.scriptHelper.getName(modelElement);
		String packageName = context.scriptHelper.getPackageName(modelElement);
		long modelLastModified = context.repository.getLastModified();

		for (Iterator it = templates.iterator(); it.hasNext();) {
			TemplateConfiguration tc = (TemplateConfiguration) it.next();
			if (tc.getStereotype().equals(stereotypeName)) {
				ScriptHelper scriptHelper = context.scriptHelper;

				if (tc.getTransformClass() != null) {
					// template has its own custom script helper
					try {
						context.scriptHelper =
							(ScriptHelper) tc.getTransformClass().newInstance();
						context.scriptHelper.setModel(
							context.repository.getModel());
						context.scriptHelper.setTypeMappings(typeMappings);
					} catch (IllegalAccessException iae) {
						throw new BuildException(iae);
					} catch (InstantiationException ie) {
						throw new BuildException(ie);
					}
				}

				Collection singleTemplates = tc.getTemplateFiles();
				for (Iterator it2 = singleTemplates.iterator();
					it2.hasNext();
					) {
					TemplateConfiguration.TemplateSingleConfig tsc =
						(TemplateConfiguration.TemplateSingleConfig) it2.next();

					File outFile =
						tsc.getFullyQualifiedOutputFile(name, packageName);

					try {
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
							)) {
							processModelElementWithOneTemplate(
								context,
								modelElement,
								tsc.getSheetName(),
								outFile);
						}
					} catch (ClassTemplateProcessingException e) {
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
	 *  <p>
	 *
	 *  Processes one type (that is class, interface or datatype) with exactly one
	 *  template script.</p>
	 *
	 *@param  styleSheetName                     name of the Velocity style sheet
	 *@param  outFile                            file to which to write the output
	 *@param  mdr                                Description of the Parameter
	 *@param  modelElement                       Description of the Parameter
	 *@throws  ClassTemplateProcessingException  if something goes wrong
	 */
	private void processModelElementWithOneTemplate(
		Context context,
		Object modelElement,
		String styleSheetName,
		File outFile)
		throws ClassTemplateProcessingException {
		Writer writer = null;

		ensureDirectoryFor(outFile);
		String encoding = getTemplateEncoding();
		try {
			writer =
				new BufferedWriter(
					new OutputStreamWriter(
						new FileOutputStream(outFile),
						encoding));
		} catch (Exception e) {
			throw new ClassTemplateProcessingException(
				"Error opening output file " + outFile.getName(),
				e);
		}

		try {
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
		} catch (Exception e) {
			try {
				writer.flush();
				writer.close();
			} catch (Exception e2) {
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
	private void addUserPropertiesToContext(VelocityContext context) {
		for (Iterator it = userProperties.iterator(); it.hasNext();) {
			UserProperty up = (UserProperty) it.next();
			context.put(up.getName(), up.getValue());
		}
	}

	/**
	 *  Gets the templateEncoding attribute of the AndroMDAGenTask object
	 *
	 *@return    The templateEncoding value
	 */
	private String getTemplateEncoding() {
		/*
		 *  get the property TEMPLATE_ENCODING
		 *  we know it's a string...
		 */
		String encoding =
			(String) ve.getProperty(RuntimeConstants.OUTPUT_ENCODING);
		if (encoding == null
			|| encoding.length() == 0
			|| encoding.equals("8859-1")
			|| encoding.equals("8859_1")) {
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
	public RepositoryConfiguration createRepository() throws BuildException {
		if (repositoryConfiguration == null) {
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
	private void ensureDirectoryFor(File targetFile) throws BuildException {
		File directory = new File(targetFile.getParent());
		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				throw new BuildException(
					"Unable to create directory: "
						+ directory.getAbsolutePath());
			}
		}
	}

	/**
	 * Context used for doing code generation
	 */
	private static class Context {
		RepositoryFacade repository = null;
		ScriptHelper scriptHelper = null;
	}

}
