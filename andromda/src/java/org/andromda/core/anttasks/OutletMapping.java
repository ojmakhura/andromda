package org.andromda.core.anttasks;

import java.io.File;

/**
 * Realizes a mapping between a logical outlet alias name and the physical
 * directory. Used by the &lt;outlet&gt; subtask of the &lt;andromda&gt; task.
 * 
 * @since 02.04.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 *
 */
public class OutletMapping
{
    private String cartridge;
    private String outlet;
    private File   dir;
    
    
    /**
     * Returns the name of the cartridge for which the mapping applies.
     * @return String
     */
    public String getCartridge()
    {
        return cartridge;
    }

    /**
     * Returns the physical directory.
     * @return File
     */
    public File getDir()
    {
        return dir;
    }

    /**
     * Returns the logical outlet name to be mapped.
     * @return String
     */
    public String getOutlet()
    {
        return outlet;
    }

    /**
     * Sets the cartridge.
     * @param cartridge The cartridge to set
     */
    public void setCartridge(String cartridge)
    {
        this.cartridge = cartridge;
    }

    /**
     * Sets the dir.
     * @param dir The dir to set
     */
    public void setDir(File dir)
    {
        this.dir = dir;
    }

    /**
     * Sets the outlet.
     * @param outlet The outlet to set
     */
    public void setOutlet(String outlet)
    {
        this.outlet = outlet;
    }

}
