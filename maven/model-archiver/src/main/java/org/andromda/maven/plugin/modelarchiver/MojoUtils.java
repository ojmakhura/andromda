/**
 * 
 */
package org.andromda.maven.plugin.modelarchiver;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Some general helpers for archiver mojos 
 * @author Plushnikov Michail
 */
public class MojoUtils 
{
    /**
     * Deletes all files with given extension in the given directory
     * @param pPath path to directory
     * @param pExtension extension of files
     */
    public static void deleteFiles(String pPath, String pExtension) 
    {
        Iterator<File> lFileIter = FileUtils.iterateFiles(new File(pPath), new String[] {pExtension}, false);
        while(lFileIter.hasNext()) 
        {    
            FileUtils.deleteQuietly(lFileIter.next());
        }
    }

    /**
     * Escapes the pattern so that the reserved regular expression
     * characters are used literally.
     * @param pattern the pattern to replace.
     * @return the resulting pattern.
     */
    public static String escapePattern(String pattern)
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
}
