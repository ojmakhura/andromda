package org.andromda.metafacades.uml14;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.lang.StringUtils;

/**
 * Utilities for dealing with entity metafacades
 *
 * @author Chad Brandon
 */
public class EntityMetafacadeUtils {

    /**
     * <p>Converts a string following the Java naming conventions to a
     * database attribute name.  For example convert customerName to
     * CUSTOMER_NAME.</p>
     *
     * @param modelElementName the string to convert
     * @param separator character used to separate words
     * @return string converted to database attribute format
     */
	protected static String toSqlName(String modelElementName, String separator) {
		final String methodName = "EntityFacadeUtils.toSqlName";
		ExceptionUtils.checkEmpty(
			methodName,
			"string",
			modelElementName);
		ExceptionUtils.checkEmpty(
			methodName,
			"separator",
			separator);

	    StringBuffer databaseAttributeName = new StringBuffer();
	    StringCharacterIterator iter = new StringCharacterIterator(
	            StringUtils.uncapitalize(modelElementName));

	    for (char character = iter.first(); character != CharacterIterator.DONE;
	            character = iter.next()) {

	        if (Character.isUpperCase(character)) {
	            databaseAttributeName.append(separator);
	        }

	        character = Character.toUpperCase(character);
	        databaseAttributeName.append(character);
	    }

	    return databaseAttributeName.toString();
	}

	/**
	 * Gets the SQL name. (i.e. column name, table name, etc.).
	 * If it can't find the corresponding tagged value with
	 * the specified <code>name</code>, then it uses the element
	 * name by default and just returns that.
	 *
	 * @param element from which to retrieve the SQL name.
	 * @param name the name of the tagged value.
	 * @param nameMaxLength if this is not null, then the name returned
	 *        will be trimmed to this length (if it happens to be longer).
	 * @return the SQL name as a String.
	 */
	protected static String getSqlNameFromTaggedValue(
			ModelElementFacade element,
			String name,
			Short nameMaxLength) {
		return getSqlNameFromTaggedValue(element, name, nameMaxLength, null);
	}

	/**
	 * Gets the SQL name. (i.e. column name, table name, etc.).
	 * If it can't find the corresponding tagged value with
	 * the specified <code>name</code>, then it uses the element
	 * name by default and just returns that.
	 *
	 * @param element from which to retrieve the SQL name.
	 * @param name the name of the tagged value.
	 * @param nameMaxLength if this is not null, then the name returned
	 *        will be trimmed to this length (if it happens to be longer).
	 * @param suffix the optional suffix to add to the sql name (i.e. foreign key
	 *        suffix, etc.)
	 * @return the SQL name as a String.
	 */
	protected static String getSqlNameFromTaggedValue(
		ModelElementFacade element,
		String name,
		Short nameMaxLength,
		String suffix) {
		if (element != null) {
            Object value = element.findTaggedValue(name);
			name = StringUtils.trimToEmpty((value==null)?null:value.toString());
			if (StringUtils.isEmpty(name)) {
				//if we can't find the tagValue then use the
				//element name for the name
				name = element.getName();
				name = toSqlName(name, "_");
				if (StringUtils.isNotEmpty(suffix)) {
					name = name + suffix;
				}
			}
			name = ensureMaximumNameLength(name, nameMaxLength);
		}
		return name;
	}

	/**
	 * <p>Trims the passed in value to the maximum name length.</p>
	 * If no maximum length has been set then this method does nothing.
	 *
	 * @param name the name length to check and trim if necessary
	 * @param nameMaxLength if this is not null, then the name returned
	 *        will be trimmed to this length (if it happens to be longer).
	 * @return String the string to be used as SQL type
	 */
	public static String ensureMaximumNameLength(String name, Short nameMaxLength) {
		if (StringUtils.isNotEmpty(name) && nameMaxLength != null) {
			short max = nameMaxLength.shortValue();
			if (name.length() > max) {
				name = name.substring(0, max);
			}
		}
		return name;
	}

}
