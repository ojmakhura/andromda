package org.andromda.cartridges.mgmt;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.andromda.cartridges.interfaces.CartridgeXmlParser;
import org.andromda.cartridges.interfaces.DefaultAndroMDACartridge;
import org.andromda.cartridges.interfaces.IAndroMDACartridge;
import org.andromda.cartridges.interfaces.ICartridgeDescriptor;
import org.apache.tools.ant.AntClassLoader;

/**
 * Finds AndroMDA cartridges on the classpath.
 *
 * @author    <a href="mailto:aslak.hellesoy@netcom.no">Aslak Hellesøy</a>
 * @author    <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @since   April 1, 2003
 * @todo      Use reflection to call AntClassLoader.getClasspath to remove dependency to Ant. This package should be Ant
 *      independent
 * @version   $Revision: 1.2 $
 */
public class CartridgeFinder
{
    private final static String resourceName =
        "META-INF/andromda-cartridge.xml";

    private final static FileFilter jarFilter = new FileFilter()
    {
        public boolean accept(File file)
        {
            return file.getName().endsWith(".jar");
        }
    };
    private static String classpath;
    private static List cartridges = null;

    public static String getClasspath()
    {
        return classpath;
    }

    /**
     * Initialises the classpath. We'll try to cast the clazz' class loader to
     * an AntClassLoader and get classpath from there. If that fails (happens if
     * the clazz was loaded by the system class loader), we'll use java.class.path.
     *
     * @param clazz  the class used to find classpath.
     */
    public static void initClasspath(Class clazz)
    {
        try
        {
            classpath =
                ((AntClassLoader) clazz.getClassLoader()).getClasspath();
        }
        catch (ClassCastException e)
        {
            classpath = System.getProperty("java.class.path");
        }
    }

    /**
     * Returns a List of ICartridgeDescriptor objects
     *
     * @return a <code>List<code> of cartriges
     */
    public static List findCartridges() throws IOException
    {
        if (cartridges == null)
        {
            cartridges = new ArrayList();

            CartridgeXmlParser parser = new CartridgeXmlParser();

            List cartridgeFiles = findCartridgeFiles();
            Iterator cartridgeFileIterator = cartridgeFiles.iterator();

            while (cartridgeFileIterator.hasNext())
            {
                File file = (File) cartridgeFileIterator.next();

                if (file.exists())
                {
                    // System.out.println("##1 searching " + file.toString());
                    
                    InputStream androXmlIs = null;
                    URL definitionURL;

                    if (file.isDirectory())
                    {
                        File androXml = new File(file, resourceName);
                        // System.out.println("Trying to open " + androXml.toString());
                        androXmlIs = new FileInputStream(androXml);
                        // System.out.println("##2 Opened !!!");
                        definitionURL = androXml.toURL();
                    }
                    else
                    {
                        // System.out.println("Trying to open JAR " + file.toString());
                        JarFile jar = new JarFile(file);
                        // System.out.println("##3 JAR opened");
                        JarEntry androXml = jar.getJarEntry(resourceName);

                        if (androXml != null)
                        {
                            androXmlIs = jar.getInputStream(androXml);
                        }

                        definitionURL =
                            new URL(
                                new URL("jar:" + file.toURL() + "!/"),
                                resourceName);
                    }

                    if (androXmlIs != null)
                    {
                        ICartridgeDescriptor cDescriptor =
                            parser.parse(androXmlIs);

                        if (cDescriptor != null)
                        {
                            cDescriptor.setDefinitionURL(definitionURL);
                            IAndroMDACartridge cartridge =
                                instantiateCartridge(cDescriptor);
                            cartridges.add(cartridge);
                        }
                        else
                        {
                            System.err.println("CartridgeFinder: Could not parse XML descriptor of " + androXmlIs.toString());
                            // @todo logging!
                        }
                    }
                }
                else
                {
                    System.err.println("File does not exist: " + file.toString());
                    // @todo logging!
                }
            }
        }

        // some debugging output
        for (Iterator iter = cartridges.iterator(); iter.hasNext();)
        {
            IAndroMDACartridge element =
                (IAndroMDACartridge) iter.next();
            System.out.println(
                "CartridgeFinder: Cartridge found: " + element.getDescriptor().getCartridgeName());

        }
        return cartridges;
    }

    /**
     * Instantiates a cartridge from a descriptor.
     * @param cDescriptor the cartridge descriptor
     * @return IAndroMDACartridge
     */
    private static IAndroMDACartridge instantiateCartridge(ICartridgeDescriptor cd)
    {
        String className = cd.getCartridgeClassName();
        if (className == null)
            className = DefaultAndroMDACartridge.class.getName();
        try
        {
            Class cl = Class.forName(className);
            IAndroMDACartridge ac = (IAndroMDACartridge) cl.newInstance();
            ac.setDescriptor(cd);
            return ac;
        }
        catch (ClassNotFoundException e)
        {
            // @todo logging
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            // @todo logging
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // @todo logging
            e.printStackTrace();
        }
        return null;
    }

    public static void resetFoundCartridges()
    {
        cartridges = null;
    }

    private static List findCartridgeFiles()
    {
        if (classpath == null)
        {
            throw new IllegalStateException("initClasspath() not called!");
        }

        ArrayList result = new ArrayList();

        StringTokenizer pathTokenizer =
            new StringTokenizer(
                classpath,
                System.getProperty("path.separator"));

        while (pathTokenizer.hasMoreTokens())
        {
            File file = new File(pathTokenizer.nextToken());

            if (file.isDirectory())
            {
                // a cartridge doesn't have to be a jar. can be a straight directory too.
                if (new File(file, resourceName).exists())
                {
                    result.add(file);
                }
            }
            else if (jarFilter.accept(file))
            {
                result.add(file);
            }
        }
        return result;
    }
}
