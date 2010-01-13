package org.hybridlabs.source.formatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.andromda.utils.beautifier.core.JavaImportBeautifierImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A utility to drive the code beautifier. Note the system properties that point
 * to the various configuration parameters used by the utility.
 * @author Jack Archer, Bob Fields
 * @version 1.0, Feb. 2007
 */
public class FormatManager
{
    private String conventionFilePath = System
            .getProperty("convention.file.path");

    private String conventionFileName = System.getProperty(
            "convention.file.name", "default-convention.xml");

    private final String slash = System.getProperty("file.separator");

    private static final Log LOG = LogFactory.getLog(FormatManager.class);

    /**
     * Run as ant task from BuildCT.xml. Formats imports and Jalopy.
     * If args[1] is empty, source input directory files will be overwritten.
     * System properties control behavior:
     * convention.file.path is the path to the Jalopy convention file (normally org/hybridlabs under ${m2repo}
     * convention.file.name is the name if the Jalopy convention file
     * updateImports true = convert fully qualified classnames to imports
     * updateJalopy true = run Jalopy formatting against output files
     * @param args 1 - root directory of the source to be formatted 2- target
     *        directory to write formatted source to
     */
    public static void main(String[] args)
    {
        try
        {
            // Format imports and Jalopy separately because file will be rolled back if
            // Jalopy parsing fails.
            boolean updateImports = Boolean.parseBoolean(System.getProperty(
                    "updateImports", "true"));

            boolean updateJalopy = Boolean.parseBoolean(System.getProperty(
                    "updateJalopy", "true"));

            LOG.info("Formatting " + args[0] + " updateImports=" + updateImports + " updateJalopy=" + updateJalopy);
            if (args.length > 1)
            {
                if (updateImports)
                {
                    FormatManager.formatImportsAllFiles(args[0], args[1]);
                }
                if (updateJalopy)
                {
                    FormatManager.formatJalopyAllFiles(args[0], args[1]);
                }
            }
            else
            {
                if (updateImports)
                {
                    FormatManager.formatImportsAllFiles(args[0]);
                }
                if (updateJalopy)
                {
                    FormatManager.formatJalopyAllFiles(args[0]);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Formats all Java files discovered in the supplied directory and subdirectories.
     * @param sourceRootDirectory Directory for Java files to format and replace
     * @throws FileNotFoundException If required files or directories do not
     *         exist
     * @throws IOException If the creation of a new file or directory fails
     */
    public static void formatJalopyAllFiles(String sourceRootDirectory)
            throws FileNotFoundException, IOException
    {
        formatAllFiles(sourceRootDirectory, sourceRootDirectory, false, true);
    }

    /**
     * Formats all Java files discovered in the supplied directory and subdirectories.
     * @param sourceRootDirectory Directory for Java files to format and replace
     * @param targetRootDirectory Target Java source output directory (if null, use source directory)
     * @throws FileNotFoundException If required files or directories do not
     *         exist
     * @throws IOException If the creation of a new file or directory fails
     */
    public static void formatImportsAllFiles(String sourceRootDirectory, String targetRootDirectory)
            throws FileNotFoundException, IOException
    {
        formatAllFiles(sourceRootDirectory, targetRootDirectory, true, false);
    }

    /**
     * Formats all Java files discovered in the supplied directory and subdirectories.
     * @param sourceRootDirectory Directory for Java files to format and replace
     * @param targetRootDirectory Target Java source output directory (if null, use source directory)
     * @throws FileNotFoundException If required files or directories do not
     *         exist
     * @throws IOException If the creation of a new file or directory fails
     */
    public static void formatJalopyAllFiles(String sourceRootDirectory, String targetRootDirectory)
            throws FileNotFoundException, IOException
    {
        formatAllFiles(sourceRootDirectory, targetRootDirectory, false, true);
    }

    /**
     * Formats all Java files discovered in the supplied directory and subdirectories.
     * @param sourceRootDirectory Directory for Java files to format and replace
     * @throws FileNotFoundException If required files or directories do not
     *         exist
     * @throws IOException If the creation of a new file or directory fails
     */
    public static void formatImportsAllFiles(String sourceRootDirectory)
            throws FileNotFoundException, IOException
    {
        formatAllFiles(sourceRootDirectory, sourceRootDirectory, true, false);
    }

    /**
     * Formats all Java files discovered in the supplied directory.
     * @param sourceRootDirectory Directory for Java files to format
     * @param targetRootDirectory Output directory for
     * @param imports Change fully qualified classnames to imports in file
     * @param jalopy Run jalopy against file
     * @throws FileNotFoundException If required files or directories do not
     *         exist
     * @throws IOException If the creation of a new file or directory fails
     */
    public static void formatAllFiles(String sourceRootDirectory,
            String targetRootDirectory, boolean imports, boolean jalopy) 
    throws FileNotFoundException, IOException
    {
        String message = "Formatting ";
        if (imports)
        {
            message += "imports";
            if(jalopy)
            {
                message += " and jalopy";
            }
        }
        else if(jalopy)
        {
            message += "jalopy";
        }
        message += ": ";
        LOG.info(message + " under " + targetRootDirectory);

        JavaImportBeautifierImpl beautifier = new JavaImportBeautifierImpl();
        FormatManager manager = new FormatManager();
        manager.setConvention(beautifier);
        beautifier.setOrganizeImports(imports);
        // There are problems with the Jalopy parser for the generated files: Unexpected char '<'
        beautifier.setFormat(jalopy);

        List<File> sourceFiles = manager.getAllJavaFiles(sourceRootDirectory);

        // Now reformat and save the targeted files to the target directory
        int listSize = sourceFiles.size();
        for (int i = 0; i < listSize; i++)
        {
            File sourceFile = sourceFiles.get(i);
            LOG.info(message + sourceFile.getName() + " under " + sourceFile.getParent());

            try 
            {
                /*CharacterSequence sequence = new CharacterSequence(FileHelper
                        .loadStringBuffer(sourceFile));
                String original = sequence.getString();*/
    
                beautifier.beautify(sourceFile, sourceFile);
                // Only overwrite the file if the beautifier changes it, or if a
                // different directory
                // Sometimes Jalopy throws an error and returns a zero length file.
                /*if (sequence.length() > 1
                        && (!original.equals(sequence.getString())))
                {
                    File targetFile = new File((manager.getTargetDirectory(
                            sourceFile, sourceRootDirectory, targetRootDirectory)),
                            sourceFile.getName());
                    // File targetDirectory = new File((manager.getTargetDirectory(
                    // sourceFile, targetRootDirectory)), sourceFile.getName());
                    FileOutputStream outputSource = new FileOutputStream(targetFile);
                    outputSource.write(sequence.getString().getBytes());
                    outputSource.flush();
                    outputSource.close();
                }*/
            } 
            catch (RuntimeException e) {
                LOG.error("Exception formatting file: " + sourceFile.getName(), e);
//                System.out.println("Exception formatting file: " + sourceFile.getName());
//                e.printStackTrace();
            }
        }
        LOG.info("Finished Formatting all files under " + sourceRootDirectory);
    }

    /**
     * Formats a single Java file.
     * @param sourceFile Java file to format
     * @param targetRootDirectory Output directory for
     * @param imports Change fully qualified classnames to imports in file
     * @param jalopy Run jalopy against file
     * @throws FileNotFoundException If required files or directories do not
     *         exist
     * @throws IOException If the creation of a new file or directory fails
     */
    public static void formatFile(File sourceFile, 
            String targetRootDirectory, boolean imports, boolean jalopy) throws FileNotFoundException,
            IOException
    {
        JavaImportBeautifierImpl beautifier = new JavaImportBeautifierImpl();
        FormatManager manager = new FormatManager();
        manager.setConvention(beautifier);
        beautifier.setOrganizeImports(imports);
        // There are problems with the Jalopy parser for the generated files: Unexpected char '<'
        beautifier.setFormat(jalopy);

        LOG.info("Formatting file: " + sourceFile.getName());

        /*CharacterSequence sequence = new CharacterSequence(FileHelper
                .loadStringBuffer(sourceFile));
        //String original = sequence.getString();*/
        try 
        {
            beautifier.beautify(sourceFile, sourceFile);
            // Only overwrite the file if the beautifier changes it, or if a
            // different directory
            // Sometimes Jalopy throws an error and returns a zero length file.
            /*if (sequence.length() > 1)
            {
                File targetFile=null;
                if (targetRootDirectory==null || "".equals(targetRootDirectory))
                {
                    targetFile = sourceFile;
                }
                else
                {
                    targetFile = new File(targetRootDirectory, sourceFile.getName());
                    targetFile = new File((manager.getTargetDirectory(
                            sourceFile, sourceFile.getParent(), targetRootDirectory)),
                            sourceFile.getName());
                }
                // File targetDirectory = new File((manager.getTargetDirectory(
                // sourceFile, targetRootDirectory)), sourceFile.getName());
                FileOutputStream outputSource = new FileOutputStream(targetFile);
                outputSource.write(sequence.getString().getBytes());
                outputSource.flush();
                outputSource.close();
            }*/
        } 
        catch (RuntimeException e) {
            LOG.error("Exception formatting file: " + sourceFile.getName(), e);
//                System.out.println("Exception formatting file: " + sourceFile.getName());
//                e.printStackTrace();
        }
        LOG.info("Finished Formatting all files");
    }

    /**
     * Locates and sets Jalopy formatting convention file
     * @param beautifier Instance of the beautifier class, not yet set to with
     *        external Jalopy formatting conventions
     * @throws FileNotFoundException If the convention file cannot be located
     */
    private void setConvention(JavaImportBeautifierImpl beautifier)
        //    throws FileNotFoundException
    {
/*        if (this.conventionFilePath==null)
        {
            //throw new FileNotFoundException("System Property convention.file.path not set");
        }
        if (this.conventionFileName==null)
        {
            throw new FileNotFoundException("System Property convention.file.name not set");
        }
*/
        String fileName = this.conventionFilePath + this.slash
                + this.conventionFileName;
        File conventionFile = new File(fileName);

        if (conventionFile.exists())
        {
            beautifier.setConventionFilePath(fileName);
        }
        else
        {
            this.conventionFilePath="/default-convention.xml";
            beautifier.setConventionFilePath("/default-convention.xml");
            //StringBuffer msg = new StringBuffer(300);
            //msg.append("Jalopy convention file ").append(fileName);
            //msg.append(" does not exist. Failed to set beautifier parameters.");
            //throw new FileNotFoundException(msg.toString());
        }
    }

    /**
     * Fetches a target directory, creating it if it doesn't exist
     * @param sourceFile Filename to create
     * @param sourceRootDirectory sourceFile directory 
     * @param targetRootDirectory target directory root to create and return
     * @return the target output directory for the specified re-formatted source
     *         File
     */
    private File getTargetDirectory(File sourceFile,
            String sourceRootDirectory, String targetRootDirectory)
    {
        String path = sourceFile.getParent();
        // Find the part of the source file below the source directory, append
        // to target directory
        int index = path.indexOf(sourceRootDirectory);
        if (index == -1)
        {
            if (sourceRootDirectory.indexOf("/") > -1)
            {
                // Pathname translation - replace doesn't work with //
                sourceRootDirectory = replace(sourceRootDirectory, "/", "\\");
                index = path.indexOf(sourceRootDirectory);
            }
            else
            {
                sourceRootDirectory = replace(sourceRootDirectory, "\\", "/");
                index = path.indexOf(sourceRootDirectory);
            }
        }
        String sourcePath = path
                .substring(index + sourceRootDirectory.length());
        if (targetRootDirectory==null || targetRootDirectory.equals(""))
        {
            targetRootDirectory = sourceRootDirectory;
        }
        File targetPath = new File(targetRootDirectory + sourcePath);
        targetPath.mkdirs();

        return targetPath;
    }

    /**
     * The String / stringBuffer replace command does not work with // !
     * @param target
     * @param toReplace
     * @param replaceBy
     * @return String with replaced tokens
     */
    public static String replace(String target, String toReplace,
            String replaceBy)
    {
        // StringBuffer result = new StringBuffer(target);
        int i = 0;
        // int j = 0;
        int toRepLength = toReplace.length();
        // int lengthDecrease = toRepLength - replaceBy.length();
        while ((i = target.indexOf(toReplace, i)) != -1)
        {
            target = target.substring(0, i)
                    + replaceBy
                    + new StringBuffer(target.substring(i + toRepLength, target
                            .length()));
            // result.replace(i - j, toRepLength, replaceBy);
            // i += toRepLength;
            // j += lengthDecrease;
        }
        return target;
    }

    /**
     * Traverses to the end of a directory tree, starting at indicated source
     * directory, and collects all found Java source files.
     * @param rootDirName The full path name of root of the directory tree to
     *        search for Java source files
     * @return List of Java source file discovered in the indicated directory
     *         tree, returned as Files
     * @throws FileNotFoundException if the root directory does not exist
     */
    private List<File> getAllJavaFiles(String rootDirName)
            throws FileNotFoundException
    {
        List<File> javaFiles = new ArrayList<File>();
        List<File> sourceDirs = new ArrayList<File>();
        File rootDir = asDirectory(rootDirName);

        // Traverse the directory tree from the starting source directory,
        // collect all the subdirectories
        List<File> subDirs = new ArrayList<File>();
        subDirs.add(rootDir);
        int listSize = 0;
        while (subDirs.size() > 0)
        {
            List<File> tempDirs = new ArrayList<File>();
            listSize = subDirs.size();
            for (int i = 0; i < listSize; i++)
            {
                File subDir = subDirs.get(i);
                sourceDirs.add(subDir);
                tempDirs.addAll(getDirectories(subDir));
            }

            subDirs = tempDirs;
        }
        listSize = sourceDirs.size();
        for (int i = 0; i < listSize; i++)
        {
            File sourceDir = sourceDirs.get(i);
            javaFiles.addAll(getJavaFiles(sourceDir));
        }

        return javaFiles;
    }

    /**
     * Collects the Java files in the indicated directory
     * @param dir The directory to search for Java source files
     * @return List of discovered Java source files as 'File' instances
     */
    private List<File> getJavaFiles(File dir)
    {
        List<File> files = new ArrayList<File>();

        // Capture the Java source in the source directory
        JavaSourceFilter javaFilter = new JavaSourceFilter();

        String[] fileNames = dir.list(javaFilter);

        int listSize = fileNames.length;

        for (int i = 0; i < listSize; i++)
        {
            String fileName = fileNames[i];

            File file = new File(dir.getAbsolutePath(), fileName);
            // Directories might end in .java also
            if (file.isFile())
            {
                files.add(file);
            }
        }

        return files;
    }

    /**
     * Collects the subdirectories for the indicated directory
     * @param dir The directory to find subdirectories for
     * @return List of discovered subdirectories as 'File' instances
     */
    private List<File> getDirectories(File dir)
    {
        List<File> dirs = new ArrayList<File>();

        // Capture the subdirectories the source directory
        DirectoryFilter dirFilter = new DirectoryFilter();

        String[] dirNameArray = dir.list(dirFilter);

        int listSize = dirNameArray.length;

        for (int i = 0; i < listSize; i++)
        {
            String dirName = dirNameArray[i];
            File subDir = new File(dir.getAbsolutePath(), dirName);
            dirs.add(subDir);
        }

        return dirs;
    }

    /**
     * Verifies the incoming path name exists and is a directory; if not throws
     * exception
     * @param dirPath Path to check
     * @return The directory specified in the incoming text as a File instance
     * @throws FileNotFoundException if the incoming path name does not exist or
     *         is not a directory
     */
    private File asDirectory(String dirPath) throws FileNotFoundException
    {
        File dir = new File(dirPath);

        if (!dir.isDirectory())
        {
            StringBuffer msg = new StringBuffer(300);
            msg.append("Path ");
            msg.append(dirPath);
            msg.append(" does not exist or is not a directory.");
            throw new FileNotFoundException(msg.toString());
        }

        return new File(dirPath);
    }

    /**
     * This Filter identifies Java source code files
     */
    class JavaSourceFilter implements FilenameFilter
    {
        /**
         * Checks for files ending with '.java', identifying them as Java source
         * @param dir the path name of the file to be filtered
         * @param name the name of the file (or last element of a directory
         *        path) of the file to be filtered
         * @return True If the file is a Java source file (has '.java'
         *         extension)
         */
        public boolean accept(File dir, String name)
        {
            return name.endsWith(".java");
        }
    }

    /**
     * This Filter identifies the directories with a list of files
     */
    class DirectoryFilter implements FilenameFilter
    {
        /**
         * Filters incoming file to see if it's a directory
         * @param dir Absolute path of the file being filtered
         * @param name he name of the file (or last element of a directory path)
         *        of the file to be filtered
         * @return True is a File is a directory
         */
        public boolean accept(File dir, String name)
        {
            File temp = new File(dir.getAbsoluteFile(), name);

            return temp.isDirectory();
        }
    }
}
