package org.andromda.cartridges.mgmt;

import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;

import org.andromda.cartridges.interfaces.IAndroMDACartridge;

/**
 * Dictionary that registers cartridges under a stereotype. It can manage more
 * than one cartridge with the same stereotype.
 * 
 * @since 01.04.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 *
 */
public class CartridgeDictionary
{
    private HashMap hm = new HashMap();
    
    /**
     * Adds a cartridge to the dictionary.
     * 
     * @param stereotype the stereotype that is supported by this cartridge
     * @param desc the cartridge descriptor
     */
    public void addCartridge (String stereotype, IAndroMDACartridge desc)
    {
        if (hm.containsKey(stereotype))
        {
            ArrayList v = (ArrayList)hm.get(stereotype);
            v.add(desc);
        }
        else
        {
            ArrayList v = new ArrayList();
            v.add(desc);
            hm.put(stereotype, v);
        }
    }
    
    
    /**
     * Looks up all cartridges that support a given stereotype.
     * 
     * @param stereotype the stereotype
     * @return Collection collection of cartridges
     */
    public Collection lookupCartridges (String stereotype)
    {
        return (ArrayList)hm.get(stereotype);
    }
}
