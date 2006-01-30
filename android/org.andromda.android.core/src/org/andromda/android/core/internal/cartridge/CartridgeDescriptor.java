package org.andromda.android.core.internal.cartridge;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.andromda.android.core.cartridge.ICartridgeDescriptor;
import org.andromda.core.cartridge.CartridgeDocument;
import org.andromda.core.cartridge.CartridgeDocument.Cartridge;
import org.andromda.core.metafacade.MetafacadeDocument;
import org.andromda.core.metafacade.MetafacadeDocument.Metafacade;
import org.andromda.core.namespace.NamespaceDocument.Namespace;
import org.andromda.core.namespace.NamespaceDocument;
import org.apache.xmlbeans.XmlOptions;

/**
 * Provides access to the cartridge descriptor documents.
 * 
 * @author Peter Friese
 * @since 30.01.2006
 */
public class CartridgeDescriptor
        implements ICartridgeDescriptor
{
    
    /** The default XML namespace for <code>namespace.xml</code> files in AndroMDA cartridges. */
    private static final String XML_NAMESPACE_ANDROMDA_NAMESPACE = "http://andromda.org/core/namespace";

    /** The default XML namespace for <code>cartridge.xml</code> files in AndroMDA cartridges. */
    private static final String XML_NAMESPACE_ANDROMDA_CARTRIDGE = "http://andromda.org/core/cartridge";
    
    /** The default XML namespace for <code>metafacades</code> files in AndroMDA cartridges. */
    private static final String XML_NAMESPACE_ANDROMDA_METAFACADES = "http://andromda.org/core/metafacades";
    
    /** Namespace options for the <code>namespace.xml</code> file. */
    private XmlOptions namespaceXmlOptions;
    
    /** Namespace options for the <code>cartridge.xml</code> file. */
    private XmlOptions cartridgeXmlOptions;
    
    /** Namespace options for the <code>metafacades.xml</code> file. */
    private XmlOptions metafacadesXmlOptions;

    /** The location of the cartridge. */
    private final String cartridgeLocation;

    /**
     * Creates a new CartridgeDescriptor.
     * 
     * @param location The location of the cartridge.
     */
    public CartridgeDescriptor(final String location)
    {
        this.cartridgeLocation = location;
        setupDefaultNamespaces();
    }
    
    /**
     * {@inheritDoc}
     */
    public Cartridge getCartridge() throws CartridgeParsingException
    {
        String documentLocation = "jar:" + cartridgeLocation + "!/META-INF/andromda/cartridge.xml";
        try
        {
            URL cartridgeURL = new URL(documentLocation);
            CartridgeDocument document = CartridgeDocument.Factory.parse(cartridgeURL, cartridgeXmlOptions);
            return document.getCartridge();
        }
        catch (Exception e)
        {
            throw new CartridgeParsingException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Namespace getNamespace() throws CartridgeParsingException
    {
        String documentLocation = "jar:" + cartridgeLocation + "!/META-INF/andromda/namespace.xml";
        try
        {
            URL namespaceURL = new URL(documentLocation);
            NamespaceDocument document = NamespaceDocument.Factory.parse(namespaceURL, namespaceXmlOptions);
            return document.getNamespace();
        }
        catch (Exception e)
        {
            throw new CartridgeParsingException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @throws CartridgeParsingException 
     */
    public Metafacade getMetafacade() throws CartridgeParsingException
    {
        String documentLocation = "jar:" + cartridgeLocation + "!/META-INF/andromda/metafacades.xml";
        try
        {
            URL metafacadesURL = new URL(documentLocation);
            MetafacadeDocument document = MetafacadeDocument.Factory.parse(metafacadesURL, metafacadesXmlOptions);
            return document.getMetafacade();
        }
        catch (Exception e)
        {
            throw new CartridgeParsingException(e);
        }
    }
    
    /**
     * Setup an XmlOptions instance so the parser will assume the default namespace for the config documents even if it
     * has no namespace set.
     */
    private void setupDefaultNamespaces()
    {
        namespaceXmlOptions = new XmlOptions();
        Map namespaceMapping = new HashMap();
        namespaceMapping.put("", XML_NAMESPACE_ANDROMDA_NAMESPACE);
        namespaceXmlOptions.setLoadSubstituteNamespaces(namespaceMapping);
        
        cartridgeXmlOptions = new XmlOptions();
        namespaceMapping = new HashMap();
        namespaceMapping.put("", XML_NAMESPACE_ANDROMDA_CARTRIDGE);
        cartridgeXmlOptions.setLoadSubstituteNamespaces(namespaceMapping);
        
        metafacadesXmlOptions = new XmlOptions();
        namespaceMapping = new HashMap();
        namespaceMapping.put("", XML_NAMESPACE_ANDROMDA_METAFACADES);
        metafacadesXmlOptions.setLoadSubstituteNamespaces(namespaceMapping);
        
        
    }    

}
