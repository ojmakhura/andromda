package org.andromda.core.metafacade;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Contains static utility methods for
 * dealing with MetafacadeMappings instances.
 */
class MetafacadeMappingsUtils {
	
	private static Logger logger = Logger.getLogger(MetafacadeMappingsUtils.class);

	/**
	 * Constructs the unique key format expected for this mapping. Note
	 * that the only argument required is the <code>metaobjectClass</code>,
	 * <code>suffixe</code> is optional.
	 * 
	 * @param metaobjectClass the begining of the ky
	 * @param suffix the suffix to append
	 * 
	 * @return String.
	 */
	protected static String constructKey(String metaobjectClass, String suffix) {
		final String methodName = "MetafacadeMapping.constructKey";
		ExceptionUtils.checkEmpty(methodName, "metaobjectClass", metaobjectClass);
		String key = metaobjectClass;
		suffix = StringUtils.trimToEmpty(suffix);
		if (StringUtils.isNotEmpty(suffix)) {
			key = appendContext(key, suffix);
		}
		
		if (logger.isDebugEnabled()) 
			logger.debug("completed '" 
				+ methodName 
				+ "' with key --> '" 
				+ key + "'");
		return key.toString();
	}
	
	/**
	 * Appends a context suffix to the <code>string</code>.  
	 * The context suffix is ':' + <code>suffix</code> argument.
	 * If suffix is null or an empty string, then <code>string</code>
	 * will just be returned unchanged.
	 * 
	 * @param context the context which we'll be appending too.
	 * @param suffix the suffix to append.
	 * @return the new context name.
	 */
	protected static String appendContext(String string, String context) {
		ExceptionUtils.checkEmpty(
			"MetafacadeMappingsUtils.appendContextSuffix", 
			"string", 
			string);
		if (StringUtils.isNotEmpty(context)) {
			StringBuffer completeContext = 
				new StringBuffer(StringUtils.trimToEmpty(string));
			completeContext.append(':' + context);
			string = completeContext.toString();
		}
		return string;
	}
	
}
