package org.andromda.metafacades.uml14;

import java.util.Collection;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.EntityAssociationFacade.
 * 
 * @see org.andromda.metafacades.uml.EntityAssociationFacade
 */
public class EntityAssociationFacadeLogicImpl
    extends EntityAssociationFacadeLogic
    implements org.andromda.metafacades.uml.EntityAssociationFacade
{
    // ---------------- constructor -------------------------------

    public EntityAssociationFacadeLogicImpl(
        java.lang.Object metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationFacade#getTableName()
     */
    public String handleGetTableName()
    {
        String tableName = null;
        Collection ends = this.getAssociationEnds();
        if (ends != null && !ends.isEmpty())
        {
            AssociationEndFacade end = (AssociationEndFacade)ends.iterator()
                .next();
            if (end.isMany2Many())
            {
                // prevent ClassCastException if the association isn't an
                // EntityFacade
                if (EntityFacade.class.isAssignableFrom(end.getType()
                    .getClass()))
                {
                    String tableNamePrefix = StringUtils
                        .trimToEmpty(String
                            .valueOf(this
                                .getConfiguredProperty(UMLMetafacadeGlobals.PROPERTY_TABLE_NAME_PREFIX)));
                    tableName = EntityMetafacadeUtils
                        .getSqlNameFromTaggedValue(
                            tableNamePrefix,
                            this,
                            UMLProfile.TAGGEDVALUE_PERSISTENCE_TABLE,
                            ((EntityFacade)end.getType()).getMaxSqlNameLength());
                }
            }
        }
        return tableName;
    }
}