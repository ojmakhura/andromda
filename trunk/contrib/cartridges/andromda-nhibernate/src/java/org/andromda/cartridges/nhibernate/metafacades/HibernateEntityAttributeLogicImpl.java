package org.andromda.cartridges.nhibernate.metafacades;

import org.andromda.cartridges.nhibernate.HibernateProfile;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.nhibernate.metafacades.HibernateEntityAttribute.
 *
 * @see org.andromda.cartridges.nhibernate.metafacades.HibernateEntityAttribute
 */
public class HibernateEntityAttributeLogicImpl
    extends HibernateEntityAttributeLogic
{
    // ---------------- constructor -------------------------------
    public HibernateEntityAttributeLogicImpl(
        Object metaObject,
        String context)
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
        if (this.getOwner() instanceof HibernateEntity)
        {
            HibernateEntity entity = (HibernateEntity)this.getOwner();
            if (entity.isHibernateInheritanceClass() && entity.getGeneralization() != null)
            {
                required = false;
            }
        }
        return required;
    }

    /**
     * @see org.andromda.cartridges.nhibernate.metafacades.HibernateEntityAttribute#isContainsEmbeddedObject()
     */
    protected boolean handleIsContainsEmbeddedObject()
    {
        boolean returnValue = false;
        if (this.getType() instanceof HibernateEmbeddedValue)
        {
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * Override to provide concatination of the embedded value name with the
     * attribute column name.
     *
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnName() public
     *      String getColumnName() { String columnName = super.getColumnName(); }
     */
    /**
     * @see org.andromda.cartridges.nhibernate.metafacades.HibernateEntityAttribute#concatColumnName(java.lang.String,
     *      java.lang.String)
     */
    protected String handleConcatColumnName(
        java.lang.String prefix,
        java.lang.String name)
    {
        String returnValue = name;
        if (StringUtils.isNotBlank(prefix))
        {
            returnValue = prefix + this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR) + name;

            // handle maxSqlNameLength
            Short maxSqlNameLength =
                Short.valueOf((String)this.getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH));
            returnValue = EntityMetafacadeUtils.ensureMaximumNameLength(returnValue, maxSqlNameLength);
        }
        return returnValue;
    }

    /**
     * @see org.andromda.cartridges.nhibernate.metafacades.HibernateEntityAttribute#getFormula()
     */
    protected String handleGetFormula()
    {
        return (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_FORMULA);
    }
}