package org.andromda.core.metafacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.PropertyUtils;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Contains static utility methods for dealing with metafacade instances.
 * 
 * @author Chad Brandon
 */
class MetafacadeUtils
{
    private static Logger logger = Logger.getLogger(MetafacadeUtils.class);

    /**
     * Constructs the unique key format expected for this mapping. Note that the
     * only argument required is the <code>object</code>,
     * <code>suffixHead</code> and <code>suffixes</code> is optional.
     * 
     * @param object the begining of the key
     * @param suffixHead the head of the suffix list. This allows us to specify
     *        a value as the head of the suffix list, which is necessary as the
     *        list is sorted alphabetically.
     * @param suffixes a collection of suffixes to append
     * @return the constructed key
     */
    static String constructKey(
        Object object,
        String suffixHead,
        Collection suffixes)
    {
        final String methodName = "MetafacadeMapping.constructKey";
        ExceptionUtils.checkNull(methodName, "object", object);
        StringBuffer key = new StringBuffer(String.valueOf(object));
        if (StringUtils.isNotEmpty(suffixHead))
        {
            key.append(':');
            key.append(suffixHead);
        }
        if (suffixes != null)
        {
            List sortedSuffixes = new ArrayList(suffixes);
            // sort the suffixes so that the key is always in the same order
            // when constructing
            Collections.sort(sortedSuffixes);
            for (Iterator suffixIterator = sortedSuffixes.iterator(); suffixIterator
                .hasNext();)
            {
                key.append(':');
                key.append(suffixIterator.next());
            }
        }
        if (logger.isDebugEnabled())
            logger.debug("completed '" + methodName + "' with key --> '" + key
                + "'");
        return key.toString();
    }

    /**
     * Constructs the unique key format expected for this mapping. Note that the
     * only argument required is the <code>object</code>,
     * <code>suffixHead</code> and <code>suffix</code> are optional.
     * 
     * @param object the begining of the key
     * @param suffixHead the head of the suffix. This value will be appended
     *        before the suffix.
     * @param suffix a single suffix to append
     * @return the constructed key
     */
    static String constructKey(Object object, String suffixHead, String suffix)
    {
        List suffixes = null;
        if (suffix != null)
        {
            suffixes = new ArrayList();
            suffixes.add(suffix);
        }
        return constructKey(object, suffixHead, suffixes);
    }

    /**
     * Constructs the unique key format expected for this mapping. Note that the
     * only argument required is the <code>object</code>,<code>suffix</code>
     * is optional.
     * 
     * @param object the begining of the key
     * @param suffix a single suffix to append
     * @return the constructed key
     */
    static String constructKey(Object object, String suffix)
    {
        return constructKey(object, suffix, (String)null);
    }

    /**
     * Indicates whether or not the mapping properties (present on the mapping,
     * if any) are valid on the <code>mappingObject</code>.
     * 
     * @param mappingObject the mapping object on which the properties will be
     *        validated.
     * @param mapping the MetafacadeMapping instance that contains the
     *        properties.
     * @return true/false
     */
    static boolean propertiesValid(Object object, MetafacadeMapping mapping)
    {
        boolean valid = false;
        try
        {
            Iterator properties = mapping.getMappingProperties().iterator();
            while (properties.hasNext())
            {
                final MetafacadeMapping.Property property = (MetafacadeMapping.Property)properties
                    .next();
                valid = PropertyUtils.containsValidProperty(object, property
                    .getName(), property.getValue());
            }
        }
        catch (Throwable th)
        {
            th.printStackTrace();
            valid = false;
        }
        return valid;
    }

    /**
     * Constructs a new <code>metafacade</code> from the given
     * <code>metafacadeClass</code> and <code>mappingObject</code>.
     * 
     * @param metafacadeClass the metafacade class.
     * @param mappingObject the object to which the metafacade is mapped.
     * @return the new metafacade.
     * @throws Exception if any error occurs during metafacade creation
     */
    static MetafacadeBase constructMetafacade(
        final Class metafacadeClass,
        final Object mappingObject,
        final String context) throws Exception
    {
        MetafacadeBase metafacade = (MetafacadeBase)ConstructorUtils
            .invokeConstructor(metafacadeClass, new Object[]
            {
                mappingObject,
                context
            }, new Class[]
            {
                mappingObject.getClass(),
                java.lang.String.class
            });
        return metafacade;
    }
}