package org.andromda.core.anttasks;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;

/**
 * This class implements the <code>&lt;template&gt;</code> tag
 * which can used within the ant <code>&lt;andromda&gt;</code> 
 * tag to add additional code generation templates to androMDA.
 * 
 * @author Matthias Bohlen
 *
 */
public class TemplateConfiguration
{

    public TemplateConfiguration()
    {
    }

    /**
     * Constructor which is used to build default
     * template configurations when initializing the AndroMDAGenTask.
     * 
     * @param stereotype the name of the stereotype
     * @param sheet name of the style sheet file
     * @param outputPattern the pattern to build the output file name
     * @param outputDir the output directory
     * @param overwrite yes/no whether output file should be overwritten
     */
    public TemplateConfiguration(
        String stereotype,
        String sheet,
        String outputPattern,
        File outputDir,
        boolean overwrite)
    {
        this.stereotype = stereotype;
        this.sheet = sheet;
        this.outputPattern = outputPattern;
        this.outputDir = outputDir;
        this.overwrite = overwrite;
        this.transformClass = null;
    }

    /**
     * Sets a reference to the ant project.
     * Is used when the TemplateConfiguration object is added to the
     * collection of templates in the AndroMDA ant task.
     * 
     * @param p the project
     */
    void setProject(Project p)
    {
        this.project = p;
    }
    
   /**
     * Sets the class name of object that the
     * template code generation scripts will use
     * to access the object model.  The class must implement
     * the ScriptHelper interface.
     * 
     * <p> This is an optional parameter and if it is not set
     * it defaults to the default transform class or the
     * one which was configured using the 
     * <code>&lt;repository&gt;</code> tag. </p>
     *  
     * <p> By writing ones own transformer class some of the
     * more complicated code generation logic can be moved from
     * the code generation script and into the transformer
     * implementation. </p>
     * 
     * @see org.andromda.core.common.ScriptHelper
     * 
     * @param scriptHelperClassName
     */
    public void setTransformClassname(String scriptHelperClassName)
    {
         try {
            transformClass = Class.forName(scriptHelperClassName);
        }
        catch (ClassNotFoundException cnfe)
        {
            new BuildException(cnfe);
        }
    }


    /**
     * Returns the class of the transform object
     * that will be used to the code generation templates
     * to access the object model.
     * 
     * @return Class	
     */
	public Class getTransformClass()
    {
    	return transformClass;
    }
     
     /**
     * Tells us the stereotype in the UML model that
     * should drive code generation with this template.
     * @param stereotype the name of the stereotype
     */
    public void setStereotype(String stereotype)
    {
        this.stereotype = stereotype;
    }

    /**
     * Tells us the stereotype in the UML model that
     * should drive code generation with this template.
     * @return String the name of the stereotype
     */
    public String getStereotype()
    {
        return stereotype;
    }

    /**
     * Tells us which Velocity stylesheet to use as a template.
     * @param sheet points to the script
     */
    public void setSheet(String sheet)
    {
        this.sheet = sheet;
    }

    /**
     * Tells us which Velocity stylesheet to use as a template.
     * @return File points to the script
     */
    public String getSheet()
    {
        return sheet;
    }

    /**
     * Sets the pattern that is used to build the
     * name of the output file.
     * @param outputPattern the pattern in java.text.MessageFormat syntax
     */
    public void setOutputPattern(String outputPattern)
    {
        this.outputPattern = outputPattern;
    }

    /**
     * Gets the pattern that is used to build the
     * name of the output file.
     * @return String the pattern in java.text.MessageFormat syntax
     */
    public String getOutputPattern()
    {
        return outputPattern;
    }

    /**
     * Sets the directory where the output file
     * that is generated from this template should be placed,
     * @param outputDir points to the output directory
     */
    public void setOutputDir(File outputDir)
    {
        this.outputDir = outputDir;
    }

    /**
     * Sets the directory where the output file
     * that is generated from this template should be placed,
     * @return File points to the output directory
     */
    public File getOutputDir()
    {
        return outputDir;
    }

    /**
     * Tells us whether output files generated by this
     * template should be overwritten if they already exist.
     * @param overwrite overwrite the file yes/no
     */
    public void setOverwrite(boolean overwrite)
    {
        this.overwrite = overwrite;
    }

    /**
     * Tells us whether output files generated by this
     * template should be overwritten if they already exist.
     * @return boolean
     */
    public boolean isOverwrite()
    {
        return overwrite;
    }

    /**
     * Creates a FileSet object when the <code>&lt;fileset&gt;</code> tag
     * in the build.xml file is encountered.
     * @return FileSet
     */
    public FileSet createFileset()
    {
        fileSet = new FileSet();
        return fileSet;
    }

    public class TemplateSingleConfig
    {
        TemplateSingleConfig(String sheetName)
        {
            this.sheetName = sheetName;
        }

        /**
         * Return the file name of the Velocity stylesheet.
         * @return String the file name relative to the template path
         */
        public String getSheetName()
        {
            return sheetName;
        }

        /**
         * Returns the fully qualified output file, that means:
         * <ul>
         * <li>the output pattern has been translated</li>
         * <li>the output dir name has been prepended</li>
         * </ul>
         * 
         * @param inputClassName name of the class from the UML model
         * @param inputPackageName name of the package from the UML model 
         *                         in which the class is contained
         * @return File absolute file
         */
        public File getFullyQualifiedOutputFile(
            String inputClassName,
            String inputPackageName)
        {
            int dotIndex = sheetName.indexOf(".");
            String sheetBaseName = sheetName.substring(0, dotIndex);

            Object[] arguments =
                {
                    inputPackageName.replace('.', '/'),
                    inputClassName,
                    sheetBaseName,
                    getStereotype()};

            String outputFileName =
                MessageFormat.format(outputPattern, arguments);

            return new File(getOutputDir(), outputFileName);
        }

        private String sheetName;
    }

    /**
     * Gets the template path of the configured templates.d
     * If the "sheet" attribute was set, then return null,
     * because the template will be searched relative to
     * the "templatePath" attribute of the <code>&lt;uml2ejb&gt;</code> task.
     * If the <code>&lt;fileset&gt;</code> element was given,
     * then return the path of that fileset.
     * 
     * @return String the template path or null
     */
    public String getTemplatePath()
    {
        if (sheet != null)
        {
            return null;
        }

        DirectoryScanner ds = fileSet.getDirectoryScanner(project);
        return ds.getBasedir().getPath();
    }

    /**
     * Gets a list of template files that were configured
     * with the <code>&lt;fileset&gt;</code> tag.
     * @return Collection a collection of templates, described by TemplateSingleConfig objects
     * @see TemplateSingleConfig
     */
    public Collection getTemplateFiles()
    {
        ArrayList result = new ArrayList();
        if (sheet != null)
        {
            result.add(new TemplateSingleConfig(sheet));
            return result;
        }

        DirectoryScanner ds = fileSet.getDirectoryScanner(project);
        String templateFiles[] = ds.getIncludedFiles();
        for (int i = 0; i < templateFiles.length; i++)
        {
            result.add(new TemplateSingleConfig(templateFiles[i]));
        }
        return result;
    }

    /**
     * Just for debugging.
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "TemplateConfiguration: "
            + stereotype
            + " "
            + sheet
            + " "
            + outputPattern
            + " "
            + outputDir
            + " "
            + overwrite
            + " "
            + fileSet;
    }

    private FileSet fileSet;
    private String stereotype;
    private String sheet;
    private String outputPattern;
    private File outputDir;
    private boolean overwrite;
    private Project project;
    private Class transformClass;
}
