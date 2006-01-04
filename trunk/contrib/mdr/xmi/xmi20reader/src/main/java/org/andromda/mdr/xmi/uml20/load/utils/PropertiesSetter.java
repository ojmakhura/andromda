package org.andromda.mdr.xmi.uml20.load.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.jmi.model.AggregationKindEnum;
import javax.jmi.model.AssociationEnd;
import javax.jmi.model.Reference;
import javax.jmi.model.StructuralFeature;
import javax.jmi.reflect.RefFeatured;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;

import org.andromda.mdr.repositoryutils.MDRReflectionHelper;
import org.andromda.mdr.xmi.uml20.load.configuration.Configuration;
import org.andromda.mdr.xmi.uml20.load.core.IDRegistry;
import org.andromda.mdr.xmi.uml20.load.core.ReferenceInfo;
import org.andromda.mdr.xmi.uml20.load.core.XMIConstants;
import org.apache.log4j.Logger;

public class PropertiesSetter
{
    
    private static Logger logger  = Logger.getLogger(PropertiesSetter.class);
    
    private Collection mRefs = new ArrayList();
    private ArrayList mBogus = new ArrayList();
    private InnerElementsCreator mInnerElementsCreator;
    private IDRegistry mIDRegistry;
    private ReferenceInfo mReferenceInfo;
    private RefPackage mExtent;

    public PropertiesSetter(
        IDRegistry registry,
        RefPackage extent,
        InnerElementsCreator creator)
    {
        mIDRegistry = registry;
        mExtent = extent;
        mInnerElementsCreator = creator;
    }

    /**
     * @param setTo
     * @param name
     * @param value
     * @param currentFeature
     * @param configuration
     * @param namespace
     * @param extensionsLoad
     */
    public void setFeature(
        RefObject setTo,
        String name,
        String value,
        String currentFeature,
        Configuration configuration,
        String namespace,
        boolean extensionsLoad)
    {
        setValue(setTo, value, name, currentFeature, configuration, namespace, extensionsLoad);
    }

    /**
     * @param element
     */
    private void setValue(
        RefObject element,
        String value,
        String propertyName,
        String currentFeature,
        Configuration configuration,
        String namespace,
        boolean extension)
    {
        if (propertyName.equals(XMIConstants.XMIid))
        {
            if (!configuration.shouldSetValue(element, propertyName, value, mInnerElementsCreator))
            {
                mReferenceInfo = null;
                return;
            }

            // element.setID( mValue); MDR does not allow to change id of
            // created element
            mReferenceInfo = null;
            return;
        }

        if (propertyName.equals(XMIConstants.XMIidref))
        {
            if (!configuration
                .shouldSetValue(element, currentFeature, value, mInnerElementsCreator))
            {
                mReferenceInfo = null;
                return;
            }

            setIDref(element, value, currentFeature, mReferenceInfo, extension);
            mReferenceInfo = null;
            return;
        }

        if (propertyName.equals(XMIConstants.XMIhref))
        {
            if (!configuration
                .shouldSetValue(element, currentFeature, value, mInnerElementsCreator))
            {
                mReferenceInfo = null;
                return;
            }
            setHref(element, value, currentFeature, mReferenceInfo, extension);

            mReferenceInfo = null;
            return;
        }

        if (MDRReflectionHelper.isFeatureReference(propertyName, element))
        {
            if (!configuration.shouldSetValue(element, propertyName, value, mInnerElementsCreator))
            {
                mReferenceInfo = null;
                return;
            }
            setIDref(element, value, propertyName, mReferenceInfo, extension);
            mReferenceInfo = null;
            return;
        }

        if (propertyName.equals(XMIConstants.XMIvalue)
            && XMIConstants.XMI_NAMESPACE_URI.equals(namespace))
        {
            if (!configuration
                .shouldSetValue(element, currentFeature, value, mInnerElementsCreator))
            {
                mReferenceInfo = null;
                return;
            }
            setValue(element, currentFeature, value, false, extension);
            mReferenceInfo = null;
            return;
        }
        mReferenceInfo = null;
        if (!configuration.shouldSetValue(element, propertyName, value, mInnerElementsCreator))
        {
            mReferenceInfo = null;
            return;
        }
        setValue(element, propertyName, value, false, extension);
    }

    /**
     * @param element2
     * @param href
     * @param featureName
     * @param info
     * @param extension
     */
    public void setHref(
        RefObject element2,
        String href,
        String featureName,
        ReferenceInfo info,
        boolean extension)
    {
        RefObject element = element2;
        String id = href;
        RefObject elementByID = mIDRegistry.getElementByID(id);

        Exception ex = null;
        boolean changeLater = info == null;
        StructuralFeature feature = MDRReflectionHelper.getFeature(featureName, element.refClass());
        boolean ordered = MDRReflectionHelper.isMultiple(feature)
            && feature.getMultiplicity().isOrdered();
        if (elementByID != null && !changeLater)
        {
            try
            {
                setValue(element, featureName, elementByID, ordered, extension);
            }
            catch (Exception e)
            {
                ex = e;
            }
        }
        if (elementByID == null || ex != null)
        {
            if (ex == null)
            {
                if (info == null)
                {
                    info = new ReferenceInfo(element, extension);
                }
                info.setFeatureName(featureName);
                info.setIDRef(href);
                info.setIsHref(true);

                mRefs.add(info);
            }
            else
            {
                logger.warn(
                    "failed to set property:" + info.getFeatureName() + " owner:" + info.getOwner()
                        + " reason:" + ex);
                mBogus.add(info);
            }
        }
    }

