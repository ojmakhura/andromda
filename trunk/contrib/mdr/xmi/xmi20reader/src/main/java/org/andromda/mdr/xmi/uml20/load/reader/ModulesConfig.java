package org.andromda.mdr.xmi.uml20.load.reader;

import org.netbeans.lib.jmi.xmi.InputConfig;

public class ModulesConfig
    extends InputConfig
{
    private String mModulesDirectories;

    public void setModulesDirectories(String modulesDirectories)
    {
        mModulesDirectories = modulesDirectories;
    }

    public String getModulesDirectories()
    {
        return mModulesDirectories;
    }
}