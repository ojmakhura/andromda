/**
 * From Apache Uima migration utils file at
 * http://svn.apache.org/repos/asf/incubator/uima/uimaj/trunk/uimaj-tools/src/main/java/org/apache/uima
 * /tools/migration/IbmUimaToApacheUima.java
 */
package org.andromda.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang.StringUtils;

/**
 * Iterate down through a directory hierarchy, replace @andromda.whatever.whatever with the Java compliant andromda_whatever_whatever tagged
 * value (attribute) format. Open .zip files if necessary.
 * 
 * @author RJF3
 */
public class GlobalPatternFileReplace
{
    private static List<Replacement> replacements = new ArrayList<Replacement>();
    private static int MAX_FILE_SIZE = 10000000; // don't update files bigger than 10 MB
    private static Set<String> extensions = new HashSet<String>();
    private static int filesScanned = 0;
    private static int filesModified = 0;
    private static int fileReplacements = 0;

    /**
     * 
     */
    public GlobalPatternFileReplace()
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException
    {
        File file = new File("C:/workspaces/A34/andromda341/cartridges/andromda-spring/pom.xml");
        // parse command line
        String dir = null;
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].startsWith("-"))
            {
                if (args[i].equals("-ext"))
                {
                    if (i + 1 >= args.length)
                    {
                        printUsageAndExit();
                    }
                    parseCommaSeparatedList(args[++i], extensions);
                }
                else
                {
                    System.err.println("Unknown switch " + args[i]);
                    printUsageAndExit();
                }
            }
            else
            {
                if (dir != null)
                {
                    printUsageAndExit();
                }
                else
                {
                    dir = args[i];
                    file = new File(dir);
                }
            }
        }
        /*if (dir == null)
        {
            //file = new File(".");
            //System.out.println("Using default current directory: " + file.getCanonicalPath());
            file = new File("C:/workspaces/A34/andromda341/cartridges/andromda-spring");
            //printUsageAndExit();
        }
        if (file == null)
        {
            printUsageAndExit();
            return;
        }*/
        if (extensions.isEmpty())
        {
            parseCommaSeparatedList("xml,java,xml.zip,vsl,vm", extensions);
        }

        try
        {
            /*
            replaceInFile(file); //read resource files //map from IBM UIMA package names to Apache UIMA package names
            */
            readMapping("packageMapping.txt", replacements, true);
            //other string replacements readMapping("stringReplacements.txt", replacements, false);

            // from system property, get list of file extensions to exclude
            // do the replacements
            System.out.println("Migrating your files in " + file.getCanonicalPath());
            replaceInAllFiles(file);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Migration complete.");
        System.out.println("Scanned " + filesScanned + " files.  " + filesModified + " files modified.");
    }

    /**
     * 
     */
    private static void printUsageAndExit()
    {
        System.err.println("Usage: java " + GlobalPatternFileReplace.class.getName() + " <directory> [-ext <fileExtensions>]");
        System.err.println("<fileExtensions> is a comma separated list of file extensions to process, e.g.: java,xml,properties");
        System.err.println("\tUse a trailing comma to include files with no extension (meaning their name contains no dot)");
        System.exit(1);
    }

    /**
     * Applies the necessary replacements to all files in the given directory. Subdirectories are processed recursively.
     * Filters out SVN files and target directory by default.
     * 
     * @param dir directory containing files to replace
     * @throws IOException if an I/O error occurs
     */
    private static void replaceInAllFiles(File dir) throws IOException
    {
        if (dir.isDirectory())
        {
            IOFileFilter fileFilter = FileFilterUtils.trueFileFilter();
            IOFileFilter notTargetFileFilter = FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter("target"));
            IOFileFilter dirFilter = FileFilterUtils.and(FileFilterUtils.makeSVNAware(null), notTargetFileFilter);
            Collection<File> files = FileUtils.listFiles(dir, fileFilter, dirFilter);
            for (File file : files)
            {
                if (file.isFile())
                {
                    boolean process = false;
                    // Check files against extensions filter
                    for (String ext : extensions)
                    {
                        if (file.getName().endsWith("." + ext))
                        {
                            process = true;
                            break;
                        }
                    }
                    if (process)
                    {
                        // do the replacements
                        // for .zip files, unzip, replace, and put back in archive
                        if (file.getName().endsWith(".zip"))
                        {
                            replaceInArchive(file);
                        }
                        else
                        {
                            replaceInFile(file);
                        }
                    }
                }
    
                // recursively call on subdirectories
                else if (file.isDirectory())
                {
                    replaceInAllFiles(file);
                }
            }
        }
        if (dir.isFile())
        {
            if (dir.getName().endsWith(".zip"))
            {
                replaceInArchive(dir);
            }
            else
            {
                replaceInFile(dir);
            }
        }
    }

    /**
     * Checks if file exists and is not excluded by extension and is writable.
     * 
     * @param file the file to process
     */
    private static boolean checkAttributes(File file) throws IOException
    {
        // skip files with extensions specified in the excludes list
        if (!extensions.isEmpty())
        {
            String filename = file.getName();
            String ext = "";
            int lastDot = filename.lastIndexOf('.');
            if (lastDot > -1)
            {
                ext = filename.substring(lastDot + 1);
            }
            if (!extensions.contains(ext.toLowerCase()))
            {
                return false;
            }
        }

        // make writable files that are readonly
        if (!file.canRead())
        {
            //new FilePermission(file.getCanonicalPath(), "read,write,execute,delete");
            //file.setReadable(true, false);
            System.err.println("Can't read file: " + file.getCanonicalPath());
            //continue;
        }
        /*if (!file.canWrite())
        {
            System.err.println("Skipping unwritable file: " + file.getCanonicalPath());
            return false;
        }*/
        // skip files that are too big
        if (file.length() > MAX_FILE_SIZE)
        {
            System.out.println("Skipping file " + file.getCanonicalPath() + " with size: " + file.length() + " bytes");
            return false;
        }

        return true;
    }

    /**
     * Applies replacements to a single file.
     * 
     * @param file the file to process
     */
    private static int replaceInArchive(File file) throws IOException
    {
        // do the replacements in each file in the archive
        
        int replacements = 0;
        // for .zip files, unzip, replace, and put back in archive
        if (file.isFile() && file.getName().endsWith(".zip"))
        {
            File location = unpack(file);
            IOFileFilter fileFilter = FileFilterUtils.suffixFileFilter("xml");
            IOFileFilter dirFilter = FileFilterUtils.makeCVSAware(null);
            Collection<File> archiveFiles = FileUtils.listFiles(location, fileFilter, dirFilter);
            for (File archiveFile : archiveFiles)
            {
                // Only update zip archive contents if they have changed
                int replacement = replaceInFile(archiveFile);
                if (replacement > 0)
                {
                    pack(file, archiveFile.getAbsolutePath());
                    replacements += replacement;
                }
                archiveFile.delete();
            }
            location.delete();
        }
        return replacements;
    }

    /**
     * Applies replacements to a single file.
     * 
     * @param file the file to process
     */
    private static int replaceInFile(File file) throws IOException
    {
        // do the replacements
        fileReplacements = 0;
        if (!checkAttributes(file))
        {
            return fileReplacements;
        }
        String original;
        try
        {
            original = FileUtils.readFileToString(file);
        }
        catch (IOException e)
        {
            System.err.println("Error reading " + file.getCanonicalPath());
            System.err.println(e.getMessage());
            return fileReplacements;
        }
        String contents = original;
        contents = replaceInPattern(contents);

        // write file if it changed
        if (!contents.equals(original))
        {
            if (!file.canWrite())
            {
                // JDK 1.6 only. How do we make file writable in JDK 1.5?
                file.setReadable(true, false);
                System.out.println("Making file writable: " + file.getCanonicalPath());
                //new FilePermission(file.getCanonicalPath(), "read,write,execute,delete");               
            }
            if (!file.canWrite())
            {
                System.err.println("Could not make file writable: " + file.getCanonicalPath());
            }
            else
            {
                FileUtils.writeStringToFile(file, contents);
                filesModified++;
            }
            System.out.println(file.getAbsolutePath() + " " + fileReplacements + " replacements");
        }
        filesScanned++;

        // check for situations that may need manual attention,
        // updates filesNeedingManualAttention field
        // checkForManualAttentionNeeded(file, original);
        return fileReplacements;
    }

    /**
     * Unpacks the archive file.
     *
     * @param file File to be unpacked.
     * @return File directory where contents were unpacked
     * @throws IOException 
     */
    protected static File unpack(
        final File file)
        throws IOException
    {
        return unpack(file, null);
    }

    /**
     * Unpacks the archive file.
     *
     * @param file File to be unpacked.
     * @param location Location where to put the unpacked files.
     * @return File directory where contents were unpacked
     * @throws IOException 
     */
    protected static File unpack(
        final File file,
        File location)
        throws IOException
    {
        final int ext = file.getAbsolutePath().lastIndexOf('.');
        if (ext<1)
        {
            throw new IOException("File has no extension: " + file.getAbsolutePath());
        }
            
        if (location==null || !location.exists())
        {
            location = new File(file.getParent() + "/" + StringUtils.remove(file.getName(), '.') + "/");
        }
        if (!location.getAbsolutePath().endsWith("/") && !location.getAbsolutePath().endsWith("\\"))
        {
            location = new File(location.getAbsolutePath() + "/");
        }
        if (!location.exists())
        {
            // Create the directory
            FileUtils.forceMkdir(location);
        }
        //final String archiveExt = file.getAbsolutePath().substring(ext+1);
        //final String archiveExt = FileUtils.getExtension(file.getAbsolutePath()).toLowerCase();
        try
        {
            /*ZipFile zipFile = new ZipFile(file);
            for (File fileEntry : zipFile.entries())
            final UnArchiver unArchiver = GlobalPatternFileReplace.archiverManager.getUnArchiver(archiveExt);
            unArchiver.setSourceFile(file);
            unArchiver.setDestDirectory(location);
            unArchiver.extract();
            }
        catch (Throwable throwable)
        {
            if (throwable instanceof IOException || throwable instanceof ArchiverException)
            {
                throw new IOException("Error unpacking file: " + file + "to: " + location, throwable);
            //}
        }*/
            byte[] buf = new byte[1024];
            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(new FileInputStream(file));
    
            zipentry = zipinputstream.getNextEntry();
            while (zipentry != null) 
            { 
                //for each entry to be extracted
                String entryName = zipentry.getName();
                //System.out.println("entryname "+entryName);
                FileOutputStream fileoutputstream;
                File newFile = new File(entryName);
                String directory = newFile.getParent();
                
                if(directory == null)
                {
                    if(newFile.isDirectory())
                        break;
                }
                
                File output = new File(location.getAbsolutePath(), entryName);
                fileoutputstream = new FileOutputStream(output);             
    
                int n;
                while ((n = zipinputstream.read(buf, 0, 1024)) > -1)
                    fileoutputstream.write(buf, 0, n);
    
                fileoutputstream.close(); 
                zipinputstream.closeEntry();
                zipentry = zipinputstream.getNextEntry();
    
            }//while
    
            zipinputstream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return location;
    }

    /**
     * Writes the given archive file and includes
     * the file given by the <code>path</code>
     *
     * @param zipArchive the zip archive.
     * @param path the path of the file to write to the zip archive.
     * @throws IOException
     */
    private static void pack(
        final File zipArchive,
        final String path)
        throws IOException
    {
        // - retrieve the name of the file given by the path.
        /*final String name = path.replaceAll(
                PATH_REMOVE_PATTERN,
                "");*/
        //final String name = zipArchive.getName();
        final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipArchive));
        File pathFile = new File(path);
        final ZipEntry zipEntry = new ZipEntry(pathFile.getName());
        zipEntry.setMethod(ZipEntry.DEFLATED);
        zipOutputStream.putNextEntry(zipEntry);
        final FileInputStream inputStream = new FileInputStream(path);
        final byte[] buffer = new byte[1024];
        int n = 0;
        while ((n =
                inputStream.read(
                    buffer,
                    0,
                    buffer.length)) > 0)
        {
            zipOutputStream.write(
                buffer,
                0,
                n);
        }
        inputStream.close();
        zipOutputStream.closeEntry();
        zipOutputStream.close();
    }

    /**
     * Replaces values within a regex match for each replacement in replacements list
     * 
     * @param contents File contents string
     * @param replacement The pattern, replace, replaceWith, and deleteStr values
     */
    private static String replaceInPattern(String contents)
    {
        // apply replacements
        for (Replacement replacement : replacements)
        {
            // contents = contents.replaceAll(replacement.regex, replacement.replacementStr);
            contents = replaceInPattern(contents, replacement);
        }
        return contents;
    }

    /**
     * Replaces values within a regex match
     * 
     * @param contents File contents string
     * @param replacement The pattern, replace, replaceWith, and deleteStr values
     */
    private static String replaceInPattern(String contents, Replacement replacement)
    {
        // StringBuffer contentBuffer = new StringBuffer(contents);
        Matcher matcher = replacement.pattern.matcher(contents);
        StringBuffer sb = new StringBuffer();
        while (matcher.find())
        {
            boolean ignoreString = false;
            String toReplace = contents.substring(matcher.start(), matcher.end());
            for (String ignorePattern : replacement.ignorePatterns)
            {
                if (toReplace.indexOf(ignorePattern) > -1)
                {
                    ignoreString = true;
                    break;
                }
            }
            if (!ignoreString)
            {
                toReplace = toReplace.replace(replacement.replaceStr, replacement.replaceWithStr);
                toReplace = StringUtils.remove(toReplace, replacement.deleteStr);
                matcher.appendReplacement(sb, toReplace);
                fileReplacements++;
            }
        }
        sb = matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Parses a comma separated list, entering each value into the results Collection. Trailing empty strings are included in the results
     * Collection.
     * 
     * @param string string to parse
     * @param results Collection to which each value will be added
     */
    private static Set<String> parseCommaSeparatedList(String string, Set<String> results)
    {
        String[] components = string.split(",", -1);
        if (results==null)
        {
            results = new HashSet<String>();
        }
        for (int i = 0; i < components.length; i++)
        {
            results.add(components[i]);
        }
        return results;
    }

    /**
     * Reads a mapping from a resource file, and populates a List of Replacement objects. We don't use a Map because the order in which the
     * replacements are applied can be important.
     * 
     * @param fileName
     *            name of file to read from (looked up looking using Class.getResource())
     * @param mappings
     *            List to which Replacement objects will be added. Each object contains the regex to search for and the replacement string.
     * @param treatAsPackageNames
     *            if true, the keys in the resource file will be considered package names, and this routine will produce regexes that
     *            replace any fully-qualified class name belonging to that package. Also in this case updates the static ibmPackageNames
     *            HashSet.
     */
    private static void readMapping(String fileName, List mappings, boolean treatAsPackageNames) throws IOException
    {
        // Placeholder until txt file is built/read
        //List<String> ignorePatterns = new ArrayList<String>();
        //ignorePatterns.add(".cvs.");
        // Find pattern @andromda. followed by lowercase letters followed by period (repeating groups) followed by lowercase letter
        Replacement replacement = new Replacement("@andromda.([a-z]+[\\\\.])+[a-z]", ".", "_", "@", parseCommaSeparatedList(".cvs.", null));
        GlobalPatternFileReplace.replacements.add(replacement);

        /*URL pkgListFile = AtAndromdaCleanup.class.getResource(fileName);
        InputStream inStream = pkgListFile.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        String line = reader.readLine();
        while (line != null)
        {
            String[] mapping = line.split(" ");
            String regex, replaceStr;
            if (treatAsPackageNames)
            {
                // we do special processing for package names to try to handle the case where
                // user code exists in a package prefixed by com.ibm.uima.
                // We only replace the package name when it appears as part of a fully-qualified
                // class name in that package, not as a prefix of another package.

                // turn package name into a regex (have to escape the . and,
                // technically, should allow whitepsace around dots)
                String pkgRegex = mapping[0].replaceAll("\\.", "\\\\s*\\\\.\\\\s*");
                // form regex that will find any fully-qualified class name in this package
                regex = pkgRegex + "(\\.(\\*|[A-Z]\\w*))";
                replaceStr = mapping[1] + "$1";
                // ibmPackageNames.add(mapping[0]);
            }
            else
            {
                // form regex from src, by escaping dots and allowing whitespace
                regex = mapping[0].replaceAll("\\.", "\\\\s*\\\\.\\\\s*");
                replaceStr = mapping[1];
            }

            // Regex for @andromda.([a-z]+[\.])+ matches @andromda. plus repeating sequence of lowercase letters followed by period
            // replace . with _ in all occurrences within the matched pattern result String, remove @
            List<String> ignorePatterns = new ArrayList<String>();
            ignorePatterns.add(".cvs.");
            Replacement replacement = new Replacement("@andromda.([a-z]+[\\\\.])+", ".", "_", "@", ignorePatterns);
            // Replacement replacement = new Replacement(regex, replaceStr);
            mappings.add(replacement);
            line = reader.readLine();
        }
        inStream.close();*/
    }

    /**
     * Replace all occurrences of replacement String with replaceWithString inside regex result Delete all occurrences of deleteStr inside
     * regex result
     * 
     * @author RJF3
     */
    private static class Replacement
    {
        String regex;
        String deleteStr;
        String replaceStr;
        String replaceWithStr;
        Set<String> ignorePatterns;
        Pattern pattern;

        /**
         * @param regexIn Input regular expression
         * @param replacement String to replace text within the existing string
         * @param replaceWithIn String to replace with
         * @param deleteStrIn String to delete within the existing String
         * @param ignorePatternsIn If one of the patterns exists in the existing String, skip replacements
         */
        Replacement(String regexIn, String replacement, String replaceWithIn, String deleteStrIn, Set<String> ignorePatternsIn)
        {
            this.regex = regexIn;
            this.replaceStr = replacement;
            this.deleteStr = deleteStrIn;
            this.replaceWithStr = replaceWithIn;
            this.ignorePatterns = ignorePatternsIn;
            this.pattern = Pattern.compile(regex);
        }
    }
}
