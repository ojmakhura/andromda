package org.andromda.core.cartridge;

import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.andromda.core.templateengine.TemplateEngine;

/**
 * Describes the capabilities of an AndroMDA cartridge.
 * 
 * @author  <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 */
public interface CartridgeDescriptor {
    
    /**
     * Returns the name of this cartridge.
     * @return String the name
     */
    public String getCartridgeName();

    /**
     * Returns the propery references supplised with this
     * cartridge. Property references are references to 
     * properties that are expected to be supplied by
     * the calling client.  These properties are then
     * made available to the template. 
     * 
     * @param reference
     */
    public Collection getPropertyReferences();
    
    /**
     * Returns the list of templates configured in this cartridge.
     * 
     * @return List the template list
     * @see TemplateConfiguration
     */
    public List getTemplateConfigurations();
    
    /**
     * Returns the List of template objects made available to
     * the templates.  (i.e. stringUtils of type org.apache.commons.lang.StringUtils
     * can be defined in the cartridge as a template object and made
     * available to the template at processing time).
     * @return the List of template objects.
     */
    public List getTemplateObjects();
        
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
    
    /**
     * Gets the TemplateEngine which implements the template 
     * processing.
     * 
     * @see org.andromda.core.templateengine.TemplateEngine
     * 
     * @return TemplateEngine
     */
    public TemplateEngine getTemplateEngine();
}
