package org.andromda.maven.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Recursively removes CVS directories and their contents from the given directory.
 */
public class CVSDirRemover
{
    private static final FileFilter dirFilter = new DirFilter();
    private static final FileFilter cvsDirFilter = new CVSDirFilter();

    public static List remove(String rootDir)
    {
        final File file = new File(rootDir);
        List removedDirs = new ArrayList();
        remove(file,removedDirs);
        Collections.sort(removedDirs);
        return removedDirs;
    }

    private static void remove(File file, List removedDirs)
    {
        File[] cvsDirs = file.listFiles(cvsDirFilter);
        for (int i = 0; i < cvsDirs.length; i++)
        {
            File cvsDir = cvsDirs[i];
            File[] files = cvsDir.listFiles();
            for (int j = 0; j < files.length; j++)
            {
                File cvsFile = files[j];
                cvsFile.delete();
            }
            cvsDir.delete();
            removedDirs.add(cvsDir.getAbsolutePath());
        }

        File[] subdirs = file.listFiles(dirFilter);
        for (int i = 0; i < subdirs.length; i++)
        {
            File subdir = subdirs[i];
            remove(subdir, removedDirs);
        }
    }

    private static class CVSDirFilter implements FileFilter
    {
        public boolean accept(File file)
        {
            return file.isDirectory() && "CVS".equals(file.getName());
        }
    }

    private static class DirFilter implements FileFilter
    {
        public boolean accept(File file)
        {
            return file.isDirectory();
        }

    }

}
