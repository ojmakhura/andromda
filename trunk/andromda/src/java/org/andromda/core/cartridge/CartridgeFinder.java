package org.andromda.core.cartridge;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.andromda.core.common.ResourceFinder;
import org.apache.log4j.Logger;

/**
 * Finds AndroMDA cartridges on the classpath.
 *
 * @author    <a href="mailto:aslak.hellesoy@netcom.no">Aslak Hellesøy</a>
 * @author    <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author    Chad Brandon
 * @since     April 1, 2003
 * @version   $Revision: 1.2 $
 */
public class CartridgeFinder
{
	private static Logger logger = Logger.getLogger(CartridgeFinder.class);
	
    private final static String resourceName =
        "META-INF/andromda-cartridge.xml";

    private static List cartridges = null;

    /**
     * Returns a List of CartridgeDescriptor objects
     *
     * @return a <code>List<code> of cartriges
     */
    public static List findCartridges() 
    {
        if (cartridges == null)
        {
            cartridges = new ArrayList();

            URL cartridgeUris[]= ResourceFinder.findResources(resourceName);

            for (int ctr = 0; ctr < cartridgeUris.length; ctr++)
            {
            	URL cartridgeUri = cartridgeUris[ctr];
                CartridgeDescriptor cDescriptor = 
                    DefaultCartridgeDescriptor.getInstance(cartridgeUri);
                AndroMDACartridge cartridge =
                    instantiateCartridge(cDescriptor);
                cartridges.add(cartridge);
            }
        }

        for (Iterator iter = cartridges.iterator(); iter.hasNext();)
        {
            AndroMDACartridge element =
                (AndroMDACartridge) iter.next();
			logger.info("found cartridge --> '" 
			    + element.getDescriptor().getCartridgeName() + "'");

        }
        return cartridges;
    }
    
    /**
     * Instantiates a cartridge from a descriptor.
     * @param cDescriptor the cartridge descriptor
     * @return AndroMDACartridge
     */
    private static AndroMDACartridge instantiateCartridge(CartridgeDescriptor cd)
	{
    	String className = cd.getCartridgeClassName();
    	if (className == null)
    		className = DefaultAndroMDACartridge.class.getName();
    	try
		{
    		Class cl = Class.forName(className);
    		AndroMDACartridge ac = (AndroMDACartridge) cl.newInstance();
    		ac.setDescriptor(cd);
    		return ac;
    	}
    	catch (ClassNotFoundException e)
		{
    		logger.error(e);
    	}
    	catch (InstantiationException e)
		{
    		logger.error(e);
    	}
    	catch (IllegalAccessException e)
		{
    		logger.error(e);
    	}
    	return null;
    }

    public static void resetFoundCartridges()
    {
        cartridges = null;
    }
}
