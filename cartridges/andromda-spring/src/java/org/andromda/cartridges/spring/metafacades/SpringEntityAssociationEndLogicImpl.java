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
        System.out.println("the name: " + this.getName());
        SpringEntityAssociationEndLogicImpl otherEnd = (SpringEntityAssociationEndLogicImpl)this
            .getOtherEnd();
        System.out.println("after firstOne2" + one2One);
        boolean otherEndForeignKeyDefined = otherEnd.foreignKeyDefined;
        if (one2One && !otherEndForeignKeyDefined)
        {
            one2One = super.isAggregation() || this.isComposition();
            if (one2One)
            {
                System.out.println("is aggregation!!!: " + this.getName());
            }
        }
        // if the flag is false delegage to the super class
        if (!one2One && !otherEndForeignKeyDefined)
        {
            one2One = super.isOne2One() && !otherEnd.isAggregation()
                && !otherEnd.isComposition();
            System.out.println("setting one-to-one finally!!!!!");
            System.out.println("one2one is: " + one2One);
        }
        return one2One;
    }

}
