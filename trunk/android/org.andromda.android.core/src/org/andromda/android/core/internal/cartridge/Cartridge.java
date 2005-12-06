package org.andromda.android.core.internal.cartridge;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.andromda.core.namespace.NamespaceDocument;
import org.apache.xmlbeans.XmlOptions;

/**
 * 
 * @author Peter Friese
 * @since 16.11.2005
 */
public class Cartridge
{

    /**
     * The default XML namespace for <code>namespace.xml</code> files in AndroMDA cartridges.
     */
    private static final String XML_NAMESPACE_ANDROMDA_NAMESPACE = "http://andromda.org/core/namespace";

    private final String cartridgeLocation;

    public Cartridge(String cartridgeLocation)
    {
        this.cartridgeLocation = cartridgeLocation;
    }

    /**
     * Loads cartridge namespace descriptor.
     * 
     * @return the namespace descriptor for the cartridge.
     * @throws CartridgeParsingException if the cartridge's namespace descriptor could not be found or parsed.
     */
    public NamespaceDocument getNamespaceDescriptor() throws CartridgeParsingException
    {
        String namespaceLocation = "jar:" + cartridgeLocation + "!/META-INF/andromda/namespace.xml";
        try
        {
            URL namespaceURL = new URL(namespaceLocation);
            XmlOptions options = setupDefaultNamespace();
            NamespaceDocument document = NamespaceDocument.Factory.parse(namespaceURL, options);
            return document;
        }
        catch (Exception e)
        {
            throw new CartridgeParsingException(e);
        }
    }

    /**
     * Setup an XmlOptions instance so the parser will assume the default namespace for the config document even if is
     * has no namespace set.
     * 
     * @return an XmlOptions instance suitable for parsing AndroMDA configuration documents.
     */
    private XmlOptions setupDefaultNamespace()
    {
        XmlOptions options = new XmlOptions();
        Map namespaceMapping = new HashMap();
        namespaceMapping.put("", XML_NAMESPACE_ANDROMDA_NAMESPACE);
        options.setLoadSubstituteNamespaces(namespaceMapping);
        return options;
    }

}
