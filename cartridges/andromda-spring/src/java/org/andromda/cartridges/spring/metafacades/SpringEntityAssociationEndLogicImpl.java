package org.andromda.cartridges.spring.metafacades;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.spring.metafacades.SpringEntityAssociationEnd.
 * 
 * @see org.andromda.cartridges.spring.metafacades.SpringEntityAssociationEnd
 */
public class SpringEntityAssociationEndLogicImpl
    extends SpringEntityAssociationEndLogic
    implements
    org.andromda.cartridges.spring.metafacades.SpringEntityAssociationEnd
{
    // ---------------- constructor -------------------------------

    public SpringEntityAssociationEndLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Flag keeping track of whether or a not a foreign key has been defined, we
     * assume when getColumnName() is accessed the foreign key is defined.
     */
    private boolean foreignKeyDefined = false;

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEndFacade#getColumnName()
     */
    public String getColumnName()
    {
        this.foreignKeyDefined = true;
        return super.getColumnName();
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityAssociationEnd#isOne2OnePrimary()
     */
    public boolean handleIsOne2OnePrimary()
    {
        boolean one2One = super.isOne2One();
        SpringEntityAssociationEndLogicImpl otherEnd = (SpringEntityAssociationEndLogicImpl)this
            .getOtherEnd();
        boolean otherEndForeignKeyDefined = otherEnd.foreignKeyDefined;
        if (one2One && !otherEndForeignKeyDefined)
        {
            one2One = super.isAggregation() || this.isComposition();
        }
        // if the flag is false delegage to the super class
        if (!one2One && !otherEndForeignKeyDefined)
        {
            one2One = super.isOne2One() && !otherEnd.isAggregation()
                && !otherEnd.isComposition();
        }
        return one2One;
    }

}
