package org.andromda.core.metafacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Contains static utility methods for dealing with MetafacadeMappings
 * instances.
 */
class MetafacadeMappingsUtils
{

    private static Logger logger = Logger
        .getLogger(MetafacadeMappingsUtils.class);

    /**
     * Constructs the unique key format expected for this mapping. Note that the
     * only argument required is the <code>object</code>, <code>suffixHead</code>
     * and <code>suffixes</code> is optional.
     * 
     * @param object the begining of the key
     * @param suffixHead the head of the suffix list.  This allows
     *        us to specify a value as the head of the suffix list, 
     *        which is necessary as the list is sorted alphabetically.
     * @param suffixes a collection of suffixes to append
     * @return the constructed key
     */
    static String constructKey(Object object, String suffixHead, Collection suffixes)
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
     * @param suffixHead the head of the suffix. This value
     *        will be appended before the suffix.
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
     * only argument required is the <code>object</code>,
     * <code>suffix</code> is optional.
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
     * Constructs the unique key format expected for this mapping. Note that the
     * only argument required is the <code>object</code>,
     * <code>suffixes</code> is optional.
     * 
     * @param object the begining of the key
     * @param suffixes a collection of suffixes to append (note that
     *        these suffixes are sorted)
     * @return the constructed key
     */
    static String constructKey(Object object, Collection suffixes)
    {
        return constructKey(object, null, suffixes);
    }
}