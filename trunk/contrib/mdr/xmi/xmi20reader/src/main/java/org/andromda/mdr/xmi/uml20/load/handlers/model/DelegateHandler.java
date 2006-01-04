package org.andromda.mdr.xmi.uml20.load.handlers.model;

import javax.jmi.xmi.MalformedXMIException;

import org.andromda.mdr.xmi.uml20.load.core.XMIConstants;
import org.andromda.mdr.xmi.uml20.load.handlers.DefaultHandler;
import org.andromda.mdr.xmi.uml20.load.handlers.SkipElementHandler;
import org.xml.sax.Attributes;

public class DelegateHandler
    extends XMIContentHandler
{
    public DelegateHandler()
    {
        super(null, null, false);
    }

    public void handleStartElement(String localName, Attributes attrs, String namespace)
        throws MalformedXMIException
    {
        if (!namespace.equals(XMIConstants.XMI_NAMESPACE_URI)
            && !namespace.equals(XMIConstants.UML_20_SCHEMA_SPEC))
        {
            if (!getMainHandler().getInfoContainer().getXMIConfig().isUnknownElementsIgnored())
            {
                throw new MalformedXMIException("unknown namespace" + namespace);
            }
            return;
        }

        if (namespace.equals(XMIConstants.XMI_NAMESPACE_URI))
        {
            if (localName.equals(XMIConstants.XMIheader))
            {
                // this is only for compatibility, header not saving anymore
                SkipElementHandler skipElementHandler = new SkipElementHandler();
                getMainHandler().pushHandler(skipElementHandler);
            }
            else if (localName.equals(XMIConstants.XMIcontent))
            {
                throw new MalformedXMIException("LOADING BETA FILE (OLD FORMAT) is not supported");
            }
            else if (localName.equals(XMIConstants.XMIextension))
            {
                // ExtensionsHandler extensionsHandler = new
                // ExtensionsHandler(getEnvironment());
                DefaultHandler extensionsHandler = new SkipElementHandler();
                getMainHandler().pushHandler(extensionsHandler);
                return;
            }
            else if (localName.equals(XMIConstants.XMI))
            {
                return;
            }
            else if (localName.equals(XMIConstants.XMIdocumentation))
            {
                getMainHandler().pushHandler(new SkipElementHandler());
                return;
            }
        }
        else
        {
            super.handleStartElement(localName, attrs, namespace);
        }
    }
}