    /**
     * @param element2
     * @param idref
     * @param feature
     * @param info
     * @param extension
     */
    public void setIDref(
        Object element2,
        String idref,
        String feature,
        ReferenceInfo info,
        boolean extension)
    {
        RefObject element = (RefObject)element2;
        Exception bogus = null;
        boolean setLater = (info == null);
        StructuralFeature f = MDRReflectionHelper.getFeature(feature, element.refClass(), false);
        if (f == null)
        {
            logger.error("feature not found:" + feature + " in " + element);
            return;
        }
        boolean ordered = false;
        boolean multiple = false;
        if (MDRReflectionHelper.isMultiple(f))
        {
            ordered = f.getMultiplicity().isOrdered();
            multiple = idref.indexOf(' ') >= 0;
        }
        else
        {
            setLater = false;
        }

        List refs;
        if (multiple)
        {
            refs = new ArrayList();
            StringTokenizer tk = new StringTokenizer(idref, " ");
            while (tk.hasMoreTokens())
            {
                String token = tk.nextToken();
                refs.add(token);
            }
        }
        else
        {
            refs = Collections.singletonList(idref);
        }

        for (int i = 0; i < refs.size(); i++)
        {
            String ref = (String)refs.get(i);
            boolean set = false;
            RefObject elementByID = mIDRegistry.getElementByID(ref);
            if (elementByID != null && !setLater && (info != null))
            {
                try
                {
                    setValue(element, feature, elementByID, ordered, extension);
                    set = true;
                }
                catch (Exception ex)
                {
                    bogus = ex;

                    // add to bogus
                }
            }
            if (!set)
            {
                if (bogus == null)
                {
                    if (info == null)
                    {
                        info = new ReferenceInfo(element, extension);
                    }
                    info.setFeatureName(feature);
                    info.setIDRef(idref);
                    mRefs.add(info);
                }
                else
                {
                    logger.warn(
                        "failed to set property:" + info.getFeatureName() + " owner:"
                            + info.getOwner() + " reason:" + bogus);
                    mBogus.add(info);
                }
            }
        }
    }

    public void setReference(ReferenceInfo info)
    {
        info.setReference(this);
    }

    private void setValue(
        RefObject element,
        String featureName,
        Object value,
        boolean ordered,
        boolean extension)
    {
        StructuralFeature feature = MDRReflectionHelper.getFeature(featureName, element.refClass());
        if (feature == null)
        {
            logger.error(
                "can not set value for:" + featureName + " owner:" + element + " value:" + value);
            return;
        }

        // for performance we will check value
        // if value is RefObject, feature cannot be enumeration, integer or
        // boolean
        // mindis
        if (value instanceof RefObject)
        {
            if (ordered)
            {
                Collection result = (Collection)element.refGetValue(featureName);
                if (result instanceof List
                    && ((List)result).indexOf(value) != ((List)result).size() - 1)
                {
                    result.remove(value);
                }
            }
            if (extension)
            {
                if (feature instanceof Reference)
                {
                    Reference ref = (Reference)feature;
                    RefFeatured owner = ((RefObject)value).refImmediateComposite();
                    AssociationEnd end = ref.getExposedEnd();
                    if (owner != element
                        && end.getAggregation().equals(AggregationKindEnum.COMPOSITE))
                    {
                        // avoiding CompositionViolationException
                        logger.warn("duplicate owners for :" + value);
                        return;
                    }
                }
            }
            MDRReflectionHelper.setValue(element, featureName, value);
        }
        else
        {
            // just for performance check for String type first
            if (MDRReflectionHelper.isString(feature))
            {
                MDRReflectionHelper.setValue(element, featureName, value);
                return;
            }

            // just for performance check for Boolean type in second place
            if (MDRReflectionHelper.isBoolean(feature))
            {
                MDRReflectionHelper.setValue(element, featureName, Boolean.valueOf((String)value));
                return;
            }

            if (MDRReflectionHelper.isFeatureEnumeration(feature))
            {
                MDRReflectionHelper.setEnumProperty(feature, (String)value, element, mExtent);
                return;
            }

            if (MDRReflectionHelper.isInteger(feature))
            {
                MDRReflectionHelper.setValue(element, featureName, Integer.valueOf((String)value));
                return;
            }
            MDRReflectionHelper.setValue(element, featureName, value);
        }
    }

    public Collection getRefs()
    {
        return mRefs;
    }

    public void setReferenceInfo(ReferenceInfo referenceInfo)
    {
        mReferenceInfo = referenceInfo;
    }
}