package org.andromda.core.cartridge;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.templateengine.TemplateEngine;
import org.apache.commons.lang.StringUtils;

/**
 * A default implementation of the CartridgeDescriptor interface.
 * 
 * @see org.andromda.core.cartridge.CartridgeDescriptor
 * @see org.andromda.common.XmlObjectFactory
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 */
public class DefaultCartridgeDescriptor implements CartridgeDescriptor
{
    
    private String cartridgeName;
    private Map properties = new HashMap();
    private Collection propertyReferences = new ArrayList();
    private List templates = new ArrayList();
    private List templateObjects = new ArrayList();
    private URL definitionURL;
    private String cartridgeClassName = null;
    
    /**
     * Returns a new configured instance of this DefaultCartridgeDescriptor as 
     * a CartridgeDescriptor configured from the CartridgeDescriptor configuration URI string.
     * 
     * @param cartridgeDescriptorUri the URI to the XML type cartridgeDescriptor configuration file.
     * @return CartridgeDescriptor the configured CartridgeDescriptor instance.
     * @throws MalformedURLException when the cartridgeDescriptorUri is invalid (not a valid URL).
     */
    public static CartridgeDescriptor getInstance(String cartridgeDescriptorUri) throws MalformedURLException 
    {
        final String methodName = "DefaultCartridgeDescriptor.getInstance";
        cartridgeDescriptorUri = StringUtils.trimToEmpty(cartridgeDescriptorUri);
        ExceptionUtils.checkEmpty(methodName, "cartridgeDescriptorUri", cartridgeDescriptorUri);
        CartridgeDescriptor cartridgeDescriptor = getInstance(new URL(cartridgeDescriptorUri));
        return cartridgeDescriptor;
    }
    
    /**
     * Returns a new configured instance of this DefaultCartridgeDescriptor as
     *  a CartridgeDescriptor configured from the cartridgeDescriptor configuration URI.
     * 
     * @param cartridgeDescriptorUri the URI to the XML cartridgeDescriptor configuration file.
     * @return CartridgeDescriptor the configured CartridgeDescriptor instance.
     */
    public static CartridgeDescriptor getInstance(URL cartridgeDescriptorUri) 
    {
        final String methodName = "DefaultCartridgeDescriptor.getInstance";
        ExceptionUtils.checkNull(methodName, "cartridgeDescriptorUri", cartridgeDescriptorUri);
        DefaultCartridgeDescriptor cartridgeDescriptor = 
            (DefaultCartridgeDescriptor)XmlObjectFactory.getInstance(
                CartridgeDescriptor.class).getObject(cartridgeDescriptorUri);
        cartridgeDescriptor.definitionURL = cartridgeDescriptorUri;
        return cartridgeDescriptor;
    }


    /**
     * @see org.andromda.core.cartridge.CartridgeDescriptor#getCartridgeName()
     */
    public String getCartridgeName()
    {
        return cartridgeName;
    }

    /**
     * @see org.andromda.core.cartridge.CartridgeDescriptor#getProperties()
     */
    public Map getProperties()
    {
        return properties;
    }

    /**
     * @see org.andromda.core.cartridge.CartridgeDescriptor#getTemplateConfigurations()
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
     * @see org.andromda.core.cartridge.CartridgeDescriptor#getPropertyReferences()
     */
    public Collection getPropertyReferences() 
    {
        return this.propertyReferences;
    }

    /**
     * Adds a property reference. Property references
     * are those properties that are expected to be 
     * supplied by the calling client.  These supplied
     * properties are made available to the template
     * during processing.
     * 
     * @param reference
     */
    public void addPropertyReference(String reference) 
    {
        this.propertyReferences.add(reference);
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
     * Adds an item to the list of defined template configurations.
     * 
     * @param templateConfiguration the new configuration to add
     */
    public void addTemplateConfiguration(TemplateConfiguration templateConfiguration)
    {
        templates.add(templateConfiguration);
    }
    
    /**
     * @see org.andromda.core.cartridge.CartridgeDescriptor#getDefinitionURL()
     */
    public URL getDefinitionURL()
    {
        return this.definitionURL;
    }
    /**
     * @see org.andromda.core.cartridge.CartridgeDescriptor#setDefinitionURL(java.net.URL)
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
        this.getTemplateEngine().addMacroLibrary(libraryName);
    }
    
    /**
     * Adds the <code>templateObject</code> and makes
     * it available to the template.
     * 
     * @param templateObject
     */
    public void addTemplateObject(TemplateObject templateObject) 
    {
        final String methodName = "DefaultCartridgeDescriptor.addTemplateObject";
        ExceptionUtils.checkNull(methodName, "templateObject", templateObject);
        templateObject.setResource(this.getDefinitionURL());
        templateObject.setNamespace(this.getCartridgeName());
        this.templateObjects.add(templateObject);
    }
    
    /**
     * @see org.andromda.core.cartridge.CartridgeDescriptor#getTemplateObjects()
     */
    public List getTemplateObjects() 
    {
        return this.templateObjects;
    }
    
    /**
     * Sets the template engine class for this cartridge.
     * 
     * @param templateEngineClassName
     */
    public void setTemplateEngineClass(String templateEngineClassName) {
        if (StringUtils.isNotBlank(templateEngineClassName)) {
	        ComponentContainer.instance().registerDefaultComponent(
	            TemplateEngine.class,
	            templateEngineClassName);
        }
    }
    
    /**
     * @see org.andromda.core.cartridge.CartridgeDescriptor#getTemplateEngine()
     */
    public TemplateEngine getTemplateEngine() {
        return (TemplateEngine)ComponentContainer.instance().findComponent(
            	TemplateEngine.class);
    }
    
}
