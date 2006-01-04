package org.andromda.mdr.xmi.uml20.load.core;

import javax.jmi.reflect.RefClass;

public interface TagConverter
{
    RefClass getClassProxyForTag(String tag);

    String getFeatureForTag(String feature);

    void registerNamespace(String prefix, String nspace);
}