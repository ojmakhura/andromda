package org.andromda.mdr.xmi.uml20.load.utils;

import java.util.HashMap;
import java.util.Map;

import javax.jmi.reflect.RefClass;

import org.andromda.mdr.xmi.uml20.load.core.TagConverter;

public class InnerFormatTaggConverter
    implements TagConverter
{
    private final InnerElementsCreator mInnerElementsCreator;
    private Map mNamespaces = new HashMap();

    public InnerFormatTaggConverter(
        InnerElementsCreator innerElementsCreator)
    {
        mInnerElementsCreator = innerElementsCreator;
    }

    public RefClass getClassProxyForTag(String tag)
    {
        int prefixEnd = tag.indexOf(':');
        String type = tag;
        String namespace = null;
        if (prefixEnd > 0)
        {
            String prefix = tag.substring(0, prefixEnd);
            namespace = (String)mNamespaces.get(prefix);
            type = tag.substring(prefixEnd + 1);
        }
        return mInnerElementsCreator.findClass(namespace, type);
    }

    public String getFeatureForTag(String tag)
    {
        return tag;
    }

    public void registerNamespace(String prefix, String nspace)
    {
        mNamespaces.put(prefix, nspace);
    }
}