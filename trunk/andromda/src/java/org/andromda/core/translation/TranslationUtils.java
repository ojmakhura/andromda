package org.andromda.core.translation;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Contains translation utilities.
 * 
 * @author Chad Brandon
 */
public class TranslationUtils {
	
	private static Logger logger = Logger.getLogger(TranslationUtils.class);
	
	/**
	 * <p><code>TranslationUtils</code> instances should NOT be constructed in
	 * standard programming. Instead, the class should be used as
	 * <code>TranslationUtils.replacePattern(" some pattern ");</code>.</p>
	 *
	 * <p>This constructor is public to permit tools that require a JavaBean
	 * instance to operate.</p>
	 */
	public TranslationUtils() {}

	/**
	 * Searches for and replaces the specified pattern 
	 * with braces around it, like so --> "{pattern}" every time it
	 * occurs in the string.
	 * 
	 * @param string the string to to perform replacement on.
	 * @param pattern the pattern to find
	 * @param replaceWith the pattern to place the existing one with.
	 * @return String the string will all replacements
	 */
	public static String replacePattern(String string, String pattern, String replaceWith) {
		final String methodName = "TranslationUtils.replacePattern";
		if (string != null) {
			ExceptionUtils.checkNull(methodName, "pattern", pattern);
			ExceptionUtils.checkNull(methodName, "replaceWith", replaceWith);	
			string = StringUtils.replace(string, "{" + pattern + "}", replaceWith);
		}
		return string;
	}
    
    /**
     * Searches for and replaces the specified pattern 
     * with braces around it, like so --> "{pattern}" the first time
     * it occurs in the string
     * 
     * @param string the string to to perform replacement on.
     * @param pattern the pattern to find
     * @param replaceWith the pattern to place the existing one with.
     * @return String the string will all replacements
     */
    public static String replaceFirstPattern(String string, String pattern, String replaceWith) {
        final String methodName = "TranslationUtils.replacePattern";
        if (string != null) {
            ExceptionUtils.checkNull(methodName, "pattern", pattern);
            ExceptionUtils.checkNull(methodName, "replaceWith", replaceWith);   
            string = StringUtils.replaceOnce(string, "{" + pattern + "}", replaceWith);
        }
        return string;
    }
	
	/**
	 * Returns true if the specified pattern with braces around it, 
	 * like so --> "{pattern}" exists in the string.
	 * 
	 * @param string the string to to perform replacement on.
	 * @param pattern the pattern to find
	 * @return boolean true if the string contains the pattern, false otherwise
	 */
	public static boolean containsPattern(String string, String pattern) {
		boolean containsPattern = string != null && pattern != null;
		if (containsPattern) {
			containsPattern = StringUtils.contains(string, "{" + pattern + "}");
		}
		return containsPattern;
	}
	
	/**
	 * Calls the object's toString method and trims the value.
	 * Returns and empty string if null is given.
	 * 
	 * @param object the object to use.
	 * @return String
	 */
	public static String trimToEmpty(Object object) {
		return StringUtils.trimToEmpty(ObjectUtils.toString(object));
	}
    
    /**
     * Calls the object's toString method and deletes
     * any whitespace from the value.
     * Returns and empty string if null is given.
     * 
     * @param object the object to deleteWhite space from.
     * @return String
     */
    public static String deleteWhitespace(Object object) {
        return StringUtils.deleteWhitespace(ObjectUtils.toString(object));
    }
	
	/**
	 * Retrieves the "starting" property name from one
	 * that is nested, for example, will return '<name1>'
	 * from the string --> <name1>.<name2>.<name3>. If the property
	 * isn't nested, then just return the name that is passed in.
	 * 
	 * @param property the property.
	 * @return String
	 */
	public static String getStartingProperty(String property) {
		StringUtils.trimToEmpty(property);
		int dotIndex = property.indexOf('.');
		if (dotIndex != -1) {
			property = property.substring(0, dotIndex);
		}
		return property;
	}
	
	/**
	 * Removes any extra whitepace --> does not remove the
	 * spaces between the words. Only removes
	 * tabs and newline characters.  This is to allow everything
	 * to be on one line while keeping the spaces between words.
	 * @param string
	 * @return String the string with the removed extra spaces.
	 */
	public static String removeExtraWhitespace(String string) {
		//first make the string an empty string if it happens to be null
		string = StringUtils.trimToEmpty(string);
		
		//remove anything that is greater than 1 space.
		string = string.replaceAll("\\s{2,}", " ");
		
		return string;
	}
	
	/**
	 * Just retriieves properties from a bean, but gives
	 * a more informational error when the property can't
	 * be retrieved, it also cleans the resulting property
	 * from any excess white space
	 * @param bean the bean from which to retrieve the property
	 * @param property the property name
	 * @return Object the value of the property
	 */
	public static Object getProperty(Object bean, String property) {
		final String methodName = "TranslationUtils.getProperty";
		try {
			Object value = PropertyUtils.getProperty(bean, property);	
			return value;
		}catch(Exception ex) {
			String errMsg = "Error performing " + methodName
				+ " with bean '" + bean + "' and property '" + property + "'";
			logger.error(errMsg, ex);
			throw new TranslatorException(errMsg, ex);
		}
	}
    
    /**
     * Returns true/false on whether or not the give <code>bean</code>
     * has the specified <code>property</code>.
     * @param bean the bean to check for the property.
     * @param property the property to check the existence of.
     * @return boolean
     */
    public static boolean hasProperty(Object bean, String property) {
        return PropertyUtils.isReadable(bean, property);
    }
    
    /**
     * Just retriieves properties from a bean, but gives
     * a more informational error when the property can't
     * be retrieved, it also cleans the resulting property
     * from any excess white space
     * @param bean the bean from which to retrieve the property
     * @param property the property name
     * @return Object the value of the property
     */
    public static String getPropertyAsString(Object bean, String property) {
        return TranslationUtils.trimToEmpty(
            TranslationUtils.getProperty(bean, property));
    }

}
