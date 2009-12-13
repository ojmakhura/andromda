package org.andromda.utils.beautifier.plugin;

/**
 * Copyright 2008 hybrid labs 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import org.andromda.utils.beautifier.core.JavaBeautifier;
import org.andromda.utils.beautifier.core.JavaImportBeautifierImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Runs hybridlabs beautifier.
 *
 * @author Jack Archer, Bob Fields; Plushnikov Michail
 * @version 3.4
 * @phase process-sources
 * @goal beautify-imports
 * @requiresProject false
 */
public class ImportBeautifierMojo extends AbstractMojo {
    /**
     * Location of the directory to be recursively beautified. Defaults to all
     * source directories for parent project (..)
     *
     * @parameter expression="${basedir}"
     * @optional
     */
    private File inputDirectory;

    /**
     * Location of the output directory for beautified source. Defaults to the
     * source directory
     *
     * @parameter expression="${basedir}"
     * @optional
     */
    private File outputDirectory;

    /**
     * Run import beautifier utility.
     *
     * @parameter default-value="true"
     * @optional
     */
    private boolean runBeautifier;

    /**
     * Delegate formatting to Jalopy after beautifying imports.
     *
     * @parameter default-value="false"
     * @optional
     */
    private boolean runJalopy;

    /**
     * Jalopy convention file path
     *
     * @parameter default-value="${basedir}"
     * @optional
     */
    private File conventionFilePath;

    /**
     * Jalopy convention file
     *
     * @parameter default-value="default-convention.xml"
     * @optional
     */
    private String conventionFileName;


    private final static String c_slash = System.getProperty("file.separator");

    /**
     * Mojo
     * @throws MojoExecutionException
     */
    public void execute() throws MojoExecutionException {
        getLog().info("process-sources:beautify-imports on " + this.inputDirectory);
        File file = this.inputDirectory;
        if (!file.exists()) {
            throw new MojoExecutionException("Beautifier format input directory does not exist: "
                    + this.inputDirectory);
        }

        if (this.outputDirectory == null) {
            this.outputDirectory = this.inputDirectory;
        } else {
            File outputFile = this.outputDirectory;
            if (!outputFile.exists()) {
                throw new MojoExecutionException("Beautifier format output directory does not exist: "
                        + this.outputDirectory);
            }
        }
        String directoryString = null;

        try {
            directoryString = file.getCanonicalPath();

            formatAllFiles(directoryString, directoryString, runBeautifier, runJalopy);
        } catch (FileNotFoundException e) {
            throw new MojoExecutionException("FileNotFound creating beautifier output: "
                    + directoryString, e);
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating beautifier output: " + directoryString, e);
        }
    }

    /**
     * Formats all Java files discovered in the supplied directory and
     * subdirectories.
     *
     * @param sourceRootDirectory Directory for Java files to format and replace
     * @throws FileNotFoundException If required files or directories do not exist
     * @throws IOException           If the creation of a new file or directory fails
     */
    public void formatJalopyAllFiles(String sourceRootDirectory)
            throws IOException {
        formatAllFiles(sourceRootDirectory, sourceRootDirectory, false, true);
    }

    /**
     * Formats all Java files discovered in the supplied directory and
     * subdirectories.
     *
     * @param sourceRootDirectory Directory for Java files to format and replace
     * @param targetRootDirectory Target Java source output directory (if null, use source
     *                            directory)
     * @throws FileNotFoundException If required files or directories do not exist
     * @throws IOException           If the creation of a new file or directory fails
     */
    public void formatImportsAllFiles(String sourceRootDirectory,
                                      String targetRootDirectory) throws IOException {
        formatAllFiles(sourceRootDirectory, targetRootDirectory, true, false);
    }

    /**
     * Formats all Java files discovered in the supplied directory and
     * subdirectories.
     *
     * @param sourceRootDirectory Directory for Java files to format and replace
     * @param targetRootDirectory Target Java source output directory (if null, use source
     *                            directory)
     * @throws FileNotFoundException If required files or directories do not exist
     * @throws IOException           If the creation of a new file or directory fails
     */
    public void formatJalopyAllFiles(String sourceRootDirectory,
                                     String targetRootDirectory) throws IOException {
        formatAllFiles(sourceRootDirectory, targetRootDirectory, false, true);
    }

    /**
     * Formats all Java files discovered in the supplied directory and
     * subdirectories.
     *
     * @param sourceRootDirectory Directory for Java files to format and replace
     * @throws FileNotFoundException If required files or directories do not exist
     * @throws IOException           If the creation of a new file or directory fails
     */
    public void formatImportsAllFiles(String sourceRootDirectory)
            throws IOException {
        formatAllFiles(sourceRootDirectory, sourceRootDirectory, true, false);
    }

