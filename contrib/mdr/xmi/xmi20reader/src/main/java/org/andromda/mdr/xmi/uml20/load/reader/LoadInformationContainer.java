package org.andromda.mdr.xmi.uml20.load.reader;

import java.io.InputStream;

import java.util.Collection;

import javax.jmi.reflect.RefPackage;

import org.andromda.mdr.repositoryutils.MDRReflectionHelper;
import org.andromda.mdr.xmi.uml20.load.configuration.Configuration;
import org.andromda.mdr.xmi.uml20.load.core.IDRegistry;
import org.andromda.mdr.xmi.uml20.load.core.TagConverter;
import org.andromda.mdr.xmi.uml20.load.utils.InnerElementsCreator;
import org.andromda.mdr.xmi.uml20.load.utils.InnerFormatTaggConverter;
import org.andromda.mdr.xmi.uml20.load.utils.ModulesResolver;
import org.andromda.mdr.xmi.uml20.load.utils.PropertiesSetter;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.lib.jmi.xmi.InputConfig;

/**
 * Container for info necessary to load project
 * 
 * @author Adriano Andrulis
 */
public class LoadInformationContainer
    implements Cloneable
{
    private PropertiesSetter mPropertiesSetter;
    private InnerElementsCreator mInnerElementsCreator;
    private TagConverter mTagConverter;
    private Configuration mConfiguration;
    private InputStream mStream;
    private String mUri;
    private MDRepository mRepository;
    private RefPackage mExtent;
    private InputConfig mXMIInputConfig;
    private ModulesResolver mModulesResolver;
    private IDRegistry mIDRegistry;

    public LoadInformationContainer(
        RefPackage extent,
        Configuration configuration,
        InputConfig config)
    {
        mModulesResolver = new ModulesResolver();
        mExtent = extent;
        mRepository = MDRReflectionHelper.getRepository(extent);
        mIDRegistry = new IDRegistry(mRepository);
        mConfiguration = configuration;
        mXMIInputConfig = config;
        mInnerElementsCreator = new InnerElementsCreator(extent, mIDRegistry, mXMIInputConfig);
        mTagConverter = new InnerFormatTaggConverter(mInnerElementsCreator);
        mPropertiesSetter = new PropertiesSetter(mIDRegistry, extent, mInnerElementsCreator);
    }

    public Configuration getConfiguration()
    {
        return mConfiguration;
    }

    public void setConfiguration(Configuration configuration)
    {
        mConfiguration = configuration;
    }

    public InputStream getStream()
    {
        return mStream;
    }

    public void setStream(InputStream stream)
    {
        mStream = stream;
    }

    public void setURI(String uri)
    {
        mUri = uri;
    }

    public String getURI()
    {
        return mUri;
    }

    public MDRepository getRepository()
    {
        return mRepository;
    }

    public RefPackage getExtent()
    {
        return mExtent;
    }

    public InputConfig getXMIConfig()
    {
        return mXMIInputConfig;
    }

    public InnerElementsCreator getInnerElementsCreator()
    {
        return mInnerElementsCreator;
    }

    public PropertiesSetter getPropertiesSetter()
    {
        return mPropertiesSetter;
    }

    public TagConverter getTagConverter()
    {
        return mTagConverter;
    }

    public ModulesResolver getReferenceResolver()
    {
        return mModulesResolver;
    }

    public Collection getCreatedRootElements()
    {
        return mInnerElementsCreator.getCreatedRoots();
    }

    protected Object clone()
    {
        try
        {
            return super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public IDRegistry getIDRegistry()
    {
        return mIDRegistry;
    }
}