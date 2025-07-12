package org.andromda.metafacades.uml;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Utilities for dealing with entity metafacades
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public class EntityMetafacadeUtils
{
    /**
     * The logger instance.
     */
    private static final Logger LOGGER = Logger.getLogger(EntityMetafacadeUtils.class);

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
        if (sqlName.toString() == null) {
            return null;
        }
        return StringUtils.replace(sqlName.toString(), "'", "''");
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
     * @param shortenSqlNamesMethod Method of shortening SQL Names, i.e. removeVowels
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(
        String prefix,
        ModelElementFacade element,
        String name,
        Short nameMaxLength,
        Object separator,
        Object shortenSqlNamesMethod)
    {
        return getSqlNameFromTaggedValue(
            prefix,
            element,
            name,
            nameMaxLength,
            null,
            separator,
            shortenSqlNamesMethod);
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
     * @param shortenSqlNamesMethod Method of shortening SQL Names, i.e. removeVowels
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(
        ModelElementFacade element,
        String name,
        Short nameMaxLength,
        String suffix,
        Object separator,
        Object shortenSqlNamesMethod)
    {
        return getSqlNameFromTaggedValue(
            null,
            element,
            name,
            nameMaxLength,
            suffix,
            separator,
            shortenSqlNamesMethod);
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
     * @param shortenSqlNamesMethod Method of shortening SQL Names, i.e. removeVowels
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(
        ModelElementFacade element,
        String name,
        Short nameMaxLength,
        Object separator,
        Object shortenSqlNamesMethod)
    {
        return getSqlNameFromTaggedValue(
            null,
            element,
            name,
            nameMaxLength,
            null,
            separator,
            shortenSqlNamesMethod);
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
     * @param shortenSqlNamesMethod Method of shortening SQL Names, i.e. removeVowels
     * @return the SQL name as a String.
     */
    public static String getSqlNameFromTaggedValue(
        String prefix,
        final ModelElementFacade element,
        String name,
        final Short nameMaxLength,
        String suffix,
        final Object separator,
        final Object shortenSqlNamesMethod)
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
                                Short.valueOf(maxLength),
                                (String)shortenSqlNamesMethod));
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

    private static List<Entity> sortedEntities;
    /**
     * Puts non-abstract entities in order based on associations. To be used in constructors and
     * tests so that entities may be created and deleted in the proper order, without
     * violating key constraints in the database
     *
     * @param entities the entity list to be sorted.
     * @param ascending true for entities with no associations first.
     * @return the sorted entity list.
     */
    public static List<Entity> sortEntities(
        final Collection<Entity> entities,
        boolean ascending)
    {
        // Initially holds entities with no outgoing relations. Add related entities to the end
        // Multiple andromda invocations - entity list is cached - check size
        List<Entity> sorted = new ArrayList<Entity>();
        if (sortedEntities==null)
        {
            sortedEntities = new ArrayList<Entity>();
        }
        if (entities == null || entities.isEmpty() ||
            (sortedEntities!=null && !sortedEntities.isEmpty() && sortedEntities.size() == entities.size()
            && sortedEntities.contains(entities.iterator().next())))
        {
            sorted = sortedEntities;
        }
        else
        {
            // Clear left-over entities from last time.
            sortedEntities.clear();
            // Move entities into the sorted list step by step
            List<Entity> toBeMoved = new ArrayList<Entity>();
            List<Entity> unsorted = new ArrayList<Entity>();
            for (Entity entity : entities)
            {
             // Remove abstract entities from the list: We won't be testing or creating abstract entities
                if (entity.isAbstract())
                {
                    toBeMoved.add(entity);
                }
                else
                {
                    //Collection<AssociationEndFacade> ends = entity.getNavigableConnectingEnds();
                    // Put the entities with no associations first in the sorted list
                    if (entity.getNavigableConnectingEnds().size()==0)
                    {
                        sorted.add(entity);
                    }
                    else
                    {
                        unsorted.add(entity);
                    }
                }
            }
            // Prevent ConcurrentModificationException by using a temp removal list
            for (Entity entity : toBeMoved)
            {
                entities.remove(entity);
            }
            /*// Holds entities with relations after first pass. Second pass sorts the entities
            for (Entity entity : entities)
            {
                Collection<AssociationEndFacade> ends = entity.getNavigableConnectingEnds();
                // Put the entities with no associations first in the sorted list
                if (ends.size()==0)
                {
                    sorted.add(entity);
                }
                else
                {
                    unsorted.add(entity);
                }
            }*/
            /*for (Entity entity : sorted)
            {
            }
            String moved = " ToBeMoved: ";
            for (Entity entity : unsorted)
            {
                moved += entity.getName() + " ";
            }
            */
            // Work backwards from unsorted list, moving entities to sorted list until none remain
            // Since all associations must be owned by one side, all will eventually be moved to sorted list
            int moveCount = 1; // Stop looping if no more to be moved, prevent infinite loop on circular relationships
            while (unsorted.size() > 0 && moveCount > 0)
            {
                toBeMoved = new ArrayList<Entity>();
                for (Entity entity : unsorted)
                {
                    Collection<AssociationEndFacade> ends = entity.getNavigableConnectingEnds();
                    // See if any navigable connecting ends are not owned by this entity
                    boolean createFirst = true;
                    for (AssociationEndFacade end : ends)
                    {
                        ClassifierFacade entityEnd = end.getType();
                        // Owning relations are sorted after entities on the opposite end
                        int referencedPosition = unsorted.lastIndexOf(entityEnd);
                        if (referencedPosition > -1 && UMLMetafacadeUtils.isOwningEnd(end) && !entityEnd.getFullyQualifiedName().equals(entity.getFullyQualifiedName()))
                        {
                            // Other Entity side of an owned relationship must be created first
                            createFirst = false;
                            break;
                        }
                    }
                    if (createFirst)
                    {
                        toBeMoved.add(entity);
                    }
                }
                moveCount = toBeMoved.size();
                /*moved = " ToBeMoved: ";
                for (Entity entity : toBeMoved)
                {
                    moved += entity.getName() + " ";
                }*/
                for (Entity entity : toBeMoved)
                {
                    unsorted.remove(entity);
                    sorted.add(entity);
                }
            }
            if (moveCount==0 && unsorted.size() > 0)
            {
                // There are unresolvable circular relationships
                String circular = "Circular relationships between Entities:";
                for (Entity entity : unsorted)
                {
                    circular += " " + entity.getName();
                }
                LOGGER.error(circular);
                // Add them to the sorted list anyway and hope for the best...
            }
            /*int moves = -1;
            for (Entity entity : unsorted)
            {
                if (!entity.isAbstract())
                {
                    Collection<AssociationEndFacade> ends = entity.getAssociationEnds();
                    // Test each association to see if incoming or outgoing. sort outgoing before incoming.
                    for (AssociationEndFacade end : ends)
                    {
                        AssociationEndFacade otherEnd = end.getOtherEnd();
                        if (otherEnd.getType() instanceof Entity)
                        {
                            // otherEnd is actually the association/type on this entity
                            Entity entityEnd = (Entity)otherEnd.getType();
                            // Incoming relations are sorted after other entities
                            // Aggregation and Composition always owns the end
                            // One to Many association many end comes last
                            int thisPosition = unsorted.lastIndexOf(entity);
                            int referencedPosition = unsorted.lastIndexOf(entityEnd);
                            // Determine if this end is the relationship owner
                            //boolean primary = BooleanUtils.toBoolean(
                            //    ObjectUtils.toString(end.findTaggedValue("andromda_persistence_associationEnd_primary")));
                            // This owning end should be created after the other side Entity
                            if (thisPosition > -1 && referencedPosition > -1)
                            {
                                if (isOwningEnd(otherEnd) && thisPosition < referencedPosition)
                                {
                                    // Move the locations of the two List entries if referenced entity is higher in the list
                                    // Avoid ConcurrentModificationEx by operating on temp List
                                    unsorted.remove(entityEnd);
                                    unsorted.add(unsorted.lastIndexOf(entity), entityEnd);
                                    moves += 1;
                                }
                                else if (!isOwningEnd(otherEnd) && thisPosition > referencedPosition)
                                {
                                    // Move the locations of the two List entries if referenced entity is higher in the list
                                    unsorted.remove(end);
                                    unsorted.add(unsorted.lastIndexOf(entityEnd), entity);
                                    moves += 1;
                                }
                            }
                        }
                    }
                }
            }*/
            sorted.addAll(unsorted);
            sortedEntities = sorted;
        }
        if (!ascending)
        {
            Collections.reverse(sorted);
        }
        return sorted;
    }

    /**
     * Gets the execution priority of a specific entity, based on dependency/owning relationships,
     * so that TestNG unit tests can be put in the proper order across all entities and CRUD methods
     *
     * @param entity the entity to be prioritized.
     * @return int the entity priority.
     */
    public static int getPriority(
        final Entity entity)
    {
        int priority = 0;
        if (sortedEntities!=null && !sortedEntities.isEmpty())
        {
            priority = sortedEntities.indexOf(entity);
            if (priority < 1)
            {
                priority = 0;
            }
            else
            {
                // Change from zero based to one based, allow 10 additional test methods between the standard tests.
                priority = priority*10 + 10;
            }
        }
        return priority;
    }

    /**
     * <p/> Trims the passed in value to the maximum name length.
     * </p>
     * If no maximum length has been set then this method does nothing.
     *
     * @param name the name length to check and trim if necessary
     * @param nameMaxLength if this is not null, then the name returned will be
     *        trimmed to this length (if it happens to be longer).
     * @param method Method of shortening SQL Names, i.e. removeVowels
     * @return String the string to be used as SQL type
     */
    public static String ensureMaximumNameLength(
        String name,
        Short nameMaxLength,
        String method)
    {
        if (StringUtils.isNotBlank(name) && nameMaxLength != null)
        {
            short max = nameMaxLength.shortValue();
            if(method != null && method.equalsIgnoreCase(UMLMetafacadeProperties.SHORTEN_SQL_NAMES_METHOD_REMOVE_VOWELS))
            {
                while(name.length() > max)
                {
                    final String[] vowels = new String[]{"A","E","I","O","U","a","e","i","o","u"};
                    final int lastVowelPos = StringUtils.lastIndexOfAny(name, vowels);
                    if(lastVowelPos == -1)
                    {
                        break; //no more vowels
                    }
                    else
                    {
                        name = name.substring(0,lastVowelPos)+name.substring(lastVowelPos+1);
                    }
                }
            }

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
     * @return the collection of entity identifier attributes.
     */
    public static Collection<ModelElementFacade> getIdentifiers(
        final Entity entity,
        final boolean follow)
    {
        //final Collection<ModelElementFacade> properties = entity.getAllProperties();
        final Collection<ModelElementFacade> identifiers = new ArrayList<ModelElementFacade>();
        // TODO Entity.getAttributes returns List<? extends AttributeFacade>, currently unchecked conversion
        final Collection<AttributeFacade> attributes = new ArrayList<AttributeFacade>(entity.getAttributes());
        MetafacadeUtils.filterByStereotype(
            attributes,
            UMLProfile.STEREOTYPE_IDENTIFIER);
        identifiers.addAll(attributes);
        final Collection<AssociationEndFacade> associations = new ArrayList<AssociationEndFacade>(entity.getNavigableConnectingEnds(follow));
        MetafacadeUtils.filterByStereotype(
                associations,
                UMLProfile.STEREOTYPE_IDENTIFIER);
        identifiers.addAll(associations);
        /*MetafacadeUtils.filterByStereotype(
                properties,
                UMLProfile.STEREOTYPE_IDENTIFIER);*/
        /*// Find identifiers of association identifiers otherEnd
        for (AssociationEndFacade associationEnd : associations)
        {
            ClassifierFacade classifier = associationEnd.getType();
            if (classifier instanceof Entity)
            {
                Collection<ModelElementFacade> entityIdentifiers = getIdentifiers((Entity)classifier, true);
                identifiers.addAll(entityIdentifiers);
            }
        } */
        // Reorder join columns if order is specified - must match FK column order
        final Collection<ModelElementFacade> sortedIdentifiers = new ArrayList<ModelElementFacade>();
        String joinOrder = (String)entity.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_JOINCOLUMN_ORDER);
        if (StringUtils.isNotBlank(joinOrder))
        {
            String[] joinList = StringUtils.split(joinOrder, " ,;|");
            for (String column : joinList)
            {
                for (ModelElementFacade facade : identifiers)
                {
                    if (facade instanceof EntityAssociationEnd)
                    {
                        EntityAssociationEnd assoc = (EntityAssociationEnd)facade;
                        if (assoc.getColumnName().equalsIgnoreCase(column))
                        {
                            sortedIdentifiers.add(assoc);
                        }
                    }
                    else if (facade instanceof EntityAttribute)
                    {
                        EntityAttribute attr = (EntityAttribute)facade;
                        if (attr.getColumnName().equalsIgnoreCase(column))
                        {
                            sortedIdentifiers.add(attr);
                        }
                    }
                }
            }
            // Add remaining identifiers not found in joincolumn ordered list
            // .contains() does not work correctly for EntityAttribute
            for (ModelElementFacade facade : identifiers)
            {
                boolean contains = false;
                for (ModelElementFacade sorted : sortedIdentifiers)
                {
                    if (sorted.getFullyQualifiedName().equals(facade.getFullyQualifiedName()))
                    {
                        contains = true;
                    }
                }
                if (!contains)
                {
                    sortedIdentifiers.add(facade);
                }
            }
        }
        else
        {
            sortedIdentifiers.addAll(identifiers);
        }
        if (sortedIdentifiers.isEmpty() && follow && entity.getGeneralization() instanceof Entity)
        {
            sortedIdentifiers.addAll(getIdentifiers((Entity)entity.getGeneralization(), follow));
        }
        return sortedIdentifiers;
    }

    /**
     * Gets all identifier attributes for an entity. If 'follow' is true, and if
     * no identifiers can be found on the entity, a search up the
     * inheritance chain will be performed, and the identifiers from
     * the first super class having them will be used. All identifier association
     * relationships are traversed to find the identifying attributes of the related
     * associationEnd classifiers, in addition to the primitive/wrapped identifiers
     * on the Entity itself.
     *
     * @param entity the entity for which to retrieve the identifiers
     * @param follow a flag indicating whether or not the inheritance hierarchy
     *        should be followed
     * @return the collection of entity identifier attributes.
     */
    public static Collection<ModelElementFacade> getIdentifierAttributes(
        final Entity entity,
        final boolean follow)
    {
        Collection<ModelElementFacade> identifierAttributes = new ArrayList<ModelElementFacade>();
        final Collection<ModelElementFacade> identifiers = EntityMetafacadeUtils.getIdentifiers(entity, follow);
        // Find identifiers of association identifiers otherEnd
        for (ModelElementFacade identifier : identifiers)
        {
            if (identifier instanceof AssociationEndFacade)
            {
                AssociationEndFacade associationEnd = (AssociationEndFacade)identifier;
                ClassifierFacade classifier = associationEnd.getType();
                if (classifier instanceof Entity)
                {
                    Collection<ModelElementFacade> entityIdentifiers = getIdentifierAttributes((Entity)classifier, true);
                    identifierAttributes.addAll(entityIdentifiers);
                }
            }
            else
            {
                identifierAttributes.add(identifier);
            }
        }
        if (identifiers.isEmpty() && follow && entity.getGeneralization() instanceof Entity)
        {
            identifierAttributes.addAll(getIdentifiers((Entity)entity.getGeneralization(), follow));
        }
        // Reorder join columns if order is specified - must match FK column order
        String joinOrder = (String)entity.findTaggedValue("andromda_persistence_joincolumn_order");
        /*
        if (entity.getTaggedValues().size() > 1)
        {
            for ( TaggedValueFacade value : entity.getTaggedValues())
            {
            }
        }*/
        if (StringUtils.isNotBlank(joinOrder))
        {
            final Collection<ModelElementFacade> sortedIdentifiers = new ArrayList<ModelElementFacade>();
            String[] joinList = StringUtils.split(joinOrder, " ,;|");
            for (String column : joinList)
            {
                for (ModelElementFacade facade : identifierAttributes)
                {
                    if (facade instanceof EntityAssociationEnd)
                    {
                        EntityAssociationEnd assoc = (EntityAssociationEnd)facade;
                        if (assoc.getColumnName().equalsIgnoreCase(column))
                        {
                            sortedIdentifiers.add(assoc);
                        }
                    }
                    else if (facade instanceof EntityAttribute)
                    {
                        EntityAttribute attr = (EntityAttribute)facade;
                        if (attr.getColumnName().equalsIgnoreCase(column))
                        {
                            sortedIdentifiers.add(attr);
                        }
                    }
                }
            }
            // Add remaining identifiers not found in joincolumn ordered list
            if (sortedIdentifiers.size() < identifierAttributes.size())
            for (ModelElementFacade facade : identifierAttributes)
            {
                if (!sortedIdentifiers.contains(facade))
                {
                    sortedIdentifiers.add(facade);
                }
            }
            identifierAttributes = sortedIdentifiers;
        }
        else
        {
            identifiers.addAll(identifierAttributes);
        }
        return identifierAttributes;
    }

    /**
     * Constructs a sql type name from the given <code>mappedName</code> and
     * <code>columnLength</code>.
     *
     * @param typeName the actual type name (usually retrieved from a mappings
     *        file, i.e. NUMBER(19).
     * @param columnLength the length of the column.
     * @return the new name construct
     */
    public static String constructSqlTypeName(
        final String typeName,
        final String columnLength)
    {
        String value = typeName;
        if (StringUtils.isNotBlank(typeName))
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
     * Constructs and returns the foreign key constraint name for the given <code>associationEnd</code>, <code>suffix</code>, <code>sqlNameSeparator</code>
     * and <code>maxLengthProperty</code>.
     *
     * @param associationEnd the association end for which to construct the constraint name.
     * @param suffix the suffix appended to the constraint name (if not limited by length).
     * @param sqlNameSeparator the SQL name separator to use (i.e. '_').
     * @param maxLengthProperty the numeric value stored as a string indicating the max length the constraint may be.
     * @param shortenSqlNamesMethod Method of shortening SQL Names, i.e. removeVowels
     * @return the constructed foreign key constraint name.
     */
    public static String getForeignKeyConstraintName(EntityAssociationEnd associationEnd, String suffix, String sqlNameSeparator, String maxLengthProperty, String shortenSqlNamesMethod)
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

            buffer.append(sqlNameSeparator);
            buffer.append(associationEnd.getColumnName());
            constraintName = buffer.toString();

            // we take into consideration the maximum length allowed
            final short maxLength = (short)(Short.valueOf(maxLengthProperty).shortValue() - suffix.length());
            buffer = new StringBuilder(EntityMetafacadeUtils.ensureMaximumNameLength(constraintName, Short.valueOf(maxLength), shortenSqlNamesMethod));
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

    /**
     * Finds the top level package in the model containing any classes or entity classes.
     * Can be used to define a groupId for a model, or in code generation scripts where top level package
     * is the starting point for further operations.
     * @param classifiers Entities in the current model
     * @param entityOnly true to find the top package containing entities
     * @return Package at the top level
     */
    public static PackageFacade getTopLevelPackage(LinkedHashSet<ClassifierFacade> classifiers, boolean entityOnly)
    {
        PackageFacade pkgFacade = null;
        if (classifiers == null || classifiers.isEmpty()) return pkgFacade;
        List<PackageFacade> packages = new ArrayList<PackageFacade>();
        for (ClassifierFacade classifier : classifiers)
        {
            if (!classifier.isDataType())
            {
                if (!entityOnly || classifier instanceof Entity)
                {
                    if (classifier.getStereotypeNames().size() > 0 && !packages.contains(classifier.getPackage()))
                    {
                        packages.add((PackageFacade)classifier.getPackage());
                    }
                }
            }
        }
        if (packages.size()>0)
        {
            pkgFacade = packages.get(0);
            // Find the shortest name in package list containing the other names
            for (PackageFacade pkg : packages)
            {
                if (pkgFacade.getFullyQualifiedName().indexOf(pkg.getFullyQualifiedName()) > 0)
                {
                    pkgFacade = pkg;
                }
                else if (pkg.getFullyQualifiedName().indexOf(pkgFacade.getFullyQualifiedName()) > 0)
                {
                    // Shortest name is there already
                }
                else
                {

                }
            }
        }
        return pkgFacade;
    }

    public String getUniqueGroupString(Collection<String> groupItems) {
        StringBuilder builder = new StringBuilder();

        Iterator<String> it = groupItems.iterator();

        while(it.hasNext()) {
            if(builder.length() > 0) {
                builder.append(", ");
            }
            builder.append('"');
            builder.append(it.next());
            builder.append('"');
        }

        return builder.toString();
    }
}
