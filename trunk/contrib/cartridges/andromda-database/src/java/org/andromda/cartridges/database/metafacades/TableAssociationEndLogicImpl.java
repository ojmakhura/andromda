package org.andromda.cartridges.database.metafacades;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.EntityAssociationEndFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.database.metafacades.TableAssociationEnd.
 * 
 * @see org.andromda.cartridges.database.metafacades.TableAssociationEnd
 */
public class TableAssociationEndLogicImpl
    extends TableAssociationEndLogic
    implements org.andromda.cartridges.database.metafacades.TableAssociationEnd
{
    // ---------------- constructor -------------------------------

    public TableAssociationEndLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.TableAssociationEnd#getForeignKeyConstraintName()
     */
    public java.lang.String handleGetForeignKeyConstraintName()
    {
        return EntityMetafacadeUtils.ensureMaximumNameLength("FK"
            + this.getForeignKeyConstraintReferencedTableName()
            + this.getForeignKeyConstraintTableName(), this
            .getMaxSqlNameLength());
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.TableAssociationEnd#getForeignKeyConstraintReferencedTableName()
     */
    public java.lang.String handleGetForeignKeyConstraintReferencedTableName()
    {
        AssociationEndFacade connectingEnd = this.getOtherEnd();
        EntityFacade connectingEndType = (EntityFacade)connectingEnd.getType();
        String foreignKeyTable = connectingEndType.getTableName();
        if (connectingEndType.isChild()
            && EntityFacade.class.isAssignableFrom(this.getType().getClass()))
        {
            foreignKeyTable = ((EntityFacade)this.getType()).getTableName();
        }
        return foreignKeyTable;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.TableAssociationEnd#getForeignKeyConstraintTableName()
     */
    public java.lang.String handleGetForeignKeyConstraintTableName()
    {
        EntityFacade sourceEndType = (EntityFacade)this.getType();
        String tableName = sourceEndType.getTableName();
        AssociationEndFacade connectingEnd = this.getOtherEnd();
        EntityFacade connectingEndType = (EntityFacade)connectingEnd.getType();
        if (connectingEndType.isChild())
        {
            tableName = connectingEndType.getTableName();
        }
        return tableName;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.TableAssociationEnd#getForeignKeyConstraintColumnName()
     */
    public java.lang.String handleGetForeignKeyConstraintColumnName()
    {
        String columnName = null;
        // avoid class cast exceptions
        AssociationEndFacade connectingEnd = this.getOtherEnd();
        if (EntityAssociationEndFacade.class.isAssignableFrom(connectingEnd
            .getClass()))
        {
            columnName = ((EntityAssociationEndFacade)connectingEnd)
                .getColumnName();
            EntityFacade connectingEndType = (EntityFacade)connectingEnd
                .getType();
            if (connectingEndType.isChild())
            {
                columnName = this.getColumnName();
            }
        }
        return columnName;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.TableAssociationEnd#isCascadeDelete()
     */
    public boolean handleIsCascadeDelete()
    {
        boolean isCascadeDelete = false;
        isCascadeDelete = this.getOtherEnd().isComposition();
        AssociationEndFacade otherEnd = this.getOtherEnd();
        // check for assignable so we don't get class cast exceptions
        if (otherEnd != null
            && EntityFacade.class.isAssignableFrom(otherEnd.getType()
                .getClass()))
            if (otherEnd.isOne2One()
                && ((EntityFacade)otherEnd.getType()).isChild())
            {
                isCascadeDelete = this.isComposition();
            }
        return isCascadeDelete;
    }

    /**
     * Gets the maximum name length SQL names may be
     */
    protected Short getMaxSqlNameLength()
    {
        return Short.valueOf((String)this.getConfiguredProperty("maxSqlNameLength"));
    }

    protected String handleGetDummyValue(int index)
    {
        String dummyValue = null;

        final String type = getOtherEnd().getType().getFullyQualifiedName(true);

        if ("datatype.String".equals(type))
        {
            dummyValue = '\'' + getName() + '-' + index + '\'';
        }
        else
        {
            dummyValue = String.valueOf(index);
        }

        return dummyValue;
    }
}