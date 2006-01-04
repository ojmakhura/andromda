package org.andromda.mdr.repositoryutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.jmi.model.AggregationKindEnum;
import javax.jmi.model.Classifier;
import javax.jmi.model.EnumerationType;
import javax.jmi.model.ModelElement;
import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.model.Namespace;
import javax.jmi.model.Reference;
import javax.jmi.model.StructuralFeature;
import javax.jmi.model.Tag;
import javax.jmi.model.TagClass;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefEnum;
import javax.jmi.reflect.RefFeatured;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;

import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;


public class MDRReflectionHelper
{
    public static String getTagValue(
        ModelElement element,
        String tagId)
    {
        Collection tags = ((ModelPackage)element.refImmediatePackage()).getAttachesTo().getTag(element);
        Tag tag = null;
        Iterator it = tags.iterator();
        while (it.hasNext())
        {
            Object obj = it.next();
            if (obj instanceof Tag)
            {
                Tag temp = (Tag)obj;
                if (tagId.equals(temp.getTagId()))
                {
                    tag = temp;
                    break;
                }
            }
        }
        if (tag == null)
        {
            return null;
        }
        Collection values = tag.getValues();
        if (values.size() == 0)
        {
            return null;
        }
        return (String)values.iterator().next();
    }

    public static void setTagValue(
        ModelElement element,
        String tagId,
        String value)
    {
        ModelPackage refImmediatePackage = (ModelPackage)element.refImmediatePackage();
        TagClass tag2 = refImmediatePackage.getTag();
        Tag tag = null;
        for (Iterator iter = tag2.refAllOfClass().iterator(); iter.hasNext();)
        {
            tag = (Tag)iter.next();
            if (tag.getTagId().equals(tagId))
            {
                break;
            }
            tag = null;
        }

        if (tag == null)
        {
            tag = tag2.createTag();
            tag.setTagId(tagId);
        }
        List values = tag.getValues();
        if (!values.contains(value))
        {
            values.add(value);
        }
        refImmediatePackage.getAttachesTo().add(
            element,
            tag);
    }

    public static List getAttributesAndReferences(Classifier classifier)
    {
        Cache cache = Cache.getCache();
        String key = "getAttributesAndReferences";
        ArrayList cached = (ArrayList)cache.getC(
                key,
                classifier);
        if (cached == null)
        {
            cached = new ArrayList(getAllContents(classifier));
            boolean onlyChangeable = false;
            for (int i = 0; i < cached.size(); i++)
            {
                Object o = cached.get(i);
                if (o instanceof StructuralFeature)
                {
                    StructuralFeature feature = (StructuralFeature)o;
                    if (!onlyChangeable || feature.isChangeable())
                    {
                        continue;
                    }
                }
                cached.remove(i);
                i--;
            }
            cache.putC(
                key,
                classifier,
                cached);
        }
        return cached;
    }

