package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute.
 * 
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute
 */
public class HibernateEntityAttributeLogicImpl
    extends HibernateEntityAttributeLogic
    implements
    org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute
{
    // ---------------- constructor -------------------------------

    public HibernateEntityAttributeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#isUnique()
     */
    public boolean handleIsUnique()
    {
        return this.hasExactStereotype(UMLProfile.STEREOTYPE_UNIQUE);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#getIndex()
     */
    public java.lang.String handleGetIndex()
    {
        String index = (String)this
            .findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_INDEX);
        return index != null ? StringUtils.trimToEmpty(index) : null;
    }

}