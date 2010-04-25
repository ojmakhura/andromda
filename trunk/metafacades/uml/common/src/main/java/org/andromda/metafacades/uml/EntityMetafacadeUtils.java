package org.andromda.metafacades.uml;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Utilities for dealing with entity metafacades
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public class EntityMetafacadeUtils
{
    /**
     * <p/> Converts a string following the Java naming conventions to a
     * database attribute name. For example convert customerName to
     * CUSTOMER_NAME.
     * </p>
     *
     * @param modelElementName the string to convert
     * @param separator character used to separate words
     * @return string converted to database attribute format
     */
    public static String toSqlName(
        String modelElementName,
        Object separator)
    {
        ExceptionUtils.checkEmpty(
            "modelElementName",
            modelElementName);

        StringBuilder sqlName = new StringBuilder();
        StringCharacterIterator iterator = new StringCharacterIterator(StringUtils.uncapitalize(modelElementName));

        for (char character = iterator.first(); character != CharacterIterator.DONE; character = iterator.next())
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
     * Gets the SQL name. (i.e. column name, table name, etc.). If it can't find
     * the corresponding tagged value with the specified <code>name</code>,
     * then it uses the element name by default and just returns that.
     *
     * @param prefix the optional prefix to add to the sql name (i.e. table name
     *        prefix, etc.).
     * @param element from which to retrieve the SQL name.
     * @param name the name of the tagged value.
     * @param nameMaxLength if this is not null, then the name returned will be
     *        trimmed to this length (if it happens to be longer).
     * @param separator character used to separate words
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(
        String prefix,
        ModelElementFacade element,
        String name,
        Short nameMaxLength,
        Object separator)
    {
        return getSqlNameFromTaggedValue(
            prefix,
            element,
            name,
            nameMaxLength,
            null,
            separator);
    }

    /**
     * Gets the SQL name. (i.e. column name, table name, etc.). If it can't find
     * the corresponding tagged value with the specified <code>name</code>,
     * then it uses the element name by default and just returns that.
     *
     * @param element from which to retrieve the SQL name.
     * @param name the name of the tagged value.
     * @param nameMaxLength if this is not null, then the name returned will be
     *        trimmed to this length (if it happens to be longer).
     * @param suffix the optional suffix to add to the sql name (i.e. foreign
     *        key suffix, etc.)
     * @param separator character used to separate words
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(
        ModelElementFacade element,
        String name,
        Short nameMaxLength,
        String suffix,
        Object separator)
    {
        return getSqlNameFromTaggedValue(
            null,
            element,
            name,
            nameMaxLength,
            suffix,
            separator);
    }

    /**
     * Gets the SQL name. (i.e. column name, table name, etc.). If it can't find
     * the corresponding tagged value with the specified <code>name</code>,
     * then it uses the element name by default and just returns that.
     *
     * @param element from which to retrieve the SQL name.
     * @param name the name of the tagged value.
     * @param nameMaxLength if this is not null, then the name returned will be
     *        trimmed to this length (if it happens to be longer).
     * @param separator character used to separate words
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(
        ModelElementFacade element,
        String name,
        Short nameMaxLength,
        Object separator)
    {
        return getSqlNameFromTaggedValue(
            null,
            element,
            name,
            nameMaxLength,
            null,
            separator);
    }

    /**
     * Gets the SQL name. (i.e. column name, table name, etc.). If it can't find
     * the corresponding tagged value with the specified <code>name</code>,
     * then it uses the element name by default and just returns that.
     *
     * @param prefix the optional prefix to add to the sql name (i.e. table name
     *        prefix, etc.).
     * @param element from which to retrieve the SQL name.
     * @param name the name of the tagged value.
     * @param nameMaxLength if this is not null, then the name returned will be
     *        trimmed to this length (if it happens to be longer).
     * @param suffix the optional suffix to add to the sql name (i.e. foreign
     *        key suffix, etc.)
     * @param separator character used to separate words
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(
        String prefix,
        final ModelElementFacade element,
        String name,
        final Short nameMaxLength,
        String suffix,
        final Object separator)
    {
        if (element != null)
        {
            Object value = element.findTaggedValue(name);
            StringBuilder buffer = new StringBuilder(StringUtils.trimToEmpty((String)value));
            if (StringUtils.isEmpty(buffer.toString()))
            {
                // if we can't find the tagValue then use the
                // element name for the name
                buffer = new StringBuilder(toSqlName(
                            element.getName(),
                            separator));
                suffix = StringUtils.trimToEmpty(suffix);
                prefix = StringUtils.trimToEmpty(prefix);
                if (nameMaxLength != null)
                {
                    final short maxLength = (short)(nameMaxLength.shortValue() - suffix.length() - prefix.length());
                    buffer =
                        new StringBuilder(
                            EntityMetafacadeUtils.ensureMaximumNameLength(
                                buffer.toString(),
                                Short.valueOf(maxLength)));
                }
                if (StringUtils.isNotBlank(prefix))
                {
                    buffer.insert(
                        0,
                        prefix);
                }
                if (StringUtils.isNotBlank(suffix))
                {
                    buffer.append(suffix);
                }
            }
            name = buffer.toString();
        }
        return name;
    }

    /**
     * <p/> Trims the passed in value to the maximum name length.
     * </p>
     * If no maximum length has been set then this method does nothing.
     *
     * @param name the name length to check and trim if necessary
     * @param nameMaxLength if this is not null, then the name returned will be
     *        trimmed to this length (if it happens to be longer).
     * @return String the string to be used as SQL type
     */
    public static String ensureMaximumNameLength(
        String name,
        Short nameMaxLength)
    {
        if (StringUtils.isNotEmpty(name) && nameMaxLength != null)
        {
            short max = nameMaxLength.shortValue();
            if (name.length() > max)
            {
                name = name.substring(
                        0,
                        max);
            }
        }
        return name;
    }

    /**
     * Gets all identifiers for an entity. If 'follow' is true, and if
     * no identifiers can be found on the entity, a search up the
     * inheritance chain will be performed, and the identifiers from
     * the first super class having them will be used.   If no
     * identifiers exist, a default identifier will be created if the
     * allowDefaultIdentifiers property is set to true.
     *
     * @param entity the entity for which to retrieve the identifiers
     * @param follow a flag indicating whether or not the inheritance hierarchy
     *        should be followed
     * @return the collection of identifiers.
     */
    public static Collection<AttributeFacade> getIdentifiers(
        final Entity entity,
        final boolean follow)
    {
        final Collection<AttributeFacade> identifiers = new ArrayList<AttributeFacade>(entity.getAttributes());
        MetafacadeUtils.filterByStereotype(
            identifiers,
            UMLProfile.STEREOTYPE_IDENTIFIER);

        return (identifiers.isEmpty() && follow && entity.getGeneralization() instanceof Entity
            ? getIdentifiers((Entity)entity.getGeneralization(), follow)
            : identifiers);
    }

    /**
     * Constructs a sql type name from the given <code>mappedName</code> and
     * <code>columnLength</code>.
     *
     * @param typeName the actual type name (usually retrieved from a mappings
     *        file, ie NUMBER(19).
     * @param columnLength the length of the column.
     * @return the new name co
     */
    public static String constructSqlTypeName(
        final String typeName,
        final String columnLength)
    {
        String value = typeName;
        if (StringUtils.isNotEmpty(typeName))
        {
            final char beginChar = '(';
            final char endChar = ')';
            final int beginIndex = value.indexOf(beginChar);
            final int endIndex = value.indexOf(endChar);
            if (beginIndex != -1 && endIndex != -1 && endIndex > beginIndex)
            {
                String replacement = value.substring(
                        beginIndex,
                        endIndex) + endChar;
                value = StringUtils.replace(
                        value,
                        replacement,
                        beginChar + columnLength + endChar);
            }
            else
            {
                value = value + beginChar + columnLength + endChar;
            }
        }
        return value;
    }

    /**
     * Constructs and returns the foreign key constraint name for the given <code>associationEnd</code>, <code>suffix</code>, <code>sqlNameSeperator</code>
     * and <code>maxLengthProperty</code>.
     *
     * @param associationEnd the association end for which to construct the constraint name.
     * @param suffix the suffix appended to the constraint name (if not limited by length).
     * @param sqlNameSeperator the SQL name separator to use (i.e. '_').
     * @param maxLengthProperty the numeric value stored as a string indicating the max length the constraint may be.
     * @return the constructed foreign key constraint name.
     */
    public static String getForeignKeyConstraintName(EntityAssociationEnd associationEnd, String suffix, String sqlNameSeperator, String maxLengthProperty)
    {
        String constraintName;

        final Object taggedValueObject = associationEnd.findTaggedValue(
                UMLProfile.TAGGEDVALUE_PERSISTENCE_FOREIGN_KEY_CONSTRAINT_NAME);
        if (taggedValueObject == null)
        {
            // we construct our own foreign key constraint name here
            StringBuilder buffer = new StringBuilder();

            final ClassifierFacade type = associationEnd.getOtherEnd().getType();
            if (type instanceof Entity)
            {
                Entity entity = (Entity)type;
                buffer.append(entity.getTableName());
            }
            else
            {
                // should not happen
                buffer.append(type.getName().toUpperCase());
            }

            buffer.append(sqlNameSeperator);
            buffer.append(associationEnd.getColumnName());
            constraintName = buffer.toString();

            // we take into consideration the maximum length allowed
            final short maxLength = (short)(Short.valueOf(maxLengthProperty).shortValue() - suffix.length());
            buffer = new StringBuilder(EntityMetafacadeUtils.ensureMaximumNameLength(constraintName, Short.valueOf(maxLength)));
            buffer.append(suffix);
            constraintName = EntityMetafacadeUtils.getUniqueForeignKeyConstraintName(buffer.toString());
        }
        else
        {
            // use the tagged value
            constraintName = taggedValueObject.toString();
        }
        return constraintName;
    }

    /**
     * An internal static cache for foreign key names (allows us to keep track
     * of which ones have been used).  Its not great that its static, but for now
     * this is the easiest way to enforce this.
     */
    private static Collection<String> foreignKeyConstraintNameCache = new ArrayList<String>();

    /**
     * Retrieves a unique foreign key constraint name given the proposedName.  Compares the proposedName
     * against any foreign key names already stored in an internal collection.
     *
     * @param proposedName the proposed foreign key name.
     * @return the unique foreign key name.
     */
    private static String getUniqueForeignKeyConstraintName(String proposedName)
    {
        final char[] characters = proposedName.toCharArray();
        int numericValue = 0;
        for (int ctr = 0; ctr < characters.length; ctr++)
        {
            numericValue = numericValue + Character.getNumericValue(characters[0]);
        }
        return getUniqueForeignKeyConstraintName(proposedName, new Random(numericValue));
    }

    /**
     * Retrieves a unique foreign key constraint name given the proposedName.  Compares the proposedName
     * against any foreign key names already stored in an internal collection.
     *
     * @param proposedName the proposed foreign key name.
     * @param random the Random number generator to use for enforcing uniqueness.
     * @return the unique foreign key name.
     */
    private static String getUniqueForeignKeyConstraintName(String proposedName, final Random random)
    {
        String name;
        if (foreignKeyConstraintNameCache.contains(proposedName))
        {
            final char[] characters = proposedName.toCharArray();
            int randomInt = random.nextInt(characters.length);
            char randomChar = Character.toUpperCase(characters[randomInt]);
            proposedName = proposedName.substring(0, proposedName.length() - 1) + randomChar;
            name = getUniqueForeignKeyConstraintName(proposedName, random);
        }
        else
        {
            name = proposedName;
            foreignKeyConstraintNameCache.add(name);
        }
        return name;
    }

    /**
     * Clears out the foreign key cache.
     */
    public static void clearForeignKeyConstraintNameCache()
    {
        foreignKeyConstraintNameCache.clear();
    }
}