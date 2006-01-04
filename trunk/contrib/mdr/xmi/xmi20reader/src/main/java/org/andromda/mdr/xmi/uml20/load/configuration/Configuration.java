package org.andromda.mdr.xmi.uml20.load.configuration;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefObject;

import org.andromda.mdr.xmi.uml20.load.utils.InnerElementsCreator;
import org.netbeans.lib.jmi.xmi.InputConfig;


/**
 * Configuration info holder for loading resources.
 */
public abstract class Configuration
{
    private Collection mNotUsedElements;

    /**
     * Default constructor.
     */
    public Configuration()
    {
        mNotUsedElements = new HashSet();
    }

    public void setReadingModuleContents(boolean readingModuleContents)
    {
        // nothing here
    }

    public boolean handleIgnoreInModule(
        Object currentElement,
        InnerElementsCreator innerElementsCreator)
    {
        return false;
    }

    /**
     * Indicates whether the given parent can receive the given child type
     *
     * @param parent parent for new element
     * @param classProxyForTag new element type
     * @return true if new element can be added
     */
    public boolean canReceiveInnerElement(
        Object parent,
        RefClass classProxyForTag)
    {
        return true;
    }

    public boolean shouldSetValue(
        RefObject element,
        String featureName,
        String value,
        InnerElementsCreator innerElementsCreator)
    {
        return true;
    }

    public void addNotUsedElement(RefObject el)
    {
        mNotUsedElements.add(el);
    }

    /**
     * Clean not used elements. Necessary because some elements should be marked
     * for dispose.
     */
    protected void disposeNotUsedElements()
    {
        for (Iterator i = mNotUsedElements.iterator(); i.hasNext();)
        {
            RefObject element = (RefObject)i.next();
            element.refDelete();
        }
    }

    /**
     * indicate whether the element should be skipped
     *
     * @param extension true if handling extension
     * @return
     */
    public boolean shouldSkipInnerElements(boolean extension)
    {
        return false;
    }

    public void handleAfterParsing(InputConfig config) throws IOException
    {
        disposeNotUsedElements();
    }

    public Collection getNotUsedElements()
    {
        return mNotUsedElements;
    }

    public boolean shouldReuseElements()
    {
        return true;
    }
}