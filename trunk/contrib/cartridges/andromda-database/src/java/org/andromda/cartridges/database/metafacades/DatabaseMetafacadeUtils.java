package org.andromda.cartridges.database.metafacades;

import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.ModelElementFacade;

/**
 * Contains utilities for dealing with some common database metafacade tasks.
 * 
 * @author Chad Brandon
 */
public class DatabaseMetafacadeUtils
{

    /**
     * Constructs a SQL identifier (constraint or index) name from the given
     * <code>tableOne</code> and <code>tableTwo</code> model elements.
     * Prefixes each name with the given <code>prefix</code>.
     * 
     * @param prefix the object prefixed to the name
     * @param tableOne the first table
     * @param tableTwo the second table
     * @param maxLength the maximum length the name may be
     * @return the identifier name
     */
    public static String toSqlIdentifierName(
        Object prefix,
        ModelElementFacade tableOne,
        ModelElementFacade tableTwo,
        Short maxLength)
    {
        StringBuffer identifierName = new StringBuffer();
        identifierName.append(prefix);
        identifierName.append(tableOne.getName());
        if (tableTwo != null)
        {
            identifierName.append(tableTwo.getName());
        }
        return EntityMetafacadeUtils.ensureMaximumNameLength(identifierName
            .toString().toUpperCase(), maxLength);
    }

    /**
     * Constructs a SQL identifier (constraint or index) name from the given
     * <code>table</code> model element. Prefixes each name with the given
     * <code>prefix</code>.
     * 
     * @param prefix the object prefixed to the name
     * @param table the table used to construct the name
     * @param maxLength the maximum length the name may be
     * @return the identifier name.
     */
    public static String toSqlIdentifierName(
        Object prefix,
        ModelElementFacade table,
        Short maxLength)
    {
        return toSqlIdentifierName(prefix, table, null, maxLength);
    }
}
