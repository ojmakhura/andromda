package org.andromda.maven.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Recursively removes CVS directories and their contents from the given directory.
 */
public class CVSDirRemover
{
    /**
     * A filter only accepting directories.
     */
    private static final FileFilter dirFilter = new DirFilter();

    /**
     * A filter only accepting CVS directories.
     */
    private static final FileFilter cvsDirFilter = new CVSDirFilter();

    /**
     * Recursively removes all CVS directories from the specified root directory and returns the names of all
     * the directories that have been removed.
     */
    public static List remove(String rootDir)
    {
        final File file = new File(rootDir);
        final List removedDirs = new ArrayList();
        remove(file, removedDirs);
        Collections.sort(removedDirs);
        return removedDirs;
    }

    /**
     * Recursively removes all CVS directories from the specified root directory and collects the names of all
     * the directories that have been removed.
     *
     * @param file the directory on which to operate
     * @param removedDirs the names of the directories that have been removed upto this point
     */
    private static void remove(File file, List removedDirs)
    {
        final File[] cvsDirs = file.listFiles(cvsDirFilter);
        for (int i = 0; i < cvsDirs.length; i++)
        {
            final File cvsDir = cvsDirs[i];
            final File[] files = cvsDir.listFiles();
            for (int j = 0; j < files.length; j++)
            {
                final File cvsFile = files[j];
                cvsFile.delete();
            }
            cvsDir.delete();
            removedDirs.add(cvsDir.getAbsolutePath());
        }

        final File[] subdirs = file.listFiles(dirFilter);
        for (int i = 0; i < subdirs.length; i++)
        {
            final File subdir = subdirs[i];
            remove(subdir, removedDirs);
        }
    }

    /**
     * Helper class only accepting CVS directories when used for filtering.
     */
    private static class CVSDirFilter
            extends DirFilter
    {
        /**
         * @return true if the argument file is a directory called CVS, false otherwise
         */
        public boolean accept(File file)
        {
            return "CVS".equals(file.getName()) && super.accept(file);
        }
    }

    /**
     * Helper class only accepting directories when used for filtering.
     */
    private static class DirFilter
            implements FileFilter
    {
        /**
         * @return true if the argument file is a directory, false otherwise
         */
        public boolean accept(File file)
        {
            return file.isDirectory();
        }

    }

}
