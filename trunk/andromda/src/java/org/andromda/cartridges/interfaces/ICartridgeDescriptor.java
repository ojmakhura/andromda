package org.andromda.cartridges.interfaces;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Describes the capabilities of an AndroMDA cartridge.
 * 
 * @author  <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * 
 */
public interface ICartridgeDescriptor {
    
    /**
     * Returns the name of this cartridge.
     * @return String the name
     */
    public String getCartridgeName();

    /**
     * Returns the stereotypes which are supported by this cartridge.
     * @return List the stereotypes
     */
    public List getSupportedStereotypes();

    /**
     * Returns the property values which are set for this cartridge. Example:
     * @andromda.persistence="ejb".
     * 
     * @return List the properties
     */
    public Map getProperties();
    
    /*
     * Gets the list of defined outlets. An outlet is a short alias name for a
     * path where output files will be written. A later step associates this
     * name with a concrete physical directory name.

     * @return List the outlets
     */
    public List getOutlets();
    
    /**
     * Returns the list of templates configured in this cartridge.
     * 
     * @return List the template lists
     */
    public List getTemplateConfigurations();
    
    /**
     * Gets the URL where this descriptor data came from.
     * 
     * @return URL
     */
    public URL getDefinitionURL();

    /**
     * Sets the URL where this descriptor data came from.
     * 
     * @param url
     */
    public void setDefinitionURL(URL url);
    
    /**
     * Returns the cartridge class name. This is used by cartridges that have an
     * own main class (other than DefaultAndroMDACartridge).
     * 
     * @return String
     */
    public String getCartridgeClassName();
}
