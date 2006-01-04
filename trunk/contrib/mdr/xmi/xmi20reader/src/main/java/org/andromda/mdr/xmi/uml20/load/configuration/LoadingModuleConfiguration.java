package org.andromda.mdr.xmi.uml20.load.configuration;

import java.io.IOException;

import javax.jmi.model.Classifier;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefObject;

import org.andromda.mdr.xmi.uml20.load.utils.InnerElementsCreator;
import org.netbeans.lib.jmi.xmi.InputConfig;

/**
 * @author Adriano Andrulis
 * @version $Revision: 1.1 $
 */
public class LoadingModuleConfiguration
    extends Configuration
{
    private boolean mReadingModuleContents;

    public boolean handleIgnoreInModule(
        Object currentElement,
        InnerElementsCreator innerElementsCreator)
    {
        if (innerElementsCreator.isCreatedElement((RefObject)currentElement))
        {
            ((RefObject)currentElement).refDelete();
            return true;
        }
        return false;
    }

    public boolean canReceiveInnerElement(Object parent, RefClass classProxyForTag)
    {
        String name = ((Classifier)classProxyForTag.refMetaObject()).getName();
        if (name.equals("Package") || name.equals("Model") || name.equals("Profile"))
        {
            return true;
        }
        return isElementInCurrentModule();
    }

    private boolean isElementInCurrentModule()
    {
        return mReadingModuleContents;
    }

    public boolean shouldSetValue(
        RefObject element,
        String featureName,
        String value,
        InnerElementsCreator innerElementsCreator)
    {
        boolean elementInModule = isElementInCurrentModule();
        return elementInModule || innerElementsCreator.isCreatedElement(element);
    }

    public boolean shouldSkipInnerElements(boolean skip)
    {
        return skip;
    }

    public void handleAfterParsing(InputConfig config) throws IOException
    {
        super.handleAfterParsing(config);
        if (!mReadingModuleContents)
        {
            if (!config.isUnknownElementsIgnored())
            {
                throw new IOException("file is not module ");
            }
        }
    }

    public void setReadingModuleContents(boolean readingModuleContents)
    {
        mReadingModuleContents = readingModuleContents;
    }
}