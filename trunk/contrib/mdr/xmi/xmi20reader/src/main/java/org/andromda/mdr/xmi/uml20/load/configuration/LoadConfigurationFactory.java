package org.andromda.mdr.xmi.uml20.load.configuration;

public class LoadConfigurationFactory
{
    public static Configuration createLoadingConfiguration()
    {
        return new LoadingConfiguration();
    }

    public static Configuration createLoadingModuleConfiguration()
    {
        return new LoadingModuleConfiguration();
    }
}