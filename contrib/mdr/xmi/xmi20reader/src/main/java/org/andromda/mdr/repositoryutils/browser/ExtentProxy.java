package org.andromda.mdr.repositoryutils.browser;

import javax.jmi.reflect.RefPackage;


class ExtentProxy
{
    private final RefPackage mExt;
    private final String mName;

    public ExtentProxy(
        RefPackage ext,
        String name)
    {
        mExt = ext;
        mName = name;
    }

    public RefPackage getExt()
    {
        return mExt;
    }

    public String toString()
    {
        return mName;
    }
}