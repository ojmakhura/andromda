package org.andromda.cartridges.database.metafacades;

import java.util.Iterator;

import org.andromda.cartridges.database.DatabaseGlobals;
import org.andromda.metafacades.uml.EntityAssociationEndFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.database.metafacades.AssociationTable.
 * 
 * @see org.andromda.cartridges.database.metafacades.AssociationTable
 */
public class AssociationTableLogicImpl
    extends AssociationTableLogic
    implements org.andromda.cartridges.database.metafacades.AssociationTable
{
    // ---------------- constructor -------------------------------

    public AssociationTableLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.AssociationTable#getForeignKeyColumns()
     */
    protected java.util.Collection handleGetForeignKeyColumns()
    {
        return this.getAssociationEnds();
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.AssociationTable#getPrimaryKeyColumns()
     */
    protected String handleGetPrimaryKeyColumns()
    {
        final StringBuffer columns = new StringBuffer();
        for (Iterator endIterator = this.getAssociationEnds().iterator(); endIterator
            .hasNext();)
        {
            Object object = endIterator.next();
            if (EntityAssociationEndFacade.class.isAssignableFrom(object
                .getClass()))
            {
                columns.append(((EntityAssociationEndFacade)object)
                    .getColumnName());
                if (endIterator.hasNext())
                {
                    columns.append(", ");
                }
            }
        }
        return columns.toString();
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.AssociationTable#getPrimaryKeyConstraintName()
     */
    protected String handleGetPrimaryKeyConstraintName()
    {
        StringBuffer buffer = new StringBuffer();
        buffer
            .append(this
                .getConfiguredProperty(DatabaseGlobals.PRIMARY_KEY_CONSTRAINT_PREFIX));
        buffer.append(getTableName());
        return EntityMetafacadeUtils.ensureMaximumNameLength(
            buffer.toString(),
            this.getMaxSqlNameLength());
    }
    
    /**
     * @see org.andromda.cartridges.database.metafacades.AssociationTable#isForeignKeyColumnsPresent()
     */
    protected boolean handleIsForeignKeyColumnsPresent()
    {
        return !this.getForeignKeyColumns().isEmpty();
    }

    /**
     * Gets the maximum name length SQL names may be
     */
    public Short getMaxSqlNameLength()
    {
        return Short
            .valueOf((String)this
                .getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH));
    }

}
