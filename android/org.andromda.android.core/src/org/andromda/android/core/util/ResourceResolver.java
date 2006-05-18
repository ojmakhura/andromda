package org.andromda.android.core.util;

import java.io.File;

import org.apache.tools.ant.DirectoryScanner;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

/**
 * This class helps to find resources and classes.
 * 
 * @author Peter Friese
 * @since 15.05.2006
 */
public class ResourceResolver
{
    /**
     * Hidden c'tor.
     */
    private ResourceResolver() {
        
    }
    
    /**
     * Tries to find a cartridge in the given base directory and all sub directories.
     * 
     * @param baseDir The directory to start the search in.
     * @param name The name of the library.
     * @param version The version of the library
     * @param strict If true, only exact version matches are considered, if false, subversions are also acceptable.
     * @return A filename pointing to the desired library or <code>null</code> if no matching library could be found.
     */
    public static final String findCartridge(final String baseDir,
        final String cartridgeName,
        final String version,
        boolean strict)
    {

        // create cartridge base name
        final StringBuffer basenameBuffer = new StringBuffer();
        basenameBuffer.append("andromda-");
        basenameBuffer.append(cartridgeName);
        basenameBuffer.append("-cartridge-");

        // create search pattern
        final StringBuffer pattern = new StringBuffer();
        pattern.append("**//");
        pattern.append(basenameBuffer);
        pattern.append(version);
        if (!strict)
        {
            pattern.append("*");
        }
        pattern.append(".jar");
        final String[] includes = { pattern.toString() };

        return findResource(baseDir, basenameBuffer.toString(), includes);

    }

    /**
     * Finds the cartridge root folder for the given file. The search will be performed in the current workspace. This
     * method can be used to find the cartridge root folder for a template file inside a cartridge.
     * 
     * @param templateFile The file to lookup the cartridge root for.
     * @return The root container of the cartridge. May either be an {@link IFolder} or an {@link IProject}.
     */
    public static IContainer findCartridgeRoot(final IFile templateFile)
    {
        final IProject project = templateFile.getProject();
        final IPath projectRelativePath = templateFile.getProjectRelativePath();

        final int segments = projectRelativePath.segmentCount();
        int segment = segments;
        boolean found = false;
        while (!found && (segment > 0))
        {
            final String string = projectRelativePath.segment(segment - 1);
            found = string.equalsIgnoreCase("src");
            segment--;
        }

        IPath cartridgeRootPath = projectRelativePath.uptoSegment(segment);
        final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        if (cartridgeRootPath.segmentCount() == 0)
        {
            cartridgeRootPath = project.getLocation();
        }
        return workspaceRoot.getContainerForLocation(cartridgeRootPath);
    }

    /**
     * @param baseName
     * @param files
     */
    private static String findHighestVersion(final String baseName,
        final String[] files)
    {
        int highestIndex = -1;
        String highest = "";
        for (int i = 0; i < files.length; i++)
        {
            final String version = getVersion(baseName, files[i]);
            if (highest.compareTo(version) < 0)
            {
                highestIndex = i;
                highest = version;
            }
        }
        return files[highestIndex];
    }

    /**
     * Tries to find a library in the given base directory and all sub directories.
     * 
     * @param baseDir The directory to start the search in.
     * @param name The name of the library.
     * @param version The version of the library
     * @param strict If true, only exact version matches are considered, if false, subversions are also acceptable.
     * @return A filename pointing to the desired library or <code>null</code> if no matching library could be found.
     */
    public static final String findLibrary(final String baseDir,
        final String name,
        final String version,
        boolean strict)
    {

        // create base name
        final StringBuffer basenameBuffer = new StringBuffer();
        basenameBuffer.append(name);
        basenameBuffer.append("-");

        // create search pattern
        final StringBuffer pattern = new StringBuffer();
        pattern.append("**//");
        pattern.append(basenameBuffer);
        pattern.append(version);
        if (!strict)
        {
            pattern.append("*");
        }
        pattern.append(".jar");
        final String[] includes = { pattern.toString() };

        return findResource(baseDir, basenameBuffer.toString(), includes);
    }

    /**
     * @param baseDir
     * @param name
     * @param includes
     * @return
     */
    private static String findResource(final String baseDir,
        final String baseName,
        final String[] includes)
    {
        // go searching
        final DirectoryScanner ds = new DirectoryScanner();
        ds.setIncludes(includes);
        ds.setBasedir(baseDir);
        ds.setCaseSensitive(false);
        ds.scan();

        // return only first result or fail
        final String[] files = ds.getIncludedFiles();
        if (files.length == 0)
        {
            return null;
        }
        else
        {
            return "file:/" + baseDir + File.separator + findHighestVersion(baseName, files);
        }
    }

    /**
     * @param string
     * @return
     */
    private static String getVersion(final String baseName,
        final String file)
    {
        final File f = new File(file);
        final String n2 = f.getName().replaceFirst(baseName, "");
        final int ext = n2.lastIndexOf(".jar");
        final String n3 = n2.substring(0, ext);
        return n3;
    }

    public static void main(final String[] args)
    {
        final String v1 = "3.2-SNAPSHOT";
        final String v2 = "3.2-RC1-SNAPSHOT";

        System.out.println(v1.compareTo(v2));

        final String libraryPath = findLibrary("D:/develop/m2repo", "andromda-andromdapp-core", "3.2", false);
        System.out.println("Found library: " + libraryPath);

        final String cartridgePath = findCartridge("D:/develop/m2repo", "spring", "3.2", false);
        System.out.println("Found cartridge: " + cartridgePath);
    }

}
