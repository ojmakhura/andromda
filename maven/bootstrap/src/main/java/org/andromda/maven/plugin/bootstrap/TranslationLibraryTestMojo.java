package org.andromda.maven.plugin.bootstrap;

import java.io.File;

import java.net.URL;

import java.util.List;

import junit.framework.TestResult;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceUtils;
import org.andromda.maven.plugin.configuration.AbstractConfigurationMojo;
import org.andromda.translation.ocl.testsuite.TranslationTestProcessor;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;


/**
 * The bootstrap version of the translation-library test.
 *
 * @phase test
 * @goal test-translation-library
 * @requiresDependencyResolution test
 * @description runs AndroMDA Translation-Library tests
 * @author Chad Brandon
 */
public class TranslationLibraryTestMojo
    extends AbstractConfigurationMojo
{
    /**
     * Base directory to which the cartridge test report is written
     *
     * @parameter expression="${project.build.directory}/translation-library-test/reports"
     */
    private String reportDirectory;

    /**
     * Whether or not the expression shall be 'traced' (i.e. the TraceTranslator will run instead of the specified translator).
     * This is helpful, in allowing us to see which expressions are being parsed in what order, etc.
     *
     * @parameter expression="${trace.expression}"
     */
    protected boolean traceExpression;

    /**
     * When specified, only this translation will be tested (If more than one TestTranslation-* file is found).
     *
     * @parameter expression="${translation.name}"
     */
    protected String translationName;

    /**
     * The directory containing the test source.
     *
     * @parameter expression="${basedir}"
     * @required
     * @readonly
     */
    protected String testSourceDirectory;

    /**
     * This is the URI to the AndroMDA configuration file.
     *
     * @parameter expression="file:${basedir}/conf/test/andromda.xml"
     * @required
     */
    protected String configurationUri;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

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
     * @parameter expression="${component.org.apache.maven.artifact.factory.ArtifactFactory}"
     * @required
     * @readonly
     */
    private ArtifactFactory factory;

    /**
     * The registered plugin implementations.
     *
     * @parameter expression="${project.build.plugins}"
     * @required
     * @readonlya
     */
    protected List plugins;

    /**
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;

    /**
     * Set this to 'true' to bypass cartridge tests entirely. Its use is NOT RECOMMENDED, but quite convenient on occasion.
     *
     * @parameter expression="${maven.test.skip}"
     */
    protected boolean skip;

    /**
     *  Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite convenient on occasion. 
     *
     * @parameter expression="${skipTests}"
     */
    protected boolean skipTests;

    /**
     * Set this to true to ignore a failure during testing. Its use is NOT RECOMMENDED, but quite convenient on occasion.
     *
     * @parameter expression="${maven.test.failure.ignore}" default-value="false"
     */
    protected boolean testFailureIgnore;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        if (!this.skip && !this.skipTests)
        {
            try
            {
                this.getLog().info("--------------------------------------------------------------------------------");
                this.getLog().info("  A n d r o M D A   T r a n s l a t i o n - L i b r a r y  T e s t   S u i t e  ");
                this.getLog().info("--------------------------------------------------------------------------------");

                this.initializeClasspathFromClassPathElements(this.project.getTestClasspathElements());

                final TranslationTestProcessor processor = TranslationTestProcessor.instance();
                processor.setTranslationName(this.translationName);
                processor.setUseTraceTranslator(this.traceExpression);
                processor.setTestSourceDirectory(this.testSourceDirectory);
                final URL configurationUri = ResourceUtils.toURL(this.configurationUri);
                if (configurationUri == null)
                {
                    throw new MojoExecutionException("No configuration could be loaded from --> '" +
                        this.configurationUri + '\'');
                }
                processor.setConfiguration(this.getConfiguration(configurationUri));

                final TranslationLibraryTestFormatter formatter = new TranslationLibraryTestFormatter();

                // - set the report location
                final File report = new File(this.reportDirectory, this.getProject().getArtifactId() + ".txt");
                formatter.setReportFile(report);
                final TestResult result = new TestResult();
                formatter.startTestSuite(this.getProject().getName());
                result.addListener(formatter);
                processor.setResult(result);
                processor.runSuite();
                this.getLog().info("");
                this.getLog().info("Results:");
                this.getLog().info(formatter.endTestSuite());
                if (result.failureCount() > 0 || result.errorCount() > 0)
                {
                    throw new MojoExecutionException("There are some test failures");
                }
                processor.shutdown();
            }
            catch (final Throwable throwable)
            {
                if (throwable instanceof MojoExecutionException && !this.testFailureIgnore)
                {
                    throw (MojoExecutionException)throwable;
                }
                else if (this.testFailureIgnore)
                {
                    this.getLog().error("An error occured while testing translation-library '" +
                            this.translationName + '\'',
                        ExceptionUtils.getRootCause(throwable));
                }
                else
                {
                    throw new MojoExecutionException("An error occured while testing translation-library '" +
                            this.translationName + '\'',
                            ExceptionUtils.getRootCause(throwable));
                }
            }
        }
        else
        {
            this.getLog().info("Skipping translation-library tests");
        }
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getProject()
     */
    protected MavenProject getProject()
    {
        return this.project;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getPropertyFiles()
     */
    protected List getPropertyFiles()
    {
        return this.propertyFiles;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getSettings()
     */
    protected Settings getSettings()
    {
        return this.settings;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getFactory()
     */
    protected ArtifactFactory getFactory()
    {
        return this.factory;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getPlugins()
     */
    protected List getPlugins()
    {
        return this.plugins;
    }

    /**
     * @see org.andromda.maven.plugin.configuration.AbstractConfigurationMojo#getLocalRepository()
     */
    protected ArtifactRepository getLocalRepository()
    {
        return this.localRepository;
    }
}