    /**
     * Formats all Java files discovered in the supplied directory.
     *
     * @param sourceRootDirectory Directory for Java files to format
     * @param targetRootDirectory Output directory for
     * @param imports             Change fully qualified classnames to imports in file
     * @param jalopy              Run jalopy against file
     * @throws FileNotFoundException If required files or directories do not exist
     * @throws IOException           If the creation of a new file or directory fails
     */
    public void formatAllFiles(String sourceRootDirectory,
                               String targetRootDirectory, boolean imports, boolean jalopy)
            throws IOException {

        String message = "Formatting ";
        if (imports) {
            message += "imports";
            if (jalopy) {
                message += " and jalopy";
            }
        } else if (jalopy) {
            message += "jalopy";
        }

        getLog().info(message + ": under " + targetRootDirectory);

        JavaImportBeautifierImpl beautifier = new JavaImportBeautifierImpl();
        setConvention(beautifier);
        beautifier.setOrganizeImports(imports);

        // There are problems with the Jalopy parser for the generated files:
        // Unexpected char '<'
        beautifier.setFormat(jalopy);

        Collection<File> sourceFiles = FileUtils.listFiles(new File(sourceRootDirectory), new String[]{".java"}, true);

        // Now reformat and save the targeted files to the target directory
        for (File sourceFile : sourceFiles) {
            getLog().info(message + sourceFile.getName() + " under " + sourceFile.getParent());

            final String lSource = FileUtils.readFileToString(sourceFile);

            try {
                String output = beautifier.beautify(lSource);
                // Only overwrite the file if the beautifier changes it, or if a
                // different directory
                // Sometimes Jalopy throws an error and returns a zero length file.
                if (null!=output && !lSource.equals(output)) {
                    File targetFile = new File((getTargetDirectory(sourceFile, sourceRootDirectory,
                            targetRootDirectory)), sourceFile.getName());

                    FileOutputStream outputSource = new FileOutputStream(targetFile);
                    outputSource.write(output.getBytes());
                    outputSource.flush();
                    outputSource.close();
                }
            } catch (RuntimeException e) {
                getLog().error("Exception formatting file: " + sourceFile.getName(), e);
            }
        }
        getLog().info("Finished Formatting all files under " + sourceRootDirectory);
    }

    /**
     * Formats a single Java file.
     *
     * @param sourceFile          Java file to format
     * @param targetRootDirectory Output directory for
     * @param imports             Change fully qualified classnames to imports in file
     * @param jalopy              Run jalopy against file
     * @throws FileNotFoundException If required files or directories do not exist
     * @throws IOException           If the creation of a new file or directory fails
     */
    public void formatFile(File sourceFile, String targetRootDirectory,
                           boolean imports, boolean jalopy) throws IOException {
        JavaImportBeautifierImpl beautifier = new JavaImportBeautifierImpl();
        setConvention(beautifier);
        beautifier.setOrganizeImports(imports);
        // There are problems with the Jalopy parser for the generated files:
        // Unexpected char '<'
        beautifier.setFormat(jalopy);

        getLog().info("Formatting file: " + sourceFile.getName());

        final String lSource = FileUtils.readFileToString(sourceFile);

        try {
            String lOutput = beautifier.beautify(lSource);
            // Only overwrite the file if the beautifier changes it, or if a
            // different directory
            // Sometimes Jalopy throws an error and returns a zero length file.
            if (null!=lOutput && !lSource.equals(lOutput)) {
                File targetFile;
                if (targetRootDirectory == null
                        || "".equals(targetRootDirectory)) {
                    targetFile = sourceFile;
                } else {
                    targetFile = new File((getTargetDirectory(sourceFile, sourceFile.getParent(),
                            targetRootDirectory)), sourceFile.getName());
                }
                // File targetDirectory = new File((manager.getTargetDirectory(
                // sourceFile, targetRootDirectory)), sourceFile.getName());
                FileOutputStream outputSource = new FileOutputStream(targetFile);
                outputSource.write(lOutput.getBytes());
                outputSource.flush();
                outputSource.close();
            }
        } catch (RuntimeException e) {
            getLog().error("Exception formatting file: " + sourceFile.getName(), e);
        }
        getLog().info("Finished Formatting all files");
    }

    /**
     * Fetches a target directory, creating it if it doesn't exist
     *
     * @param sourceFile          Filename to create
     * @param sourceRootDirectory sourceFile directory
     * @param targetRootDirectory target directory root to create and return
     * @return the target output directory for the specified re-formatted source
     *         File
     */
    private File getTargetDirectory(File sourceFile,
                                    String sourceRootDirectory, String targetRootDirectory) {
        String path = sourceFile.getParent();
        // Find the part of the source file below the source directory, append
        // to target directory
        int index = path.indexOf(sourceRootDirectory);
        if (index == -1) {
            if (sourceRootDirectory.indexOf('/') > -1) {
                sourceRootDirectory = sourceRootDirectory.replaceAll("/", "\\\\");
                index = path.indexOf(sourceRootDirectory);
            } else {
                sourceRootDirectory = sourceRootDirectory.replaceAll("\\\\", "/");
                index = path.indexOf(sourceRootDirectory);
            }
        }
        String sourcePath = path
                .substring(index + sourceRootDirectory.length());
        if (StringUtils.isEmpty(targetRootDirectory)) {
            targetRootDirectory = sourceRootDirectory;
        }
        File targetPath = new File(targetRootDirectory + sourcePath);
        targetPath.mkdirs();

        return targetPath;
    }

    /**
     * Locates and sets Jalopy formatting convention file
     *
     * @param beautifier Instance of the beautifier class, not yet set to with external
     *                   Jalopy formatting conventions
     */
    private void setConvention(JavaBeautifier beautifier) {
        String fileName = this.conventionFilePath.getAbsoluteFile() +
                c_slash + this.conventionFileName;
        File conventionFile = new File(fileName);

        if (conventionFile.exists()) {
            beautifier.setConventionFilePath(fileName);
        } else {
            beautifier.setConventionFilePath("/default-convention.xml");
        }
    }        
}
