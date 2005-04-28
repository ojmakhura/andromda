package org.andromda.maven.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Recursively lists empty directories from the given directory. A directory is empty when it and it's subdirectories do
 * not contain any files. CVS subdirectories are ignored.
 */
public class EmptyDirsLister
{
    /**
     * A filter only accepting directories.
     */
    private static final FileFilter dirFilter = new DirFilter();

    /**
     * A filter not accepting directories.
     */
    private static final FileFilter noDirFilter = new NoDirFilter();

    /**
     * Lists all the names of the empty directories encountered while recursively going through the subdirectories
     * when using the argument root directory.
     */
    public static List list(String rootDir)
    {
        final File root = new File(rootDir);

        final Set emptyDirs = new HashSet();
        collectEmptyDirs(root, emptyDirs);
        normalize(emptyDirs);

        final List dirNames = new ArrayList();
        for (Iterator iterator = emptyDirs.iterator(); iterator.hasNext();)
        {
            final File file = (File)iterator.next();
            dirNames.add(file.getAbsolutePath());
        }

        Collections.sort(dirNames);

        return dirNames;
    }

    /**
     * Reads the File instances from the argument collection and removes those files which have their parent listed
     * too. Since the argument contains the File instances representing empty directories it is redundant
     * to list all empty child directories since the first empty ancestor is sufficient.
     */
    private static void normalize(Set emptyDirs)
    {
        for (Iterator iterator = emptyDirs.iterator(); iterator.hasNext();)
        {
            final File file = (File)iterator.next();
            if (emptyDirs.contains(file.getParent()))
            {
                iterator.remove();
            }
        }
    }

    /**
     * Recursively stores all empty directories.
     *
     * @param file the root directory which is currently processed, this method does nothing if this is an actual file
     * @param emptyDirs the current set of empty directories to which newly found empty directories are added
     */
    private static void collectEmptyDirs(File file, Set emptyDirs)
    {
        if (file.isDirectory())
        {
            if (isEmptyDir(file))
            {
                emptyDirs.add(file);
            }
            else
            {
                File[] dirs = file.listFiles(dirFilter);
                for (int i = 0; i < dirs.length; i++)
                {
                    File dir = dirs[i];
                    if ("CVS".equals(dir.getName()) == false)
                    {
                        collectEmptyDirs(dir, emptyDirs);
                    }
                }
            }
        }
    }

    /**
     * @return true if the argument is a directory not containing any file or subdirectory.
     */
    private static boolean isEmptyDir(File file)
    {
        boolean emptyDir = true;

        if (file.isDirectory() == false)
        {
            emptyDir = false;
        }
        else if ("CVS".equals(file.getName()) == false)
        {
            File[] files = file.listFiles(noDirFilter);
            if (files.length > 0)
            {
                emptyDir = false;
            }
            else
            {
                File[] dirs = file.listFiles(dirFilter);
                for (int i = 0; i < dirs.length && emptyDir; i++)
                {
                    File dir = dirs[i];
                    emptyDir = isEmptyDir(dir);
                }
            }
        }
        return emptyDir;
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

    /**
     * Helper class not accepting directories when used for filtering.
     */
    private static class NoDirFilter
            implements FileFilter
    {
        /**
         * @return false if the argument file is a directory, true otherwise
         */
        public boolean accept(File file)
        {
            return file.isDirectory() == false;
        }
    }
}