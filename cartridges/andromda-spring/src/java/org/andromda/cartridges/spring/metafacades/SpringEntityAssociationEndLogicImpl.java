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
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityAssociationEnd#isOne2OnePrimary()
     */
    public boolean handleIsOne2OnePrimary()
    {
        boolean primaryOne2One = super.isOne2One();
        SpringEntityAssociationEndLogicImpl otherEnd = (SpringEntityAssociationEndLogicImpl)this
            .getOtherEnd();
        if (primaryOne2One)
        {
            primaryOne2One = super.isAggregation() || this.isComposition();
        }
        // if the flag is false delegate to the super class
        if (!primaryOne2One)
        {
            primaryOne2One = super.isOne2One() && !otherEnd.isAggregation()
                && !otherEnd.isComposition();
        }
        return primaryOne2One;
    }

}
