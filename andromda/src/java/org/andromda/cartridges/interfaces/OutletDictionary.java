package org.andromda.cartridges.interfaces;

import java.io.File;
import java.util.HashMap;

/**
 * Dictionary of mappings from cartridge outlets to physical directories.
 * 
 * @since 02.04.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 *
 */
public class OutletDictionary
{
    private HashMap dict = new HashMap();

    /**
     * Add a mapping for a catridge outlet to a physical directory.
     * 
     * @param cartridgeName name of the cartridge to which the mapping applies
     * @param outletName name of the outlet to map
     * @param physDir physical directory
     */
    public void addOutletMapping(
        String cartridgeName,
        String outletName,
        File physDir)
    {
        HashMap hm;
        if (dict.containsKey(cartridgeName))
        {
            hm = (HashMap) dict.get(cartridgeName);
        }
        else
        {
            hm = new HashMap();
            dict.put(cartridgeName, hm);
        }
        hm.put(outletName, physDir);
    }

    /**
     * Lookup a mapping for a catridge outlet to a physical directory.
     * 
     * @param cartridgeName name of the cartridge to which the mapping applies
     * @param outletName name of the outlet to map
     * @return File the absolute path to the directory
     */
    public File lookupOutlet(String cartridgeName, String outletName)
    {
        HashMap outlets = (HashMap) dict.get(cartridgeName);
        if (outlets == null)
        {
            // @todo: better logging!
            System.err.println(
                "lookupOutlet: Could not find cartridge \""
                    + cartridgeName
                    + "\"");
            return null;
        }
        return (File) outlets.get(outletName);
    }
}