    /**
     * @param metaClass meta object.
     * @return collection of available contents.
     */
    public static List getAllContents(Classifier metaClass)
    {
        Cache cache = Cache.getCache();
        String key = "getAllContents";
        ArrayList cached = (ArrayList)cache.getC(
                key,
                metaClass);

        if (cached == null)
        {
            cached = new ArrayList(metaClass.getContents());

            // RefClass classProxy = getClassProxy(null, metaClass);
            List supperTypes = getSupperTypes(metaClass);
            for (int i = 0; i < supperTypes.size(); i++)
            {
                Classifier c = (Classifier)supperTypes.get(i);
                List contents = c.getContents();
                for (int j = 0; j < contents.size(); j++)
                {
                    Object content = contents.get(j);
                    if (!cached.contains(content))
                    {
                        boolean alreadyAdded = false;
                        if (content instanceof StructuralFeature)
                        {
                            StructuralFeature sf = (StructuralFeature)content;
                            String name = sf.getName();
                            for (int k = cached.size() - 1; k >= 0 && !alreadyAdded; k--)
                            {
                                Object o = cached.get(k);
                                if (o instanceof StructuralFeature)
                                {
                                    StructuralFeature sf2 = (StructuralFeature)o;
                                    if (sf2.getName().equals(name))
                                    {
                                        // allready added
                                        alreadyAdded = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!alreadyAdded)
                        {
                            cached.add(content);
                        }
                    }
                }
            }
            cache.putC(
                key,
                metaClass,
                cached);
        }
        return cached;
    }

    /**
     * @param metaClass Classifier
     * @return list of all superclasses (metaobjects) for given Classifier.
     */
    public static List getSupperTypes(Classifier metaClass)
    {
        Cache cache = Cache.getCache();
        String key = "getSupperTypes";
        ArrayList cached = (ArrayList)cache.getC(
                key,
                metaClass);
        if (cached == null)
        {
            cached = new ArrayList(metaClass.getSupertypes());
            for (int i = 0; i < cached.size(); i++)
            {
                Classifier c1 = (Classifier)cached.get(i);
                List ss = c1.getSupertypes();
                for (int j = 0; j < ss.size(); j++)
                {
                    Object superType = ss.get(j);
                    if (!cached.contains(superType))
                    {
                        cached.add(superType);
                    }
                }
            }
            cache.putC(
                key,
                metaClass,
                cached);
        }
        return cached;
    }

    public static StructuralFeature getFeature(
        String featureName,
        RefClass proxyClass)
    {
        return getFeature(
            featureName,
            proxyClass,
            true);
    }

    public static StructuralFeature getFeature(
        String featureName,
        RefClass proxyClass,
        boolean onlyChangable)
    {
        Classifier metaClass = (Classifier)proxyClass.refMetaObject();
        return getFeature(
            featureName,
            onlyChangable,
            metaClass);
    }

    private static StructuralFeature getFeature(
        String featureName,
        boolean onlyChangable,
        Classifier metaClass)
    {
        Cache c = Cache.getCache();
        String key = "getFeature";
        StructuralFeature f = (StructuralFeature)c.getC(
                key,
                featureName,
                Boolean.valueOf(onlyChangable),
                metaClass);
        if (f == null)
        {
            List aa = getAttributesAndReferences(metaClass);
            for (int i = 0; i < aa.size(); i++)
            {
                StructuralFeature o = (StructuralFeature)aa.get(i);
                if (o.getName().equals(featureName))
                {
                    f = o;
                    break;
                }
            }
            c.putC(
                key,
                featureName,
                Boolean.valueOf(onlyChangable),
                metaClass,
                f);
        }
        return f;
    }

    /**
     * @param metaClass meta object.
     * @return collection of available references. Collection elements type is
     *         com.io_software.catools.tas.mof.model.StructuralFeature
     */
    public static List getReferences(Classifier metaClass)
    {
        String key = "references";
        List cached = (List)Cache.getCache().getC(
                key,
                metaClass);
        if (cached == null)
        {
            cached = new ArrayList(getAttributesAndReferences(metaClass));
            for (int i = 0; i < cached.size(); i++)
            {
                Object o = cached.get(i);
                if (!(o instanceof Reference))
                {
                    cached.remove(i);
                    i--;
                }
            }
            Cache.getCache().putC(
                key,
                metaClass,
                cached);
        }
        return (cached);
    }

    /**
     * Composite references is objects that are inner objects to given one.
     *
     * @param metaClass meta object.
     * @return collection of composite references. Collection elements type is
     *         com.io_software.catools.tas.mof.model.StructuralFeature
     */
    public static List getCompositeReferences(Classifier metaClass)
    {
        String key = "getCompositeReferences";
        List cached = (List)Cache.getCache().getC(
                key,
                metaClass);
        if (cached == null)
        {
            cached = new ArrayList(getReferences(metaClass));
            for (int i = 0; i < cached.size(); i++)
            {
                if (!isCompositeReference((Reference)cached.get(i)))
                {
                    cached.remove(i);
                    i--;
                }
            }
            Cache.getCache().putC(
                key,
                metaClass,
                cached);
        }
        return cached;
    }

    public static boolean isCompositeReference(StructuralFeature f)
    {
        boolean val = false;
        if (f instanceof Reference)
        {
            Reference reference = (Reference)f;
            val = AggregationKindEnum.COMPOSITE.equals(reference.getExposedEnd().getAggregation());
        }
        return val;
    }

    public static boolean isFeatureReference(
        String propertyName,
        RefObject object)
    {
        return getFeature(
            propertyName,
            object.refClass()) instanceof Reference;
    }

    public static boolean isMultiple(StructuralFeature feature)
    {
        boolean b = feature.getMultiplicity().getUpper() < 0;
        return b;
    }

    public static boolean isString(StructuralFeature feature)
    {
        Classifier type = feature.getType();
        if (type instanceof javax.jmi.model.PrimitiveType)
        {
            return "String".equals(type.getName());
        }
        return false;
    }

    public static boolean isBoolean(StructuralFeature feature)
    {
        Classifier type = feature.getType();
        if (type instanceof javax.jmi.model.PrimitiveType)
        {
            return "Boolean".equals(type.getName());
        }
        return false;
    }

    public static boolean isFeatureEnumeration(StructuralFeature feature)
    {
        Classifier type = feature.getType();
        return type instanceof EnumerationType;
    }

    public static void setEnumProperty(
        StructuralFeature feature,
        String string,
        RefObject refObj,
        RefPackage extent)
    {
        Classifier type = feature.getType();
        RefPackage found = searchP(
                extent,
                type);
        RefEnum enumeration = found.refGetEnum(
                type.getName(),
                string);
        refObj.refSetValue(
            feature.getName(),
            enumeration);
    }

    private static RefPackage searchP(
        RefPackage extent,
        Classifier type)
    {
        String key = "searchP";
        RefPackage found = (RefPackage)Cache.getCache().getC(
                key,
                extent,
                type);
        if (found == null)
        {
            ArrayList containers = new ArrayList();
            Namespace container = type.getContainer();
            while (container != null)
            {
                containers.add(
                    0,
                    container);
                container = container.getContainer();
            }
            found = extent;
            for (int i = 1; i < containers.size(); i++)
            {
                MofPackage mp = (MofPackage)containers.get(i);
                for (Iterator iter = found.refAllPackages().iterator(); iter.hasNext();)
                {
                    RefPackage rp = (RefPackage)iter.next();
                    if (((MofPackage)rp.refMetaObject()) == mp)
                    {
                        found = rp;
                        break;
                    }
                }
            }
            Cache.getCache().putC(
                key,
                extent,
                type,
                found);
        }
        return found;
    }

    public static boolean isInteger(StructuralFeature feature)
    {
        Classifier type = feature.getType();
        if (type instanceof javax.jmi.model.PrimitiveType)
        {
            return "Integer".equals(type.getName());
        }
        return false;
    }

    /**
     * @param pack root package
     * @return all packages from given one
     */
    private static List collectPacks(RefPackage pack)
    {
        ArrayList packs = new ArrayList();
        packs.add(pack);
        for (int i = 0; i < packs.size(); i++)
        {
            RefPackage p = (RefPackage)packs.get(i);
            packs.addAll(p.refAllPackages());
        }
        return packs;
    }

    /**
     * @param pack root package (M2) for retrieving classes
     * @param tagID tag id
     * @return map where namespaces URI is key, value is collection of classes
     */
    public static Map groupClassesByTagValues(
        RefPackage pack,
        String tagID)
    {
        List packages = collectPacks(pack);
        HashMap namespaces = new HashMap();
        for (int i = 0; i < packages.size(); i++)
        {
            RefPackage rp = (RefPackage)packages.get(i);

            String tagValue = MDRReflectionHelper.getTagValue(
                    (ModelElement)rp.refMetaObject(),
                    tagID);
            if (tagValue == null)
            {
                tagValue = "";
            }
            Collection registered = (Collection)namespaces.get(tagValue);
            if (registered == null)
            {
                registered = new ArrayList();
                namespaces.put(
                    tagValue,
                    registered);
            }
            registered.addAll(rp.refAllClasses());
        }
        return namespaces;
    }

    public static MDRepository getRepository(RefPackage extent)
    {
        String[] repositoryNames = MDRManager.getDefault().getRepositoryNames();
        for (int i = 0; i < repositoryNames.length; i++)
        {
            String repositoryName = repositoryNames[i];
            MDRepository repository = MDRManager.getDefault().getRepository(repositoryName);
            String[] extentNames = repository.getExtentNames();
            for (int j = 0; j < extentNames.length; j++)
            {
                String extentName = extentNames[j];
                if (repository.getExtent(extentName) == extent)
                {
                    return repository;
                }
            }
        }
        return null;
    }

    public static void setValue(
        RefFeatured currentElement,
        String feature,
        Object value)
    {
        StructuralFeature f = getFeature(
                feature,
                ((RefObject)currentElement).refClass());
        boolean multiple = isMultiple(f);
        if (multiple)
        {
            ((Collection)currentElement.refGetValue(feature)).add(value);
        }
        else
        {
            currentElement.refSetValue(
                feature,
                value);
        }
    }

    public static void reset()
    {
        Cache.getCache().reset();
    }

    private static class Cache
    {
        private static Cache mCache;
        private Map mTables;

        private Cache()
        {
            mTables = new TreeMap();
        }

        public void reset()
        {
            mTables.clear();
        }

        public Object getC(
            String name,
            Object key1,
            Object key2)
        {
            Map m0 = (Map)getL1(
                    mTables,
                    name,
                    HashMap.class);
            Map m1 = (Map)getL1(
                    m0,
                    key1,
                    HashMap.class);
            return m1.get(key2);
        }

        public Object putC(
            String name,
            Object key1,
            Object key2,
            Object value)
        {
            Map m0 = (Map)getL1(
                    mTables,
                    name,
                    HashMap.class);
            Map m1 = (Map)getL1(
                    m0,
                    key1,
                    HashMap.class);
            return m1.put(
                key2,
                value);
        }

        public Object getC(
            String name,
            String key1,
            Object key2,
            Object key3)
        {
            Map m0 = (Map)getL1(
                    mTables,
                    name,
                    HashMap.class);
            Map m1 = (Map)getL1(
                    m0,
                    key1,
                    HashMap.class);
            Map m2 = (Map)getL1(
                    m1,
                    key2,
                    HashMap.class);
            return m2.get(key3);
        }

        public void putC(
            String name,
            String key1,
            Object key2,
            Object key3,
            Object value)
        {
            Map m0 = (Map)getL1(
                    mTables,
                    name,
                    HashMap.class);
            Map m1 = (Map)getL1(
                    m0,
                    key1,
                    HashMap.class);
            Map m2 = (Map)getL1(
                    m1,
                    key2,
                    HashMap.class);
            m2.put(
                key3,
                value);
        }

        static Cache getCache()
        {
            if (mCache == null)
            {
                mCache = new Cache();
            }
            return mCache;
        }

        public Object getC(
            String name,
            Object key)
        {
            Map m = (Map)getL1(
                    mTables,
                    name,
                    HashMap.class);
            return m.get(key);
        }

        public Object putC(
            String name,
            Object key,
            Object value)
        {
            Map m = (Map)getL1(
                    mTables,
                    name,
                    HashMap.class);
            return m.put(
                key,
                value);
        }

        private Object getL1(
            Map tables,
            Object name,
            Class class1)
        {
            Object got = tables.get(name);
            if (got == null)
            {
                try
                {
                    got = class1.newInstance();
                }
                catch (InstantiationException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                tables.put(
                    name,
                    got);
            }
            return got;
        }
    }

    public static List getAllElements(MDRepository repository)
    {
        ArrayList l = new ArrayList();
        String[] extentNames = repository.getExtentNames();
        for (int i = 0; i < extentNames.length; i++)
        {
            String ext = extentNames[i];
            RefPackage refPackage = repository.getExtent(ext);
            Collection classes = refPackage.refAllClasses();
            for (Iterator iter = classes.iterator(); iter.hasNext();)
            {
                RefClass clazz = (RefClass)iter.next();
                l.addAll(clazz.refAllOfClass());
            }
        }
        return l;
    }
}