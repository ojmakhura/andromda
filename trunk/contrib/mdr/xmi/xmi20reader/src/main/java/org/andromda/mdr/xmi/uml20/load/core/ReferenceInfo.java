package org.andromda.mdr.xmi.uml20.load.core;

import javax.jmi.reflect.RefObject;

import org.andromda.mdr.xmi.uml20.load.utils.PropertiesSetter;

public class ReferenceInfo
{
    private HREFInfo mHREFInfo;
    private RefObject mOwner;
    private String mFeatureName;
    private String mIDRef;
    private boolean mHref;
    private final boolean mExtension;

    public ReferenceInfo(
        RefObject element,
        String currentFeature,
        String value,
        boolean extension)
    {
        this(element, extension);
        mFeatureName = currentFeature;
        mIDRef = value;
    }

    public ReferenceInfo(
        RefObject owner,
        boolean extension)
    {
        mOwner = owner;
        mExtension = extension;
    }

    public ReferenceInfo(
        RefObject currentElement,
        HREFInfo refInfo,
        boolean extension)
    {
        mHREFInfo = refInfo;
        mOwner = currentElement;
        mExtension = extension;
    }

    public String getFeatureName()
    {
        return mFeatureName;
    }

    public String getIDRef()
    {
        return mIDRef;
    }

    public RefObject getOwner()
    {
        return mOwner;
    }

    public void setFeatureName(String featureName)
    {
        mFeatureName = featureName;
    }

    public void setIDRef(String ref)
    {
        mIDRef = ref;
    }

    public HREFInfo getHRefInfo()
    {
        return mHREFInfo;
    }

    public void setReference(PropertiesSetter setter)
    {
        if (!mHref)
        {
            setter.setIDref(getOwner(), getIDRef(), getFeatureName(), this, mExtension);
        }
        else
        {
            setter.setHref(getOwner(), getIDRef(), getFeatureName(), this, mExtension);
        }
    }

    public void setIsHref(boolean href)
    {
        mHref = href;
    }
}