package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;

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
     * Overridden to provide name masking.
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    protected String handleGetName()
    {
        final String nameMask = String.valueOf(
            this.getConfiguredProperty(UMLMetafacadeProperties.ENTITY_PROPERTY_NAME_MASK));
        return NameMasker.mask(super.handleGetName(), nameMask);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEndFacade#getColumnName()()
     */
    public java.lang.String handleGetColumnName()
    {
        String columnName = null;
        // prevent ClassCastException if the association isn't an EntityFacade
        if (this.getType() instanceof EntityFacade)
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
            // we construct our own foreign key constraint name here
            final StringBuffer buffer = new StringBuffer();

            ClassifierFacade type = getOtherEnd().getType();
            if (type instanceof EntityFacade)
            {
                EntityFacade entity = (EntityFacade) type;
                buffer.append(entity.getTableName());
            }
            else
            {
                // should not happen
                buffer.append(type.getName().toUpperCase());
            }

            buffer.append(getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));
            buffer.append(this.getColumnName());
            buffer.append(getConfiguredProperty(UMLMetafacadeProperties.CONSTRAINT_SUFFIX));

            constraintName = buffer.toString();
        }
        else
        {
            // use the tagged value
            constraintName = taggedValueObject.toString();
        }

        // we take into consideration the maximum length allowed
        String maxLengthString = (String)getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH);
        Short maxLength = Short.valueOf(maxLengthString);

        return EntityMetafacadeUtils.ensureMaximumNameLength(constraintName, maxLength);
    }

}