package org.andromda.metafacades.uml14;

import java.util.Collection;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EntityAssociationFacade.
 *
 * @see org.andromda.metafacades.uml.EntityAssociation
 * @author Bob Fields
 */
public class EntityAssociationLogicImpl
    extends EntityAssociationLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public EntityAssociationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociation#getTableName()
     */
    @Override
    public String handleGetTableName()
    {
        String tableName = null;
        final Collection<AssociationEndFacade> ends = this.getAssociationEnds();
        if (ends != null && !ends.isEmpty())
        {
            final AssociationEndFacade end = (AssociationEndFacade)ends.iterator().next();
            if (end.isMany2Many())
            {
                // prevent ClassCastException if the association isn't an
                // Entity
                if (Entity.class.isAssignableFrom(end.getType().getClass()))
                {
                    final String prefixProperty = UMLMetafacadeProperties.TABLE_NAME_PREFIX;
                    final String tableNamePrefix =
                        this.isConfiguredProperty(prefixProperty)
                        ? ObjectUtils.toString(this.getConfiguredProperty(prefixProperty)) : null;
                    tableName =
                        EntityMetafacadeUtils.getSqlNameFromTaggedValue(
                            tableNamePrefix,
                            this,
                            UMLProfile.TAGGEDVALUE_PERSISTENCE_TABLE,
                            ((Entity)end.getType()).getMaxSqlNameLength(),
                            this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));
                }
            }
        }
        return tableName;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociation#getSchema()
     */
    @Override
    protected String handleGetSchema()
    {
        String schemaName = ObjectUtils.toString(this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_SCHEMA));
        if (StringUtils.isBlank(schemaName))
        {
            schemaName = ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.SCHEMA_NAME));
        }
        return schemaName;
    }

    /**
     *  @see org.andromda.metafacades.uml.EntityAssociation#isEntityAssociation()
     */
    @Override
    protected boolean handleIsEntityAssociation()
    {
        boolean isEntityAssociation = false;
        if (getAssociationEndA().isAssociationEndFacadeMetaType())
        {
            isEntityAssociation = true;
        }
        return isEntityAssociation;
    }
}