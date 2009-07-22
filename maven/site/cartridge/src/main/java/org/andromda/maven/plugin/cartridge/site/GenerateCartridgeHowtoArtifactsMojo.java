package org.andromda.maven.plugin.cartridge.site;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.andromda.core.common.ResourceUtils;
import org.andromda.maven.plugin.AndroMDAMojo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.maven.project.MavenProject;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.settings.Settings;

/**
 * Goal that runs AndroMDA over the howto model to generate the cartridge java source files
 * which are referenced via the cartridge xhtml docs.  It also unpacks the cartridge related
 * archive files, such as the howto pictures for the cartridge site documentation,
 * to target/site/howto locations preparing for deployment.
 * 
 * @phase site
 * @goal generate-cartridge-howto-artifacts
 * @description Goal to run AndroMDA and generate howto source and unpack cartridge 
 * archive files prior to site deployment
 * @author vancek
 */
public class GenerateCartridgeHowtoArtifactsMojo
    extends AbstractSiteMojo
{
    /**
     * Path to the cartridge howto pictures zip source.  This file's path is typically
     * cartridge/src/site/resource/howto/HowToPictures.zip.
     * 
     * @parameter expression="${basedir}/src/site/resources/howto/HowToPictures.zip"
     */
    private String howtoCartridgePicturesSourcePath;
    
    /**
     * Path to the cartride howto pictures destination extraction directory
     * 
     * @parameter expression="${basedir}/target/site/howto"
     */
    private String howtoCartridgePicturesOutputDirectory;
    
    /**
     * This is the URI to the AndroMDA configuration file.
     *
     * @parameter expression="file:${basedir}/conf/howto/andromda.xml"
     * @required
     */
    private String configurationUri;

    /**
     * @parameter expression="${project.build.filters}"
     */
    private List propertyFiles;

    /**
     * The current user system settings for use in Maven. (allows us to pass the user
     * settings to the AndroMDA configuration).
     *
     * @parameter expression="${settings}"
     * @required
     * @readonly
     */
    private Settings settings;
    
    /**
     * The name of the project injected from pom.xml
     * 
     * @parameter default-value="${project.name}"
     */
    private String projectName;
    
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;
    
    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        this.getLog().info("---------------------------------------------------------------------------------------");
        this.getLog().info("  A n d r o M D A   G e n e r a t e   C a r t r i d g e   H o w T o   A r t i f a c t s");
        this.getLog().info("---------------------------------------------------------------------------------------");

        this.unpackHowToPictures();
        this.generateHowToSource();
        
        this.getLog().info("GENERATE CARTRIDGE HOWTO ARTIFACTS SUCCESSFUL");
    }
    
    /**
     * Unpack the cartridge howto pictures
     * 
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    private void unpackHowToPictures()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            final File howtoCartridgePicturesSourceFile = new File(this.howtoCartridgePicturesSourcePath);
            if (!howtoCartridgePicturesSourceFile.exists())
            {
                throw new MojoExecutionException("Cartridge howto pictures source location is invalid");
            }
            
            this.unpack(
                    howtoCartridgePicturesSourceFile, 
                    new File(this.howtoCartridgePicturesOutputDirectory));
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException)
            {
                throw (MojoExecutionException)throwable;
            }
            throw new MojoExecutionException("An error occured unpacking cartridge howto pictures '" +
                this.project.getArtifactId() + "'",
                ExceptionUtils.getRootCause(throwable));
        }
    }
 
    /**
     * Generate the howto source by running AndroMDA using the howto specific model
     * and andromda.xml config.
     * 
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    private void generateHowToSource()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            final AndroMDAMojo andromdaMojo = new AndroMDAMojo();
            andromdaMojo.setConfigurationUri(this.configurationUri);
            andromdaMojo.setProject(this.project);
            andromdaMojo.setSettings(this.settings);
            andromdaMojo.setPropertyFiles(this.propertyFiles);
            andromdaMojo.execute();
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException)
            {
                throw (MojoExecutionException)throwable;
            }
            throw new MojoExecutionException(
                    "An error occured while running AndroMDA over the cartridge howto model '" +
                    this.project.getArtifactId() + "'", ExceptionUtils.getRootCause(throwable));
        }
    }
}
