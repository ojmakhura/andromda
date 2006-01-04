package org.andromda.mdr.xmi.uml20.load.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.jmi.model.Classifier;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;

import org.andromda.mdr.repositoryutils.MDRReflectionHelper;
import org.andromda.mdr.xmi.uml20.load.configuration.Configuration;
import org.andromda.mdr.xmi.uml20.load.core.IDRegistry;
import org.netbeans.lib.jmi.xmi.InputConfig;
import org.netbeans.lib.jmi.xmi.XmiConstants;

public class InnerElementsCreator // extends InheritanceVisitor
{
    private Collection mCreatedElements = new HashSet();
    private Map mNamespaces;
    private final IDRegistry mRegistry;
    private final InputConfig mInputConfig;
    private Collection mRoots;

    public InnerElementsCreator(
        RefPackage extent,
        IDRegistry registry,
        InputConfig inputConfig)
    {
        mRegistry = registry;
        mInputConfig = inputConfig;
        mNamespaces = MDRReflectionHelper.groupClassesByTagValues(extent, XmiConstants.TAG_NS_URI);
        mRoots = new ArrayList();
    }

    public RefClass findClass(String namespace, String classType)
    {
        if (namespace == null)
        {
            namespace = "";
        }
        Collection registered = (Collection)mNamespaces.get(namespace);

        if (registered != null)
        {
            for (Iterator iter = registered.iterator(); iter.hasNext();)
            {
                RefClass r = (RefClass)iter.next();
                if (((Classifier)r.refMetaObject()).getName().equals(classType))
                {
                    return r;
                }
            }
        }
        return null;
    }

    public RefObject getRootRepositoryElement(
        String id,
        String type,
        String namespace,
        Configuration configuration)
    {
        if (id != null)
        {
            RefObject byMofId = mRegistry.getElementByID(id);
            if (byMofId != null)
            {
                return byMofId;
            }
        }
        RefClass refClass = findClass(namespace, type);
        RefObject createInner;
        createInner = createInner(refClass, id, configuration);
        if (createInner != null)
        {
            addRootCreatedElement(createInner);
        }
        return createInner;
    }

    private void addRootCreatedElement(RefObject createInner)
    {
        mRoots.add(createInner);
    }

    public RefObject createInner(RefClass refClass, String id, Configuration configuration)
    {
        Classifier refMetaObject = (Classifier)refClass.refMetaObject();
        boolean abstract1 = (refMetaObject).isAbstract();
        if (abstract1 && mInputConfig.isUnknownElementsIgnored())
        {
            return null;
        }
        return createInnerElement(refClass, id, configuration);
    }

    /**
     * @param refClass
     * @param shouldClearElement
     * @param project
     * @param element
     * @return created inner element
     */
    private RefObject createInnerElement(RefClass refClass, String id, Configuration configuration)
    {
        RefObject newElement;
        if (id != null && !id.equals("") && configuration.shouldReuseElements())
        {
            newElement = mRegistry.getElementByID(id);
            if (newElement == null)
            {
                newElement = refClass.refCreateInstance(null);
                addCreatedElement(newElement);

                // MDLog.getLoadLog().debug("element creator not found ID: "+ id
                // + " creating type: "+newElement.getClassType().getName());
            }
        }
        else
        {
            newElement = refClass.refCreateInstance(null);
            addCreatedElement(newElement);

            // MDLog.getLoadLog().debug("element creator creating type:
            // "+newElement.getClassType().getName());
        }
        mRegistry.registerID(id, newElement.refMofId());
        return newElement;
    }

    /**
     * @param newElement
     * @return true if such element was not added before
     */
    private boolean addCreatedElement(RefObject newElement)
    {
        return mCreatedElements.add(newElement);
    }

    /**
     * @param element element to check
     * @return true if the current element was created.
     */
    public boolean isCreatedElement(RefObject element)
    {
        return mCreatedElements.contains(element);
    }

    public Collection getCreatedRoots()
    {
        return mRoots;
    }
}