package org.andromda.core.cartridge;

import java.net.URL;
import java.util.Collection;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ResourceFinder;
import org.apache.log4j.Logger;

/**
 * Finds and registers AndroMDA cartridges.
 * Discovered cartridges are registered in the
 * <code>ComponentContainer</code>.
 * 
 * @see org.andromda.core.common.ComponentContainer
 *
 * @author    <a href="mailto:aslak.hellesoy@netcom.no">Aslak Hellesøy</a>
 * @author    <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author    Chad Brandon
 * @since     April 1, 2003
 * @version   $Revision: 1.5 $
 */
public class CartridgeFinder
{
	private static Logger logger = Logger.getLogger(CartridgeFinder.class);
	
    private final static String resourceName =
        "META-INF/andromda-cartridge.xml";
	
	private static CartridgeFinder instance = null;
	
	/**
	 * Gets the shared intance.
	 * 
	 * @return the shared CartridgeFinder instance.
	 */
	public static CartridgeFinder instance() {
	    if (instance == null) {
	        instance = new CartridgeFinder();
	    }
	    return instance;
	}
    
    /**
     * Discovers and initializes all <code>AndroMDACartridge</code>
     * objects on the current classpath.  If the cartridge
     * with the given name is already registered in the 
     * ComponentContainer, it will not be registered again.
     */
    public void discoverCartridges()
    {
       this.discoverCartridges(true); 
    }

	/**
	 * Discovers and initializes all <code>AndroMDACartridge</code>
	 * objects on the current classpath.  If the cartridge
	 * with the given name is already registered in the 
	 * ComponentContainer, it will not be registered again.
     * 
     * @param showCartridges if true then the cartridge found will
     *        logged, otherwise nothing will be shown.
	 */
    public void discoverCartridges(boolean showCartridges) 
    {         
        URL cartridgeUris[]= ResourceFinder.findResources(resourceName);

        for (int ctr = 0; ctr < cartridgeUris.length; ctr++)
        {
        	URL cartridgeUri = cartridgeUris[ctr];
            CartridgeDescriptor descriptor = 
                DefaultCartridgeDescriptor.getInstance(cartridgeUri);
            AndroMDACartridge cartridge =
                instantiateCartridge(descriptor);
            
            String name = descriptor.getCartridgeName();
			
			if (!ComponentContainer.instance().isRegistered(name)) {
                if (showCartridges) {
				    logger.info("found cartridge --> '" + name + "'");
                }
			    ComponentContainer.instance().registerComponent(name, cartridge);
            }
        } 
    }
    
    /**
     * Returns all avialable <code>cartridges</code> 
     * that have been discovered and registered in 
     * the <code>ComponentContainer</code>.
     * 
     * @param type the component type.
     * @return Collection all components
     */
    public Collection getCartridges() {
       return ComponentContainer.instance().findComponentsOfType(
           AndroMDACartridge.class); 
    }

    /**
     * Instantiates a cartridge from a descriptor.
     * @param cDescriptor the cartridge descriptor
     * @return AndroMDACartridge
     */
    private AndroMDACartridge instantiateCartridge(CartridgeDescriptor cd)
	{
    	String className = cd.getCartridgeClassName();
    	if (className == null)
    		className = DefaultAndroMDACartridge.class.getName();
    	try
		{
    		Class cl = ClassUtils.loadClass(className);
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
}
