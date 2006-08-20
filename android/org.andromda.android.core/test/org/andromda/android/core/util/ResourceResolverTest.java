package org.andromda.android.core.util;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import junit.framework.TestCase;

/**
 * Test case for {@link ResourceResolver}.
 *
 * @author Peter Friese
 * @since 29.05.2006
 */
public class ResourceResolverTest
        extends TestCase
{

    /**
     * @return the cartridge root path
     */
    private String getCartridgeRootPath()
    {
        URL cartridgesRootURL = getClass().getResource("cartridges");
        String cartridesRootPath = cartridgesRootURL.getPath();
        return cartridesRootPath;
    }

    /**
     * Test method for {@link org.andromda.android.core.util.ResourceResolver#findCartridge(java.lang.String, java.lang.String, java.lang.String, boolean)}.
     */
    public void testFindCartridge()
    {
        String cartridesRootPath = getCartridgeRootPath();

        // non-strict mode:
        final String libraryPath1 = ResourceResolver.findCartridge(cartridesRootPath, "dummy", "3.2", false);
        assertEquals("file:/" + cartridesRootPath + File.separator + "andromda-dummy-cartridge-3.2-SNAPSHOT.jar", libraryPath1);

        // finding in strict mode, so the version must be an exact match:
        final String libraryPath2 = ResourceResolver.findCartridge(cartridesRootPath, "dummy", "3.2", true);
        assertEquals("file:/" + cartridesRootPath + File.separator + "andromda-dummy-cartridge-3.2.jar", libraryPath2);

        // no cartridge exists with name "andromda-void-cartridge-3.2.jar":
        final String libraryPath3 = ResourceResolver.findCartridge(cartridesRootPath, "void", "3.2", true);
        assertEquals(null, libraryPath3);

        // finding a cartridge in one of the subdirectories:
        final String libraryPath4 = ResourceResolver.findCartridge(cartridesRootPath, "yetanother", "3.2", true);
        assertEquals("file:/" + cartridesRootPath + File.separator + "down" + File.separator + "under" + File.separator + "andromda-yetanother-cartridge-3.2.jar", libraryPath4);
    }

    /**
     * Test method for {@link org.andromda.android.core.util.ResourceResolver#findCartridgeRoot(org.eclipse.core.resources.IFile)}.
     */
    public void testFindCartridgeRoot()
    {
        // ResourceResolver.findCartridgeRoot()
    }

    /**
     * Test method for {@link org.andromda.android.core.util.ResourceResolver#findLibrary(java.lang.String, java.lang.String, java.lang.String, boolean)}.
     */
    public void testFindLibrary()
    {
        String cartridesRootPath = getCartridgeRootPath();

        // non-strict mode:
        final String libraryPath1 = ResourceResolver.findLibrary(cartridesRootPath, "andromda-dummy-cartridge", "3.2", false);
        assertEquals("file:/" + cartridesRootPath + File.separator + "andromda-dummy-cartridge-3.2-SNAPSHOT.jar", libraryPath1);

        // finding in strict mode, so the version must be an exact match:
        final String libraryPath2 = ResourceResolver.findLibrary(cartridesRootPath, "andromda-dummy-cartridge", "3.2", true);
        assertEquals("file:/" + cartridesRootPath + File.separator + "andromda-dummy-cartridge-3.2.jar", libraryPath2);
    }

    /**
     * Test method for {@link org.andromda.android.core.util.ResourceResolver#findProjectCartridge(java.lang.String, java.lang.String, java.lang.String, boolean)}.
     */
    public void testFindProjectCartridge()
    {
        String cartridesRootPath = getCartridgeRootPath();

        final String libraryPath1 = ResourceResolver.findProjectCartridge(cartridesRootPath, "p1", "3.2", false);
        assertEquals("file:/" + cartridesRootPath + File.separator + "project" + File.separator + "andromda-andromdapp-project-p1-3.2.jar", libraryPath1);

        final String libraryPath2 = ResourceResolver.findProjectCartridge(cartridesRootPath, "p2", "3.2", false);
        assertEquals("file:/" + cartridesRootPath + File.separator + "project" + File.separator + "andromda-andromdapp-project-p2-3.2.jar", libraryPath2);

        final Collection projectCartridges32 = ResourceResolver.findProjectCartridges(cartridesRootPath, "3.2", false);
        assertEquals(2, projectCartridges32.size());

        final Collection projectCartridges33 = ResourceResolver.findProjectCartridges(cartridesRootPath, "3.3", false);
        assertEquals(1, projectCartridges33.size());
    }

}
