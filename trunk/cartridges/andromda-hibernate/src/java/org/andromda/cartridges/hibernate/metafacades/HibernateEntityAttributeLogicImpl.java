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

    public HibernateEntityAttributeLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide handling of inheritance.
     *
     * @see org.andromda.metafacades.uml.AttributeFacade#isRequired()
     */
    public boolean isRequired()
    {
        boolean required = super.isRequired();
        HibernateEntity entity = (HibernateEntity)this.getOwner();
        if (entity.isHibernateInheritanceClass() && entity.getGeneralization() != null)
        {
            required = false;
        }
        return required;
    }

}