package org.hybridlabs.beautifier.plugin;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.hybridlabs.beautifier.core.JavaImportBeautifierImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class JavaImportBeautifierAntTask extends Task {

    private FileSet fileSet;
    private String conventionFilePath;

    private boolean organizeImport;
    private boolean format;

    private boolean debugMode;

    public void execute() throws BuildException {
        super.execute();

        JavaImportBeautifierImpl beautifier = new JavaImportBeautifierImpl();
        beautifier.setConventionFilePath(conventionFilePath);
        beautifier.setFormat(format);
        beautifier.setOrganizeImports(organizeImport);
        
        // find all .java files in the specified path
        final String[] fileList = fileSet.getDirectoryScanner(getProject()).getIncludedFiles();

        for (String fileName : fileList) {
            File file = new File(fileSet.getDir(getProject()).getAbsolutePath() + '/' + fileName);
            try {
                beautifier.beautify(file, debugMode ? new File(file.getAbsoluteFile() + "_debug") : null);
            } catch (FileNotFoundException e) {
                throw new BuildException(e.getMessage(), e);
            } catch (IOException e) {
                throw new BuildException(e.getMessage(), e);
            }
        }
    }

    public String getConventionFilePath() {
        return conventionFilePath;
    }

    public void setConventionFilePath(String configuration) {
        this.conventionFilePath = configuration;
    }

    public FileSet getFileSet() {
        return fileSet;
    }

    public void addFileset(FileSet fileSet) {
        this.fileSet = fileSet;
    }

    public boolean isFormat() {
        return format;
    }

    public void setFormat(boolean format) {
        this.format = format;
    }

    public boolean isOrganizeImport() {
        return organizeImport;
    }

    public void setOrganizeImport(boolean organizeImport) {
        this.organizeImport = organizeImport;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }


}
