package org.andromda.cartridges.hibernate.metafacades;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute
 */
public class HibernateEntityAttributeLogicImpl
    extends HibernateEntityAttributeLogic
{
    // ---------------- constructor -------------------------------

    public HibernateEntityAttributeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#isInheritanceRequired()
     */
    protected boolean handleIsInheritanceRequired()
    {
        HibernateEntity entity=(HibernateEntity)this.getOwner();
        if (entity.isHibernateInheritanceSubclass() && (entity.getGeneralization()!=null))
        {
            return false;
        }
        else
        {
            return this.isRequired();
        }
    }

}
