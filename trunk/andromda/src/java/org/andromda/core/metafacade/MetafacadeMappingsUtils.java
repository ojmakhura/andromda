package org.andromda.core.metafacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
     * only argument required is the <code>object</code>,<code>suffixes</code>
     * is optional.
     * 
     * @param object the begining of the key
     * @param suffixes a collection of suffixes to append
     * @return the constructed key
     */
    protected static String constructKey(Object object, Collection suffixes)
    {
        final String methodName = "MetafacadeMapping.constructKey";
        ExceptionUtils.checkNull(methodName, "object", object);
        StringBuffer key = new StringBuffer(String.valueOf(object));
        if (suffixes != null)
        {
            for (Iterator suffixIterator = suffixes.iterator(); suffixIterator.hasNext();)
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
     * only argument required is the <code>object</code>,<code>suffixes</code>
     * is optional.
     * 
     * @param object the begining of the key
     * @param suffix a single suffix to append
     * @return the constructed key
     */
    protected static String constructKey(Object object, String suffix)
    {
        Collection suffixes = null;
        if (StringUtils.isNotEmpty(suffix))
        {
            suffixes = new ArrayList();
            suffixes.add(suffix);
        }
        return constructKey(object, suffixes);
    }
}