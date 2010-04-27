package org.andromda.cartridges.ejb3.metafacades;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3AssociationFacade.
 *
 * @see EJB3AssociationFacade
 */
public class EJB3AssociationFacadeLogicImpl
    extends EJB3AssociationFacadeLogic
{

    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public EJB3AssociationFacadeLogicImpl(final Object metaObject, final String context)
    {
        super(metaObject, context);
    }

    // --------------- methods ---------------------

    /**
     * Override to provide support for One-2-Many unidirectional associations as well as Many-2-Many.
     *
     * Returns the EJB3 cartridge specific table name for the association
     * @return table name
     */
    @Override
    public String getTableName()
    {
        String tableName = null;
        final List<AssociationEndFacade> ends = this.getAssociationEnds();
        if (ends != null && !ends.isEmpty())
        {
            for (AssociationEndFacade facade : ends)
            {
                final EJB3AssociationEndFacade end = (EJB3AssociationEndFacade)facade;
                if ((end.isMany2Many() && end.isOwning()) ||
                    (end.isOne2Many() && !end.isNavigable() && end.getOtherEnd().isNavigable()))
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
                            EJB3MetafacadeUtils.getSqlNameFromTaggedValue(
                                tableNamePrefix,
                                this,
                                UMLProfile.TAGGEDVALUE_PERSISTENCE_TABLE,
                                ((Entity)end.getType()).getMaxSqlNameLength(),
                                null,
                                this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));
                    }
                    break;
                }
            }
        }

//        if (StringUtils.isNotBlank(tableName) && getName().toLowerCase().startsWith(tableName.toLowerCase()))
//        {
//            tableName = getRelationName().replaceAll("-", "_").toUpperCase();
//        }

        return tableName;
    }

    /**
     * Override the default implementation to use the current getRelationName implementation
     * @return name
     */
    @Override
    public String getName()
    {
        String name = (super.getName().equalsIgnoreCase(super.getRelationName()) ? null : super.getName());

        // if the name isn't defined, use the this implementation of relation name
        if (StringUtils.isEmpty(name))
        {
            name = this.getRelationName();
        }
        return name;
    }

    /**
     * Override the default implementation to set the owning side name first followed by inverse side.
     * If there is no owning side defined, then adopt the default logic of using alphabetical ordering.
     * @return relation name
     */
    @Override
    public String getRelationName()
    {
        final Collection<AssociationEndFacade> ends = this.getAssociationEnds();
        final Iterator endIt = ends.iterator();
        final EJB3AssociationEndFacade firstEnd = (EJB3AssociationEndFacade)endIt.next();
        final EJB3AssociationEndFacade secondEnd = (EJB3AssociationEndFacade)endIt.next();
        final String separator = String.valueOf(
                this.getConfiguredProperty(UMLMetafacadeProperties.RELATION_NAME_SEPARATOR));

        if (secondEnd.isOwning())
        {
            return secondEnd.getName() + separator + firstEnd.getName();
        }
        else if (firstEnd.isOwning())
        {
            return firstEnd.getName() + separator + secondEnd.getName();
        }
        else
        {
            return MetafacadeUtils.toRelationName(
                    firstEnd.getName(),
                    secondEnd.getName(),
                    separator);
        }
    }
}