package org.andromda.maven.plugin.modelarchiver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.manager.ArchiverManager;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Base class for all ArchiverMojos
 *
 * @author Plushnikov Michail
 * @version $Id: $
 * @since 3.4
 */
public abstract class BaseArchiveMojo extends AbstractMojo
{
    /**
     * Single directory that contains the model
     *
     * @parameter expression="${basedir}/src/main/uml"
     * @required
     */
    protected File modelSourceDirectory;
    /**
     * Directory that resources are copied to during the build.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    protected String workDirectory;
    /**
     * The directory for the generated emx.
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    protected String outputDirectory;
    /**
     * The name of the emx file to generate.
     *
     * @parameter alias="modelName" expression="${project.build.finalName}"
     * @required
     * @readonly
     */
    protected String finalName;
    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     * @description "the maven project to use"
     */
    protected MavenProject project;

    /**
     * To look up Archiver/UnArchiver implementations
     *
     * @parameter expression="${component.org.codehaus.plexus.archiver.manager.ArchiverManager}"
     * @required
     */
    protected ArchiverManager archiverManager;

    /**
     * The maven project's helper.
     *
     * @parameter expression="${component.org.apache.maven.project.MavenProjectHelper}"
     * @required
     * @readonly
     */
    protected MavenProjectHelper projectHelper;

    /**
     * Whether or not to do replacement of embedded model HREF reference extensions.
     *
     * @parameter expression=false
     * @required
     */
    protected boolean replaceExtensions;

    /**
     * Deletes all files with given extension in the given directory
     *
     * @param pPath      path to directory
     * @param pExtension extension of files
     */
    protected void deleteFiles(String pPath, String pExtension)
    {
        Iterator<File> lFileIter = FileUtils.iterateFiles(new File(pPath), new String[]{pExtension}, false);
        while (lFileIter.hasNext())
        {
            FileUtils.deleteQuietly(lFileIter.next());
        }
    }

    /**
     * Escapes the pattern so that the reserved regular expression
     * characters are used literally.
     *
     * @param pattern the pattern to replace.
     * @return the resulting pattern.
     */
    protected String escapePattern(String pattern)
    {
        pattern = StringUtils.replace(
                pattern,
                ".",
                "\\.");
        pattern = StringUtils.replace(
                pattern,
                "-",
                "\\-");
        return pattern;
    }

    /**
     * Replace all extensions in the file
     *
     * @param pReplacementExtensions Extensions to replace
     * @param pFile                  File where all Extensions should be replaced
     * @throws java.io.IOException Exception on IO-Error
     */
    protected void replaceExtensions(String pReplacementExtensions, File pFile) throws IOException
    {
        String[] replacementExtensions = pReplacementExtensions != null ? pReplacementExtensions.split(",\\s*") : new String[0];

        final String version = escapePattern(this.project.getVersion());
        String contents = FileUtils.readFileToString(pFile);
        for (String replacementExtension : replacementExtensions)
        {
            final String extension = escapePattern(replacementExtension);
            // add ' to extension, to match only href elements (example: href='abcdefg.xml')
            // and not abc.xml.efg substrings
            final String extensionPattern = "((\\-" + version + ")?)" + extension + "(['\"|])";
            final String newExtension = "\\-" + version + extension + "$3";
            contents = contents.replaceAll(
                    extensionPattern,
                    newExtension);
            // Fix replacement error for standard UML profiles which follow the _Profile. naming convention.
            contents =
                    contents.replaceAll(
                            "_Profile\\-" + version,
                            "_Profile");
        }
        FileUtils.writeStringToFile(pFile, contents);
    }

    /**
     * Sets File for current Artifact
     * @param newFile File to set for current Artifact
     */
    protected void setArtifactFile(File newFile)
    {
        final Artifact artifact = this.project.getArtifact();
        // - set the artifact file to the correct file
        artifact.setFile(newFile);
        getLog().debug("File artifact set " + newFile.getAbsolutePath() + " packaging " + project.getPackaging());
    }
}
