package org.andromda.core.metafacade;

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
     * only argument required is the <code>object</code>,<code>suffix</code>
     * is optional.
     * 
     * @param object the begining of the key
     * @param suffix the suffix to append
     * @return the constructed key
     */
    protected static String constructKey(Object object, String suffix)
    {
        final String methodName = "MetafacadeMapping.constructKey";
        ExceptionUtils.checkNull(methodName, "object", object);
        Object key = object;
        suffix = StringUtils.trimToEmpty(suffix);
        if (StringUtils.isNotEmpty(suffix))
        {
            key = appendContext(key, suffix);
        }

        if (logger.isDebugEnabled())
            logger.debug("completed '" + methodName + "' with key --> '" + key
                + "'");
        return key.toString();
    }

    /**
     * Appends a context suffix to the <code>object</code>. The context
     * suffix is ':' +<code>suffix</code> argument. If suffix is null or an
     * empty object, then <code>object</code> will just be returned as a
     * String unchanged.
     * 
     * @param context the context which we'll be appending too.
     * @param suffix the suffix to append.
     * @return the new context name.
     */
    protected static String appendContext(Object object, String context)
    {
        ExceptionUtils.checkNull(
            "MetafacadeMappingsUtils.appendContextSuffix",
            "object",
            object);
        String objectAsString = String.valueOf(object);
        if (StringUtils.isNotEmpty(context))
        {
            StringBuffer completeContext = new StringBuffer(StringUtils
                .trimToEmpty(objectAsString));
            completeContext.append(':' + context);
            object = completeContext.toString();
        }
        return String.valueOf(object);
    }

}