package org.andromda.cartridges.interfaces;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A default implementation of the ICartridgeDescriptor interface.
 * 
 * @see org.andromda.cartridges.interfaces.ICartridgeDescriptor
 * @see org.andromda.cartridges.interfaces.CartridgeXmlParser
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class DefaultCartridgeDescriptor implements ICartridgeDescriptor
{
    private String cartridgeName;
    private HashMap properties = new HashMap();
    private ArrayList supportedStereotypes = new ArrayList();
    private ArrayList outlets = new ArrayList();
    private ArrayList templates = new ArrayList();
    private ArrayList macrolibs = new ArrayList();
    private URL definitionURL;
    private String cartridgeClassName = null;


    /**
     * @see org.andromda.cartridges.interfaces.ICartridgeDescriptor#getCartridgeName()
     * @return String
     */
    public String getCartridgeName()
    {
        return cartridgeName;
    }

    /**
     * @see org.andromda.cartridges.interfaces.ICartridgeDescriptor#getProperties()
     */
    public Map getProperties()
    {
        return properties;
    }

    /**
     * @see org.andromda.cartridges.interfaces.ICartridgeDescriptor#getSupportedStereotypes()
     */
    public List getSupportedStereotypes()
    {
        return supportedStereotypes;
    }

    /**
     * @see org.andromda.cartridges.interfaces.ICartridgeDescriptor#getOutlets()
     */
    public List getOutlets()
    {
        return outlets;
    }

    /**
     * @see org.andromda.cartridges.interfaces.ICartridgeDescriptor#getTemplateConfigurations()
     */
    public List getTemplateConfigurations()
    {
        return templates;
    }

    /**
     * Adds a property to the list of properties of this cartridge. Properties
     * may be used to designate implementations for architectural aspects.
     * 
     * @param propertyName the name of the property
     * @param propertyValue the value of the property
     */
    public void addProperty(String propertyName, String propertyValue)
    {
        properties.put(propertyName, propertyValue);
    }

    /**
     * Adds a stereotype to the list of supported stereotypes of this cartridge.
     * 
     * @param stereotypeName the name of the stereotype
     */
    public void addSupportedStereotype(String stereotypeName)
    {
        supportedStereotypes.add(stereotypeName);
    }
    
    /**
     * Sets the name of the cartridge.
     * @param cartridgeName The cartridgeName to set
     */
    public void setCartridgeName(String cartridgeName)
    {
        this.cartridgeName = cartridgeName;
    }
    
    /**
     * Adds an outlet to the list of defined outlets. An outlet is a short name
     * for a path where output files will be written. A later step associates
     * this name with a concrete physical directory name.
     * 
     * @param string the outlet identifier
     */
    public void addOutlet(String outletname)
    {
        outlets.add(outletname);
    }
    
    /**
     * Adds an item to the list of defined template configurations.
     * 
     * @param templateConfiguration the new configuration to add
     */
    public void addTemplateConfiguration(TemplateConfiguration templateConfiguration)
    {
        templates.add(templateConfiguration);
    }
    
    /**
     * @see org.andromda.cartridges.interfaces.ICartridgeDescriptor#getDefinitionURL()
     */
    public URL getDefinitionURL()
    {
        return this.definitionURL;
    }
    /**
     * @see org.andromda.cartridges.interfaces.ICartridgeDescriptor#setDefinitionURL(java.net.URL)
     */
    public void setDefinitionURL(URL url)
    {
        this.definitionURL = url;
    }

    /**
     * Returns the cartridgeClassName.
     * @return String
     */
    public String getCartridgeClassName()
    {
        return cartridgeClassName;
    }

    /**
     * Sets the cartridgeClassName.
     * @param cartridgeClassName The cartridgeClassName to set
     */
    public void setCartridgeClassName(String cartridgeClassName)
    {
        this.cartridgeClassName = cartridgeClassName;
    }

    /**
     * Adds an item to the list of defined macro libraries.
     * 
     * @param libraryName String the name of the library
     */
    public void addMacroLibrary(String libraryName)
    {
        macrolibs.add(libraryName);
    }
    
    /**
     * @see org.andromda.cartridges.interfaces.ICartridgeDescriptor#getMacroLibraries()
     */
    public List getMacroLibraries()
    {
        return macrolibs;
    }

}
