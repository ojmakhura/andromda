package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.ClassifierFacade;

/**
 * <p>
 * Represents an association end of an entity.
 * </p>
 * Metaclass facade implementation.
 */
public class EntityAssociationEndFacadeLogicImpl
    extends EntityAssociationEndFacadeLogic
{
    // ---------------- constructor -------------------------------

    public EntityAssociationEndFacadeLogicImpl(
        java.lang.Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEndFacade#getColumnName()()
     */
    public java.lang.String handleGetColumnName()
    {
        String columnName = null;
        // prevent ClassCastException if the association isn't an EntityFacade
        if (EntityFacade.class.isAssignableFrom(this.getType().getClass()))
        {
            columnName = EntityMetafacadeUtils.getSqlNameFromTaggedValue(
                this,
                UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN,
                ((EntityFacade)this.getType()).getMaxSqlNameLength(),
                this.getForeignKeySuffix(),
                this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));
        }
        return columnName;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEndFacade#getForeignKeySuffix()
     */
    public String handleGetForeignKeySuffix()
    {
        return (String)this.getConfiguredProperty(UMLMetafacadeProperties.FOREIGN_KEY_SUFFIX);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEndFacade#isForeignIdentifier()
     */
    protected boolean handleIsForeignIdentifier()
    {
        Object value = this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_FOREIGN_IDENTIFIER);
        boolean test = value != null && Boolean.valueOf(String.valueOf(value)).booleanValue();
        return test;        
    }

    /**
     * @see AssociationEndFacadeLogic#getForeignKeyConstraintName()
     */
    protected String handleGetForeignKeyConstraintName()
    {
        String constraintName = null;

        final Object taggedValueObject = findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_FOREIGN_KEY_CONSTRAINT_NAME);
        if (taggedValueObject == null)
        {
            // no tagged value, construct: FK_TABLENAME_COLUMNNAME
            final StringBuffer buffer = new StringBuffer();
            buffer.append("FK_");

            ClassifierFacade type = getType();
            if (type instanceof EntityFacade)
            {
                EntityFacade entity = (EntityFacade) type;
                buffer.append(entity.getTableName());
            }
            else
            {
                buffer.append(type.getName().hashCode());
            }

            buffer.append("_");
            buffer.append(this.getColumnName());

            constraintName = buffer.toString();
        }
        else
        {
            // use the tagged value
            constraintName = taggedValueObject.toString();
        }

        return constraintName;
    }

}