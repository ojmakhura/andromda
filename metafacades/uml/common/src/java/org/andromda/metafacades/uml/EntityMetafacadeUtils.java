package org.andromda.metafacades.uml;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Collection;

/**
 * Utilities for dealing with entity metafacades
 *
 * @author Chad Brandon
 */
public class EntityMetafacadeUtils
{    
    /**
     * <p/>
     * Converts a string following the Java naming conventions to a database attribute name. For example convert
     * customerName to CUSTOMER_NAME. </p>
     *
     * @param modelElementName the string to convert
     * @param separator        character used to separate words
     * @return string converted to database attribute format
     */
    public static String toSqlName(String modelElementName, Object separator)
    {
        final String methodName = "EntityMetaFacadeUtils.toSqlName";
        ExceptionUtils.checkEmpty(methodName, "modelElementName", modelElementName);

        StringBuffer sqlName = new StringBuffer();
        StringCharacterIterator iter = new StringCharacterIterator(StringUtils.uncapitalize(modelElementName));

        for (char character = iter.first(); character != CharacterIterator.DONE; character = iter.next())
        {
            if (Character.isUpperCase(character))
            {
                sqlName.append(separator);
            }
            character = Character.toUpperCase(character);
            sqlName.append(character);
        }
        return StringEscapeUtils.escapeSql(sqlName.toString());
    }

    /**
     * Gets the SQL name. (i.e. column name, table name, etc.). If it can't find the corresponding tagged value with the
     * specified <code>name</code>, then it uses the element name by default and just returns that.
     *
     * @param prefix        the optional prefix to add to the sql name (i.e. table name prefix, etc.).
     * @param element       from which to retrieve the SQL name.
     * @param name          the name of the tagged value.
     * @param nameMaxLength if this is not null, then the name returned will be trimmed to this length (if it happens to
     *                      be longer).
     * @param separator     character used to separate words
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(String prefix, ModelElementFacade element, String name,
                                                   Short nameMaxLength, Object separator)
    {
        return getSqlNameFromTaggedValue(prefix, element, name, nameMaxLength, null, separator);
    }

    /**
     * Gets the SQL name. (i.e. column name, table name, etc.). If it can't find the corresponding tagged value with the
     * specified <code>name</code>, then it uses the element name by default and just returns that.
     *
     * @param element       from which to retrieve the SQL name.
     * @param name          the name of the tagged value.
     * @param nameMaxLength if this is not null, then the name returned will be trimmed to this length (if it happens to
     *                      be longer).
     * @param suffix        the optional suffix to add to the sql name (i.e. foreign key suffix, etc.)
     * @param separator     character used to separate words
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(ModelElementFacade element, String name, Short nameMaxLength,
                                                   String suffix, Object separator)
    {
        return getSqlNameFromTaggedValue(null, element, name, nameMaxLength, suffix, separator);
    }

    /**
     * Gets the SQL name. (i.e. column name, table name, etc.). If it can't find the corresponding tagged value with the
     * specified <code>name</code>, then it uses the element name by default and just returns that.
     *
     * @param element       from which to retrieve the SQL name.
     * @param name          the name of the tagged value.
     * @param nameMaxLength if this is not null, then the name returned will be trimmed to this length (if it happens to
     *                      be longer).
     * @param separator     character used to separate words
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(ModelElementFacade element, String name, Short nameMaxLength,
                                                   Object separator)
    {
        return getSqlNameFromTaggedValue(null, element, name, nameMaxLength, null, separator);
    }

    /**
     * Gets the SQL name. (i.e. column name, table name, etc.). If it can't find the corresponding tagged value with the
     * specified <code>name</code>, then it uses the element name by default and just returns that.
     *
     * @param prefix        the optional prefix to add to the sql name (i.e. table name prefix, etc.).
     * @param element       from which to retrieve the SQL name.
     * @param name          the name of the tagged value.
     * @param nameMaxLength if this is not null, then the name returned will be trimmed to this length (if it happens to
     *                      be longer).
     * @param suffix        the optional suffix to add to the sql name (i.e. foreign key suffix, etc.)
     * @param separator     character used to separate words
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(String prefix, ModelElementFacade element, String name,
                                                   Short nameMaxLength, String suffix, Object separator)
    {
        if (element != null)
        {
            Object value = element.findTaggedValue(name);
            name = StringUtils.trimToEmpty((String)value);
            if (StringUtils.isEmpty(name))
            {
                //if we can't find the tagValue then use the
                //element name for the name
                name = element.getName();
                name = toSqlName(name, separator);
                if (StringUtils.isNotBlank(prefix))
                {
                    name = StringUtils.trimToEmpty(prefix) + name;
                }
                if (StringUtils.isNotBlank(suffix))
                {
                    name = name + StringUtils.trimToEmpty(suffix);
                }
            }
            name = ensureMaximumNameLength(name, nameMaxLength);
        }
        return name;
    }

    /**
     * <p/>
     * Trims the passed in value to the maximum name length. </p> If no maximum length has been set then this method
     * does nothing.
     *
     * @param name          the name length to check and trim if necessary
     * @param nameMaxLength if this is not null, then the name returned will be trimmed to this length (if it happens to
     *                      be longer).
     * @return String the string to be used as SQL type
     */
    public static String ensureMaximumNameLength(String name, Short nameMaxLength)
    {
        if (StringUtils.isNotEmpty(name) && nameMaxLength != null)
        {
            short max = nameMaxLength.shortValue();
            if (name.length() > max)
            {
                name = name.substring(0, max);
            }
        }
        return name;
    }

    /**
     * Retrieves the identifiers for the given <code>entity</code>.  If <code>follow</code> is true then the inheritance
     * hierachy will also be searched.
     *
     * @param follow a flag indicating whether or not the inheritance hiearchy should be followed
     * @return the collection of identifiers.
     */
    public static Collection getIdentifiers(Entity entity, boolean follow)
    {
        Collection identifiers = entity.getAttributes();
        MetafacadeUtils.filterByStereotype(identifiers, UMLProfile.STEREOTYPE_IDENTIFIER);

        for (ClassifierFacade superClass = (ClassifierFacade)entity.getGeneralization(); superClass != null &&
                identifiers.isEmpty() && follow; superClass = (ClassifierFacade)superClass.getGeneralization())
        {
            if (superClass.hasStereotype(UMLProfile.STEREOTYPE_ENTITY))
            {
                Entity facade = (Entity)superClass;
                identifiers.addAll(facade.getIdentifiers(follow));
            }
        }
        return identifiers;
    }
    
    /**
     * Constructs a sql type name from the given <code>mappedName</code>
     * and <code>columnLength</code>.
     * 
     * @param typeName the actual type name (usually retrieved from a mappings file, ie
     *                 NUMBER(19).
     * @param columnLength the length of the column.
     * @return the new name co
     */
    public static String constructSqlTypeName(final String typeName, final String columnLength)
    {
        String value = typeName;
        if (StringUtils.isNotEmpty(typeName))
        {
            char beginChar = '(';
            char endChar = ')';
            int beginIndex = value.indexOf(beginChar);
            int endIndex = value.indexOf(endChar);
            if (beginIndex != -1 && endIndex != -1 && endIndex > beginIndex)
            {
                String replacement = value.substring(beginIndex, endIndex) + endChar;
                value = StringUtils.replace(value, replacement, beginChar + columnLength + endChar);
            }
            else
            {
                value = value + beginChar + columnLength + endChar;
            }
        }
        return value;
    }
}