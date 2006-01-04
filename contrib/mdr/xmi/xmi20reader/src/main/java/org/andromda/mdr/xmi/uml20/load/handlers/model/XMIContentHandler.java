package org.andromda.mdr.xmi.uml20.load.handlers.model;

import javax.jmi.model.StructuralFeature;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefFeatured;
import javax.jmi.reflect.RefObject;
import javax.jmi.xmi.MalformedXMIException;

import org.andromda.mdr.repositoryutils.MDRReflectionHelper;
import org.andromda.mdr.xmi.uml20.load.core.TagConverter;
import org.andromda.mdr.xmi.uml20.load.core.XMIConstants;
import org.andromda.mdr.xmi.uml20.load.handlers.SkipElementHandler;
import org.andromda.mdr.xmi.uml20.load.reader.LoadInformationContainer;
import org.andromda.mdr.xmi.uml20.load.utils.ModulesResolver;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public class XMIContentHandler
    extends ModelHandler
{
    private RefObject mCurrentElement;
    private String mCurrentFeature;
    private ElementExtensionHandler mExtension;
    private final Attributes mAttrs;
    private final String mNamespace;
    private final boolean mExtensionsLoad;

    public XMIContentHandler(
        RefObject currentElement,
        String currentFeature,
        boolean extensionsLoad)
    {
        this(currentElement, currentFeature, null, null, extensionsLoad);
    }

    public XMIContentHandler(
        RefObject currentElement,
        String currentFeature,
        Attributes attrs,
        String namespace,
        boolean extensionsLoad)
    {
        mExtensionsLoad = extensionsLoad;
        mCurrentElement = currentElement;
        mCurrentFeature = currentFeature;
        mNamespace = namespace;
        if (attrs != null && attrs.getLength() > 0)
        {
            mAttrs = new AttributesImpl(attrs);
        }
        else
        {
            mAttrs = null;
        }
    }

    public void handleStartElement(String localName, Attributes attrs, String namespace)
        throws MalformedXMIException
    {
        boolean skip = false;
        if (mExtension != null)
        {
            skip = mExtension.ignoreInModule();
        }
        if (getMainHandler().getInfoContainer().getConfiguration().shouldSkipInnerElements(skip))
        {
            SkipElementHandler skipElementHandler = new SkipElementHandler();
            skipElementHandler.inc();
            getMainHandler().pushAndReplaceHandler(skipElementHandler);
            return;
        }

        RefObject created = null;
        if (getCurrentElement() == null)
        {
            String id = attrs.getValue(XMIConstants.XMI_NAMESPACE_URI, XMIConstants.XMIid);

            // model root
            created = getRootRepositoryElement(id, localName, namespace);
        }
        else
        {
            String id = attrs.getValue(XMIConstants.XMI_NAMESPACE_URI, XMIConstants.XMIid);
            Object currentElement = getCurrentElement();
            String type = getType(attrs, id != null, (RefObject)currentElement, localName);

            String href = attrs.getValue("", XMIConstants.XMIhref);

            if (type != null && href == null)
            {
                RefClass classProxyForTag = getTagConverter().getClassProxyForTag(type);
                mCurrentFeature = getTagConverter().getFeatureForTag(localName);
                if (classProxyForTag != null)
                {
                    if (getMainHandler().getInfoContainer().getConfiguration()
                        .canReceiveInnerElement(getCurrentElement(), classProxyForTag))
                    {
                        created = getElementsCreator().createInner(
                            classProxyForTag,
                            id,
                            getMainHandler().getInfoContainer().getConfiguration());
                        if (created != null) // if unknown element found null
                                                // is return
                        {
                            setValue(created);
                        }
                    }
                    else
                    {
                        getMainHandler().pushHandler(new SkipElementHandler());
                    }
                }
                else
                {
                    if (getMainHandler().getInfoContainer().getXMIConfig()
                        .isUnknownElementsIgnored())
                    {
                        getMainHandler().pushHandler(new SkipElementHandler());
                        return;
                    }
                    throw new MalformedXMIException(namespace + ":" + localName + " " + attrs);
                }
            }
        }
        if (created != null)
        {
            getMainHandler().pushHandler(
                new XMIContentHandler(created, localName, attrs, namespace, mExtensionsLoad));
            return;
        }
        else if (localName.equals(XMIConstants.XMIextension)
            && namespace.equals(XMIConstants.XMI_NAMESPACE_URI))
        {
            String extender = attrs.getValue(
                XMIConstants.XMI_NAMESPACE_URI,
                XMIConstants.XMIextender);
            ElementExtensionHandler elementExtensionHandler = null;
            if (extender.startsWith(XMIConstants.MagicDrawID))
            {
                elementExtensionHandler = new ElementExtensionHandler(
                    getCurrentElement(),
                    getCurrentFeature());
            }

            ElementExtensionHandler h = elementExtensionHandler;

            if (h != null)
            {
                mExtension = h;
                mExtension.setMainHandler(getMainHandler());
                getMainHandler().pushHandler(mExtension);
            }
            else
            {
                getMainHandler().pushHandler(new SkipElementHandler());
            }
            return;
        }
        else
        {
            getMainHandler().pushHandler(
                new XMIContentHandler(
                    getCurrentElement(),
                    localName,
                    attrs,
                    namespace,
                    mExtensionsLoad));
            return;
        }
    }

    /**
     * @param created
     */
    private void setValue(Object created)
    {
        Object currentElement = getCurrentElement();
        String curentFeature = mCurrentFeature;
        MDRReflectionHelper.setValue((RefFeatured)currentElement, curentFeature, created);
    }

    private RefObject getRootRepositoryElement(String id, String type, String namespace)
    {
        return getMainHandler().getInfoContainer().getInnerElementsCreator()
            .getRootRepositoryElement(
                id,
                type,
                namespace,
                getMainHandler().getInfoContainer().getConfiguration());
    }

    protected void setAttribute(RefObject element, String name, String value, String namespace)
    {
        value = value.intern();
        LoadInformationContainer info = getMainHandler().getInfoContainer();
        TagConverter tagConverter = info.getTagConverter();
        String feature = tagConverter.getFeatureForTag(getCurrentFeature());
        if (name.equals(XMIConstants.XMIhref))
        {
            ModulesResolver referenceResolver = info.getReferenceResolver();
            if (referenceResolver != null)
            {
                String fileNameFromHREF = ModulesResolver.getFileNameFromHREF(value);
                String id = ModulesResolver.getIDFromHREF(value);

                // referenceResolver.register(fileNameFromHREF, id, element);
                referenceResolver.register(fileNameFromHREF);
                info.getPropertiesSetter().setFeature(
                    element,
                    tagConverter.getFeatureForTag(name),
                    id,
                    feature,
                    info.getConfiguration(),
                    namespace,
                    mExtensionsLoad);
                return;
            }
        }
        info.getPropertiesSetter().setFeature(
            element,
            tagConverter.getFeatureForTag(name),
            value,
            feature,
            info.getConfiguration(),
            namespace,
            mExtensionsLoad);
    }

    public void handleEndElement(String name, String value, String namespace)
    {
        if (value != null)
        {
            setAttribute(getCurrentElement(), name, value, namespace);
        }
    }

    public RefObject getCurrentElement()
    {
        return mCurrentElement;
    }

    public void setCurrentElement(RefObject currentElement)
    {
        mCurrentElement = currentElement;
    }

    public String getCurrentFeature()
    {
        return mCurrentFeature;
    }

    public void finish()
    {
        if (mAttrs != null)
        {
            RefObject currentElement = getCurrentElement();
            if (currentElement != null)
            {
                if (mExtension != null)
                {
                    mExtension.handleFinish(this);
                    if (mExtension.isElementDisposed())
                    {
                        return;
                    }
                }
                setAttributes(currentElement, mAttrs);
            }
        }
    }

    protected void setAttributes(RefObject currentElement, Attributes attrs)
    {
        for (int i = 0; i < attrs.getLength(); i++)
        {
            String name = attrs.getLocalName(i);
            if (name.equals(XMIConstants.XMItype) || name.equals(XMIConstants.XMIid))
            {
                continue;
            }
            String value = attrs.getValue(i);

            // getPropertiesSetter().setFeature( currentElement,
            // getTagConverter().getFeatureForTag( name ) , value, featureToSet,
            // refInfo);
            if (name.equals(XMIConstants.XMIvalue)
                && XMIConstants.XMI_NAMESPACE_URI.equals(mNamespace))
            {
                setAttribute(currentElement, getCurrentFeature(), value, mNamespace);
            }
            else
            {
                setAttribute(currentElement, name, value, mNamespace);
            }
        }
    }

    public void setCurrentFeature(String currentFeature)
    {
        mCurrentFeature = currentFeature;
    }

    private String getType(
        Attributes attrs,
        boolean idFound,
        RefObject currentElement,
        String localName)
    {
        String type = attrs.getValue(XMIConstants.XMI_NAMESPACE_URI, XMIConstants.XMItype);
        if (type == null && idFound)
        {
            // single type element found. trying to determinate it using
            // property type
            StructuralFeature feature = MDRReflectionHelper.getFeature(localName, currentElement
                .refClass());
            if (feature != null)
            {
                type = feature.getType().getName();
            }
        }
        return type;
    }
}