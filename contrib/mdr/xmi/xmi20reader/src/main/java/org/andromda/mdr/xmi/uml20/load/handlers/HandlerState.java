package org.andromda.mdr.xmi.uml20.load.handlers;

import java.io.IOException;

import javax.jmi.xmi.MalformedXMIException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface HandlerState
{
    public void handleStartElement(String localName, Attributes attrs, String namespace)
        throws MalformedXMIException;

    public void handleEndElement(String name, String value, String namespace)
        throws MalformedXMIException,
            IOException,
            ParserConfigurationException,
            SAXException;
}